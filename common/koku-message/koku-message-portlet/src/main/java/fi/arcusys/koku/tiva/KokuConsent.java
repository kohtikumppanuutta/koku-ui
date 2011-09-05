package fi.arcusys.koku.tiva;

import java.util.List;

/**
 * Consent data model for citizen
 * @author Jinhua Chen
 * Aug 11, 2011
 */
public class KokuConsent {
	 
	private long consentId;
	private String anotherPermitterUid;
	private String requester;
	private String templateName;
	private String createType;
	private String assignedDate;
	private String status;
	private String approvalStatus;
	private String validDate;
	private List<ActionRequest> actionRequests;
	private String recipients;
	
	public long getConsentId() {
		return consentId;
	}
	
	public void setConsentId(long consentId) {
		this.consentId = consentId;
	}
	
	public String getAnotherPermitterUid() {
		return anotherPermitterUid;
	}
	
	public void setAnotherPermitterUid(String anotherPermitterUid) {
		this.anotherPermitterUid = anotherPermitterUid;
	}
	
	public String getRequester() {
		return requester;
	}
	
	public void setRequester(String requestor) {
		this.requester = requestor;
	}
	
	public String getTemplateName() {
		return templateName;
	}
	
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	public String getCreateType() {
		return createType;
	}
	
	public void setCreateType(String createType) {
		this.createType = createType;
	}
	
	public String getAssignedDate() {
		return assignedDate;
	}
	
	public void setAssignedDate(String assignedDate) {
		this.assignedDate = assignedDate;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getValidDate() {
		return validDate;
	}
	
	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}
	
	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public List<ActionRequest> getActionRequests() {
		return actionRequests;
	} 
	
	public void setActionRequests(List<ActionRequest> actionRequests) {
		this.actionRequests = actionRequests;
	}
	
	public String getRecipients() {
		return recipients;
	}
	
	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}
	
}
