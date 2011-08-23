package com.ixonos.koku.lok;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import fi.koku.services.utility.log.v1.LogService;
import fi.koku.services.utility.log.v1.LogServicePortType;

/**
 * Helper class for creating WS endpoints.
 * 
 * @author aspluma
 */
public class KoKuWSFactory {
  private String uid;
  private String pwd;
  private String epBaseUrl;

  public KoKuWSFactory() {
    uid = "marko"; // TODO: parameterize
    pwd = "marko";
    epBaseUrl = "http://localhost:8080";
  }
  
  public LogServicePortType getLogService() {
    String ep = epBaseUrl+"/lok-service-0.0.1-SNAPSHOT/LogServiceBean?wsdl"; // TODO: parameterize

    URL wsdlLocation;
    try {
      wsdlLocation = new URL(ep);
    } catch (MalformedURLException e) {
      throw new RuntimeException("invalid url", e);
    }
    QName serviceName = new QName("http://services.koku.fi/utility/log/v1", "logService");
    LogService logService = new LogService(wsdlLocation, serviceName);
    
    LogServicePortType port = logService.getLogServiceSoap11Port();
    ((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, uid);
    ((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, pwd);
    return port;
  }
  
}
