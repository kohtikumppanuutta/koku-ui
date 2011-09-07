<%@ include file="init.jsp"%>
<%@ page import="fi.arcusys.koku.kv.Message" %>
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
String content = htmlToCode(srcContent);

%>
<script type="text/javascript"> 

window.onload = function() {
	try{
		var content = '<%= content %>';
	}catch(error){
		var content = error.description;
	}
	
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
		<iframe id="msgFrame" name="msgFrame" style="width:100%;" frameborder="0" scrolling="no"></iframe>
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

