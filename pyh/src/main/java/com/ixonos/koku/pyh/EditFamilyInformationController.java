package com.ixonos.koku.pyh;

import java.util.Enumeration;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;

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

import com.ixonos.koku.pyh.model.Dependant;
import com.ixonos.koku.pyh.model.FamilyMember;
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
    pyhDemoService.clearSearchedUsers();
    return "editfamilyinformation";
  }
  
  // this render method does not clear the search results
  @RenderMapping(params = "action=editFamilyInformationWithSearchResults")
  public String renderWithSearchResults(RenderRequest request) {
    log.info("calling EditFamilyInformationController.renderWithSearchResults");
    
    request.setAttribute("search", true);
    return "editfamilyinformation";
  }
  
  @ModelAttribute(value = "user")
  private Person getUser() {
    return pyhDemoService.getUser();
  }
  
  @ModelAttribute(value = "dependants")
  private List<Dependant> getDependants() {
    return pyhDemoService.getDependants();
  }
  
  @ModelAttribute(value = "otherFamilyMembers")
  private List<FamilyMember> getFamilyMembers() {
    return pyhDemoService.getOtherFamilyMembers();
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
    
    pyhDemoService.addDependantAsFamilyMember(dependantSSN);
    
    response.setRenderParameter("action", "editFamilyInformation");
  }
  
  @ActionMapping(params = "action=addNewChild")
  public void addNewChild(@ModelAttribute Person child, ActionResponse response) {
    
    log.info("calling EditFamilyInformationController.addNewChild");
    log.info("child name: " + child.getFirstname() + " " + child.getMiddlename() + " " + child.getSurname());
    log.info("child ssn: " + child.getSsn());
    log.info("birthday: " + child.getBirthday());
    log.info("birthmonth: " + child.getBirthmonth());
    log.info("birthyear: " + child.getBirthyear());
    
    // TODO: call service to add a new child
    child.setBirthdate(child.getBirthday(), child.getBirthmonth(), child.getBirthyear());
    
    log.info("birthdate: " + child.getBirthdate());
    
    response.setRenderParameter("action", "editFamilyInformation");
  }
  
  @ActionMapping(params = "action=removeFamilyMember")
  public void removeFamilyMember(@RequestParam String familyMemberSSN, ActionResponse response) {
    log.info("calling EditFamilyInformationController.removeFamilyMember");
    log.info("family member ssn: " + familyMemberSSN);
    
    // TODO: call service to remove family member
    
    response.setRenderParameter("action", "editFamilyInformation");
  }
  
  // TODO: käyttäjien haku: ilmoitus 'ei hakutuloksia' jos käyttäjiä ei löytynyt,
  // haku tulokset tai ilmoitus näytetään vain kun hakutoiminto on suoritettu
  
  @ActionMapping(params = "action=searchUsers")
  public void searchUsers(ActionRequest request, ActionResponse response) {
    log.info("calling EditFamilyInformationController.searchUsers");
    
    String fn = request.getParameter("searchFirstname");
    String sn = request.getParameter("searchSurname");
    String ssn = request.getParameter("searchSSN");
    
    log.info("etunimi: " + fn);
    log.info("sukunimi: " + sn);
    log.info("ssn: " + ssn);
    
    // calling service to query users,
    // users are returned as an model attribute object searchedUsers
    pyhDemoService.searchUsers(fn, sn, ssn);
    
    //response.setRenderParameter("action", "editFamilyInformation");
    response.setRenderParameter("action", "editFamilyInformationWithSearchResults");
    
  }
  
  @ActionMapping(params = "action=addUsersToFamily")
  public void addUsersToFamily(ActionRequest request, ActionResponse response) {
    log.info("calling EditFamilyInformationController.addUsersToFamily");
    
    log.info("parametrit:");
    
    for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
      String param = e.nextElement();
      log.info(param + ": " + request.getParameter(param));
    }
    
    response.setRenderParameter("action", "editFamilyInformation");
    
  }
  
}
