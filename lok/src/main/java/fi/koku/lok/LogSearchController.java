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
import javax.xml.ws.soap.SOAPFaultException;

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

  // Use log service
  private LogServicePortType logService;

  private CriteriaSerializer criteriaSerializer = new CriteriaSerializer();
  SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
  LogUtils lu = new LogUtils();

  public LogSearchController() {
    LogServiceFactory logServiceFactory = new LogServiceFactory(LogConstants.LOG_SERVICE_USER_ID,
        LogConstants.LOG_SERVICE_PASSWORD, LogConstants.LOG_SERVICE_ENDPOINT);
    logService = logServiceFactory.getLogService();
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
  public String render(RenderRequest req, @RequestParam(value = "visited", required = false) String visited,
      @ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria,
      @RequestParam(value = "user") String user, @RequestParam(value = "userRole") String userRole, RenderResponse res,
      Model model) {

    log.info("render searchLog");
    log.debug("render user: " + user);
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
        
      //  log.debug(errors[0]+", "+errors[1]+", "+errors[2]);
        
        // if (LogConstants.REAL_LOG) {
        // TODO: tähän kohtaan jokin virheenkäsittely?

        if(errors[0] ==null && errors[1] == null && errors[2] ==null){
          // get the entries from the database
          model.addAttribute("entries", getLogEntries(criteria, user));
          /*
           * } else { //TODO: poista tämä! model.addAttribute("entries",
           * getDemoLogEntries(criteria)); }
           */
          model.addAttribute("searchParams", criteria);
          model.addAttribute("visited", "---");
        }
      }
      log.info("criteria: " + criteria.getPic() + ", " + criteria.getConcept() + ", " + criteria.getFrom() + ", "
          + criteria.getTo());

      if (StringUtils.isNotBlank(criteria.getPic())) {
        model.addAttribute("logSearchCriteria", criteria);
      }
    }
    
    model.addAttribute("pic", criteria.getPic());
    model.addAttribute("user", user);
    model.addAttribute("userRole", userRole);

    return "search";
  }

  // This is run after the customer has been searched and the user clicks
  // 'Valitse'
  // portlet action phase
  @ActionMapping(params = "action=searchLog")
  public void doSearch(@ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria, BindingResult result,
      @RequestParam(value = "visited") String visited, @RequestParam(value = "user") String user,
      @RequestParam(value = "userRole") String userRole, ActionResponse response) {

    log.debug("user: " + user);

    // pass criteria to render phase
    if (visited != null) {
      response.setRenderParameter("visited", visited);
    }
    response.setRenderParameter("logSearchCriteria", criteriaSerializer.getAsText(criteria));
    response.setRenderParameter("action", "searchLog");
    response.setRenderParameter("user", user);
    response.setRenderParameter("userRole", userRole);
  }

  /**
   * Method for reading log entries
   * 
   * @param searchCriteria
   * @return
   */
  private List<LogEntry> getLogEntries(LogSearchCriteria searchCriteria, String user) {
    List<LogEntry> entryList = new ArrayList<LogEntry>();

    try {
      LogQueryCriteriaType criteriatype = new LogQueryCriteriaType();

      // set the criteria
      criteriatype.setCustomerPic(searchCriteria.getPic());
      log.debug("searchcriteria: from=" + searchCriteria.getFrom() + ", to=" + searchCriteria.getTo());

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

      log.debug("criteriatype cust pic: " + criteriatype.getCustomerPic() + "\n" + "start: "
          + criteriatype.getStartTime() + "\n" + "end: " + criteriatype.getEndTime() + "\n" + "dataItem: "
          + criteriatype.getDataItemType() + "\n" + "logtype: " + criteriatype.getLogType());

      // call to log database
      AuditInfoType audit = new AuditInfoType();
      audit.setComponent("lok"); // FIXME
      log.debug("set user pic: " + user);
      // set pic that was got from the session
      audit.setUserId(user);

      // call to log service
      LogEntriesType entriestype = logService.opQueryLog(criteriatype, audit);

      // the log entries list from the database
      List<LogEntryType> entryTypeList = entriestype.getLogEntry();

      log.debug("entrytype list size: " + entryTypeList.size());

      for (Iterator<?> i = entryTypeList.iterator(); i.hasNext();) {
        LogEntry logEntry = new LogEntry();
        LogEntryType logEntryType = (LogEntryType) i.next();

        log.debug(logEntryType.getTimestamp() + "\n");
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

    } // TODO: Parempi virheenkäsittely
    catch (ServiceFault e) {
      // TODO Auto-generated catch block
      // e.printStackTrace();
      e.getFaultInfo().getCode();
      // }catch(javax.xml.ws.soap.SoapFaultException ee){
    } catch (Exception ee) {
      log.error("jokin virhe servicesta");
    }

    return entryList;
  }

  /*
   * Mock method that creates some random lines of log.
   */
  private List<LogEntry> getDemoLogEntries(LogSearchCriteria searchCriteria) {
    List<LogEntry> r = null;

    log.debug("getDemoLogEntries");

    // TEMPORARY solution:
    // show the results only if something has been written in the pic field
    if (searchCriteria.getPic() != null && searchCriteria.getPic().length() > 0) {

      /*
       * TODO: time stamp name of the user type of entry (? tapahtumatyyppi)
       * information that has been viewed or edited (käsitelty tieto
       * tapahtumakuvauksesta ja kohteesta) kutsuva palvelu
       */

      // TEMPORARY SOLUTION:
      // Create 5 lines of demo log entries
      LogDemoFactory factory = new LogDemoFactory();
      r = new ArrayList<LogEntry>();

      for (int i = 0; i < 5; i++) {
        r.add(factory.createLogEntry(i, LogDemoFactory.BASIC_LOG));
      }

    }
    return r;
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
