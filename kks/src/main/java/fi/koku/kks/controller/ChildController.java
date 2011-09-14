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
import fi.koku.kks.model.KksService;
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
  @Qualifier("kksService")
  private KksService kksService;

  private static final Logger LOG = LoggerFactory.getLogger(ChildController.class);

  @ActionMapping(params = "action=toChildInfo")
  public void toChildInfo(@ModelAttribute(value = "child") Person child, BindingResult bindingResult,
      ActionResponse response, SessionStatus sessionStatus) {
    LOG.debug("toChildInfo");

    response.setRenderParameter("action", "showChild");
    response.setRenderParameter("pic", child.getPic());
    getCommandObject();
    sessionStatus.setComplete();
  }

  @RenderMapping(params = "action=showChild")
  public String show(@ModelAttribute(value = "child") Person child,
      @ModelAttribute(value = "creation") Creation creation, RenderResponse response, Model model) {
    LOG.info("show child");
    model.addAttribute("child", child);
    model.addAttribute("collections", kksService.getKksCollections(child.getPic()));
    model.addAttribute("creatables", kksService.searchPersonCreatableCollections(child));

    creation.setName("");
    return "child";
  }

  @RenderMapping(params = "action=showPegasos")
  public String showPegasos(@ModelAttribute(value = "child") Person child, RenderResponse response, Model model) {
    LOG.info("show child");
    model.addAttribute("child", child);
    return "pegasos";
  }

  @ModelAttribute("child")
  public Person getChild(@RequestParam String pic) {
    return kksService.searchChild(pic);
  }

  @ModelAttribute("creation")
  public Creation getCommandObject() {
    LOG.debug("get creation command object");
    return new Creation();
  }

}
