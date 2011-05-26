package com.ixonos.koku.kks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.ixonos.koku.kks.mock.Aktivointi;
import com.ixonos.koku.kks.mock.Henkilo;
import com.ixonos.koku.kks.mock.KKSService;
import com.ixonos.koku.kks.mock.KehitysTietoTila;
import com.ixonos.koku.kks.mock.Kehitystieto;
import com.ixonos.koku.kks.utils.KehitystietoTyyppiEditor;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;
import com.ixonos.koku.kks.utils.enums.Tila;

@Controller(value = "aktivoiKehitystietoController")
@RequestMapping(value = "VIEW")
public class AktivoiKehitystietoController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Logger log = LoggerFactory
      .getLogger(AktivoiKehitystietoController.class);

  @InitBinder("aktivointi")
  public void initBinder(WebDataBinder binder) {
    log.debug("init binder");
    binder.registerCustomEditor(KehitystietoTyyppi.class,
        new KehitystietoTyyppiEditor(service));
  }

  @ActionMapping(params = "toiminto=aktivoiKehitystieto")
  public void aktivoi(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      @ModelAttribute(value = "aktivointi") Aktivointi aktivointi,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {
    log.debug("lapsenTietoihin");

    Kehitystieto kt = lapsi.getKks().getKehitystieto(
        aktivointi.getAktivoitavaKentta());

    if (kt != null) {
      KehitysTietoTila tila = kt.getTila();
      tila.setTila(Tila.AKTIIVINEN);

      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

      try {

        if (aktivointi.getAlkaa() == null
            || "".equals(aktivointi.getAlkaa().trim())) {
          tila.setAlkuPvm(new Date());
        } else {
          tila.setAlkuPvm(dateFormat.parse(aktivointi.getAlkaa()));
        }
        if (aktivointi.getAlkaa() == null
            || "".equals(aktivointi.getLoppuu().trim())) {
          tila.setLoppuPvm(new Date());
        } else {

          tila.setLoppuPvm(dateFormat.parse(aktivointi.getLoppuu()));
        }
      } catch (ParseException e) {
      }

    }
    response.setRenderParameter("toiminto", "naytaLapsi");
    response.setRenderParameter("hetu", lapsi.getHetu());
    aktivointi = getCommandObject();
    aktivointi.setAlkaa("");
    aktivointi.setLoppuu("");
    sessionStatus.setComplete();
  }

  @ModelAttribute("lapsi")
  public Henkilo getLapsi(@RequestParam String hetu) {
    log.info("getLapsi");
    return service.getChild(hetu);
  }

  @ModelAttribute("aktivointi")
  public Aktivointi getCommandObject() {
    log.debug("get aktivointi command object");
    return new Aktivointi();
  }
}
