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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ixonos.koku.kks.malli.DemoService;
import com.ixonos.koku.kks.malli.Henkilo;
import com.ixonos.koku.kks.malli.Kirjaus;
import com.ixonos.koku.kks.malli.KirjausTyyppi;
import com.ixonos.koku.kks.malli.Kokoelma;

@Controller(value = "kokoelmaController")
@RequestMapping(value = "VIEW")
public class KokoelmaController {

  @Autowired
  @Qualifier("demoKksService")
  private DemoService demoService;

  private static Logger log = LoggerFactory.getLogger(KokoelmaController.class);

  @RenderMapping(params = "toiminto=naytaKokoelma")
  public String nayta(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @RequestParam(value = "kokoelma") String kokoelma, RenderResponse response, Model model) {
    log.info("nayta kokoelma");
    model.addAttribute("lapsi", lapsi);
    model.addAttribute("kokoelma", lapsi.getKks().getKokoelma(kokoelma));
    return "kokoelma";
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam(value = "hetu") String hetu) {
    return demoService.haeLapsi(hetu);
  }

  @ActionMapping(params = "toiminto=tallennaKokoelma")
  public void tallenna(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @ModelAttribute(value = "kirjaus") Kokoelma kirjaus, BindingResult bindingResult,
      @RequestParam(required = false) String multiValueId, @RequestParam(required = false) String type,
      ActionResponse response, SessionStatus sessionStatus) {
    log.info("tallenna kokoelma");
    lapsi.getKks().poistaKokoelma(kirjaus);
    lapsi.getKks().lisaaKokoelma(kirjaus);

    if (type != null && !"".equals(type)) {

      response.setRenderParameter("toiminto", "naytaMoniarvoinen");
      response.setRenderParameter("hetu", lapsi.getHetu());
      response.setRenderParameter("kokoelma", kirjaus.getId());
      response.setRenderParameter("kirjausTyyppi", type);

      if (multiValueId != null && !"".equals(multiValueId)) {
        response.setRenderParameter("kirjausId", multiValueId);
      }

    } else {
      response.setRenderParameter("toiminto", "naytaLapsi");
      response.setRenderParameter("hetu", lapsi.getHetu());
    }
    sessionStatus.setComplete();
  }

  @ModelAttribute("kirjaus")
  public Kokoelma getCommandObject(@RequestParam(value = "kokoelma") String kokoelma,
      @RequestParam(value = "hetu") String hetu) {
    log.debug("get sairaus command object");

    Henkilo lapsi = demoService.haeLapsi(hetu);
    return lapsi.getKks().getKokoelma(kokoelma);
  }

  @ActionMapping(params = "toiminto=lisaaMoniarvoinen")
  public void tallennaMoniarvoinen(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @RequestParam(value = "kirjausTyyppi") String kirjausTyyppi, @RequestParam(value = "kokoelma") String kokoelma,
      @RequestParam(value = "kirjausId", required = false) String kirjaus, @RequestParam(value = "arvo") String arvo,
      ActionResponse response, SessionStatus sessionStatus) {
    log.info("tallenna moniarvoinen");

    Kokoelma kok = lapsi.getKks().getKokoelma(kokoelma);

    if (kirjaus != null && !"".equals(kirjaus)) {
      Kirjaus k = kok.getKirjaus(kirjaus);
      k.setArvo(arvo);
    } else {
      KirjausTyyppi t = kok.getTyyppi().getKirjausTyyppi(kirjausTyyppi);
      if (t != null) {
        Kirjaus k = new Kirjaus(arvo, new Date(), 1, t.getRekisteri(), "Kaisa Kirjaaja", t);
        kok.lisaaMoniarvoinenKirjaus(k);
      }

    }
    response.setRenderParameter("toiminto", "naytaKokoelma");
    response.setRenderParameter("hetu", lapsi.getHetu());
    response.setRenderParameter("kokoelma", kokoelma);
    sessionStatus.setComplete();
  }

  @ActionMapping(params = "toiminto=poistaMoniarvoinen")
  public void poistaMoniarvoinen(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @RequestParam(value = "kokoelma") String kokoelma, @RequestParam(value = "kirjausId") String kirjaus,
      ActionResponse response, SessionStatus sessionStatus) {
    log.info("poista moniarvoinen");

    Kokoelma kok = lapsi.getKks().getKokoelma(kokoelma);

    if (kirjaus != null && !"".equals(kirjaus)) {
      kok.poistaMoniarvoinenKirjaus(kirjaus);
    }

    response.setRenderParameter("toiminto", "naytaKokoelma");
    response.setRenderParameter("hetu", lapsi.getHetu());
    response.setRenderParameter("kokoelma", kokoelma);
    sessionStatus.setComplete();
  }

  @ActionMapping(params = "toiminto=peruMoniarvoinen")
  public void peruMoniarvoinen(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @RequestParam(value = "kokoelma") String kokoelma, ActionResponse response, SessionStatus sessionStatus) {
    log.info("peru moniarvoinen");

    response.setRenderParameter("toiminto", "naytaKokoelma");
    response.setRenderParameter("hetu", lapsi.getHetu());
    response.setRenderParameter("kokoelma", kokoelma);
    sessionStatus.setComplete();
  }

  @RenderMapping(params = "toiminto=naytaMoniarvoinen")
  public String naytaMoniarvoinen(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @RequestParam(value = "kokoelma") String kokoelma,
      @RequestParam(value = "kirjausTyyppi", required = false) String kirjausTyyppi,
      @RequestParam(value = "kirjausId", required = false) String kirjaus, RenderResponse response, Model model) {
    log.info("nayta kokoelma");

    Kokoelma kok = lapsi.getKks().getKokoelma(kokoelma);
    KirjausTyyppi t = kok.getTyyppi().getKirjausTyyppi(kirjausTyyppi);

    model.addAttribute("lapsi", lapsi);
    model.addAttribute("kokoelma", kok);
    model.addAttribute("tyyppi", t);

    if (kirjaus != null && !"".equals(kirjaus)) {
      model.addAttribute("kirjausArvo", kok.getKirjaus(kirjaus));
    }
    return "moniarvoinen";
  }
}
