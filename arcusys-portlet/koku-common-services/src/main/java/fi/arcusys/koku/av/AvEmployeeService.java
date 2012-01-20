package fi.arcusys.koku.av;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.koku.av.employeeservice.Appointment;
import fi.arcusys.koku.av.employeeservice.AppointmentCriteria;
import fi.arcusys.koku.av.employeeservice.AppointmentSummary;
import fi.arcusys.koku.av.employeeservice.KokuLooraAppointmentService_Service;
import fi.arcusys.koku.exceptions.KokuServiceException;
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
	public List<AppointmentSummary> getCreatedAppointments(String user, AppointmentCriteria criteria, int startNum, int maxNum) throws KokuServiceException {
		try {
			return as.getKokuLooraAppointmentServicePort().getCreatedAppointments(user, startNum, maxNum, criteria);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getCreatedAppointments failed. user: '"+user+"'", e);
		}
	}
	
	/**
	 * Gets the responded appointments
	 * @param user user name
	 * @param startNum the start index
	 * @param maxNum the maximum number
	 * @return a list of summary of appointments
	 */
	public List<AppointmentSummary> getProcessedAppointments(String user, AppointmentCriteria criteria, int startNum, int maxNum) throws KokuServiceException {
		try {
			return as.getKokuLooraAppointmentServicePort().getProcessedAppointments(user, startNum, maxNum, criteria);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getProcessedAppointments failed. user: '"+user+"'", e);
		}
		
	}
	
	/**
	 * Gets the amount of created appointments
	 * @param user user name
	 * @return the number of created appointments
	 */
	public int getTotalCreatedAppointmentNum(String user, AppointmentCriteria criteria) throws KokuServiceException {
		try {
		return as.getKokuLooraAppointmentServicePort().getTotalCreatedAppointments(user, criteria);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalCreatedAppointmentNum failed. user: '"+user+"'", e);
		}
	}
	
	/**
	 * Gets the amount of processed appointments
	 * @param user user name
	 * @return the number of processed appointments
	 */
	public int getTotalProcessedAppointments(String user, AppointmentCriteria criteria) throws KokuServiceException {
		try {
			return as.getKokuLooraAppointmentServicePort().getTotalProcessedAppointments(user, criteria);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalProcessedAppointments failed. user: '"+user+"'", e);
		}
	}
	
	/**
	 * Gets the responded appointment in detail
	 * @param appointmentId appointment id
	 * @return detailed appointment
	 */
	public Appointment getAppointmentById(long appointmentId) throws KokuServiceException {
		try {
			return as.getKokuLooraAppointmentServicePort().getAppointmentById(appointmentId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getAppointmentById failed. appointmentId: '"+appointmentId+"'", e);
		}	
	}
	
	/**
	 * Cancels appointment
	 * @param appointmentId
	 * @param comment
	 */
	public void cancelAppointment(long appointmentId, String comment) throws KokuServiceException {
		try {
			as.getKokuLooraAppointmentServicePort().cancelAppointment(appointmentId, comment);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getAppointmentById failed. appointmentId: '"+appointmentId+"' comment: '"+comment+"'", e);
		}
	}
	
}
