<%--
 Copyright 2012 Ixonos Plc, Finland. All rights reserved.
  
 This file is part of Kohti kumppanuutta.
 
 This file is licensed under GNU LGPL version 3.
 Please see the 'license.txt' file in the root directory of the package you received.
 If you did not receive a license, please contact the copyright holder
 (http://www.ixonos.com/).
--%>
<%-- <%@ include file="init.jsp"%> --%>
<%-- <%@ include file="kks\imports.jsp" %> --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" %>
<%@ page contentType="text/html" isELIgnored="false"%>

<portlet:defineObjects />
	
<!-- <fmt:setBundle basename="Language-ext"/> -->

<c:choose>
	<c:when test="${not empty requestScope.authenticationURL}">
	  <div class="portlet-section-text">
<%-- 	   	<spring:message code="ui.requires.strong.authentication"/> --%>
		Tämä sovellus edellyttää vahvaa tunnistautumista
	  </div>
	  <div class="kks-link">
	  	<a href="${requestScope.authenticationURL}">Klikkaa tästä tunnistautuaksesi vahvasti
<%-- 	  	<spring:message code="ui.log.in.using.strong.authentication"/></a> --%>

	  </div>
	</c:when>
	<c:otherwise>
		<div class="portlet-section-text">
<%-- 	  		<spring:message code="ui.not.logged.in"/> --%>
			Kirjaudu järjestelmään
	  	</div>
	</c:otherwise>
</c:choose>

