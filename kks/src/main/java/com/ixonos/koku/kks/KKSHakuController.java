package com.ixonos.koku.kks;

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
import com.ixonos.koku.kks.mock.KenttaHaku;
import com.ixonos.koku.kks.mock.Lapsi;
import com.ixonos.koku.kks.utils.KKSKenttaEditor;
import com.ixonos.koku.kks.utils.enums.KKSKentta;

@Controller(value = "kksHakuController")
@RequestMapping(value = "VIEW")
public class KKSHakuController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Log log = LogFactory.getLog(KKSHakuController.class);

  @ActionMapping(params = "toiminto=hae")
  public void hae(@ModelAttribute(value = "haku") KenttaHaku search,
      @ModelAttribute(value = "lapsi") Lapsi lapsi,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {

    response.setRenderParameter("toiminto", "naytaLapsi");
    response.setRenderParameter("hetu", lapsi.getHetu());
    response.setRenderParameter("kentat", toArray(search));
    search.clear();
    sessionStatus.setComplete();
  }

  @InitBinder("haku")
  public void initBinder(WebDataBinder binder) {
    log.debug("init binder");
    binder.registerCustomEditor(KKSKentta.class, new KKSKenttaEditor(service));
  }

  @ModelAttribute("haku")
  public KenttaHaku getCommandObject() {
    log.debug("get kenttähaku");
    return new KenttaHaku();
  }

  @ModelAttribute("lapsi")
  public Lapsi getLapsi(@RequestParam String hetu) {
    log.debug("getLapsi");
    return service.getChild(hetu);
  }

  private String[] toArray(KenttaHaku search) {
    String[] tmp = null;

    if (search.getKentat() == null || search.getKentat().size() == 0) {
      tmp = new String[1];
      tmp[0] = search.getPaaKentta().getId();
    } else {

      boolean containsMain = search.getPaaKentta() == null
          || search.getKentat().contains(search.getPaaKentta());

      int arraySize = containsMain ? search.getKentat().size() : search
          .getKentat().size() + 1;

      tmp = new String[arraySize];

      if (!containsMain) {
        tmp[0] = search.getPaaKentta().getId();
      }
      for (int i = 0; i < search.getKentat().size(); i++) {
        tmp[i] = search.getKentat().get((i)).getId();
      }

      if (!containsMain) {
        tmp[arraySize - 1] = search.getPaaKentta().getId();
      }
    }
    return tmp;
  }
}
