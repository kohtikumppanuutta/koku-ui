<%@ include file="init.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ page import="fi.arcusys.koku.request.KokuRequest" %>
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
    
    iframe.style.height = doc.body.scrollHeight + "px";
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
	window.location="<%= homeURL %>";
}

function exportFile() {	
	window.location = "<%= exportURL %>";
}


</script>
<div id="task-manager-wrap">
	<div id="show-message" style="padding:12px">
	<span class="request-c-1"><c:out value="${request.subject}" /> </span><br />
	<span class="request-c-1">Valid:</span> <c:out value="${request.creationDate}" /> to <c:out value="${request.endDate}" /> <br />
	<iframe id="msgFrame" name="msgFrame" style="width:100%; height:100%" frameborder="0" scrolling="no"></iframe>
	<!--  
	<span class="request-c-1">Questions:</span> <c:forEach var="question" items="${request.questions}" varStatus="status">
        <div><span class="request-c-1">Q"${status.count}":</span> ${question.description}</div>
      </c:forEach>
     --> 
    <h3>Response Summary</h3>
    <table class="request-table">
    	<tr><td rowspan=2 style="vertical-align: middle;" class="head">Respondent</td><c:forEach items="${request.questions}" varStatus="status" ><td colspan=2 class="head">Q${status.count}</td></c:forEach></tr>
    	<tr><c:forEach items="${request.questions}" ><td class="head">Answer</td><td class="head">Comment</td></c:forEach></tr>
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

    <h3>Missed:</h3>
    <c:choose>
    	<c:when test="${fn:length(request.unrespondedList) == 0}">None</c:when>
    	<c:otherwise>
    		<table>
	  			<tr style="font-weight:bold;" ><td>NAME</td></tr>
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

