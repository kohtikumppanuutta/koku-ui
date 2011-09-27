package com.ixonos.koku.pyh;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//@Controller(value = "EditController")
//@RequestMapping(value = "EDIT")
//public class EditController {
//
//  @Autowired
//  @Qualifier("pyhDemoService")
//  private PyhDemoService demoService;
//
//  @Autowired
//  @Qualifier(value = "pyhMessageService")
//  private MessageService messageService;
//
//  private static Logger log = LoggerFactory.getLogger(EditController.class);
//
//  @RenderMapping
//  public String render(RenderRequest req, @RequestParam(value = "note", required = false) String note, Model model) {
//    if (note != null) {
//      model.addAttribute("note", "Malli resetoitu");
//    }
//    return "edit";
//  }
//
//  @RenderMapping(params = "action=reset")
//  public String naytaLuokittelu(RenderRequest req, @RequestParam(value = "note", required = false) String note,
//      Model model) {
//    //demoService.reset();
//    messageService.reset();
//    if (note != null) {
//      model.addAttribute("note", "Malli resetoitu");
//    }
//    return "edit";
//  }
//}
