/*
 * Copyright 2012 Ixonos Plc, Finland. All rights reserved.
 * 
 * This file is part of Kohti kumppanuutta.
 *
 * This file is licensed under GNU LGPL version 3.
 * Please see the 'license.txt' file in the root directory of the package you received.
 * If you did not receive a license, please contact the copyright holder
 * (http://www.ixonos.com/).
 *
 */
package fi.koku.lok;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.lok.model.User;
import fi.koku.services.utility.authorizationinfo.util.AuthUtils;
import fi.koku.services.utility.authorizationinfo.v1.AuthorizationInfoService;
import fi.koku.services.utility.authorizationinfo.v1.impl.AuthorizationInfoServiceDummyImpl;
import fi.koku.services.utility.authorizationinfo.v1.model.Role;
import fi.koku.services.entity.customer.v1.CustomerServiceFactory;
import fi.koku.services.entity.customer.v1.CustomerServicePortType;
import fi.koku.services.entity.customer.v1.CustomerType;
import fi.koku.services.entity.customer.v1.ServiceFault;
import fi.koku.services.entity.person.v1.Person;
import fi.koku.services.entity.person.v1.PersonConstants;
import fi.koku.services.entity.person.v1.PersonService;

/**
 * Controller for user search (LOK). This relates to LOK-3.
 * 
 * A simple controller that handles user search, listing and forwarding userid
 * (pic) to next phase which is the actual log search
 * 
 * @author mikkope
 * @author makinsu
 */
@Controller
@RequestMapping(value = "VIEW")
public class UserSearchController {

  private static final Logger log = LoggerFactory.getLogger(UserSearchController.class);
  private static final String DEFAULT_PIC ="customerPic";

  // use customer service:
  private CustomerServicePortType customerService;
  private PersonService personService;

  private AuthorizationInfoService authorizationInfoService;

  public UserSearchController() {
    ServiceFactory f = new ServiceFactory();
    authorizationInfoService = f.getAuthorizationInfoService();
    customerService = f.getCustomerService();
    personService = new PersonService();
  }

  @RenderMapping(params = "action=searchUser")
  public String renderSearch(PortletSession session, RenderRequest req, Model model) {

    // get user pic and role
    String userPic = LogUtils.getPicFromSession(session);

    List<Role> userRoles = authorizationInfoService.getUsersRoles(LogConstants.COMPONENT_LOK, userPic);
    
    model.addAttribute("picType" ,DEFAULT_PIC );

    // add a flag for allowing this user to see the operations on page
    // search.jsp
    if (AuthUtils.isOperationAllowed("AdminSystemLogFile", userRoles)) {
      model.addAttribute("allowedToView", true);
    }

    model.addAttribute("search", false); // This means that search was NOT done

    return "usersearch";
  }

  @ActionMapping(params = "action=searchUserWithParams")
  public void searchUserWithParams(ActionResponse response, @RequestParam(value = "pic", required = false) String pic,@RequestParam(value = "picSelection", required = false) String picSelection,
      Model model) {

    // Form sending required to use ActionURL and now there parameters are send
    // forward to render method
	
	response.setRenderParameter("pic", pic);	
    response.setRenderParameter("picSelection", picSelection);
    response.setRenderParameter("action", "searchUserParams");
  }



  
  @RenderMapping(params = "action=searchUserParams")
  public String renderParams(PortletSession session, @RequestParam(value = "pic", required = false) String pic, @RequestParam(value = "picSelection", required = false) String picSelection,
      RenderRequest req, RenderResponse res, Model model) {

    User customer = null;

    // add logging mode for LOK to model
    boolean picCheck= false;
    
    // get user pic and role
    String userSessionPic = LogUtils.getPicFromSession(session);

    List<Role> userRoles = authorizationInfoService.getUsersRoles(LogConstants.COMPONENT_LOK, userSessionPic);

    // add a flag for allowing this user to see the operations on page
    // search.jsp
    if (AuthUtils.isOperationAllowed("AdminSystemLogFile", userRoles)) {
      model.addAttribute("allowedToView", true);
    }
    
    model.addAttribute("picType" ,picSelection == null ? DEFAULT_PIC : picSelection );
    
 // see http://fi.wikipedia.org/wiki/Henkil%C3%B6tunnus#Tunnuksen_muoto
    if (pic != null && pic.length() == 11 &&
    	 	   (pic.charAt(6) == '-' || pic.charAt(6) == '+' || pic.charAt(6) == 'A') ) {
    	        // pic is well formed
    	       
    	       picCheck = true;
    	   }
    
    
    if (picCheck)
    {
    try {
        customer = findUser(pic, userSessionPic, picSelection != null && picSelection.equals("customerPic"));
      } catch (ServiceFault fault) {
        if (fault.getMessage().equalsIgnoreCase("Customer not found.")) {
          model.addAttribute("error", "koku.lok.no.user.results");
        } else {
          model.addAttribute("error", "koku.lok.error.customer");
        }
        log.error("servicefault");
        log.error(fault.getMessage());
      } catch (SOAPFaultException e) {
        log.error("SOAPFaultException: " + e.getMessage());
        model.addAttribute("error", "koku.lok.error.customer");
      }

      if (customer != null) {
        model.addAttribute("searchedUsers", customer);
        model.addAttribute("foundName", customer.getSname() + " " + customer.getFname());
        model.addAttribute("foundPic", customer.getPic());
      } else {
        model.addAttribute("error", "koku.lok.no.user.results");
      }
    } else {
      // pic is not well formed
      model.addAttribute("error", "koku.lok.malformed.pic");
    }
    
    model.addAttribute("search", true); // This means that search was done

    return "usersearch";
  }

  /*
   * Finds a user in the customer database by pic. There can be only one
   * matching user!
   */
  public User findUser(String pic, String userPic, boolean searchCustomer) throws ServiceFault, SOAPFaultException {

    User user = null;
    if (searchCustomer) {
      log.info("Try to find customer with pic=" + pic);

      CustomerType customer = null;

      fi.koku.services.entity.customer.v1.AuditInfoType customerAuditInfoType = new fi.koku.services.entity.customer.v1.AuditInfoType();
      customerAuditInfoType.setComponent(LogConstants.COMPONENT_LOK);
      customerAuditInfoType.setUserId(userPic);

      customer = customerService.opGetCustomer(pic, customerAuditInfoType);

      if (customer != null) {
        // the User instance is needed so that the full name can be shown
        user = new User(customer.getHenkiloTunnus(), customer.getId(), customer.getEtunimetNimi(),
            customer.getSukuNimi());
        log.debug(user.getFname() + ", " + user.getSname() + ", " + user.getPic());
      }
    } else {
      log.info("Try to find employee with pic=" + pic);
      List<String> picList = new ArrayList<String>();
      picList.add(pic);
      // call the Person service to get the persons
      List<Person> personlist = personService.getPersonsByPics(picList, PersonConstants.PERSON_SERVICE_DOMAIN_OFFICER,
          userPic, LogConstants.COMPONENT_LOK);

      if (personlist.size() > 0) {
        Person p = personlist.get(0);
        
        if ( p.getPic() != null ) {
          user = new User(p.getPic(), p.getUid(), p.getFname(), p.getSname());
          log.debug(user.getFname() + ", " + user.getSname() + ", " + user.getPic());
        }
      }
    }

    return user;
  }
}