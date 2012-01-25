<%@ include file="init.jsp"%>

<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>

<%@ include file="js_koku_detail.jspf" %>

<c:choose> 
  <c:when test="${appointment.responseStatus == 'FAIL'}" > 
  	<script type="text/javascript"> 
  			kokuErrorMsg += "<span class=\"failureUuid\">${appointment.errorCode}</span></div>";
			jQuery.jGrowl(kokuErrorMsg, kokuErrorMsgOptions);
	</script>
  </c:when>
  
  <c:when test="${appointment.responseStatus == 'OK'}" >

	<div id="task-manager-wrap" class="single">
		<div id="show-message" style="padding:12px">
		<span class="request-c-1"><spring:message code="message.from"/>: <c:out value="${appointment.model.sender}" /> </span><br />
		<span class="request-c-1"><spring:message code="message.subject"/>:</span> <c:out value="${appointment.model.subject}" /><br />
		<span class="request-c-1"><spring:message code="message.description"/>:</span> <c:out value="${appointment.model.description}" /><br />
		<span class="request-c-1"><spring:message code="message.status"/>:</span> <c:out value="${appointment.model.status}" /><br />
		<c:if test="${appointment.model.cancellationComment != null}">
		<span class="request-c-1"><spring:message code="appointment.cancellationComment"/>:</span> <c:out value="${appointment.model.cancellationComment}" /><br />
		</c:if>
		
		
		<c:if test="${fn:length(appointment.model.approvedSlots) > 0}">
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
	    	<c:forEach var="slot" items="${appointment.model.approvedSlots}" varStatus="loopStatus">
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
		
		<c:if test="${fn:length(appointment.model.unapprovedSlots) > 0}">
	    <h3><spring:message code="appointment.unapprovedSlots"/></h3>   
	    <table class="request-table">
	    	<tr>
	    		<td class="head"><spring:message code="appointment.date"/></td>
	    		<td class="head"><spring:message code="appointment.start"/></td>
	    		<td class="head"><spring:message code="appointment.end"/></td>
	    		<td class="head"><spring:message code="appointment.location"/></td>
	    		<td class="head"><spring:message code="appointment.comment"/></td>
	    	</tr>
	    	<c:forEach var="slot" items="${appointment.model.unapprovedSlots}" varStatus="loopStatus">
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
		<c:if test="${fn:length(appointment.model.unrespondedUsers) == 0}">
			<spring:message code="appointment.none"/>
		</c:if>
		<c:if test="${fn:length(appointment.model.unrespondedUsers) > 0}">
		<table class="request-table">
	    	<tr>
	    		<td class="head"><spring:message code="appointment.targetPerson"/></td>
	    		<td class="head"><spring:message code="appointment.recipients"/></td>
	
	    	</tr>
	    	<c:forEach var="user" items="${appointment.model.unrespondedUsers}" varStatus="loopStatus">
	        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
	          <td>${user.targetPerson}</td>
	          <td>${user.recipients}</td>  
	        </tr>
	      	</c:forEach>
	    </table>  
	    </c:if>
		<h3><spring:message code="appointment.rejectedUsers"/></h3>
		<c:if test="${fn:length(appointment.model.rejectedUsers) == 0}">
			<spring:message code="appointment.none"/>
		</c:if>
		<c:if test="${fn:length(appointment.model.rejectedUsers) > 0}">
		<table class="request-table">
	    	<tr>
	    		<td class="head"><spring:message code="appointment.targetPerson"/></td>
	    		<td class="head"><spring:message code="appointment.recipients"/></td>
	    	</tr>
	    	<c:forEach var="user" items="${appointment.model.rejectedUsers}" varStatus="loopStatus">
	        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
	          <td>${user.targetPerson}</td>
	          <td>${user.recipients}</td>  
	        </tr>
	      	</c:forEach>
	    </table>  
		</c:if>
		
		<%-- Users who cancelled their appointment --%>
		<c:if test="${fn:length(appointment.model.usersRejected) > 0}">
		<h3><spring:message code="appointment.userCancellationHeader"/></h3>
		<table class="request-table">
	    	<tr>
	    		<td class="head"><spring:message code="appointment.targetPerson"/></td>
	    		<td class="head"><spring:message code="appointment.replierComment"/></td>
	    	</tr>
	    	<c:forEach var="reject" items="${appointment.model.usersRejected}" varStatus="loopStatus">
	        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
	          <td>${reject.userDisplayName}</td>
	          <td>${reject.rejectComment}</td>  
	        </tr>
	      	</c:forEach>
	    </table>  
		</c:if>
	</c:when> 
</c:choose>
	
	
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

