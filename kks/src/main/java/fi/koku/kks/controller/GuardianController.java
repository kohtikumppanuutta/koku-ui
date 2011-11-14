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
import fi.koku.kks.ui.common.utils.Utils;

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
  public String showChilds(PortletSession session, RenderResponse response, Model model) {
    LOG.debug("showChildrens");
    String pic = Utils.getPicFromSession(session);
    model.addAttribute("childs", getChilds(session));
    return "childs";
  }

  @ModelAttribute("childs")
  public List<Person> getChilds(PortletSession session) {
    LOG.debug("getchilds");
    return kksService.searchChilds(Utils.getPicFromSession(session));
  }

}
