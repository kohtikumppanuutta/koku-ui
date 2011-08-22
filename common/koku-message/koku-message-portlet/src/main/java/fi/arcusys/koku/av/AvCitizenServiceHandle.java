package fi.arcusys.koku.av;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fi.arcusys.koku.av.citizenservice.AppointmentRespondedTO;
import fi.arcusys.koku.av.citizenservice.AppointmentSlot;
import fi.arcusys.koku.av.citizenservice.AppointmentSummary;
import fi.arcusys.koku.util.MessageUtil;

public class AvCitizenServiceHandle {
	
	private AvCitizenService acs;
	
	public AvCitizenServiceHandle() {
		acs = new AvCitizenService();
	}
	
	public List<KokuAppointment> getAppointments(String user, int startNum, int maxNum, String taskType) {
		List<AppointmentSummary> appSummaryList;
		List<KokuAppointment> appList = new ArrayList<KokuAppointment>();
		
		if(taskType.equals("app_inbox_citizen"))
			appSummaryList = acs.getAssignedAppointments(user, startNum, maxNum);
		else if(taskType.equals("app_response_citizen"))
			appSummaryList = acs.getRespondedAppointments(user, startNum, maxNum);
		else
			return appList;
		
		
		KokuAppointment kokuAppointment;
		
		Iterator<AppointmentSummary> it = appSummaryList.iterator();
		while(it.hasNext()) {
			AppointmentSummary appSummary = it.next();
			kokuAppointment = new KokuAppointment();
			kokuAppointment.setAppointmentId(appSummary.getAppointmentId());
			kokuAppointment.setSender(appSummary.getSender());
			kokuAppointment.setSubject(appSummary.getSubject());
			kokuAppointment.setDescription(appSummary.getDescription());
			
			appList.add(kokuAppointment);		
		}
		
		return appList;
	}

	public CitizenAppointment getAppointmentById(String appointmentId) {
		long  appId = (long) Long.parseLong(appointmentId);
		CitizenAppointment ctzAppointment = new CitizenAppointment();
		AppointmentRespondedTO appointment = acs.getAppointmentRespondedById(appId);
		ctzAppointment.setAppointmentId(appointment.getAppointmentId());
		ctzAppointment.setSender(appointment.getSender());
		ctzAppointment.setSubject(appointment.getSubject());
		ctzAppointment.setDescription(appointment.getDescription());
		ctzAppointment.setStatus(appointment.getStatus());
		ctzAppointment.setSlot(formatSlot(appointment.getApprovedSlot()));
		ctzAppointment.setReplier(appointment.getReplier());
		ctzAppointment.setReplierComment(appointment.getReplierComment());
		ctzAppointment.setTargetPerson(appointment.getTargetPerson());
		
		return ctzAppointment;		
	}
	
	public int getTotalAppointmentsNum(String user, String taskType) {
		
		if(taskType.equals("app_inbox_citizen"))// for citizen
			return acs.getTotalAssignedAppointmentNum(user);
		else if(taskType.equals("app_response_citizen"))
			return acs.getTotalRespondedAppointmentNum(user);
		else
			return 0;
	}
	
	private Slot formatSlot(AppointmentSlot appSlot) {
		Slot slot = new Slot();
		String dateString = "d.M.yyyy";
		String timeString = "HH:mm:ss";
		slot.setAppointmentId(appSlot.getAppointmentId());
		slot.setSlotNumber(appSlot.getSlotNumber());
		slot.setAppointmentDate(MessageUtil.formatDateByString(appSlot.getAppointmentDate(), dateString));
		slot.setStartTime(MessageUtil.formatDateByString(appSlot.getStartTime(), timeString));
		slot.setEndTime(MessageUtil.formatDateByString(appSlot.getEndTime(), timeString));
		slot.setLocation(appSlot.getLocation());
		
		return slot;
	}

}
