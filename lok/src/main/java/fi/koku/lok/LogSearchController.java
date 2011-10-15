package fi.koku.lok;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
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

import fi.koku.services.entity.authorizationinfo.util.AuthUtils;
import fi.koku.services.entity.authorizationinfo.v1.AuthorizationInfoService;
import fi.koku.services.entity.authorizationinfo.v1.impl.AuthorizationInfoServiceDummyImpl;
import fi.koku.services.entity.authorizationinfo.v1.model.Role;
import fi.koku.services.entity.person.v1.PersonService;
import fi.koku.services.utility.log.v1.AuditInfoType;
import fi.koku.services.utility.log.v1.LogEntriesType;
import fi.koku.services.utility.log.v1.LogEntryType;
import fi.koku.services.utility.log.v1.LogQueryCriteriaType;
import fi.koku.services.utility.log.v1.LogServiceFactory;
import fi.koku.services.utility.log.v1.LogServicePortType;
import fi.koku.services.utility.log.v1.ServiceFault;

/**
 * Controller for log search (LOK). This implements LOK-3 (Etsi lokitieto).
 * 
 * @author aspluma
 * @author makinsu
 */
@Controller
@RequestMapping(value = "VIEW")
public class LogSearchController {

  private static final Logger log = LoggerFactory.getLogger(LogSearchController.class);

  private AuthorizationInfoService authorizationInfoService;
  private PersonService personService;
  
  // Use log service
  private LogServicePortType logService;

  private CriteriaSerializer criteriaSerializer = new CriteriaSerializer();
  SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
  LogUtils lu = new LogUtils();

  public LogSearchController() {
    LogServiceFactory logServiceFactory = new LogServiceFactory(
        LogConstants.LOG_SERVICE_USER_ID,
        LogConstants.LOG_SERVICE_PASSWORD, 
        LogConstants.LOG_SERVICE_ENDPOINT);
    logService = logServiceFactory.getLogService();
    
    authorizationInfoService = new AuthorizationInfoServiceDummyImpl();
    personService = new PersonService();
  }

  // customize form data binding
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    df.setLenient(false);
    CustomDateEditor dateEditor = new CustomDateEditor(df, true);
    binder.registerCustomEditor(Date.class, dateEditor);
  }

  // initialize form backing objects
  @ModelAttribute("logSearchCriteria")
  public LogSearchCriteria getCommandObject() {
    return new LogSearchCriteria();
  }

  // portlet render phase
  @RenderMapping(params = "action=searchLog")
  public String render(PortletSession session, RenderRequest req, @RequestParam(value = "visited", required = false) String visited,
      @ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria, RenderResponse res, Model model) {

    // get user pic and role
    String userPic = LogUtils.getPicFromSession(session);
      
    List<Role> userRoles = authorizationInfoService.getUsersRoles(LogConstants.COMPONENT_LOK, userPic);
    
    // add a flag for allowing this user to see the operations on page search.jsp 
    if (AuthUtils.isOperationAllowed("AdminSystemLogFile", userRoles)) {
      model.addAttribute("allowedToView", true);
    }
    
    // these are runtime constants, not given by the user!
    String startDateStr = lu.getDateString(1);
    String endDateStr = lu.getDateString(0);
    model.addAttribute("startDate", startDateStr);
    model.addAttribute("endDate", endDateStr);
    log.debug("startDateStr = " + startDateStr + ", endDateStr = " + endDateStr);

    if (criteria != null) {
      if (visited != null) { // page has been visited
        
        // Check that the input parameters are not null and in the correct format
        String[] errors = lu.checkInputParameters(criteria, LogConstants.LOG_NORMAL);
        model.addAttribute("error0", errors[0]);
        model.addAttribute("error1", errors[1]);
        model.addAttribute("error2", errors[2]);
 
        if(errors[0] == null && errors[1] == null && errors[2] == null){
          
          try{
            // get the entries from the database
            List<LogEntry> entries = getLogEntries(criteria, userPic);
            
            // The user's name (not pic as in the database) should be shown, 
            // so change pics to names
            lu.changePicsToNames(entries, userPic, personService);
            
            model.addAttribute("entries", entries);
         
          }catch(ServiceFault fault){
            model.addAttribute("error", "koku.lok.error.log");
          }catch(Exception e){
            log.debug("Other exception: "+e.getMessage());
          }
          
          model.addAttribute("searchParams", criteria);
          model.addAttribute("visited", "---");
        }
      }

      if (StringUtils.isNotBlank(criteria.getPic())) {
        model.addAttribute("logSearchCriteria", criteria);
      }
    }
    
    model.addAttribute("pic", criteria.getPic());

    return "search";
  }

  // This is run after the customer has been searched and clicked
  // portlet action phase
  @ActionMapping(params = "action=searchLog")
  public void doSearch(@ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria, BindingResult result,
      @RequestParam(value = "visited") String visited, ActionResponse response) {   

    // pass criteria to render phase
    if (visited != null) {
      response.setRenderParameter("visited", visited);
    }
    response.setRenderParameter("logSearchCriteria", criteriaSerializer.getAsText(criteria));
    response.setRenderParameter("action", "searchLog");
  }

  /**
   * Method for reading log entries
   * 
   * @param searchCriteria
   * @return
   */
  private List<LogEntry> getLogEntries(LogSearchCriteria searchCriteria, String userPic) throws ServiceFault{
    List<LogEntry> entryList = new ArrayList<LogEntry>();

  //  try {
      LogQueryCriteriaType criteriatype = new LogQueryCriteriaType();

      // set the criteria
      criteriatype.setCustomerPic(searchCriteria.getPic());
   
      // The from and to fields are not allowed to be null
      Calendar start = lu.dateToCalendar(searchCriteria.getFrom());
      Calendar end = lu.dateToCalendar(searchCriteria.getTo());

      log.debug("parsitut päivämäärät: " + start + "\n" + end + "\n");
      // these have been null-checked earlier
      criteriatype.setStartTime(start);
      criteriatype.setEndTime(end);

      // data item type: kks.vasu, kks.4v, family/community info, consent, ...
      criteriatype.setDataItemType(searchCriteria.getConcept());
      // log type: loki, lokin seurantaloki
      criteriatype.setLogType(LogConstants.LOG_NORMAL);

      // call to log database
      AuditInfoType audit = new AuditInfoType();
      audit.setComponent(LogConstants.COMPONENT_LOK); 
      audit.setUserId(userPic); // pic from the session    
      
      // call to log service
      LogEntriesType entriestype = logService.opQueryLog(criteriatype, audit);

    
      // the log entries list from the database
      List<LogEntryType> entryTypeList = entriestype.getLogEntry();

      log.debug("entrytype list size: " + entryTypeList.size());

      for (Iterator<?> i = entryTypeList.iterator(); i.hasNext();) {
        LogEntry logEntry = new LogEntry();
        LogEntryType logEntryType = (LogEntryType) i.next();

        // put values that were read from the database in logEntry for showing
        // them to the user

        // kks, pyh, kunpo, ..
        logEntry.setClientSystemId(logEntryType.getClientSystemId());
        // pic of the child
        logEntry.setChild(logEntryType.getCustomerPic());
        // kks.vasu, kks.4v, ..
        logEntry.setDataItemType(logEntryType.getDataItemType());
        // read, write, ..
        logEntry.setOperation(logEntryType.getOperation());
        // id given by the system that wrote the log
        logEntry.setLogId(logEntryType.getDataItemId());
        // other info about the log entry
        logEntry.setMessage(logEntryType.getMessage());
        logEntry.setTimestamp(logEntryType.getTimestamp().getTime());
        logEntry.setUser(logEntryType.getUserPic());

        entryList.add(logEntry);
      }

  
    return entryList;
  }

  
 
  /**
   * Helper class for serializing and deserializing LogSearchCriteria objects as
   * text.
   * 
   * NB: The class is expecting data to be valid since it's been already
   * validated by the controller so, input errors are not handled.
   * 
   * @author aspluma
   */
  private static class CriteriaSerializer {

    public String[] getAsText(LogSearchCriteria c) {
      String[] text = new String[] {};

      SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);

      log.debug("getAsText from: " + c.getFrom());
      if (c != null) {
        text = new String[] { c.getPic(), c.getConcept(), c.getFrom() != null ? df.format(c.getFrom()) : "",
            c.getTo() != null ? df.format(c.getTo()) : "" };
      }
      return text;
    }

    // TODO: Is pic or some other parameter required for criteria?? Add error
    // handling!
    public LogSearchCriteria getFromRenderParameter(String[] text) {
      SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
      String pic = null, concept = null;
      Date d1 = null, d2 = null;
      try {
        if (ArrayUtils.isNotEmpty(text)) {
          if (StringUtils.isNotBlank(text[0])) {
            pic = text[0]; // TODO: Add here some validation!!!
          }

          if (StringUtils.isNotBlank(text[1])) {
            concept = text[1]; // TODO: What kind of input is accepted here?
          }

          if (StringUtils.isNotBlank(text[2])) {
            d1 = df.parse(text[2]);
          }

          if (StringUtils.isNotBlank(text[3])) {
            d2 = df.parse(text[3]);
          }
        }

      } catch (ParseException e) {
        throw new IllegalArgumentException("error parsing date string", e);
      }

      return new LogSearchCriteria(pic, concept, d1, d2);
    }
  }

}
