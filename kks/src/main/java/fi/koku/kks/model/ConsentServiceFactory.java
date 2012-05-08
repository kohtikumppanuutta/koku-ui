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
package fi.koku.kks.model;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import fi.koku.services.entity.tiva.v1.KokuTivaToKksService;
import fi.koku.services.entity.tiva.v1.KokuTivaToKksService_Service;
import fi.koku.settings.KoKuPropertiesUtil;

/**
 * Factory for accessing consents
 * 
 * @author Ixonos / tuomape
 * 
 */
public class ConsentServiceFactory {

  private final URL wsdlLocation = getClass().getClassLoader().getResource("wsdl/tivaService.wsdl");

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
