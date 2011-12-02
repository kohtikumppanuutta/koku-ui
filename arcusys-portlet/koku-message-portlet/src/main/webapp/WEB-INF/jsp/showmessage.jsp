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
		var content = '<%= content %>';
		content = content.replace(/&rsquo;/g, "'");
		jQuery('#messageContent').html(content);
	}
	
	
	var KokuMessage = {			
		citizen : {
			redirectToRequestsRecieved: function () {
				window.location = "<%= NavigationPortletProperties.NAVIGATION_PORTLET_PATH %><%= NavigationPortletProperties.REQUESTS_RECIEVED_REQUESTS %>";
			},
			redirectToRequestsReplied: function() {
				window.location = KokuMessage.util.formatUrl("<%= homeURL %>&NAVI_TYPE=<%= Constants.TASK_TYPE_REQUEST_REPLIED %>");
			},
			redirectToAppointmentsRecieved: function() {
				window.location = KokuMessage.util.formatUrl("<%= homeURL %>&NAVI_TYPE=<%= Constants.TASK_TYPE_APPOINTMENT_INBOX_CITIZEN %>");
			},
			redirectToAppointmentsOld: function() {
				window.location = KokuMessage.util.formatUrl("<%= homeURL %>&NAVI_TYPE=<%= Constants.TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN_OLD %>");
			},
			redirectToConsentsRecieved: function() {
				window.location = KokuMessage.util.formatUrl("<%= homeURL %>&NAVI_TYPE=<%= Constants.TASK_TYPE_CONSENT_ASSIGNED_CITIZEN %>");
			}
		},
		
		employee: {			
			redirectToRequestOpen: function() {
				window.location = KokuMessage.util.formatUrl("<%= homeURL %>&NAVI_TYPE=<%= Constants.TASK_TYPE_REQUEST_VALID_EMPLOYEE %>");
			},
			redirectToRequestReady: function() {
				window.location = KokuMessage.util.formatUrl("<%= homeURL %>&NAVI_TYPE=<%= Constants.TASK_TYPE_REQUEST_DONE_EMPLOYEE %>");
			},			
			redirectToAppointmentsOpen: function() {
				window.location = KokuMessage.util.formatUrl("<%= homeURL %>&NAVI_TYPE=<%= Constants.TASK_TYPE_APPOINTMENT_INBOX_EMPLOYEE %>");
			},
			redirectToAppointmentsReady: function() {
				window.location = KokuMessage.util.formatUrl("<%= homeURL %>&NAVI_TYPE=<%= Constants.TASK_TYPE_APPOINTMENT_RESPONSE_EMPLOYEE %>");
			},
			redirectToInfoRequestsRecieved: function() {
				window.location = "<%= NavigationPortletProperties.NAVIGATION_PORTLET_PATH %><%= NavigationPortletProperties.INFO_REQ_RECIEVED_INFO_REQS %>";
			},
			redirectToBrowseWarrants: function() {
				window.location = "<%= NavigationPortletProperties.NAVIGATION_PORTLET_PATH %><%= NavigationPortletProperties.WARRANTS_BROWSE_WARRANTS %>";
			},
			redirectToKindergartenApplicationBrowsing: function() {
				window.location = KokuMessage.util.formatUrl("<%= homeURL %>&NAVI_TYPE=<%= Constants.TASK_TYPE_APPLICATION_KINDERGARTEN_BROWSE %>");
			},
			redirectToKindergartenApplication: function(formId) {
				window.location = "<%= NavigationPortletProperties.NAVIGATION_PORTLET_PATH %>/EditKindergarten?FormID="+formId;
			}
		},
			
		util : {			
			returnToMainPage: function() {			
				window.location = KokuMessage.formatUrl("<%= homeURL %>");
			},
			formatUrl: function(url) {
				var newUrl;
				newUrl = url.replace(/&quot;/g,'"');
				newUrl = newUrl.replace(/&amp;/g,"&");
				newUrl = newUrl.replace(/&lt;/g,"<");
				newUrl =  newUrl.replace(/&gt;/g,">");			
				return newUrl;
			},			
			/**
			 * Returns to the main portlet page
			 */
			returnMainPage: function() {
				var url = "<%= homeURL %>";
				url = formatUrl(url);
				window.location = url;
			}
		}
	};	
	
	/**
	 * @Deprecated Please use KokuMessage.citizen.redirectToRecievedRequests() method
	 */
	function kokuRedirectToRecievedRequests() {
		KokuMessage.citizen.redirectToRecievedRequests();
	}
	
	/**
	 * @Deprecated Please use KokuMessage.util.returnToMainPage() method
	 */
	function returnToMainPage() {
		KokuMessage.util.returnToMainPage();
	}

</script>
<div id="task-manager-wrap" class="single">	
	<div id="messageContent"></div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="KokuMessage.util.returnMainPage()" />
	</div>
</div>

