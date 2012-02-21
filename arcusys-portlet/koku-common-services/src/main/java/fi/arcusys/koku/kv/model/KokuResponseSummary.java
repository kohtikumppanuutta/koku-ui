package fi.arcusys.koku.kv.model;

import fi.arcusys.koku.kv.requestservice.ResponseSummary;
import fi.arcusys.koku.users.KokuUser;


public class KokuResponseSummary {
	
	private KokuUser replierUser;
	private KokuRequestShortSummary request;
    private Long responseId;
    
    
	public KokuResponseSummary(ResponseSummary summary) {
		if (summary != null) {
			replierUser = new KokuUser(summary.getReplierUserInfo());
			responseId = summary.getResponseId();
			if (summary.getRequest() != null) {
				request = new KokuRequestShortSummary(summary.getRequest());			
			}
		}
	}
	
	/**
	 * @return the replierUser
	 */
	public final KokuUser getReplierUser() {
		return replierUser;
	}

	/**
	 * @param replierUser the replierUser to set
	 */
	public final void setReplierUser(KokuUser replierUser) {
		this.replierUser = replierUser;
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
	

}
