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
package fi.koku.pyh.ui.common;

import fi.koku.settings.KoKuPropertiesUtil;

/**
 * This class contains static constants used in PYH.
 * 
 * @author hurulmi
 *
 */

public class PyhConstants {
  
  public static final String CUSTOMER_SERVICE_ENDPOINT = KoKuPropertiesUtil.get("customer.service.endpointaddress");
  public static final String COMMUNITY_SERVICE_ENDPOINT = KoKuPropertiesUtil.get("community.service.endpointaddress");
  public static final String LOK_SERVICE_ENDPOINT = KoKuPropertiesUtil.get("lok.service.endpointaddress");
  
  public static final String KOKU_FROM_EMAIL_ADDRESS = KoKuPropertiesUtil.get("koku.from.email.address");
  public static final String KOKU_SUPPORT_EMAIL_ADDRESS = KoKuPropertiesUtil.get("koku.support.email.address");
  
  public static final String CUSTOMER_SERVICE_USER_ID = KoKuPropertiesUtil.get("pyh.customer.service.user.id");
  public static final String CUSTOMER_SERVICE_PASSWORD = KoKuPropertiesUtil.get("pyh.customer.service.password");
  
  public static final String COMMUNITY_SERVICE_USER_ID = KoKuPropertiesUtil.get("pyh.community.service.user.id");
  public static final String COMMUNITY_SERVICE_PASSWORD = KoKuPropertiesUtil.get("pyh.community.service.password");
  
  public static final String LOK_SERVICE_USER_ID = KoKuPropertiesUtil.get("pyh.lok.service.user.id");
  public static final String LOK_SERVICE_PASSWORD = KoKuPropertiesUtil.get("pyh.lok.service.password");
  
  public static final String COMPONENT_PYH = "PYH";
  
  private PyhConstants() {
  }
}
