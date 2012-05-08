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
