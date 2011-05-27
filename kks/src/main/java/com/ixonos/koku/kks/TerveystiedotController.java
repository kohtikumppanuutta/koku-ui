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

@Controller(value = "terveystiedotController")
@RequestMapping(value = "VIEW")
public class TerveystiedotController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Logger log = LoggerFactory
      .getLogger(TerveystiedotController.class);

  @RenderMapping(params = "toiminto=naytaTerveys")
  public String nayta(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      RenderResponse response, Model model) {
    log.info("nayta terveys");
    model.addAttribute("lapsi", lapsi);

    Kehitystieto tieto = lapsi.getKks().getKehitystieto(
        KehitystietoTyyppi.TERVEYDEN_TILA);
    model.addAttribute(
        "sairaudet",
        tieto == null ? new ArrayList<KehitysAsia>() : tieto
            .getKehitysAsiat(KehitysAsiaTyyppi.SAIRAUS));

    model.addAttribute(
        "ruokavaliot",
        tieto == null ? new ArrayList<KehitysAsia>() : tieto
            .getKehitysAsiat(KehitysAsiaTyyppi.ERIKOISRUOKAVALIO));

    model.addAttribute("kaynnit", tieto == null ? new ArrayList<KehitysAsia>()
        : tieto.getKehitysAsiat(KehitysAsiaTyyppi.KAYNTI));

    model.addAttribute("aktiivinen", tieto.getTila().isAktiivinen());

    return "terveystiedot";
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam String hetu) {
    log.info("getLapsi");
    return service.haeLapsi(hetu);
  }

  @ModelAttribute("sairaus")
  public KehitysAsia getCommandObject() {
    log.debug("get sairaus command object");
    KehitysAsia tmp = new KehitysAsia("", KehitysAsiaTyyppi.SAIRAUS);
    tmp.addProperty(new KKSProperty("oireet", ""));
    tmp.addProperty(new KKSProperty("hoito", ""));
    tmp.addProperty(new KKSProperty("laake", ""));
    tmp.addProperty(new KKSProperty("toimintasuunnitelma", ""));
    return tmp;
  }

  @ModelAttribute("ruokavalio")
  public KehitysAsia getCommandObject2() {
    log.debug("get ruokavalio command object");
    KehitysAsia tmp = new KehitysAsia("", KehitysAsiaTyyppi.ERIKOISRUOKAVALIO);
    tmp.addProperty(new KKSProperty("peruste", "ALLERGIA"));
    tmp.addProperty(new KKSProperty("kuvaus", ""));
    return tmp;
  }

  @ModelAttribute("kaynti")
  public KehitysAsia getCommandObject3() {
    log.debug("get kaynti command object");
    KehitysAsia tmp = new KehitysAsia("", KehitysAsiaTyyppi.KAYNTI);
    tmp.setMuokkaaja("Mikko Muokkaaja");
    tmp.setMuokkausPvm(new Date());
    tmp.addProperty(new KKSProperty("kaynti", "NEUVOLA"));
    tmp.addProperty(new KKSProperty("kuvaus", ""));
    return tmp;
  }

  @InitBinder("sairaus")
  public void initBinder(WebDataBinder binder) {
    log.debug("init binder");
    binder.registerCustomEditor(KehitysAsiaTyyppi.class,
        new KehitysAsiaTyyppiEditor(service));
  }

  @InitBinder("ruokavalio")
  public void initBinder2(WebDataBinder binder) {
    log.debug("init binder 2");
    binder.registerCustomEditor(KehitysAsiaTyyppi.class,
        new KehitysAsiaTyyppiEditor(service));
  }

  @InitBinder("kaynti")
  public void initBinder3(WebDataBinder binder) {
    log.debug("init binder 3");
    binder.registerCustomEditor(KehitysAsiaTyyppi.class,
        new KehitysAsiaTyyppiEditor(service));
  }

  @ActionMapping(params = "toiminto=lisaaSairaus")
  public void lisaa(@ModelAttribute(value = "sairaus") KehitysAsia tarve,
      @ModelAttribute(value = "lapsi") Henkilo lapsi,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {
    log.debug("lisaa sairaus");

    viimeistele(tarve, lapsi, response, sessionStatus);
  }

  @ActionMapping(params = "toiminto=lisaaRuokavalio")
  public void lisaaRuokavalio(
      @ModelAttribute(value = "ruokavalio") KehitysAsia tarve,
      @ModelAttribute(value = "lapsi") Henkilo lapsi,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {
    log.debug("lisaa ruokavalio");

    viimeistele(tarve, lapsi, response, sessionStatus);
  }

  @ActionMapping(params = "toiminto=lisaaKaynti")
  public void lisaaKaynti(@ModelAttribute(value = "kaynti") KehitysAsia tarve,
      @ModelAttribute(value = "lapsi") Henkilo lapsi,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {
    log.debug("lisaa kaynti");

    viimeistele(tarve, lapsi, response, sessionStatus);
  }

  private void viimeistele(KehitysAsia tarve, Henkilo lapsi,
      ActionResponse response, SessionStatus sessionStatus) {
    tarve.setMuokkaaja("Koodista muokkaaja");
    tarve.setMuokkausPvm(new Date());

    String id = ""
        + lapsi.getKks().getKehitystieto(KehitystietoTyyppi.TERVEYDEN_TILA)
            .getKehitysAsiat().values().size();
    tarve.setId(id);
    lapsi.getKks().getKehitystieto(KehitystietoTyyppi.TERVEYDEN_TILA)
        .addKehitysAsia(tarve);

    tarve = getCommandObject();
    response.setRenderParameter("toiminto", "naytaTerveys");
    response.setRenderParameter("hetu", lapsi.getHetu());
    sessionStatus.setComplete();
  }
}
