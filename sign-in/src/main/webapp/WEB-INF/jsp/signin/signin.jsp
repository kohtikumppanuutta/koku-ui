<%--
 Copyright 2011 Ixonos Plc, Finland. All rights reserved.

 You should have received a copy of the license text along with this program.
 If not, please contact the copyright holder (http://www.ixonos.com/).
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>

<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page import="javax.portlet.WindowState" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<portlet:defineObjects />

<div class="koku-signin"> 
	<div class="portlet-section-body">
	        
	   <div class="koku-signin-main" style="padding: 5px; padding-top: 10px; padding-bottom: 10px;">     
       <c:if test="${not empty fullname }">         
			<span class="koku-signin-right">
				<spring:message code="ui.signin.welcome" /> <b>${ fullname }</b> <c:if test="${ pwdSupported }"> <span class="koku-signin-tabulator"><spring:message code="ui.signin.separator" /></span> <a class="koku-signin-account" onclick="showKokuAccountSettings();"><spring:message code="ui.signin.account" /></a> </c:if> <span class="koku-signin-tabulator"><spring:message code="ui.signin.separator" /></span> <a class="koku-signin-logout" onclick="logoutKokuUser();"><spring:message code="ui.signin.logout" /></a>   
			</span> 			
       </c:if>
       
       
       <c:if test="${ empty fullname }">
       	   <span class="koku-signin-right">
       	     <c:if test="${ empty loginUrl }">
			 	<b><a class="koku-signin-login" onclick="loginKokuUser();"><spring:message code="ui.signin.login" /></a> </b>
			 </c:if>
			 
			 <c:if test="${ not empty loginUrl }">
			 	<b><a class="koku-signin-login" href="${loginUrl}"><spring:message code="ui.signin.login" /></a></b> 
			 </c:if>
			</span>
       </c:if>
       </div>
       <div class="koku-signin-reset-floating"></div>
      </div>	
</div>

<script type="text/javascript">
	
	function logoutKokuUser() {
		eXo.portal.logout();
	}
	
	function loginKokuUser() {
		if(document.getElementById('UIMaskWorkspace')) ajaxGet(eXo.env.server.createPortalURL('UIPortal', 'ShowLoginForm', true)); 
	}
	
	function showKokuAccountSettings() {
		if(document.getElementById('UIMaskWorkspace')) ajaxGet(eXo.env.server.createPortalURL('UIPortal', 'AccountSettings', true));
	}
	
</script>
