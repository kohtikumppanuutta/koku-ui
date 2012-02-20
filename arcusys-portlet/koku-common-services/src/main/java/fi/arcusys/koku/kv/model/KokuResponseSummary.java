package fi.arcusys.koku.kv.model;

import fi.arcusys.koku.kv.requestservice.ResponseSummary;


public class KokuResponseSummary {
	
    private String replierUid;
    private String replierName;
    private KokuRequestShortSummary request;
    private Long responseId;
    
    
	public KokuResponseSummary(ResponseSummary summary) {
		if (summary != null) {
			replierUid = summary.getReplierUid();
			responseId = summary.getResponseId();
			if (summary.getRequest() != null) {
				request = new KokuRequestShortSummary(summary.getRequest());			
			}
		}
	}
	public String getReplierUid() {
		return replierUid;
	}
	public void setReplierUid(String replierUid) {
		this.replierUid = replierUid;
	}
	public KokuRequestShortSummary getRequest() {
		return request;
	}
	public void setRequest(KokuRequestShortSummary request) {
		this.request = request;
	}
	public Long getResponseId() {
		return responseId;
	}
	public void setResponseId(Long responseId) {
		this.responseId = responseId;
	}
	
	public String getReplierName() {
		return replierName;
	}
	public void setReplierName(String replierName) {
		this.replierName = replierName;
	}

	
	
	@Override
	public String toString() {
		return "KokuResponseSummary [replierUid=" + replierUid
				+ ", replierName=" + replierName + ", request=" + request
				+ ", responseId=" + responseId + "]";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((replierName == null) ? 0 : replierName.hashCode());
		result = prime * result
				+ ((replierUid == null) ? 0 : replierUid.hashCode());
		result = prime * result + ((request == null) ? 0 : request.hashCode());
		result = prime * result
				+ ((responseId == null) ? 0 : responseId.hashCode());
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
		KokuResponseSummary other = (KokuResponseSummary) obj;
		if (replierName == null) {
			if (other.replierName != null) {
				return false;
			}
		} else if (!replierName.equals(other.replierName)) {
			return false;
		}
		if (replierUid == null) {
			if (other.replierUid != null) {
				return false;
			}
		} else if (!replierUid.equals(other.replierUid)) {
			return false;
		}
		if (request == null) {
			if (other.request != null) {
				return false;
			}
		} else if (!request.equals(other.request)) {
			return false;
		}
		if (responseId == null) {
			if (other.responseId != null) {
				return false;
			}
		} else if (!responseId.equals(other.responseId)) {
			return false;
		}
		return true;
	}	
}
