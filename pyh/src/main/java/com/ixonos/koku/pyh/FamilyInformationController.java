package com.ixonos.koku.pyh;

import java.util.List;

import javax.portlet.RenderRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ixonos.koku.pyh.mock.User;
import com.ixonos.koku.pyh.model.Person;

@Controller(value = "familyInformationController")
@RequestMapping(value = "VIEW")
public class FamilyInformationController {
  
  private static Logger log = LoggerFactory.getLogger(FamilyInformationController.class);
  
  @Autowired
  @Qualifier(value = "pyhDemoService")
  private PyhDemoService pyhDemoService;
  
  @RenderMapping(params="action=guardianFamilyInformation")
  public String render(RenderRequest request, Model model)
  {
    return "familyinformation";
  }
  
  @ModelAttribute(value = "guardedChildren")
  private List<Person> getGuardiansChildren() {
    return pyhDemoService.getGuardiansChildren("guardian ssn here" /* TODO: get guardian by SSN */ );
  }
  
  /**
   * Returns the current (login) user and adds it to the model.
   * @return
   */
  @ModelAttribute(value = "user")
  private User getUser() {
    return pyhDemoService.getUser();
  }
  
}
