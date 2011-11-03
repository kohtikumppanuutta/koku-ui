/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.pyh.controller;

import java.util.HashMap;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.portlet.filter.userinfo.UserInfoUtils;
import fi.koku.pyh.model.CommunityRole;
import fi.koku.pyh.model.Dependant;
import fi.koku.pyh.model.DependantsAndFamily;
import fi.koku.pyh.model.Family;
import fi.koku.pyh.model.FamilyIdAndFamilyMembers;
import fi.koku.pyh.model.Person;
import fi.koku.pyh.ui.common.FamilyNotFoundException;
import fi.koku.pyh.ui.common.GuardianForChildNotFoundException;
import fi.koku.pyh.ui.common.PyhConstants;
import fi.koku.pyh.ui.common.PyhDemoService;
import fi.koku.services.entity.customer.v1.CustomerServiceFactory;
import fi.koku.services.entity.customer.v1.CustomerServicePortType;
import fi.koku.services.entity.customer.v1.CustomerType;

/**
 * Controller for user's family information editing view.
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
  
  private CustomerServicePortType customerService;
  
  public EditFamilyInformationController() {
    CustomerServiceFactory customerServiceFactory = new CustomerServiceFactory(PyhConstants.CUSTOMER_SERVICE_USER_ID, PyhConstants.CUSTOMER_SERVICE_PASSWORD, PyhConstants.CUSTOMER_SERVICE_ENDPOINT);
    customerService = customerServiceFactory.getCustomerService();
  }
  
  @RenderMapping(params = "action=editFamilyInformation")
  public String render(RenderRequest request, Model model) {
    String userPic = UserInfoUtils.getPicFromSession(request);
    
    // TODO: hae perhe ja huoltajuusyhteisöt vain kertaalleen
    
    CustomerType customer;
    fi.koku.services.entity.customer.v1.AuditInfoType customerAuditInfoType = new fi.koku.services.entity.customer.v1.AuditInfoType();
    customerAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    customerAuditInfoType.setUserId(userPic);
    
    try {
      customer = customerService.opGetCustomer(userPic, customerAuditInfoType);
    } catch (fi.koku.services.entity.customer.v1.ServiceFault fault) {
      log.error("EditFamilyInformationController.render: opGetCustomer raised a ServiceFault", fault);
      return null;
    }
    
    log.debug("EditFamilyInformationController.render(): returning customer: " + customer.getEtunimetNimi() + " " + customer.getSukuNimi() + ", " + customer.getHenkiloTunnus());
    log.debug("--");
    Person user = new Person(customer);
    
    DependantsAndFamily daf = pyhDemoService.getDependantsAndFamily(userPic);
    FamilyIdAndFamilyMembers fidm = pyhDemoService.getOtherFamilyMembers(userPic);
    
    model.addAttribute("user", user);
    model.addAttribute("dependants", daf.getDependants());
    model.addAttribute("otherFamilyMembers", fidm.getFamilyMembers());
    model.addAttribute("parentsFull", pyhDemoService.isParentsSet(userPic));
    model.addAttribute("messages", pyhDemoService.getSentMessages(user));
    model.addAttribute("searchedUsers", null);
    
    Boolean childsGuardianshipInformationNotFound = Boolean.valueOf(request.getParameter("childsGuardianshipInformationNotFound"));
    
    // if child's guardianship information is not found show a notification in JSP
    model.addAttribute("childsGuardianshipInformationNotFound", childsGuardianshipInformationNotFound.booleanValue());
    
    // create user family community if does not exist
    if (daf.getFamily() == null) {
      pyhDemoService.addFamily(userPic);
    }
    
    return "editfamilyinformation";
  }
  
  // this render method does not clear the search results
  @RenderMapping(params = "action=editFamilyInformationWithSearchResults")
  public String renderWithSearchResults(RenderRequest request, Model model) {
    String userPic = UserInfoUtils.getPicFromSession(request);
    
    String surname = request.getParameter("surname");
    String pic = request.getParameter("pic");
    List<Person> searchedUsers = pyhDemoService.searchUsers(surname, pic, userPic);
    
    CustomerType customer;
    fi.koku.services.entity.customer.v1.AuditInfoType customerAuditInfoType = new fi.koku.services.entity.customer.v1.AuditInfoType();
    customerAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    customerAuditInfoType.setUserId(userPic);
    
    try {
      customer = customerService.opGetCustomer(userPic, customerAuditInfoType);
    } catch (fi.koku.services.entity.customer.v1.ServiceFault fault) {
      log.error("EditFamilyInformationController.render: opGetCustomer raised a ServiceFault", fault);
      return null;
    }
    
    log.debug("EditFamilyInformationController.render(): returning customer: " + customer.getEtunimetNimi() + " " + customer.getSukuNimi() + ", " + customer.getHenkiloTunnus());
    log.debug("--");
    Person user = new Person(customer);
    
    DependantsAndFamily daf = pyhDemoService.getDependantsAndFamily(userPic);
    FamilyIdAndFamilyMembers fidm = pyhDemoService.getOtherFamilyMembers(userPic);
    
    request.setAttribute("search", true);
    model.addAttribute("user", user);
    model.addAttribute("dependants", daf.getDependants());
    model.addAttribute("otherFamilyMembers", fidm.getFamilyMembers());
    model.addAttribute("parentsFull", pyhDemoService.isParentsSet(userPic));
    model.addAttribute("messages", pyhDemoService.getSentMessages(user));
    model.addAttribute("searchedUsers", searchedUsers);
    
    // user family shouldn't be null because it was created in the other render method already
    Family family = daf.getFamily();
    String familyCommunityId = family.getCommunityId();
    model.addAttribute("familyCommunityId", familyCommunityId);
    
    return "editfamilyinformation";
  }
  
  @ActionMapping(params = "action=addDependantAsFamilyMember")
  public void addDependantAsFamilyMember(ActionRequest request, @RequestParam String dependantPic, ActionResponse response) {
    String userPic = UserInfoUtils.getPicFromSession(request);
    
    pyhDemoService.insertDependantToFamily(userPic, dependantPic, CommunityRole.CHILD);
    response.setRenderParameter("action", "editFamilyInformation");
  }

  @ActionMapping(params = "action=removeFamilyMember")
  public void removeFamilyMember(ActionRequest request, @RequestParam String familyMemberPic, ActionResponse response) {
    String userPic = UserInfoUtils.getPicFromSession(request);
    
    pyhDemoService.removeFamilyMember(familyMemberPic, userPic);
    response.setRenderParameter("action", "editFamilyInformation");
  }

  @ActionMapping(params = "action=removeDependant")
  public void removeDependant(ActionRequest request, @RequestParam String familyMemberPic, ActionResponse response) {
    String userPic = UserInfoUtils.getPicFromSession(request);
    
    for (Dependant d : pyhDemoService.getDependantsAndFamily(userPic).getDependants()) {
      if (d.getPic().equals(familyMemberPic)) {
        d.setMemberOfUserFamily(false);
      }
    }
    pyhDemoService.removeFamilyMember(familyMemberPic, userPic);
    response.setRenderParameter("action", "editFamilyInformation");
  }
  
  @ActionMapping(params = "action=searchUsers")
  public void searchUsers(ActionRequest request, ActionResponse response) {
    String surname = request.getParameter("searchSurname");
    String pic = request.getParameter("searchPic");
    
    // TODO: refactoring: this method will not be called but we call render method directly
    //pyhDemoService.searchUsers(surname, pic /*, userPic /*customer id == user pic*/, userPic);
    response.setRenderParameter("surname", surname);
    response.setRenderParameter("pic", pic);
    response.setRenderParameter("action", "editFamilyInformationWithSearchResults");
  }

  @ActionMapping(params = "action=addUsersToFamily")
  public void addUsersToFamily(ActionRequest request, ActionResponse response, PortletSession session) {
    String userPic = UserInfoUtils.getPicFromSession(request);
    
    //HashMap<String, String> parameterMap = new HashMap<String, String>();
    HashMap<String, String> personMap = new HashMap<String, String>();
    
    /*
    for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
      String param = e.nextElement();
      String paramValue = request.getParameter(param);
      log.info("param name: " + param + ", param value: " + paramValue);
      //if (param.startsWith("userPic") || param.startsWith("userRole")) {
      if ("userPic".equalsIgnoreCase(param)) {
        personPic = paramValue;
      } else if ("userRole".equalsIgnoreCase(param)) {
        personRole = paramValue;
      } else if ("familyCommunityId".equalsIgnoreCase(param)) {
        familyCommunityId = paramValue;
      }
    }
    */
    
    String familyCommunityId = request.getParameter("familyCommunityId");
    String personPic = request.getParameter("userPic");
    String personRole = request.getParameter("userRole");
    
    // TODO: remove
    log.info("ADDING PERSON: ");
    log.info("familyCommunityId: " + familyCommunityId);
    log.info("personPic: " + personPic);
    log.info("personRole: " + personRole);
    
    personMap.put(personPic, personRole);
    
    /*
    Set<String> keys = parameterMap.keySet();
    Iterator<String> si = keys.iterator();
    while (si.hasNext()) {
      String key = si.next();
      //if (key.startsWith("userPic")) {
      if ("userPic".equalsIgnoreCase(key)) {
        String[] tokens = key.split("_");
        // index is the number after '_' in parameter name, e.g. userPic_1
        // (index is 1)
        String index = tokens[1];

        personPic = parameterMap.get(key);
        personRole = parameterMap.get("userRole_" + index);
        personMap.put(personPic, personRole);
      }
    }
    */
    
    boolean childsGuardianshipInformationNotFound = false;
    try {
      pyhDemoService.addPersonsAsFamilyMembers(personMap, userPic, familyCommunityId);
    } catch (FamilyNotFoundException fnfe) {
      log.error("EditFamilyInformationController.addUsersToFamily() caught FamilyNotFoundException!", fnfe);
      // show general error page
      throw new RuntimeException(fnfe);
    } catch (GuardianForChildNotFoundException gnfe) {
      log.error("EditFamilyInformationController.addUsersToFamily() caught GuardianForChildNotFoundException!", gnfe);
      // show error message in JSP view
      childsGuardianshipInformationNotFound = true;
    }
    
    response.setRenderParameter("childsGuardianshipInformationNotFound", String.valueOf(childsGuardianshipInformationNotFound));
    response.setRenderParameter("action", "editFamilyInformation");
  }

}
