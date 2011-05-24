package com.ixonos.koku.kks;

import java.util.Date;

import javax.portlet.ActionResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import com.ixonos.koku.kks.mock.Henkilo;
import com.ixonos.koku.kks.mock.KKSProperty;
import com.ixonos.koku.kks.mock.KKSService;
import com.ixonos.koku.kks.mock.KehitysAsia;
import com.ixonos.koku.kks.utils.enums.KehitysAsiaTyyppi;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

@Controller(value = "lisaaTukitarveController")
@RequestMapping(value = "VIEW")
public class LisaaTukitarveController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Logger log = LoggerFactory
      .getLogger(LisaaTukitarveController.class);

  @ActionMapping(params = "toiminto=lisaaTukitarve")
  public void lisaaTukitarve(
      @ModelAttribute(value = "tarve") KehitysAsia tarve,
      @ModelAttribute(value = "lapsi") Henkilo lapsi,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {
    log.debug("lisaaTukitarve");

    tarve.setMuokkaaja("Koodista muokkaaja");
    tarve.setMuokkausPvm(new Date());

    String id = ""
        + lapsi.getKks().getKehitystieto(KehitystietoTyyppi.TUKITARVE)
            .getKehitysAsiat().values().size();
    tarve.setId(id);
    lapsi.getKks().getKehitystieto(KehitystietoTyyppi.TUKITARVE)
        .addKehitysAsia(tarve);

    tarve = getCommandObject();
    response.setRenderParameter("toiminto", "naytaTukitoimet");
    response.setRenderParameter("hetu", lapsi.getHetu());
    sessionStatus.setComplete();
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
