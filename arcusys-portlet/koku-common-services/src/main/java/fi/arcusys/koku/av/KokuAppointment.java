package fi.arcusys.koku.av;

/**
 * Summary appointment data model
 * @author Jinhua Chen
 * Aug 23, 2011
 */
public class KokuAppointment {

	private long appointmentId;
	private String sender;
	private String subject;
	private String description;
	private String status;
	private String cancellationComment;
	
	/* getters */
	public long getAppointmentId() {
		return appointmentId;
	}
	
	public String getSender() {
		return sender;
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
	
	public void setSender(String sender) {
		this.sender = sender;
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
}


