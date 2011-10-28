package fi.koku.pyh;

import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import fi.koku.portlet.filter.userinfo.UserInfo;

@Controller(value = "messageController")
@RequestMapping(value = "VIEW")
public class MessageController {
  
  private static Logger log = LoggerFactory.getLogger(MessageController.class);
  
  @Autowired
  @Qualifier(value = "pyhDemoService")
  private PyhDemoService pyhDemoService;
  
  private boolean debug = false;
  
  /**
   * Call service to accept a request.
   */
  @ActionMapping(params = "action=acceptMessage")
  public void accept(@RequestParam String userPic, @RequestParam String messageId, @RequestParam String currentFamilyId, @RequestParam boolean removeCurrentFamily, ActionResponse response) {
    if (debug) {
      log.info("calling MessageController.accept() with parameters:");
      log.info("userPic: " + userPic);
      log.info("messageId: " + messageId);
    }
    
    String familyId = removeCurrentFamily ? currentFamilyId : null;
    
    pyhDemoService.acceptOrRejectMembershipRequest(messageId, userPic /*approver*/, "approved", familyId);
    response.setRenderParameter("action", "");
  }

  /**
   * Call service to reject a request.
   */
  @ActionMapping(params = "action=rejectMessage")
  public void reject(@RequestParam String userPic, @RequestParam String messageId, ActionResponse response) {
    if (debug) {
      log.info("calling MessageController.reject() with parameters:");
      log.info("userPic: " + userPic);
      log.info("messageId: " + messageId);
    }
    
    pyhDemoService.acceptOrRejectMembershipRequest(messageId, userPic /*approver*/, "rejected", null);
    response.setRenderParameter("action", "");
  }

}
