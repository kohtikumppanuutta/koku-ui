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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import fi.koku.pyh.ui.common.Log;
import fi.koku.pyh.ui.common.PyhConstants;
import fi.koku.services.entity.community.v1.AuditInfoType;
import fi.koku.services.entity.community.v1.CommunityServiceConstants;
import fi.koku.services.entity.community.v1.CommunityServiceFactory;
import fi.koku.services.entity.community.v1.CommunityServicePortType;
import fi.koku.services.entity.community.v1.MembershipApprovalType;
import fi.koku.services.entity.community.v1.ServiceFault;

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
  
  private CommunityServicePortType communityService;  
  
  public MessageController() {
    CommunityServiceFactory communityServiceFactory = new CommunityServiceFactory(PyhConstants.COMMUNITY_SERVICE_USER_ID, PyhConstants.COMMUNITY_SERVICE_PASSWORD, PyhConstants.COMMUNITY_SERVICE_ENDPOINT);
    communityService = communityServiceFactory.getCommunityService();    
  }
  
  /**
   * Call service to accept a request.
   */
  @ActionMapping(params = "action=acceptMessage")
  public void accept(@RequestParam String userPic, @RequestParam String messageId, @RequestParam String currentFamilyId, @RequestParam boolean removeCurrentFamily, ActionResponse response) {    
    String familyId = removeCurrentFamily ? currentFamilyId : null;
    
    MembershipApprovalType membershipApproval = new MembershipApprovalType();
    membershipApproval.setApproverPic(userPic);
    membershipApproval.setMembershipRequestId(messageId);
    membershipApproval.setStatus(CommunityServiceConstants.MEMBERSHIP_REQUEST_STATUS_APPROVED);
        
    AuditInfoType communityAuditInfoType = CommunityServiceFactory.createAuditInfoType(PyhConstants.COMPONENT_PYH, userPic);
    
    try {
      communityService.opUpdateMembershipApproval(membershipApproval, communityAuditInfoType);
      Log.getInstance().update(userPic, "", "pyh.membership.approval", "Membership approval status for user " + userPic + " was set to '" + CommunityServiceConstants.MEMBERSHIP_REQUEST_STATUS_APPROVED + "'");
      
      if (familyId != null && !"".equals(familyId)) {
        communityService.opDeleteCommunity(familyId, communityAuditInfoType);
        Log.getInstance().update(userPic, "", "pyh.family.community", "Removing family " + familyId);
      }
    } catch (ServiceFault fault) {
      log.error("PyhDemoService.acceptMembershipRequest: opUpdateMembershipApproval raised a ServiceFault", fault);
    }
    
    // go to familyinformation.jsp
    response.setRenderParameter("action", "");
  }

  /**
   * Call service to reject a request.
   */
  @ActionMapping(params = "action=rejectMessage")
  public void reject(@RequestParam String userPic, @RequestParam String messageId, ActionResponse response) {
    MembershipApprovalType membershipApproval = new MembershipApprovalType();
    membershipApproval.setApproverPic(userPic);
    membershipApproval.setMembershipRequestId(messageId);
    membershipApproval.setStatus(CommunityServiceConstants.MEMBERSHIP_REQUEST_STATUS_REJECTED);
    
    try {
      communityService.opUpdateMembershipApproval(membershipApproval, CommunityServiceFactory.createAuditInfoType(PyhConstants.COMPONENT_PYH, userPic));
      Log.getInstance().update(userPic, "", "pyh.membership.approval", "Membership approval status for user " + userPic + " was set to '" + CommunityServiceConstants.MEMBERSHIP_REQUEST_STATUS_REJECTED + "'");      
    } catch (ServiceFault fault) {
      log.error("PyhDemoService.acceptMembershipRequest: opUpdateMembershipApproval raised a ServiceFault", fault);
    }    
    
    // go to familyinformation.jsp
    response.setRenderParameter("action", "");
  }
}
