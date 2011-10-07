package com.ixonos.koku.pyh;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ixonos.koku.pyh.model.Dependant;
import com.ixonos.koku.pyh.model.DependantsAndFamily;
import com.ixonos.koku.pyh.model.Family;
import com.ixonos.koku.pyh.model.Person;
import com.ixonos.koku.pyh.util.CommunityRole;

import fi.koku.portlet.filter.userinfo.UserInfo;

/**
 * Controller for editing user's family information.
 * 
 * @author hurulmi
 * 
 */

@Controller(value = "editFamilyInformationController")
@RequestMapping(value = "VIEW")
public class EditFamilyInformationController {

  private static Logger log = LoggerFactory.getLogger(EditFamilyInformationController.class);

  @Autowired
  @Qualifier(value = "pyhDemoService")
  private PyhDemoService pyhDemoService;

  @RenderMapping(params = "action=editFamilyInformation")
  public String render(Model model, PortletSession session) {
    String userPic = "";
    
    UserInfo userInfo = (UserInfo)session.getAttribute(UserInfo.KEY_USER_INFO);
    if (userInfo != null) {
      userPic = userInfo.getPic();
    } else {
      // TODO: mitä tehdään kun käyttäjää ei voida tunnistaa?
      log.error("ERROR: UserInfo returns no PIC!");
    }
    
    Person user = pyhDemoService.getUser(userPic);
    DependantsAndFamily daf = pyhDemoService.getDependantsAndFamily(userPic);
    pyhDemoService.clearSearchedUsers();
    model.addAttribute("user", user);
    model.addAttribute("dependants", daf.getDependants());
    model.addAttribute("otherFamilyMembers", pyhDemoService.getOtherFamilyMembers(userPic));
    model.addAttribute("parentsFull", pyhDemoService.isParentsSet(userPic));
    model.addAttribute("messages", pyhDemoService.getSentMessages(user));
    
    // if child's guardianship information is not found show a notification in JSP
    model.addAttribute("childsGuardianshipInformationNotFound", pyhDemoService.getChildGuardianshipInformationNotFound());
    pyhDemoService.setChildsGuardianshipInformationNotFound(false);
    
    Family family = daf.getFamily();
    String communityId;
    if (family != null) {
       communityId = family.getCommunityId();
    } else {
      communityId = pyhDemoService.addFamily(userPic); // create a family community for user if does not exist
    }
    
    log.info("EditFamilyInformationController.render: put communityId in session: " + communityId);
    session.setAttribute("familyCommunityId", communityId);
    
    return "editfamilyinformation";
  }

  // this render method does not clear the search results
  @RenderMapping(params = "action=editFamilyInformationWithSearchResults")
  public String renderWithSearchResults(RenderRequest request, Model model, PortletSession session) {
    String userPic = "";
    
    UserInfo userInfo = (UserInfo)session.getAttribute(UserInfo.KEY_USER_INFO);
    if (userInfo != null) {
      userPic = userInfo.getPic();
    } else {
      // TODO: mitä tehdään kun käyttäjää ei voida tunnistaa?
      log.error("ERROR: UserInfo returns no PIC!");
    }
    
    Person user = pyhDemoService.getUser(userPic);
    DependantsAndFamily daf = pyhDemoService.getDependantsAndFamily(userPic);
    request.setAttribute("search", true);
    model.addAttribute("user", user);
    model.addAttribute("dependants", daf.getDependants());
    model.addAttribute("otherFamilyMembers", pyhDemoService.getOtherFamilyMembers(userPic));
    model.addAttribute("parentsFull", pyhDemoService.isParentsSet(userPic));
    model.addAttribute("messages", pyhDemoService.getSentMessages(user));

    return "editfamilyinformation";
  }
  
  @ModelAttribute(value = "searchedUsers")
  private List<Person> getSearchedUsers() {
    return pyhDemoService.getSearchedUsers();
  }
  
  @ActionMapping(params = "action=addDependantAsFamilyMember")
  public void addDependantAsFamilyMember(@RequestParam String dependantPic, ActionResponse response, PortletSession session) {
    String userPic = "";
    
    UserInfo userInfo = (UserInfo)session.getAttribute(UserInfo.KEY_USER_INFO);
    if (userInfo != null) {
      userPic = userInfo.getPic();
    } else {
      // TODO: mitä tehdään kun käyttäjää ei voida tunnistaa?
      log.error("ERROR: UserInfo returns no PIC!");
    }
    
    pyhDemoService.insertDependantToFamily(userPic, dependantPic, CommunityRole.CHILD);
    response.setRenderParameter("action", "editFamilyInformation");
  }

  @ActionMapping(params = "action=removeFamilyMember")
  public void removeFamilyMember(@RequestParam String familyMemberPic, ActionResponse response, PortletSession session) {
    String userPic = "";
    
    UserInfo userInfo = (UserInfo)session.getAttribute(UserInfo.KEY_USER_INFO);
    if (userInfo != null) {
      userPic = userInfo.getPic();
    } else {
      // TODO: mitä tehdään kun käyttäjää ei voida tunnistaa?
      log.error("ERROR: UserInfo returns no PIC!");
    }
    
    pyhDemoService.removeFamilyMember(familyMemberPic, userPic);
    response.setRenderParameter("action", "editFamilyInformation");
  }

  @ActionMapping(params = "action=removeDependant")
  public void removeDependant(@RequestParam String familyMemberPic, ActionResponse response, PortletSession session) {
    String userPic = "";
    
    UserInfo userInfo = (UserInfo)session.getAttribute(UserInfo.KEY_USER_INFO);
    if (userInfo != null) {
      userPic = userInfo.getPic();
    } else {
      // TODO: mitä tehdään kun käyttäjää ei voida tunnistaa?
      log.error("ERROR: UserInfo returns no PIC!");
    }
    
    for (Dependant d : pyhDemoService.getDependantsAndFamily(userPic).getDependants()) {
      if (d.getPic().equals(familyMemberPic)) {
        d.setMemberOfUserFamily(false);
      }
    }
    pyhDemoService.removeFamilyMember(familyMemberPic, userPic);
    response.setRenderParameter("action", "editFamilyInformation");
  }
  
  @ActionMapping(params = "action=searchUsers")
  public void searchUsers(ActionRequest request, ActionResponse response, PortletSession session) {
    String userPic = "";
    
    UserInfo userInfo = (UserInfo)session.getAttribute(UserInfo.KEY_USER_INFO);
    if (userInfo != null) {
      userPic = userInfo.getPic();
    } else {
      // TODO: mitä tehdään kun käyttäjää ei voida tunnistaa?
      log.error("ERROR: UserInfo returns no PIC!");
    }
    
    String sn = request.getParameter("searchSurname");
    String pic = request.getParameter("searchPic");
    
    // call service to query users,
    // users are returned as a model attribute object searchedUsers
    // TODO: what is the correct search criteria?
    pyhDemoService.searchUsers(sn, pic, userPic /*customer id == user pic*/, userPic);
    response.setRenderParameter("action", "editFamilyInformationWithSearchResults");
  }

  @ActionMapping(params = "action=addUsersToFamily")
  public void addUsersToFamily(ActionRequest request, ActionResponse response, PortletSession session) {
    String userPic = "";
    
    UserInfo userInfo = (UserInfo)session.getAttribute(UserInfo.KEY_USER_INFO);
    if (userInfo != null) {
      userPic = userInfo.getPic();
    } else {
      // TODO: mitä tehdään kun käyttäjää ei voida tunnistaa?
      log.error("ERROR: UserInfo returns no PIC!");
    }
    
    // TODO: test if this works; get community id (family community where to add users)
    String communityId = (String) session.getAttribute("familyCommunityId");
    log.info("EditFamilyInformationController.addUsersToFamily: get communityId from session: '" + communityId + "'");
    
    HashMap<String, String> parameterMap = new HashMap<String, String>();
    HashMap<String, String> personMap = new HashMap<String, String>();
    String personPic = "";
    String personRole = "";

    for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
      String param = e.nextElement();
      String paramValue = request.getParameter(param);
      if (param.startsWith("userPic") || param.startsWith("userRole")) {
        parameterMap.put(param, paramValue);
      }
    }

    Set<String> keys = parameterMap.keySet();
    Iterator<String> si = keys.iterator();
    while (si.hasNext()) {
      String key = si.next();
      if (key.startsWith("userPic")) {
        String[] tokens = key.split("_");
        // index is the number after '_' in parameter name, e.g. userPic_1
        // (index is 1)
        String index = tokens[1];

        personPic = parameterMap.get(key);
        personRole = parameterMap.get("userRole_" + index);
        personMap.put(personPic, personRole);
      }
    }

    try {
      pyhDemoService.addPersonsAsFamilyMembers(personMap, userPic, communityId);
    } catch (FamilyNotFoundException fnfe) {
      log.error("EditFamilyInformationController.addUsersToFamily() caught FamilyNotFoundException!");
      log.error(fnfe.getMessage());
    }
    
    response.setRenderParameter("action", "editFamilyInformation");
  }

}
