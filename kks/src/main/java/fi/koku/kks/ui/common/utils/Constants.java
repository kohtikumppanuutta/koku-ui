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
package fi.koku.kks.ui.common.utils;

import fi.koku.settings.KoKuPropertiesUtil;

/**
 * Constants for KKS
 * 
 * @author Ixonos / tuomape
 * 
 */
public final class Constants {

  private Constants() {

  }

  public static final String ENDPOINT = KoKuPropertiesUtil.get("kks.service.endpointaddress");
  public static final String CUSTOMER_ENDPOINT = KoKuPropertiesUtil.get("customer.service.endpointaddress");
  public static final String COMMUNITY_ENDPOINT = KoKuPropertiesUtil.get("community.service.endpointaddress");
  public static final String TIVA_ENDPOINT = KoKuPropertiesUtil.get("tiva-kks.service.endpointaddress");
  public static final String AUTH_ENDPOINT = KoKuPropertiesUtil.get("authentication.service.endpointaddress");

  public static final String CUSTOMER_SERVICE_USER_ID = KoKuPropertiesUtil.get("kks.customer.service.user.id");
  public static final String CUSTOMER_SERVICE_PASSWORD = KoKuPropertiesUtil.get("kks.customer.service.password");

  public static final String COMMUNITY_SERVICE_USER_ID = KoKuPropertiesUtil.get("kks.community.service.user.id");
  public static final String COMMUNITY_SERVICE_PASSWORD = KoKuPropertiesUtil.get("kks.community.service.password");

  public static final String AUTH_SERVICE_USER_ID = KoKuPropertiesUtil
      .get("kks.authentication.community.service.user.id");
  public static final String AUTH_SERVICE_PASSWORD = KoKuPropertiesUtil
      .get("kks.authentication.community.service.password");

  public static final String KKS_SERVICE_USER_ID = "marko";
  public static final String KKS_SERVICE_PASSWORD = "marko";

  public static final String COMMUNITY_TYPE_GUARDIAN_COMMUNITY = "guardian_community";

  public static final String ROLE_DEPENDANT = "dependant";
  public static final String ROLE_GUARDIAN = "guardian";

  public static final String COMPONENT_KKS = "KKS";

}
