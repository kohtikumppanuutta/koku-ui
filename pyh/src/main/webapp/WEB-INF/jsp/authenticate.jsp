<%--
 Copyright 2011 Ixonos Plc, Finland. All rights reserved.

 You should have received a copy of the license text along with this program.
 If not, please contact the copyright holder (http://www.ixonos.com/).
--%>
<%@ include file="pyh/imports.jsp" %>
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

