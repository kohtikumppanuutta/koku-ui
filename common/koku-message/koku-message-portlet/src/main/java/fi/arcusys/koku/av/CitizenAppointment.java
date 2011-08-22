package fi.arcusys.koku.av;


public class CitizenAppointment extends KokuAppointment{

	private String status;
	private Slot slot;
	private String replier;
	private String replierComment;
	private String targetPerson;
	
	/* getters */
	public String getStatus() {
		return status;
	}
	
	public Slot getSlot() {
		return slot;
	}
	
	public String getReplier() {
		return replier;
	}
	
	public String getReplierComment() {
		return replierComment;
	}
	
	public String getTargetPerson() {
		return targetPerson;
	}
	
	/* setters */
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setSlot(Slot slot) {
		this.slot = slot;
	}
	
	public void setReplier(String replier) {
		this.replier = replier;
	}
	
	public void setReplierComment(String replierComment) {
		this.replierComment = replierComment;
	}
	
	public void setTargetPerson(String targetPerson) {
		this.targetPerson = targetPerson;
	}
}
