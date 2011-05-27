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

@Controller(value = "KayntiController")
@RequestMapping(value = "VIEW")
public class KayntiController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Logger log = LoggerFactory.getLogger(KayntiController.class);

  @RenderMapping(params = "toiminto=naytaKaynti")
  public String nayta(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @ModelAttribute(value = "kaynti") KehitysAsia kehitys,
      @RequestParam(value = "aktiivinen") String aktiivinen,
      RenderResponse response, Model model) {
    log.info("nayta ruokavalio");
    model.addAttribute("lapsi", lapsi);
    model.addAttribute("kaynti", kehitys);
    model.addAttribute("aktiivinen", aktiivinen);
    return "kaynti";
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam String hetu) {
    log.info("getLapsi");
    return service.haeLapsi(hetu);
  }

  @ModelAttribute("kaynti")
  public KehitysAsia getCommandObject(
      @RequestParam(value = "kaynti") String kehitysAsia,
      @RequestParam(value = "hetu") String hetu) {
    log.debug("get kaynti command object");

    Henkilo lapsi = service.haeLapsi(hetu);
    return lapsi.getKks().getKehitystieto(KehitystietoTyyppi.TERVEYDEN_TILA)
        .getKehitysAsia(kehitysAsia);
  }

  @InitBinder("kaynti")
  public void initBinder(WebDataBinder binder) {
    log.debug("init binder");
    binder.registerCustomEditor(KehitysAsiaTyyppi.class,
        new KehitysAsiaTyyppiEditor(service));
  }

  @ActionMapping(params = "toiminto=muokkaaKayntia")
  public void muokkaa(@ModelAttribute(value = "kaynti") KehitysAsia tarve,
      @ModelAttribute(value = "lapsi") Henkilo lapsi,
      @RequestParam(value = "vanha") String vanha,
      @RequestParam(value = "aktiivinen") String aktiivinen,
      ActionResponse response, SessionStatus sessionStatus) {
    log.debug("muokkaa kayntia");

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
