<%@ include file="init.jsp"%>

<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>

<script type="text/javascript"> 

/**
 * Returns to the main portlet page
 */
function returnMainPage() {
	window.location="<%= homeURL %>";
}

</script>
<div id="task-manager-wrap" class="single">
	<div id="show-message" style="padding:12px">
	<span class="text-bold"><spring:message code="consent.requester" />: <c:out value="${consent.requester}" /> </span><br />
	<span class="text-bold"><spring:message code="consent.templateName" />:</span> <c:out value="${consent.templateName}" /><br />
	<span class="text-bold"><spring:message code="consent.status"/>:</span> <c:out value="${consent.status}" /><br />
	<span class="text-bold"><spring:message code="consent.createType"/>:</span> <c:out value="${consent.createType}" /><br />
	<span class="text-bold"><spring:message code="consent.givenDate"/>:</span> <c:out value="${consent.assignedDate}" /><br />
	<span class="text-bold"><spring:message code="consent.validDate"/>:</span> <c:out value="${consent.validDate}" /><br />
	<span class="text-bold"><spring:message code="consent.secondApprover"/>:</span> <c:out value="${consent.anotherPermitterUid}" /><br />
	
    <h3>Action requests</h3>
    <table class="request-table">
    	<tr><td class="head"><spring:message code="consent.description"/></td><td class="head"><spring:message code="consent.status"/></td></tr>
    	<c:forEach var="request" items="${consent.actionRequests}" varStatus="loopStatus">
        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
          <td>${request.description}</td>
          <td>${request.status}</td>    
        </tr>
      	</c:forEach>
    </table>  

	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

