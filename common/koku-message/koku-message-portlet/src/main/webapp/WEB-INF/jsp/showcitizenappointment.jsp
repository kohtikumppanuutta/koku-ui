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
	<span class="request-c-1"><spring:message code="message.from"/>: <c:out value="${appointment.sender}" /> </span><br />
	<span class="request-c-1"><spring:message code="message.subject"/>:</span> <c:out value="${appointment.subject}" /><br />
	<span class="request-c-1"><spring:message code="message.description"/>:</span> <c:out value="${appointment.description}" /><br />
	<span class="request-c-1"><spring:message code="message.status"/>:</span> <c:out value="${appointment.status}" /><br />
	<span class="request-c-1"><spring:message code="appointment.targetPerson"/>:</span> <c:out value="${appointment.targetPerson}" /><br />
	<span class="request-c-1"><spring:message code="appointment.replier"/>:</span> <c:out value="${appointment.replier}" /><br />
	<span class="request-c-1"><spring:message code="appointment.replierComment"/>:</span> <c:out value="${appointment.replierComment}" /><br />
	
	<c:if test="${appointment.slot != null}" >
    <h3><spring:message code="appointment.approvedSlots"/></h3>   
    <table class="request-table">
    	<tr>
    		<td class="head"><spring:message code="appointment.date"/></td>
    		<td class="head"><spring:message code="appointment.start"/></td>
    		<td class="head"><spring:message code="appointment.end"/></td>
    		<td class="head"><spring:message code="appointment.location"/></td>
    		<td class="head"><spring:message code="appointment.comment"/></td>
    	</tr>
        <tr class="evenRow">
          <td>${appointment.slot.date}</td>
          <td>${appointment.slot.startTime}</td>
          <td>${appointment.slot.endTime}</td>  
          <td>${appointment.slot.location}</td>
          <td>${appointment.slot.comment}</td>  
        </tr>
    </table>  
	</c:if>
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

