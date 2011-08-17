package com.ixonos.koku.lok;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

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
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;


/**
 * Controller for viewing log views, for admin.
 * This implements LOK-4.
 * 
 * @author makinsu
 */
@Controller
@RequestMapping(value = "VIEW")
public class LogViewController {
  private static final String CRITERIA_RENDER_PARAM = "log-search-criteria";
  
  private static Logger log = LoggerFactory.getLogger(LogViewController.class);
  
  private CriteriaSerializer criteriaSerializer = new CriteriaSerializer();
  
  @Autowired
  private ResourceBundleMessageSource resourceBundle;
  
//customize form data binding
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(LogConstants.DATE_FORMAT);
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
  @RenderMapping(params="op=viewLog")
  public String render(RenderRequest req, RenderResponse res, Model model) {

    res.setTitle(resourceBundle.getMessage("koku.lok.header.view", null, req.getLocale()));

    LogSearchCriteria searchCriteria = null;
    if(req.getParameterValues(CRITERIA_RENDER_PARAM) != null) {
      searchCriteria = criteriaSerializer.getFromRenderParameter(req.getParameterValues(CRITERIA_RENDER_PARAM));
      model.addAttribute("entries", doSearchEntries(searchCriteria));
      model.addAttribute("searchParams", searchCriteria);
    }
    
    if(searchCriteria!=null){
      log.debug("criteria: "+searchCriteria.getFrom()+", "+searchCriteria.getTo());
    } else{
      log.debug("criteria: null");
    }
    
    return "view";
}

  
//portlet action phase
  @ActionMapping(params="op=viewLog")
  public void doArchive(@ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria, BindingResult result,
      ActionResponse response) {
  
    response.setRenderParameter(CRITERIA_RENDER_PARAM, criteriaSerializer.getAsText(criteria));
    
    response.setRenderParameter("op", "viewLog");
  }
  
  private List<LogEntry> doSearchEntries(LogSearchCriteria searchCriteria) {
    
    if(searchCriteria!=null){
      log.debug("searchCriteria="+searchCriteria.toString());
    }
    
    List<LogEntry> r = null;
    LogDemoFactory factory = new LogDemoFactory();
    r =  new ArrayList<LogEntry>();
  
    for(int i=0;i<5; i++){
      r.add(factory.createLogEntry(i, LogDemoFactory.MANIPULATION_LOG));
    }
    
    
    return r;
    
  }
  
private static class CriteriaSerializer {
  
  public String[] getAsText(LogSearchCriteria c) {
    SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
    String [] text = new String[] {
        c.getFrom() != null ? df.format(c.getFrom()) : "",
        c.getTo() != null ? df.format(c.getTo()) : ""};
    return text;
  }
  
  public LogSearchCriteria getFromRenderParameter(String[] text) {
    SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
    Date d1 = null, d2 = null;
    try {
      d1 = text[0].length() > 0 ? df.parse(text[0]) : null;
      d2 = text[1].length() > 0 ? df.parse(text[1]) : null;
    } catch (ParseException e) {
      throw new IllegalArgumentException("error parsing date string", e);
    }
    return new LogSearchCriteria(d1, d2);
  }
}

}