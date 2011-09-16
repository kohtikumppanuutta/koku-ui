package com.ixonos.koku.pyh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ixonos.koku.pyh.model.MessageService;

/**
 * Controller for viewing user's family information. This is the controller for
 * the main view of PYH portlet.
 * 
 * @author hurulmi
 * 
 */

@Controller(value = "familyInformationController")
@RequestMapping(value = "VIEW")
public class FamilyInformationController {

  private static Logger log = LoggerFactory.getLogger(FamilyInformationController.class);

  @Autowired
  @Qualifier(value = "pyhMessageService")
  private MessageService messageService;

  @Autowired
  @Qualifier(value = "pyhDemoService")
  private PyhDemoService pyhDemoService;

  @RenderMapping(params = "action=guardianFamilyInformation")
  public String render(Model model) {

    String pic = pyhDemoService.getUserPic();
    model.addAttribute("user", pyhDemoService.getUser());
    model.addAttribute("dependants", pyhDemoService.getDependants());
    model.addAttribute("otherFamilyMembers", pyhDemoService.getOtherFamilyMembers());
    model.addAttribute("messages", messageService.getMessagesFor(pic));
    model.addAttribute("sentMessages", messageService.getSentMessages(pic));
    return "familyinformation";
  }
  
  @RenderMapping(params = "action=userSelection")
  public String render(@RequestParam String pic, Model model) {
    model.addAttribute("user", pyhDemoService.getUser());
    model.addAttribute("dependants", pyhDemoService.getDependants());
    model.addAttribute("otherFamilyMembers", pyhDemoService.getOtherFamilyMembers());
    model.addAttribute("messages", messageService.getMessagesFor(pic));
    model.addAttribute("sentMessages", messageService.getSentMessages(pic));
    return "familyinformation";
  }

}
