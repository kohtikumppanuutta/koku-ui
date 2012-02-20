package fi.arcusys.koku.av;

import static fi.arcusys.koku.util.Constants.DATE;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_APPOINTMENT_INBOX_CITIZEN;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN_OLD;
import static fi.arcusys.koku.util.Constants.TIME;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.NoSuchMessageException;

import fi.arcusys.koku.AbstractHandle;
import fi.arcusys.koku.av.citizenservice.AppointmentRespondedTO;
import fi.arcusys.koku.av.citizenservice.AppointmentSlot;
import fi.arcusys.koku.av.citizenservice.AppointmentSummaryStatus;
import fi.arcusys.koku.av.citizenservice.AppointmentWithTarget;
import fi.arcusys.koku.av.citizenservice.User;
import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.users.KokuUser;
import fi.arcusys.koku.util.MessageUtil;


/**
 * Handles appointments related operations
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class AvCitizenServiceHandle extends AbstractHandle {
	
	private static final Logger LOG = Logger.getLogger(AvCitizenServiceHandle.class);
		
	private AvCitizenService acs;
	private String loginUserId;
	
	/**
	 * Constructor and initialization
	 */
	public AvCitizenServiceHandle() {
		acs = new AvCitizenService();
	}
	
	public AvCitizenServiceHandle( String loginUser) {
		loginUserId = loginUser;
		acs = new AvCitizenService();
	}
	
	/**
	 * Gets appointments and generates the appointment data model
	 * @param user user name
	 * @param startNum start index of appointment
	 * @param maxNum maximum number of appointments
	 * @param taskType task type requested
	 * @return a list of appointments
	 */
	public List<KokuAppointment> getAppointments(String userId, int startNum, int maxNum, String taskType) throws KokuServiceException {
		List<AppointmentWithTarget> appSummaryList = null;
		List<KokuAppointment> appList = new ArrayList<KokuAppointment>();
		
		if (taskType.equals(TASK_TYPE_APPOINTMENT_INBOX_CITIZEN)) {
			appSummaryList = acs.getAssignedAppointments(userId, startNum, maxNum);		
		} else if (taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN)) {
			appSummaryList = acs.getRespondedAppointments(userId, startNum, maxNum);			
		} else if (taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN_OLD)) {
			appSummaryList = acs.getOldAppointments(userId, startNum, maxNum);
		} else {
			return appList;	
		}
				
		//KokuAppointment kokuAppointment;
		CitizenAppointment kokuAppointment;
		Iterator<AppointmentWithTarget> it = appSummaryList.iterator();
		while(it.hasNext()) {
			AppointmentWithTarget appSummary = it.next();
			kokuAppointment = new CitizenAppointment();
			kokuAppointment.setAppointmentId(appSummary.getAppointmentId());
			kokuAppointment.setSender(getDisplayName(appSummary.getSenderUserInfo()));
			kokuAppointment.setSenderUser(new KokuUser(appSummary.getSenderUserInfo()));
			kokuAppointment.setSubject(appSummary.getSubject());
			kokuAppointment.setDescription(appSummary.getDescription());
			kokuAppointment.setTargetPersonUid(getUserUid(appSummary.getTargetPersonUserInfo()));
			kokuAppointment.setTargetPersonDisplayName(getDisplayName(appSummary.getTargetPersonUserInfo()));
			kokuAppointment.setStatus(localizeActionRequestStatus(appSummary.getStatus()));
			appList.add(kokuAppointment);		
		}		
		return appList;
	}

    private String getUserUid(final User user) {
        if (user == null) {
            return null;
        }
        return user.getUid();
    }

	/**
	 * Gets the appointment in detail
	 * @param appointmentId appointment id
	 * @return detailed citizen appointment
	 */
	public CitizenAppointment getAppointmentById(String appointmentId, String targetUser) throws KokuServiceException {
		long  appId = 0;
		try {
			appId = (long) Long.parseLong(appointmentId);
		} catch (NumberFormatException nfe) {
			throw new KokuServiceException("Invalid appointmentId. AppointmentId: '"+appointmentId+"'", nfe);
		}
		CitizenAppointment ctzAppointment = new CitizenAppointment();
		AppointmentRespondedTO appointment = acs.getAppointmentRespondedById(appId, targetUser);
		ctzAppointment.setAppointmentId(appointment.getAppointmentId());
		ctzAppointment.setSender(getDisplayName(appointment.getSenderUserInfo()));
		ctzAppointment.setSenderUser(new KokuUser(appointment.getSenderUserInfo()));
		ctzAppointment.setSubject(appointment.getSubject());
		ctzAppointment.setDescription(appointment.getDescription());
		if (appointment.getStatus() != null) {
			ctzAppointment.setStatus(localizeActionRequestStatus(appointment.getStatus()));
		}
		if (appointment.getApprovedSlot() != null) {
			ctzAppointment.setSlot(formatSlot(appointment.getApprovedSlot()));			
		}
		ctzAppointment.setReplier(getDisplayName(appointment.getReplierUserInfo()));
		ctzAppointment.setReplierUser(new KokuUser(appointment.getReplierUserInfo()));
		ctzAppointment.setReplierComment(appointment.getReplierComment());
        ctzAppointment.setTargetPersonUid(getUserUid(appointment.getTargetPersonUserInfo()));
		ctzAppointment.setTargetPersonUser(new KokuUser(appointment.getTargetPersonUserInfo()));
		ctzAppointment.setTargetPersonDisplayName(getDisplayName(appointment.getTargetPersonUserInfo()));
		ctzAppointment.setCancellationComment(appointment.getEmployeesCancelComent());
		
		return ctzAppointment;		
	}
	
	/**
     * @param senderUserInfo
     * @return
     */
    private String getDisplayName(User user) {
        if (user == null) {
            return null;
        }
        return user.getDisplayName();
    }

    /**
	 * Gets the total number of appointments
	 * @param user username
	 * @param taskType task type requested 
	 * @return the total number of appointments
	 */
	public int getTotalAppointmentsNum(String userId, String taskType) throws KokuServiceException {

		if (taskType.equals(TASK_TYPE_APPOINTMENT_INBOX_CITIZEN)) {// for citizen
			return acs.getTotalAssignedAppointmentNum(userId);
		} else if(taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN)) {
			return acs.getTotalRespondedAppointmentNum(userId);
		} else if(taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN_OLD)) {
			return acs.getTotalOldAppointments(userId);
		} else {
			return 0;
		}
	}
	
	/**
	 * Formats the slot data model
	 * @param appSlot slot of appointment
	 * @return formatted slot data model
	 */
	private Slot formatSlot(AppointmentSlot appSlot) {
		Slot slot = new Slot();
		slot.setSlotNumber(appSlot.getSlotNumber());
		slot.setAppointmentDate(MessageUtil.formatDateByString(appSlot.getAppointmentDate(), DATE));
		slot.setStartTime(MessageUtil.formatDateByString(appSlot.getStartTime(), TIME));
		slot.setEndTime(MessageUtil.formatDateByString(appSlot.getEndTime(), TIME));
		slot.setLocation(appSlot.getLocation());
		slot.setComment(appSlot.getComment());
		return slot;
	}
	
	/**
	 * Cancels appointments
	 * @param appointmentId
	 * @param targetPerson
	 * @param comment
	 * @return operation response
	 */
	public void cancelAppointments(long appointmentId, String targetPerson, String comment) throws KokuServiceException {
		acs.cancelAppointment(appointmentId, targetPerson, loginUserId, comment);
	}
	
	private String localizeActionRequestStatus(AppointmentSummaryStatus appointmentStatus) {
		if (getMessageSource() == null) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return appointmentStatus.toString();
		}
		Locale locale = MessageUtil.getLocale();
		try {
			switch (appointmentStatus) {
			case APPROVED:
				return getMessageSource().getMessage("AppointmentStatus.Approved", null, locale);
			case CANCELLED:
				return getMessageSource().getMessage("AppointmentStatus.Cancelled", null, locale);				
			case CREATED:
				return getMessageSource().getMessage("AppointmentStatus.Created", null, locale);
			default:
				return appointmentStatus.toString();
			}							
		} catch (NoSuchMessageException nsme) {
			LOG.warn("Coulnd't find localized message for '" +appointmentStatus +"'. Localization doesn't work properly");
			return appointmentStatus.toString();
		}
	}
	
}
