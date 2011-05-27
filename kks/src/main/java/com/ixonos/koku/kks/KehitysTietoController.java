package com.ixonos.koku.kks;

import javax.portlet.ActionResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import com.ixonos.koku.kks.mock.Henkilo;
import com.ixonos.koku.kks.mock.KKSService;
import com.ixonos.koku.kks.utils.ViewHelper;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

@Controller(value = "kehitysTietoController")
@RequestMapping(value = "VIEW")
public class KehitysTietoController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Logger log = LoggerFactory
      .getLogger(KehitysTietoController.class);

  @ActionMapping(params = "toiminto=kehitysTietoihin")
  public void siirry(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @ModelAttribute(value = "tyyppi") KehitystietoTyyppi tyyppi,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {
    log.info("kehitys tietoihin");
    response.setRenderParameter("toiminto", ViewHelper.toiminto(tyyppi));
    response.setRenderParameter("hetu", lapsi.getHetu());
    response.setRenderParameter("tyyppi", tyyppi.toString());
    sessionStatus.setComplete();
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam String hetu) {
    log.info("getLapsi");
    return service.haeLapsi(hetu);
  }

  @ModelAttribute("tyyppi")
  public KehitystietoTyyppi getTyyppi(@RequestParam String tyyppi) {
    log.info("getTyyppi");
    return service.getTyyppi(tyyppi);
  }
}
