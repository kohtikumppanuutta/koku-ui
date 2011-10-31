/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package com.ixonos.koku.pyh;

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
  
  final public static String CUSTOMER_SERVICE_USER_ID = KoKuPropertiesUtil.get("pyh.customer.service.user.id");
  final public static String CUSTOMER_SERVICE_PASSWORD = KoKuPropertiesUtil.get("pyh.customer.service.password");
  
  final public static String COMMUNITY_SERVICE_USER_ID = KoKuPropertiesUtil.get("pyh.community.service.user.id");
  final public static String COMMUNITY_SERVICE_PASSWORD = KoKuPropertiesUtil.get("pyh.community.service.password");
  
  final public static String LOK_SERVICE_USER_ID = KoKuPropertiesUtil.get("pyh.lok.service.user.id");
  final public static String LOK_SERVICE_PASSWORD = KoKuPropertiesUtil.get("pyh.lok.service.password");
  
  final public static String COMMUNITY_TYPE_GUARDIAN_COMMUNITY = "guardian_community";
  final public static String COMMUNITY_TYPE_FAMILY = "family_community";
  
  final public static String ROLE_DEPENDANT = "dependant";
  final public static String ROLE_GUARDIAN = "guardian";
  final public static String ROLE_PARENT = "parent";
  
  final public static String COMPONENT_PYH = "PYH";
  
}
