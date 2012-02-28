<%@page import="fi.arcusys.koku.kv.model.KokuResponseDetail"%>
<%@ include file="init.jsp"%>
<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>

<%@ include file="js_koku_detail.jspf" %>
<%@ include file="js_koku_navigation_helper.jspf" %>
<%@ include file="js_koku_reset_view.jspf" %>


<c:choose> 
  <c:when test="${response.responseStatus == 'FAIL'}" > 
  	<script type="text/javascript"> 
  			kokuErrorMsg += "<span class=\"failureUuid\"><c:out value="${response.errorCode}" /></span></div>";
			jQuery.jGrowl(kokuErrorMsg, kokuErrorMsgOptions);
	</script>
  </c:when> 
  
  <c:when test="${response.responseStatus == 'OK'}" >	   
	<div id="task-manager-wrap" class="single">
		<div id="show-message" style="padding:12px">
			<span class="text-bold"><spring:message code="response.reciever" />:</span> <c:out value="${response.model.replierUser.fullName}" /><br />
			<span class="text-bold"><spring:message code="response.sender" />:</span> <c:out value="${response.model.request.senderUser.fullName}" /><br />
			<span class="text-bold"><spring:message code="response.subject" />:</span> <c:out value="${response.model.request.subject}" /><br />
			<span class="text-bold"><spring:message code="response.createdAt" />:</span> <c:out value="${response.model.request.creationDate}" /><br />
			<span class="text-bold"><spring:message code="response.endDate" />:</span> <c:out value="${response.model.request.endDate}" /><br />
		
		    <h3><spring:message code="response.questions.header"/></h3>
		    <table class="request-table">
		    	<tr>
		    		<td class="head"><spring:message code="response.tableheader.indexnro"/></td>
		    		<td class="head"><spring:message code="response.tableheader.description"/></td>    		
		    		<td class="head"><spring:message code="response.tableheader.answer"/></td>
		    	</tr>
		    	<c:forEach var="question" items="${response.model.questions}" varStatus="loopStatus">
			        <tr class="<c:out value="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}"/>">
			          <td><c:out value="${question.number}"/></td>
			          <td><c:out value="${question.description}"/></td>
			          <td><c:out value="${question.answer.answer}"/></td>
			        </tr>
		      	</c:forEach>
		    </table>
		    <h3><spring:message code="response.comment"/></h3>
		    <div class="responseComment"><c:out value="${response.model.comment}"/></div>
    
		</div>
	</div>
	  </c:when> 
</c:choose>
	
	
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="kokuNavigationHelper.returnMainPage()" />
	</div>
