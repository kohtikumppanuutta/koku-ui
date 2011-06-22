package com.ixonos.koku.kks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

  private static Logger log = LoggerFactory
      .getLogger(AktivoiKokoelmaController.class);

  @ActionMapping(params = "toiminto=aktivoiKokoelma")
  public void aktivoi(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @ModelAttribute(value = "aktivointi") Aktivointi aktivointi,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {
    
    // create a new collection if it hasn't been created earlier
    log.debug("luoKokoelma");

    demoService.luoKokoelma(lapsi, aktivointi.getAktivoitavaKentta());
    
    
    // activate the collection for a given time period
    log.debug("aktivoi kokoelma");

    Kokoelma kokoelma = lapsi.getKks().getKokoelma(
        aktivointi.getAktivoitavaKentta());

    if (kokoelma != null) {
      KokoelmaTila tila = kokoelma.getTila();
      tila.setTila(Tila.AKTIIVINEN);

      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

      try {

        if (aktivointi.getAlkaa() == null
            || "".equals(aktivointi.getAlkaa().trim())) {
          tila.setAlkuPvm(new Date());
        } else {
          tila.setAlkuPvm(dateFormat.parse(aktivointi.getAlkaa()));
        }
        if (aktivointi.getAlkaa() == null
            || "".equals(aktivointi.getLoppuu().trim())) {
          tila.setLoppuPvm(new Date());
        } else {

          tila.setLoppuPvm(dateFormat.parse(aktivointi.getLoppuu()));
        }
      } catch (ParseException e) {
      }

    }
    response.setRenderParameter("toiminto", "naytaLapsi");
    response.setRenderParameter("hetu", lapsi.getHetu());
    aktivointi = getCommandObject();
    aktivointi.setAlkaa("");
    aktivointi.setLoppuu("");
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
}
