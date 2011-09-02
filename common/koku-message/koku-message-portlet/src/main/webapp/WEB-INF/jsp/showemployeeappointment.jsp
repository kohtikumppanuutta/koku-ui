<%@ include file="init.jsp"%>

<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>

<script type="text/javascript"> 

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
	<span class="request-c-1"><spring:message code="message.from"/>: <c:out value="${appointment.sender}" /> </span><br />
	<span class="request-c-1"><spring:message code="message.subject"/>:</span> <c:out value="${appointment.subject}" /><br />
	<span class="request-c-1"><spring:message code="message.description"/>:</span> <c:out value="${appointment.description}" /><br />
	<span class="request-c-1"><spring:message code="message.status"/>:</span> <c:out value="${appointment.status}" /><br />
	
	<c:if test="${fn:length(appointment.approvedSlots) > 0}">
    <h3><spring:message code="appointment.approvedSlots"/></h3>   
    <table class="request-table">
    	<tr>
    		<td class="head"><spring:message code="appointment.date"/></td>
    		<td class="head"><spring:message code="appointment.start"/></td>
    		<td class="head"><spring:message code="appointment.end"/></td>
    		<td class="head"><spring:message code="appointment.location"/></td>
    		<td class="head"><spring:message code="appointment.comment"/></td>
    		<td class="head"><spring:message code="appointment.targetPerson"/></td>
    		<td class="head"><spring:message code="appointment.recipients"/></td>
    	</tr>
    	<c:forEach var="slot" items="${appointment.approvedSlots}" varStatus="loopStatus">
        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
          <td>${slot.date}</td>
          <td>${slot.startTime}</td>
          <td>${slot.endTime}</td>  
          <td>${slot.location}</td>
          <td>${slot.comment}</td>  
          <td>${slot.targetPerson}</td>
          <td>${slot.recipients}</td>  
        </tr>
      	</c:forEach>
    </table>  
	</c:if>
	
	<c:if test="${fn:length(appointment.unapprovedSlots) > 0}">
    <h3><spring:message code="appointment.unapprovedSlots"/></h3>   
    <table class="request-table">
    	<tr>
    		<td class="head"><spring:message code="appointment.date"/></td>
    		<td class="head"><spring:message code="appointment.start"/></td>
    		<td class="head"><spring:message code="appointment.end"/></td>
    		<td class="head"><spring:message code="appointment.location"/></td>
    		<td class="head"><spring:message code="appointment.comment"/></td>
    	</tr>
    	<c:forEach var="slot" items="${appointment.unapprovedSlots}" varStatus="loopStatus">
        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
          <td>${slot.date}</td>
          <td>${slot.startTime}</td>
          <td>${slot.endTime}</td>  
          <td>${slot.location}</td>
          <td>${slot.comment}</td>  
        </tr>
      	</c:forEach>
    </table>  
	</c:if>
	
	<h3><spring:message code="appointment.unrepliedUsers"/></h3>
	<c:if test="${fn:length(appointment.unrespondedUsers) == 0}">
		<spring:message code="appointment.none"/>
	</c:if>
	<c:if test="${fn:length(appointment.unrespondedUsers) > 0}">
	<table class="request-table">
    	<tr>
    		<td class="head"><spring:message code="appointment.targetPerson"/></td>
    		<td class="head"><spring:message code="appointment.recipients"/></td>

    	</tr>
    	<c:forEach var="user" items="${appointment.unrespondedUsers}" varStatus="loopStatus">
        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
          <td>${user.targetPerson}</td>
          <td>${user.recipients}</td>  
        </tr>
      	</c:forEach>
    </table>  
    </c:if>
	<h3><spring:message code="appointment.rejectedUsers"/></h3>
	<c:if test="${fn:length(appointment.rejectedUsers) == 0}">
		<spring:message code="appointment.none"/>
	</c:if>
	<c:if test="${fn:length(appointment.rejectedUsers) > 0}">
	<table class="request-table">
    	<tr>
    		<td class="head"><spring:message code="appointment.targetPerson"/></td>
    		<td class="head"><spring:message code="appointment.recipients"/></td>

    	</tr>
    	<c:forEach var="user" items="${appointment.rejectedUsers}" varStatus="loopStatus">
        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
          <td>${user.targetPerson}</td>
          <td>${user.recipients}</td>  
        </tr>
      	</c:forEach>
    </table>  
	</c:if>
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

