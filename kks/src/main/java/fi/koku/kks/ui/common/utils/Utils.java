package fi.koku.kks.ui.common.utils;

import javax.portlet.PortletSession;

import fi.koku.portlet.filter.userinfo.UserInfo;

/**
 * Common helper methods for KKS portlet
 * 
 * @author Ixonos / tuomape
 * 
 */
public class Utils {

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
