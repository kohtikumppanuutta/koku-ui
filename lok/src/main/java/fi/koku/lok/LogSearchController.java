package fi.koku.lok;

import java.net.MalformedURLException;
import java.net.URL;
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.ws.BindingProvider;

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

import fi.koku.services.utility.log.v1.LogEntriesType;
import fi.koku.services.utility.log.v1.LogEntryType;
import fi.koku.services.utility.log.v1.LogQueryCriteriaType;
import fi.koku.services.utility.log.v1.LogService;
import fi.koku.services.utility.log.v1.LogServicePortType;

/**
 * Controller for log search (LOK). This implements LOK-3.
 * 
 * @author aspluma
 */
@Controller
@RequestMapping(value = "VIEW")
public class LogSearchController {

  private static final Logger log = LoggerFactory.getLogger(LogSearchController.class);

  private CriteriaSerializer criteriaSerializer = new CriteriaSerializer();
  SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
  LogUtils lu = new LogUtils();
  
  @Autowired
  private ResourceBundleMessageSource resourceBundle;

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

  // start page
  @RenderMapping
  public String render(RenderRequest req, Model model) {
    return "menu";
  }

  /*
   * The "real" LOK service.
   */
  private LogServicePortType getLogService() throws MalformedURLException {
    String uid = LogConstants.LOG_USERNAME;
    String pwd = LogConstants.LOG_PASSWORD;
    

    
    URL wsdlLocation = new URL(LogConstants.LOG_SERVICE);
    QName serviceName = new QName("http://services.koku.fi/utility/log/v1", "logService");
    LogService logService = new LogService(wsdlLocation, serviceName);

    LogServicePortType port = logService.getLogServiceSoap11Port();
    ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, uid);
    ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, pwd);
    
    return port;
  }

  /**
   * Method for reading log entries
   * @param searchCriteria
   * @return
   */
  private List<LogEntry> getLogEntries(LogSearchCriteria searchCriteria) {
    List<LogEntry> entryList = new ArrayList<LogEntry>();
    
    //TODO: onko tarkoituksella startTime ja endDate?
    
    try{
      // connect to the log service
      LogServicePortType port = getLogService();
      
      LogQueryCriteriaType criteriatype = new LogQueryCriteriaType();
     
      
      // the user does not have to give the dates so these might be null
      XMLGregorianCalendar xmlstart = lu.getXMLGregorianDate(searchCriteria.getFrom());
      XMLGregorianCalendar xmlend = lu.getXMLGregorianDate(searchCriteria.getTo());
      
   // set the criteria
      criteriatype.setCustomerPic(searchCriteria.getPic());
      // assume that null arguments are ok
      criteriatype.setStartTime(xmlstart);
      criteriatype.setEndDdate(xmlend);
      
     // data item type: kks.vasu, kks.4v, family/community info, consent, ...
      criteriatype.setDataItemType(searchCriteria.getConcept());
      //log type: loki, lokin seurantaloki
      criteriatype.setLogType(LogConstants.LOG_NORMAL);
  
      //TODO: ADD HERE LOGGING OF LOG 
      
      log.debug("criteriatype cust pic: "+criteriatype.getCustomerPic()+"\n"+
          "start: "+criteriatype.getStartTime()+"\n"+
          "end: "+criteriatype.getEndDdate()+"\n"+
          "dataItem: "+criteriatype.getDataItemType()+"\n"+
          "logtype: "+criteriatype.getLogType());
      
      // call to log database
      LogEntriesType entriestype = port.opQueryLog(criteriatype);
    
      // the log entries list from the database  
      List<LogEntryType> entryTypeList = entriestype.getLogEntry();
  
     log.debug("entrytypelist size: "+entryTypeList.size());
     
      for(Iterator<?> i = entryTypeList.iterator(); i.hasNext(); ) {
        LogEntry logEntry = new LogEntry();  
        LogEntryType logEntryType = (LogEntryType)i.next();
        
        // put values that were read from the database in logEntry for showing them to the user 
        logEntry.setCallingSystem(logEntryType.getClientSystemId());
        logEntry.setChild(logEntryType.getCustomerPic());
        logEntry.setEventDescription(logEntryType.getOperation());
        logEntry.setEventType(logEntryType.getDataItemType());
        logEntry.setLogId(logEntryType.getDataItemId());
        logEntry.setMessage(logEntryType.getMessage());
        logEntry.setTimestamp(lu.getDate(logEntryType.getTimestamp()));
        logEntry.setUser(logEntryType.getUserPic());
        
        entryList.add(logEntry);
        
      }

    }catch(MalformedURLException e){
      log.error(e.getMessage(),e);
    }catch(DatatypeConfigurationException ex){
      log.error(ex.getMessage(), ex);
    } //TODO: Parempi virheenkäsittely
    
    
    return entryList;
  }
  
    
  // portlet render phase
  @RenderMapping(params = "action=searchLog")
  public String render(RenderRequest req, @RequestParam(value = "visited", required = false) String visited,
      @ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria, RenderResponse res, Model model) {

    log.info("action = searchLog");

    res.setTitle(resourceBundle.getMessage("koku.lok.portlet.title", null, req.getLocale()));

    try{
      
      String startDateStr = lu.getDateString(1);
      String endDateStr = lu.getDateString(0);
      model.addAttribute("startDate", startDateStr);
      model.addAttribute("endDate", endDateStr);
      log.debug("modeliin lisätty startDateStr = " + startDateStr + ", endDateStr = " + endDateStr);
    }catch(Exception e){
      log.error(e.getMessage(), e);
      //TODO: Lisää virheidenkäsittely
    }
    
 
    if (criteria != null) {
      if(LogConstants.REAL_LOG){
        model.addAttribute("entries", getLogEntries(criteria));
      } else{
        model.addAttribute("entries", getDemoLogEntries(criteria));
      }
      model.addAttribute("searchParams", criteria);
      if (visited != null) {
        model.addAttribute("visited", "---");
      }
      log.info("criteria: " + criteria.getPic() + ", " + criteria.getConcept() + ", " + criteria.getFrom() + ", "
          + criteria.getTo());

      // TODO: ADD HERE THE ACTUAL CALL TO LOG SERVICE

      if (StringUtils.isNotBlank(criteria.getPic())) {
        model.addAttribute("logSearchCriteria", criteria);
      }
    }

    // TODO: SOMEWHERE HERE SHOULD BE ADDED A PIECE OF CODE THAT WRITES LOG ABOUT
    // LOG SEARCHES

    return "search";
  }

 
    
  // portlet action phase
  @ActionMapping(params = "action=searchLog")
  public void doSearch(@ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria,
      @ModelAttribute(value = "visited") String visited, BindingResult result, ActionResponse response) {

    log.debug("action = searchLog");

    // TODO:
    // Hausta tallentuu tieto tapahtumalokin käsittelylokiin (tapahtumatietona
    // hakuehdot)

    // pass criteria to render phase
    if (visited != null) {
      response.setRenderParameter("visited", visited);
    }
    response.setRenderParameter("logSearchCriteria", criteriaSerializer.getAsText(criteria));
    response.setRenderParameter("action", "searchLog");
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
