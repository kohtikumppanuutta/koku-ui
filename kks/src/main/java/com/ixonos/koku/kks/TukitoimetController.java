package com.ixonos.koku.kks;

import java.util.ArrayList;

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

import com.ixonos.koku.kks.mock.Henkilo;
import com.ixonos.koku.kks.mock.KKSService;
import com.ixonos.koku.kks.mock.KehitysAsia;
import com.ixonos.koku.kks.mock.Kehitystieto;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

@Controller(value = "tukitoimetController")
@RequestMapping(value = "VIEW")
public class TukitoimetController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Logger log = LoggerFactory
      .getLogger(TukitoimetController.class);

  @ActionMapping(params = "toiminto=tukitoimet")
  public void lapsenTietoihin(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {
    log.debug("tukitoimet");

    response.setRenderParameter("toiminto", "naytaTukitoimet");
    response.setRenderParameter("hetu", lapsi.getHetu());
    sessionStatus.setComplete();
  }

  @RenderMapping(params = "toiminto=naytaTukitoimet")
  public String nayta(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      RenderResponse response, Model model) {
    log.info("näytä tukitoimet");
    model.addAttribute("lapsi", lapsi);

    Kehitystieto tieto = lapsi.getKks().getKehitystieto(
        KehitystietoTyyppi.TUKITARVE);
    model.addAttribute("tukitarpeet",
        tieto == null ? new ArrayList<KehitysAsia>() : tieto.getKehitysAsiat()
            .values());
    return "tukitarpeet";
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam String hetu) {
    log.info("getLapsi");
    return service.getChild(hetu);
  }

}
