package fi.koku.kks.ui.common.utils;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import fi.koku.services.entity.tiva.v1.KokuTivaToKksService;
import fi.koku.services.entity.tiva.v1.KokuTivaToKksService_Service;

public class ConsentServiceFactory {

  private String uid;
  private String pwd;
  private String endpointBaseUrl;
  private String implVersion = "0.1-SNAPSHOT";
  private final URL wsdlLocation = getClass().getClassLoader().getResource("wsdl/communityService.wsdl");

  public ConsentServiceFactory(String uid, String pwd, String endpointBaseUrl) {
    this.uid = uid;
    this.pwd = pwd;
    this.endpointBaseUrl = endpointBaseUrl;
  }

  public KokuTivaToKksService getService() {

    KokuTivaToKksService_Service ft = new KokuTivaToKksService_Service(wsdlLocation, new QName(
        "http://services.koku.fi/entity/tiva/v1", "KokuTivaToKksService"));

    KokuTivaToKksService port = ft.getKokuTivaToKksServicePort();
    String epAddr = endpointBaseUrl + "arcusys-koku-" + implVersion + "-tiva-model-" + implVersion
        + "/KokuTivaToKksServiceImpl";

    ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, epAddr);
    ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, uid);
    ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, pwd);
    return port;
  }

}