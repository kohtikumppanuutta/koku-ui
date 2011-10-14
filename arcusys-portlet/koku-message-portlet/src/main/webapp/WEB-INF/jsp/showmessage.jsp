<%@ include file="init.jsp"%>
<%@ page import="fi.arcusys.koku.kv.model.Message" %>
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
		s = s.replace("'", "&rsquo;");
		
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
		s = s.replace("&", "&amp;");
		s = s.replace("'", "&rsquo;");
		return s;
	}
} 

%>

<%
Message message = (Message) request.getAttribute("message");
String srcContent = message.getContent();
String sender = message.getSender();
String recipients = message.getRecipients();
String subject = message.getSubject();
String type = message.getMessageType();
String content = htmlToCode(srcContent);
Boolean isHtml = false;
if (srcContent != null) {
	isHtml = srcContent.startsWith("<html");	
}

%>
<script type="text/javascript"> 


window.onload = function() {
	
	
	jQuery('#messageContent').html('<%= content %>');
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

</script>
<div id="task-manager-wrap" class="single">	
	<div id="messageContent"></div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

