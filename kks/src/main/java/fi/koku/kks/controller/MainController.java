package fi.koku.kks.controller;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.kks.model.KksService;

@Controller("mainController")
@RequestMapping(value = "VIEW")
public class MainController {

  @Autowired
  @Qualifier("kksService")
  private KksService kksService;

  @RenderMapping
  public String render(PortletSession session, RenderRequest req, Model model) {

    if (!kksService.onkoLuotu()) {
      kksService.luo("");
    }

    return "choose";
  }

}
