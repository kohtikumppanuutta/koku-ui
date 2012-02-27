<%@ include file="init.jsp"%>

<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>

<%@ include file="js_koku_detail.jspf" %>
<%@ include file="js_koku_navigation_helper.jspf" %>

<c:choose> 
  <c:when test="${appointment.responseStatus == 'FAIL'}" > 
  	<script type="text/javascript"> 
  			kokuErrorMsg += "<span class=\"failureUuid\"><c:out value="${appointment.errorCode}" /></span></div>";
			jQuery.jGrowl(kokuErrorMsg, kokuErrorMsgOptions);
	</script>
  </c:when>
  
  <c:when test="${appointment.responseStatus == 'OK'}" >

	<div id="task-manager-wrap" class="single">
		<div id="show-message" style="padding:12px">
		<c:if test="${appointment.model.senderUser != null}">
		<span class="request-c-1"><spring:message code="message.from"/>: <c:out value="${appointment.model.senderUser.fullName}" /> </span><br />
		</c:if>
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
	        <tr class="<c:out value="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}" />">
	          <td><c:out value="${slot.date}" /></td>
	          <td><c:out value="${slot.startTime}" /></td>
	          <td><c:out value="${slot.endTime}" /></td> 
	          <td><c:out value="${slot.location}" /></td>
	          <td><c:out value="${slot.comment}" /></td> 
	          <td><c:out value="${slot.targetPersonUser.fullName}" /></td>
	          <td>
	          <c:forEach var="recipient" items="${slot.recipientUsers}" varStatus="loopStatus">
	          	<c:out value="${recipient.fullName}" />,
	          </c:forEach>
	          </td>
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
	        <tr class="<c:out value="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}" />">
	          <td><c:out value="${slot.date}" /></td>
	          <td><c:out value="${slot.startTime}" /></td>
	          <td><c:out value="${slot.endTime}" /></td>  
	          <td><c:out value="${slot.location}" /></td>
	          <td><c:out value="${slot.comment}" /></td>  
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
	        <tr class="<c:out value="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}" />">
	          <td><c:out value="${user.targetPersonUser.fullName}" /></td>
	          <td>
	           	<c:forEach var="recipientUser" items="${user.recipientUsers}" varStatus="loopStatus">
		          <c:out value="${recipientUser.fullName}" />, 
	          	</c:forEach>
	          </td>  
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
	    	<c:forEach var="rejectedUser" items="${appointment.model.rejectedUsers}" varStatus="loopStatus">
	        <tr class="<c:out value="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}" />">
	          <td><c:out value="${rejectedUser.targetPersonUser.fullName}" /></td>
	            <td>
	           	<c:forEach var="rejectedUserInfo" items="${rejectedUser.recipientUsers}" varStatus="loopStatus">
		          <c:out value="${rejectedUserInfo.fullName}" />, 
	          	</c:forEach>
	          </td>  
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
	        <tr class="<c:out value="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}" />">
	          <td><c:out value="${reject.user.fullName}" /></td>
	          <td><c:out value="${reject.rejectComment}" /></td>  
	        </tr>
	      	</c:forEach>
	    </table>  
		</c:if>
	</c:when> 
</c:choose>
	
	
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="kokuNavigationHelper.returnMainPage()" />
	</div>
</div>

