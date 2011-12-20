/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.lok;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

import fi.koku.calendar.CalendarUtil;
import fi.koku.services.entity.person.v1.PersonService;
import fi.koku.services.utility.authorizationinfo.util.AuthUtils;
import fi.koku.services.utility.authorizationinfo.v1.AuthorizationInfoService;
import fi.koku.services.utility.authorizationinfo.v1.model.Role;
import fi.koku.services.utility.log.v1.AuditInfoType;
import fi.koku.services.utility.log.v1.LogEntriesType;
import fi.koku.services.utility.log.v1.LogEntryType;
import fi.koku.services.utility.log.v1.LogQueryCriteriaType;
import fi.koku.services.utility.log.v1.LogServicePortType;
import fi.koku.services.utility.log.v1.ServiceFault;

/**
 * Controller for viewing log views, for admin. This implements use case LOK-4
 * (Tarkista lokin käsittelyloki).
 * 
 * @author makinsu
 */
@Controller
@RequestMapping(value = "VIEW")
public class LogViewController {

  private static final Logger log = LoggerFactory.getLogger(LogViewController.class);

  // Use log service
  private LogServicePortType logService;
  private PersonService personService;
  private AuthorizationInfoService authorizationInfoService;

  private CriteriaSerializer criteriaSerializer = new CriteriaSerializer();
  private SimpleDateFormat dateFormat = new SimpleDateFormat(LogConstants.DATE_FORMAT);

  private LogUtils lu = new LogUtils();

  public LogViewController() {
    ServiceFactory f = new ServiceFactory();
    logService = f.getLogservice();
    authorizationInfoService = f.getAuthorizationInfoService();
    personService = new PersonService();
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
  public String render(PortletSession session, RenderRequest req,
      @RequestParam(value = "visited", required = false) String visited,
      @ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria,
      RenderResponse res, Model model) {

    // get user pic and role
    String userPic = LogUtils.getPicFromSession(session);

    List<Role> userRoles = authorizationInfoService.getUsersRoles(LogConstants.COMPONENT_LOK, userPic);

    // add a flag for allowing this user to see the operations on page
    // search.jsp
    if (AuthUtils.isOperationAllowed("ViewAdminLogFile", userRoles)) {
      model.addAttribute("allowedToView", true);
    }

    if (visited == null) {
      // these are runtime constants, not given by the user!
      String startDateStr = lu.getDateString(1);
      String endDateStr = lu.getDateString(0);
      model.addAttribute("startDate", startDateStr);
      model.addAttribute("endDate", endDateStr);
    }

    if (criteria != null) {
      if (visited != null) {

        // Check that the input parameters are not null and in the correct
        // format
        String[] errors = lu.checkInputParameters(criteria, LogConstants.LOG_ADMIN);
        model.addAttribute("error0", errors[0]);
        model.addAttribute("error1", errors[1]);
        model.addAttribute("error3", errors[3]);
        
        if (errors[0] == null && errors[1] == null && errors[3] == null) {

          try {
            // make the query to the admin log
            List<AdminLogEntry> entries = getAdminLogEntries(criteria, userPic);
            
            if(entries.size() > LogConstants.QUERY_RESULT_LIMIT){
              model.addAttribute("limit", "koku.lok.query.limit.reached");
            }
            
            // The user's name (not pic as in the database) should be shown,
            // so change pics to names
            lu.changePicsToNamesAdmin(entries, userPic, personService);

            model.addAttribute("entries", entries);

          } catch (ServiceFault fault) { 
            model.addAttribute("error", "koku.lok.error.viewlog");
            log.info("error while searching admin log: "+fault.getFaultInfo().getCode());
          }
          model.addAttribute("searchParams", criteria);        
          model.addAttribute("visited", "---");
          model.addAttribute("logSearchCriteria", criteria);
        } 
      }

    } else {
      log.info("criteria: null");
    }

    return "view";
  }

  // portlet action phase
  @ActionMapping(params = "action=viewLog")
  public void doSearchArchive(@ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria,
      BindingResult result, @RequestParam(value = "visited") String visited, ActionResponse response) {

    if (visited != null) {
      response.setRenderParameter("visited", visited);
    }
    
    // If something goes wrong in serializing the criteria, the portlet must not
    // die and the portlet must not query the log service
    try {
      response.setRenderParameter("logSearchCriteria", criteriaSerializer.getAsText(criteria));
    } catch (IllegalArgumentException e) {
      log.error(e.getMessage());
    }

    response.setRenderParameter("action", "viewLog");
  }

  
  /**
   * Method for reading log entries in the 'log of logs'
   * 
   * @param criteria
   * @return
   */
  private List<AdminLogEntry> getAdminLogEntries(LogSearchCriteria criteria, String userPic) throws ServiceFault {
    List<AdminLogEntry> entryList = new ArrayList<AdminLogEntry>();

    LogQueryCriteriaType criteriatype = new LogQueryCriteriaType();

    // Starttime and endtime cannot be null. Null check has been done earlier.
    criteriatype.setStartTime(CalendarUtil.getXmlDateTime(criteria.getFrom()));
    criteriatype.setEndTime(CalendarUtil.getXmlDateTime(criteria.getTo()));
    criteriatype.setLogType(LogConstants.LOG_ADMIN);

    // Set the user information
    AuditInfoType audit = new AuditInfoType();
    audit.setComponent(LogConstants.COMPONENT_LOK);
    audit.setUserId(userPic);

 
    if (criteriatype.getStartTime() != null && criteriatype.getEndTime() != null) {
    
      // call to lok service
      LogEntriesType entriestype = logService.opQueryLog(criteriatype, audit);

      // set the values 
      List<LogEntryType> entryTypeList = entriestype.getLogEntry();

      for (Iterator<?> i = entryTypeList.iterator(); i.hasNext();) {
        AdminLogEntry logEntry = new AdminLogEntry();
        LogEntryType logEntryType = (LogEntryType) i.next();

        // put values that were read from the database in logEntry for showing
        // them to the user
        logEntry.setTimestamp(CalendarUtil.getDate(logEntryType.getTimestamp()));
        logEntry.setUser(logEntryType.getUserPic());
        logEntry.setOperation(logEntryType.getOperation()); // read, write, ..

        // pic of the child
        logEntry.setCustomer(logEntryType.getCustomerPic());
        // kks.vasu, kks.4v, ..

        // other info about the log entry
        logEntry.setMessage(logEntryType.getMessage());

        entryList.add(logEntry);
      }
    }
  
    return entryList;
  }

  
  private static class CriteriaSerializer {

    public String[] getAsText(LogSearchCriteria c) {
      String[] text = new String[] {};

      SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);

      if (c != null) {
        text = new String[] { c.getFrom() != null ? df.format(c.getFrom()) : "",
            c.getTo() != null ? df.format(c.getTo()) : "" };
      }
      return text;
    }
  }
}