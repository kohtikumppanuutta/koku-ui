package fi.koku.kks.controller;

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

import fi.koku.kks.model.Creation;
import fi.koku.kks.model.DemoService;
import fi.koku.kks.model.Person;

/**
 * Controller for child info
 * 
 * @author tuomape
 * 
 */
@Controller(value = "childController")
@RequestMapping(value = "VIEW")
public class ChildController {

  @Autowired
  @Qualifier("demoKksService")
  private DemoService demoService;

  private static Logger log = LoggerFactory.getLogger(ChildController.class);

  @ActionMapping(params = "action=toChildInfo")
  public void toChildInfo(@ModelAttribute(value = "child") Person child, BindingResult bindingResult,
      ActionResponse response, SessionStatus sessionStatus) {
    log.debug("toChildInfo");

    response.setRenderParameter("action", "showChild");
    response.setRenderParameter("pic", child.getPic());
    getCommandObject();
    sessionStatus.setComplete();
  }

  @RenderMapping(params = "action=showChild")
  public String show(@ModelAttribute(value = "child") Person child,
      @ModelAttribute(value = "creation") Creation creation, RenderResponse response, Model model) {
    log.info("nayta child");
    model.addAttribute("child", child);
    model.addAttribute("collections", child.getKks().getCollections());
    model.addAttribute("creatables", demoService.searchPersonCreatableCollections(child));

    creation.setName("");
    return "child";
  }

  @RenderMapping(params = "action=showPegasos")
  public String showPegasos(@ModelAttribute(value = "child") Person child, RenderResponse response, Model model) {
    log.info("nayta child");
    model.addAttribute("child", child);
    return "pegasos";
  }

  @ModelAttribute("child")
  public Person getChild(@RequestParam String pic) {
    return demoService.searchChild(pic);
  }

  @ModelAttribute("creation")
  public Creation getCommandObject() {
    log.debug("get creation command object");
    return new Creation();
  }

}
