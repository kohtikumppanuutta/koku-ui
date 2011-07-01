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

@Controller(value = "editFamilyInformationController")
@RequestMapping(value = "VIEW")
public class EditFamilyInformationController {
  
  private static Logger log = LoggerFactory.getLogger(EditFamilyInformationController.class);
  
  @Autowired
  @Qualifier(value = "pyhDemoService")
  private PyhDemoService pyhDemoService;
  
  @RenderMapping(params="action=editFamilyInformation")
  public String render(RenderRequest request, Model model)
  {
    return "editfamilyinformation";
  }
  
  
}
