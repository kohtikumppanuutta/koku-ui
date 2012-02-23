package fi.arcusys.koku.kv.model;

import java.util.ArrayList;
import java.util.List;

import fi.arcusys.koku.users.KokuUser;

/**
 * Message data model
 * @author Jinhua Chen
 * Jun 22, 2011
 */
public class KokuMessage {
	
	private long messageId;
	private KokuUser senderUser;
	private List<KokuUser> recipientUsers;	
	private String subject;
	private String content;	
	private String creationDate;
	private String messageType;
	private String messageStatus;
	private String messageStatusLocalized;
	private List<KokuUser> deliveryFailedTo;
	
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
	 * @return the recipientUsers
	 */
	public final List<KokuUser> getRecipientUsers() {
		if (recipientUsers == null) {
			recipientUsers = new ArrayList<KokuUser>();
		}
		return recipientUsers;
	}

	public long getMessageId() {
		return messageId;
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
	
	/**
	 * @return the messageStatusLocalized
	 */
	public final String getMessageStatusLocalized() {
		return messageStatusLocalized;
	}

	/**
	 * @param messageStatusLocalized the messageStatusLocalized to set
	 */
	public final void setMessageStatusLocalized(String messageStatusLocalized) {
		this.messageStatusLocalized = messageStatusLocalized;
	}

	public final List<KokuUser> getDeliveryFailedTo() {
		if (deliveryFailedTo == null) {
			deliveryFailedTo = new ArrayList<KokuUser>();
		}
		return deliveryFailedTo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KokuMessage [messageId=" + messageId + ", senderUser="
				+ senderUser + ", recipientUsers=" + recipientUsers
				+ ", subject=" + subject + ", content=" + content
				+ ", creationDate=" + creationDate + ", messageType="
				+ messageType + ", messageStatus=" + messageStatus
				+ ", deliveryFailedTo=" + deliveryFailedTo + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime
				* result
				+ ((deliveryFailedTo == null) ? 0 : deliveryFailedTo.hashCode());
		result = prime * result + (int) (messageId ^ (messageId >>> 32));
		result = prime * result
				+ ((messageStatus == null) ? 0 : messageStatus.hashCode());
		result = prime * result
				+ ((messageType == null) ? 0 : messageType.hashCode());
		result = prime * result
				+ ((recipientUsers == null) ? 0 : recipientUsers.hashCode());
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
		KokuMessage other = (KokuMessage) obj;
		if (content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (!content.equals(other.content)) {
			return false;
		}
		if (creationDate == null) {
			if (other.creationDate != null) {
				return false;
			}
		} else if (!creationDate.equals(other.creationDate)) {
			return false;
		}
		if (deliveryFailedTo == null) {
			if (other.deliveryFailedTo != null) {
				return false;
			}
		} else if (!deliveryFailedTo.equals(other.deliveryFailedTo)) {
			return false;
		}
		if (messageId != other.messageId) {
			return false;
		}
		if (messageStatus == null) {
			if (other.messageStatus != null) {
				return false;
			}
		} else if (!messageStatus.equals(other.messageStatus)) {
			return false;
		}
		if (messageType == null) {
			if (other.messageType != null) {
				return false;
			}
		} else if (!messageType.equals(other.messageType)) {
			return false;
		}
		if (recipientUsers == null) {
			if (other.recipientUsers != null) {
				return false;
			}
		} else if (!recipientUsers.equals(other.recipientUsers)) {
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
