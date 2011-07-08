package fi.koku.lok;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;

import org.springframework.beans.propertyeditors.CustomDateEditor;
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
 * Controller for log search (LOK).
 * 
 * @author aspluma
 */
@Controller
@RequestMapping(value = "VIEW")
public class LogSearchController {
  private static final String CRITERIA_RENDER_PARAM = "log-search-criteria";
  private CriteriaSerializer criteriaSerializer = new CriteriaSerializer();
  
  // customize form data binding
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(Date.class, getDatePropertyEditor());
  }
  
  private CustomDateEditor getDatePropertyEditor() {
    SimpleDateFormat dateFormat = new SimpleDateFormat(LogConstants.DATE_FORMAT);
    dateFormat.setLenient(false);
    return new CustomDateEditor(dateFormat, true);
  }
  
  // initialize form backing objects
  @ModelAttribute("logSearchCriteria")
  public LogSearchCriteria getCommandObject() {
    return new LogSearchCriteria();
  }

  // portlet render phase
  @RenderMapping
  public String render(RenderRequest req, Model model) {
    System.out.println("render phase");
    LogSearchCriteria searchCriteria = null;
    if(req.getParameterValues(CRITERIA_RENDER_PARAM) != null) {
      searchCriteria = criteriaSerializer.getFromRenderParameter(req.getParameterValues(CRITERIA_RENDER_PARAM));
      model.addAttribute("entries", doSearchEntries(searchCriteria));
    }
    System.out.println("criteria: "+searchCriteria);
    return "search";
  }
  
  // portlet action phase
  @ActionMapping(params="op=searchLog")
  public void doSearch(@ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria, BindingResult result,
      ActionRequest request, ActionResponse response) {
    System.out.println("action phase: criteria: "+criteria);
    response.setRenderParameter(CRITERIA_RENDER_PARAM, criteriaSerializer.getAsText(criteria));
  }

  private List<LogEntry> doSearchEntries(LogSearchCriteria searchCriteria) {
    List<LogEntry> r = new ArrayList<LogEntry>();
    r.add(new LogEntry("foo"));
    r.add(new LogEntry("hello"));
    return r;
  }

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
        throw new IllegalArgumentException("error parsing date string", e); // expecting data to be valid since it's been already validated
      }
      return new LogSearchCriteria(text[0], text[1], d1, d2);
    }
  }
  
}
