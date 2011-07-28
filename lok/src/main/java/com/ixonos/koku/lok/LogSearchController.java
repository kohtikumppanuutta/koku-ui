package com.ixonos.koku.lok;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.bind.annotation.RequestParam;

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

//import com.ixonos.koku.kks.utils.Vakiot;

/**
 * Controller for log search (LOK).
 * This implements LOK-3.
 * 
 * @author aspluma
 */
@Controller
@RequestMapping(value = "VIEW")
public class LogSearchController {
  private static final String CRITERIA_RENDER_PARAM = "log-search-criteria";
  private CriteriaSerializer criteriaSerializer = new CriteriaSerializer();
  
  @Autowired
  private ResourceBundleMessageSource resourceBundle;
  
  // customize form data binding
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

  @RenderMapping
  public String render(RenderRequest req, Model model){
    return "menu";
  }
  
  // portlet render phase
  @RenderMapping(params = "op=searchLog")
  public String render(RenderRequest req, RenderResponse res, Model model) {

    System.out.println("log search render phase");
 
    res.setTitle(resourceBundle.getMessage("koku.lok.portlet.title", null, req.getLocale()));

    LogSearchCriteria searchCriteria = null;
    if(req.getParameterValues(CRITERIA_RENDER_PARAM) != null) {
      searchCriteria = criteriaSerializer.getFromRenderParameter(req.getParameterValues(CRITERIA_RENDER_PARAM));
      model.addAttribute("entries", doSearchEntries(searchCriteria));
      model.addAttribute("searchParams", searchCriteria);
    }
    if(searchCriteria!=null){
      System.out.println("criteria: "+searchCriteria.getPic()+", "+searchCriteria.getConcept()+", "+searchCriteria.getFrom()+", "+searchCriteria.getTo());
    } else{
      System.out.println("criteria: null");
    }
    return "search";
  }
  
  // portlet action phase
  @ActionMapping(params="op=searchLog")
  public void doSearch(@ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria, BindingResult result,
      ActionResponse response) {
    System.out.println("log search action phase");
   
    // TODO:
    // Hausta tallentuu tieto tapahtumalokin käsittelylokiin (tapahtumatietona hakuehdot)
    
 // pass criteria to render phase
    response.setRenderParameter(CRITERIA_RENDER_PARAM, criteriaSerializer.getAsText(criteria));
  
    response.setRenderParameter("op", "searchLog");
  }

  private List<LogEntry> doSearchEntries(LogSearchCriteria searchCriteria) {
    List<LogEntry> r = null;
    
    // TEMPORARY solution: 
    // show the results only if something has been written in the pic field
    if(searchCriteria.getPic()!=null && searchCriteria.getPic().length()>0){
     
      /*TODO:
       * time stamp 
       * name of the user
       * type of entry (? tapahtumatyyppi)
       * information that has been viewed or edited (käsitelty tieto tapahtumakuvauksesta ja kohteesta)
       * kutsuva palvelu
       * 
       */
 
      // TEMPORARY SOLUTION:
      // Create 5 lines of demo log entries
      LogDemoFactory factory = new LogDemoFactory();
      r =  new ArrayList<LogEntry>();
      LogEntry entry = null;
      for(int i=0;i<5; i++){
        r.add(factory.createLogEntry(i));
      }
      
    /*
      r =  new ArrayList<LogEntry>();
      r.add(new LogEntry("2011-02-01 17:01:34"));
      r.add(new LogEntry("Utelias Työntekijä"));
      r.add(new LogEntry("Tapahtumatyyppi")); // What does this mean?
      r.add(new LogEntry("4v-tarkastus"));
      r.add(new LogEntry("Järjestelmä Y"));
      */
    }
    return r;
  }

  /**
   * Helper class for serializing and deserializing LogSearchCriteria objects as text.
   * 
   * NB: The class is expecting data to be valid since it's been already validated by the controller so, input errors are not handled. 
   * @author aspluma
   */
  private static class CriteriaSerializer {
    
    public String[] getAsText(LogSearchCriteria c) {
      SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
      String [] text = new String[] {c.getPic(), c.getConcept(),
          c.getFrom() != null ? df.format(c.getFrom()) : "",
          c.getTo() != null ? df.format(c.getTo()) : ""};
      return text;
    }
    
    public LogSearchCriteria getFromRenderParameter(String[] text) {
      SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
      Date d1 = null, d2 = null;
      try {
        d1 = text[2].length() > 0 ? df.parse(text[2]) : null;
        d2 = text[3].length() > 0 ? df.parse(text[3]) : null;
      } catch (ParseException e) {
        throw new IllegalArgumentException("error parsing date string", e);
      }
      return new LogSearchCriteria(text[0], text[1], d1, d2);
    }
  }
  
}
