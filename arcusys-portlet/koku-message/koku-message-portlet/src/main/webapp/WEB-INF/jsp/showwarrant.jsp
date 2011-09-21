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
	<span class="text-bold"><spring:message code="warrant.templateName" />:</span> <c:out value="${warrant.template.templateName}" /><br />
	<span class="text-bold"><spring:message code="warrant.templateDescription" />:</span> <c:out value="${warrant.template.description}" /><br />
	<span class="text-bold"><spring:message code="warrant.receiver"/>:</span> <c:out value="${warrant.recieverName}" /><br />
	<span class="text-bold"><spring:message code="warrant.sender"/>:</span> <c:out value="${warrant.senderName}" /><br />	
	<span class="text-bold"><spring:message code="warrant.status"/>:</span> <c:out value="${warrant.localizedStatus}" /><br />
	<span class="text-bold"><spring:message code="warrant.createType"/>:</span> <c:out value="${warrant.localizedType}" /><br />
	<span class="text-bold"><spring:message code="warrant.givenDate"/>:</span> <c:out value="${warrant.givenAt}" /><br />
	<span class="text-bold"><spring:message code="warrant.validTill"/>:</span> <c:out value="${warrant.validTill}" /><br />
	<span class="text-bold"><spring:message code="warrant.replyTill"/>:</span> <c:out value="${warrant.replyTill}" /><br />
	
<%--     <h3><spring:message code="consent.actionRequest"/></h3> --%>
<!--     <table class="request-table"> -->
<%--     	<tr><td class="head"><spring:message code="consent.description"/></td><td class="head"><spring:message code="consent.status"/></td></tr> --%>
<%--     	<c:forEach var="request" items="${warrant.actionRequests}" varStatus="loopStatus"> --%>
<%--         <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}"> --%>
<%--           <td>${request.description}</td> --%>
<%--           <td>${request.status}</td>     --%>
<!--         </tr> -->
<%--       	</c:forEach> --%>
<!--     </table>   -->

	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

