package com.ixonos.koku.kks;
// TÄTÄ LUOKKAA EI ENÄÄ TARVITA, KUN KOKOELMAN AKTIVOINTI JA LUONTI ON YHDISTETTY AKTIVOINTILUOKKAAN  
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

@Controller(value = "lisaaKokoelmaController")
@RequestMapping(value = "VIEW")
public class LisaaKokoelmaController {

  @Autowired
  @Qualifier("demoKksService")
  private DemoService demoService;

  private static Logger log = LoggerFactory
      .getLogger(LisaaKokoelmaController.class);

  @ActionMapping(params = "toiminto=luoKokoelma")
  public void aktivoi(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @ModelAttribute(value = "aktivointi") Aktivointi aktivointi,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {
    log.debug("luoKokoelma");

    demoService.luoKokoelma(lapsi, aktivointi.getAktivoitavaKentta());
    response.setRenderParameter("toiminto", "naytaLapsi");
    response.setRenderParameter("hetu", lapsi.getHetu());
    aktivointi = getCommandObject();
    aktivointi.setAlkaa("");
    aktivointi.setLoppuu("");
    sessionStatus.setComplete();
  }

  @ModelAttribute("aktivointi")
  public Aktivointi getCommandObject() {
    log.debug("get aktivointi command object");
    return new Aktivointi();
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam String hetu) {
    log.info("getLapsi");
    return demoService.haeLapsi(hetu);
  }

}
