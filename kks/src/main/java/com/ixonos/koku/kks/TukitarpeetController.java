package com.ixonos.koku.kks;

import java.util.ArrayList;

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
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ixonos.koku.kks.mock.Henkilo;
import com.ixonos.koku.kks.mock.KKSProperty;
import com.ixonos.koku.kks.mock.KKSService;
import com.ixonos.koku.kks.mock.KehitysAsia;
import com.ixonos.koku.kks.mock.Kehitystieto;
import com.ixonos.koku.kks.utils.enums.KehitysAsiaTyyppi;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

@Controller(value = "tukitoimetController")
@RequestMapping(value = "VIEW")
public class TukitarpeetController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Logger log = LoggerFactory
      .getLogger(TukitarpeetController.class);

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

  @ModelAttribute("tarve")
  public KehitysAsia getCommandObject() {
    log.debug("get tarve command object");
    KehitysAsia tmp = new KehitysAsia("", KehitysAsiaTyyppi.TUKITARVE);
    tmp.addProperty(new KKSProperty("kuvaus", ""));
    tmp.addProperty(new KKSProperty("tehtavat", ""));
    return tmp;
  }

}
