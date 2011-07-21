package com.ixonos.koku.pyh;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ixonos.koku.pyh.model.Dependant;
import com.ixonos.koku.pyh.model.FamilyMember;
import com.ixonos.koku.pyh.model.Person;

/**
 * Controller for viewing user's family information.
 * This is the controller for the main view of PYH portlet.
 * 
 * @author hurulmi
 *
 */

@Controller(value = "familyInformationController")
@RequestMapping(value = "VIEW")
public class FamilyInformationController {
  
  private static Logger log = LoggerFactory.getLogger(FamilyInformationController.class);
  
  @Autowired
  @Qualifier(value = "pyhDemoService")
  private PyhDemoService pyhDemoService;
  
  @RenderMapping(params="action=guardianFamilyInformation")
  public String render() {
    return "familyinformation";
  }
  
  /**
   * Sets the current (login) user as a model attribute.
   * @return
   */
  @ModelAttribute(value = "user")
  private Person getUser() {
    return pyhDemoService.getUser();
  }
  
  /**
   * Sets the user's dependants as a model attribute.
   * @return
   */
  @ModelAttribute(value = "dependants")
  private List<Dependant> getDependants() {
    return pyhDemoService.getDependants();
  }
  
  /**
   * Sets other family members as a model attribute.
   * Other family members means all other except the user and user's dependants.
   * @return
   */
  @ModelAttribute(value = "otherFamilyMembers")
  private List<FamilyMember> getFamilyMembers() {
    return pyhDemoService.getOtherFamilyMembers();
  }
  
}
