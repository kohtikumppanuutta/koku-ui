package fi.arcusys.koku.appointment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fi.arcusys.koku.appointmentservice.Appointment;
import fi.arcusys.koku.appointmentservice.AppointmentSlot;
import fi.arcusys.koku.appointmentservice.AppointmentSummary;
import fi.arcusys.koku.util.MessageUtil;

public class AppointmentHandle {
	public AppointmentHandle() {}
	
	public List<KokuAppointment> getAppointments(String user, int startNum, int maxNum, String taskType) {
		AppointmentService as = new AppointmentService();
		List<AppointmentSummary> appSummaryList;
		List<KokuAppointment> appList = new ArrayList<KokuAppointment>();
		
		if(taskType.equals("app_inbox_employee"))
			appSummaryList = as.getCreatedAppointments(user, startNum, maxNum);
		else if(taskType.equals("app_inbox_citizen"))
			appSummaryList = as.getAssignedAppointments(user, startNum, maxNum);
		else if(taskType.equals("app_response_employee"))
			appSummaryList = as.getRespondedAppointments(user, startNum, maxNum);
		else if(taskType.equals("app_response_citizen"))
			appSummaryList = as.getRespondedAppointments(user, startNum, maxNum);
		else
			return appList;
		
		
		KokuAppointment kokuAppointment;
		
		Iterator<AppointmentSummary> it = appSummaryList.iterator();
		while(it.hasNext()) {
			AppointmentSummary appSummary = it.next();
			kokuAppointment = new KokuAppointment();
			kokuAppointment.setAppointmentId(appSummary.getAppointmentId());
			kokuAppointment.setSender(appSummary.getSender());
			kokuAppointment.SetRecipients(formatRecipients(appSummary.getRecipients()));
			kokuAppointment.setSubject(appSummary.getSubject());
			kokuAppointment.setDescription(appSummary.getDescription());
			
			appList.add(kokuAppointment);		
		}
		
		return appList;
	}

	public KokuAppointment getAppointmentById(String appointmentId) {
		long  appId = (long) Long.parseLong(appointmentId);
		KokuAppointment kokuAppointment = new KokuAppointment();
		AppointmentService as = new AppointmentService();
		Appointment appointment = as.getAppointmentById(appId);
		kokuAppointment.setAppointmentId(appointment.getAppointmentId());
		kokuAppointment.setSender(appointment.getSender());
		kokuAppointment.SetRecipients(formatRecipients(appointment.getRecipients()));
		kokuAppointment.setSubject(appointment.getSubject());
		kokuAppointment.setDescription(appointment.getDescription());
		kokuAppointment.setStatus(appointment.getStatus());
		kokuAppointment.setSlots(formatSlots(appointment.getSlots()));
		
		if(appointment.getStatus().equals("Approved"))
			kokuAppointment.setApprovedSlotNumber(appointment.getApprovedSlotNumber().intValue());
		else
			kokuAppointment.setApprovedSlotNumber(-1);
		
		return kokuAppointment;		
	}
	
	public int getTotalAppointmentsNum(String user, String taskType) {
		AppointmentService as = new AppointmentService();
		
		if(taskType.equals("app_inbox_employee"))// for employee
			return as.getTotalCreatedAppointmentNum(user);
		else if(taskType.equals("app_response_employee"))
			return as.getTotalRespondedAppointmentNum(user);
		else if(taskType.equals("app_inbox_citizen"))// for citizen
			return as.getTotalAssignedAppointmentNum(user);
		else if(taskType.equals("app_response_citizen"))
			return as.getTotalRespondedAppointmentNum(user);
		else
			return 0;
	}
	
	private String formatRecipients(List<String> recipients) {
		Iterator<String> it = recipients.iterator();
		String recipientStr = "";
		String recipient;
		
		while(it.hasNext()) {
			recipient = it.next();
			
			if(recipient.trim().length() > 0)
				recipientStr += recipient + ", ";		
		}
		
		if(recipientStr.lastIndexOf(",") > 0)
			recipientStr = recipientStr.substring(0, recipientStr.length()-2);
		
		return recipientStr;
	}
	
	private List<Slot> formatSlots(List<AppointmentSlot> appSlots) {
		List<Slot> slots = new ArrayList<Slot>();
		Slot slot;
		String dateString = "d.M.yyyy";
		String timeString = "HH:mm:ss";
		Iterator<AppointmentSlot> it = appSlots.iterator();
		
		while(it.hasNext()) {
			slot = new Slot();
			AppointmentSlot appSlot = it.next();
			slot.setAppointmentId(appSlot.getAppointmentId());
			slot.setSlotNumber(appSlot.getSlotNumber());
			slot.setAppointmentDate(MessageUtil.formatDateByString(appSlot.getAppointmentDate(), dateString));
			slot.setStartTime(MessageUtil.formatDateByString(appSlot.getStartTime(), timeString));
			slot.setEndTime(MessageUtil.formatDateByString(appSlot.getEndTime(), timeString));
			slot.setLocation(appSlot.getLocation());
			
			slots.add(slot);
		}
		
		return slots;
	}
}
