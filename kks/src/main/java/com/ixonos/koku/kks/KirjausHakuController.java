package com.ixonos.koku.kks;

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

import com.ixonos.koku.kks.malli.DemoService;
import com.ixonos.koku.kks.malli.Henkilo;
import com.ixonos.koku.kks.utils.HakuTulokset;

@Controller(value = "kirjausHakuController")
@RequestMapping(value = "VIEW")
public class KirjausHakuController {

  @Autowired
  @Qualifier("demoKksService")
  private DemoService demoService;

  private HakuTulokset hakuTulos;

  private static Logger log = LoggerFactory
      .getLogger(KirjausHakuController.class);

  @ModelAttribute("tulos")
  public HakuTulokset get() {
    return this.hakuTulos;
  }

  @RenderMapping(params = "toiminto=naytaHakutulos")
  public String nayta(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @RequestParam(value = "kuvaus") String luokitus, RenderResponse response,
      Model model) {
    log.info("nayta hakutulokset");

    model.addAttribute("lapsi", lapsi);
    model.addAttribute("hakutulos", this.hakuTulos);
    model.addAttribute("kuvaus", luokitus);

    return "haku_tulos";
  }

  @ActionMapping(params = "toiminto=haeKirjauksia")
  public void haeLapsi(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @RequestParam(value = "luokitus") String luokitus,
      @RequestParam(value = "kuvaus") String kuvaus, ActionResponse response,
      SessionStatus sessionStatus) {
    log.info("hae kirjauksia");
    String tmp[] = luokitus.replaceAll(" ", "").split(",");
    this.hakuTulos = demoService.haeKirjauksia(lapsi, tmp);
    response.setRenderParameter("toiminto", "naytaHakutulos");
    response.setRenderParameter("hetu", lapsi.getHetu());
    response.setRenderParameter("kuvaus", kuvaus);
    sessionStatus.setComplete();
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam(value = "hetu") String hetu) {
    return demoService.haeLapsi(hetu);
  }

}
