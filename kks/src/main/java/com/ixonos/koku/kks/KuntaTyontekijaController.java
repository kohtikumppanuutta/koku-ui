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
import com.ixonos.koku.kks.mock.KenttaHaku;
import com.ixonos.koku.kks.mock.Lapsi;

@Controller(value = "kuntaTyontekijaController")
@RequestMapping(value = "VIEW")
public class KuntaTyontekijaController {

  @Autowired
  @Qualifier("myKKSService")
  private KKSService service;

  private static Log log = LogFactory.getLog(KuntaTyontekijaController.class);

  @RenderMapping(params = "toiminto=naytaTyontekija")
  public String nayta(RenderResponse response,
      @RequestParam("lapset") String[] childs, Model model) {
    log.info("näytä kuntatyöntekijä");
    model.addAttribute("lapset", toChilds(childs));
    return "hae";
  }

  @ActionMapping(params = "toiminto=haeLapsi")
  public void haeLapsi(@ModelAttribute(value = "lapsi") Lapsi lapsi,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {
    log.info("hae lapsi");

    response.setRenderParameter("toiminto", "naytaTyontekija");
    response.setRenderParameter("lapset", toArray(service.searchChilds(lapsi)));
    sessionStatus.setComplete();
  }

  @ModelAttribute("lapsi")
  public Lapsi getCommandObject() {
    log.debug("get entry command object");
    return new Lapsi();
  }

  @ModelAttribute("haku")
  public KenttaHaku getSearchCommandObject() {
    log.debug("get search command object in add controller");
    return new KenttaHaku();
  }

  private String[] toArray(List<Lapsi> lapset) {
    String[] tmp = null;

    if (lapset.isEmpty()) {
      tmp = new String[] { "" };
    } else {
      tmp = new String[lapset.size()];
    }

    for (int i = 0; i < lapset.size(); i++) {
      tmp[i] = lapset.get(i).getHetu();
    }
    return tmp;
  }

  private List<Lapsi> toChilds(String[] childIds) {
    List<Lapsi> tmp = new ArrayList<Lapsi>();

    if (childIds != null) {
      for (String s : childIds) {
        if (!"".equals(s)) {
          tmp.add(service.getChild(s));
        }
      }
    }
    return tmp;
  }

}
