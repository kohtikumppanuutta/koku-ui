package com.ixonos.koku.kks;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import com.ixonos.koku.kks.mock.KKSService;
import com.ixonos.koku.kks.mock.Kehitystieto;
import com.ixonos.koku.kks.mock.KenttaHaku;
import com.ixonos.koku.kks.mock.Lapsi;
import com.ixonos.koku.kks.utils.enums.KKSKentta;
import com.ixonos.koku.kks.utils.enums.UIField;

@Controller(value = "lapsiController")
@RequestMapping(value = "VIEW")
public class LapsiController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Log log = LogFactory.getLog(LapsiController.class);

  @ActionMapping(params = "toiminto=lapsenTietoihin")
  public void lapsenTietoihin(@ModelAttribute(value = "lapsi") Lapsi lapsi,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {
    log.debug("lapsenTietoihin");

    response.setRenderParameter("toiminto", "naytaLapsi");
    response.setRenderParameter("hetu", lapsi.getHetu());
    response.setRenderParameter("kentat", UIField.ALL.toArray());
    sessionStatus.setComplete();
  }

  @RenderMapping(params = "toiminto=naytaLapsi")
  public String nayta(@ModelAttribute(value = "lapsi") Lapsi lapsi,
      @RequestParam("kentat") String[] fields, RenderResponse response,
      Model model) {
    log.info("näytä lapsi");
    model.addAttribute("lapsi", lapsi);
    model.addAttribute("kentat",
        service.searchEntries(lapsi, toKKSFields(fields)));
    return "lapsi";
  }

  @ModelAttribute("lapsi")
  public Lapsi getLapsi(@RequestParam String hetu) {
    log.info("getLapsi");
    return service.getChild(hetu);
  }

  @ModelAttribute("tieto")
  public Kehitystieto getCommandObject() {
    log.debug("get entry command object");
    return new Kehitystieto();
  }

  @ModelAttribute("haku")
  public KenttaHaku getKenttaHaku() {
    return new KenttaHaku();
  }

  private List<KKSKentta> toKKSFields(String[] fields) {
    List<KKSKentta> tmp = new ArrayList<KKSKentta>();

    for (String s : fields) {
      tmp.add(service.getField(s));
    }
    return tmp;
  }
}
