<%@ include file="init.jsp"%>

<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>

<portlet:resourceURL var="ajaxURL" id="getTask">
</portlet:resourceURL>

<%@ include file="js_koku_detail.jspf" %>
<%@ include file="js_koku_navigation_helper.jspf" %>
<%@ include file="js_koku_reset_view.jspf" %>


<div id="task-manager-wrap" class="single">
	<div id="show-message" style="padding:12px">
		<span class="text-bold"><spring:message code="application.kindergarten.name" />:</span> <c:out value="${application.kindergartenName}" /><br />
		<span class="text-bold"><spring:message code="application.kindergarten.applicantAccepted" />:</span> <c:out value="${application.applicantAccepted}" /><br />
		<span class="text-bold"><spring:message code="application.kindergarten.kindergarteAcceppted" />:</span> <c:out value="${application.placeAccepted}" /><br />
		<span class="text-bold"><spring:message code="application.kindergarten.childname" />:</span> <c:out value="${application.applicantName}" /><br />
		<span class="text-bold"><spring:message code="application.kindergarten.guardianName" />:</span> <c:out value="${application.applicantGuardianName}" /><br />
		<span class="text-bold"><spring:message code="application.kindergarten.createdAt" />:</span> <c:out value="${application.createdAt}" /><br />
		<span class="text-bold"><spring:message code="application.kindergarten.decision" />:</span> <c:out value="${application.answeredAt}" /><br />
		<span class="text-bold"><spring:message code="application.kindergarten.needForCareDate"/>:</span> <c:out value="${application.inEffectAt}" /><br />	
		<span class="modifyConsentLink">
		<a href="<%= portletPath %>/EditKindergarten?FormID=<c:out value="${application.applicationId}"/>"><spring:message code="application.kindergarten.details.sendConfirmation"/></a>
	</span><br />
	
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="kokuNavigationHelper.returnMainPage()" />
	</div>
</div>

