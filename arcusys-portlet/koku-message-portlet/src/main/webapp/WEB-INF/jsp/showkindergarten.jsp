<%@ include file="init.jsp"%>

<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>

<portlet:resourceURL var="ajaxURL" id="getTask">
</portlet:resourceURL>

<script type="text/javascript"> 

<%
/* Parses the parent path url from the portlet ajaxURL */

String defaultPath = "";

int pos = ajaxURL.indexOf("default");
if(pos > -1) { // for Jboss portal
	defaultPath = ajaxURL.substring(0, pos+7);		
}else { // for Gatein portal
	int pos1 = ajaxURL.indexOf("classic");
	defaultPath = ajaxURL.substring(0, pos1+7);
}
%>

/**
 * Returns to the main portlet page
 */
 
function returnMainPage() {
	var url = "<%= homeURL %>";
	url = formatUrl(url);
	window.location = url;
}

/* Formats url mainly for gatein epp*/
function formatUrl(url) {
	var newUrl;
	newUrl = url.replace(/&quot;/g,'"');
	newUrl = newUrl.replace(/&amp;/g,"&");
	newUrl = newUrl.replace(/&lt;/g,"<");
	newUrl =  newUrl.replace(/&gt;/g,">");
	
	return newUrl;
}

</script>
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
		<a href="<%= defaultPath %>/Message/EditKindergarten?FormID=<c:out value="${application.applicationId}"/>"><spring:message code="application.kindergarten.details.sendConfirmation"/></a>
	</span><br />
	
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

