package com.ixonos.koku.kks;

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
import com.ixonos.koku.kks.mock.KKSService;
import com.ixonos.koku.kks.mock.Kehitystieto;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

@Controller(value = "neljavuotistarkastusController")
@RequestMapping(value = "VIEW")
public class NeljavuotistarkastusController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Logger log = LoggerFactory
      .getLogger(NeljavuotistarkastusController.class);

  @RenderMapping(params = "toiminto=naytaNeljavuotistarkastus")
  public String nayta(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      RenderResponse response, Model model) {
    log.info("nayta neljavuotistarkastus");
    model.addAttribute("lapsi", lapsi);

    Kehitystieto tieto = lapsi.getKks().getKehitystieto(
        KehitystietoTyyppi.NELJA_VUOTISTARKASTUS);
    // model.addAttribute("neljavuotistiedot",
    // tieto == null ?
    // new ArrayList<KehitysAsia>() :
    // tieto.getKehitysAsiat().values());

    return "neljavuotistarkastus";

  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam String hetu) {
    log.info("getLapsi");
    return service.getChild(hetu);
  }
}
