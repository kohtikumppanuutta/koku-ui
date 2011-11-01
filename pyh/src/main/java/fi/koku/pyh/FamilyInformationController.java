/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.pyh;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.portlet.filter.userinfo.UserInfo;
import fi.koku.portlet.filter.userinfo.UserInfoUtils;
import fi.koku.pyh.model.DependantsAndFamily;
import fi.koku.pyh.model.FamilyIdAndFamilyMembers;
import fi.koku.pyh.model.Person;

@Controller(value = "familyInformationController")
@RequestMapping(value = "VIEW")
public class FamilyInformationController {

  private static Logger log = LoggerFactory.getLogger(FamilyInformationController.class);

  @Autowired
  @Qualifier(value = "pyhDemoService")
  private PyhDemoService pyhDemoService;

  @RenderMapping
  public String render(Model model, RenderRequest request) {
    String userPic = UserInfoUtils.getPicFromSession(request);
    
    // vetuma test -->
//    if (userInfo.isStrongAuthenticationEnabled() && !userInfo.hasStrongAuthentication()) {
//      String authURL = (String)request.getPortletSession().getAttribute(UserInfo.KEY_VETUMA_AUTHENTICATION_URL);
//      model.addAttribute("authURL", authURL);
//    }
//    model.addAttribute("hasStrongAuthentication", userInfo.hasStrongAuthentication());
//    model.addAttribute("isStrongAuthenticationEnabled", userInfo.isStrongAuthenticationEnabled());
    // <-- vetuma test
    
    Person user = pyhDemoService.getUser(userPic);
    DependantsAndFamily daf = pyhDemoService.getDependantsAndFamily(userPic);
    FamilyIdAndFamilyMembers fidm = pyhDemoService.getOtherFamilyMembers(userPic);
    
    model.addAttribute("user", user);
    model.addAttribute("dependants", daf.getDependants());
    model.addAttribute("otherFamilyMembers", fidm.getFamilyMembers());
    model.addAttribute("currentFamilyId", fidm.getFamilyId());
    model.addAttribute("messages", pyhDemoService.getMessagesFor(user));
    model.addAttribute("sentMessages", pyhDemoService.getSentMessages(user));
    
    model.addAttribute("supportEmailAddress", PyhConstants.KOKU_SUPPORT_EMAIL_ADDRESS);
    
    return "familyinformation";
  }
  
}
