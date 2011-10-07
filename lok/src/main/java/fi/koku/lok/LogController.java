package fi.koku.lok;

import java.util.List;

import javax.portlet.PortletSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.services.entity.authorizationinfo.util.AuthUtils;
import fi.koku.services.entity.authorizationinfo.v1.AuthorizationInfoService;
import fi.koku.services.entity.authorizationinfo.v1.impl.AuthorizationInfoServiceDummyImpl;
import fi.koku.services.entity.authorizationinfo.v1.model.Role;

/**
 * Controller for the main view of Lok portlet.
 * 
 * @author makinsu
 */
@Controller(value = "logController")
@RequestMapping(value = "VIEW")
public class LogController {
  private static final Logger log = LoggerFactory.getLogger(LogSearchController.class);
 
  private AuthorizationInfoService authorizationInfoService;
  
  public LogController() {
    authorizationInfoService = new AuthorizationInfoServiceDummyImpl();
  }
 
 
  @RenderMapping
  public String render(PortletSession session,  Model model) {
    log.debug("Main Log controller");
    
    model.addAttribute("user", LogUtils.getPicFromSession(session));
    
    // this is used for selecting which part of the page to show
    List<Role> userRoles = authorizationInfoService.getUsersRoles("lok", LogUtils.getPicFromSession(session));
    
    if (AuthUtils.isOperationAllowed("AdminSystemLogFile", userRoles)) {
      model.addAttribute("showMenu", true);
    } else if (AuthUtils.isOperationAllowed("ViewAdminLogFile", userRoles)) {
      model.addAttribute("redirectToSearch", true);
    }

    return "menu";
  }

}
