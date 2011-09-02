<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page import="javax.portlet.WindowState" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 

<portlet:defineObjects />

<%
PortletPreferences preferences = renderRequest.getPreferences();

String taskFilter = preferences.getValue("taskFilter", "");
String notifFilter = preferences.getValue("notifFilter", "");
String refreshDuration = preferences.getValue("refreshDuration", "30");
String openForm = preferences.getValue("openForm", "1");
String defaultTaskType = preferences.getValue("defaultTaskType", "task");

%>