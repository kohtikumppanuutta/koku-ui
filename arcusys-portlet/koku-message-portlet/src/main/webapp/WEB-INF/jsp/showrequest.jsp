<%@ include file="init.jsp"%>
<%@ page import="fi.arcusys.koku.kv.model.KokuRequest" %>
<%@ page import="fi.arcusys.koku.web.util.ModelWrapper" %>


<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>

<%!

public String htmlToCode(String s)
{
	if(s == null) {
		return "";
	} else {
		s = s.replace("\n\n", "");
		s = s.replace("\r\n", "");
		s = s.replace("\n", "");
		return s;
	}
} 

%>

<%
	ModelWrapper<KokuRequest> model = (ModelWrapper<KokuRequest>) request.getAttribute("request");
	KokuRequest kokuRequest = model.getModel();
	String requestId = "";
	String srcContent = "";
	String content = "";
	if (kokuRequest != null) {
		srcContent = kokuRequest.getContent();
		content = htmlToCode(srcContent);
		requestId = String.valueOf(kokuRequest.getRequestId());
	}
%>

<portlet:resourceURL var="exportURL" id="exportFile">
	<portlet:param name="newRequestId" value="<%= requestId %>" />
</portlet:resourceURL>

<%@ include file="js_koku_detail.jspf" %>

<c:choose> 
  <c:when test="${request.responseStatus == 'FAIL'}" > 
  	<script type="text/javascript"> 
  			kokuErrorMsg += "<span class=\"failureUuid\"><c:out value="${request.errorCode}" /></span></div>";
			jQuery.jGrowl(kokuErrorMsg, kokuErrorMsgOptions);
	</script>
  </c:when> 

  <c:when test="${request.responseStatus == 'OK'}" >	   


	<script type="text/javascript"> 
	
	window.onload = function() {
		var content = '<%= content %>';
		var iframe = document.getElementById('msgFrame');
		var doc = iframe.document;
		
		if(iframe.contentDocument)
	        doc = iframe.contentDocument; // For NS6
	    else if(iframe.contentWindow)
	        doc = iframe.contentWindow.document; // For IE5.5 and IE6
	        	
	    // Put the content in the iframe
	    doc.open();
	    doc.writeln(content);
	    doc.close(); 
	    var iframeHeight;
	    
	    // if(iframe.contentDocument) {
	    if(jQuery.browser.msie) {
	    	// FIXME: QUICK FIX
	    	var body = iframe.contentWindow.document.body;
	    	iframeHeight = body.scrollHeight + body.clientHeight; //IE6, IE7
	    } else if (jQuery.browser.webkit) {
	    	iframeHeight = iframe.contentWindow.document.body.scrollHeight+10;  // Chrome
	    } else {
	    	iframeHeight = iframe.contentDocument.documentElement.scrollHeight+10; //FF 3.0.11, Opera 9.63, and Chrome
	    }
	    
	    iframe.style.height = iframeHeight + "px";
	
	}
	
	function htmlToCode(s) {
		s = s.replace("\n\n", "");
		s = s.replace("\r\n", "");
		s = s.replace("\n", "");
		return s;
	}
	
	function exportFile() {	
		var url = "<%= exportURL %>";
		url = formatUrl(url);
		window.location = url;
	}
	
	
	</script>
	<div id="task-manager-wrap" class="single">
		<div id="show-message" style="padding:12px">
		<span class="request-c-1"><c:out value="${request.model.subject}" /> </span><br />
		<span class="request-c-1"><spring:message code="request.createdAt"/>:</span> <c:out value="${request.model.creationDate}" />  <br />
		<span class="request-c-1"><spring:message code="request.validTill"/>:</span> <c:out value="${request.model.endDate}" /> <br />
		
		<%--  Why iframe here?? --%>
		<iframe id="msgFrame" name="msgFrame" style="width:100%;" frameborder="0" scrolling="no"></iframe>
		
	    <h3><spring:message code="request.responseSummary"/></h3>
	    <table class="request-table">
	    	<tr>
	    		<td rowspan=2 style="vertical-align: middle;" class="head"><spring:message code="request.respondent"/></td>
	    		<c:forEach items="${request.model.questions}" varStatus="status" var="question" >
	    			<td  class="head"><c:out value="${question.description}"/></td>
	    		</c:forEach>
	    		<td rowspan=2 style="vertical-align: middle;" class="head"><spring:message code="request.comment"/></td>
	   		</tr>    		
	    	<tr>
	    	<c:forEach items="${request.model.questions}" >
	    		<td class="head"><spring:message code="request.answer"/></td>
	    	</c:forEach>
	    	</tr>
	    	<c:forEach var="response" items="${request.model.respondedList}" varStatus="loopStatus">
	        <tr class="<c:out value="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}"/>">
	          <td><c:out value="${response.replierUser.fullName}"/></td>
	          <c:forEach var="answer" items="${response.answers}">
	          <td><c:out value="${answer.answer}"/></td>
	          </c:forEach>
	          <td><c:out value="${response.comment}"/></td>
	        </tr>
	      </c:forEach>
	    </table>  
	
	    <h3><spring:message code="request.missed"/>:</h3>
	    <c:choose>
	    	<c:when test="${fn:length(request.model.unrespondedList) == 0}"><spring:message code="request.none"/></c:when>
	    	<c:otherwise>
	    		<table>
		  			<tr style="font-weight:bold;" ><td><spring:message code="request.name"/></td></tr>
	      			<c:forEach var="unresponse" items="${request.model.unrespondedList}">
	        		<tr>
	          			<td><c:out value="${unresponse.fullName}"/></td>
	        		</tr>
	      </c:forEach>
	    </table>
	  		</c:otherwise>
	    </c:choose>	
	    <div id="export"><input type="button" value="<spring:message code="request.export"/>" onclick="exportFile()"/></div>
		</div>
	</div>
	</c:when> 
</c:choose>
	
		
	<div id="task-manager-operation" class="task-manager-operation-part">
			<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>

