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
	<span class="request-c-1"><spring:message code="message.receiver"/>:</span> <c:out value="${appointment.recipients}" /><br />
	<span class="request-c-1"><spring:message code="message.subject"/>:</span> <c:out value="${appointment.subject}" /><br />
	<span class="request-c-1"><spring:message code="message.description"/>:</span> <c:out value="${appointment.description}" /><br />
	<span class="request-c-1"><spring:message code="message.status"/>:</span> <c:out value="${appointment.status}" /><br />
	
    <h3>Slots</h3>
    <table class="request-table">
    	<tr><td class="head"><spring:message code="appointment.approved"/></td><td class="head"><spring:message code="appointment.date"/></td>
    	<td class="head"><spring:message code="appointment.start"/></td><td class="head"><spring:message code="appointment.end"/></td>
    	<td class="head"><spring:message code="appointment.location"/></td></tr>
    	<c:forEach var="slot" items="${appointment.slots}" varStatus="loopStatus">
        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
          <td width=50><input type="checkbox" <c:if test="${appointment.approvedSlotNumber == slot.slotNumber}">checked="checked"</c:if> /></td>
          <td>${slot.date}</td>
          <td>${slot.startTime}</td>
          <td>${slot.endTime}</td>  
          <td>${slot.location}</td>      
        </tr>
      	</c:forEach>
    </table>  

	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

