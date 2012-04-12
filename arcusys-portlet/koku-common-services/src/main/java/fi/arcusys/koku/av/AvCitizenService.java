package fi.arcusys.koku.av;

import java.util.List;

import javax.xml.ws.BindingProvider;

import fi.arcusys.koku.av.citizenservice.AppointmentRespondedTO;
import fi.arcusys.koku.av.citizenservice.AppointmentWithTarget;
import fi.arcusys.koku.av.citizenservice.KokuKunpoAppointmentService;
import fi.arcusys.koku.av.citizenservice.KokuKunpoAppointmentService_Service;
import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.util.Properties;

/**
 * Retrieves appointment data and related operations via web services
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class AvCitizenService {
	
	private final KokuKunpoAppointmentService service;
	
	/**
	 * Constructor and initialization
	 */
	public AvCitizenService() {
		KokuKunpoAppointmentService_Service as = new KokuKunpoAppointmentService_Service();
		service = as.getKokuKunpoAppointmentServicePort();
		((BindingProvider)service).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, Properties.AV_CITIZEN_SERVICE);
	}
	
	/**
	 * Gets the assigned appointments
	 * @param user user name
	 * @param startNum the start index
	 * @param maxNum the maximum number
	 * @return a list of summary of appointments
	 */
	public List<AppointmentWithTarget> getAssignedAppointments(String user, int startNum, int maxNum) throws KokuServiceException {
		try {
			return service.getAssignedAppointments(user, startNum, maxNum);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getAssignedAppointments failed. messageId: user: '"+user+"'", e);
		}
	}
	
	/**
	 * Gets the responded appointments
	 * @param user user name
	 * @param startNum the start index
	 * @param maxNum the maximum number
	 * @return a list of summary of appointments
	 */
	public List<AppointmentWithTarget> getRespondedAppointments(String user, int startNum, int maxNum) throws KokuServiceException {
		try {
			return service.getRespondedAppointments(user, startNum, maxNum);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getRespondedAppointments failed. messageId: user: '"+user+"'", e);
		}
	}
	
	/**
	 * Gets the already expired or cancelled appointments
	 * 
	 * @param userId
	 * @param startNum
	 * @param maxNum
	 * @return a list of summary of appointments
	 */
	public List<AppointmentWithTarget>  getOldAppointments(String userId, int startNum, int maxNum) throws KokuServiceException {
		try {
			return service.getOldAppointments(userId, startNum, maxNum);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getOldAppointments failed. messageId: userId: '"+userId+"'", e);
		}
	}
	
	
	/**
	 * Gets the amount of assigned appointments
	 * @param user user name
	 * @return the number of assigned appointments
	 */
	public int getTotalAssignedAppointmentNum(String user) throws KokuServiceException {
		try {
			return service.getTotalAssignedAppointments(user);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalAssignedAppointmentNum failed. messageId: user: '"+user+"'", e);
		}
	}
	
	/**
	 * Gets the amount of responded appointments
	 * @param user user name
	 * @return the number of responded appointments
	 */
	public int getTotalRespondedAppointmentNum(String user) throws KokuServiceException {
		try {
			return service.getTotalRespondedAppointments(user);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalRespondedAppointmentNum failed. messageId: user: '"+user+"'", e);
		}
	}
	
	/**
	 * Gets the amount of old (expired/cancelled) appointments
	 * @param user user name
	 * @return the number of old appointments
	 */
	public int getTotalOldAppointments(String userId) throws KokuServiceException {
		try {
			return service.getTotalOldAppointments(userId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalOldAppointments failed. messageId: userId: '"+userId+"'", e);
		}
	}
	
	/**
	 * Gets the responded appointment in detail
	 * @param appointmentId appointment id
	 * @return detailed appointment
	 */
	public AppointmentRespondedTO getAppointmentRespondedById(long appointmentId, String targetUser) throws KokuServiceException {
		try {
			return service.getAppointmentRespondedById(appointmentId, targetUser);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getAppointmentRespondedById failed. messageId: appointmentId: '"+appointmentId+"' targetUser: '"+targetUser+"'", e);
		}
	}
	
	/**
	 * Cancels appointment
	 * @param appointmentId
	 * @param targetUser
	 * @param user
	 * @param comment
	 */
	public void cancelAppointment(long appointmentId, String targetUser, String user, String comment) throws KokuServiceException {
		try {
			service.cancelRespondedAppointment(appointmentId, targetUser, user, comment);
		} catch(RuntimeException e) {
			throw new KokuServiceException("cancelAppointment failed. messageId: appointmentId: '"+appointmentId+"' targetUser: '"+targetUser+"' user: '"+user+"' comment: '"+comment+"'", e);
		}
	}
	

}
