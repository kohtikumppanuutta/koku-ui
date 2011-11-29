package fi.arcusys.koku.kv.model;

import java.util.ArrayList;
import java.util.List;

import fi.arcusys.koku.util.MessageUtil;

/**
 * Request data model
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class KokuRequest {

	private long requestId;
	private String sender;
	private String subject;
	private String content;
	private int respondedAmount;
	private int missedAmount;
	private String creationDate;
	private String endDate;
	private String requestType;
	private List<KokuResponse> respondedList;
	private List<String> unrespondedList;
	private List<KokuQuestion> questions;
	
	public KokuRequest() {
		
	}
	
	public KokuRequest(fi.arcusys.koku.kv.requestservice.RequestSummary reqSum) {
		if (reqSum == null) {
			return;
		}
		requestId = reqSum.getRequestId();
		sender = reqSum.getSender();
		subject = reqSum.getSubject();
		respondedAmount = reqSum.getRespondedAmount();
		missedAmount = reqSum.getMissedAmout();
		creationDate = MessageUtil.formatTaskDate(reqSum.getCreationDate());
		endDate = MessageUtil.formatTaskDate(reqSum.getEndDate());
	}

	public KokuRequest(fi.arcusys.koku.kv.requestservice.Request req) {
		this((fi.arcusys.koku.kv.requestservice.RequestSummary) req);
		if (req == null) {
			return;
		}
		content = req.getContent();
		unrespondedList = req.getNotResponded();
		
		if (req.getResponses() != null) {
			for (fi.arcusys.koku.kv.requestservice.Response response : req.getResponses()) {
				getRespondedList().add(new KokuResponse(response));
			}
		}
		if (req.getQuestions() != null) {
			for (fi.arcusys.koku.kv.requestservice.Question question : req.getQuestions()) {
				getQuestions().add(new KokuQuestion(question));							
			}
		}
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getRespondedAmount() {
		return respondedAmount;
	}
	public void setRespondedAmount(int respondedAmount) {
		this.respondedAmount = respondedAmount;
	}
	public int getMissedAmount() {
		return missedAmount;
	}
	public void setMissedAmount(int missedAmount) {
		this.missedAmount = missedAmount;
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
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public final List<KokuResponse> getRespondedList() {
		if (respondedList== null) {
			respondedList = new ArrayList<KokuResponse>();
		}
		return respondedList;
	}
	public final void setRespondedList(List<KokuResponse> respondedList) {
		this.respondedList = respondedList;
	}
	public final List<String> getUnrespondedList() {
		if (respondedList == null) {
			respondedList = new ArrayList<KokuResponse>();
		}
		return unrespondedList;
	}
	public final void setUnrespondedList(List<String> unrespondedList) {
		this.unrespondedList = unrespondedList;
	}
	public final List<KokuQuestion> getQuestions() {
		if (questions == null) {
			questions = new ArrayList<KokuQuestion>();
		}
		return questions;
	}
	public final void setQuestions(List<KokuQuestion> questions) {
		this.questions = questions;
	}
	
	
	@Override
	public String toString() {
		return "KokuRequest [requestId=" + requestId + ", sender=" + sender
				+ ", subject=" + subject + ", content=" + content
				+ ", respondedAmount=" + respondedAmount + ", missedAmount="
				+ missedAmount + ", creationDate=" + creationDate
				+ ", endDate=" + endDate + ", requestType=" + requestType
				+ ", respondedList=" + respondedList + ", unrespondedList="
				+ unrespondedList + ", questions=" + questions + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + missedAmount;
		result = prime * result
				+ ((questions == null) ? 0 : questions.hashCode());
		result = prime * result + (int) (requestId ^ (requestId >>> 32));
		result = prime * result
				+ ((requestType == null) ? 0 : requestType.hashCode());
		result = prime * result + respondedAmount;
		result = prime * result
				+ ((respondedList == null) ? 0 : respondedList.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result
				+ ((unrespondedList == null) ? 0 : unrespondedList.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		KokuRequest other = (KokuRequest) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content)) {
			return false;
		}
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate)) {
			return false;
		}
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate)) {
			return false;
		}
		if (missedAmount != other.missedAmount) {
			return false;
		}
		if (questions == null) {
			if (other.questions != null)
				return false;
		} else if (!questions.equals(other.questions)) {
			return false;
		}
		if (requestId != other.requestId) {
			return false;
		}
		if (requestType == null) {
			if (other.requestType != null)
				return false;
		} else if (!requestType.equals(other.requestType)) {
			return false;
		}
		if (respondedAmount != other.respondedAmount) {
			return false;
		}
		if (respondedList == null) {
			if (other.respondedList != null)
				return false;
		} else if (!respondedList.equals(other.respondedList)) {
			return false;
		}
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender)) {
			return false;
		}
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject)) {
			return false;
		}
		if (unrespondedList == null) {
			if (other.unrespondedList != null)
				return false;
		} else if (!unrespondedList.equals(other.unrespondedList)) {
			return false;
		}
		return true;
	}

}
