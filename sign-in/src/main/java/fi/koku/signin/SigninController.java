/*
 * Copyright 2012 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.signin;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.settings.KoKuPropertiesUtil;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.portal.application.PortalRequestContext;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.portal.webui.util.Util;

/**
 * Controller for the signin
 * 
 * @author Ixonos / tuomape
 * 
 */
@Controller("SigninController")
@RequestMapping(value = "VIEW")
public class SigninController {

  @RenderMapping
  public String render(PortletSession session, RenderRequest req, Model model) {
    setUserInfo(model);    
    setLoginProperties(model);
    return "signin";
  }

  /**
   * Sets login related properties
   * 
   * @param model
   */
  private void setLoginProperties(Model model) {
    String pwdSupported = KoKuPropertiesUtil.get("signin.change.pwd.supported");
    String loginUrl = KoKuPropertiesUtil.get("signin.login.url");
    model.addAttribute("loginUrl", loginUrl );
    model.addAttribute("pwdSupported", pwdSupported);
  }

  /**
   * Sets user info 
   * 
   * @param model
   */
  private void setUserInfo(Model model) {
    User portletUser = null;

    PortalRequestContext context = Util.getPortalRequestContext();

    String remoteUserName = context.getRemoteUser();

    OrganizationService organizationService = (OrganizationService) PortalContainer.getInstance()
        .getComponentInstanceOfType(

        OrganizationService.class);

    if (remoteUserName != null) {

      try {
        portletUser = organizationService.getUserHandler().findUserByName(remoteUserName);
        model.addAttribute("fullname", portletUser.getFullName());
      } catch (Exception e) {  
        // not intrested 
      }

    }
  }

}
