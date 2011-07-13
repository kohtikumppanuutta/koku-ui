package com.ixonos.koku.pyh;

import java.util.Enumeration;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ixonos.koku.pyh.mock.User;
import com.ixonos.koku.pyh.model.Child;
import com.ixonos.koku.pyh.model.Person;

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
  public String render() {
    return "editfamilyinformation";
  }
  
  @ModelAttribute(value = "user")
  private User getUser() {
    return pyhDemoService.getUser();
  }
  
  @ModelAttribute(value = "guardedChildren")
  private List<Child> getGuardiansChildren() {
    return pyhDemoService.getGuardiansChildren("guardian ssn here" /* TODO: get guardian by SSN */);
  }
  
  @ModelAttribute(value = "familyMembers")
  private List<Person> getFamilyMembers() {
    return pyhDemoService.getFamilyMembers("user ssn here" /* TODO: get family members by user's SSN */);
  }
  
  @ModelAttribute(value = "newChild")
  public Person getCommandObject() {
    return new Person();
  }
  
  @ModelAttribute(value = "searchedUsers")
  private List<Person> getSearchedUsers() {
    return pyhDemoService.getSearchedUsers();
  }
  
  /* this method is not used, before removing it make sure if it's needed
  
  @ActionMapping(params = "action=removeDependant")
  public void removeDependat(@RequestParam String dependantSSN, ActionResponse response) {
    log.info("calling EditFamilyInformationController.removeDependant");
    log.info("dependant ssn: " + dependantSSN);
    
    // TODO: call service to remove guardian's dependant
    
    response.setRenderParameter("action", "editFamilyInformation");
  }
  */
  
  @ActionMapping(params = "action=addDependantAsFamilyMember")
  public void addDependantAsFamilyMember(@RequestParam String dependantSSN, ActionResponse response) {
    log.info("calling EditFamilyInformationController.addDependantAsFamilyMember");
    log.info("dependant ssn: " + dependantSSN);
    
    response.setRenderParameter("action", "editFamilyInformation");
  }
  
  @ActionMapping(params = "action=addNewChild")
  public void addNewChild(@ModelAttribute Person child, ActionResponse response) {
    
    log.info("calling EditFamilyInformationController.addNewChild");
    log.info("child name: " + child.getFirstname() + " " + child.getMiddlename() + " " + child.getSurname());
    log.info("child ssn: " + child.getSsn());
    
    // TODO: call service to add a new child
    
    response.setRenderParameter("action", "editFamilyInformation");
  }
  
  @ActionMapping(params = "action=removeFamilyMember")
  public void removeFamilyMember(@RequestParam String familyMemberSSN, ActionResponse response) {
    log.info("calling EditFamilyInformationController.removeFamilyMember");
    log.info("family member ssn: " + familyMemberSSN);
    
    // TODO: call service to remove family member
    
    response.setRenderParameter("action", "editFamilyInformation");
  }
  
  @ActionMapping(params = "action=searchUsers")
  public void searchUsers(ActionRequest request, ActionResponse response) {
    log.info("calling EditFamilyInformationController.searchUsers");
    
    log.info("etunimi: " + request.getParameter("searchFirstname"));
    log.info("sukunimi: " + request.getParameter("searchSurname"));
    log.info("ssn: " + request.getParameter("searchSSN"));
    
    // TODO: call service to query users and return users as an model attribute object
    
    pyhDemoService.searchUsers("firstname", "surname", "ssn");
    
    response.setRenderParameter("action", "editFamilyInformation");
    
  }
  
  @ActionMapping(params = "action=addUsersToFamily")
  public void addUsersToFamily(ActionRequest request, ActionResponse response) {
    log.info("calling EditFamilyInformationController.addUsersToFamily");
    
    log.info("parametrit:");
    
    for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
      String param = e.nextElement();
      System.out.println(param + ": " + request.getParameter(param));
    }
    
    response.setRenderParameter("action", "editFamilyInformation");
  }
  
}
