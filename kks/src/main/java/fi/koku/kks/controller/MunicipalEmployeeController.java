package fi.koku.kks.controller;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.kks.model.DemoService;
import fi.koku.kks.model.Person;

/**
 * Controller for role municipal employee
 * 
 * @author tuomape
 * 
 */
@Controller(value = "kuntaTyontekijaController")
@RequestMapping(value = "VIEW")
public class MunicipalEmployeeController {

  @Autowired
  @Qualifier("demoKksService")
  private DemoService demoService;

  private static Logger log = LoggerFactory.getLogger(MunicipalEmployeeController.class);

  @RenderMapping(params = "action=showEmployee")
  public String show(RenderResponse response, @RequestParam(value = "childs", required = false) String[] childs,
      @RequestParam(value = "search", required = false) String search, Model model) {
    log.info("show employee");
    model.addAttribute("childs", toChilds(childs));

    if (search != null) {
      model.addAttribute("search", search);
    }
    return "search";
  }

  @ActionMapping(params = "action=searchChild")
  public void fecthChild(@ModelAttribute(value = "child") Person child, BindingResult bindingResult,
      ActionResponse response, SessionStatus sessionStatus) {
    log.info("search child");

    response.setRenderParameter("action", "showEmployee");
    response.setRenderParameter("childs", toArray(demoService.haeHenkilo(child)));
    response.setRenderParameter("search", "true");
    sessionStatus.setComplete();
  }

  @ModelAttribute("child")
  public Person getCommandObject() {
    log.debug("get entry command object");
    return new Person();
  }

  private String[] toArray(List<Person> childs) {
    String[] tmp = null;

    if (childs.isEmpty()) {
      tmp = new String[] { "" };
    } else {
      tmp = new String[childs.size()];
    }

    for (int i = 0; i < childs.size(); i++) {
      tmp[i] = childs.get(i).getPic();
    }
    return tmp;
  }

  private List<Person> toChilds(String[] childIds) {
    List<Person> tmp = new ArrayList<Person>();

    if (childIds != null) {
      for (String s : childIds) {
        if (!"".equals(s)) {
          tmp.add(demoService.searchChild(s));
        }
      }
    }
    return tmp;
  }

}
