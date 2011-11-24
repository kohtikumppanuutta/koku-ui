<%@page import="fi.koku.settings.KoKuPropertiesUtil"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page import="javax.portlet.WindowState" %>
<%@ page import="fi.arcusys.koku.util.Constants" %>
<%@ page import="fi.arcusys.koku.web.PortletProperties" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 

<portlet:defineObjects />

<%
final PortletPreferences preferences = renderRequest.getPreferences();
final String refreshDuration = preferences.getValue(Constants.PREF_REFRESH_DURATION, "30");
final String messageType = preferences.getValue(Constants.PREF_MESSAGE_TYPE, "2");
// final String portletPath = preferences.getValue(Constants.PREF_MESSAGE_PORTLET_PATH, "/portal/private/classic/Message");
final String portletPath = PortletProperties.NAVIGATION_PORTLET_PATH;
final String portalInfo = renderRequest.getPortalContext().getPortalInfo();
final String naviPortalMode = KoKuPropertiesUtil.get("environment.name");
%>