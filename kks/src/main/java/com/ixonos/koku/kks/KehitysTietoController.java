package com.ixonos.koku.kks;

import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

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

  @RenderMapping(params = "toiminto=naytaKehitystieto")
  public String nayta(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @ModelAttribute(value = "tyyppi") KehitystietoTyyppi tyyppi,
      RenderResponse response, Model model) {
    log.info("näytä kehitystieto");
    model.addAttribute("lapsi", lapsi);
    model.addAttribute("tieto", lapsi.getKks().getKehitystieto(tyyppi));
    return ViewHelper.navigate(tyyppi);
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam String hetu) {
    log.info("getLapsi");
    return service.getChild(hetu);
  }

  @ModelAttribute("tyyppi")
  public KehitystietoTyyppi getTyyppi(@RequestParam String tyyppi) {
    log.info("getTyyppi");
    return service.getField(tyyppi);
  }
}
