package fi.koku.lok;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.KoKuFaultException;
import fi.koku.calendar.CalendarUtil;
import fi.koku.services.utility.authorizationinfo.util.AuthUtils;
import fi.koku.services.utility.authorizationinfo.v1.AuthorizationInfoService;
import fi.koku.services.utility.authorizationinfo.v1.model.Role;
import fi.koku.services.utility.log.v1.ArchivalResultsType;
import fi.koku.services.utility.log.v1.AuditInfoType;
import fi.koku.services.utility.log.v1.LogArchivalParametersType;
import fi.koku.services.utility.log.v1.LogEntriesType;
import fi.koku.services.utility.log.v1.LogEntryType;
import fi.koku.services.utility.log.v1.LogServicePortType;
import fi.koku.services.utility.log.v1.ServiceFault;

/**
 * Controller for log archiving (LOK). This implements LOK-2.
 * 
 * @author makinsu
 */
@Controller
@RequestMapping(value = "VIEW")
public class LogArchiveController {

  private static final Logger log = LoggerFactory.getLogger(LogArchiveController.class);

  private LogServicePortType logService;

  private AuthorizationInfoService authorizationInfoService;

  private ArchiveSerializer archiveSerializer = new ArchiveSerializer();
  SimpleDateFormat dateFormat = new SimpleDateFormat(LogConstants.DATE_FORMAT);
  LogUtils lu = new LogUtils();

  public LogArchiveController() {
    ServiceFactory f = new ServiceFactory();
    logService = f.getLogservice();
    authorizationInfoService = f.getAuthorizationInfoService();
  }

  // customize form data binding
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    dateFormat.setLenient(false);
    CustomDateEditor dateEditor = new CustomDateEditor(dateFormat, true);
    binder.registerCustomEditor(Date.class, dateEditor);
  }

  // initialize form backing objects
  @ModelAttribute("logArchiveDate")
  public LogArchiveDate getCommandObject() {
    return new LogArchiveDate();
  }

  // portlet render phase
  @RenderMapping(params = "action=archiveLog")
  public String render(PortletSession session, RenderRequest req,
      @ModelAttribute(value = "logArchiveDate") LogArchiveDate logarchivedate,
      @RequestParam(value = "visited", required = false) String visited,
      @RequestParam(value = "error", required = false) String error,
      @RequestParam(value = "change", required = false) String change, RenderResponse res, Model model) {

    // get user pic and role
    String userPic = LogUtils.getPicFromSession(session);

    List<Role> userRoles = authorizationInfoService.getUsersRoles(LogConstants.COMPONENT_LOK, userPic);

    // add a flag for allowing this user to see the operations on page
    // search.jsp
    if (AuthUtils.isOperationAllowed("AdminSystemLogFile", userRoles)) {
      model.addAttribute("allowedToView", true);
    }

    try {

      if (error == null) {

        if (visited != null || change != null) { // page has been visited

          String archiveDateStr = dateFormat.format(logarchivedate.getEndDate());

          // this is needed so that the date can be easily formatted to
          // YYYY-MM-dd format on the jsp
          model.addAttribute("archiveDateDate", logarchivedate.getEndDate());
          model.addAttribute("logArchiveDate", logarchivedate);

          if (change != null) {
            model.addAttribute("visited", null);
          } else {
            model.addAttribute("visited", "---");
          }
          model.addAttribute("endDate", archiveDateStr);

        } else {

          // default is two years ago
          String defaultDateStr = lu.getDateString(2); 
          model.addAttribute("endDate", defaultDateStr);
        
          Calendar time = Calendar.getInstance();
          // default is two years ago
          time.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 2); 
        
          model.addAttribute("archiveDateDate", time.getTime());

        }
      } else { // Some error
        if (logarchivedate != null) {
          model.addAttribute("logArchiveDate", logarchivedate);
        } else {
          // set the default archive date 2 years ago
          String defaultDateStr = lu.getDateString(2); 
          model.addAttribute("endDate", defaultDateStr);
        }
      }
      log.debug("error = " + error);

      model.addAttribute("error", error);

    } catch (KoKuFaultException e) {
      log.error(e.getMessage(), e);
    }
   
    return "archive";
  }

  // portlet action phase
  @ActionMapping(params = "action=archiveLog")
  public void doArchive(@ModelAttribute(value = "logArchiveDate") LogArchiveDate logarchivedate, BindingResult result,
      @RequestParam(value = "visited") String visited, @RequestParam(value = "change", required = false) String change,
      ActionResponse response) {

    log.debug("action archiveLog, visited: " + visited);

    String archivedate = archiveSerializer.getAsText(logarchivedate);
    log.debug("logarchivedate: " + logarchivedate);
    log.debug("archivedate: " + archivedate);

    response.setRenderParameter("visited", visited);

    if (change != null) {
      response.setRenderParameter("change", change);
    }

    if (logarchivedate == null || logarchivedate.getEndDate() == null) {
      response.setRenderParameter("error", "koku.lok.archive.parsing.error");
    }
    // Check that archiving date is earlier than today
    else if (!lu.isBeforeToday(logarchivedate.getEndDate())) {
      response.setRenderParameter("error", "koku.lok.archive.error.wrongDate");
    }
    response.setRenderParameter("endDate", archiveSerializer.getAsText(logarchivedate));
    response.setRenderParameter("action", "archiveLog");
  }

  // portlet render phase
  @RenderMapping(params = "action=startArchiveLog")
  public String renderStart(PortletSession session, RenderRequest req,
      @ModelAttribute(value = "logArchiveDate") LogArchiveDate logarchivedate,
      @RequestParam(value = "error", required = false) String error, RenderResponse res, Model model) {

    // get user pic and role
    String userPic = LogUtils.getPicFromSession(session);

    List<Role> userRoles = authorizationInfoService.getUsersRoles(LogConstants.COMPONENT_LOK, userPic);

    // add a flag for allowing this user to see the operations on page
    // search.jsp
    if (AuthUtils.isOperationAllowed("AdminSystemLogFile", userRoles)) {
      model.addAttribute("allowedToView", true);
    }

    log.debug("startArchiveLog render phase: archiving started");

    if (logarchivedate != null) {
      log.debug("archive end date: " + logarchivedate.getEndDate());
    } else {
      log.debug("action: logarchivedate == null!");
    }

    log.debug("error = " + error);
    if (error != null) {

      model.addAttribute("error", error); // TODO: voisi olla virhekoodi tms.
      log.debug("logarchivedate: " + logarchivedate.getEndDate());

      return "archive";
    }
    log.debug("sivulle archive2");

    return "archive2";
  }

  @ActionMapping(params = "action=startArchiveLog")
  public void startArchive(PortletSession session,
      @ModelAttribute(value = "logArchiveDate") LogArchiveDate logarchivedate, BindingResult result,
      ActionResponse response) {

    log.debug("painettiin nappia Käynnistä arkistointi");
    log.debug("action startArchiveLog");

    log.debug("logarchivedate: " + logarchivedate);
    if (logarchivedate != null) {
      log.debug(logarchivedate.getEndDate().toString());
    }

    // get user pic and role
    String userPic = LogUtils.getPicFromSession(session);

    // call to log database
    AuditInfoType audit = new AuditInfoType();
    audit.setComponent(LogConstants.COMPONENT_LOK);
    audit.setUserId(userPic);
    
    try {
      LogArchivalParametersType archiveParametersType = new LogArchivalParametersType();

      if (logarchivedate != null && logarchivedate.getEndDate() != null) {

        archiveParametersType.setEndDate(CalendarUtil.getXmlDate(logarchivedate.getEndDate()));
       

        log.debug("log archive action phase: starting archiving");

        // call to log archive service
        ArchivalResultsType archiveCount = logService.opArchiveLog(archiveParametersType, audit);

        if (archiveCount.getLogEntryCount() == 0) {
          response.setRenderParameter("error", "koku.lok.archive.nothing.to.archive");
          log.debug("ei arkistoitavaa");
        } else {
          log.debug("arkistoitiin " + archiveCount.getLogEntryCount() + " entrya");
        }
      } else {
        response.setRenderParameter("error", "arkistointipvm puuttuu");
      }

    }
    catch (ServiceFault e) {
      log.error("startArchive fault: " + e.getFaultInfo().getCode());

      // Show the same error to the user no matter what the cause is
      response.setRenderParameter("error", "koku.lok.error.archive");
      log.debug("startArchivessa virhe: " + e.getMessage());
      
      // write to admin log about the error
      log.info("Write to admin log about the error in archiving");
      writeErrorToAdminLog(audit);  
    }
// If archive succeeds but write to admin log does not succeed, transaction is rolled back!
    
    response.setRenderParameter("endDate", archiveSerializer.getAsText(logarchivedate));
    response.setRenderParameter("action", "startArchiveLog");

  }
  
  private void writeErrorToAdminLog(AuditInfoType audit){
    AdminLogEntry adminLogEntry = new AdminLogEntry();
    adminLogEntry.setTimestamp(Calendar.getInstance().getTime());
    adminLogEntry.setUser(audit.getUserId());
    adminLogEntry.setOperation("archive");
    adminLogEntry.setMessage("error in archiving");
    

    LogEntriesType logEntriesType = new LogEntriesType();
    LogEntryType logEntryType = lu.toWsFromAdminType(adminLogEntry);
    
    logEntriesType.getLogEntry().add(logEntryType);
    
    try{
      //write to admin log
     
      logService.opLog(logEntriesType, audit);
    }catch(ServiceFault f){
      log.error("Error writing to admin log.");
      
    }
  }
    
  private static class ArchiveSerializer {

    private SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);

    /**
     * Method formats the given Date into a String. If the input is null or
     * cannot be formatted, the method returns a null value.
     */
    public String getAsText(LogArchiveDate logarchivedate) {
      String date = null;

      if (logarchivedate != null) {
        date = logarchivedate.getEndDate() != null ? df.format(logarchivedate.getEndDate()) : "";
      }

      return date;
    }

    /**
     * Method for parsing the date parameter.
     * 
     * @param text
     * @return
     */
    /*
     * public LogArchiveDate getFromRenderParameter(String text) {
     * 
     * Date d1 = null; try { if (StringUtils.isNotEmpty(text)) { d1 =
     * df.parse(text); } } catch (ParseException e) { throw new
     * IllegalArgumentException("error parsing date string", e); }
     * 
     * return new LogArchiveDate(d1); }
     * 
     * // TODO: tarvitaanko tätä? public LogArchiveDate
     * getFromRenderParameter(String[] text) {
     * 
     * Date d1 = null; try { if (ArrayUtils.isNotEmpty(text) &&
     * StringUtils.isNotEmpty(text[0])) { d1 = df.parse(text[0]); } } catch
     * (ParseException e) { throw new
     * IllegalArgumentException("error parsing date string", e); }
     * 
     * return new LogArchiveDate(d1); } }
     */
  }
}