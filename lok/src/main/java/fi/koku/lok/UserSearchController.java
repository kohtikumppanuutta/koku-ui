package fi.koku.lok;

import javax.portlet.ActionResponse;
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
import fi.koku.services.entity.customer.v1.CustomerServiceFactory;
import fi.koku.services.entity.customer.v1.CustomerServicePortType;
import fi.koku.services.entity.customer.v1.CustomerType;
import fi.koku.services.entity.customer.v1.ServiceFault;


/**
 * Controller for user search (LOK).
 * This relates to LOK-3.
 * 
 * A simple controller that handles user search, listing and forwarding userid (pic)
 * to next phase which is the actual log search
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
  
  public UserSearchController() {
    CustomerServiceFactory customerServiceFactory = new CustomerServiceFactory(
        LogConstants.CUSTOMER_SERVICE_USER_ID, LogConstants.CUSTOMER_SERVICE_PASSWORD,
        LogConstants.CUSTOMER_SERVICE_ENDPOINT);
    customerService = customerServiceFactory.getCustomerService();
  }
 
 
  @RenderMapping(params = "action=home")
  public String render(Model model){
    log.debug("log search render phase, return main menu");
    model.addAttribute("search", false); //This means that search was NOT done
    return "menu";
  }
  
  @RenderMapping(params = "action=searchUser")
  public String renderSearch(RenderRequest req, Model model){
    log.debug("log search render phase, return user search");
    model.addAttribute("search", false);//This means that search was NOT done
    return "usersearch";
  }
  
  @ActionMapping(params = "action=searchUserWithParams")
   public void searchUserWithParams(ActionResponse response, @RequestParam(value = "pic", required = false) String pic, Model model){

  log.debug("log search user action phase with action=searchUserWithParams "+pic);
    
    //Form sending required to use ActionURL and now there parameters are send forward to render method
    response.setRenderParameter("pic", pic);
    response.setRenderParameter("action", "searchUserParams");
  }
  
  @RenderMapping(params = "action=searchUserParams")
  public String renderParams(@RequestParam(value = "pic", required = false) String pic, RenderRequest req, RenderResponse res, Model model) {
       
    User user = null;
    log.debug("log: search user with pic = "+pic); 
    //TODO: poista tämä HAETAAN 4 kovakoodattua KÄYTTÄJÄÄ
   // model.addAttribute("searchedUsers", lokDemoService.findUsers(pic, null, fname, sname));
    try{
      user = findUser(pic);
    }catch(ServiceFault fault){
      //TODO: lisää virheviesti
      //TODO: tuleelo väärällä hetulla hausta erityislokitus tapahtumalokiin?
     log.debug(fault.getMessage());
    }catch(SOAPFaultException e){
      log.debug("SOAPFaultException");
    }
    
    if(user!=null){
 
      model.addAttribute("searchedUsers", user);
      model.addAttribute("foundName", user.getSname()+" "+user.getFname());
      model.addAttribute("foundPic", user.getPic());
    }
    
    model.addAttribute("search", true); // This means that search was done
    
    return "usersearch";
  }
 
 
  /*
   * Finds a user in the customer database by pic. There can be only one matching user! 
   */
  public User findUser(String pic) throws ServiceFault, SOAPFaultException{
  
    log.info("pic=" + pic);
    
    CustomerType customer = null;
   
    fi.koku.services.entity.customer.v1.AuditInfoType customerAuditInfoType = new fi.koku.services.entity.customer.v1.AuditInfoType();
    customerAuditInfoType.setComponent(LogConstants.COMPONENT_LOK);
    customerAuditInfoType.setUserId(LogConstants.LOK_USER_ID);
    
    User user = null;

      customer = customerService.opGetCustomer(pic, customerAuditInfoType);

    if(customer!=null){
    
      //TODO: oletus tässä: getEtunimetNimi() palauttaa kaikki nimet. Tarkista, palauttaako todella!
      // the User instance is needed so that the full name can be shown
     user = new User(customer.getHenkiloTunnus(), customer.getId(), customer.getEtunimetNimi(), customer.getSukuNimi());
      log.debug(user.getFname()+", "+user.getSname()+", "+user.getPic());
    }
      return user;
  }

  
   // DEMO-KÄYTTÄJÄT:
    // Return list of users if one of the search params is not null and not
    // empty string. Else return empty arraylist.
 /*
  *    if (StringUtils.isNotBlank(pic) | StringUtils.isNotBlank(fname) | StringUtils.isNotBlank(sname)) {
      log.info("Returning searchedUsers.size=" + searchedUsers.size());
      return searchedUsers;
    } else {
      return new ArrayList<User>(0);
    }
*/
}
