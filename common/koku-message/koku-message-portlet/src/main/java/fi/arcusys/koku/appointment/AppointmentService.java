package fi.arcusys.koku.appointment;

import java.net.URL;
import java.util.List;

import fi.arcusys.koku.appointmentservice.Appointment;
import fi.arcusys.koku.appointmentservice.AppointmentSummary;
import fi.arcusys.koku.appointmentservice.KokuAppointmentService_Service;

public class AppointmentService {
	public final URL APPOINTMENT_WSDL_LOCATION = getClass().getClassLoader().getResource("AppointmentService.wsdl");
	private KokuAppointmentService_Service as;
	
	public AppointmentService() {
		this.as = new KokuAppointmentService_Service(APPOINTMENT_WSDL_LOCATION);
	}
	
	public List<AppointmentSummary> getCreatedAppointments(String user, int startNum, int maxNum) {
		return as.getKokuAppointmentServicePort().getCreatedAppointments(user, startNum, maxNum);
	}
	
	public List<AppointmentSummary> getAssignedAppointments(String user, int startNum, int maxNum) {
		return as.getKokuAppointmentServicePort().getAssignedAppointments(user, startNum, maxNum);
	}
	
	public List<AppointmentSummary> getRespondedAppointments(String user, int startNum, int maxNum) {
		return as.getKokuAppointmentServicePort().getRespondedAppointments(user, startNum, maxNum);
	}
	
	public int getTotalCreatedAppointmentNum(String user) {
		return as.getKokuAppointmentServicePort().getTotalCreatedAppointments(user);
	}
	
	public int getTotalAssignedAppointmentNum(String user) {
		return as.getKokuAppointmentServicePort().getTotalAssignedAppointments(user);
	}
	
	public int getTotalRespondedAppointmentNum(String user) {
		return as.getKokuAppointmentServicePort().getTotalRespondedAppointments(user);
	}
	
	public Appointment getAppointmentById(long appointmentId) {
		return as.getKokuAppointmentServicePort().getAppointmentById(appointmentId);
	}
	
	public void removeAppointments(long appointmentId) {
		as.getKokuAppointmentServicePort().removeAppointment(appointmentId);
	}
}
