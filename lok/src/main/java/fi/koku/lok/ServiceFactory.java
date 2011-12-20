/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.lok;

import fi.koku.services.entity.customer.v1.CustomerServiceFactory;
import fi.koku.services.entity.customer.v1.CustomerServicePortType;
import fi.koku.services.utility.authorizationinfo.v1.AuthorizationInfoService;
import fi.koku.services.utility.authorizationinfo.v1.AuthorizationInfoServiceFactory;
import fi.koku.services.utility.log.v1.LogServiceFactory;
import fi.koku.services.utility.log.v1.LogServicePortType;
import fi.koku.settings.KoKuPropertiesUtil;

/**
 * service factory for lok ui.
 * 
 * @author aspluma
 */
public class ServiceFactory {

  public LogServicePortType getLogservice() {
    LogServiceFactory logServiceFactory = new LogServiceFactory(
        LogConstants.LOG_SERVICE_USER_ID,
        LogConstants.LOG_SERVICE_PASSWORD, LogConstants.LOG_SERVICE_ENDPOINT);
    return logServiceFactory.getLogService();
  }
  
  public CustomerServicePortType getCustomerService() {
    CustomerServiceFactory customerServiceFactory = new CustomerServiceFactory(
        LogConstants.CUSTOMER_SERVICE_USER_ID, 
        LogConstants.CUSTOMER_SERVICE_PASSWORD,
        LogConstants.CUSTOMER_SERVICE_ENDPOINT);
    return customerServiceFactory.getCustomerService();
  }

  public AuthorizationInfoService getAuthorizationInfoService() {
    String uid = KoKuPropertiesUtil.get(LogConstants.AUTHORIZATION_INFO_SERVICE_USER_ID);
    String pwd = KoKuPropertiesUtil.get(LogConstants.AUTHORIZATION_INFO_SERVICE_PASSWORD);
    String ep = KoKuPropertiesUtil.get(LogConstants.AUTHORIZATION_INFO_SERVICE_ENDPOINT);
    AuthorizationInfoServiceFactory f = new AuthorizationInfoServiceFactory(uid, pwd, ep);
    return f.getAuthorizationInfoService();
  }
}