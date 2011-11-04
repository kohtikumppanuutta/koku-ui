/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.kks.controller;

import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
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
import fi.koku.kks.ui.common.utils.Utils;

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
    sessionStatus.setComplete();
  }

  @RenderMapping(params = "action=showChild")
  public String show(PortletSession session, @ModelAttribute(value = "child") Person child,
      @RequestParam(value = "error", required = false) String error,
      @RequestParam(value = "message", required = false) String message, RenderResponse response, Model model) {
    LOG.debug("show child");

    String pic = Utils.getPicFromSession(session);
    
    boolean loggedIn = Utils.isLoggedIn(session);
    
    if ( !loggedIn ) {
      return Utils.notAuthenticated(model, session);
    }
    
    if (StringUtils.isEmpty(child.getFirstName())) {
      child = getChild(session, child.getPic());
    }

    model.addAttribute("child", child);
    model.addAttribute("collections", kksService.getKksCollections(child.getPic(), pic));
    model.addAttribute("creatables", kksService.searchPersonCreatableCollections(child, pic));
    model.addAttribute("registries", kksService.getAuthorizedRegistries(pic));

    if (!model.containsAttribute("creation")) {
      model.addAttribute("creation", new Creation());
    }

    if (StringUtils.isNotEmpty(error)) {
      model.addAttribute("error", error);
    }

    if (StringUtils.isNotEmpty(message)) {
      model.addAttribute("message", message);
    }

    return "child";
  }

  @ActionMapping(params = "action=sendConsentRequest")
  public void sendConsentRequest(PortletSession session, @ModelAttribute(value = "child") Person child,
      @RequestParam String collectionId, @RequestParam String consent, ActionResponse response,
      SessionStatus sessionStatus) {
    LOG.debug("sendConsentRequest");

    boolean success = kksService.sendConsentRequest(consent, child.getPic(), Utils.getPicFromSession(session));

    response.setRenderParameter("action", "showChild");
    response.setRenderParameter("pic", child.getPic());

    if (!success) {
      response.setRenderParameter("error", "collection.consent.request.failed");
    } else {
      response.setRenderParameter("message", "collection.consent.request.success");
    }
    sessionStatus.setComplete();
  }

  @RenderMapping(params = "action=showPegasos")
  public String showPegasos(@ModelAttribute(value = "child") Person child, RenderResponse response, Model model) {
    LOG.debug("show child");
    model.addAttribute("child", child);
    return "pegasos";
  }

  @ModelAttribute("child")
  public Person getChild(PortletSession session, @RequestParam String pic) {
    return kksService.searchCustomer(pic, Utils.getPicFromSession(session));
  }

}
