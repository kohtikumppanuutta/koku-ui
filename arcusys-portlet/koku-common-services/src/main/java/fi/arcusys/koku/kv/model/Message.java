package fi.arcusys.koku.kv.model;

import java.util.ArrayList;
import java.util.List;

import fi.arcusys.koku.users.KokuUser;

/**
 * Message data model
 * @author Jinhua Chen
 * Jun 22, 2011
 */
public class Message {
	
	private long messageId;
	private String sender;
	private String recipients;
	private String subject;
	private String content;	
	private String creationDate;
	private String messageType;
	private String messageStatus;
	private List<KokuUser> deliveryFailedTo;
	
	public long getMessageId() {
		return messageId;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getRecipients() {
		return recipients;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getCreationDate() {
		return creationDate;
	}
	
	public String getMessageType() {
		return messageType;
	}
	
	public String getMessageStatus() {
		return messageStatus;
	}
	
	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}

	public final List<KokuUser> getDeliveryFailedTo() {
		if (deliveryFailedTo == null) {
			deliveryFailedTo = new ArrayList<KokuUser>();
		}
		return deliveryFailedTo;
	}

}
