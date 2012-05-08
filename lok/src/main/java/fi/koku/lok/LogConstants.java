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
package fi.koku.lok;

import java.util.Locale;

import fi.koku.services.utility.authorizationinfo.v1.model.Role;
import fi.koku.settings.KoKuPropertiesUtil;

/**
 * Log constants.
 * 
 * @author aspluma
 */
public class LogConstants {
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final Locale LOCALE_FI = new Locale("fi");

  public static final String LOG_NORMAL = "loki";
  public static final String LOG_ADMIN = "seurantaloki";
 
  public static final String CUSTOMER_SERVICE_ENDPOINT = KoKuPropertiesUtil.get("customer.service.endpointaddress");
  public static final String CUSTOMER_SERVICE_USER_ID = KoKuPropertiesUtil.get("lok.customer.service.user.id");
  public static final String CUSTOMER_SERVICE_PASSWORD = KoKuPropertiesUtil.get("lok.customer.service.password");
  
  public static final String COMPONENT_LOK = "lok";
  public static final String LOG_WRITER_LOG = "log";
  
  public static final String LOG_SERVICE_ENDPOINT = KoKuPropertiesUtil.get("lok.service.endpointaddress");
  public static final String LOG_SERVICE_USER_ID = KoKuPropertiesUtil.get("lok.service.user.id");
  public static final String LOG_SERVICE_PASSWORD = KoKuPropertiesUtil.get("lok.service.password");
  
  public static final String AUTHORIZATION_INFO_SERVICE_ENDPOINT = "authorizationinfo.service.endpointaddress";
  public static final String AUTHORIZATION_INFO_SERVICE_USER_ID = "lok.authorizationinfo.service.user.id";
  public static final String AUTHORIZATION_INFO_SERVICE_PASSWORD = "lok.authorizationinfo.service.password";
  
  public static Role ROLE_LOK_ADMIN = new Role("ROLE_LOK_ADMIN");
  public static Role ROLE_LOK_LOG_ADMIN = new Role("ROLE_LOK_LOG_ADMIN");
  
  public static final int QUERY_RESULT_LIMIT = 50;
  public static final int CONCEPT_MIN_LENGTH = 4;
  
  private LogConstants() {
    // Contains only static constants. No need for new instances
  }
}