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

import com.ixonos.koku.kks.malli.Aktivointi;
import com.ixonos.koku.kks.malli.DemoService;
import com.ixonos.koku.kks.malli.Henkilo;

@Controller(value = "lapsiController")
@RequestMapping(value = "VIEW")
public class LapsiController {

  @Autowired
  @Qualifier("demoKksService")
  private DemoService demoService;

  private static Logger log = LoggerFactory.getLogger(LapsiController.class);

  @ActionMapping(params = "toiminto=lapsenTietoihin")
  public void lapsenTietoihin(@ModelAttribute(value = "lapsi") Henkilo lapsi, BindingResult bindingResult,
      ActionResponse response, SessionStatus sessionStatus) {
    log.debug("lapsenTietoihin");

    response.setRenderParameter("toiminto", "naytaLapsi");
    response.setRenderParameter("hetu", lapsi.getHetu());
    getCommandObject();
    sessionStatus.setComplete();
  }

  @RenderMapping(params = "toiminto=naytaLapsi")
  public String nayta(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @ModelAttribute(value = "aktivointi") Aktivointi aktivointi, RenderResponse response, Model model) {
    log.info("nayta lapsi");
    model.addAttribute("lapsi", lapsi);
    model.addAttribute("kokoelmat", lapsi.getKks().getKokoelmat());
    model.addAttribute("aktivoitavat", demoService.haeHenkilonKokoelmat(lapsi));

    aktivointi = getCommandObject();
    aktivointi.setNimi("");
    return "lapsi";
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam String hetu) {
    return demoService.haeLapsi(hetu);
  }

  @ModelAttribute("aktivointi")
  public Aktivointi getCommandObject() {
    log.debug("get aktivointi command object");
    return new Aktivointi();
  }

}
