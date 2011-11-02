/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.pyh.controller;

import javax.portlet.ActionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import fi.koku.pyh.ui.common.PyhDemoService;

/**
 * Controller for handling acceptance or rejection of membership request messages.
 * 
 * @author hurulmi
 *
 */

@Controller(value = "messageController")
@RequestMapping(value = "VIEW")
public class MessageController {
  
  private static Logger log = LoggerFactory.getLogger(MessageController.class);
  
  @Autowired
  @Qualifier(value = "pyhDemoService")
  private PyhDemoService pyhDemoService;
  
  /**
   * Call service to accept a request.
   */
  @ActionMapping(params = "action=acceptMessage")
  public void accept(@RequestParam String userPic, @RequestParam String messageId, @RequestParam String currentFamilyId, @RequestParam boolean removeCurrentFamily, ActionResponse response) {
    log.debug("calling MessageController.accept() with parameters:");
    log.debug("userPic: " + userPic);
    log.debug("messageId: " + messageId);
    
    String familyId = removeCurrentFamily ? currentFamilyId : null;
    
    // userPic is the person approving the request
    pyhDemoService.acceptOrRejectMembershipRequest(messageId, userPic, "approved", familyId);
    // go to familyinformation.jsp
    response.setRenderParameter("action", "");
  }

  /**
   * Call service to reject a request.
   */
  @ActionMapping(params = "action=rejectMessage")
  public void reject(@RequestParam String userPic, @RequestParam String messageId, ActionResponse response) {
    log.debug("calling MessageController.reject() with parameters:");
    log.debug("userPic: " + userPic);
    log.debug("messageId: " + messageId);
    
    // userPic is the person approving the request
    pyhDemoService.acceptOrRejectMembershipRequest(messageId, userPic, "rejected", null);
    // go to familyinformation.jsp
    response.setRenderParameter("action", "");
  }
}
