package fi.arcusys.koku.palvelut.util;

import javax.portlet.PortletRequest;

/**
 * Porting from Liferay to JBoss - here I've placed all values, related to Liferay's specifics.
 * 
 * @author Dmitry Kudinov (dmitry.kudinov@arcusys.fi)
 * Apr 15, 2011
 */
public class MigrationUtil {
		
	
	public static String getCompanyWebId(PortletRequest request) {
//		PortalUtil.getCompany(request).getWebId()
		return "test";
	}

	public static String getUser(PortletRequest request) {
//		PortalUtil.getUser(request).getScreenName()
		return request.getUserPrincipal().getName();
	}
		
	
	public static String getUserPassword(PortletRequest request) {
//		PortalUtil.getUserPassword(request)
		return "test";
//		return getUser(request);
	}
	

}
