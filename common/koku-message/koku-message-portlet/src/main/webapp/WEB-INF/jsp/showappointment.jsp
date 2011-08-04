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
<div id="task-manager-wrap">
	<div id="show-message" style="padding:12px">
	<span class="request-c-1">Sender: <c:out value="${appointment.sender}" /> </span><br />
	<span class="request-c-1">Recipients:</span> <c:out value="${appointment.recipients}" /><br />
	<span class="request-c-1">Subject:</span> <c:out value="${appointment.subject}" /><br />
	<span class="request-c-1">Description:</span> <c:out value="${appointment.description}" /><br />
	
    <h3>Slots</h3>
    <table class="request-table">
    	<tr><td class="head">Date</td><td class="head">Start</td><td class="head">End</td><td class="head">Location</td></tr>
    	<c:forEach var="slot" items="${appointment.slots}" varStatus="loopStatus">
        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
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

