/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.kks.ui.common.utils;

import javax.portlet.PortletSession;


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
  
  
}
