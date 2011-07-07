package fi.koku.lok;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Controller for log management (LOK).
 * 
 * @author aspluma
 */
@Controller
@RequestMapping(value = "VIEW")
public class LogSearchController {

  @RenderMapping
  public String render(RenderRequest req, Model model) {
    System.out.println("render: ");
    model.addAttribute("entries", doSearchEntries(null));
    return "search";
  }
  
  @ModelAttribute("logSearchCriteria")
  public LogSearchCriteria getCommandObject() {
    return new LogSearchCriteria();
  }

  @ActionMapping(params="op=searchLog")
  public void doSearch(@ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria, BindingResult result,
      ActionResponse response) {
    System.out.println("doSearch: "+criteria);
    System.out.println("search criteria: "+criteria.getPic());
  }

  private List<LogEntry> doSearchEntries(LogSearchCriteria searchCriteria) {
    List<LogEntry> r = new ArrayList<LogEntry>();
    r.add(new LogEntry("foo"));
    r.add(new LogEntry("hello"));
    return r;
  }
  
}
