<%@ page import="net.sf.json.JSONArray"%>
<%@ include file="init.jsp"%>
<%@ page import="fi.arcusys.koku.kv.model.Message" %>
<%@ page import="fi.arcusys.koku.users.KokuUser" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

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
	List<KokuUser> missingUsers = message.getDeliveryFailedTo();
	List<String> missingUserNames = new ArrayList<String>();
	if (!missingUsers.isEmpty()) {
		for (KokuUser user : missingUsers) {
			missingUserNames.add(user.getFullName());
		}
	}	
	JSONArray usernameArray = JSONArray.fromObject(missingUserNames);
	
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
		var missingUsers = <%= usernameArray.toString() %>;
		
		if (missingUsers.length >= 2) {
			var persons = "";
			for(var i in missingUsers) {
				persons += missingUsers[i] + ", ";
			}
			persons = persons.substring(0, persons.length-2);
			jQuery('#failedEmailDelivery').html("Huom! Viestiä ei voitu välittää sähköpostitse seuraaville henkilöille: "+persons);
		} else if (missingUsers.length >= 1) {
			jQuery('#failedEmailDelivery').html("Huom! Viestiä ei voitu välittää sähköpostitse seuraavalle henkilölle:  " + missingUsers[0]);
		}
	}
	
	
	var KokuMessage = {
			
		portal: "<%= naviPortalMode %>",
		
		common : {
			redirectToAppointmentCancelled : function() {
				if (KokuMessage.portal == '<%= Constants.PORTAL_MODE_KUNPO %>') {
					KokuMessage.citizen.redirectToAppointmentsOld();
				} else if (KokuMessage.portal == '<%= Constants.PORTAL_MODE_LOORA %>') {
					KokuMessage.employee.redirectToAppointmentsReady();
				}
			}
		},
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
			},
			redirectToSentConsents: function() {
				window.location = KokuMessage.util.formatUrl("<%= homeURL %>&NAVI_TYPE=<%= Constants.TASK_TYPE_CONSENT_EMPLOYEE_CONSENTS %>");
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
		KokuMessage.citizen.redirectToRequestsRecieved();
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
	<div id="failedEmailDelivery"></div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="KokuMessage.util.returnMainPage()" />
	</div>
</div>

