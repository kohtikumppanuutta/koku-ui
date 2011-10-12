/**
 * Utility/Helper class to get UserInfo-related data more easily.
 * Notice: Use of this class requires use of UserInfoPortletFilter in portlet
 */
package fi.koku.portlet.filter.userinfo;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import fi.koku.portlet.filter.userinfo.UserInfo;

/**
 * @author mikkope, tuomape
 *
 */
public class UserInfoUtils {

  //Singleton
  private UserInfoUtils() {}

  /**
   * Get user's PIC from request
   * 
   * @param session, is user's portletrequest object
   * @return PIC (HETU in this case) or empty string ("") if not found.
   */
  public static String getPicFromSession(PortletRequest pReq) {
    String ret = "";
    PortletSession p = pReq.getPortletSession();
    if(p!=null){
      UserInfo info = UserInfoUtils.getUserInfoFromSession(p);
      if (info != null) {
        return info.getPic();
      }
    }
    return ret;
  }
  
  /**
   * Get user's PIC from session
   * 
   * @param session, is user's portletSession object
   * @return PIC (HETU in this case) or empty string ("") if not found.
   */
  public static String getPicFromSession(PortletSession session) {
    UserInfo info = UserInfoUtils.getUserInfoFromSession(session);
    if (info == null) {
      return "";
    }
    return info.getPic();
  }

  /**
   * Get UserInfo object from portlet session
   * 
   * @param session, is user's portletSession object
   * @return UserInfo-object filled with user data or null, if not found
   */
  public static UserInfo getUserInfoFromSession(PortletSession session) {
    return (UserInfo) session.getAttribute(UserInfo.KEY_USER_INFO);
  }
  
}
