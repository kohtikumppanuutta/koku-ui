<%@page import="fi.arcusys.koku.kv.model.KokuResponseDetail"%>
<%@ include file="init.jsp"%>
<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>

<%
// KokuResponseDetail response = (KokuResponseDetail) request.getAttribute("response");
%>

<script type="text/javascript"> 


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
		<span class="text-bold"><spring:message code="response.reciever" />:</span> <c:out value="${response.replierName}" /><br />
		<span class="text-bold"><spring:message code="response.sender" />:</span> <c:out value="${response.request.sender}" /><br />
		<span class="text-bold"><spring:message code="response.subject" />:</span> <c:out value="${response.request.subject}" /><br />
		<span class="text-bold"><spring:message code="response.createdAt" />:</span> <c:out value="${response.request.creationDate}" /><br />
		<span class="text-bold"><spring:message code="response.endDate" />:</span> <c:out value="${response.request.endDate}" /><br />
		
	
    <h3><spring:message code="response.questions.header"/></h3>
    <table class="request-table">
    	<tr>
    		<td class="head"><spring:message code="response.tableheader.indexnro"/></td>
    		<td class="head"><spring:message code="response.tableheader.description"/></td>    		
    		<td class="head"><spring:message code="response.tableheader.answer"/></td>
    	</tr>
    	<c:forEach var="question" items="${response.questions}" varStatus="loopStatus">
	        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
	          <td>${question.number}</td>
	          <td>${question.description}</td>
	          <td>${question.answer.answer}</td>
	        </tr>
      	</c:forEach>
    </table>
    <h3><spring:message code="response.comment"/></h3>
    <div class="responseComment">${response.comment}</div>
    
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
