<%--
 Copyright 2012 Ixonos Plc, Finland. All rights reserved.
  
 This file is part of Kohti kumppanuutta.
 
 This file is licensed under GNU LGPL version 3.
 Please see the 'license.txt' file in the root directory of the package you received.
 If you did not receive a license, please contact the copyright holder
 (http://www.ixonos.com/).
--%>
<%@ include file="kks/imports.jsp" %>
<%@ page language="java" import="java.util.*" %>
<portlet:defineObjects />
	
<fmt:setBundle basename="Language-ext"/>


<c:choose>
	<c:when test="${not empty requestScope.authenticationURL}">
	  <div class="portlet-section-text">
	   	<fmt:message key="ui.requires.strong.authentication"/>
	  </div>
	  <div class="kks-link">
	  	<a href="${requestScope.authenticationURL}"><fmt:message key="ui.log.in.using.strong.authentication"/></a>
	  </div>
	</c:when>
	<c:otherwise>
		<div class="portlet-section-text">
	  		<fmt:message key="ui.not.logged.in"/>
	  	</div>
	</c:otherwise>
</c:choose>

