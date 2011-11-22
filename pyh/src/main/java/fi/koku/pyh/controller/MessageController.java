/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.pyh.controller;

import java.util.List;

import javax.portlet.ActionResponse;

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
import fi.koku.services.entity.community.v1.MembershipRequestQueryCriteriaType;
import fi.koku.services.entity.community.v1.MembershipRequestType;
import fi.koku.services.entity.community.v1.MembershipRequestsType;
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
  
  private CommunityServicePortType communityService;  
  
  /**
   * Constructor creates a community service instance.
   */
  public MessageController() {
    
    CommunityServiceFactory communityServiceFactory = new CommunityServiceFactory(PyhConstants.COMMUNITY_SERVICE_USER_ID, PyhConstants.COMMUNITY_SERVICE_PASSWORD, PyhConstants.COMMUNITY_SERVICE_ENDPOINT);
    communityService = communityServiceFactory.getCommunityService();    
  }
  
  /**
   * Action method to accept a community membership request.
   * 
   * @param userPic - current user's PIC
   * @param messageId - ID of the membership request message
   * @param currentFamilyId - ID of the family community of the user who is accepting the message (used for removing the old family community)
   * @param removeCurrentFamily - boolean to indicate is it necessary to remove the family community
   * @param response - portlet action response
   * @throws ServiceFault
   */
  @ActionMapping(params = "action=acceptMessage")
  public void accept(@RequestParam String userPic, @RequestParam String messageId, @RequestParam String currentFamilyId, @RequestParam boolean removeCurrentFamily, @RequestParam String requesterPic, ActionResponse response) throws ServiceFault {    
    
    String familyId = removeCurrentFamily ? currentFamilyId : null;
    
    MembershipApprovalType membershipApproval = new MembershipApprovalType();
    membershipApproval.setApproverPic(userPic);
    membershipApproval.setMembershipRequestId(messageId);
    membershipApproval.setStatus(CommunityServiceConstants.MEMBERSHIP_REQUEST_STATUS_APPROVED);
    
    AuditInfoType communityAuditInfoType = CommunityServiceFactory.createAuditInfoType(PyhConstants.COMPONENT_PYH, userPic);
    
    communityService.opUpdateMembershipApproval(membershipApproval, communityAuditInfoType);
    Log.getInstance().update(userPic, "", "pyh.membership.approval", "Membership approval status for user " + userPic + " was set to '" + CommunityServiceConstants.MEMBERSHIP_REQUEST_STATUS_APPROVED + "'");
    
    if (familyId != null && !"".equals(familyId)) {
      
      // opposite membership requests must be deleted: 
      // check if the member to be added into the family has sent a request to add the current user into her family
      
      // query opposite membership request: set requester as approver and approver as requester
      MembershipRequestQueryCriteriaType membershipRequestQueryCriteria = new MembershipRequestQueryCriteriaType();
      membershipRequestQueryCriteria.setApproverPic(requesterPic);
      membershipRequestQueryCriteria.setRequesterPic(userPic);
      MembershipRequestsType membershipRequestsType = communityService.opQueryMembershipRequests(membershipRequestQueryCriteria, communityAuditInfoType);
      List<MembershipRequestType> membershipRequests = membershipRequestsType.getMembershipRequest();
      // there should be only one request in the list because sending more than one request to a person at a time is not allowed
      if (membershipRequests.size() > 0) {
        MembershipRequestType membershipRequest = membershipRequests.get(0);
        communityService.opDeleteMembershipRequest(membershipRequest.getId(), communityAuditInfoType);
      }
      
      communityService.opDeleteCommunity(familyId, communityAuditInfoType);
      Log.getInstance().update(userPic, "", "pyh.family.community", "Removing family " + familyId);
    }
    
    // go to familyinformation.jsp
    response.setRenderParameter("action", "");
  }

  /**
   * Action method to reject a community membership request.
   * 
   * @param userPic - current user's PIC
   * @param messageId - ID of the membership request message
   * @param response - portlet action response
   * @throws ServiceFault
   */
  @ActionMapping(params = "action=rejectMessage")
  public void reject(@RequestParam String userPic, @RequestParam String messageId, ActionResponse response) throws ServiceFault {
    
    MembershipApprovalType membershipApproval = new MembershipApprovalType();
    membershipApproval.setApproverPic(userPic);
    membershipApproval.setMembershipRequestId(messageId);
    membershipApproval.setStatus(CommunityServiceConstants.MEMBERSHIP_REQUEST_STATUS_REJECTED);
    
    communityService.opUpdateMembershipApproval(membershipApproval, CommunityServiceFactory.createAuditInfoType(PyhConstants.COMPONENT_PYH, userPic));
    Log.getInstance().update(userPic, "", "pyh.membership.approval", "Membership approval status for user " + userPic + " was set to '" + CommunityServiceConstants.MEMBERSHIP_REQUEST_STATUS_REJECTED + "'");
    
    // go to familyinformation.jsp
    response.setRenderParameter("action", "");
  }
}
