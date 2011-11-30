package fi.arcusys.koku.av;

/**
 * Appointment data model for citizen
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class CitizenAppointment extends KokuAppointment{

	private Slot slot;
	private String replier;
	private String replierComment;
	private String targetPerson;
	
	
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
