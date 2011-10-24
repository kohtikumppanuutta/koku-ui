package fi.koku.lok;

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

  // use customer service:
  private CustomerServicePortType customerService;

  private AuthorizationInfoService authorizationInfoService;

  public UserSearchController() {
    ServiceFactory f = new ServiceFactory();
    authorizationInfoService = f.getAuthorizationInfoService();
    customerService = f.getCustomerService();
  }

  @RenderMapping(params = "action=searchUser")
  public String renderSearch(PortletSession session, RenderRequest req, Model model) {

    // get user pic and role
    String userPic = LogUtils.getPicFromSession(session);

    List<Role> userRoles = authorizationInfoService.getUsersRoles(LogConstants.COMPONENT_LOK, userPic);

    // add a flag for allowing this user to see the operations on page
    // search.jsp
    if (AuthUtils.isOperationAllowed("AdminSystemLogFile", userRoles)) {
      model.addAttribute("allowedToView", true);
    }

    model.addAttribute("search", false); // This means that search was NOT done

    return "usersearch";
  }

  @ActionMapping(params = "action=searchUserWithParams")
  public void searchUserWithParams(ActionResponse response, @RequestParam(value = "pic", required = false) String pic,
      Model model) {

    // Form sending required to use ActionURL and now there parameters are send
    // forward to render method
    response.setRenderParameter("pic", pic);
    response.setRenderParameter("action", "searchUserParams");
  }

  @RenderMapping(params = "action=searchUserParams")
  public String renderParams(PortletSession session, @RequestParam(value = "pic", required = false) String pic,
      RenderRequest req, RenderResponse res, Model model) {

    User customer = null;

    // get user pic and role
    String userPic = LogUtils.getPicFromSession(session);

    List<Role> userRoles = authorizationInfoService.getUsersRoles(LogConstants.COMPONENT_LOK, userPic);

    // add a flag for allowing this user to see the operations on page
    // search.jsp
    if (AuthUtils.isOperationAllowed("AdminSystemLogFile", userRoles)) {
      model.addAttribute("allowedToView", true);
    }

    try {
      customer = findUser(pic, userPic);
    } catch (ServiceFault fault) {
      log.error(fault.getMessage());
    } catch (SOAPFaultException e) {
      log.error("SOAPFaultException: " + e.getMessage());
    }

    if (customer != null) {
      model.addAttribute("searchedUsers", customer);
      model.addAttribute("foundName", customer.getSname() + " " + customer.getFname());
      model.addAttribute("foundPic", customer.getPic());
    }

    model.addAttribute("search", true); // This means that search was done

    return "usersearch";
  }

  /*
   * Finds a user in the customer database by pic. There can be only one
   * matching user!
   */
  public User findUser(String pic, String userPic) throws ServiceFault, SOAPFaultException {

    log.info("Try to find user with pic=" + pic);

    CustomerType customer = null;

    fi.koku.services.entity.customer.v1.AuditInfoType customerAuditInfoType = new fi.koku.services.entity.customer.v1.AuditInfoType();
    customerAuditInfoType.setComponent(LogConstants.COMPONENT_LOK);
    customerAuditInfoType.setUserId(userPic);

    User cust = null;

    customer = customerService.opGetCustomer(pic, customerAuditInfoType);

    if (customer != null) {
      
      // the User instance is needed so that the full name can be shown
      cust = new User(customer.getHenkiloTunnus(), customer.getId(), customer.getEtunimetNimi(), customer.getSukuNimi());
      log.debug(cust.getFname() + ", " + cust.getSname() + ", " + cust.getPic());
    }
    return cust;
  }

}
