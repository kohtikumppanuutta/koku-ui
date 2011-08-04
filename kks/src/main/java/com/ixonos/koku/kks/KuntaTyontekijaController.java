package com.ixonos.koku.kks;

import java.util.ArrayList;
import java.util.List;

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

import com.ixonos.koku.kks.malli.DemoService;
import com.ixonos.koku.kks.malli.Henkilo;

@Controller(value = "kuntaTyontekijaController")
@RequestMapping(value = "VIEW")
public class KuntaTyontekijaController {

  @Autowired
  @Qualifier("demoKksService")
  private DemoService demoService;

  private static Logger log = LoggerFactory
      .getLogger(KuntaTyontekijaController.class);

  @RenderMapping(params = "toiminto=naytaTyontekija")
  public String nayta(RenderResponse response,
      @RequestParam("lapset") String[] childs,
      @RequestParam(value = "haku", required = false) String haku, Model model) {
    log.info("nayta kuntatyontekija");
    model.addAttribute("lapset", toChilds(childs));

    if (haku != null) {
      model.addAttribute("haku", haku);
    }
    return "hae";
  }

  @ActionMapping(params = "toiminto=haeLapsi")
  public void haeLapsi(@ModelAttribute(value = "lapsi") Henkilo lapsi,
      BindingResult bindingResult, ActionResponse response,
      SessionStatus sessionStatus) {
    log.info("hae lapsi");

    response.setRenderParameter("toiminto", "naytaTyontekija");
    response.setRenderParameter("lapset",
        toArray(demoService.haeHenkilo(lapsi)));
    response.setRenderParameter("haku", "true");
    sessionStatus.setComplete();
  }

  @ModelAttribute("lapsi")
  public Henkilo getCommandObject() {
    log.debug("get entry command object");
    return new Henkilo();
  }

  private String[] toArray(List<Henkilo> lapset) {
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

  private List<Henkilo> toChilds(String[] childIds) {
    List<Henkilo> tmp = new ArrayList<Henkilo>();

    if (childIds != null) {
      for (String s : childIds) {
        if (!"".equals(s)) {
          tmp.add(demoService.haeLapsi(s));
        }
      }
    }
    return tmp;
  }

}
