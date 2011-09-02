package fi.koku.lok;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

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

/**
 * Controller for viewing log views, for admin. This implements LOK-4.
 * 
 * @author makinsu
 */
@Controller
@RequestMapping(value = "VIEW")
public class LogViewController {
  private static final String CRITERIA_RENDER_PARAM = "log-search-criteria";

  private static final Logger log = LoggerFactory.getLogger(LogViewController.class);

  private CriteriaSerializer criteriaSerializer = new CriteriaSerializer();
  private SimpleDateFormat dateFormat = new SimpleDateFormat(LogConstants.DATE_FORMAT);
  
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
  public String render(RenderRequest req,  @RequestParam(value = "visited", required = false) String visited,
      @ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria, 
      RenderResponse res, Model model) {

    res.setTitle(resourceBundle.getMessage("koku.lok.header.view", null, req.getLocale()));

    // default endtime is now
    Calendar endtime = Calendar.getInstance();
    // default starttime is 1 year ago
    Calendar starttime = Calendar.getInstance();
    starttime.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1);

    String startDateStr = dateFormat.format(starttime.getTime());
    model.addAttribute("startDate", startDateStr);
    String endDateStr = dateFormat.format(endtime.getTime());
    model.addAttribute("endDate", endDateStr);

    log.debug("modeliin lisätty startDateStr = " + startDateStr + ", endDateStr = " + endDateStr);
    
   
    if (criteria != null) {
      model.addAttribute("entries", doSearchEntries(criteria));
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
      @ModelAttribute(value="visited") String visited, BindingResult result,
      ActionResponse response) {

    if(visited != null){
      response.setRenderParameter("visited", visited);
    }
    
    response.setRenderParameter("logSearchCriteria", criteriaSerializer.getAsText(criteria));
    response.setRenderParameter("action", "viewLog");
  }

  private List<LogEntry> doSearchEntries(LogSearchCriteria searchCriteria) {

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
        if(ArrayUtils.isNotEmpty(text)){
          if(StringUtils.isNotBlank(text[0])){
            d1 = df.parse(text[0]);
          }
          
          if(StringUtils.isNotBlank(text[1])){
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