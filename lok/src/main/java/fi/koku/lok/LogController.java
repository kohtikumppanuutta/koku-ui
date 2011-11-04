/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.lok;

import java.util.List;

import javax.portlet.PortletSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.services.utility.authorizationinfo.util.AuthUtils;
import fi.koku.services.utility.authorizationinfo.v1.AuthorizationInfoService;
import fi.koku.services.utility.authorizationinfo.v1.impl.AuthorizationInfoServiceDummyImpl;
import fi.koku.services.utility.authorizationinfo.v1.model.Role;

/**
 * Controller for the main view of Lok portlet.
 * 
 * @author makinsu
 */
@Controller(value = "logController")
@RequestMapping(value = "VIEW")
public class LogController {
  private static final Logger log = LoggerFactory.getLogger(LogController.class);
  
  private AuthorizationInfoService authorizationInfoService;
  
  public LogController() {
    ServiceFactory f = new ServiceFactory();
    authorizationInfoService = f.getAuthorizationInfoService();     
  }
 
  @RenderMapping(params = "action=home")
  public String renderHome(PortletSession session, Model model){
    
  log.info("Lok portlet main menu");
  List<Role> userRoles = authorizationInfoService.getUsersRoles(LogConstants.COMPONENT_LOK, LogUtils.getPicFromSession(session));
    
    if (AuthUtils.isOperationAllowed("AdminSystemLogFile", userRoles)) {
      model.addAttribute("showMenu", true);
    } else if (AuthUtils.isOperationAllowed("ViewAdminLogFile", userRoles)) {
      model.addAttribute("redirectToSearch", true);
    }
    
    model.addAttribute("search", false); //This means that search was NOT done
    
    return "menu";
  }
  
  @RenderMapping
  public String render(PortletSession session,  Model model) {
    
    // this is used for selecting which part of the page to show
    List<Role> userRoles = authorizationInfoService.getUsersRoles(LogConstants.COMPONENT_LOK, 
        LogUtils.getPicFromSession(session));
    
    if (AuthUtils.isOperationAllowed("AdminSystemLogFile", userRoles)) {
      model.addAttribute("showMenu", true);
    } else if (AuthUtils.isOperationAllowed("ViewAdminLogFile", userRoles)) {
      model.addAttribute("redirectToSearch", true);
    } else{
      log.error("No role found for Lok");
    }
    
    return "menu";
  }

}
