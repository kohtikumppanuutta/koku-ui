package fi.arcusys.koku.tiva.tietopyynto.model;

import java.util.LinkedList;
import java.util.List;

import fi.arcusys.koku.tiva.tietopyynto.employee.InformationRequestDetail;
import fi.arcusys.koku.util.MessageUtil;

public class KokuInformationRequestDetail extends KokuInformationRequestSummary {

	
	private KokuInformationAccessType accessType;
	private String localizedAccessType;
	private String additionalInfo;
	private String additionalReplyInfo;
	private String attachmentURL;
	private List<String> categories;
	private String createdDate;
	private String description;
	private String informationDetails;
	private String legislationInfo;
	private String replyDescription;
	private String requestPurpose;
	
	
	
	public KokuInformationRequestDetail(InformationRequestDetail requestDetails) {
		super(requestDetails);
		if (requestDetails == null) {
			throw new IllegalArgumentException("InformationRequestDetail can't be null!");
		}
		this.additionalInfo = requestDetails.getAdditionalInfo();
		this.attachmentURL = requestDetails.getAttachmentURL();
		if (requestDetails.getCategories() == null) {
			this.categories = new LinkedList<String>();
		} else {
			this.categories = requestDetails.getCategories();			
		}
		this.createdDate = MessageUtil.formatTaskDateByDay(requestDetails.getCreatedDate());
		this.description = requestDetails.getDescription();
		this.informationDetails = requestDetails.getInformationDetails();
		this.legislationInfo = requestDetails.getLegislationInfo();
		this.replyDescription = requestDetails.getReplyDescription();
		this.requestPurpose = requestDetails.getRequestPurpose();
		if (requestDetails.getAccessType() != null) {
			setAccessTypeAsString(requestDetails.getAccessType().toString());	
		}
	}
	
	private final void setAccessTypeAsString(String accessType) {
		setAccessType(KokuInformationAccessType.fromValue(accessType));
	}

	
	public String getLocalizedAccessType() {
		return localizedAccessType;
	}

	public void setLocalizedAccessType(String localizedAccessType) {
		this.localizedAccessType = localizedAccessType;
	}

	public KokuInformationAccessType getAccessType() {
		return accessType;
	}
	public void setAccessType(KokuInformationAccessType accessType) {
		this.accessType = accessType;
	}
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	public String getAdditionalReplyInfo() {
		return additionalReplyInfo;
	}
	public void setAdditionalReplyInfo(String additionalReplyInfo) {
		this.additionalReplyInfo = additionalReplyInfo;
	}
	public String getAttachmentURL() {
		return attachmentURL;
	}
	public void setAttachmentURL(String attachmentURL) {
		this.attachmentURL = attachmentURL;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getInformationDetails() {
		return informationDetails;
	}
	public void setInformationDetails(String informationDetails) {
		this.informationDetails = informationDetails;
	}
	public String getLegislationInfo() {
		return legislationInfo;
	}
	public void setLegislationInfo(String legislationInfo) {
		this.legislationInfo = legislationInfo;
	}
	public String getReplyDescription() {
		return replyDescription;
	}
	public void setReplyDescription(String replyDescription) {
		this.replyDescription = replyDescription;
	}
	public String getRequestPurpose() {
		return requestPurpose;
	}
	public void setRequestPurpose(String requestPurpose) {
		this.requestPurpose = requestPurpose;
	}
	
	@Override
	public String toString() {
		return "KokuInformationRequestDetail [accessType=" + accessType
				+ ", additionalInfo=" + additionalInfo
				+ ", additionalReplyInfo=" + additionalReplyInfo
				+ ", attachmentURL=" + attachmentURL + ", categories="
				+ categories + ", createdDate=" + createdDate
				+ ", description=" + description + ", informationDetails="
				+ informationDetails + ", legislationInfo=" + legislationInfo
				+ ", replyDescription=" + replyDescription
				+ ", requestPurpose=" + requestPurpose + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accessType == null) ? 0 : accessType.hashCode());
		result = prime * result
				+ ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
		result = prime
				* result
				+ ((additionalReplyInfo == null) ? 0 : additionalReplyInfo
						.hashCode());
		result = prime * result
				+ ((attachmentURL == null) ? 0 : attachmentURL.hashCode());
		result = prime * result
				+ ((categories == null) ? 0 : categories.hashCode());
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime
				* result
				+ ((informationDetails == null) ? 0 : informationDetails
						.hashCode());
		result = prime * result
				+ ((legislationInfo == null) ? 0 : legislationInfo.hashCode());
		result = prime
				* result
				+ ((replyDescription == null) ? 0 : replyDescription.hashCode());
		result = prime * result
				+ ((requestPurpose == null) ? 0 : requestPurpose.hashCode());
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
		KokuInformationRequestDetail other = (KokuInformationRequestDetail) obj;
		if (accessType != other.accessType)
			return false;
		if (additionalInfo == null) {
			if (other.additionalInfo != null)
				return false;
		} else if (!additionalInfo.equals(other.additionalInfo))
			return false;
		if (additionalReplyInfo == null) {
			if (other.additionalReplyInfo != null)
				return false;
		} else if (!additionalReplyInfo.equals(other.additionalReplyInfo))
			return false;
		if (attachmentURL == null) {
			if (other.attachmentURL != null)
				return false;
		} else if (!attachmentURL.equals(other.attachmentURL))
			return false;
		if (categories == null) {
			if (other.categories != null)
				return false;
		} else if (!categories.equals(other.categories))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (informationDetails == null) {
			if (other.informationDetails != null)
				return false;
		} else if (!informationDetails.equals(other.informationDetails))
			return false;
		if (legislationInfo == null) {
			if (other.legislationInfo != null)
				return false;
		} else if (!legislationInfo.equals(other.legislationInfo))
			return false;
		if (replyDescription == null) {
			if (other.replyDescription != null)
				return false;
		} else if (!replyDescription.equals(other.replyDescription))
			return false;
		if (requestPurpose == null) {
			if (other.requestPurpose != null)
				return false;
		} else if (!requestPurpose.equals(other.requestPurpose))
			return false;
		return true;
	}
	
}
