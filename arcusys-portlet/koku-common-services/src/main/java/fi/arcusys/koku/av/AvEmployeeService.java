package fi.arcusys.koku.av;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.koku.av.employeeservice.Appointment;
import fi.arcusys.koku.av.employeeservice.AppointmentSummary;
import fi.arcusys.koku.av.employeeservice.KokuLooraAppointmentService_Service;
import fi.arcusys.koku.util.PropertiesUtil;
import fi.koku.settings.KoKuPropertiesUtil;

/**
 * Retrieves appointment data and related operations via web services
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class AvEmployeeService {
	
	private static final Logger LOG = Logger.getLogger(AvEmployeeService.class);	
	public static final URL AV_EMPLOYEE_WSDL_LOCATION;
	
	static {
		try {
			LOG.info("AvEmployeeService WSDL location: " + KoKuPropertiesUtil.get("AvEmployeeService"));
			AV_EMPLOYEE_WSDL_LOCATION =  new URL(KoKuPropertiesUtil.get("AvEmployeeService"));
		} catch (MalformedURLException e) {
			LOG.error("Failed to create AvEmployeeService WSDL url! Given URL address is not valid!");
			throw new ExceptionInInitializerError(e);
		}
	}
	
	private KokuLooraAppointmentService_Service as;
	
	/**
	 * Constructor and initialization
	 */
	public AvEmployeeService() {
		this.as = new KokuLooraAppointmentService_Service(AV_EMPLOYEE_WSDL_LOCATION);
	}
	
	/**
	 * Gets the assigned appointments
	 * @param user user name
	 * @param startNum the start index
	 * @param maxNum the maximum number
	 * @return a list of summary of appointments
	 */
	public List<AppointmentSummary> getCreatedAppointments(String user, int startNum, int maxNum) {
		return as.getKokuLooraAppointmentServicePort().getCreatedAppointments(user, startNum, maxNum);
	}
	
	/**
	 * Gets the responded appointments
	 * @param user user name
	 * @param startNum the start index
	 * @param maxNum the maximum number
	 * @return a list of summary of appointments
	 */
	public List<AppointmentSummary> getProcessedAppointments(String user, int startNum, int maxNum) {
		return as.getKokuLooraAppointmentServicePort().getProcessedAppointments(user, startNum, maxNum);
	}
	
	/**
	 * Gets the amount of created appointments
	 * @param user user name
	 * @return the number of created appointments
	 */
	public int getTotalCreatedAppointmentNum(String user) {
		return as.getKokuLooraAppointmentServicePort().getTotalCreatedAppointments(user);
	}
	
	/**
	 * Gets the amount of processed appointments
	 * @param user user name
	 * @return the number of processed appointments
	 */
	public int getTotalProcessedAppointments(String user) {
		return as.getKokuLooraAppointmentServicePort().getTotalProcessedAppointments(user);
	}
	
	/**
	 * Gets the responded appointment in detail
	 * @param appointmentId appointment id
	 * @return detailed appointment
	 */
	public Appointment getAppointmentById(long appointmentId) {
		return as.getKokuLooraAppointmentServicePort().getAppointmentById(appointmentId);
	}
	
	/**
	 * Cancels appointment
	 * @param appointmentId
	 * @param comment
	 */
	public void cancelAppointment(long appointmentId, String comment) {
		as.getKokuLooraAppointmentServicePort().cancelAppointment(appointmentId, comment);
	}
	
}
