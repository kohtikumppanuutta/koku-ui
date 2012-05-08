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
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

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

import fi.koku.kks.model.CollectionForm;
import fi.koku.kks.model.Entry;
import fi.koku.kks.model.EntryValue;
import fi.koku.kks.model.KKSCollection;
import fi.koku.kks.model.KksService;
import fi.koku.kks.model.Person;
import fi.koku.kks.model.Version;
import fi.koku.kks.ui.common.Accountable;
import fi.koku.kks.ui.common.utils.Utils;
import fi.koku.services.entity.kks.v1.KksCollectionClassType;
import fi.koku.services.entity.kks.v1.KksEntryClassType;
import fi.koku.services.entity.kks.v1.KksGroupType;
import fi.koku.services.entity.kks.v1.ServiceFault;

/**
 * Controller for managing collection showing and value setting
 * 
 * @author tuomape
 * 
 */
@Controller(value = "collectionController")
@RequestMapping(value = "VIEW")
public class CollectionController {

  @Autowired
  @Qualifier("kksService")
  private KksService kksService;
  private final static String VALID = "VALID";

  private static final Logger LOG = LoggerFactory.getLogger(CollectionController.class);
  
  

  @RenderMapping(params = "action=showCollection")
  public String show(PortletSession session, @ModelAttribute(value = "child") Person child,
      @ModelAttribute(value = "collectionForm") CollectionForm collectionForm,
      @RequestParam(value = "collection") String collection,
      @RequestParam(value = "print", required = false) String print,
      @RequestParam(value = "error", required = false) String error, RenderResponse response, Model model) {
    LOG.debug("show collection");

    try {
      KKSCollection c = kksService.getKksCollection(collection, Utils.getUserInfoFromSession(session));
      collectionForm.setEntries(c.getEntries());
      session.setAttribute("kks.collection", c);
      String pic = Utils.getPicFromSession(session);

      boolean parent = c.isParent();
      boolean canSave = !parent || hasParentGroups(c.getCollectionClass());
      model.addAttribute("child", child);
      model.addAttribute("collection", c);
      model.addAttribute("authorized", kksService.getAuthorizedRegistries(pic));
      model.addAttribute("master", parent || VALID.equals(c.getUserConsentStatus()));
      model.addAttribute("parent", parent);
      model.addAttribute("empty_collection", c == null || c.getEntries() == null || c.getEntries().size() == 0);
      model.addAttribute("can_save", canSave);

      if (!model.containsAttribute("version")) {
        Version v = new Version();
        v.setName(c == null ? "" : c.getName());
        model.addAttribute("version", v);
      }

      if (StringUtils.isNotEmpty(error)) {
        model.addAttribute("error", error);
      }
      
      if (StringUtils.isNotEmpty(print)) {
        model.addAttribute("print_mode", print);
      }
      return "collection";
    } catch (ServiceFault e) {
      LOG.error("Failed to get KKS collection " + collection );
      return "error";
    }
  }

  private boolean hasParentGroups(KksCollectionClassType metadata) {
    for (KksGroupType group : metadata.getKksGroups().getKksGroup()) {
      if (group.getAccountable().equals(Accountable.GUARDIAN.getName())) {
        return true;
      }

      for (KksGroupType subGroup : group.getSubGroups().getKksGroup()) {
        if (subGroup.getAccountable().equals(Accountable.GUARDIAN.getName())) {
          return true;
        }
      }
    }
    return false;
  }

  @ModelAttribute("child")
  public Person getchild(PortletSession session, @RequestParam(value = "pic") String pic) {
    return kksService.searchCustomer(pic, Utils.getPicFromSession(session));
  }

  @ActionMapping(params = "action=saveCollection")
  public void save(PortletSession session, @ModelAttribute(value = "child") Person child, @ModelAttribute(value = "collectionForm") CollectionForm collectionForm, BindingResult bindingResult,
      @RequestParam(required = false) String multiValueId, @RequestParam(required = false) String type,
      @RequestParam(value = "valueId", required = false) String valueId, Model model, ActionResponse response,
      SessionStatus sessionStatus) {
    LOG.debug("save collection");

    KKSCollection collection = (KKSCollection) session.getAttribute("kks.collection");
    collection.setEntries(collectionForm.getEntries());
    boolean success = kksService.updateKksCollection(collection, child.getPic(), Utils.getPicFromSession(session));

    if (!success) {
      response.setRenderParameter("error", "collection.update.failed");
      response.setRenderParameter("action", "showCollection");
      response.setRenderParameter("pic", child.getPic());
      response.setRenderParameter("collection", collection.getId());
    } else {
      if (StringUtils.isNotBlank(type)) {
        response.setRenderParameter("action", "showMultivalue");
        response.setRenderParameter("pic", child.getPic());
        response.setRenderParameter("collection", collection.getId());
        response.setRenderParameter("entryType", type);
        response.setRenderParameter("valueId", valueId == null ? "" : valueId);

        if (multiValueId != null && !"".equals(multiValueId)) {
          response.setRenderParameter("entryId", multiValueId);
        }
      } else {
        response.setRenderParameter("action", "showChild");
        response.setRenderParameter("pic", child.getPic());
        session.removeAttribute("kks.collection");
      }
      sessionStatus.setComplete();
    }
  }

  @ModelAttribute("collectionForm")
  public CollectionForm getCommandObject(PortletSession session, @RequestParam(value = "collection") String collection,
      @RequestParam(value = "pic") String pic) {

    CollectionForm form = new CollectionForm();
    KKSCollection c = (KKSCollection)session.getAttribute("kks.collection");
    if ( c != null ) {
      form.setEntries(c.getEntries());
    }
    return form;
  }

  @ModelAttribute("multi")
  public EntryValue getEntryValue() {
    return new EntryValue();
  }

  @ActionMapping(params = "action=addMultivalue")
  public void saveMultivalue(PortletSession session, @ModelAttribute(value = "child") Person child,
      @RequestParam(value = "entryType") String entryType, @RequestParam(value = "collection") String collection,
      @RequestParam(value = "entryId", required = false) String entry,
      @RequestParam(value = "valueId", required = false) String valueId, EntryValue value, BindingResult bindingResult,
      ActionResponse response, SessionStatus sessionStatus) {
    LOG.debug("save multivalue");

    String id = kksService.addKksEntry(collection, child.getPic(), entry, entryType, valueId, value.getValue(),
        Utils.getPicFromSession(session));

    if (id == null) {
      response.setRenderParameter("error", "collection.entry.update.failed");
    }

    response.setRenderParameter("action", "showCollection");
    response.setRenderParameter("pic", child.getPic());
    response.setRenderParameter("collection", collection);

    sessionStatus.setComplete();
  }

  @ActionMapping(params = "action=removeMultivalue")
  public void removeMultiValue(PortletSession session, @ModelAttribute(value = "child") Person child,
      @RequestParam(value = "collection") String collection, @RequestParam(value = "entryId") String entry,
      @RequestParam(value = "valueId") String valueId, EntryValue value, BindingResult bindingResult,
      ActionResponse response, SessionStatus sessionStatus) {
    LOG.debug("remove multivalue");
    boolean success = kksService.removeKksEntry(collection, child.getPic(), entry, valueId, "",
        Utils.getPicFromSession(session));

    if (!success) {
      response.setRenderParameter("error", "collection.entry.delete.failed");
    }

    response.setRenderParameter("action", "showCollection");
    response.setRenderParameter("pic", child.getPic());
    response.setRenderParameter("collection", collection);
    sessionStatus.setComplete();
  }

  @ActionMapping(params = "action=cancelMultivalue")
  public void cancelMultiValue(@ModelAttribute(value = "child") Person child,
      @RequestParam(value = "collection") String collection, ActionResponse response, SessionStatus sessionStatus) {
    LOG.debug("cancel multivalue");

    response.setRenderParameter("action", "showCollection");
    response.setRenderParameter("pic", child.getPic());
    response.setRenderParameter("collection", collection);
    sessionStatus.setComplete();
  }
  
  @ActionMapping(params = "action=printCollection")
  public void printCollection(@ModelAttribute(value = "child") Person child,
      @RequestParam(value = "collection") String collection, ActionResponse response, SessionStatus sessionStatus) {
    LOG.debug("printCollection");
    response.setRenderParameter("action", "showCollection");
    response.setRenderParameter("pic", child.getPic());
    response.setRenderParameter("collection", collection);
    response.setRenderParameter("print", "print");
    try {
      response.setWindowState(WindowState.MAXIMIZED);
    } catch (WindowStateException e) {
     LOG.error("Failed to set maximized state",e);
    }
    sessionStatus.setComplete();
  }

  @RenderMapping(params = "action=showMultivalue")
  public String showMultivalue(PortletSession session, @ModelAttribute(value = "child") Person child,
      @RequestParam(value = "collection") String collection,
      @RequestParam(value = "entryType", required = false) String entryType,
      @RequestParam(value = "entryId", required = false) String entry,
      @RequestParam(value = "valueId", required = false) String valueId, RenderResponse response, Model model) {
    LOG.debug("show collection");

    KKSCollection c = (KKSCollection)session.getAttribute("kks.collection");
    
    String pic = Utils.getPicFromSession(session);
    
    KksEntryClassType t = kksService.getEntryClassType(entryType, pic);
    model.addAttribute("child", child);
    model.addAttribute("collection", c);
    model.addAttribute("type", t);
    model.addAttribute("valueId", valueId);


    if (StringUtils.isNotEmpty(entry)) {
      Entry ent = c.getEntry(entry);
      EntryValue val = ent.getEntryValue(valueId);

      if (val == null) {
        val = new EntryValue();
        val.setValue("");
        ent.addEntryValue(val);
      }
      model.addAttribute("entry", ent);

      model.addAttribute("value", val);

    } else {
      EntryValue val = new EntryValue();
      val.setValue("");
      model.addAttribute("value", val);

    }

    return "multivalue";
  }
  
}
