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
		<c:if test="${tipy.recieverName != null}" >
		<span class="text-bold"><spring:message code="tipy.details.reciever" />:</span> <c:out value="${tipy.recieverName}" /><br />
		</c:if>
		<c:if test="${tipy.recieverRoleUid != null}" >
		<span class="text-bold"><spring:message code="tipy.details.reciever" />:</span> <c:out value="${tipy.recieverRoleUid}" /><br />
		</c:if>
		<span class="text-bold"><spring:message code="tipy.details.sender" />:</span> <c:out value="${tipy.senderName}" /><br />
		<span class="text-bold"><spring:message code="tipy.details.requestStatus" />:</span> <c:out value="${tipy.localizedStatus}" /><br />
		<span class="text-bold"><spring:message code="tipy.details.targetPerson" />:</span> <c:out value="${tipy.targetPersonName}" /><br />
		<span class="text-bold"><spring:message code="tipy.details.title" />:</span> <c:out value="${tipy.title}" /><br />
		<span class="text-bold"><spring:message code="tipy.details.legislationinfo" />:</span> <c:out value="${tipy.legislationInfo}" /><br />
		<span class="text-bold"><spring:message code="tipy.details.replyDescription" />:</span> <c:out value="${tipy.replyDescription}" /><br />
		<span class="text-bold"><spring:message code="tipy.details.requestPurpose"/>:</span> <c:out value="${tipy.requestPurpose}" /><br />
		<span class="text-bold"><spring:message code="tipy.details.description"/>:</span> <c:out value="${tipy.description}" /><br />	
		<span class="text-bold"><spring:message code="tipy.details.createdDate"/>:</span> <c:out value="${tipy.createdDate}" /><br />
		<span class="text-bold"><spring:message code="tipy.details.validTill"/>:</span> <c:out value="${tipy.validTill}" /><br />
		<span class="text-bold"><spring:message code="tipy.details.attachmentURL"/>:</span> <c:out value="${tipy.attachmentURL}" /><br />
		<span class="text-bold"><spring:message code="tipy.details.additionalInfo"/>:</span> <c:out value="${tipy.additionalInfo}" /><br />
		<span class="text-bold"><spring:message code="tipy.details.additionalReplyInfo"/>:</span> <c:out value="${tipy.additionalReplyInfo}" /><br />
		<span class="text-bold"><spring:message code="tipy.details.accessType"/>:</span> <c:out value="${tipy.localizedAccessType}" /><br />
		<span class="text-bold"><spring:message code="tipy.details.informationDetails"/>:</span> <c:out value="${tipy.informationDetails}" /><br />
		
	
    <h3><spring:message code="tipy.details.categoryHeader"/></h3>
    <table class="request-table">
    	<tr>
    		<td class="head"><spring:message code="tipy.details.categoryTableHeader"/></td>
    	</tr>
    	<c:forEach var="cat" items="${tipy.categories}" varStatus="loopStatus">
	        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
	          <td>${cat}</td>
	        </tr>
      	</c:forEach>
    </table>  

	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

