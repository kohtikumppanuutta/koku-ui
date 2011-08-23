package fi.arcusys.koku.av;

import java.net.URL;
import java.util.List;

import fi.arcusys.koku.av.citizenservice.AppointmentRespondedTO;
import fi.arcusys.koku.av.citizenservice.AppointmentSummary;
import fi.arcusys.koku.av.citizenservice.KokuKunpoAppointmentService_Service;

/**
 * Retrieves appointment data and related operations via web services
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class AvCitizenService {
	
	public final URL AV_CITIZEN_WSDL_LOCATION = getClass().getClassLoader().getResource("AvCitizenService.wsdl");
	private KokuKunpoAppointmentService_Service as;
	
	/**
	 * Constructor and initialization
	 */
	public AvCitizenService() {
		this.as = new KokuKunpoAppointmentService_Service(AV_CITIZEN_WSDL_LOCATION);
	}
	
	/**
	 * Gets the assigned appointments
	 * @param user user name
	 * @param startNum the start index
	 * @param maxNum the maximum number
	 * @return a list of summary of appointments
	 */
	public List<AppointmentSummary> getAssignedAppointments(String user, int startNum, int maxNum) {
		return as.getKokuKunpoAppointmentServicePort().getAssignedAppointments(user, startNum, maxNum);
	}
	
	/**
	 * Gets the responded appointments
	 * @param user user name
	 * @param startNum the start index
	 * @param maxNum the maximum number
	 * @return a list of summary of appointments
	 */
	public List<AppointmentSummary> getRespondedAppointments(String user, int startNum, int maxNum) {
		return as.getKokuKunpoAppointmentServicePort().getRespondedAppointments(user, startNum, maxNum);
	}
	
	/**
	 * Gets the amount of assigned appointments
	 * @param user user name
	 * @return the number of assigned appointments
	 */
	public int getTotalAssignedAppointmentNum(String user) {
		return as.getKokuKunpoAppointmentServicePort().getTotalAssignedAppointments(user);
	}
	
	/**
	 * Gets the amount of responded appointments
	 * @param user user name
	 * @return the number of responded appointments
	 */
	public int getTotalRespondedAppointmentNum(String user) {
		return as.getKokuKunpoAppointmentServicePort().getTotalRespondedAppointments(user);
	}
	
	/**
	 * Gets the responded appointment in detail
	 * @param appointmentId appointment id
	 * @return detailed appointment
	 */
	public AppointmentRespondedTO getAppointmentRespondedById(long appointmentId) {
		return as.getKokuKunpoAppointmentServicePort().getAppointmentRespondedById(appointmentId);
	}
	
}
