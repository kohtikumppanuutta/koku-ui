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

import com.ixonos.koku.kks.malli.Aktivointi;
import com.ixonos.koku.kks.malli.Aktivoitava;
import com.ixonos.koku.kks.malli.DemoService;
import com.ixonos.koku.kks.malli.Henkilo;
import com.ixonos.koku.kks.malli.Kokoelma;
import com.ixonos.koku.kks.malli.KokoelmaTila;
import com.ixonos.koku.kks.utils.enums.Tila;

@Controller(value = "aktivoiKokoelmaController")
@RequestMapping(value = "VIEW")
public class AktivoiKokoelmaController {

  @Autowired
  @Qualifier("demoKksService")
  private DemoService demoService;

  private static Logger log = LoggerFactory.getLogger(AktivoiKokoelmaController.class);

  @ActionMapping(params = "toiminto=aktivoiKokoelma")
  public void aktivoi(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @ModelAttribute(value = "aktivointi") Aktivointi aktivointi, BindingResult bindingResult,
      ActionResponse response, SessionStatus sessionStatus) {

    log.debug("luoKokoelma");

    Aktivoitava a = Aktivoitava.luo(aktivointi.getAktivoitavaKentta());
    String nimi = "".equals(aktivointi.getNimi()) ? a.getNimi() : aktivointi.getNimi();
    Kokoelma kokoelma = demoService.luoKokoelma(lapsi, nimi, a);

    // activate the collection for a given time period
    log.debug("aktivoi kokoelma");

    if (kokoelma != null) {
      KokoelmaTila tila = kokoelma.getTila();
      tila.setTila(Tila.AKTIIVINEN);

      /*
       * SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
       * 
       * try {
       * 
       * if (aktivointi.getAlkaa() == null ||
       * "".equals(aktivointi.getAlkaa().trim())) { tila.setAlkuPvm(new Date());
       * } else { tila.setAlkuPvm(dateFormat.parse(aktivointi.getAlkaa())); } if
       * (aktivointi.getAlkaa() == null ||
       * "".equals(aktivointi.getLoppuu().trim())) { tila.setLoppuPvm(new
       * Date()); } else {
       * 
       * tila.setLoppuPvm(dateFormat.parse(aktivointi.getLoppuu())); } } catch
       * (ParseException e) { }
       */

    }
    response.setRenderParameter("toiminto", "naytaLapsi");
    response.setRenderParameter("hetu", lapsi.getHetu());
    aktivointi = getCommandObject();
    aktivointi.setAlkaa("");
    aktivointi.setLoppuu("");
    aktivointi.setNimi("");
    aktivointi.setAktivoitavaKentta("");
    sessionStatus.setComplete();
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam String hetu) {
    log.info("getLapsi");
    return demoService.haeLapsi(hetu);
  }

  @ModelAttribute("aktivointi")
  public Aktivointi getCommandObject() {
    log.debug("get aktivointi command object");
    return new Aktivointi();
  }

  @ActionMapping(params = "toiminto=aktivoi")
  public void aktivoi(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @RequestParam(value = "kokoelma") String kokoelma, ActionResponse response, SessionStatus sessionStatus) {
    Kokoelma k = lapsi.getKks().getKokoelma(kokoelma);

    // activate the collection for a given time period
    log.debug("aktivoi kokoelma");

    if (k != null) {
      KokoelmaTila tila = k.getTila();
      tila.setTila(Tila.AKTIIVINEN);
    }

    response.setRenderParameter("toiminto", "naytaLapsi");
    response.setRenderParameter("hetu", lapsi.getHetu());
    sessionStatus.setComplete();
  }

  @ActionMapping(params = "toiminto=lukitse")
  public void lukitse(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @RequestParam(value = "kokoelma") String kokoelma, ActionResponse response, SessionStatus sessionStatus) {
    Kokoelma k = lapsi.getKks().getKokoelma(kokoelma);

    // activate the collection for a given time period
    log.debug("aktivoi kokoelma");

    if (k != null) {
      KokoelmaTila tila = k.getTila();
      tila.setTila(Tila.LUKITTU);
    }

    response.setRenderParameter("toiminto", "naytaLapsi");
    response.setRenderParameter("hetu", lapsi.getHetu());
    sessionStatus.setComplete();
  }
}
