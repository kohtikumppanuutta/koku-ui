<%@ include file="init.jsp"%>
<%@ page import="fi.arcusys.koku.message.Message" %>
<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>
<%!

public String htmlToCode_old(String s)
{
	if(s == null) {
		return "";
	} else {
		s = s.replace("\n\n", "");
		s = s.replace("\r\n", "");
		s = s.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		s = s.replace(" ", "&nbsp;");
		s = s.replace("<", "&lt;");
		s = s.replace(">", "&gt;");
		s = s.replace("&", "&amp;");
		
		return s;
	}
}
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
Message message = (Message) request.getAttribute("message");
String srcContent = message.getContent();
String content = htmlToCode(srcContent);

%>
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
    
    if(iframe.contentDocument) {
    	iframeHeight = iframe.contentDocument.documentElement.scrollHeight+10; //FF 3.0.11, Opera 9.63, and Chrome
    } else {
    	iframeHeight = iframe.contentWindow.document.body.scrollHeight+10; //IE6, IE7 and Chrome
    }
    
    iframe.style.height = iframeHeight + "px";
}
/**
 * Returns to the main portlet page
 */
function returnMainPage() {
	window.location="<%= homeURL %>";
}

</script>
<div id="task-manager-wrap" class="single">
	<div id="show-message" style="padding:12px">
	<!-- 
		<h3>
		<% if(messageType.equals("1")) { %> From </h3> <c:out value="${message.sender}" /> <% }  else if(messageType.equals("2")) { %> To
		<% } else { %> To </h3> <c:out value="${message.recipients}" /> <% } %>
		
		<h3>Subject</h3>
		<c:out value="${message.subject}" />	
		<h3>Content</h3>
 	-->
		<iframe id="msgFrame" name="msgFrame" style="width:100%;" frameborder="0" scrolling="no"></iframe>
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

