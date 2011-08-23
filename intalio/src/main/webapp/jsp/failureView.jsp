
<%@ include file="/jsp/init.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="fi.arcusys.koku.palvelut.model.client.FormHolder"%>
<%@ page import="fi.arcusys.koku.palvelut.controller.ViewController"%>


<%@page import="javax.portlet.PortletPreferences"%><div class="portlet-linkit-container">

	<!-- UI-messages -->
	<jsp:include page="/jsp/messages.jsp" />
	
	<!-- Portlet Header -->
	
	<div class="portlet-header">
	
		<jsp:include page="/jsp/breadCrumb.jsp" />	
		
		<div class="failureJsp">
			<div class="failureMessage" style="color: red;">Oops.. Something went wrong in the server. Please refresh the page and try again or contact to Administrator.</div>			
		</div>
	</div>
