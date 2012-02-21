package fi.arcusys.koku.tiva.warrant.model;

import fi.arcusys.koku.users.KokuUser;
import fi.arcusys.koku.util.MessageUtil;

public class KokuAuthorizationSummary {
	
	private Long authorizationId;	
	private KokuUser senderUser;
	private KokuUser recieverUser;
	private KokuUser targetPersonUser;
	
    private KokuAuthorizationStatus status;
    private String localizedStatus;
    private KokuValtakirjapohja template;
    
    private String validTill;
    private String createdAt;
    private String givenAt;
    private String replyTill;
    private KokuAuthorizationCreateType type;
    private String localizedType;
        
	public KokuAuthorizationSummary(fi.arcusys.koku.tiva.warrant.citizenwarrantservice.AuthorizationShortSummary authShortSummary) {
		authorizationId = authShortSummary.getAuthorizationId();
		senderUser = new KokuUser(authShortSummary.getSenderUserInfo());
		recieverUser = new KokuUser(authShortSummary.getReceiverUserInfo());
		targetPersonUser = new KokuUser(authShortSummary.getTargetPersonUserInfo());
		setStatusAsString(authShortSummary.getStatus().toString());
		setTemplateCitizen(authShortSummary.getTemplate());
		if (authShortSummary.getValidTill() != null) {
			validTill = MessageUtil.formatTaskDateByDay(authShortSummary.getValidTill());
		}
	}
	
	public KokuAuthorizationSummary(fi.arcusys.koku.tiva.warrant.citizenwarrantservice.AuthorizationSummary authSummary) {
		this((fi.arcusys.koku.tiva.warrant.citizenwarrantservice.AuthorizationShortSummary) authSummary);
		createdAt = MessageUtil.formatTaskDateByDay(authSummary.getCreatedAt());
		replyTill = MessageUtil.formatTaskDateByDay(authSummary.getReplyTill());		
		givenAt = MessageUtil.formatTaskDateByDay(authSummary.getGivenAt());
		createdAt = MessageUtil.formatTaskDateByDay(authSummary.getCreatedAt());
		validTill = MessageUtil.formatTaskDateByDay(authSummary.getValidTill());
		setStatusAsString(authSummary.getStatus().toString());
		setTypeAsString(authSummary.getType().toString());
	}

	public KokuAuthorizationSummary(fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationShortSummary authShortSummary) {
		authorizationId = authShortSummary.getAuthorizationId();
		senderUser = new KokuUser(authShortSummary.getSenderUserInfo());
		recieverUser = new KokuUser(authShortSummary.getReceiverUserInfo());
		targetPersonUser = new KokuUser(authShortSummary.getTargetPersonUserInfo());
		setStatusAsString(authShortSummary.getStatus().toString());
		setTemplateEmployee(authShortSummary.getTemplate());
		if (authShortSummary.getValidTill() != null) {
			validTill = MessageUtil.formatTaskDateByDay(authShortSummary.getValidTill());
		}
	}
	
	public KokuAuthorizationSummary(fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationSummary authSummary) {
		this((fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationShortSummary) authSummary);
		createdAt = MessageUtil.formatTaskDateByDay(authSummary.getCreatedAt());
		replyTill = MessageUtil.formatTaskDateByDay(authSummary.getReplyTill());		
		givenAt = MessageUtil.formatTaskDateByDay(authSummary.getGivenAt());
		createdAt = MessageUtil.formatTaskDateByDay(authSummary.getCreatedAt());
		validTill = MessageUtil.formatTaskDateByDay(authSummary.getValidTill());
		setStatusAsString(authSummary.getStatus().toString());
		setTypeAsString(authSummary.getType().toString());
	}
	
	
	
	/**
	 * @return the senderUser
	 */
	public final KokuUser getSenderUser() {
		return senderUser;
	}

	/**
	 * @param senderUser the senderUser to set
	 */
	public final void setSenderUser(KokuUser senderUser) {
		this.senderUser = senderUser;
	}

	/**
	 * @return the recieverUser
	 */
	public final KokuUser getRecieverUser() {
		return recieverUser;
	}

	/**
	 * @param recieverUser the recieverUser to set
	 */
	public final void setRecieverUser(KokuUser recieverUser) {
		this.recieverUser = recieverUser;
	}

	/**
	 * @return the targetPersonUser
	 */
	public final KokuUser getTargetPersonUser() {
		return targetPersonUser;
	}

	/**
	 * @param targetPersonUser the targetPersonUser to set
	 */
	public final void setTargetPersonUser(KokuUser targetPersonUser) {
		this.targetPersonUser = targetPersonUser;
	}

	protected final void setStatusAsString(String status) {
		setStatus(KokuAuthorizationStatus.valueOf(status));
	}
	
	protected final void setTypeAsString(String type) {
		setType(KokuAuthorizationCreateType.valueOf(type));
	}

	protected final void setTemplateEmployee(fi.arcusys.koku.tiva.warrant.employeewarrantservice.Valtakirjapohja template) {
		setTemplate(new KokuValtakirjapohja(template));
	}

	protected final void setTemplateCitizen(fi.arcusys.koku.tiva.warrant.citizenwarrantservice.Valtakirjapohja template) {
		setTemplate(new KokuValtakirjapohja(template));		
	}
	
	public Long getAuthorizationId() {
		return authorizationId;
	}

	public void setAuthorizationId(Long authorizationId) {
		this.authorizationId = authorizationId;
	}

	public KokuAuthorizationStatus getStatus() {
		return status;
	}

	public void setStatus(KokuAuthorizationStatus status) {
		this.status = status;
	}

	public KokuValtakirjapohja getTemplate() {
		return template;
	}

	public void setTemplate(KokuValtakirjapohja template) {
		this.template = template;
	}

	public String getValidTill() {
		return validTill;
	}

	public void setValidTill(String validTill) {
		this.validTill = validTill;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getGivenAt() {
		return givenAt;
	}

	public void setGivenAt(String givenAt) {
		this.givenAt = givenAt;
	}

	public String getReplyTill() {
		return replyTill;
	}

	public void setReplyTill(String replyTill) {
		this.replyTill = replyTill;
	}

	public KokuAuthorizationCreateType getType() {
		return type;
	}

	public void setType(KokuAuthorizationCreateType type) {
		this.type = type;
	}

	public String getLocalizedStatus() {
		return localizedStatus;
	}

	public void setLocalizedStatus(String localizedStatus) {
		this.localizedStatus = localizedStatus;
	}

	public String getLocalizedType() {
		return localizedType;
	}

	public void setLocalizedType(String localizedType) {
		this.localizedType = localizedType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KokuAuthorizationSummary [authorizationId=" + authorizationId
				+ ", senderUser=" + senderUser + ", recieverUser="
				+ recieverUser + ", targetPersonUser=" + targetPersonUser
				+ ", status=" + status + ", localizedStatus=" + localizedStatus
				+ ", template=" + template + ", validTill=" + validTill
				+ ", createdAt=" + createdAt + ", givenAt=" + givenAt
				+ ", replyTill=" + replyTill + ", type=" + type
				+ ", localizedType=" + localizedType + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authorizationId == null) ? 0 : authorizationId.hashCode());
		result = prime * result
				+ ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((givenAt == null) ? 0 : givenAt.hashCode());
		result = prime * result
				+ ((localizedStatus == null) ? 0 : localizedStatus.hashCode());
		result = prime * result
				+ ((localizedType == null) ? 0 : localizedType.hashCode());
		result = prime * result
				+ ((recieverUser == null) ? 0 : recieverUser.hashCode());
		result = prime * result
				+ ((replyTill == null) ? 0 : replyTill.hashCode());
		result = prime * result
				+ ((senderUser == null) ? 0 : senderUser.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime
				* result
				+ ((targetPersonUser == null) ? 0 : targetPersonUser.hashCode());
		result = prime * result
				+ ((template == null) ? 0 : template.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((validTill == null) ? 0 : validTill.hashCode());
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
		KokuAuthorizationSummary other = (KokuAuthorizationSummary) obj;
		if (authorizationId == null) {
			if (other.authorizationId != null) {
				return false;
			}
		} else if (!authorizationId.equals(other.authorizationId)) {
			return false;
		}
		if (createdAt == null) {
			if (other.createdAt != null) {
				return false;
			}
		} else if (!createdAt.equals(other.createdAt)) {
			return false;
		}
		if (givenAt == null) {
			if (other.givenAt != null) {
				return false;
			}
		} else if (!givenAt.equals(other.givenAt)) {
			return false;
		}
		if (localizedStatus == null) {
			if (other.localizedStatus != null) {
				return false;
			}
		} else if (!localizedStatus.equals(other.localizedStatus)) {
			return false;
		}
		if (localizedType == null) {
			if (other.localizedType != null) {
				return false;
			}
		} else if (!localizedType.equals(other.localizedType)) {
			return false;
		}
		if (recieverUser == null) {
			if (other.recieverUser != null) {
				return false;
			}
		} else if (!recieverUser.equals(other.recieverUser)) {
			return false;
		}
		if (replyTill == null) {
			if (other.replyTill != null) {
				return false;
			}
		} else if (!replyTill.equals(other.replyTill)) {
			return false;
		}
		if (senderUser == null) {
			if (other.senderUser != null) {
				return false;
			}
		} else if (!senderUser.equals(other.senderUser)) {
			return false;
		}
		if (status != other.status) {
			return false;
		}
		if (targetPersonUser == null) {
			if (other.targetPersonUser != null) {
				return false;
			}
		} else if (!targetPersonUser.equals(other.targetPersonUser)) {
			return false;
		}
		if (template == null) {
			if (other.template != null) {
				return false;
			}
		} else if (!template.equals(other.template)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		if (validTill == null) {
			if (other.validTill != null) {
				return false;
			}
		} else if (!validTill.equals(other.validTill)) {
			return false;
		}
		return true;
	}

	
}
