package fi.arcusys.koku.kv.model;

import fi.arcusys.koku.kv.requestservice.RequestShortSummary;
import fi.arcusys.koku.util.MessageUtil;

public class KokuRequestShortSummary {
	
    private String creationDate;
    private String endDate;
    private long requestId;
    private String sender;
    private String subject;
    
    
	public KokuRequestShortSummary(RequestShortSummary request) {
		if (request != null) {			
			creationDate = MessageUtil.formatTaskDateByDay(request.getCreationDate());
			endDate = MessageUtil.formatTaskDateByDay(request.getEndDate());
			requestId = request.getRequestId();
			sender = request.getSender();
			subject = request.getSubject();
		}
	}	
	
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public long getRequestId() {
		return requestId;
	}
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@Override
	public String toString() {
		return "KokuRequestShortSummary [creationDate=" + creationDate
				+ ", endDate=" + endDate + ", requestId=" + requestId
				+ ", sender=" + sender + ", subject=" + subject
				+ ", getCreationDate()=" + getCreationDate()
				+ ", getEndDate()=" + getEndDate() + ", getRequestId()="
				+ getRequestId() + ", getSender()=" + getSender()
				+ ", getSubject()=" + getSubject() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + (int) (requestId ^ (requestId >>> 32));
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
		KokuRequestShortSummary other = (KokuRequestShortSummary) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (requestId != other.requestId)
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}
    
}
