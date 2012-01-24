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

<c:choose> 
  <c:when test="${tipy.responseStatus == 'FAIL'}" > 
  	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jgrowl_minimized.js"></script>
  	<script type="text/javascript"> 
  		var options = { 
				sticky: true,
				theme : "serverErrorWarningMsg",
				header: "Oops!",
				closer: true,
				position: "center"	
			};
			var msg = "<div class=\"serverErrorMsg\"><spring:message code="notification.serverError.msg" /></div>";
			msg += "<div class=\"serverErrorUuidMsg\"><spring:message code="notification.serverError.uuidMsg" />";
			msg += "<span class=\"failureUuid\">${tipy.errorCode}</span></div>";
			jQuery.jGrowl(msg, options);
	</script>
  </c:when> 
  
  <c:when test="${tipy.responseStatus == 'OK'}" >	   
	<div id="task-manager-wrap" class="single">
		<div id="show-message" style="padding:12px">
			<span class="text-bold"><spring:message code="tipy.details.reciever" />:</span> <c:out value="${tipy.model.recieverName}" /><br />
			<span class="text-bold"><spring:message code="tipy.details.sender" />:</span> <c:out value="${tipy.model.senderName}" /><br />
			<span class="text-bold"><spring:message code="tipy.details.requestStatus" />:</span> <c:out value="${tipy.model.localizedStatus}" /><br />
			<span class="text-bold"><spring:message code="tipy.details.targetPerson" />:</span> <c:out value="${tipy.model.targetPersonName}" /><br />
			<span class="text-bold"><spring:message code="tipy.details.title" />:</span> <c:out value="${tipy.model.title}" /><br />
			<span class="text-bold"><spring:message code="tipy.details.legislationinfo" />:</span> <c:out value="${tipy.model.legislationInfo}" /><br />
			<span class="text-bold"><spring:message code="tipy.details.replyDescription" />:</span> <c:out value="${tipy.model.replyDescription}" /><br />
			<span class="text-bold"><spring:message code="tipy.details.requestPurpose"/>:</span> <c:out value="${tipy.model.requestPurpose}" /><br />
			<span class="text-bold"><spring:message code="tipy.details.description"/>:</span> <c:out value="${tipy.model.description}" /><br />	
			<span class="text-bold"><spring:message code="tipy.details.createdDate"/>:</span> <c:out value="${tipy.model.createdDate}" /><br />
			<span class="text-bold"><spring:message code="tipy.details.validTill"/>:</span> <c:out value="${tipy.model.validTill}" /><br />
			<span class="text-bold"><spring:message code="tipy.details.attachmentURL"/>:</span> <c:out value="${tipy.model.attachmentURL}" /><br />
			<span class="text-bold"><spring:message code="tipy.details.additionalInfo"/>:</span> <c:out value="${tipy.model.additionalInfo}" /><br />
			<span class="text-bold"><spring:message code="tipy.details.additionalReplyInfo"/>:</span> <c:out value="${tipy.model.additionalReplyInfo}" /><br />
			<span class="text-bold"><spring:message code="tipy.details.accessType"/>:</span> <c:out value="${tipy.model.localizedAccessType}" /><br />
			<span class="text-bold"><spring:message code="tipy.details.informationDetails"/>:</span> <c:out value="${tipy.model.informationDetails}" /><br />
			
		
	    <h3><spring:message code="tipy.details.categoryHeader"/></h3>
	    <table class="request-table">
	    	<tr>
	    		<td class="head"><spring:message code="tipy.details.categoryTableHeader"/></td>
	    	</tr>
	    	<c:forEach var="cat" items="${tipy.model.categories}" varStatus="loopStatus">
		        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
		          <td>${cat}</td>
		        </tr>
	      	</c:forEach>
	    </table>  
	  </c:when> 
	</c:choose>

	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

