package fi.koku.kks.ui.common.utils;

import fi.koku.settings.KoKuPropertiesUtil;

public final class Constants {

  private Constants() {

  }

  public final static String ENDPOINT = KoKuPropertiesUtil.get("kks.service.endpointaddress");
  public final static String CUSTOMER_ENDPOINT = KoKuPropertiesUtil.get("customer.service.endpointaddress");
  public final static String COMMUNITY_ENDPOINT = KoKuPropertiesUtil.get("community.service.endpointaddress");
  public final static String TIVA_ENDPOINT = KoKuPropertiesUtil.get("tiva-kks.service.endpointaddress");

  public final static String CUSTOMER_SERVICE_USER_ID = KoKuPropertiesUtil.get("kks.customer.service.user.id");
  public final static String CUSTOMER_SERVICE_PASSWORD = KoKuPropertiesUtil.get("kks.customer.service.password");

  public final static String COMMUNITY_SERVICE_USER_ID = KoKuPropertiesUtil.get("kks.community.service.user.id");
  public final static String COMMUNITY_SERVICE_PASSWORD = KoKuPropertiesUtil.get("kks.community.service.password");

  public final static String KKS_SERVICE_USER_ID = "marko";
  public final static String KKS_SERVICE_PASSWORD = "marko";

  public final static String COMMUNITY_TYPE_GUARDIAN_COMMUNITY = "guardian_community";

  public final static String ROLE_DEPENDANT = "dependant";
  public final static String ROLE_GUARDIAN = "guardian";

  public final static String COMPONENT_KKS = "KKS";

}
