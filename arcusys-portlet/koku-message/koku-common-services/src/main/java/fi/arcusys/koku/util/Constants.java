package fi.arcusys.koku.util;


/**
 * Collection of constants
 * 
 * @author Toni Turunen
 *
 */
public class Constants {

	private Constants() { /* Non instantiable */  }
	
	public static final String DATE_FORMAT 								= "d.M.yyyy HH:mm:ss";
	public static final String DATE										= "d.M.yyyy";
	public static final String TIME 									= "HH:mm:ss";

	public static final String TIME_ZONE 								= "Europe/Helsinki";
	public static final int PAGE_NUMBER 								= 10; // number of tasks in one page
	
	/* ModelMap attribute key */
	public static final String RESPONSE									= "response";
	
	/* PortletURL attribute keys */
	public static final String ATTR_CURRENT_PAGE						= "currentPage";
	public static final String ATTR_TASK_TYPE	 						= "taskType";
	public static final String ATTR_TASK_LINK	 						= "tasklink";
	public static final String ATTR_KEYWORD								= "keyword";
	public static final String ATTR_ORDER_TYPE							= "orderType";
	public static final String ATTR_USERNAME							= "USER_username";
	public static final String ATTR_TOKEN								= "USER_token";
	public static final String ATTR_MESSAGE_ID							= "messageId";
	public static final String ATTR_REQUEST_ID							= "requestId";
	public static final String ATTR_APPOIMENT_ID						= "appointmentId";
	public static final String ATTR_CONSENT_ID							= "consentId";
	public static final String ATTR_AUTHORIZATION_ID					= "authorizationId";
	public static final String ATTR_MY_ACTION							= "myaction";
	public static final String ATTR_TARGET_PERSON						= "targetPerson";
	public static final String ATTR_PORTAL_INFO							= "portalInfo";
	
	/* ATTR_MY_ACTION parameter possible values  */
	public static final String MY_ACTION_SHOW_REQUEST 					= "showRequest";
	public static final String MY_ACTION_SHOW_APPOINTMENT				= "showAppointment";
	public static final String MY_ACTION_SHOW_CONSENT 					= "showConsent";
	public static final String MY_ACTION_SHOW_WARRANT					= "showWarrant";
	public static final String MY_ACTION_TASKFORM	 					= "taskform";

	
	/* ATTR_MY_TASK_TYPE parameter possible values. (Message-portlet) */
	public static final String TASK_TYPE_CONSENT_BROWSE					= "cst_browse_all_consents";			// Tietopyynnöt - Virkailija: Selaa tietopyyntöjä
	public static final String TASK_TYPE_CONSENT_EMPLOYEE_CONSENTS		= "cst_own_employee";					// Virkailijan lähetetyt suostumus pyynnöt 
	public static final String TASK_TYPE_CONSENT_CITIZEN_CONSENTS		= "cst_own_citizen";					// Kansalaiselle vastatut pyynnöt(/suostumukset) 
	public static final String TASK_TYPE_CONSENT_ASSIGNED_CITIZEN		= "cst_assigned_citizen";	 			// Kansalaiselle saapuneet pyynnöt(/suostumukset) 
	public static final String TASK_TYPE_WARRANT_LIST_CITIZEN_CONSENTS	= "cst_browse_customer_consents";		// Virkam.: Selaa asiakkaan valtakirjoja
	public static final String TASK_TYPE_WARRANT_LIST_SUBJECT_CONSENTS	= "cst_browse_subject_consents";		// Virkam.: Selaa asiakkaan valtakirjoja
	public static final String TASK_TYPE_WARRANT_BROWSE_RECEIEVED 		= "cst_browse_own_warrants_from_user";	// Kuntalainen: Valtuuttajana
	public static final String TASK_TYPE_WARRANT_BROWSE_SENT 			= "cst_browse_own_warrants_to_user";	// Kuntalainen: Valtuutettuna
	public static final String TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN 	= "app_response_citizen";
	public static final String TASK_TYPE_APPOINTMENT_RESPONSE_EMPLOYEE 	= "app_response_employee";
	public static final String TASK_TYPE_APPOINTMENT_INBOX_CITIZEN 		= "app_inbox_citizen";
	public static final String TASK_TYPE_APPOINTMENT_INBOX_EMPLOYEE 	= "app_inbox_employee";	
	public static final String TASK_TYPE_REQUEST_VALID_EMPLOYEE			= "req_valid";
	
	/* ATTR_PORTAL_INFO possible values */ 
	public static final String PORTAL_LIFERAY							= "liferay";
	public static final String PORTAL_JBOSS								= "jboss";
	public static final String PORTAL_GATEIN							= "gatein";
	
	/* Views */	
	public static final String VIEW_CONFIG								= "config";
	/* TaskManagerPortlet Views */
	public static final String VIEW_TASK_MANAGER						= "taskmanager";	
	public static final String VIEW_POPUP_FORM							= "popupform";
	public static final String VIEW_TASK_FORM							= "taskform";	

	/* Message-portlet Views*/
	public static final String VIEW_SHOW_REQUEST						= "showrequest";
	public static final String VIEW_SHOW_MESSAGE						= "showmessage";	
	public static final String VIEW_SHOW_INTALIO_FORM					= "showIntalioForm";
	public static final String VIEW_SHOW_CONSENT						= "showconsent";
	public static final String VIEW_SHOW_WARRANT						= "showwarrant";
	public static final String VIEW_SHOW_EMPLOYEE_APPOINTMENT			= "showemployeeappointment";
	public static final String VIEW_SHOW_CITIZEN_APPOINTMENT			= "showcitizenappointment";
	public static final String VIEW_MESSAGE								= "message";
	
	/* JSON keys */
	public static final String JSON_TOTAL_ITEMS 						= "totalItems";
	public static final String JSON_TOTAL_PAGES 						= "totalPages";
	public static final String JSON_TASKS 								= "tasks";
	public static final String JSON_TOKEN_STATUS 						= "tokenStatus";	                                                            	
	public static final String JSON_TASK_STATE							= "taskState";
	public static final String JSON_EDITABLE 							= "editable";
	public static final String JSON_RENDER_URL							= "renderUrl";
	public static final String JSON_RESULT								= "result";
	public static final String JSON_LOGIN_STATUS						= "loginStatus";

	/* PortletPreferences keys: */
	public static final String PREF_REFRESH_DURATION 					= "refreshDuration";
	
	/* PortletPreferences keys: TaskManager */
	public static final String PREF_TASK_FILTER 						= "taskFilter";
	public static final String PREF_NOTIFICATION_FILTER 				= "notifFilter";
	public static final String PREF_OPEN_FORM 							= "openForm";
	public static final String PREF_DEFAULT_TASK_TYPE 					= "defaultTaskType";
	public static final String PREF_EDITABLE 							= "editable";
	
	/* PortletPreferences keys: MessagePortlet */
	public static final String PREF_MESSAGE_TYPE 						= "messageType";

	/* ?  */
	public static final String TOKEN_STATUS_VALID						= "VALID";
	public static final String TOKEN_STATUS_INVALID						= "INVALID";

	/* JSON_RESULT possible values */
	public static final String RESPONSE_OK 								= "OK";
	public static final String RESPONSE_FAIL 							= "FAIL";
	
	/* JSON_Suggestion types */
	public static final String SUGGESTION_CONSENT						= "ConsentTemplateSuggestion";
	public static final String SUGGESTION_WARRANT						= "WarrantTemplateSuggestion";
	public static final String SUGGESTION_BROWSE_ALL_CONSENTS			= "BrowseAllConsents";	
	
}