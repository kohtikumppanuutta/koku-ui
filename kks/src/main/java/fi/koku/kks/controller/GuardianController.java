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

import fi.koku.kks.model.KksService;
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
  @Qualifier("kksService")
  private KksService kksService;

  private static final Logger LOG = LoggerFactory.getLogger(GuardianController.class);

  @RenderMapping(params = "action=showChildrens")
  public String showChilds(RenderResponse response, Model model) {
    LOG.info("showChildrens");
    model.addAttribute("childs", getChilds());
    return "childs";
  }

  @ModelAttribute("childs")
  public List<Person> getChilds() {
    LOG.info("getchilds");
    return kksService.searchChilds(null);
  }

}
