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

import javax.portlet.RenderRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.kks.model.KksService;

/**
 * Edit mode controller. Allows metadata clearing
 * 
 * @author Ixonos / tuomape
 *
 */
@Controller(value = "EditController")
@RequestMapping(value = "EDIT")
public class EditController {

  @Autowired
  @Qualifier("kksService")
  private KksService kksService;

  @RenderMapping
  public String render(RenderRequest req, @RequestParam(value = "message", required = false) String message, Model model) {

    if (message != null) {
      model.addAttribute("message", "metadata resetted");
    }
    return "edit";
  }

  @RenderMapping(params = "action=resetModel")
  public String showClassifications(RenderRequest req,
      @RequestParam(value = "message", required = false) String message, Model model) {
    kksService.clearMetadata();

    if (message != null) {
      model.addAttribute("message", "");
    }
    return "edit";
  }
}
