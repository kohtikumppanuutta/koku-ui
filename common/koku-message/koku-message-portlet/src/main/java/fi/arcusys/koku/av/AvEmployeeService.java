package fi.arcusys.koku.av;

import java.net.URL;
import java.util.List;

import fi.arcusys.koku.av.employeeservice.Appointment;
import fi.arcusys.koku.av.employeeservice.AppointmentSummary;
import fi.arcusys.koku.av.employeeservice.KokuLooraAppointmentService_Service;


public class AvEmployeeService {
	public final URL AV_EMPLOYEE_WSDL_LOCATION = getClass().getClassLoader().getResource("AvEmployeeService.wsdl");
	private KokuLooraAppointmentService_Service as;
	
	public AvEmployeeService() {
		this.as = new KokuLooraAppointmentService_Service(AV_EMPLOYEE_WSDL_LOCATION);
	}
	
	public List<AppointmentSummary> getCreatedAppointments(String user, int startNum, int maxNum) {
		return as.getKokuLooraAppointmentServicePort().getCreatedAppointments(user, startNum, maxNum);
	}
	
	public List<AppointmentSummary> getRespondedAppointments(String user, int startNum, int maxNum) {
		return as.getKokuLooraAppointmentServicePort().getProcessedAppointments(user, startNum, maxNum);
	}
	
	public int getTotalCreatedAppointmentNum(String user) {
		return as.getKokuLooraAppointmentServicePort().getTotalCreatedAppointments(user);
	}
	
	public int getTotalProcessedAppointments(String user) {
		return as.getKokuLooraAppointmentServicePort().getTotalProcessedAppointments(user);
	}
	
	public Appointment getAppointmentById(long appointmentId) {
		return as.getKokuLooraAppointmentServicePort().getAppointmentById(appointmentId);
	}
	
}
