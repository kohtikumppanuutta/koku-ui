package fi.arcusys.koku.util;

public class Constants {

	private Constants() { /* Non instantiable */  }
			
	
	/* PortletURL attribute names */
	public static final String ATTR_CURRENT_PAGE						= "currentPage";
	public static final String ATTR_TASK_TYPE	 						= "taskType";
	public static final String ATTR_KEYWORD								= "keyword";
	public static final String ATTR_ORDER_TYPE							= "orderType";
	public static final String ATTR_USERNAME							= "USER_username";
	public static final String ATTR_TOKEN								= "USER_token";
	public static final String ATTR_MESSAGE_ID							= "messageId";
	public static final String ATTR_REQUEST_ID							= "requestId";
	public static final String ATTR_APPOIMENT_ID						= "appointmentId";
	public static final String ATTR_CONSENT_ID							= "consentId";
	public static final String ATTR_MY_ACTION							= "myaction";
	public static final String ATTR_TARGET_PERSON						= "targetPerson";
	public static final String ATTR_PORTAL_INFO							= "portalInfo";
	
	/* ATTR_MY_ACTION parameter possible values  */
	public static final String MY_ACTION_SHOW_REQUEST 					= "showRequest";
	public static final String MY_ACTION_SHOW_APPOINTMENT				= "showAppointment";
	public static final String MY_ACTION_SHOW_CONSENT 					= "showConsent";

	/* ATTR_MY_TASK_TYPE parameter possible values */
	public static final String TASK_TYPE_CONSENT_EMPLOYEE_CONSENTS		= "cst_own_employee";
	public static final String TASK_TYPE_CONSENT_CITIZEN_CONSENTS		= "cst_own_citizen";	
	public static final String TASK_TYPE_CONSENT_LIST_CITIZEN_CONSENTS	= "cst_browse_customer_consents";
	public static final String TASK_TYPE_CONSENT_ASSIGNED_CITIZEN		= "cst_assigned_citizen";	
	public static final String TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN 	= "app_response_citizen";
	public static final String TASK_TYPE_APPOINTMENT_RESPONSE_EMPLOYEE 	= "app_response_employee";
	public static final String TASK_TYPE_APPOINTMENT_INBOX_CITIZEN 		= "app_inbox_citizen";
	public static final String TASK_TYPE_APPOINTMENT_INBOX_EMPLOYEE 	= "app_inbox_employee";	
	public static final String TASK_TYPE_REQUEST_VALID_EMPLOYEE			= "req_valid";
	
	/* ATTR_PORTAL_INFO possible values */ 
	public static final String PORTAL_LIFERAY							= "liferay";
	public static final String PORTAL_JBOSS								= "jboss";
	public static final String PORTAL_GATEIN							= "gatein";
	
}
