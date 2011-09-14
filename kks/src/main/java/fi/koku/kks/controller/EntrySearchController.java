package fi.koku.kks.controller;

import javax.portlet.ActionResponse;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.kks.model.DemoService;
import fi.koku.kks.model.Person;
import fi.koku.kks.ui.common.utils.SearchResult;

/**
 * Controller for entry searches
 * 
 * @author tuomape
 */
@Controller(value = "entrySearchController")
@RequestMapping(value = "VIEW")
public class EntrySearchController {

  @Autowired
  @Qualifier("demoKksService")
  private DemoService demoService;

  private SearchResult searchResult;

  private static final Logger LOG = LoggerFactory.getLogger(EntrySearchController.class);

  @ModelAttribute("result")
  public SearchResult get() {
    return this.searchResult;
  }

  @RenderMapping(params = "action=showSearchResult")
  public String showResults(@ModelAttribute(value = "child") Person child,
      @RequestParam(value = "description") String description, RenderResponse response, Model model) {
    LOG.info("show search result");

    model.addAttribute("child", child);
    model.addAttribute("searchResult", this.searchResult);
    model.addAttribute("description", description);

    return "search_result";
  }

  @ActionMapping(params = "action=searchEntries")
  public void search(@ModelAttribute(value = "child") Person child,
      @RequestParam(value = "classification") String classification,
      @RequestParam(value = "description") String description, ActionResponse response, SessionStatus sessionStatus) {
    LOG.info("search entries");
    String tmp[] = classification.replaceAll(" ", "").split(",");
    this.searchResult = demoService.searchEntries(child, tmp);
    response.setRenderParameter("action", "showSearchResult");
    response.setRenderParameter("pic", child.getPic());
    response.setRenderParameter("description", description);
    sessionStatus.setComplete();
  }

  @ModelAttribute("child")
  public Person getchild(@RequestParam(value = "pic") String pic) {
    return demoService.searchChild(pic);
  }

}
