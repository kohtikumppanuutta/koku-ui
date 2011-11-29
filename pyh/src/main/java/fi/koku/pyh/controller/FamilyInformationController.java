/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.pyh.controller;

import java.util.Locale;

import javax.portlet.RenderRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.portlet.filter.userinfo.UserInfoUtils;
import fi.koku.pyh.ui.common.PyhConstants;
import fi.koku.services.entity.community.v1.CommunityServiceFactory;
import fi.koku.services.entity.community.v1.CommunityServicePortType;
import fi.koku.services.entity.customer.v1.CustomerServiceFactory;
import fi.koku.services.entity.customer.v1.CustomerServicePortType;
import fi.koku.services.entity.customer.v1.CustomerType;
import fi.koku.services.entity.customer.v1.ServiceFault;
import fi.koku.services.entity.customerservice.exception.FamilyNotFoundException;
import fi.koku.services.entity.customerservice.exception.TooManyFamiliesException;
import fi.koku.services.entity.customerservice.helper.FamilyHelper;
import fi.koku.services.entity.customerservice.helper.MessageHelper;
import fi.koku.services.entity.customerservice.model.DependantsAndFamily;
import fi.koku.services.entity.customerservice.model.Family;
import fi.koku.services.entity.customerservice.model.FamilyIdAndFamilyMembers;
import fi.koku.services.entity.customerservice.model.Person;

/**
 * Controller for user's family information view.
 * 
 * @author hurulmi
 *
 */

@Controller(value = "familyInformationController")
@RequestMapping(value = "VIEW")
public class FamilyInformationController {

  private static Logger logger = LoggerFactory.getLogger(FamilyInformationController.class);
  private CustomerServicePortType customerService;
  private FamilyHelper familyHelper;
  private MessageHelper messageHelper;
  
  @Autowired
  private ResourceBundleMessageSource messageSource;
  
  /**
   * Constructor creates customer service and community service instances and helper class instances.
   */
  public FamilyInformationController() {
    
    CustomerServiceFactory customerServiceFactory = new CustomerServiceFactory(PyhConstants.CUSTOMER_SERVICE_USER_ID, PyhConstants.CUSTOMER_SERVICE_PASSWORD, PyhConstants.CUSTOMER_SERVICE_ENDPOINT);
    customerService = customerServiceFactory.getCustomerService();
    
    CommunityServiceFactory communityServiceFactory = new CommunityServiceFactory(PyhConstants.COMMUNITY_SERVICE_USER_ID, PyhConstants.COMMUNITY_SERVICE_PASSWORD, PyhConstants.COMMUNITY_SERVICE_ENDPOINT);
    CommunityServicePortType communityService = communityServiceFactory.getCommunityService();
    
    familyHelper = new FamilyHelper(customerService, communityService, PyhConstants.COMPONENT_PYH);
    messageHelper = new MessageHelper(customerService, communityService, PyhConstants.COMPONENT_PYH);
  }
  
  /**
   * Render method for showing user's family information view.
   * 
   * @param model - model object which makes data accessible to the JSP page
   * @param request - portlet render request
   * @return - the name of the JSP page to render
   * @throws ServiceFault
   */
  @RenderMapping
  public String render(Model model, RenderRequest request) throws fi.koku.services.entity.customer.v1.ServiceFault, 
    fi.koku.services.entity.community.v1.ServiceFault, TooManyFamiliesException {
    
    String userPic = UserInfoUtils.getPicFromSession(request);
    
    CustomerType customer = null;
    try {
      customer = customerService.opGetCustomer(userPic, CustomerServiceFactory.createAuditInfoType(PyhConstants.COMPONENT_PYH, userPic));
    } catch (fi.koku.services.entity.customer.v1.ServiceFault fault) {
      // catch the exception when the user information cannot be fetched; user will be null and a notification about 
      // missing user information will be shown in JSP
    }
    
    Person user = null;
    if (customer != null) {
      logger.debug("FamilyInformationController.render(): returning customer: " + customer.getEtunimetNimi() + " " + customer.getSukuNimi() + ", " + customer.getHenkiloTunnus());
      user = new Person(customer);
    }
    
    Family userFamily = null;
    try {
      userFamily = familyHelper.getFamily(userPic);
    } catch (FamilyNotFoundException e) {
      // user doesn't belong to any family community
    }
    
    DependantsAndFamily daf = familyHelper.getDependantsAndFamily(userPic, userFamily);
    FamilyIdAndFamilyMembers fidm = familyHelper.getOtherFamilyMembers(userPic, userFamily);
    
    model.addAttribute("user", user);
    model.addAttribute("dependants", daf.getDependants());
    model.addAttribute("otherFamilyMembers", fidm.getFamilyMembers());
    model.addAttribute("currentFamilyId", fidm.getFamilyId());
    model.addAttribute("messages", messageHelper.getMessagesFor(user, familyHelper.isParentsSet(userPic, userFamily), messageSource.getMessage("ui.pyh.received.messages.content", null, "", Locale.getDefault()), messageSource.getMessage("ui.pyh.received.messages.content.two.parents", null, "", Locale.getDefault())));
    model.addAttribute("sentMessages", messageHelper.getSentMessages(user, messageSource.getMessage("ui.pyh.sent.messages.content", null, "", Locale.getDefault())));
    model.addAttribute("supportEmailAddress", PyhConstants.KOKU_SUPPORT_EMAIL_ADDRESS);
    
    return "familyinformation";
  }
}
