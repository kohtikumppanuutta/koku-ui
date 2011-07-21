package com.ixonos.koku.pyh;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
  private List<FamilyMember> getOtherFamilyMembers() {
    return pyhDemoService.getOtherFamilyMembers();
  }
  
  @ModelAttribute(value = "newDependant")
  public Person getCommandObject() {
    return new Person();
  }
  
  @ModelAttribute(value = "searchedUsers")
  private List<Person> getSearchedUsers() {
    return pyhDemoService.getSearchedUsers();
  }
  
  @ActionMapping(params = "action=addDependantAsFamilyMember")
  public void addDependantAsFamilyMember(@RequestParam String dependantSSN, ActionResponse response) {
    pyhDemoService.addDependantAsFamilyMember(dependantSSN);
    
    response.setRenderParameter("action", "editFamilyInformation");
  }
  
  @ActionMapping(params = "action=addNewDependant")
  public void addNewDependant(@ModelAttribute Person dependant, ActionResponse response) {
    String ssn = dependant.getSsn();
    String firstName = dependant.getFirstname();
    String middleName = dependant.getMiddlename();
    String surname = dependant.getSurname();
    String birthDay = dependant.getBirthday();
    String birthMonth = dependant.getBirthmonth();
    String birthYear = dependant.getBirthyear();
    dependant.setBirthdate(dependant.getBirthday(), dependant.getBirthmonth(), dependant.getBirthyear());
    
    if (ssn.equals("") == false && firstName.equals("") == false && 
        middleName.equals("") == false && surname.equals("") == false) {
      pyhDemoService.addNewDependant(dependant);
    }
    
    response.setRenderParameter("action", "editFamilyInformation");
  }
  
  // TODO: user should disappear from the view immediately after clicking 'poista jäsenyys' 
  @ActionMapping(params = "action=removeFamilyMember")
  public void removeFamilyMember(@RequestParam String familyMemberSSN, ActionResponse response) {
    pyhDemoService.removeFamilyMember(familyMemberSSN);
    
    response.setRenderParameter("action", "editFamilyInformation");
  }
  
  @ActionMapping(params = "action=searchUsers")
  public void searchUsers(ActionRequest request, ActionResponse response) {
    String fn = request.getParameter("searchFirstname");
    String sn = request.getParameter("searchSurname");
    String ssn = request.getParameter("searchSSN");
    
    // call service to query users,
    // users are returned as a model attribute object searchedUsers
    pyhDemoService.searchUsers(fn, sn, ssn);
    
    response.setRenderParameter("action", "editFamilyInformationWithSearchResults");
  }
  
  @ActionMapping(params = "action=addUsersToFamily")
  public void addUsersToFamily(ActionRequest request, ActionResponse response) {
    HashMap<String, String> parameterMap = new HashMap<String, String>();
    HashMap<String, String> personMap = new HashMap<String, String>();
    String personSSN = "";
    String personRole = "";
    
    for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
      String param = e.nextElement();
      String paramValue = request.getParameter(param);
      if (param.startsWith("userSSN") || param.startsWith("userRole")) {
        parameterMap.put(param, paramValue);
      }
    }
    
    Set<String> keys = parameterMap.keySet();
    Iterator<String> si = keys.iterator();
    while (si.hasNext()) {
      String key = si.next();
      if (key.startsWith("userSSN")) {
        String[] tokens = key.split("_");
        // index is the number after '_' in parameter name, e.g. userSSN_1 (index is 1)
        String index = tokens[1];
        
        personSSN = parameterMap.get(key);
        personRole = parameterMap.get("userRole_" + index);
        personMap.put(personSSN, personRole);
      }
    }
    
    // call service to add users to family
    pyhDemoService.addPersonsAsFamilyMembers(personMap);
    
    response.setRenderParameter("action", "editFamilyInformation");
  }
  
}
