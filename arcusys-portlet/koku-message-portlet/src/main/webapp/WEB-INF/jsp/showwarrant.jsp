<%@ include file="init.jsp"%>

<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>

<%@ include file="js_koku_detail.jspf" %>
<c:choose> 
  <c:when test="${warrant.responseStatus == 'FAIL'}" > 
  	<script type="text/javascript"> 
  			kokuErrorMsg += "<span class=\"failureUuid\">${warrant.errorCode}</span></div>";
			jQuery.jGrowl(kokuErrorMsg, kokuErrorMsgOptions);
	</script>
  </c:when> 
  <c:when test="${warrant.responseStatus == 'OK'}" >
	<div id="task-manager-wrap" class="single">
		<div id="show-message" style="padding:12px">
		<span class="text-bold"><spring:message code="warrant.templateName" />:</span> <c:out value="${warrant.model.template.templateName}" /><br />
		<span class="text-bold"><spring:message code="warrant.templateDescription" />:</span> <c:out value="${warrant.model.template.description}" /><br />
		<span class="text-bold"><spring:message code="warrant.receiver"/>:</span> <c:out value="${warrant.model.recieverName}" /><br />
		<span class="text-bold"><spring:message code="warrant.sender"/>:</span> <c:out value="${warrant.model.senderName}" /><br />
		<span class="text-bold"><spring:message code="warrant.targetPersonName"/>:</span> <c:out value="${warrant.model.targetPersonName}" /><br />		
		<span class="text-bold"><spring:message code="warrant.status"/>:</span> <c:out value="${warrant.model.localizedStatus}" /><br />
	<%-- 	<span class="text-bold"><spring:message code="warrant.createType"/>:</span> <c:out value="${warrant.model.localizedType}" /><br /> --%>
		<span class="text-bold"><spring:message code="warrant.createdAt"/>:</span> <c:out value="${warrant.model.createdAt}" /><br />
		<span class="text-bold"><spring:message code="warrant.givenDate"/>:</span> <c:out value="${warrant.model.givenAt}" /><br />
		<span class="text-bold"><spring:message code="warrant.validTill"/>:</span> <c:out value="${warrant.model.validTill}" /><br />
		<span class="text-bold"><spring:message code="warrant.replyTill"/>:</span> <c:out value="${warrant.model.replyTill}" /><br />
		
	<%--     <h3><spring:message code="consent.actionRequest"/></h3> --%>
	<!--     <table class="request-table"> -->
	<%--     	<tr><td class="head"><spring:message code="consent.description"/></td><td class="head"><spring:message code="consent.status"/></td></tr> --%>
	<%--     	<c:forEach var="request" items="${warrant.model.actionRequests}" varStatus="loopStatus"> --%>
	<%--         <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}"> --%>
	<%--           <td>${request.description}</td> --%>
	<%--           <td>${request.status}</td>     --%>
	<!--         </tr> -->
	<%--       	</c:forEach> --%>
	<!--     </table>   -->
	  </c:when> 
	</c:choose>

	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

