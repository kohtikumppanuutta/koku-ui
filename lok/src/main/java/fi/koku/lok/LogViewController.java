package fi.koku.lok;

import java.net.MalformedURLException;
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
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.support.ResourceBundleMessageSource;
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
import fi.koku.services.utility.log.v1.LogServicePortType;
import fi.koku.services.utility.log.v1.ServiceFault;

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

  private CriteriaSerializer criteriaSerializer = new CriteriaSerializer();
  private SimpleDateFormat dateFormat = new SimpleDateFormat(LogConstants.DATE_FORMAT);

  LogUtils lu = new LogUtils();

  @Autowired
  private ResourceBundleMessageSource resourceBundle;

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
  public String render(RenderRequest req, @RequestParam(value = "visited", required = false) String visited,
      @ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria, RenderResponse res, Model model) {

    res.setTitle(resourceBundle.getMessage("koku.lok.header.view", null, req.getLocale()));

    try {

      String startDateStr = lu.getDateString(1); // 1 year ago
      String endDateStr = lu.getDateString(0); // now
      model.addAttribute("startDate", startDateStr);
      model.addAttribute("endDate", endDateStr);
      log.debug("modeliin lisätty startDateStr = " + startDateStr + ", endDateStr = " + endDateStr);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      // TODO: Lisää virheidenkäsittely
    }

    if (criteria != null) {
      if (LogConstants.REAL_ADMIN_LOG) {
        model.addAttribute("entries", getAdminLogEntries(criteria));
      } else {
        model.addAttribute("entries", getDemoAdminLogEntries(criteria));
      }

      model.addAttribute("searchParams", criteria);

      if (visited != null) {
        model.addAttribute("visited", "---");
      }

      log.debug("criteria: " + criteria.getFrom() + ", " + criteria.getTo());
    } else {
      log.debug("criteria: null");
    }

    return "view";
  }

  // portlet action phase
  @ActionMapping(params = "action=viewLog")
  public void doSearchArchive(@ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria,
      @ModelAttribute(value = "visited") String visited, BindingResult result, ActionResponse response) {

    if (visited != null) {
      response.setRenderParameter("visited", visited);
    }

    response.setRenderParameter("logSearchCriteria", criteriaSerializer.getAsText(criteria));
    response.setRenderParameter("action", "viewLog");
  }

  /**
   * Method for reading log entries in the 'log of logs'
   * 
   * @param criteria
   * @return
   */
  private List<LogEntry> getAdminLogEntries(LogSearchCriteria criteria) {
    List<LogEntry> entryList = new ArrayList<LogEntry>();

    try {
      // connect to the log service
      LogServicePortType port = lu.getLogService();

      LogQueryCriteriaType criteriatype = new LogQueryCriteriaType();

      // the user does not have to give the dates so these might be null
      Calendar t1 = Calendar.getInstance();
      t1.setTime(criteria.getFrom());
      Calendar t2 = Calendar.getInstance();
      t2.setTime(criteria.getTo());

      // assume that also null arguments are ok!! TODO: ota huomioon
      // kantakyselyissä
      criteriatype.setStartTime(t1);
      criteriatype.setEndDate(t2);
      criteriatype.setCustomerPic(criteria.getPic());
      criteriatype.setDataItemType(criteria.getConcept());
      criteriatype.setLogType(LogConstants.LOG_ADMIN);

      // TODO: ADD HERE LOGGING OF LOG

      log.debug("criteriatype start: " + criteriatype.getStartTime() + ", end: " + criteriatype.getEndDate());

      // call to log database
      AuditInfoType audit = new AuditInfoType();
      audit.setComponent("lok"); //FIXME
      audit.setUserId("luser");  // FIXME
      LogEntriesType entriestype = port.opQueryLog(criteriatype, audit);

      // get the log entries list from the database
      List<LogEntryType> entryTypeList = entriestype.getLogEntry();

      log.debug("entryTypeList size: " + entryTypeList.size());

      for (Iterator<?> i = entryTypeList.iterator(); i.hasNext();) {
        LogEntry logEntry = new LogEntry();
        LogEntryType logEntryType = (LogEntryType) i.next();

        // put values that were read from the database in logEntry for showing
        // them to the user
        logEntry.setTimestamp(logEntryType.getTimestamp().getTime());
        logEntry.setUser(logEntryType.getUserPic());
        logEntry.setOperation(logEntryType.getOperation()); // read, write, ..

        // "käsitelty tieto": all these together!
        // kks, pyh, kunpo, ..
        logEntry.setClientSystemId(logEntryType.getClientSystemId()); 
        // pic of the child
        logEntry.setChild(logEntryType.getCustomerPic()); 
        // kks.vasu, kks.4v, ..
        logEntry.setDataItemType(logEntryType.getDataItemType()); 
        // id given by the system that wrote the log
        logEntry.setLogId(logEntryType.getDataItemId()); 
        // other info about the log entry 
        logEntry.setMessage(logEntryType.getMessage()); 

        entryList.add(logEntry);
      }

   // TODO: Parempi virheenkäsittely
    } catch (MalformedURLException e) {
      log.error(e.getMessage(), e);
    } catch (ServiceFault e) {
      log.error("service fault", e);
    }

    return entryList;
  }

  /**
   * Method creates random log entries for admin log for demo purposes.
   * 
   * @param searchCriteria
   * @return
   */
  private List<LogEntry> getDemoAdminLogEntries(LogSearchCriteria searchCriteria) {

    if (searchCriteria != null) {
      log.debug("searchCriteria=" + searchCriteria.toString());
    }

    List<LogEntry> r = null;
    LogDemoFactory factory = new LogDemoFactory();
    r = new ArrayList<LogEntry>();

    for (int i = 0; i < 5; i++) {
      r.add(factory.createLogEntry(i, LogDemoFactory.MANIPULATION_LOG));
    }

    return r;

  }

  private static class CriteriaSerializer {

    public String[] getAsText(LogSearchCriteria c) {
      SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
      String[] text = new String[] { c.getFrom() != null ? df.format(c.getFrom()) : "",
          c.getTo() != null ? df.format(c.getTo()) : "" };
      return text;
    }

    public LogSearchCriteria getFromRenderParameter(String[] text) {
      SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
      Date d1 = null, d2 = null;
      try {
        if (ArrayUtils.isNotEmpty(text)) {
          if (StringUtils.isNotBlank(text[0])) {
            d1 = df.parse(text[0]);
          }

          if (StringUtils.isNotBlank(text[1])) {
            d2 = df.parse(text[1]);
          }
        }
      } catch (ParseException e) {
        throw new IllegalArgumentException("error parsing date string", e);
      }
      return new LogSearchCriteria(d1, d2);
    }
  }

}