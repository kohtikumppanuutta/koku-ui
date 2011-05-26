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
import com.ixonos.koku.kks.mock.KehitysAsia;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

@Controller(value = "muokkaaTukitarvettaController")
@RequestMapping(value = "VIEW")
public class TukitarveController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Logger log = LoggerFactory
      .getLogger(TukitarveController.class);

  @ActionMapping(params = "toiminto=muokkaaTukitarvetta")
  public void muokkaaTukitarvetta(
      @ModelAttribute(value = "tukitarve") KehitysAsia tarve,
      @ModelAttribute(value = "lapsi") Henkilo lapsi,
      @RequestParam(value = "vanhaNimi") String vanhaNimi,
      @RequestParam(value = "aktiivinen") String aktiivinen,
      ActionResponse response, SessionStatus sessionStatus) {
    log.debug("muokkaaTukitarvetta");

    tarve.setMuokkausPvm(new Date());
    tarve.setMuokkaaja("Koodista Muokkkaaja");
    lapsi.getKks().getKehitystieto(KehitystietoTyyppi.TUKITARVE)
        .removeKehitysAsia(vanhaNimi);

    lapsi.getKks().getKehitystieto(KehitystietoTyyppi.TUKITARVE)
        .addKehitysAsia(tarve);

    response.setRenderParameter("toiminto", "naytaTukitoimet");
    response.setRenderParameter("hetu", lapsi.getHetu());
    response.setRenderParameter("aktiivinen", "" + aktiivinen);
    sessionStatus.setComplete();
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam String hetu) {
    log.info("getLapsi");
    return service.getChild(hetu);
  }

  @RenderMapping(params = "toiminto=naytaTukitarve")
  public String naytaTukitarve(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @ModelAttribute(value = "tukitarve") KehitysAsia tarve,
      @RequestParam(value = "aktiivinen") String aktiivinen,
      RenderResponse response, Model model) {
    log.info("näytä tukitoimet");
    model.addAttribute("lapsi", lapsi);

    model.addAttribute("tukitarve", tarve);
    model.addAttribute("aktiivinen", aktiivinen.toString());
    return "tukitarve";
  }

  @ModelAttribute("tukitarve")
  public KehitysAsia getTukitarve(@RequestParam String hetu,
      @RequestParam String tarve) {
    log.info("getTukitarve");
    Henkilo lapsi = service.getChild(hetu);
    return lapsi.getKks().getKehitystieto(KehitystietoTyyppi.TUKITARVE)
        .getKehitysAsia(tarve);
  }

}
