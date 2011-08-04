package fi.arcusys.koku.appointment;

import java.util.List;

public class KokuAppointment {

	private long appointmentId;
	private String sender;
	private String recipients;
	private String subject;
	private String description;	
	private String status;
	private List<Slot> slots;
	private int approvedSlotNumber;
	
	/* getters */
	public long getAppointmentId() {
		return appointmentId;
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
	
	public String getDescription() {
		return description;
	}
	
	public String getStatus() {
		return status;
	}
	
	public List<Slot> getSlots() {
		return slots;
	}
	
	public int getApprovedSlotNumber() {
		return approvedSlotNumber;
	}
	
	/* setters */
	public void setAppointmentId(long appointmentId) {
		this.appointmentId = appointmentId;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public void SetRecipients(String recipients) {
		this.recipients = recipients;
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
	
	public void setSlots(List<Slot> slots) {
		this.slots = slots;
	}
	
	public void setApprovedSlotNumber(int approvedSlotNumber) {
		this.approvedSlotNumber = approvedSlotNumber;
	}
}
