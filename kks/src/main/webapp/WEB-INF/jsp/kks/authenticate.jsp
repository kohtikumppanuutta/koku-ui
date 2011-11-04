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
