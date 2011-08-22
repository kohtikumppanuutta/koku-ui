package fi.arcusys.koku.av;

public class Slot {

    private long appointmentId;
    private int slotNumber;
    private String date;
    private String startTime;
    private String endTime;
    private String location;
    private String comment;
    private boolean approved;
    private String targetPerson;
    private String recipients;
    /* getters */
    public long getAppointmentId() {
    	return appointmentId;
    }
    
    public long getSlotNumber() {
    	return slotNumber;
    }
    
    public String getDate() {
    	return date;
    }
    
    public String getStartTime() {
    	return startTime;
    }
    
    public String getEndTime() {
    	return endTime;
    }
    
    public String getLocation() {
    	return location;
    }
    
    public String getComment() {
    	return comment;
    }
    
    public boolean getApproved() {
    	return approved;
    }
    
    public String getTargetPerson() {
    	return targetPerson;
    }
    
    public String getRecipients() {
    	return recipients;
    }
    /* setters */
    public void setAppointmentId(long appointmentId) {
    	this.appointmentId = appointmentId;
    }
    
    public void setSlotNumber(int slotNumber) {
    	this.slotNumber = slotNumber;
    }
    
    public void setAppointmentDate(String date) {
    	this.date = date;
    }
    
    public void setStartTime(String startTime) {
    	this.startTime = startTime;
    }
    
    public void setEndTime(String endTime) {
    	this.endTime = endTime;
    }
    
    public void setLocation(String location) {
    	this.location = location;
    }
    
    public void setComment(String comment) {
    	this.comment = comment;
    }
    
    public void setApproved(boolean approved) {
    	this.approved = approved;
    }
    
    public void setTargetPerson(String targetPerson) {
    	this.targetPerson = targetPerson;
    }
    
    public void setRecipients(String recipients) {
    	this.recipients = recipients;
    }
}
