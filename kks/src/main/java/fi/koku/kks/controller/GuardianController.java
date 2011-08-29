package fi.koku.kks.controller;

import java.util.List;

import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.kks.model.DemoService;
import fi.koku.kks.model.Person;

/**
 * Controller for guardian role
 * 
 * @author tuomape
 */
@Controller(value = "guardianController")
@RequestMapping(value = "VIEW")
public class GuardianController {

  @Autowired
  @Qualifier("demoKksService")
  private DemoService demoService;

  private static final Logger log = LoggerFactory.getLogger(GuardianController.class);

  @RenderMapping(params = "action=showChildrens")
  public String showChilds(RenderResponse response, Model model) {
    log.info("showChildrens");
    model.addAttribute("childs", getChilds());
    return "childs";
  }

  @ModelAttribute("childs")
  public List<Person> getChilds() {
    log.info("getchilds");
    return demoService.searchChilds(null);
  }

}
