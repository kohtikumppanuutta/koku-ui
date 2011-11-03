/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.pyh.controller;

import javax.portlet.RenderRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.portlet.filter.userinfo.UserInfoUtils;
import fi.koku.pyh.model.DependantsAndFamily;
import fi.koku.pyh.model.FamilyIdAndFamilyMembers;
import fi.koku.pyh.model.Person;
import fi.koku.pyh.ui.common.PyhConstants;
import fi.koku.pyh.ui.common.PyhDemoService;
import fi.koku.services.entity.customer.v1.CustomerServiceFactory;
import fi.koku.services.entity.customer.v1.CustomerServicePortType;
import fi.koku.services.entity.customer.v1.CustomerType;

/**
 * Controller for user's family information view.
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
  
  private CustomerServicePortType customerService;
  
  public FamilyInformationController() {
    CustomerServiceFactory customerServiceFactory = new CustomerServiceFactory(PyhConstants.CUSTOMER_SERVICE_USER_ID, PyhConstants.CUSTOMER_SERVICE_PASSWORD, PyhConstants.CUSTOMER_SERVICE_ENDPOINT);
    customerService = customerServiceFactory.getCustomerService();
  }
  
  @RenderMapping
  public String render(Model model, RenderRequest request) {
    String userPic = UserInfoUtils.getPicFromSession(request);
    
    CustomerType customer;
    fi.koku.services.entity.customer.v1.AuditInfoType customerAuditInfoType = new fi.koku.services.entity.customer.v1.AuditInfoType();
    customerAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    customerAuditInfoType.setUserId(userPic);
    
    try {
      customer = customerService.opGetCustomer(userPic, customerAuditInfoType);
    } catch (fi.koku.services.entity.customer.v1.ServiceFault fault) {
      log.error("FamilyInformationController.render: opGetCustomer raised a ServiceFault", fault);
      return null;
    }
    
    log.debug("FamilyInformationController.render(): returning customer: " + customer.getEtunimetNimi() + " " + customer.getSukuNimi() + ", " + customer.getHenkiloTunnus());
    log.debug("--");
    Person user = new Person(customer);
    
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
