<%@ include file="init.jsp"%>

<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>

<%@ include file="js_koku_detail.jspf" %>

<c:choose> 
  <c:when test="${appointment.responseStatus == 'FAIL'}" > 
  	<script type="text/javascript"> 
  			kokuErrorMsg += "<span class=\"failureUuid\"><c:out value="${appointment.errorCode}" /></span></div>";
			jQuery.jGrowl(kokuErrorMsg, kokuErrorMsgOptions);
	</script>
  </c:when>
  
  <c:when test="${appointment.responseStatus == 'OK'}" >

	</script>
	<div id="task-manager-wrap" class="single">
		<div id="show-message" style="padding:12px">
		<c:if test="${appointment.model.senderUser != null}">
		<span class="request-c-1"><spring:message code="message.from"/>: <c:out value="${appointment.model.senderUser.fullName}" /> </span><br />
		</c:if>		
		<span class="request-c-1"><spring:message code="message.subject"/>:</span> <c:out value="${appointment.model.subject}" /><br />
		<span class="request-c-1"><spring:message code="message.description"/>:</span> <c:out value="${appointment.model.description}" /><br />
		<span class="request-c-1"><spring:message code="message.status"/>:</span> <c:out value="${appointment.model.status}" /><br />
		<c:if test="${appointment.model.cancellationComment  != null}">
		<span class="request-c-1"><spring:message code="appointment.cancellationComment"/>:</span> <c:out value="${appointment.model.cancellationComment}" /><br />
		</c:if>
		<c:if test="${appointment.model.targetPersonUser != null}">
		<span class="request-c-1"><spring:message code="appointment.targetPerson"/>:</span> <c:out value="${appointment.model.targetPersonUser.fullName}" /><br />
		</c:if>
		<span class="request-c-1"><spring:message code="appointment.replier"/>:</span> <c:out value="${appointment.model.replierUser.fullName}" /><br />
		<span class="request-c-1"><spring:message code="appointment.replierComment"/>:</span> <c:out value="${appointment.model.replierComment}" /><br />
		
		<c:if test="${appointment.model.slot != null}" >
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
	          <td><c:out value="${appointment.model.slot.date}" /></td>
	          <td><c:out value="${appointment.model.slot.startTime}" /></td>
	          <td><c:out value="${appointment.model.slot.endTime}" /></td>
	          <td><c:out value="${appointment.model.slot.location}" /></td>
	          <td><c:out value="${appointment.model.slot.comment}" /></td>
	        </tr>
	    </table>  
		</c:if>		
	</c:when>
</c:choose>
	
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

