<%@ page import="fi.koku.taskmanager.util.Constants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page import="javax.portlet.WindowState" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 

<portlet:defineObjects />

<%
PortletPreferences preferences = renderRequest.getPreferences();

String taskFilter = preferences.getValue(Constants.PREF_TASK_FILTER, "");
String notifFilter = preferences.getValue(Constants.PREF_NOTIFICATION_FILTER, "");
String refreshDuration = preferences.getValue(Constants.PREF_REFRESH_DURATION, "30");
String openForm = preferences.getValue(Constants.PREF_OPEN_FORM, "1");
String defaultTaskType = preferences.getValue(Constants.PREF_DEFAULT_TASK_TYPE, "task");
// No need to show edit column in TaskManager portlet
// String editable = preferences.getValue(Constants.PREF_EDITABLE, "false");
String editable = "false";

%>