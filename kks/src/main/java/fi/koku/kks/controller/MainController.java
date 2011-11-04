/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.kks.controller;

import java.util.List;

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
import fi.koku.settings.KoKuPropertiesUtil;

@Controller("mainController")
@RequestMapping(value = "VIEW")
public class MainController {

  @Autowired
  @Qualifier("kksService")
  private KksService kksService;

  @RenderMapping
  public String render(PortletSession session, RenderRequest req, Model model) {

    String pic = Utils.getPicFromSession(session);
    boolean loggedIn = Utils.isLoggedIn(session);
    
    if ( !loggedIn ) {
      return Utils.notAuthenticated(model, session);
    }
    
    String kunpo = KoKuPropertiesUtil.get("environment.name");
    if ("kunpo".equalsIgnoreCase(kunpo)) {
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
