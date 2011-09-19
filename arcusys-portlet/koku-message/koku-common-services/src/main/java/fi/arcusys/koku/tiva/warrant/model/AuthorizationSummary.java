package fi.arcusys.koku.tiva.warrant.model;

public class AuthorizationSummary {
	
	private Long authorizationId;
	private String receiverUid;
    private String senderUid;
    private AuthorizationStatus status;
    private String targetPersonUid;
    private Valtakirjapohja template;
    private Object validTill;
	
	public AuthorizationSummary(fi.arcusys.koku.tiva.warrant.citizenwarrantservice.AuthorizationShortSummary authShortSummary) {
		setAuthorizationId(authShortSummary.getAuthorizationId());
		setReceiverUid(authShortSummary.getReceiverUid());
		setSenderUid(authShortSummary.getSenderUid());
		setStatusAsString(authShortSummary.getStatus().toString());
		setTargetPersonUid(authShortSummary.getTargetPersonUid());
		setTemplateCitizen(authShortSummary.getTemplate());
		setValidTill(authShortSummary.getValidTill());
	}

	public AuthorizationSummary(fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationShortSummary authShortSummary) {
		setAuthorizationId(authShortSummary.getAuthorizationId());
		setReceiverUid(authShortSummary.getReceiverUid());
		setSenderUid(authShortSummary.getSenderUid());
		setStatusAsString(authShortSummary.getStatus().toString());
		setTargetPersonUid(authShortSummary.getTargetPersonUid());
		setTemplateEmployee(authShortSummary.getTemplate());
		setValidTill(authShortSummary.getValidTill());
	}

	private void setStatusAsString(String status) {
		setStatus(AuthorizationStatus.valueOf(status));
	}	

	private void setTemplateEmployee(fi.arcusys.koku.tiva.warrant.employeewarrantservice.Valtakirjapohja template) {
		setTemplate(new Valtakirjapohja(template));
	}

	private void setTemplateCitizen(fi.arcusys.koku.tiva.warrant.citizenwarrantservice.Valtakirjapohja template) {
		setTemplate(new Valtakirjapohja(template));		
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

	public AuthorizationStatus getStatus() {
		return status;
	}

	public void setStatus(AuthorizationStatus status) {
		this.status = status;
	}

	public String getTargetPersonUid() {
		return targetPersonUid;
	}

	public void setTargetPersonUid(String targetPersonUid) {
		this.targetPersonUid = targetPersonUid;
	}

	public Valtakirjapohja getTemplate() {
		return template;
	}

	public void setTemplate(Valtakirjapohja template) {
		this.template = template;
	}

	public Object getValidTill() {
		return validTill;
	}

	public void setValidTill(Object validTill) {
		this.validTill = validTill;
	}

	@Override
	public String toString() {
		return "AuthorizationSummary [authorizationId=" + authorizationId
				+ ", receiverUid=" + receiverUid + ", senderUid=" + senderUid
				+ ", status=" + status + ", targetPersonUid=" + targetPersonUid
				+ ", template=" + template + ", validTill=" + validTill + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authorizationId == null) ? 0 : authorizationId.hashCode());
		result = prime * result
				+ ((receiverUid == null) ? 0 : receiverUid.hashCode());
		result = prime * result
				+ ((senderUid == null) ? 0 : senderUid.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((targetPersonUid == null) ? 0 : targetPersonUid.hashCode());
		result = prime * result
				+ ((template == null) ? 0 : template.hashCode());
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
		AuthorizationSummary other = (AuthorizationSummary) obj;
		if (authorizationId == null) {
			if (other.authorizationId != null)
				return false;
		} else if (!authorizationId.equals(other.authorizationId))
			return false;
		if (receiverUid == null) {
			if (other.receiverUid != null)
				return false;
		} else if (!receiverUid.equals(other.receiverUid))
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
		if (validTill == null) {
			if (other.validTill != null)
				return false;
		} else if (!validTill.equals(other.validTill))
			return false;
		return true;
	}
	

}
