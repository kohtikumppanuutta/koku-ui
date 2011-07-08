package com.ixonos.koku.pyh;

import javax.portlet.RenderRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ixonos.koku.pyh.mock.User;

/**
 * This is a dummy role (selector) controller that is used to select the user role.
 * At later phase of implementation the role will be defined elsewhere.
 * 
 * @author hurulmi
 *
 */

@Controller(value = "dummyRoleController")
@RequestMapping(value = "VIEW")
public class DummyRoleController {
  
  private static Logger log = LoggerFactory.getLogger(DummyRoleController.class);
  
  @RenderMapping
  public String render(/*RenderRequest request, RenderResponse response*/)
  {
    return "choose";
  }
  
}
