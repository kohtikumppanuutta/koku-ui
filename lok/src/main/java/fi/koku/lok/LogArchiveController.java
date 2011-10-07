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
import fi.koku.services.entity.authorizationinfo.util.AuthUtils;
import fi.koku.services.entity.authorizationinfo.v1.AuthorizationInfoService;
import fi.koku.services.entity.authorizationinfo.v1.impl.AuthorizationInfoServiceDummyImpl;
import fi.koku.services.entity.authorizationinfo.v1.model.Role;
import fi.koku.services.utility.log.v1.ArchivalResultsType;
import fi.koku.services.utility.log.v1.AuditInfoType;
import fi.koku.services.utility.log.v1.LogArchivalParametersType;
import fi.koku.services.utility.log.v1.LogServiceFactory;
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

// Use log service
  private LogServicePortType logService;
  
  private AuthorizationInfoService authorizationInfoService;
  
  private ArchiveSerializer archiveSerializer = new ArchiveSerializer();
  SimpleDateFormat dateFormat = new SimpleDateFormat(LogConstants.DATE_FORMAT);
  LogUtils lu = new LogUtils();
  
  public LogArchiveController(){
    LogServiceFactory logServiceFactory = new LogServiceFactory(
        LogConstants.LOG_SERVICE_USER_ID, LogConstants.LOG_SERVICE_PASSWORD,
        LogConstants.LOG_SERVICE_ENDPOINT);
    logService = logServiceFactory.getLogService();  
    
    authorizationInfoService = new AuthorizationInfoServiceDummyImpl();
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
  public String render(PortletSession session, RenderRequest req, @ModelAttribute(value = "logArchiveDate") LogArchiveDate logarchivedate,
      @RequestParam(value = "visited", required = false) String visited, 
      @RequestParam(value = "error", required = false) String error,
      @RequestParam(value = "change", required = false) String change,  
      RenderResponse res, Model model) {
    
    log.debug("render archiveLog: visited= "+visited);
    log.debug("render archiveLog: change= "+change);
 
    // get user pic and role
    String userPic = LogUtils.getPicFromSession(session);
      
    List<Role> userRoles = authorizationInfoService.getUsersRoles("lok", userPic);
    
    log.debug("render searchLog");
    // add a flag for allowing this user to see the operations on page search.jsp 
    if (AuthUtils.isOperationAllowed("AdminSystemLogFile", userRoles)) {
      log.debug("lisätään allowedToView");
      model.addAttribute("allowedToView", true);
    }
    
    if(change != null){
      log.debug("Painettiin nappia Vaihda päivämäärää");
    }
    try{
      
      //TODO: tähän tulee: jos !error
      if(error==null){
        log.debug("logarchivedate: "+logarchivedate.getEndDate());

        if (visited != null || change != null ) { // page has been visited

          log.debug("visited tai change");

          String archiveDateStr = dateFormat.format(logarchivedate.getEndDate());

          // this is needed so that the date can be easily formatted to YYYY-MM-dd format on the jsp
          model.addAttribute("archiveDateDate", logarchivedate.getEndDate());
          model.addAttribute("logArchiveDate", logarchivedate);

          if(change!=null){
            model.addAttribute("visited", null);
          }else{
            model.addAttribute("visited", "---");
          }
          model.addAttribute("endDate", archiveDateStr);  
          log.debug("modeliin lisätty endDate= " + archiveDateStr);

        } else{
          log.debug("visited == null");

          String defaultDateStr = lu.getDateString(2); // default is two years ago TODO: make static
          model.addAttribute("endDate", defaultDateStr);  
          log.debug("modeliin lisätty endDate = " + defaultDateStr);
          model.addAttribute("endDate", defaultDateStr);

          Calendar time = Calendar.getInstance();
          time.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 2); // default is  two years ago
          //TODO: tai ei tehdä parsintaa webbisivulla vaan tässä??
          model.addAttribute("archiveDateDate", time.getTime());

          log.debug("modeliin lisätty archiveDate=" + defaultDateStr);

        }
      }else{ //Jos on error
        log.debug("logarchivedate == null");
        // set the default archive date
        String defaultDateStr = lu.getDateString(2); // default is two years ago TODO: make static
        model.addAttribute("endDate", defaultDateStr);  
       
        log.debug("modeliin lisätty endDate = " + defaultDateStr);
      }
      log.debug("error = "+error);

      if(error != null ){
        model.addAttribute("error", error); 
      }

    }catch(KoKuFaultException e){
      log.error(e.getMessage(), e);
      //TODO: Lisää virheidenkäsittely
      // käyttäjälle näytetään virheviesti "koku.lok.archive.parsing.error"

    }
  
    return "archive";
  }


  // portlet action phase
  @ActionMapping(params = "action=archiveLog")
  public void doArchive(@ModelAttribute(value = "logArchiveDate") LogArchiveDate logarchivedate, 
      BindingResult result,
      @RequestParam(value = "visited") String visited, 
      @RequestParam(value = "change", required = false) String change, 
      ActionResponse response) {

    log.debug("action archiveLog, visited: "+visited);

    String archivedate = archiveSerializer.getAsText(logarchivedate);
    log.debug("logarchivedate: "+logarchivedate);
    log.debug("archivedate: "+archivedate);
    
    if(archivedate == null){
      log.debug("archivedate on null!");
      //TODO: PITÄÄ LISÄTÄ KÄYTTÄJÄLLE ILMOITUS RUUDULLE VÄÄRÄSTÄ SYÖTTEESTÄ!
    } else{
      log.debug("saatiin jsp-sivulta archive end date: " + logarchivedate.getEndDate());
    }
   
    response.setRenderParameter("visited", visited); 
    
    if(change!=null){
      response.setRenderParameter("change", change);
    }
  
    if(logarchivedate==null || logarchivedate.getEndDate()==null){   
      response.setRenderParameter("error", "koku.lok.archive.parsing.error");
    }
    // Check that archiving date is earlier than today
    else if(!lu.isBeforeToday(logarchivedate.getEndDate())){
      response.setRenderParameter("error", "koku.lok.archive.error.wrongDate");
    }
    response.setRenderParameter("endDate", archiveSerializer.getAsText(logarchivedate));
    response.setRenderParameter("action", "archiveLog");
  }

//portlet render phase
  @RenderMapping(params = "action=startArchiveLog")
  public String renderStart(PortletSession session, RenderRequest req, 
      @ModelAttribute(value = "logArchiveDate") LogArchiveDate logarchivedate,
      @RequestParam(value = "error", required = false) String error,
      RenderResponse res, Model model) {

    // get user pic and role
    String userPic = LogUtils.getPicFromSession(session);
      
    List<Role> userRoles = authorizationInfoService.getUsersRoles("lok", userPic);
    
  
    // add a flag for allowing this user to see the operations on page search.jsp 
    if (AuthUtils.isOperationAllowed("AdminSystemLogFile", userRoles)) {
      log.debug("lisätään allowedToView");
      model.addAttribute("allowedToView", true);
    }
    
    log.debug("startArchiveLog render phase: archiving started");
    
    if (logarchivedate != null) {
      log.debug("archive end date: " + logarchivedate.getEndDate());
    } else{
      log.debug("action: logarchivedate == null!");
    }
    
    log.debug("error = "+error);
    if(error != null){  
     
      model.addAttribute("error", error); // TODO: voisi olla virhekoodi tms.
      log.debug("logarchivedate: "+logarchivedate.getEndDate());
      log.debug("sivulle archive");
      
      return "archive";
    }
    log.debug("sivulle archive2");
    
    return "archive2";
  }

  @ActionMapping(params = "action=startArchiveLog")
  public void startArchive(PortletSession session, @ModelAttribute(value = "logArchiveDate") LogArchiveDate logarchivedate,
      BindingResult result, 
      ActionResponse response) {
    
    log.debug("painettiin nappia Käynnistä arkistointi");
    log.debug("action startArchiveLog");
    
    log.debug("logarchivedate: "+logarchivedate);
    if(logarchivedate != null) {
      log.debug(logarchivedate.getEndDate().toString());
    }
	
    try{
      LogArchivalParametersType archiveParametersType = new LogArchivalParametersType();
      
      if(logarchivedate != null && logarchivedate.getEndDate() != null){
        
        archiveParametersType.setEndDate(lu.dateToCalendar(logarchivedate.getEndDate()));

        // get user pic and role
        String userPic = LogUtils.getPicFromSession(session);
        
        // call to log database
        AuditInfoType audit = new AuditInfoType();
        audit.setComponent("lok"); //FIXME Voi olla demossa näin!
        audit.setUserId(userPic);  // FIXME

        log.debug("log archive action phase: starting archiving");

        // call to log archive service
        ArchivalResultsType archiveCount = logService.opArchiveLog(archiveParametersType, audit);
        
        if(archiveCount.getLogEntryCount() == 0){
          response.setRenderParameter("error", "koku.lok.archive.nothing.to.archive"); 
          log.debug("ei arkistoitavaa");
        }else{
          log.debug("arkistoitiin "+archiveCount.getLogEntryCount()+ " entrya");
        }
      }else{
        response.setRenderParameter("error", "arkistointipvm puuttuu");
      }
      
    }// TODO: lisää tähän catch sitä varten, että tulee virheet 2.1 tai 2.2
 catch (ServiceFault e) {
   log.debug("fault: "+e.getFaultInfo().getCode());
 
 // if(e.getFaultInfo().getCode()==LogConstants.LOG_UNKNOWN_ERROR){
     response.setRenderParameter("error", "koku.lok.archive.error.unknown"); //TODO: mikä olisi tämä yleinen virhe???
     log.debug("tuntematon virhe startArchivessa");
  // }
   
 }
 
   response.setRenderParameter("endDate", archiveSerializer.getAsText(logarchivedate));
   response.setRenderParameter("action", "startArchiveLog");

  }

  
  private static class ArchiveSerializer {

    private SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);

    /**
     * Method formats the given Date into a String.
     * If the input is null or cannot be formatted, the method returns a null value.
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
     * @param text
     * @return
     */
   /*
    public LogArchiveDate getFromRenderParameter(String text) {

      Date d1 = null;
      try {
        if (StringUtils.isNotEmpty(text)) {
          d1 = df.parse(text);
        }
      } catch (ParseException e) {
        throw new IllegalArgumentException("error parsing date string", e);
      }

      return new LogArchiveDate(d1);
    }

    // TODO: tarvitaanko tätä?
    public LogArchiveDate getFromRenderParameter(String[] text) {

      Date d1 = null;
      try {
        if (ArrayUtils.isNotEmpty(text) && StringUtils.isNotEmpty(text[0])) {
          d1 = df.parse(text[0]);
        }
      } catch (ParseException e) {
        throw new IllegalArgumentException("error parsing date string", e);
      }

      return new LogArchiveDate(d1);
    }
  }
  */
}
}