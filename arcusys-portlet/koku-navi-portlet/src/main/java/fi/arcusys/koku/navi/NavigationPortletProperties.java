package fi.arcusys.koku.navi;

import fi.koku.settings.KoKuPropertiesUtil;

public class NavigationPortletProperties {
		
	/* koku-settings.properties keys */
	public static final String PROPERTY_NAVIGATION_PORTLET_PATH 		= "navigationPortlet.portlet.path";
	public static final String PROPERTY_FRONTPAGE_LINK 					= "navigationPortlet.link.frontpageUrl";
	public static final String PROPERTY_KKS_PORTLET_LINK				= "navigationPortlet.link.kks";
	public static final String PROPERTY_LOK_PORTLET_LINK				= "navigationPortlet.link.lok";
	public static final String PROPERTY_PYH_PORTLET_LINK				= "navigationPortlet.link.pyh";
	public static final String PROPERTY_HELP_LINK						= "navigationPortlet.link.help";
	
	
	/* koku-settings.properties key values */
	
	public static final String NAVIGATION_PORTLET_PATH 					= KoKuPropertiesUtil.get(PROPERTY_NAVIGATION_PORTLET_PATH);
	public static final String FRONTPAGE_LINK 							= KoKuPropertiesUtil.get(PROPERTY_FRONTPAGE_LINK);	
	public static final String KKS_PORTLET_LINK 						= KoKuPropertiesUtil.get(PROPERTY_KKS_PORTLET_LINK);		
	public static final String LOK_PORTLET_LINK 						= KoKuPropertiesUtil.get(PROPERTY_LOK_PORTLET_LINK);			
	public static final String PYH_PORTLET_LINK 						= KoKuPropertiesUtil.get(PROPERTY_PYH_PORTLET_LINK);		
	public static final String HELP_LINK 								= KoKuPropertiesUtil.get(PROPERTY_HELP_LINK);
				
}
