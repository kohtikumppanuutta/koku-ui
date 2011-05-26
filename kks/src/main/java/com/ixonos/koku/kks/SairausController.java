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

@Controller(value = "sairausController")
@RequestMapping(value = "VIEW")
public class SairausController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Logger log = LoggerFactory.getLogger(SairausController.class);

  @RenderMapping(params = "toiminto=naytaSairaus")
  public String nayta(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @ModelAttribute(value = "sairaus") KehitysAsia kehitys,
      @RequestParam(value = "aktiivinen") String aktiivinen,
      RenderResponse response, Model model) {
    log.info("nayta sairaus");
    model.addAttribute("lapsi", lapsi);
    model.addAttribute("sairaus", kehitys);
    model.addAttribute("aktiivinen", aktiivinen.toString());
    return "sairaus";
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam String hetu) {
    log.info("getLapsi");
    return service.getChild(hetu);
  }

  @ModelAttribute("sairaus")
  public KehitysAsia getCommandObject(
      @RequestParam(value = "sairaus") String kehitysAsia,
      @RequestParam(value = "hetu") String hetu) {
    log.debug("get sairaus command object");

    Henkilo lapsi = service.getChild(hetu);
    return lapsi.getKks().getKehitystieto(KehitystietoTyyppi.TERVEYDEN_TILA)
        .getKehitysAsia(kehitysAsia);
  }

  @InitBinder("sairaus")
  public void initBinder(WebDataBinder binder) {
    log.debug("init binder");
    binder.registerCustomEditor(KehitysAsiaTyyppi.class,
        new KehitysAsiaTyyppiEditor(service));
  }

  @ActionMapping(params = "toiminto=muokkaaSairautta")
  public void muokkaa(@ModelAttribute(value = "sairaus") KehitysAsia tarve,
      @ModelAttribute(value = "lapsi") Henkilo lapsi,
      @RequestParam(value = "vanha") String vanha, BindingResult bindingResult,
      @RequestParam(value = "aktiivinen") String aktiivinen,
      ActionResponse response, SessionStatus sessionStatus) {
    log.debug("muokkaa sairautta");

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
