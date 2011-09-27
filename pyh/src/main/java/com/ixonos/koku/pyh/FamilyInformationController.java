package com.ixonos.koku.pyh;

import javax.portlet.PortletSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ixonos.koku.pyh.model.DependantsAndFamily;
import com.ixonos.koku.pyh.model.Family;
import com.ixonos.koku.pyh.model.Person;

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

//  @Autowired
//  @Qualifier(value = "pyhMessageService")
//  private MessageService messageService;

  @Autowired
  @Qualifier(value = "pyhDemoService")
  private PyhDemoService pyhDemoService;

  @RenderMapping
  public String render(PortletSession session, Model model) {
    String userPic = "";
    
    UserInfo userInfo = (UserInfo)session.getAttribute(UserInfo.KEY_USER_INFO);
    if (userInfo != null) {
      userPic = userInfo.getPic();
      log.info("Got PIC from session. userPic = " + userPic);
      
    } else {
      // TODO: mitä tehdään kun käyttäjää ei voida tunnistaa?
      log.error("ERROR: UserInfo returns no PIC!");
    }
    
    Person user = pyhDemoService.getUser(userPic);
    DependantsAndFamily daf = pyhDemoService.getDependantsAndFamily(userPic);
    model.addAttribute("user", user);
    model.addAttribute("dependants", daf.getDependants());
    model.addAttribute("otherFamilyMembers", pyhDemoService.getOtherFamilyMembers(userPic));
//    model.addAttribute("messages", messageService.getMessagesFor(userPic));
//    model.addAttribute("sentMessages", messageService.getSentMessages(userPic));
    model.addAttribute("messages", pyhDemoService.getMessagesFor(user));
    model.addAttribute("sentMessages", pyhDemoService.getSentMessages(user));
    return "familyinformation";
  }
  
}
