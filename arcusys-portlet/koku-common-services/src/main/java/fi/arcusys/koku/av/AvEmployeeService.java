package fi.arcusys.koku.av;

import java.util.List;

import javax.xml.ws.BindingProvider;

import fi.arcusys.koku.av.employeeservice.Appointment;
import fi.arcusys.koku.av.employeeservice.AppointmentCriteria;
import fi.arcusys.koku.av.employeeservice.AppointmentSummary;
import fi.arcusys.koku.av.employeeservice.KokuLooraAppointmentService;
import fi.arcusys.koku.av.employeeservice.KokuLooraAppointmentService_Service;
import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.util.Properties;

/**
 * Retrieves appointment data and related operations via web services
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class AvEmployeeService {
	
	private final KokuLooraAppointmentService service;
	
	/**
	 * Constructor and initialization
	 */
	public AvEmployeeService() {
		KokuLooraAppointmentService_Service as = new KokuLooraAppointmentService_Service();
		service = as.getKokuLooraAppointmentServicePort();
		((BindingProvider)service).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, Properties.AV_EMPLOYEE_SERVICE);
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
			return service.getCreatedAppointments(user, startNum, maxNum, criteria);
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
			return service.getProcessedAppointments(user, startNum, maxNum, criteria);
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
		return service.getTotalCreatedAppointments(user, criteria);
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
			return service.getTotalProcessedAppointments(user, criteria);
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
			return service.getAppointmentById(appointmentId);
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
			service.cancelAppointment(appointmentId, comment);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getAppointmentById failed. appointmentId: '"+appointmentId+"' comment: '"+comment+"'", e);
		}
	}
	
}
