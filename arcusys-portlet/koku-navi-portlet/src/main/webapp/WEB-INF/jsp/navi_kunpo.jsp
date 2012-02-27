<%@ include file="init.jsp" %>

<portlet:resourceURL var="ajaxURL" id="update">
</portlet:resourceURL>

<portlet:renderURL var="naviURL" >
	<portlet:param name="myaction" value="showNavi" />
</portlet:renderURL>

<portlet:resourceURL var="naviRenderURL" id="createNaviRenderUrl">
</portlet:resourceURL>

<%
	/* Parses the parent path url from the portlet renderURL */
	String currentPage = "";
	String actionParam = "";	
	
	int pos2 = naviURL.indexOf("?");
	String currentPath = naviURL.substring(0, pos2);
	int pos3 = currentPath.lastIndexOf("/");
	currentPage = currentPath.substring(pos3+1);
	
	
// 	System.out.println("------------------------------------------------------------------------------------------------------");
// 	System.out.println("defaultPath: '"+defaultPath+"' currentPage: '"+currentPage+"' actionParam: '"+actionParam+"' \n NaviURL: '"+naviURL+"'");
// 	System.out.println("NAVI MODE: "+naviPortalMode);
// 	System.out.println("portalInfo: "+portalInfo);
// 	System.out.println("------------------------------------------------------------------------------------------------------");
			
%>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.5.2.min.js"></script>
<script type="text/javascript">

/*
 * Handle action message navigation
 * @Author: Jinhua Chen
 */
 	var koku_navi_type = "<%= navigationType %>";
 	var defaultPath = "<%= defaultPath %>";
 	var naviRefreshTimer;
 	
	jQuery(document).ready(function(){
		focusCurrentItem();	
		ajaxUpdate();		
		clearInterval(naviRefreshTimer);
	});

	<%@ include file="js_navi.jspf" %>
</script>

<div id="koku-navigation">
	<a href="#"><img src="<%= request.getContextPath() %>/images/kklogo.jpg" width="189" height="59" />
	</a>
	<ul class="main">
		
<%
	String kksPath = kksPref; 
	String lokPath = lokPref; 
	String pyhPath = pyhPref;
%>		
		
		<!--  VIESTIT -->
		<li><a href="<%= frontPagePath %>">Etusivu</a></li>
		<li id="kks"><a href="<%= kksPath %>">Sopimukset ja suunnitelmat</a>
		<li id="pyh"><a href="<%= pyhPath %>">Omat tiedot</a></li>
		
		
		<li><span class="naviLinkHeaderNonLink">Viestit</span>
			<ul class="child">
				<li id="msg_inbox"><a href="javascript:void(0)" onclick="navigateToPage('msg_inbox')">Saapuneet</a><span id="inbox_num" class="message_num"></span></li>
				<li id="msg_outbox"><a href="javascript:void(0)" onclick="navigateToPage('msg_outbox')">Lähetetyt</a></li>
				<li><a href="javascript:void(0)" onclick="showArchiveUI()">Arkistoidut</a>
					<ul id="archive-part">
						<li id="msg_archive_inbox"><a href="javascript:void(0)" onclick="navigateToPage('msg_archive_inbox')">Saapuneet</span></a><span id="archive_inbox_num" class="message_num"></span></li>
						<li id="msg_archive_outbox"><a href="javascript:void(0)" onclick="navigateToPage('msg_archive_outbox')">Lähetetyt</a></li>
					</ul>
				</li>
			</ul>
		</li>
			
		<!--  PYYNNÖT -->
		<li><span class="naviLinkHeaderNonLink">Pyynnöt</span>
			<ul class="child">
				<li id="req_valid_request"><a href="<%= defaultPath %><%= NavigationPortletProperties.REQUESTS_RECIEVED_REQUESTS %>">Saapuneet</a><span id="requests_num" class="message_num"></span></li>
				<li id="<%=Constants.TASK_TYPE_REQUEST_REPLIED %>"><a href="javascript:void(0)" onclick="navigateToPage('<%=Constants.TASK_TYPE_REQUEST_REPLIED %>')">Vastatut</a></li>
				<li id="<%=Constants.TASK_TYPE_REQUEST_OLD %>"><a href="javascript:void(0)" onclick="navigateToPage('<%=Constants.TASK_TYPE_REQUEST_OLD %>')">Vanhat</a></li>
			</ul>
		</li>
		
		<!-- TAPAAMISET -->
		<li><span class="naviLinkHeaderNonLink">Tapaamiset</span>
			<ul class="child">
				<li id="<%= Constants.TASK_TYPE_APPOINTMENT_INBOX_CITIZEN%>"><a href="javascript:void(0)" onclick="navigateToPage('<%= Constants.TASK_TYPE_APPOINTMENT_INBOX_CITIZEN%>')">Vastausta odottavat</a><span id="appointments_num" class="message_num"></span></li>
				<li id="<%= Constants.TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN%>"><a href="javascript:void(0)" onclick="navigateToPage('<%= Constants.TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN%>')">Vastatut</a></li>
				<li id="<%= Constants.TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN_OLD%>"><a href="javascript:void(0)" onclick="navigateToPage('<%= Constants.TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN_OLD%>')">Vanhat</a></li>
			</ul>
		</li>
		
		<!--  SUOSTUMUKSET -->
			<li><span class="naviLinkHeaderNonLink">Suostumukset</span>
				<ul class="child">
					<li id="<%= Constants.TASK_TYPE_CONSENT_ASSIGNED_CITIZEN%>"><a href="javascript:void(0)" onclick="navigateToPage('<%= Constants.TASK_TYPE_CONSENT_ASSIGNED_CITIZEN%>')">Saapuneet</a><span id="consents_num" class="message_num"></span></li>
					<li id="<%= Constants.TASK_TYPE_CONSENT_CITIZEN_CONSENTS%>"><a href="javascript:void(0)" onclick="navigateToPage('<%= Constants.TASK_TYPE_CONSENT_CITIZEN_CONSENTS%>')">Vastatut</a></li>
					<li id="<%= Constants.TASK_TYPE_CONSENT_CITIZEN_CONSENTS_OLD%>"><a href="javascript:void(0)" onclick="navigateToPage('<%= Constants.TASK_TYPE_CONSENT_CITIZEN_CONSENTS_OLD%>')">Vanhat</a></li>					
				</ul>
			</li>
				
			<li><span class="naviLinkHeaderNonLink">Valtakirjat</span>
				<ul class="child">
					<li id="valtakirjaconsent"><a href="<%= defaultPath %><%= NavigationPortletProperties.WARRANTS_NEW_WARRANT %>">Anna valtakirja</a></li>
					<li id="<%= Constants.TASK_TYPE_WARRANT_BROWSE_RECEIEVED%>"><a href="javascript:void(0)" onclick="navigateToPage('<%= Constants.TASK_TYPE_WARRANT_BROWSE_RECEIEVED%>')">Vastaanotetut</a></li>
					<li><a href="#">Omat valtakirjat</a>
						<ul class="child">
							<li id="<%= Constants.TASK_TYPE_WARRANT_BROWSE_SENT%>"><a href="javascript:void(0)" onclick="navigateToPage('<%= Constants.TASK_TYPE_WARRANT_BROWSE_SENT%>')">Valtuuttajana</a></li>
							<li id="selaaOmiaValtakirjoja"><a href="<%= defaultPath %><%= NavigationPortletProperties.WARRANTS_BROWSE_WARRANTS %>">Valtuutettuna</a></li>
						</ul>
					</li>
				</ul>
			</li>

		
		<li><span class="naviLinkHeaderNonLink">Asiointipalvelut</span>
			<ul class="child">
				<li><span class="naviLinkHeaderNonLink">Palveluhakemukset</span>
					<ul class="child">
						<li id="kid_new"><a href="<%= defaultPath %><%= NavigationPortletProperties.APPLICATIONS_NEW_KINDERGARTEN %>">Päivähoitohakemus</a></li>
						<li id="applicationsConfirm"><a href="<%= defaultPath %><%= NavigationPortletProperties.APPLICATIONS_NEED_TO_CONFIRM %>">Hakemusten vahvistuspyynnöt</a></li>
					</ul>
				</li>
				<li><a href="#">Voimassaolevat palvelut</a></li>
				<li><a href="#">Ajanvaraustiedot</a></li>
			</ul>
		</li>
		<li><a href="<%= helpLinkPath %>">Ohjeet</a></li>
	</ul>
</div>
