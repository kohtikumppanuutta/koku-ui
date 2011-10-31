/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
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
