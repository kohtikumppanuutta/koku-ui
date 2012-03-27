package fi.arcusys.koku.av;

import fi.arcusys.koku.users.KokuUser;

/**
 * Summary appointment data model
 * @author Jinhua Chen
 * Aug 23, 2011
 */
public class KokuAppointment {

	private long appointmentId;
	private KokuUser senderUser;
	private KokuUser receivingUser;
	private String subject;
	private String description;
	private String status;
	private String cancellationComment;
	
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

	/* getters */
	public long getAppointmentId() {
		return appointmentId;
	}
	
	
	public String getSubject() {
		return subject;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getStatus() {
		return status;
	}
	/* setters */
	public void setAppointmentId(long appointmentId) {
		this.appointmentId = appointmentId;
	}
		
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public final String getCancellationComment() {
		return cancellationComment;
	}

	public final void setCancellationComment(String cancellationComment) {
		this.cancellationComment = cancellationComment;
	}

	/**
	 * @return the receivingUser
	 */
	public KokuUser getReceivingUser() {
		return receivingUser;
	}

	/**
	 * @param receivingUser the receivingUser to set
	 */
	public void setReceivingUser(KokuUser receivingUser) {
		this.receivingUser = receivingUser;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (appointmentId ^ (appointmentId >>> 32));
		result = prime
				* result
				+ ((cancellationComment == null) ? 0 : cancellationComment
						.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((receivingUser == null) ? 0 : receivingUser.hashCode());
		result = prime * result
				+ ((senderUser == null) ? 0 : senderUser.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		KokuAppointment other = (KokuAppointment) obj;
		if (appointmentId != other.appointmentId) {
			return false;
		}
		if (cancellationComment == null) {
			if (other.cancellationComment != null) {
				return false;
			}
		} else if (!cancellationComment.equals(other.cancellationComment)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (receivingUser == null) {
			if (other.receivingUser != null) {
				return false;
			}
		} else if (!receivingUser.equals(other.receivingUser)) {
			return false;
		}
		if (senderUser == null) {
			if (other.senderUser != null) {
				return false;
			}
		} else if (!senderUser.equals(other.senderUser)) {
			return false;
		}
		if (status == null) {
			if (other.status != null) {
				return false;
			}
		} else if (!status.equals(other.status)) {
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


