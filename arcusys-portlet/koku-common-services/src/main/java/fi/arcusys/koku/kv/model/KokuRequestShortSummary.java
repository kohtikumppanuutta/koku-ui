package fi.arcusys.koku.kv.model;

import fi.arcusys.koku.kv.requestservice.RequestShortSummary;
import fi.arcusys.koku.users.KokuUser;
import fi.arcusys.koku.util.MessageUtil;

public class KokuRequestShortSummary {
	
    private String creationDate;
    private String endDate;
    private long requestId;
    private KokuUser senderUser;
    private String subject;
    
    
	public KokuRequestShortSummary(RequestShortSummary request) {
		if (request != null) {			
			creationDate = MessageUtil.formatTaskDateByDay(request.getCreationDate());
			endDate = MessageUtil.formatTaskDateByDay(request.getEndDate());
			requestId = request.getRequestId();
			senderUser = new KokuUser(request.getSenderUserInfo());
			subject = request.getSubject();
		}
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
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KokuRequestShortSummary [creationDate=" + creationDate
				+ ", endDate=" + endDate + ", requestId=" + requestId
				+ ", senderUser=" + senderUser + ", subject=" + subject + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + (int) (requestId ^ (requestId >>> 32));
		result = prime * result
				+ ((senderUser == null) ? 0 : senderUser.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
		KokuRequestShortSummary other = (KokuRequestShortSummary) obj;
		if (creationDate == null) {
			if (other.creationDate != null) {
				return false;
			}
		} else if (!creationDate.equals(other.creationDate)) {
			return false;
		}
		if (endDate == null) {
			if (other.endDate != null) {
				return false;
			}
		} else if (!endDate.equals(other.endDate)) {
			return false;
		}
		if (requestId != other.requestId) {
			return false;
		}
		if (senderUser == null) {
			if (other.senderUser != null) {
				return false;
			}
		} else if (!senderUser.equals(other.senderUser)) {
			return false;
		}
		if (subject == null) {
			if (other.subject != null) {
				return false;
			}
		} else if (!subject.equals(other.subject)) {
			return false;
		}
		return true;
	}
	
}
