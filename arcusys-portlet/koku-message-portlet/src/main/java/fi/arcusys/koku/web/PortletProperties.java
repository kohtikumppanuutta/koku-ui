package fi.arcusys.koku.web;

import fi.koku.settings.KoKuPropertiesUtil;

public class PortletProperties {
	/* koku-settings.properties keys */
	public static final String PROPERTY_MESSAGE_PORTLET_PATH 		= "messagePortlet.path";	
	
	/* koku-settings.properties key values */	
	public static final String MESSAGE_PORTLET_PATH 					= KoKuPropertiesUtil.get(PROPERTY_MESSAGE_PORTLET_PATH);
}
