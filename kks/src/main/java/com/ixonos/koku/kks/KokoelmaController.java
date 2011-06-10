package com.ixonos.koku.kks;

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
      @RequestParam(value = "kokoelma") String kokoelma,
      RenderResponse response, Model model) {
    log.info("nayta kokoelma");
    model.addAttribute("lapsi", lapsi);
    model.addAttribute("kokoelma", lapsi.getKks().getKokoelma(kokoelma));
    return "kokoelma";
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam(value = "hetu") String hetu) {
    log.info("getLapsi with hetu=" + hetu);// #TODO# Remove hetu from log
    return demoService.haeLapsi(hetu);
  }

  @ActionMapping(params = "toiminto=tallennaKokoelma")
  public void tallenna(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @ModelAttribute(value = "kirjaus") Kokoelma kirjaus,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {
    log.info("tallenna kokoelma");
    lapsi.getKks().poistaKokoelma(kirjaus);
    lapsi.getKks().lisaaKokoelma(kirjaus);
    response.setRenderParameter("toiminto", "naytaLapsi");
    response.setRenderParameter("hetu", lapsi.getHetu());
    sessionStatus.setComplete();
  }

  @ModelAttribute("kirjaus")
  public Kokoelma getCommandObject(
      @RequestParam(value = "kokoelma") String kokoelma,
      @RequestParam(value = "hetu") String hetu) {
    log.debug("get sairaus command object");

    Henkilo lapsi = demoService.haeLapsi(hetu);
    return lapsi.getKks().getKokoelma(kokoelma);
  }

}
