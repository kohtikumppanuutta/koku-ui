package fi.koku.kks.ui.common.utils;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import fi.koku.services.entity.tiva.v1.KokuTivaToKksService;
import fi.koku.services.entity.tiva.v1.KokuTivaToKksService_Service;
import fi.koku.settings.KoKuPropertiesUtil;

public class ConsentServiceFactory {

  private final URL wsdlLocation = getClass().getClassLoader().getResource("wsdl/communityService.wsdl");

  public ConsentServiceFactory() {

  }

  public KokuTivaToKksService getService() {

    KokuTivaToKksService_Service ft = new KokuTivaToKksService_Service(wsdlLocation, new QName(
        "http://services.koku.fi/entity/tiva/v1", "KokuTivaToKksService"));

    KokuTivaToKksService port = ft.getKokuTivaToKksServicePort();
    String epAddr = KoKuPropertiesUtil.get("kks.tiva.service.full.endpointaddress");

    ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, epAddr);

    return port;
  }
}