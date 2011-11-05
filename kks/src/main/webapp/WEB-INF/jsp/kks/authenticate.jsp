<%--
 Copyright 2011 Ixonos Plc, Finland. All rights reserved.

 You should have received a copy of the license text along with this program.
 If not, please contact the copyright holder (http://www.ixonos.com/).
--%>
<%@ include file="imports.jsp" %>

<portlet:defineObjects />

<c:choose>
	<c:when test="${stronAuthenticationURL}">
	  <div class="portlet-section-text">
	   	<spring:message code="ui.requires.strong.authentication"/>
	  </div>
	  <div class="kks-link">
	  	<a href="${stronAuthenticationURL}"><spring:message code="ui.log.in.using.strong.authentication"/></a>
	  </div>
	</c:when>
	<c:otherwise>
		<div class="portlet-section-text">
	  		<spring:message code="ui.not.logged.in"/>
	  	</div>
	</c:otherwise>
</c:choose>
