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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ixonos.koku.kks.mock.Henkilo;
import com.ixonos.koku.kks.mock.KKSService;
import com.ixonos.koku.kks.mock.KehitysAsia;
import com.ixonos.koku.kks.mock.Kehitystieto;
import com.ixonos.koku.kks.utils.KehitysAsiaTyyppiEditor;
import com.ixonos.koku.kks.utils.enums.KehitysAsiaTyyppi;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

@Controller(value = "ruokavalioController")
@RequestMapping(value = "VIEW")
public class RuokavalioController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Logger log = LoggerFactory
      .getLogger(RuokavalioController.class);

  @RenderMapping(params = "toiminto=naytaRuokavalio")
  public String nayta(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @ModelAttribute(value = "ruokavalio") KehitysAsia kehitys,
      @RequestParam(value = "aktiivinen") String aktiivinen,
      RenderResponse response, Model model) {
    log.info("nayta ruokavalio");
    model.addAttribute("lapsi", lapsi);
    model.addAttribute("ruokavalio", kehitys);
    model.addAttribute("aktiivinen", aktiivinen.toString());
    return "erikoisruokavalio";
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam String hetu) {
    log.info("getLapsi");
    return service.getChild(hetu);
  }

  @ModelAttribute("ruokavalio")
  public KehitysAsia getCommandObject(
      @RequestParam(value = "ruokavalio") String kehitysAsia,
      @RequestParam(value = "hetu") String hetu) {
    log.debug("get kehitys asia command object");

    Henkilo lapsi = service.getChild(hetu);
    return lapsi.getKks().getKehitystieto(KehitystietoTyyppi.TERVEYDEN_TILA)
        .getKehitysAsia(kehitysAsia);
  }

  @InitBinder("ruokavalio")
  public void initBinder(WebDataBinder binder) {
    log.debug("init binder");
    binder.registerCustomEditor(KehitysAsiaTyyppi.class,
        new KehitysAsiaTyyppiEditor(service));
  }

  @ActionMapping(params = "toiminto=muokkaaRuokavaliota")
  public void muokkaa(@ModelAttribute(value = "ruokavalio") KehitysAsia tarve,
      @ModelAttribute(value = "lapsi") Henkilo lapsi,
      @RequestParam(value = "vanha") String vanha,
      @RequestParam(value = "aktiivinen") String aktiivinen,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {
    log.debug("muokkaa ruokavaliota");

    tarve.setMuokkaaja("Koodista muokkaaja");
    tarve.setMuokkausPvm(new Date());

    Kehitystieto t = lapsi.getKks().getKehitystieto(
        KehitystietoTyyppi.TERVEYDEN_TILA);
    t.removeKehitysAsia(vanha);
    t.addKehitysAsia(tarve);

    response.setRenderParameter("toiminto", "naytaTerveys");
    response.setRenderParameter("hetu", lapsi.getHetu());
    response.setRenderParameter("aktiivinen", "" + aktiivinen.toString());
    sessionStatus.setComplete();
  }

}
