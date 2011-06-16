package com.ixonos.koku.kks;

import javax.portlet.RenderRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ixonos.koku.kks.malli.DemoService;
import com.ixonos.koku.kks.utils.Vakiot;

@Controller("mainController")
@RequestMapping(value = "VIEW")
public class MainController {

  @Autowired
  @Qualifier("demoKksService")
  private DemoService demoService;

  private static Logger log = LoggerFactory.getLogger(MainController.class);

  @RenderMapping
  public String render(RenderRequest req, Model model) {

    // if (!demoService.onkoLuotu())
    demoService.luo("");
    return "valitse";
  }

  @RenderMapping(params = "toiminto=naytaLuokittelu")
  public String naytaLuokittelu(RenderRequest req, Model model) {

    model.addAttribute("kokoelmaTyypit", demoService.haeKokoelmaTyypit());
    model.addAttribute("kehitysAsialajit", Vakiot.kehitysasiaLajit());
    model.addAttribute("luokitukset", Vakiot.luokittelut());
    return "luokitukset";
  }

}
