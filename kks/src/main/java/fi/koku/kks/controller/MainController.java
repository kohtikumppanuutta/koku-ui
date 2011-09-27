package fi.koku.kks.controller;

import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.kks.model.KksService;
import fi.koku.kks.model.Person;
import fi.koku.kks.ui.common.utils.Utils;

@Controller("mainController")
@RequestMapping(value = "VIEW")
public class MainController {

  @Autowired
  @Qualifier("kksService")
  private KksService kksService;

  @RenderMapping
  public String render(PortletSession session, RenderRequest req, Model model) {
    String kunpo = ((PortletConfig) req.getAttribute("javax.portlet.config")).getInitParameter("kunpo");
    if (Boolean.valueOf(kunpo)) {
      System.out.println("************* KUNPO ************************");
      model.addAttribute("childs", getChilds(session));
      return "childs";
    } else {
      model.addAttribute("child", new Person());
      return "search";
    }
  }

  public List<Person> getChilds(PortletSession session) {
    return kksService.searchChilds(Utils.getPicFromSession(session));
  }

}
