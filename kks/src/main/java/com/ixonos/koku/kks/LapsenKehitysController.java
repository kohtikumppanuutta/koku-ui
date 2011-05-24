package com.ixonos.koku.kks;

import java.util.ArrayList;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ixonos.koku.kks.mock.Henkilo;
import com.ixonos.koku.kks.mock.KKSProperty;
import com.ixonos.koku.kks.mock.KKSService;
import com.ixonos.koku.kks.mock.KehitysAsia;
import com.ixonos.koku.kks.mock.Kehitystieto;
import com.ixonos.koku.kks.utils.KehitysAsiaTyyppiEditor;
import com.ixonos.koku.kks.utils.enums.KehitysAsiaTyyppi;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

@Controller(value = "lapsenKehitysController")
@RequestMapping(value = "VIEW")
public class LapsenKehitysController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Logger log = LoggerFactory
      .getLogger(LapsenKehitysController.class);

  @RenderMapping(params = "toiminto=naytaKehitys")
  public String nayta(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      RenderResponse response, Model model) {
    log.info("nayta kehitys");
    model.addAttribute("lapsi", lapsi);

    Kehitystieto tieto = lapsi.getKks().getKehitystieto(
        KehitystietoTyyppi.LAPSEN_KEHITYS);
    model.addAttribute(
        "mittaukset",
        tieto == null ? new ArrayList<KehitysAsia>() : tieto
            .getKehitysAsiat(KehitysAsiaTyyppi.MITTAUS));

    model.addAttribute("arviot", tieto == null ? new ArrayList<KehitysAsia>()
        : tieto.getKehitysAsiat(KehitysAsiaTyyppi.ARVIO));

    model.addAttribute(
        "havainnot",
        tieto == null ? new ArrayList<KehitysAsia>() : tieto
            .getKehitysAsiat(KehitysAsiaTyyppi.HAVAINTO));

    return "kehitys";
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam String hetu) {
    log.info("getLapsi");
    return service.getChild(hetu);
  }

  @ModelAttribute("kehitys")
  public KehitysAsia getCommandObject() {
    log.debug("get mittaus command object");
    KehitysAsia tmp = new KehitysAsia("", KehitysAsiaTyyppi.MITTAUS);
    tmp.addProperty(new KKSProperty("kuvaus", ""));
    return tmp;
  }

  @InitBinder("kehitys")
  public void initBinder(WebDataBinder binder) {
    log.debug("init binder");
    binder.registerCustomEditor(KehitysAsiaTyyppi.class,
        new KehitysAsiaTyyppiEditor(service));
  }

  @ActionMapping(params = "toiminto=lisaa")
  public void lisaaMittaus(
      @ModelAttribute(value = "kehitys") KehitysAsia tarve,
      @ModelAttribute(value = "lapsi") Henkilo lapsi,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {
    log.debug("lisaa mittaus");

    tarve.setMuokkaaja("Koodista muokkaaja");
    tarve.setMuokkausPvm(new Date());

    String id = ""
        + lapsi.getKks().getKehitystieto(KehitystietoTyyppi.LAPSEN_KEHITYS)
            .getKehitysAsiat().values().size();
    tarve.setId(id);
    lapsi.getKks().getKehitystieto(KehitystietoTyyppi.LAPSEN_KEHITYS)
        .addKehitysAsia(tarve);

    tarve = getCommandObject();
    response.setRenderParameter("toiminto", "naytaKehitys");
    response.setRenderParameter("hetu", lapsi.getHetu());
    sessionStatus.setComplete();
  }
}
