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

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderResponse;

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

import fi.koku.kks.model.KksService;
import fi.koku.kks.model.Person;
import fi.koku.kks.ui.common.utils.Utils;

/**
 * Controller for role municipal employee
 * 
 * @author tuomape
 * 
 */
@Controller(value = "municipalEmplouyeeController")
@RequestMapping(value = "VIEW")
public class MunicipalEmployeeController {

  @Autowired
  @Qualifier("kksService")
  private KksService kksService;

  private static final Logger LOG = LoggerFactory.getLogger(MunicipalEmployeeController.class);

  @RenderMapping(params = "action=showEmployee")
  public String show(PortletSession session, RenderResponse response,
      @RequestParam(value = "childs", required = false) String[] childs,
      @RequestParam(value = "search", required = false) String search,  @RequestParam(value = "error", required = false) String error,
      Model model) {
    LOG.debug("show employee");
    
    String pic = Utils.getPicFromSession(session);
   
    model.addAttribute("childs", toChilds(childs, pic));

    if (search != null) {
      model.addAttribute("search", search);
    }
    
    if (error != null) {
      model.addAttribute("error", error);
    }
    return "search";
  }

  @ActionMapping(params = "action=searchChild")
  public void fecthChild(PortletSession session, @ModelAttribute(value = "child") Person child,
      BindingResult bindingResult, ActionResponse response, SessionStatus sessionStatus) {
    LOG.info("search child");

    String pic = Utils.getPicFromSession(session);
    String error = validateInput(child, pic);
    Person p = error == null ? kksService.searchPerson(child, Utils.getPicFromSession(session)) : null;

    if (p != null) {
      response.setRenderParameter("action", "showChild");
      response.setRenderParameter("pic", p.getPic());
    } else {
      response.setRenderParameter("action", "showEmployee");
      response.setRenderParameter("childs", new String[] { "" });
      response.setRenderParameter("search", "true");
      
      if ( error != null ) {
        response.setRenderParameter("error", error );
      }
    }
    sessionStatus.setComplete();
  }

  private String validateInput(Person child, String pic) {
    if ( child.getPic().trim().equals( pic ) ) {
      return "ui.kks.illegal.pic.search";
    }
    
    return null;
  }

  @ModelAttribute("child")
  public Person getCommandObject() {
    LOG.debug("get entry command object");
    return new Person();
  }

  private List<Person> toChilds(String[] childIds, String user) {
    List<Person> tmp = new ArrayList<Person>();

    if (childIds != null) {
      for (String s : childIds) {
        if (!"".equals(s)) {
          tmp.add(kksService.searchCustomer(s, user));
        }
      }
    }
    return tmp;
  }

}
