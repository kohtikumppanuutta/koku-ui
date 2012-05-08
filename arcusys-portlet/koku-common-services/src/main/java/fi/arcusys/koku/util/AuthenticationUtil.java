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
package fi.arcusys.koku.util;

import javax.portlet.PortletSession;

import fi.koku.portlet.filter.userinfo.UserInfo;

/**
 * Common helper methods for KKS portlet
 *
 * 
 * @author Ixonos / tuomape
 * 
 */
public final class AuthenticationUtil {

  private AuthenticationUtil() {

  }

  public static String getPicFromSession(PortletSession session) {
	    UserInfo info = AuthenticationUtil.getUserInfoFromSession(session);
	    if (info == null) {
	      return "";
	    }
	    return info.getPic();
	  }

	  public static UserInfo getUserInfoFromSession(PortletSession session) {
	    return (UserInfo) session.getAttribute(UserInfo.KEY_USER_INFO);
	  }
  
}
