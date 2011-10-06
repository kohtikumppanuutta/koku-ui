<%@ include file="init.jsp"%>
<%@ page import="fi.arcusys.koku.kv.KokuRequest" %>
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
KokuRequest kokuRequest = (KokuRequest) request.getAttribute("request");
String srcContent = kokuRequest.getContent();
String content = htmlToCode(srcContent);
String requestId = String.valueOf(kokuRequest.getRequestId());
%>

<portlet:resourceURL var="exportURL" id="exportFile">
	<portlet:param name="newRequestId" value="<%= requestId %>" />
</portlet:resourceURL>

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

function exportFile() {	
	var url = "<%= exportURL %>";
	url = formatUrl(url);
	window.location = url;
}


</script>
<div id="task-manager-wrap" class="single">
	<div id="show-message" style="padding:12px">
	<span class="request-c-1"><c:out value="${request.subject}" /> </span><br />
	<span class="request-c-1"><spring:message code="request.createdAt"/>:</span> <c:out value="${request.creationDate}" />  <br />
	<span class="request-c-1"><spring:message code="request.validTill"/>:</span> <c:out value="${request.endDate}" /> <br />
	
	<iframe id="msgFrame" name="msgFrame" style="width:100%;" frameborder="0" scrolling="no"></iframe>
	<!--  
	<span class="request-c-1">Questions:</span> <c:forEach var="question" items="${request.questions}" varStatus="status">
        <div><span class="request-c-1">Q"${status.count}":</span> ${question.description}</div>
      </c:forEach>
     --> 
    <h3><spring:message code="request.responseSummary"/></h3>
    <table class="request-table">
    	<tr><td rowspan=2 style="vertical-align: middle;" class="head"><spring:message code="request.respondent"/></td><c:forEach items="${request.questions}" varStatus="status" ><td colspan=2 class="head"><spring:message code="request.question"/> ${status.count}</td></c:forEach></tr>
    	<tr><c:forEach items="${request.questions}" ><td class="head"><spring:message code="request.answer"/></td><td class="head"><spring:message code="request.comment"/></td></c:forEach></tr>
    	<c:forEach var="response" items="${request.respondedList}" varStatus="loopStatus">
        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
          <td>${response.name}</td>
          <c:forEach var="answer" items="${response.answers}">
          <td>${answer.answer}</td>
          <td>${answer.comment}</td>
          </c:forEach>         
        </tr>
      </c:forEach>
    </table>  

    <h3><spring:message code="request.missed"/>:</h3>
    <c:choose>
    	<c:when test="${fn:length(request.unrespondedList) == 0}"><spring:message code="request.none"/></c:when>
    	<c:otherwise>
    		<table>
	  			<tr style="font-weight:bold;" ><td><spring:message code="request.name"/></td></tr>
      			<c:forEach var="unresponse" items="${request.unrespondedList}">
        		<tr>
          			<td>${unresponse}</td>
        		</tr>
      </c:forEach>
    </table>
  		</c:otherwise>
    </c:choose>	
    <div id="export"><input type="button" value="<spring:message code="request.export"/>" onclick="exportFile()"/></div>
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

