package fi.koku.kks.controller;

import javax.portlet.RenderRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.kks.model.DemoService;

@Controller(value = "EditController")
@RequestMapping(value = "EDIT")
public class EditController {

  @Autowired
  @Qualifier("demoKksService")
  private DemoService demoService;

  private static Logger log = LoggerFactory.getLogger(EditController.class);

  @RenderMapping
  public String render(RenderRequest req, @RequestParam(value = "message", required = false) String message, Model model) {

    if (message != null) {
      model.addAttribute("message", "Malli resetoitu");
    }
    return "edit";
  }

  @RenderMapping(params = "action=resetModel")
  public String showClassifications(RenderRequest req, @RequestParam(value = "message", required = false) String message,
      Model model) {
    demoService.luo("");

    if (message != null) {
      model.addAttribute("message", "Malli resetoitu");
    }
    return "edit";
  }
}
