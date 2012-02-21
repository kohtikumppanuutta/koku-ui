package fi.arcusys.koku.tiva;

import java.util.ArrayList;
import java.util.List;

import fi.arcusys.koku.tiva.employeeservice.User;
import fi.arcusys.koku.users.KokuUser;

/**
 * Consent data model for citizen
 * @author Jinhua Chen
 * Aug 11, 2011
 */
public class KokuConsent {
	 
	private long consentId;
	private KokuUser anotherPermitterUser;
	private KokuUser requesterUser;
	private String templateName;
	private String createType;
	private String assignedDate;
	private String status;
	private String approvalStatus;
	private String validDate;
	private List<ActionRequest> actionRequests;
	private List<KokuUser> recipientUsers;
	private String replyTill;
	private String comment;
	private String templateTypeName;
	private String templateDescription;
	private KokuUser targetPerson;
	
	
	
	/**
	 * @return the templateDescription
	 */
	public final String getTemplateDescription() {
		return templateDescription;
	}

	/**
	 * @param templateDescription the templateDescription to set
	 */
	public final void setTemplateDescription(String templateDescription) {
		this.templateDescription = templateDescription;
	}

	public long getConsentId() {
		return consentId;
	}
	
	public void setConsentId(long consentId) {
		this.consentId = consentId;
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

	public String getReplyTill() {
		return replyTill;
	}

	public void setReplyTill(String replyTill) {
		this.replyTill = replyTill;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setTemplateTypeName(String templateTypeName) {
		this.templateTypeName = templateTypeName;
	}
	
	public String getTemplateTypeName() {
		return templateTypeName;
	}

	public KokuUser getTargetPerson() {
		return targetPerson;
	}

	public void setTargetPerson(KokuUser targetPerson) {
		this.targetPerson = targetPerson;
	}
	
	/**
	 * @return the anotherPermitterUser
	 */
	public final KokuUser getAnotherPermitterUser() {
		return anotherPermitterUser;
	}

	/**
	 * @param anotherPermitterUser the anotherPermitterUser to set
	 */
	public final void setAnotherPermitterUser(KokuUser anotherPermitterUser) {
		this.anotherPermitterUser = anotherPermitterUser;
	}

	/**
	 * @return the requesterUser
	 */
	public final KokuUser getRequesterUser() {
		return requesterUser;
	}

	/**
	 * @param requesterUser the requesterUser to set
	 */
	public final void setRequesterUser(KokuUser requesterUser) {
		this.requesterUser = requesterUser;
	}

	/**
	 * @return the recipientUsers
	 */
	public final List<KokuUser> getRecipientUsers() {
		if (recipientUsers == null) {
			recipientUsers = new ArrayList<KokuUser>();
		}
		return recipientUsers;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actionRequests == null) ? 0 : actionRequests.hashCode());
		result = prime
				* result
				+ ((anotherPermitterUser == null) ? 0 : anotherPermitterUser
						.hashCode());
		result = prime * result
				+ ((approvalStatus == null) ? 0 : approvalStatus.hashCode());
		result = prime * result
				+ ((assignedDate == null) ? 0 : assignedDate.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + (int) (consentId ^ (consentId >>> 32));
		result = prime * result
				+ ((createType == null) ? 0 : createType.hashCode());
		result = prime * result
				+ ((recipientUsers == null) ? 0 : recipientUsers.hashCode());
		result = prime * result
				+ ((replyTill == null) ? 0 : replyTill.hashCode());
		result = prime * result
				+ ((requesterUser == null) ? 0 : requesterUser.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((targetPerson == null) ? 0 : targetPerson.hashCode());
		result = prime * result
				+ ((templateName == null) ? 0 : templateName.hashCode());
		result = prime
				* result
				+ ((templateTypeName == null) ? 0 : templateTypeName.hashCode());
		result = prime * result
				+ ((validDate == null) ? 0 : validDate.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		KokuConsent other = (KokuConsent) obj;
		if (actionRequests == null) {
			if (other.actionRequests != null) {
				return false;
			}
		} else if (!actionRequests.equals(other.actionRequests)) {
			return false;
		}
		if (anotherPermitterUser == null) {
			if (other.anotherPermitterUser != null) {
				return false;
			}
		} else if (!anotherPermitterUser.equals(other.anotherPermitterUser)) {
			return false;
		}
		if (approvalStatus == null) {
			if (other.approvalStatus != null) {
				return false;
			}
		} else if (!approvalStatus.equals(other.approvalStatus)) {
			return false;
		}
		if (assignedDate == null) {
			if (other.assignedDate != null) {
				return false;
			}
		} else if (!assignedDate.equals(other.assignedDate)) {
			return false;
		}
		if (comment == null) {
			if (other.comment != null) {
				return false;
			}
		} else if (!comment.equals(other.comment)) {
			return false;
		}
		if (consentId != other.consentId) {
			return false;
		}
		if (createType == null) {
			if (other.createType != null) {
				return false;
			}
		} else if (!createType.equals(other.createType)) {
			return false;
		}
		if (recipientUsers == null) {
			if (other.recipientUsers != null) {
				return false;
			}
		} else if (!recipientUsers.equals(other.recipientUsers)) {
			return false;
		}
		if (replyTill == null) {
			if (other.replyTill != null) {
				return false;
			}
		} else if (!replyTill.equals(other.replyTill)) {
			return false;
		}
		if (requesterUser == null) {
			if (other.requesterUser != null) {
				return false;
			}
		} else if (!requesterUser.equals(other.requesterUser)) {
			return false;
		}
		if (status == null) {
			if (other.status != null) {
				return false;
			}
		} else if (!status.equals(other.status)) {
			return false;
		}
		if (targetPerson == null) {
			if (other.targetPerson != null) {
				return false;
			}
		} else if (!targetPerson.equals(other.targetPerson)) {
			return false;
		}
		if (templateName == null) {
			if (other.templateName != null) {
				return false;
			}
		} else if (!templateName.equals(other.templateName)) {
			return false;
		}
		if (templateTypeName == null) {
			if (other.templateTypeName != null) {
				return false;
			}
		} else if (!templateTypeName.equals(other.templateTypeName)) {
			return false;
		}
		if (validDate == null) {
			if (other.validDate != null) {
				return false;
			}
		} else if (!validDate.equals(other.validDate)) {
			return false;
		}
		return true;
	}
	
	
	
		
}
