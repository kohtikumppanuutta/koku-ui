package fi.arcusys.koku.util;

import fi.koku.settings.KoKuPropertiesUtil;

public class NavigationPortletProperties {
		
	/**
	 *  koku-settings.properties keys 
	 */
	static class Property {		
		public static final String NAVIGATION_PORTLET_PATH 			= "navigationPortlet.portlet.path";
		public static final String FRONTPAGE_LINK 					= "navigationPortlet.link.frontpageUrl";
		public static final String KKS_PORTLET_LINK					= "navigationPortlet.link.kks";
		public static final String LOK_PORTLET_LINK					= "navigationPortlet.link.lok";
		public static final String PYH_PORTLET_LINK					= "navigationPortlet.link.pyh";
		public static final String HELP_LINK						= "navigationPortlet.link.help";
		
		public static final String MESSAGES_NEW_MESSAGE 			= "navigationPortlet.link.messages.newMessage";
		public static final String REQUESTS_NEW_REQUEST				= "navigationPortlet.link.requests.newRequest";
		public static final String REQUESTS_NEW_TEMPLATE			= "navigationPortlet.link.requests.newTemplate";
		public static final String APPOINTMENTS_NEW_APPOINTMENT		= "navigationPortlet.link.appointments.newAppointment";
		public static final String CONSENTS_NEW_CONSENT_TEMPLATE	= "navigationPortlet.link.consents.newConsentTemplate";
		public static final String CONSENTS_NEW_CONSENT				= "navigationPortlet.link.consents.newConsent";
		public static final String CONSENTS_CUSTOMER_CONSENT		= "navigationPortlet.link.consents.customerConsent";
		public static final String INFO_REQ_NEW_INFORMATION_REQ		= "navigationPortlet.link.informationRequests.newInformationRequest";
		public static final String INFO_REQ_RECIEVED_INFO_REQS		= "navigationPortlet.link.informationRequests.recievedInformationRequests";
	     
		public static final String WARRANTS_BROWSE_WARRANTS			= "navigationPortlet.link.warrants.browseWarrants";
		public static final String WARRANTS_NEW_WARRANT				= "navigationPortlet.link.warrants.newWarrant";
		public static final String REQUESTS_RECIEVED_REQUESTS		= "navigationPortlet.link.requests.recievedRequests";
		public static final String APPLICATIONS_NEW_KINDERGARTEN	= "navigationPortlet.link.applications.newKindergartenApplication";
		public static final String APPLICATIONS_NEED_TO_CONFIRM		= "navigationPortlet.link.applications.needToConfirm";
		public static final String CONSENTS_ANSWER_TO_CONSENT		= "navigationPortlet.link.consents.answerToConsent";
	}
	
	
	/* koku-settings.properties key values */
	
	public static final String NAVIGATION_PORTLET_PATH 					= KoKuPropertiesUtil.get(Property.NAVIGATION_PORTLET_PATH);
	public static final String FRONTPAGE_LINK 							= KoKuPropertiesUtil.get(Property.FRONTPAGE_LINK);	
	public static final String KKS_PORTLET_LINK 						= KoKuPropertiesUtil.get(Property.KKS_PORTLET_LINK);		
	public static final String LOK_PORTLET_LINK 						= KoKuPropertiesUtil.get(Property.LOK_PORTLET_LINK);			
	public static final String PYH_PORTLET_LINK 						= KoKuPropertiesUtil.get(Property.PYH_PORTLET_LINK);		
	public static final String HELP_LINK 								= KoKuPropertiesUtil.get(Property.HELP_LINK);
	
	public static final String MESSAGES_NEW_MESSAGE 		            = KoKuPropertiesUtil.get(Property.MESSAGES_NEW_MESSAGE);		
	public static final String REQUESTS_NEW_REQUEST			            = KoKuPropertiesUtil.get(Property.REQUESTS_NEW_REQUEST);
	public static final String REQUESTS_NEW_TEMPLATE		            = KoKuPropertiesUtil.get(Property.REQUESTS_NEW_TEMPLATE);
	public static final String APPOINTMENTS_NEW_APPOINTMENT	            = KoKuPropertiesUtil.get(Property.APPOINTMENTS_NEW_APPOINTMENT);
	public static final String CONSENTS_NEW_CONSENT_TEMPLATE            = KoKuPropertiesUtil.get(Property.CONSENTS_NEW_CONSENT_TEMPLATE);
    public static final String CONSENTS_NEW_CONSENT			            = KoKuPropertiesUtil.get(Property.CONSENTS_NEW_CONSENT);    public static final String CONSENTS_CUSTOMER_CONSENT	            = KoKuPropertiesUtil.get(Property.CONSENTS_CUSTOMER_CONSENT);    public static final String INFO_REQ_NEW_INFORMATION_REQ	            = KoKuPropertiesUtil.get(Property.INFO_REQ_NEW_INFORMATION_REQ);    public static final String INFO_REQ_RECIEVED_INFO_REQS	            = KoKuPropertiesUtil.get(Property.INFO_REQ_RECIEVED_INFO_REQS);
    		    public static final String WARRANTS_BROWSE_WARRANTS		            = KoKuPropertiesUtil.get(Property.WARRANTS_BROWSE_WARRANTS);    public static final String WARRANTS_NEW_WARRANT			            = KoKuPropertiesUtil.get(Property.WARRANTS_NEW_WARRANT);    public static final String REQUESTS_RECIEVED_REQUESTS	            = KoKuPropertiesUtil.get(Property.REQUESTS_RECIEVED_REQUESTS);    public static final String APPLICATIONS_NEW_KINDERGARTEN            = KoKuPropertiesUtil.get(Property.APPLICATIONS_NEW_KINDERGARTEN);    public static final String APPLICATIONS_NEED_TO_CONFIRM	            = KoKuPropertiesUtil.get(Property.APPLICATIONS_NEED_TO_CONFIRM);
    public static final String CONSENTS_ANSWER_TO_CONSENT				= KoKuPropertiesUtil.get(Property.CONSENTS_ANSWER_TO_CONSENT);
    
}
    
    