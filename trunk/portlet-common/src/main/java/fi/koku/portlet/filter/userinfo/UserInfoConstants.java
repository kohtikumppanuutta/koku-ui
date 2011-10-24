/**
 * 
 */
package fi.koku.portlet.filter.userinfo;

import fi.koku.settings.KoKuPropertiesUtil;

/**
 * @author mikkope
 *
 */
public class UserInfoConstants {

  public static final String COMPONENT_USER_INFO_FILTER = "UserInfoFilter";
  public static final String AUTH_IMPL_CLASS_NAME = KoKuPropertiesUtil.get("userinfo.portlet.filter.auth.class.name");
  
}
