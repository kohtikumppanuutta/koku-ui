package fi.arcusys.koku.tiva.warrant.model;

import fi.arcusys.koku.util.MessageUtil;

public class KokuAuthorizationSummary {
	
	private Long authorizationId;
	private String receiverUid;
    private String senderUid;
    private String recieverName;
    private String senderName;
    private KokuAuthorizationStatus status;
    private String localizedStatus;
    private String targetPersonUid;
    private String targetPersonName;
    private KokuValtakirjapohja template;
    
    private String validTill;
    private String createdAt;
    private String givenAt;
    private String replyTill;
    private KokuAuthorizationCreateType type;
    private String localizedType;
        
	public KokuAuthorizationSummary(fi.arcusys.koku.tiva.warrant.citizenwarrantservice.AuthorizationShortSummary authShortSummary) {
		setAuthorizationId(authShortSummary.getAuthorizationId());
		setReceiverUid(authShortSummary.getReceiverUid());
		setSenderUid(authShortSummary.getSenderUid());
		setStatusAsString(authShortSummary.getStatus().toString());
		setTargetPersonUid(authShortSummary.getTargetPersonUid());
		setTemplateCitizen(authShortSummary.getTemplate());
		if (authShortSummary.getValidTill() != null) {
			setValidTill(MessageUtil.formatTaskDateByDay(authShortSummary.getValidTill()));
		}
	}
	
	public KokuAuthorizationSummary(fi.arcusys.koku.tiva.warrant.citizenwarrantservice.AuthorizationSummary authSummary) {
		this((fi.arcusys.koku.tiva.warrant.citizenwarrantservice.AuthorizationShortSummary) authSummary);
		setCreatedAt((MessageUtil.formatTaskDateByDay(authSummary.getCreatedAt())));
		setReplyTill((MessageUtil.formatTaskDateByDay(authSummary.getReplyTill())));
		setGivenAt(MessageUtil.formatTaskDateByDay(authSummary.getGivenAt()));
		setCreatedAt(MessageUtil.formatTaskDateByDay(authSummary.getCreatedAt()));
		setValidTill(MessageUtil.formatTaskDateByDay(authSummary.getValidTill()));
		setReceiverUid(authSummary.getReceiverUid());
		setSenderUid(authSummary.getSenderUid());
		setStatusAsString(authSummary.getStatus().toString());
		setTypeAsString(authSummary.getType().toString());
	}

	public KokuAuthorizationSummary(fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationShortSummary authShortSummary) {
		setAuthorizationId(authShortSummary.getAuthorizationId());
		setReceiverUid(authShortSummary.getReceiverUid());
		setSenderUid(authShortSummary.getSenderUid());
		setStatusAsString(authShortSummary.getStatus().toString());
		setTargetPersonUid(authShortSummary.getTargetPersonUid());
		setTemplateEmployee(authShortSummary.getTemplate());
		if (authShortSummary.getValidTill() != null) {
			setValidTill(MessageUtil.formatTaskDateByDay(authShortSummary.getValidTill()));			
		}
	}
	
	public KokuAuthorizationSummary(fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationSummary authSummary) {
		this((fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationShortSummary) authSummary);
		setCreatedAt((MessageUtil.formatTaskDateByDay(authSummary.getCreatedAt())));
		setReplyTill((MessageUtil.formatTaskDateByDay(authSummary.getReplyTill())));		
		setGivenAt(MessageUtil.formatTaskDateByDay(authSummary.getGivenAt()));
		setCreatedAt(MessageUtil.formatTaskDateByDay(authSummary.getCreatedAt()));
		setValidTill(MessageUtil.formatTaskDateByDay(authSummary.getValidTill()));
		setReceiverUid(authSummary.getReceiverUid());
		setSenderUid(authSummary.getSenderUid());
		setStatusAsString(authSummary.getStatus().toString());
		setTypeAsString(authSummary.getType().toString());
	}
	
	private void setStatusAsString(String status) {
		setStatus(KokuAuthorizationStatus.valueOf(status));
	}
	
	private void setTypeAsString(String type) {
		setType(KokuAuthorizationCreateType.valueOf(type));
	}	

	private void setTemplateEmployee(fi.arcusys.koku.tiva.warrant.employeewarrantservice.Valtakirjapohja template) {
		setTemplate(new KokuValtakirjapohja(template));
	}

	private void setTemplateCitizen(fi.arcusys.koku.tiva.warrant.citizenwarrantservice.Valtakirjapohja template) {
		setTemplate(new KokuValtakirjapohja(template));		
	}
	
	public Long getAuthorizationId() {
		return authorizationId;
	}

	public void setAuthorizationId(Long authorizationId) {
		this.authorizationId = authorizationId;
	}

	public String getReceiverUid() {
		return receiverUid;
	}

	public void setReceiverUid(String receiverUid) {
		this.receiverUid = receiverUid;
	}

	public String getSenderUid() {
		return senderUid;
	}

	public void setSenderUid(String senderUid) {
		this.senderUid = senderUid;
	}

	public KokuAuthorizationStatus getStatus() {
		return status;
	}

	public void setStatus(KokuAuthorizationStatus status) {
		this.status = status;
	}

	public String getTargetPersonUid() {
		return targetPersonUid;
	}

	public void setTargetPersonUid(String targetPersonUid) {
		this.targetPersonUid = targetPersonUid;
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

	public String getRecieverName() {
		return recieverName;
	}

	public void setRecieverName(String recieverName) {
		this.recieverName = recieverName;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
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

	public String getTargetPersonName() {
		return targetPersonName;
	}

	public void setTargetPersonName(String targetPersonName) {
		this.targetPersonName = targetPersonName;
	}

	@Override
	public String toString() {
		return "KokuAuthorizationSummary [authorizationId=" + authorizationId
				+ ", receiverUid=" + receiverUid + ", senderUid=" + senderUid
				+ ", recieverName=" + recieverName + ", senderName="
				+ senderName + ", status=" + status + ", targetPersonUid="
				+ targetPersonUid + ", template=" + template + ", validTill="
				+ validTill + ", createdAt=" + createdAt + ", givenAt="
				+ givenAt + ", replyTill=" + replyTill + ", type=" + type + "]";
	}

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
				+ ((receiverUid == null) ? 0 : receiverUid.hashCode());
		result = prime * result
				+ ((recieverName == null) ? 0 : recieverName.hashCode());
		result = prime * result
				+ ((replyTill == null) ? 0 : replyTill.hashCode());
		result = prime * result
				+ ((senderName == null) ? 0 : senderName.hashCode());
		result = prime * result
				+ ((senderUid == null) ? 0 : senderUid.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((targetPersonUid == null) ? 0 : targetPersonUid.hashCode());
		result = prime * result
				+ ((template == null) ? 0 : template.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((validTill == null) ? 0 : validTill.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KokuAuthorizationSummary other = (KokuAuthorizationSummary) obj;
		if (authorizationId == null) {
			if (other.authorizationId != null)
				return false;
		} else if (!authorizationId.equals(other.authorizationId))
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (givenAt == null) {
			if (other.givenAt != null)
				return false;
		} else if (!givenAt.equals(other.givenAt))
			return false;
		if (receiverUid == null) {
			if (other.receiverUid != null)
				return false;
		} else if (!receiverUid.equals(other.receiverUid))
			return false;
		if (recieverName == null) {
			if (other.recieverName != null)
				return false;
		} else if (!recieverName.equals(other.recieverName))
			return false;
		if (replyTill == null) {
			if (other.replyTill != null)
				return false;
		} else if (!replyTill.equals(other.replyTill))
			return false;
		if (senderName == null) {
			if (other.senderName != null)
				return false;
		} else if (!senderName.equals(other.senderName))
			return false;
		if (senderUid == null) {
			if (other.senderUid != null)
				return false;
		} else if (!senderUid.equals(other.senderUid))
			return false;
		if (status != other.status)
			return false;
		if (targetPersonUid == null) {
			if (other.targetPersonUid != null)
				return false;
		} else if (!targetPersonUid.equals(other.targetPersonUid))
			return false;
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (!template.equals(other.template))
			return false;
		if (type != other.type)
			return false;
		if (validTill == null) {
			if (other.validTill != null)
				return false;
		} else if (!validTill.equals(other.validTill))
			return false;
		return true;
	}
}
