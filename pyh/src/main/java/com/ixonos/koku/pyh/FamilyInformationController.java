package com.ixonos.koku.pyh;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;

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

import fi.koku.portlet.filter.userinfo.UserInfo;

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
  public String render(PortletSession session, Model model) {
    String userPic = "";
    
    UserInfo userInfo = (UserInfo)session.getAttribute(UserInfo.KEY_USER_INFO);
    if (userInfo != null) {
      userPic = userInfo.getPic();
    } else {
      // TODO: mitä tehdään kun käyttäjää ei voida tunnistaa?
    }
    
    model.addAttribute("user", pyhDemoService.getUser(userPic));
    model.addAttribute("dependants", pyhDemoService.getDependants(userPic));
    model.addAttribute("otherFamilyMembers", pyhDemoService.getOtherFamilyMembers(userPic));
    model.addAttribute("messages", messageService.getMessagesFor(userPic));
    model.addAttribute("sentMessages", messageService.getSentMessages(userPic));
    return "familyinformation";
  }
  
  // TODO: remove when not needed anymore
  @RenderMapping(params = "action=userSelection")
  public String render(@RequestParam String pic, Model model, PortletSession session) {
    String userPic = "";
    
    UserInfo userInfo = (UserInfo)session.getAttribute(UserInfo.KEY_USER_INFO);
    if (userInfo != null) {
      userPic = userInfo.getPic();
    } else {
      // TODO: mitä tehdään kun käyttäjää ei voida tunnistaa?
    }
    
    model.addAttribute("user", pyhDemoService.getUser(userPic));
    model.addAttribute("dependants", pyhDemoService.getDependants(userPic));
    model.addAttribute("otherFamilyMembers", pyhDemoService.getOtherFamilyMembers(userPic));
    model.addAttribute("messages", messageService.getMessagesFor(userPic));
    model.addAttribute("sentMessages", messageService.getSentMessages(userPic));
    return "familyinformation";
  }
  
}
