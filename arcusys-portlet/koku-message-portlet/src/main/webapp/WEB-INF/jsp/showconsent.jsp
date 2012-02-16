<%@ include file="init.jsp"%>

<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>

<portlet:resourceURL var="ajaxURL" id="getTask">
</portlet:resourceURL>

<%
	/* Parses the parent path url from the portlet ajaxURL */
	final String defaultPath = portletPath;
%>

<%@ include file="js_koku_detail.jspf" %>

<c:choose> 
  <c:when test="${consent.responseStatus == 'FAIL'}" > 
  	<script type="text/javascript"> 
  			kokuErrorMsg += "<span class=\"failureUuid\"><c:out value="${consent.errorCode}" /></span></div>";
			jQuery.jGrowl(kokuErrorMsg, kokuErrorMsgOptions);
	</script>
  </c:when> 
  <c:when test="${consent.responseStatus == 'OK'}" >	   

	<div id="task-manager-wrap" class="single">
		<div id="show-message" style="padding:12px">
		<span class="text-bold"><spring:message code="consent.requester" />: <c:out value="${consent.model.requester}" /> </span><br />
		<span class="text-bold"><spring:message code="consent.templateName" />:</span> <c:out value="${consent.model.templateName}" /><br />
		<span class="text-bold"><spring:message code="consent.templateTypeName" />:</span> <c:out value="${consent.model.templateTypeName}" /><br />
		<span class="text-bold"><spring:message code="consent.status"/>:</span> <c:out value="${consent.model.status}" /><br />
		<span class="text-bold"><spring:message code="consent.approvalStatus"/>:</span> <c:out value="${consent.model.approvalStatus}" /><br />
		<span class="text-bold"><spring:message code="consent.createType"/>:</span> <c:out value="${consent.model.createType}" /><br />
		<span class="text-bold"><spring:message code="consent.givenDate"/>:</span> <c:out value="${consent.model.assignedDate}" /><br />
		<span class="text-bold"><spring:message code="consent.validDate"/>:</span> <c:out value="${consent.model.validDate}" /><br />
		<c:if test="${consent.model.anotherPermitterUid != null && consent.model.anotherPermitterUid != '' }">
		<span class="text-bold"><spring:message code="consent.secondApprover"/>:</span> <c:out value="${consent.model.anotherPermitterUid}" /><br />
		</c:if>
		<span class="text-bold"><spring:message code="consent.recipients"/>:</span> <c:out value="${consent.model.recipients}" /><br />
		<c:if test="${consent.model.targetPerson != null && consent.model.targetPerson != '' }">
		<span class="text-bold"><spring:message code="consent.targetPerson"/>:</span> <c:out value="${consent.model.targetPerson.fullName}" /><br />
		</c:if>
		
		<span class="text-bold"><spring:message code="consent.comment"/>:</span> <c:out value="${consent.model.comment}" /><br />
		
		<% if (naviPortalMode.equals(Constants.PORTAL_MODE_KUNPO)) { %>
		<span class="modifyConsentLink">
			<a href="<%= defaultPath %><%= NavigationPortletProperties.CONSENTS_NEW_CONSENT %>?FormID=<c:out value="${consent.model.consentId}"/>"><spring:message code="consent.modifyConsentLink"/></a>
		</span><br />
		<% } %>
		
		
	    <h3><spring:message code="consent.actionRequest"/></h3>
	    <table class="request-table">
	    	<tr>
	    		<td class="head"><spring:message code="consent.name"/></td>
	    		<td class="head"><spring:message code="consent.description"/></td>
	    		<td class="head"><spring:message code="consent.status"/></td>
	    	</tr>
	    	<c:forEach var="request" items="${consent.model.actionRequests}" varStatus="loopStatus">
	        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
	          <td><c:out value="${request.name}" /></td>
	          <td><c:out value="${request.description}" /></td>
	          <td style="min-width:60px"><c:out value="${request.status}" /></td>    
	        </tr>
	      	</c:forEach>
	    </table>
	</c:when> 
</c:choose>


	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

