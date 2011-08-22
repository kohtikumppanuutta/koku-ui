package fi.arcusys.koku.av;

import java.net.URL;
import java.util.List;

import fi.arcusys.koku.av.citizenservice.AppointmentRespondedTO;
import fi.arcusys.koku.av.citizenservice.AppointmentSummary;
import fi.arcusys.koku.av.citizenservice.KokuKunpoAppointmentService_Service;

public class AvCitizenService {
	public final URL AV_CITIZEN_WSDL_LOCATION = getClass().getClassLoader().getResource("AvCitizenService.wsdl");
	private KokuKunpoAppointmentService_Service as;
	
	public AvCitizenService() {
		this.as = new KokuKunpoAppointmentService_Service(AV_CITIZEN_WSDL_LOCATION);
	}
	
	public List<AppointmentSummary> getAssignedAppointments(String user, int startNum, int maxNum) {
		return as.getKokuKunpoAppointmentServicePort().getAssignedAppointments(user, startNum, maxNum);
	}
	
	public List<AppointmentSummary> getRespondedAppointments(String user, int startNum, int maxNum) {
		return as.getKokuKunpoAppointmentServicePort().getRespondedAppointments(user, startNum, maxNum);
	}
	
	public int getTotalAssignedAppointmentNum(String user) {
		return as.getKokuKunpoAppointmentServicePort().getTotalAssignedAppointments(user);
	}
	
	public int getTotalRespondedAppointmentNum(String user) {
		return as.getKokuKunpoAppointmentServicePort().getTotalRespondedAppointments(user);
	}
	
	public AppointmentRespondedTO getAppointmentRespondedById(long appointmentId) {
		return as.getKokuKunpoAppointmentServicePort().getAppointmentRespondedById(appointmentId);
	}
	
}
