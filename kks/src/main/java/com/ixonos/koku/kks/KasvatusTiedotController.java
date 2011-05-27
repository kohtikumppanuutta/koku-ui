package com.ixonos.koku.kks;

import java.util.Date;

import javax.portlet.ActionResponse;
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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ixonos.koku.kks.mock.Henkilo;
import com.ixonos.koku.kks.mock.KKSService;
import com.ixonos.koku.kks.mock.Kehitystieto;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

@Controller(value = "kasvatusTiedotController")
@RequestMapping(value = "VIEW")
public class KasvatusTiedotController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Logger log = LoggerFactory
      .getLogger(KasvatusTiedotController.class);

  @ActionMapping(params = "toiminto=muokkaaKasvatusTietoa")
  public void muokkaa(@ModelAttribute(value = "tieto") Kehitystieto tieto,
      @ModelAttribute(value = "lapsi") Henkilo lapsi,
      @RequestParam(value = "aktiivinen") String aktiivinen,
      ActionResponse response, SessionStatus sessionStatus) {
    log.debug("muokkaa kasvatustietoa");

    tieto.setMuokkausPvm(new Date());
    tieto.setMuokkaaja("Koodista Muokkkaaja");
    lapsi.getKks().addKehitystieto(tieto);

    response.setRenderParameter("toiminto", "naytaLapsi");
    response.setRenderParameter("hetu", lapsi.getHetu());
    response.setRenderParameter("aktiivinen", "" + aktiivinen.toString());
    sessionStatus.setComplete();
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam String hetu) {
    log.info("getLapsi");
    return service.haeLapsi(hetu);
  }

  @RenderMapping(params = "toiminto=naytaKasvatusTiedot")
  public String nayta(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @ModelAttribute(value = "tieto") Kehitystieto tieto,
      RenderResponse response, Model model) {
    log.info("näytä kasvatustiedot");
    model.addAttribute("lapsi", lapsi);
    model.addAttribute("tieto", tieto);
    model.addAttribute("aktiivinen", tieto.getTila().isAktiivinen());
    return "kasvatustiedot";
  }

  @ModelAttribute("tieto")
  public Kehitystieto getKasvatustieto(@RequestParam String hetu) {
    log.info("getTukitarve");
    Henkilo lapsi = service.haeLapsi(hetu);
    return lapsi.getKks().getKehitystieto(
        KehitystietoTyyppi.KASVATUSTA_OHJAAVAT_TIEDOT);
  }

}
