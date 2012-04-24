package fi.arcusys.koku.tiva.tietopyynto.model;

import fi.arcusys.koku.tiva.tietopyynto.employee.InformationRequestSummary;
import fi.arcusys.koku.users.KokuUser;
import fi.arcusys.koku.util.MessageUtil;

public class KokuInformationRequestSummary {
	
	private Long requestId;
	private KokuUser recieverUser;
	private KokuUser senderUser;
	private KokuUser targetPersonUser;
    private KokuInformationRequestStatus status;
    private String localizedStatus;
    private String recieverRoleUid;
    private String title;
    private String validTill;
        
	public KokuInformationRequestSummary(InformationRequestSummary summary) {
		this.requestId = summary.getRequestId();
		this.recieverUser = new KokuUser(summary.getReceiverUserInfo());
		this.senderUser = new KokuUser(summary.getSenderUserInfo());
		setStatusAsString(summary.getStatus().value());
		this.targetPersonUser = new KokuUser(summary.getTargetPersonUserInfo());
		this.title = summary.getTitle();
		this.recieverRoleUid = summary.getReceiverRoleUid();
		this.validTill = MessageUtil.formatTaskDateByDay(summary.getValidTill());
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
		setStatus(KokuInformationRequestStatus.fromValue(status));
	}
	
	public Long getRequestId() {
		return requestId;
	}
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	public KokuInformationRequestStatus getStatus() {
		return status;
	}
	public final void setStatus(KokuInformationRequestStatus kokuInformationRequestStatus) {
		this.status = kokuInformationRequestStatus;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getValidTill() {
		return validTill;
	}
	public void setValidTill(String validTill) {
		this.validTill = validTill;
	}

	public String getLocalizedStatus() {
		return localizedStatus;
	}

	public void setLocalizedStatus(String localizedStatus) {
		this.localizedStatus = localizedStatus;
	}

	public String getRecieverRoleUid() {
		return recieverRoleUid;
	}

	public void setRecieverRoleUid(String recieverRoleUid) {
		this.recieverRoleUid = recieverRoleUid;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((localizedStatus == null) ? 0 : localizedStatus.hashCode());
		result = prime * result
				+ ((recieverRoleUid == null) ? 0 : recieverRoleUid.hashCode());
		result = prime * result
				+ ((recieverUser == null) ? 0 : recieverUser.hashCode());
		result = prime * result
				+ ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result
				+ ((senderUser == null) ? 0 : senderUser.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime
				* result
				+ ((targetPersonUser == null) ? 0 : targetPersonUser.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		KokuInformationRequestSummary other = (KokuInformationRequestSummary) obj;
		if (localizedStatus == null) {
			if (other.localizedStatus != null) {
				return false;
			}
		} else if (!localizedStatus.equals(other.localizedStatus)) {
			return false;
		}
		if (recieverRoleUid == null) {
			if (other.recieverRoleUid != null) {
				return false;
			}
		} else if (!recieverRoleUid.equals(other.recieverRoleUid)) {
			return false;
		}
		if (recieverUser == null) {
			if (other.recieverUser != null) {
				return false;
			}
		} else if (!recieverUser.equals(other.recieverUser)) {
			return false;
		}
		if (requestId == null) {
			if (other.requestId != null) {
				return false;
			}
		} else if (!requestId.equals(other.requestId)) {
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
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KokuInformationRequestSummary [requestId=" + requestId
				+ ", recieverUser=" + recieverUser + ", senderUser="
				+ senderUser + ", targetPersonUser=" + targetPersonUser
				+ ", status=" + status + ", localizedStatus=" + localizedStatus
				+ ", recieverRoleUid=" + recieverRoleUid + ", title=" + title
				+ ", validTill=" + validTill + "]";
	}


	
}