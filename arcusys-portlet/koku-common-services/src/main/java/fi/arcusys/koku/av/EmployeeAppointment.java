package fi.arcusys.koku.av;

import java.util.ArrayList;
import java.util.List;

import fi.arcusys.koku.av.employeeservice.Appointment.AcceptedSlots;
import fi.arcusys.koku.av.employeeservice.AppointmentReceipientTO;

/**
 * Appointment data model for employee
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class EmployeeAppointment extends KokuAppointment {

	private List<Slot> approvedSlots;
	private List<Slot> unapprovedSlots;
	private List<AppointmentReceipientTO> recipients;
	private AcceptedSlots acceptedSlots;
	private List<UserWithTarget> rejectedUsers;
	private List<UserWithTarget> unrespondedUsers;
	private List<KokuAppointmentUserRejected> usersRejected;
	
	/* getters */	
	public List<AppointmentReceipientTO> getRecipients() {
		return recipients;
	}
		
	public List<Slot> getApprovedSlots() {
		return approvedSlots;
	}
	
	public List<Slot> getUnapprovedSlots() {
		return unapprovedSlots;
	}
	
	public AcceptedSlots getAcceptedSlots() {
		return acceptedSlots;
	}
		
	public List<UserWithTarget> getRejectedUsers() {
		return rejectedUsers;
	}
	
	public List<UserWithTarget> getunrespondedUsers() {
		return unrespondedUsers;
	}
	/* setters */	
	public void setRecipients(List<AppointmentReceipientTO> recipients) {
		this.recipients = recipients;
	}
	
	public void setApprovedSlots(List<Slot> approvedSlots) {
		this.approvedSlots = approvedSlots;
	}
	
	public void setUnapprovedSlots(List<Slot> unapprovedSlots) {
		this.unapprovedSlots = unapprovedSlots;
	}
	
	public void setAcceptedSlots(AcceptedSlots acceptedSlots) {
		this.acceptedSlots = acceptedSlots;
	}
		
	public void setRejectedUsers(List<UserWithTarget> rejectedUsers) {
		this.rejectedUsers = rejectedUsers;
	}
	
	public void setUnrespondedUsers(List<UserWithTarget> unrespondedUsers) {
		this.unrespondedUsers = unrespondedUsers;
	}
	
	public final List<KokuAppointmentUserRejected> getUsersRejected() {
		if (usersRejected == null) {
			return new ArrayList<KokuAppointmentUserRejected>();
		}
		return usersRejected;
	}

	public final void setUserRejected(List<KokuAppointmentUserRejected> usersRejected) {
		this.usersRejected = usersRejected;
	}

	public static class UserWithTarget {
		private String targetPerson;
		private String recipients;
		
		public String getTargetPerson() {
			return targetPerson;
		}
		
		public String getRecipients() {
			return recipients;
		}
		
		public void setTargetPerson(String targetPerson) {
			this.targetPerson = targetPerson;
		}
		
		public void setRecipients(String recipients) {
			this.recipients = recipients;
		}
	}
}

