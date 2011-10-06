package fi.koku.lok;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.portlet.ActionResponse;
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

import fi.koku.services.utility.log.v1.AuditInfoType;
import fi.koku.services.utility.log.v1.LogEntriesType;
import fi.koku.services.utility.log.v1.LogEntryType;
import fi.koku.services.utility.log.v1.LogQueryCriteriaType;
import fi.koku.services.utility.log.v1.LogServiceFactory;
import fi.koku.services.utility.log.v1.LogServicePortType;
import fi.koku.services.utility.log.v1.ServiceFault;

import fi.koku.lok.*;

/**
 * Controller for viewing log views, for admin. This implements LOK-4 (Tarkista
 * lokin käsittelyloki).
 * 
 * @author makinsu
 */
@Controller
@RequestMapping(value = "VIEW")
public class LogViewController {

  private static final Logger log = LoggerFactory.getLogger(LogViewController.class);

  // Use log service
  private LogServicePortType logService;
  
  private CriteriaSerializer criteriaSerializer = new CriteriaSerializer();
  private SimpleDateFormat dateFormat = new SimpleDateFormat(LogConstants.DATE_FORMAT);

  LogUtils lu = new LogUtils();

  public LogViewController(){
    LogServiceFactory logServiceFactory = new LogServiceFactory(
        LogConstants.LOG_SERVICE_USER_ID, LogConstants.LOG_SERVICE_PASSWORD,
        LogConstants.LOG_SERVICE_ENDPOINT);
    logService = logServiceFactory.getLogService();    
    log.debug("Got logService!");
  }
  // customize form data binding
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    dateFormat.setLenient(false);
    CustomDateEditor dateEditor = new CustomDateEditor(dateFormat, true);
    binder.registerCustomEditor(Date.class, dateEditor);
  }

  // initialize form backing objects
  @ModelAttribute("logSearchCriteria")
  public LogSearchCriteria getCommandObject() {
    return new LogSearchCriteria();
  }

  // portlet render phase
  @RenderMapping(params = "action=viewLog")
  public String render(RenderRequest req, 
      @RequestParam(value = "visited", required = false) String visited, @RequestParam(value = "user") String user,
      @RequestParam(value = "userRole") String userRole,
      @ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria, RenderResponse res, Model model) {
  
    // these are runtime constants, not given by the user!
    String startDateStr = lu.getDateString(1);
    String endDateStr = lu.getDateString(0);
    model.addAttribute("startDate", startDateStr);
    model.addAttribute("endDate", endDateStr);
    
    log.debug("modeliin lisätty startDateStr = " + startDateStr + ", endDateStr = " + endDateStr);
    
    if (criteria != null) {
      if (visited != null) {
      //TODO: tähän kohtaan jokin virheenkäsittely?
        // make the query to the admin log
        model.addAttribute("entries", getAdminLogEntries(criteria, user));
        
        model.addAttribute("searchParams", criteria);
        log.debug("criteria: " + criteria.getFrom() + ", " + criteria.getTo());
        model.addAttribute("visited", "---");
      }
      
      model.addAttribute("logSearchCriteria", criteria);
      
    } else {
      log.debug("criteria: null");
    }

    model.addAttribute("user", user);
    model.addAttribute("userRole", userRole);
    
    return "view";
  }

  // portlet action phase
  @ActionMapping(params = "action=viewLog")
  public void doSearchArchive(@ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria,
      BindingResult result,
      @RequestParam(value = "visited") String visited, 
      @RequestParam(value = "user") String user, 
      @RequestParam(value = "userRole") String userRole, ActionResponse response) {

    if (visited != null) {
      response.setRenderParameter("visited", visited);
    }

    log.debug("action criteria = "+criteria);
 
    // If something goes wrong in serializing the criteria, the portlet must not die
    // and the portlet must not query the log service
    try{
      String[] logSearchCriteria = criteriaSerializer.getAsText(criteria);
      response.setRenderParameter("logSearchCriteria", logSearchCriteria);
    }catch(IllegalArgumentException e){
      log.error("illegal argument");
    }
    //    response.setRenderParameter("logSearchCriteria", criteriaSerializer.getAsText(criteria));
    response.setRenderParameter("user", user);
    response.setRenderParameter("userRole", userRole);
    response.setRenderParameter("action", "viewLog");
  }

  /**
   * Method for reading log entries in the 'log of logs'
   * 
   * @param criteria
   * @return
   */
  private List<AdminLogEntry> getAdminLogEntries(LogSearchCriteria criteria, String user) {
    List<AdminLogEntry> entryList = new ArrayList<AdminLogEntry>();

    try {
     
      LogQueryCriteriaType criteriatype = new LogQueryCriteriaType();

      // the user does not have to give the dates so these might be null
      Calendar start = Calendar.getInstance();
      if(criteria.getFrom() == null){
        start = null;
      } else{
        start.setTime(criteria.getFrom());
      }
      Calendar end = Calendar.getInstance();
      if(criteria.getTo() == null){
        end = null;
      } else{
        end.setTime(criteria.getTo());
      }
      
      // assume that also null arguments are ok!! TODO: ota huomioon
      // kantakyselyissä Vai onko pakolliset?
      criteriatype.setStartTime(start);
      criteriatype.setEndTime(end);
      criteriatype.setLogType(LogConstants.LOG_ADMIN);

      // TODO: ADD HERE WRITING TO LOG
      // "tapahtumatieto = hakuehdot"

      // Set the user information
    
      // call to log database
      AuditInfoType audit = new AuditInfoType();
      audit.setComponent("lok"); //FIXME
      audit.setUserId(user);  

      log.debug("criteriatype start: " + criteriatype.getStartTime() + "\n end: " + criteriatype.getEndTime());
    if(criteriatype.getStartTime() == null || criteriatype.getEndTime() == null){
      log.debug("null-arvoja kriteriassa");
    }else{
      // set the end time 1 day later so that everything added on the last day will be found
/*      Calendar endday = criteriatype.getEndTime();
      endday.set(Calendar.DATE, endday.get(Calendar.DATE) +1);
      
      log.debug("The query end date has been set 1 day later in order to get all results from the given end date");
      criteriatype.setEndTime(endday);
  */
      // call to lok service
      LogEntriesType entriestype = logService.opQueryLog(criteriatype, audit);

      // get the log entries list from the database
      List<LogEntryType> entryTypeList = entriestype.getLogEntry();

      log.debug("entryTypeList size: " + entryTypeList.size());

      for (Iterator<?> i = entryTypeList.iterator(); i.hasNext();) {
        AdminLogEntry logEntry = new AdminLogEntry();
        LogEntryType logEntryType = (LogEntryType) i.next();

        // put values that were read from the database in logEntry for showing
        // them to the user
        logEntry.setTimestamp(logEntryType.getTimestamp().getTime());
        logEntry.setUser(logEntryType.getUserPic());
        logEntry.setOperation(logEntryType.getOperation()); // read, write, ..

        // pic of the child
        logEntry.setCustomer(logEntryType.getCustomerPic());
        // kks.vasu, kks.4v, ..

        // other info about the log entry
        logEntry.setMessage(logEntryType.getMessage());

        entryList.add(logEntry);
      }

   // TODO: Parempi virheenkäsittely
  
    }
    } // TODO: Parempi virheenkäsittely
 catch (ServiceFault e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return entryList;
  }

  

  private static class CriteriaSerializer {

    public String[] getAsText(LogSearchCriteria c) {
      SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
     // log.debug(c.getFrom()+", "+df.format(c.getFrom()));
      String[] text = new String[] { c.getFrom() != null ? df.format(c.getFrom()) : "",
          c.getTo() != null ? df.format(c.getTo()) : "" };
    
      return text;
    }
  }
}