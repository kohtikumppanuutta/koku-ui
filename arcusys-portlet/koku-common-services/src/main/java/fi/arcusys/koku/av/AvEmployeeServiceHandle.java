package fi.arcusys.koku.av;

import static fi.arcusys.koku.util.Constants.DATE;
import static fi.arcusys.koku.util.Constants.RESPONSE_FAIL;
import static fi.arcusys.koku.util.Constants.RESPONSE_OK;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_APPOINTMENT_INBOX_EMPLOYEE;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_APPOINTMENT_RESPONSE_EMPLOYEE;
import static fi.arcusys.koku.util.Constants.TIME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.NoSuchMessageException;

import fi.arcusys.koku.AbstractHandle;
import fi.arcusys.koku.av.EmployeeAppointment.UserWithTarget;
import fi.arcusys.koku.av.employeeservice.Appointment;
import fi.arcusys.koku.av.employeeservice.Appointment.AcceptedSlots;
import fi.arcusys.koku.av.employeeservice.Appointment.AcceptedSlots.Entry;
import fi.arcusys.koku.av.employeeservice.AppointmentCriteria;
import fi.arcusys.koku.av.employeeservice.AppointmentReceipientTO;
import fi.arcusys.koku.av.employeeservice.AppointmentSlot;
import fi.arcusys.koku.av.employeeservice.AppointmentSummary;
import fi.arcusys.koku.av.employeeservice.AppointmentSummaryStatus;
import fi.arcusys.koku.av.employeeservice.AppointmentUserRejected;
import fi.arcusys.koku.av.employeeservice.User;
import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.users.KokuUser;
import fi.arcusys.koku.util.MessageUtil;


/**
 * Handles the appointments for employee
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class AvEmployeeServiceHandle extends AbstractHandle {
	
	private static final Logger LOG = Logger.getLogger(AvEmployeeServiceHandle.class);
	
	private AvEmployeeService aes;
	
	/**
	 * Constructor and initialization
	 */
	public AvEmployeeServiceHandle() {
		aes = new AvEmployeeService();
	}
	
	/**
	 * Gets summary appointments
	 * @param user user
	 * @param startNum start index of appointment
	 * @param maxNum maximum number of appointments
	 * @param taskType task type requested
	 * @return a list of summary appointments
	 */
	public List<KokuAppointment> getAppointments(String userId, int startNum, int maxNum, String taskType, String keyword) throws KokuServiceException {		
		
		String targetPersonSsn = keyword;
		
		if(taskType.equals(TASK_TYPE_APPOINTMENT_INBOX_EMPLOYEE)) {
			return getCreatedAppointments(userId, targetPersonSsn, startNum, maxNum);			
		} else  if(taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_EMPLOYEE)) {
			return getProcessedAppointment(userId, targetPersonSsn,  startNum, maxNum);			
		} else {
			return new ArrayList<KokuAppointment>();
		}
	}
	
	
	public List<KokuAppointment> getCreatedAppointments(String userId, String targetPersonSsn, int startNum, int maxNum) throws KokuServiceException {
		return getAppointmentList(aes.getCreatedAppointments(userId,  createAppointmentCriteria(targetPersonSsn), startNum, maxNum));
	}
		
	public List<KokuAppointment> getProcessedAppointment(String userId, String targetPersonSsn, int startNum, int maxNum) throws KokuServiceException {
		return getAppointmentList(aes.getProcessedAppointments(userId, createAppointmentCriteria(targetPersonSsn), startNum, maxNum));
	}
	
	private AppointmentCriteria createAppointmentCriteria(String targetPersonSsn) {
		AppointmentCriteria criteria = null;
		if (targetPersonSsn != null && !targetPersonSsn.trim().isEmpty()) {
			criteria = new AppointmentCriteria();
			criteria.setTargetPersonHetu(targetPersonSsn);			
		}
		return criteria;
	}
	
	
	/**
	 * Gets summary appointments
	 * @param user user
	 * @param startNum start index of appointment
	 * @param maxNum maximum number of appointments
	 * @param taskType task type requested
	 * @return a list of summary appointments
	 */
	private List<KokuAppointment> getAppointmentList(List<AppointmentSummary> appSummaryList) {
		List<KokuAppointment> appList = new ArrayList<KokuAppointment>();					
		for (AppointmentSummary appSummary : appSummaryList) {
			KokuAppointment kokuAppointment = new KokuAppointment();
			kokuAppointment.setAppointmentId(appSummary.getAppointmentId());
			kokuAppointment.setSender(getDisplayName(appSummary.getSenderUserInfo()));
			kokuAppointment.setSenderUser(new KokuUser(appSummary.getSenderUserInfo()));
			kokuAppointment.setSubject(appSummary.getSubject());
			kokuAppointment.setDescription(appSummary.getDescription());
			kokuAppointment.setStatus(localizeActionRequestStatus(appSummary.getStatus()));
			appList.add(kokuAppointment);
		}
		return appList;
	}

	/**
	 * Gets detailed appointment which is used to be presented
	 * @param appointmentId appointment id
	 * @return detailed appointment for employee
	 */
	public EmployeeAppointment getAppointmentById(String appointmentId) throws KokuServiceException {
		 
		long  appId = 0;
		try {
			appId = (long) Long.parseLong(appointmentId);
		} catch (NumberFormatException nfe) {
			throw new KokuServiceException("Invalid appointmentId. AppointmentId: '"+appointmentId+"'", nfe);
		}
		EmployeeAppointment empAppointment = new EmployeeAppointment();
		Appointment appointment = aes.getAppointmentById(appId);
		empAppointment.setAppointmentId(appointment.getAppointmentId());
		empAppointment.setSender(getDisplayName(appointment.getSenderUserInfo()));
		empAppointment.setSenderUser(new KokuUser(appointment.getSenderUserInfo()));
		empAppointment.setSubject(appointment.getSubject());
		empAppointment.setDescription(appointment.getDescription());
		empAppointment.setStatus(localizeActionRequestStatus(appointment.getStatus()));		
		empAppointment.setAcceptedSlots(appointment.getAcceptedSlots());
		empAppointment.setRecipients(appointment.getRecipients());
		empAppointment.setCancellationComment(appointment.getCancelComment());
//		empAppointment.setUsersRejected(appointment.getUsersRejected());
		empAppointment.setUserRejected(convertUserRejectedToKokuUserRejected(appointment.getUsersRejectedWithComments()));
		empAppointment.setRejectedUsers(formatRejectedUsers(getDisplayNames(getUsers(appointment.getUsersRejectedWithComments())), appointment.getRecipients()));
		empAppointment.setUnrespondedUsers(calcUnrespondedUsers(appointment));
		List<Slot> allSlots = formatSlots(appointment.getSlots(), appointment.getAcceptedSlots(), appointment.getRecipients());		
		setSlots(empAppointment, allSlots);
		return empAppointment;		
	}
	
	private List<KokuAppointmentUserRejected> convertUserRejectedToKokuUserRejected(List<AppointmentUserRejected> rejectedUsers) {
		if (rejectedUsers == null) {
			return new ArrayList<KokuAppointmentUserRejected>();
		}
		List<KokuAppointmentUserRejected> list = new ArrayList<KokuAppointmentUserRejected>();
		for (AppointmentUserRejected rejectedUser : rejectedUsers ) {
			list.add(new KokuAppointmentUserRejected(rejectedUser));
		}
		return list;
	}
	
	/**
	 * Gets the total number of appointments
	 * @param user user
	 * @param taskType task type requested
	 * @return the number of appointments
	 */
	public int getTotalAppointmentsNum(String userId, String taskType, String keyword) throws KokuServiceException {
		if(taskType.equals(TASK_TYPE_APPOINTMENT_INBOX_EMPLOYEE)) {	// for employee
			return aes.getTotalCreatedAppointmentNum(userId, createAppointmentCriteria(keyword));
		} else if(taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_EMPLOYEE)) {
			return aes.getTotalProcessedAppointments(userId, createAppointmentCriteria(keyword));
		} else {
			return 0;
		}
	}
	
	/**
	 * Generates slot object for client side user interface
	 * @param appSlots AppointmentSlot
	 * @param acceptedSlots AcceptedSlots
	 * @param recipients AppointmentReceipientTO
	 * @return a list of slots
	 */
	private List<Slot> formatSlots(List<AppointmentSlot> appSlots, AcceptedSlots acceptedSlots,
			List<AppointmentReceipientTO> recipients) {
		
		List<Slot> slots = new ArrayList<Slot>();
		Slot slot;
		Iterator<AppointmentSlot> it = appSlots.iterator();
		
		while(it.hasNext()) {
			slot = new Slot();
			AppointmentSlot appSlot = it.next();
			slot.setSlotNumber(appSlot.getSlotNumber());
			slot.setAppointmentDate(MessageUtil.formatDateByString(appSlot.getAppointmentDate(), DATE));
			slot.setStartTime(MessageUtil.formatDateByString(appSlot.getStartTime(), TIME));
			slot.setEndTime(MessageUtil.formatDateByString(appSlot.getEndTime(), TIME));
			slot.setLocation(appSlot.getLocation());
			slot.setComment(appSlot.getComment());
			
			slots.add(slot);
		}
		// go through the accepted slots and set related information
		Iterator<Entry> itEntry = acceptedSlots.getEntry().iterator();
		
		while(itEntry.hasNext()) {
			Entry entry = itEntry.next();
			int slotId = entry.getKey();
			String targetPerson;
			
			for(int i=0; i < slots.size(); i++) {
				slot = slots.get(i);
				if(slot.getSlotNumber() == slotId) {
					slot.setApproved(true);
					targetPerson = getDisplayName(entry.getValue());
					slot.setTargetPerson(targetPerson);
					slot.setRecipients(mapRecipients(targetPerson, recipients));
					break;
				}
			}
		}
		
		return slots;
	}

    private String getDisplayName(final User user) {
        if (user == null) {
            return null;
        }
        return user.getDisplayName();
    }
    
	/**
	 * Finds the approved slots and unapproved slots and sets them afterwards
	 * @param empAppointment EmployeeAppointment
	 * @param slots all slots
	 */
	private void setSlots(EmployeeAppointment empAppointment, List<Slot> slots) {
		List<Slot> approvedSlots = new ArrayList<Slot>();
		List<Slot> unapprovedSlots = new ArrayList<Slot>();
		Slot slot;
		Iterator<Slot> it = slots.iterator();
		
		while(it.hasNext()) {
			slot = it.next();
			if (slot.getApproved() == true) {
				approvedSlots.add(slot);
			} else {
				unapprovedSlots.add(slot);
			}
		}
		
		empAppointment.setApprovedSlots(approvedSlots);
		empAppointment.setUnapprovedSlots(unapprovedSlots);	
	}
	/**
	 * Maps the recipients with the given targetPerson and convert the
	 * recipients list to string
	 * @param targetPerson target person
	 * @param recipients AppointmentReceipientTO recipients
	 * @return recipients string corresponding target person 
	 */
	private String mapRecipients(String targetPerson, List<AppointmentReceipientTO> recipients) {
		String recipientsStr = "";
		Iterator<AppointmentReceipientTO> it = recipients.iterator();
		AppointmentReceipientTO receipientTo;
		
		while(it.hasNext()) {
			receipientTo = it.next();
			if(getDisplayName(receipientTo.getTargetPersonUserInfo()).equals(targetPerson)) {
				recipientsStr = MessageUtil.formatRecipients(getDisplayNames(receipientTo.getReceipientUserInfos()));
				break;
			}
		}
		
		return recipientsStr;
	}
	
	/**
	 * Formats the rejected users with corresponding target person
	 * @param userRejected user who rejected slot
	 * @param recipients recipients list
	 * @return a list of rejected users
	 */
	private List<UserWithTarget> formatRejectedUsers(List<String> userRejected, List<AppointmentReceipientTO> recipients) {
		List<UserWithTarget> rejectedUsers = new ArrayList<UserWithTarget>();
		UserWithTarget user;
		Iterator<String> it = userRejected.iterator();
		String targetPerson;
		String recipientsStr;
		
		while(it.hasNext()) {
			targetPerson = it.next();
			recipientsStr = mapRecipients(targetPerson, recipients);
			user = new UserWithTarget();
			user.setTargetPerson(targetPerson);
			user.setRecipients(recipientsStr);
			rejectedUsers.add(user);
		}
		
		return rejectedUsers;
	}
	
	private List<UserWithTarget> calcUnrespondedUsers(Appointment appointment) {
		List<UserWithTarget> unrespondedUsers = new ArrayList<UserWithTarget>();
		List<String> userRejected = getDisplayNames(getUsers(appointment.getUsersRejectedWithComments()));
		List<AppointmentReceipientTO> recipients = appointment.getRecipients();
		AcceptedSlots acceptedSlots = appointment.getAcceptedSlots();
		
		Iterator<AppointmentReceipientTO> itApp = recipients.iterator();
		
		while(itApp.hasNext()) {
			AppointmentReceipientTO app = itApp.next();
			String targetPerson = getDisplayName(app.getTargetPersonUserInfo());
			
			if(!hasTargetPerson(targetPerson, acceptedSlots, userRejected)) {
				UserWithTarget user = new UserWithTarget();
				user.setTargetPerson(targetPerson);
				user.setTargetPersonUser(new KokuUser(app.getTargetPersonUserInfo()));
				for (User recipientUser : app.getReceipientUserInfos()) {
					user.getRecipientUsers(new KokuUser(recipientUser));					
				}
				user.setRecipients(MessageUtil.formatRecipients(getDisplayNames(app.getReceipientUserInfos())));
				unrespondedUsers.add(user);
			}
		}		
		return unrespondedUsers;
	}
	
	/**
     * @param users
     * @return
     */
    private List<User> getUsers(List<AppointmentUserRejected> users) {
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }
        
        final List<User> result = new ArrayList<User>(users.size());
        for (final AppointmentUserRejected user : users) {
            result.add(user.getUserInfo());
        }
        return result;
    }

    /**
     * @param receipientUserInfos
     * @return
     */
    private List<String> getDisplayNames(List<User> userInfos) {
        if (userInfos == null || userInfos.isEmpty()) {
            return Collections.emptyList();
        }
        
        final List<String> result = new ArrayList<String>(userInfos.size());
        for (final User user : userInfos) {
            result.add(getDisplayName(user));
        }
        
        return result;
    }

    /**
	 * Checks that the targetPeson exists or not
	 * @param targetPerson
	 * @param acceptedSlots
	 * @param userRejected
	 * @return true or false
	 */
	private boolean hasTargetPerson(String targetPerson, AcceptedSlots acceptedSlots, List<String> userRejected) {
		Iterator<Entry> itEntry = acceptedSlots.getEntry().iterator();
		
		while(itEntry.hasNext()) {
			Entry entry = itEntry.next();
			String target = getDisplayName(entry.getValue());
			
			if(target.equals(targetPerson)) {
				return true;
			}
		}
		
		Iterator<String> itRej = userRejected.iterator();
		while(itRej.hasNext()) {
			String user = itRej.next();
			if(user.equals(targetPerson)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Cancels appointment
	 * @param appointmentIdStr
	 * @param comment
	 */
	public void cancelAppointments(long appId, String comment) throws KokuServiceException {		
			aes.cancelAppointment(appId, comment);
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
