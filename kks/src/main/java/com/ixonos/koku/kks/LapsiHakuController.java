package com.ixonos.koku.kks;

import javax.portlet.ActionResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import com.ixonos.koku.kks.malli.DemoService;
import com.ixonos.koku.kks.malli.Henkilo;

@Controller(value = "kksHakuController")
@RequestMapping(value = "VIEW")
public class LapsiHakuController {

  @Autowired
  @Qualifier("demoKksService")
  private DemoService demoService;

  private static Logger log = LoggerFactory
      .getLogger(LapsiHakuController.class);

  @ActionMapping(params = "toiminto=hae")
  public void hae(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {

    response.setRenderParameter("toiminto", "naytaLapsi");
    response.setRenderParameter("hetu", lapsi.getHetu());
    sessionStatus.setComplete();
  }

  @InitBinder("haku")
  public void initBinder(WebDataBinder binder) {
    log.debug("init binder");
    // binder.registerCustomEditor(KKSKentta.class, new
    // KehitystietoTyyppiEditor(service));
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam String hetu) {
    log.debug("getLapsi");
    return demoService.haeLapsi(hetu);
  }

}
