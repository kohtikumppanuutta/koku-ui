package fi.koku.lok;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import fi.koku.services.utility.log.v1.LogService;
import fi.koku.services.utility.log.v1.LogServicePortType;

/**
 * Helper class for creating WS endpoints.
 * 
 * @author Ixonos / laukksa
 * @author Ixonos / aspluma
 */
public class KoKuWSFactory {
  private String uid;
  private String pwd;
  private String endpointBaseUrl;
  
  private final URL LOG_WSDL_LOCATION = getClass().getClassLoader().getResource("/wsdl/logService.wsdl");
  private static final String LOG_SERVICE_VERSION = "0.0.1-SNAPSHOT";


  @Deprecated
  public KoKuWSFactory() {
    this("marko", "marko", "http://kohtikumppanuutta-dev.dmz:8180");
  }
  
  public KoKuWSFactory(String uid, String pwd, String endpointBaseUrl) {
    this.uid = uid;
    this.pwd = pwd;
    this.endpointBaseUrl = endpointBaseUrl;
  }

  public LogServicePortType getLogService() {
    LogService service = new LogService(LOG_WSDL_LOCATION, new QName(
        "http://services.koku.fi/utility/log/v1", "logService"));
    LogServicePortType logServicePort = service.getLogServiceSoap11Port();
    String epAddr = endpointBaseUrl + "/lok-service-"+LOG_SERVICE_VERSION+"/LogServiceBean";

    ((BindingProvider)logServicePort).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, epAddr);
    ((BindingProvider)logServicePort).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, uid);
    ((BindingProvider)logServicePort).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, pwd);
    return logServicePort;
  }
  
}
