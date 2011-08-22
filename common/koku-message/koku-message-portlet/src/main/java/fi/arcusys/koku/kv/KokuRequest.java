package fi.arcusys.koku.kv;

import java.util.List;

import fi.arcusys.koku.kv.requestservice.Question;
import fi.arcusys.koku.kv.requestservice.Response;


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
	private List<Response> respondedList;
	private List<String> unrespondedList;
	private List<Question> questions;
	
	/* getters */
	public long getRequestId() {
		return requestId;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public String getContent() {
		return content;
	}
	
	public int getRespondedAmount() {
		return respondedAmount;
	}
	
	public int getMissedAmount() {
		return missedAmount;
	}
	
	public String getCreationDate() {
		return creationDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public String getRequestType() {
		return requestType;
	}
	
	public List<Response> getRespondedList() {
		return respondedList;
	}
	
	public List<String> getUnrespondedList() {
		return unrespondedList;
	}
	
	public List<Question> getQuestions() {
		return questions;
	}
	/* setters */
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setRespondedAmount(int respondedAmount) {
		this.respondedAmount = respondedAmount;
	}
	
	public void setMissedAmount(int missedAmount) {
		this.missedAmount = missedAmount;
	}
	
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
	public void setRespondedList(List<Response> respondedList) {
		this.respondedList = respondedList;
	}
	
	public void setUnrespondedList(List<String> unrespondedList) {
		this.unrespondedList = unrespondedList;
	}
	
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
}
