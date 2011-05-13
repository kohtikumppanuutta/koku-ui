package com.ixonos.koku.kks;

import java.util.Date;

import javax.portlet.ActionResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import com.ixonos.koku.kks.mock.KKSService;
import com.ixonos.koku.kks.mock.Kehitystieto;
import com.ixonos.koku.kks.mock.KenttaHaku;
import com.ixonos.koku.kks.mock.Lapsi;
import com.ixonos.koku.kks.utils.KKSKenttaEditor;
import com.ixonos.koku.kks.utils.enums.KKSKentta;
import com.ixonos.koku.kks.utils.enums.UIField;

@Controller(value = "lisaaKKSController")
@RequestMapping(value = "VIEW")
public class LisaaKKSController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Log log = LogFactory.getLog(LisaaKKSController.class);

  @ActionMapping(params = "toiminto=lisaaTieto")
  public void lisaaTieto(@ModelAttribute(value = "tieto") Kehitystieto tieto,
      @ModelAttribute(value = "lapsi") Lapsi lapsi,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {
    log.debug("lisaaTieto");
    tieto.setLapsi(lapsi);
    tieto.setPvm(new Date(System.currentTimeMillis()));
    service.addEntry(tieto);

    response.setRenderParameter("toiminto", "naytaLapsi");
    response.setRenderParameter("hetu", lapsi.getHetu());
    response.setRenderParameter("kentat", UIField.ALL.toArray());
    sessionStatus.setComplete();
  }

  @InitBinder("tieto")
  public void initBinder(WebDataBinder binder) {
    log.debug("init binder");
    binder.registerCustomEditor(KKSKentta.class, new KKSKenttaEditor(service));
  }

  @ModelAttribute("tieto")
  public Kehitystieto getCommandObject() {
    log.debug("get entry command object");
    return new Kehitystieto();
  }

  @ModelAttribute("lapsi")
  public Lapsi getLapsi(@RequestParam String hetu) {
    log.debug("getLapsi");
    return service.getChild(hetu);
  }

  @ModelAttribute("haku")
  public KenttaHaku getSearchCommandObject() {
    log.debug("get search command");
    return new KenttaHaku();
  }
}
