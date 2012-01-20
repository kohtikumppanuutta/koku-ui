package fi.arcusys.koku.av;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.koku.av.citizenservice.AppointmentRespondedTO;
import fi.arcusys.koku.av.citizenservice.AppointmentWithTarget;
import fi.arcusys.koku.av.citizenservice.KokuKunpoAppointmentService_Service;
import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.koku.settings.KoKuPropertiesUtil;

/**
 * Retrieves appointment data and related operations via web services
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class AvCitizenService {
		
	private static final Logger LOG = Logger.getLogger(AvCitizenService.class);		
	public static final URL AV_CITIZEN_WSDL_LOCATION;
	
	static {
		try {
			LOG.info("AvCitizenService WSDL location: " + KoKuPropertiesUtil.get("AvCitizenService"));
			AV_CITIZEN_WSDL_LOCATION =  new URL(KoKuPropertiesUtil.get("AvCitizenService"));
		} catch (MalformedURLException e) {
			LOG.error("Failed to create AvCitizenService WSDL url! Given URL address is not valid!");
			throw new ExceptionInInitializerError(e);
		}
	}
	
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
	public List<AppointmentWithTarget> getAssignedAppointments(String user, int startNum, int maxNum) throws KokuServiceException {
		try {
			return as.getKokuKunpoAppointmentServicePort().getAssignedAppointments(user, startNum, maxNum);
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
			return as.getKokuKunpoAppointmentServicePort().getRespondedAppointments(user, startNum, maxNum);
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
			return as.getKokuKunpoAppointmentServicePort().getOldAppointments(userId, startNum, maxNum);
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
			return as.getKokuKunpoAppointmentServicePort().getTotalAssignedAppointments(user);
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
			return as.getKokuKunpoAppointmentServicePort().getTotalRespondedAppointments(user);
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
			return as.getKokuKunpoAppointmentServicePort().getTotalOldAppointments(userId);
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
			return as.getKokuKunpoAppointmentServicePort().getAppointmentRespondedById(appointmentId, targetUser);
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
			as.getKokuKunpoAppointmentServicePort().cancelRespondedAppointment(appointmentId, targetUser, user, comment);
		} catch(RuntimeException e) {
			throw new KokuServiceException("cancelAppointment failed. messageId: appointmentId: '"+appointmentId+"' targetUser: '"+targetUser+"' user: '"+user+"' comment: '"+comment+"'", e);
		}
	}
	

}
