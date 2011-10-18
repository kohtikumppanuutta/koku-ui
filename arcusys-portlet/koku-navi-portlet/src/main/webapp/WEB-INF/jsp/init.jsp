<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page import="javax.portlet.WindowState" %>
<%@ page import="fi.arcusys.koku.util.Constants" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<portlet:defineObjects />


<%

	PortletPreferences preferences = renderRequest.getPreferences();
	String useRelativePath = preferences.getValue(Constants.PREF_NAVI_RELATIVE_PATH, "true");
	String kksPref = preferences.getValue(Constants.PREF_NAVI_KKS, "/KKS");
	String lokPref = preferences.getValue(Constants.PREF_NAVI_LOK, "/LOK");
	String pyhPref = preferences.getValue(Constants.PREF_NAVI_PYH, "/PYH");

%>