package fi.arcusys.koku.tiva.tietopyynto.model;

import fi.arcusys.koku.tiva.tietopyynto.employee.InformationRequestSummary;
import fi.arcusys.koku.util.MessageUtil;

public class KokuInformationRequestSummary {
	
	private Long requestId;
	private String receiverUid;
	private String recieverName;
    private String senderUid;
    private String senderName;
    private KokuInformationRequestStatus status;
    private String localizedStatus;
    private String targetPersonUid;
    private String targetPersonName;
    private String title;
    private String validTill;
    
    
	public KokuInformationRequestSummary(InformationRequestSummary summary) {
		this.receiverUid = summary.getReceiverUid();
		this.requestId = summary.getRequestId();
		this.senderUid = summary.getSenderUid();
		setStatusAsString(summary.getStatus().toString());
		this.targetPersonUid = summary.getTargetPersonUid();
		this.title = summary.getTitle();
		this.validTill = MessageUtil.formatTaskDate(summary.getValidTill());
	}
	
	private final void setStatusAsString(String status) {
		setStatus(KokuInformationRequestStatus.fromValue(status));
	}
	
	public String getReceiverUid() {
		return receiverUid;
	}
	public void setReceiverUid(String receiverUid) {
		this.receiverUid = receiverUid;
	}
	public Long getRequestId() {
		return requestId;
	}
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	public String getSenderUid() {
		return senderUid;
	}
	public void setSenderUid(String senderUid) {
		this.senderUid = senderUid;
	}
	public KokuInformationRequestStatus getStatus() {
		return status;
	}
	public void setStatus(KokuInformationRequestStatus kokuInformationRequestStatus) {
		this.status = kokuInformationRequestStatus;
	}
	public String getTargetPersonUid() {
		return targetPersonUid;
	}
	public void setTargetPersonUid(String targetPersonUid) {
		this.targetPersonUid = targetPersonUid;
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

	public String getRecieverName() {
		return recieverName;
	}

	public void setRecieverName(String receiverName) {
		this.recieverName = receiverName;
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

	public String getTargetPersonName() {
		return targetPersonName;
	}

	public void setTargetPersonName(String targetPersonName) {
		this.targetPersonName = targetPersonName;
	}

	@Override
	public String toString() {
		return "KokuInformationRequestSummary [requestId=" + requestId
				+ ", receiverUid=" + receiverUid + ", receiverName="
				+ recieverName + ", senderUid=" + senderUid + ", senderName="
				+ senderName + ", status=" + status + ", localizedStatus="
				+ localizedStatus + ", targetPersonUid=" + targetPersonUid
				+ ", targetPersonName=" + targetPersonName + ", title=" + title
				+ ", validTill=" + validTill + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((localizedStatus == null) ? 0 : localizedStatus.hashCode());
		result = prime * result
				+ ((recieverName == null) ? 0 : recieverName.hashCode());
		result = prime * result
				+ ((receiverUid == null) ? 0 : receiverUid.hashCode());
		result = prime * result
				+ ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result
				+ ((senderName == null) ? 0 : senderName.hashCode());
		result = prime * result
				+ ((senderUid == null) ? 0 : senderUid.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime
				* result
				+ ((targetPersonName == null) ? 0 : targetPersonName.hashCode());
		result = prime * result
				+ ((targetPersonUid == null) ? 0 : targetPersonUid.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		KokuInformationRequestSummary other = (KokuInformationRequestSummary) obj;
		if (localizedStatus == null) {
			if (other.localizedStatus != null)
				return false;
		} else if (!localizedStatus.equals(other.localizedStatus))
			return false;
		if (recieverName == null) {
			if (other.recieverName != null)
				return false;
		} else if (!recieverName.equals(other.recieverName))
			return false;
		if (receiverUid == null) {
			if (other.receiverUid != null)
				return false;
		} else if (!receiverUid.equals(other.receiverUid))
			return false;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
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
		if (targetPersonName == null) {
			if (other.targetPersonName != null)
				return false;
		} else if (!targetPersonName.equals(other.targetPersonName))
			return false;
		if (targetPersonUid == null) {
			if (other.targetPersonUid != null)
				return false;
		} else if (!targetPersonUid.equals(other.targetPersonUid))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (validTill == null) {
			if (other.validTill != null)
				return false;
		} else if (!validTill.equals(other.validTill))
			return false;
		return true;
	}
	
}