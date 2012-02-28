<%@page import="fi.koku.settings.KoKuPropertiesUtil"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page import="javax.portlet.WindowState" %>
<%@ page import="fi.arcusys.koku.util.Constants" %>
<%@ page import="fi.arcusys.koku.util.NavigationPortletProperties" %>
<%@ page import="fi.arcusys.koku.util.Properties" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<portlet:defineObjects />

<%
	PortletPreferences preferences = renderRequest.getPreferences();
// 	final String useRelativePath = preferences.getValue(Constants.PREF_NAVI_RELATIVE_PATH, "true");	
// 	final String kksPref = preferences.getValue(Constants.PREF_NAVI_KKS, "/KKS");
// 	final String lokPref = preferences.getValue(Constants.PREF_NAVI_LOK, "/LOK");
// 	final String pyhPref = preferences.getValue(Constants.PREF_NAVI_PYH, "/PYH");
// 	final String defaultPathPref = preferences.getValue(Constants.PREF_NAVI_DEFAULT_PATH, "/portal/auth/portal/default/koku/Message");
// 	final String frontPagePath = preferences.getValue(Constants.PREF_NAVI_FRONTPAGE, "/portal/auth/portal/default/koku/");

	final String defaultPath = NavigationPortletProperties.NAVIGATION_PORTLET_PATH;
	final String frontPagePath = NavigationPortletProperties.FRONTPAGE_LINK;
	final String kksPref = NavigationPortletProperties.KKS_PORTLET_LINK;
	final String lokPref = NavigationPortletProperties.LOK_PORTLET_LINK;
	final String pyhPref = NavigationPortletProperties.PYH_PORTLET_LINK;
	final String helpLinkPath = NavigationPortletProperties.HELP_LINK;

	final String defaultPage = defaultPath.substring(defaultPath.lastIndexOf('/')+1, defaultPath.length());
	final String naviPortalMode = Properties.PORTAL_MODE;
	request.setAttribute("naviPortalMode", naviPortalMode);	
	final String portalInfo = renderRequest.getPortalContext().getPortalInfo();	
	
	/* Current position in navigation */
	// final String navigationType = (String) renderRequest.getPortletSession().getAttribute("naviType");
	
%>