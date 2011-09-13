package fi.arcusys.koku.palvelut.util;

public class Constants {
	
	/* PortletPreferences key values */	
	/** ? */
	public static final String SHOW_ONLY_CHECKED 						= "showOnlyChecked";
	public static final String SHOW_ONLY_FORM_BY_ID 					= "showOnlyForm";
	public static final String SHOW_ONLY_FORM_BY_DESCRIPTION			= "showOnlyFormByDescription";
	/** Use taskId or taskDescription to get intalio form. Possible values: "true" / "false" */
	public static final String SHOW_TASKS_BY_ID							= "useTaskId";
	
	/* WS service names */
	public static final String APPOINTMENT_PROCESSING_SERVICE_NAME 		= "AppointmentProcessingService";
	public static final String APPOINTMENT_SERVICE_CITIZEN_NAME 		= "AppointmentServiceCitizen";
	public static final String APPOINTMENT_SERVICE_EMPLOYEE_NAME 		= "AppointmentServiceEmployee";
	public static final String MESSAGE_SERVICE_NAME 					= "MessageService";
	public static final String REQUEST_SERVICE_NAME						= "RequestService";
	public static final String TIVA_CITIZEN_SERVICE_NAME				= "TivaServiceCitizen";
	public static final String TIVA_EMPLOYEE_SERVICE_NAME				= "TivaServiceEmployee";
	
	
	/* ModelAndView attribute names */
	/** PortalId. Check Portal types PORTAL_*  */
	public static final String ATTR_PORTAL_ID 							= "portal";
	/** PortalPreferences key */
	public static final String ATTR_PREFERENCES 						= "prefs";
	public static final String ATTR_FORM_LIST							= "formList";
	
	/* Portal types */
	public static final String PORTAL_JBOSS								= "jBoss";
	public static final String PORTAL_GATEIN							= "gateIn";
	public static final String PORTAL_LIFERAY							= "liferay";
	
	private Constants() {
		// Not instantiable
	}

}
