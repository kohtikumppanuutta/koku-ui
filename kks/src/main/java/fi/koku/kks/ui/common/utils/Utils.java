/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.kks.ui.common.utils;

import javax.portlet.PortletSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;

import fi.koku.portlet.filter.userinfo.UserInfo;

/**
 * Common helper methods for KKS portlet
 * 
 * @author Ixonos / tuomape
 * 
 */
public final class Utils {

  private Utils() {

  }

  public static String getPicFromSession(PortletSession session) {
    UserInfo info = Utils.getUserInfoFromSession(session);
    if (info == null) {
      return "";
    }
    return info.getPic();
  }

  public static UserInfo getUserInfoFromSession(PortletSession session) {
    return (UserInfo) session.getAttribute(UserInfo.KEY_USER_INFO);
  }
  
  public static boolean isLoggedIn(PortletSession session) {
    UserInfo ui = (UserInfo)session.getAttribute(UserInfo.KEY_USER_INFO);
    
    if ( ui == null ) {
      return false;
    }
    
    if (ui.isStrongAuthenticationEnabled()) {
      return ui.hasStrongAuthentication();
    }
    return StringUtils.isNotEmpty(getPicFromSession(session));
  }
  
  
  public static String getAuthenticationURL(PortletSession session) {
    UserInfo ui = (UserInfo)session.getAttribute(UserInfo.KEY_USER_INFO);
    
    String url = "";
    if (ui != null && ui.isStrongAuthenticationEnabled() && !ui.hasStrongAuthentication()) {
      url = (String)session.getAttribute(UserInfo.KEY_VETUMA_AUTHENTICATION_URL);
    }
    return url;
  }
  
  public static String notAuthenticated(Model model, PortletSession session ) {
    model.addAttribute("stronAuthenticationURL", Utils.getAuthenticationURL(session));
    return "authenticate";
  }
}
