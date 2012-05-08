/*
 * Copyright 2012 Ixonos Plc, Finland. All rights reserved.
 * 
 * This file is part of Kohti kumppanuutta.
 *
 * This file is licensed under GNU LGPL version 3.
 * Please see the 'license.txt' file in the root directory of the package you received.
 * If you did not receive a license, please contact the copyright holder
 * (http://www.ixonos.com/).
 *
 */
package fi.koku.kks.controller;

import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import fi.koku.kks.model.Creatable;
import fi.koku.kks.model.Creation;
import fi.koku.kks.model.KksService;
import fi.koku.kks.model.Person;
import fi.koku.kks.model.Version;
import fi.koku.kks.ui.common.State;
import fi.koku.kks.ui.common.utils.Utils;

/**
 * Controller for creating agreements and plans
 * 
 * @author tuomape
 */
@Controller(value = "createCollectionController")
@RequestMapping(value = "VIEW")
public class CreateCollectionController {

  @Autowired
  @Qualifier("kksService")
  private KksService kksService;

  private static final Logger LOG = LoggerFactory.getLogger(CreateCollectionController.class);

  @ActionMapping(params = "action=createNewVersion")
  public void createVersion(PortletSession session, @ModelAttribute(value = "child") Person child,
      @RequestParam String id, Version version, BindingResult result, ActionResponse response,
      SessionStatus sessionStatus) {

    version.validate(version, result);
    if (!result.hasErrors()) {
      LOG.debug("create new version");
      String collection = kksService.createKksCollectionVersion(version.getName(), id, child.getPic(),
          version.isClear(), Utils.getPicFromSession(session));

      if (collection == null) {
        result.reject("collection.create.failed");
        response.setRenderParameter("action", "showCollection");
        response.setRenderParameter("pic", child.getPic());
        response.setRenderParameter("collection", id);
      } else {
        version.setClear(false);
        response.setRenderParameter("action", "showCollection");
        response.setRenderParameter("pic", child.getPic());
        response.setRenderParameter("collection", collection);
        sessionStatus.setComplete();
      }
    } else {
      response.setRenderParameter("action", "showCollection");
      response.setRenderParameter("pic", child.getPic());
      response.setRenderParameter("collection", id);
    }
  }

  @ActionMapping(params = "action=createCollection")
  public void create(PortletSession session, @ModelAttribute(value = "child") Person child, Creation creation,
      BindingResult bindingResult, ActionResponse response, SessionStatus sessionStatus) {

    LOG.debug("create collection");

    creation.validate(creation, bindingResult);
    if (!bindingResult.hasErrors()) {

      Creatable a = Creatable.create(creation.getField());
      String name = "".equals(creation.getName()) ? a.getName() : creation.getName();
      String id = kksService.createKksCollection(name, a.getId(), child.getPic(), Utils.getPicFromSession(session));

      if (id == null) {
        bindingResult.reject("collection.create.failed");
      }

      creation.setField("");
      creation.setName("");
      response.setRenderParameter("action", "showChild");
      response.setRenderParameter("pic", child.getPic());

      if (id != null) {
        sessionStatus.setComplete();
      }
    } else {
      response.setRenderParameter("action", "showChild");
      response.setRenderParameter("pic", child.getPic());
    }
  }

  @ModelAttribute("child")
  public Person getchild(PortletSession session, @RequestParam String pic) {
    LOG.debug("getchild");
    return kksService.searchCustomer(pic, Utils.getPicFromSession(session));
  }

  @ActionMapping(params = "action=activate")
  public void activate(PortletSession session, @ModelAttribute(value = "child") Person child,
      @RequestParam(value = "collection") String collection, ActionResponse response, SessionStatus sessionStatus) {

    boolean success = kksService.updateKksCollectionStatus(child.getPic(), collection, State.ACTIVE.toString(),
        Utils.getPicFromSession(session));
    response.setRenderParameter("action", "showChild");
    response.setRenderParameter("pic", child.getPic());

    if (!success) {
      response.setRenderParameter("error", "collection.status.update.failed");
    }
    sessionStatus.setComplete();
  }

  @ActionMapping(params = "action=lock")
  public void lock(PortletSession session, @ModelAttribute(value = "child") Person child,
      @RequestParam(value = "collection") String collection, ActionResponse response, SessionStatus sessionStatus) {

    boolean success = kksService.updateKksCollectionStatus(child.getPic(), collection, State.LOCKED.toString(),
        Utils.getPicFromSession(session));

    response.setRenderParameter("action", "showChild");
    response.setRenderParameter("pic", child.getPic());

    if (!success) {
      response.setRenderParameter("error", "collection.status.update.failed");
    }
    sessionStatus.setComplete();
  }
}
