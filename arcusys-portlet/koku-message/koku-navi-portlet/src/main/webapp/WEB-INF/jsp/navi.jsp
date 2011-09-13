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
	String defaultPath = "";
	String currentPage = "";
	String actionParam = "";
	
	int pos = naviURL.indexOf("default");
	if(pos > -1) { // for Jboss portal
		defaultPath = naviURL.substring(0, pos+7);
		int pos1 = naviURL.lastIndexOf("/");
		actionParam = naviURL.substring(pos1);
		String currentPath = naviURL.substring(0, pos1);
		int pos2 = currentPath.lastIndexOf("/");
		currentPage = currentPath.substring(pos2+1);		
	}else { // for Gatein portal
		int pos1 = naviURL.indexOf("classic");
		defaultPath = naviURL.substring(0, pos1+7);
		int pos2 = naviURL.indexOf("?");
		String currentPath = naviURL.substring(0, pos2);
		int pos3 = currentPath.lastIndexOf("/");
		currentPage = currentPath.substring(pos3+1);
	}
			
%>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.5.2.min.js"></script>
<script type="text/javascript">

/*
 * Handle action message navigation
 * @Author: Jinhua Chen
 */
 	var koku_navi_type = "${naviType}";
 	var defaultPath = "<%= defaultPath %>";
 	var naviRefreshTimer;
 	
	jQuery(document).ready(function(){
		focusCurrentItem();	
		ajaxUpdate();		
		clearInterval(naviRefreshTimer);
		var duration = 5 * 1000; // 5 seconds
		naviRefreshTimer = setInterval('ajaxUpdate()', duration);		
	});

	/**
	 * Finds the current item in navigation list and decorates the item, e.g. bold font 
	 */
	function focusCurrentItem() {
		var currentPage = "<%= currentPage %>";
		
		if(currentPage == 'Message') {
			var naviType = getNaviType();
			if(naviType != "") {
				var obj = "#" + naviType;
				jQuery(obj).css("font-weight", "bold");
					
			}else jQuery("#msg_inbox").css("font-weight", "bold");
		}else if(currentPage == 'NewMessage')
			jQuery("#msg_new").css("font-weight", "bold");
		else if(currentPage == 'NewRequest')
			jQuery("#req_new").css("font-weight", "bold");
		else if(currentPage == 'ValidRequest')
			jQuery("#req_valid_request").css("font-weight", "bold");
		else if(currentPage == 'NewAppointment')
			jQuery("#app_new").css("font-weight", "bold");
		else if(currentPage == 'NewKindergarten')
			jQuery("#kid_new").css("font-weight", "bold");
		else if(currentPage == 'NewConsent')
			jQuery("#cst_new").css("font-weight", "bold");
		else {
			var obj = "#" + currentPage.toLowerCase();
			jQuery(obj).css("font-weight", "bold");
		}
			
	}
	
	/**
	 * Gets message type from url link
	 */
	function getNaviType() {
		var naviType = koku_navi_type;
		
		var global_url = document.URL;
		var index = global_url.lastIndexOf("NAVI_TYPE=");
		if(index > 0) {
			naviType = global_url.substring(index+10);
			koku_navi_type = naviType;
		}		
		return naviType;
	}
	/**
	 * Execute ajax query in Post way, and parse the Json format response, and
	 * then create tasks in table and task page filed.
	 */
	function ajaxUpdate() {

		var url="<%= ajaxURL %>";
		url = formatUrl(url);
		
		jQuery.ajax({
			  type: 'POST',
			  url: url,
			  global:false,
			  data: {number:"0"},
			  success: function(data) {
					var obj = eval('(' + data + ')');
					var json = obj.response;
					var navi_login_status = json["loginStatus"];
					
					if(navi_login_status == 'VALID') {
						var inbox_num = json["inbox"];
						var archive_inbox_num = json["archive_inbox"];
						updateMessageNum(inbox_num, archive_inbox_num);
					}else {}
			  }
			  
			});		
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
	
	/**
	 * Updates the new message number to indicate user
	 */
	function updateMessageNum(inbox_num, archive_inbox_num) {
		
		if(inbox_num != 0)
			jQuery('#inbox_num').html('(' + inbox_num + ')');
		else
			jQuery('#inbox_num').html("");
		
		if(archive_inbox_num != 0) 
			jQuery('#archive_inbox_num').html('(' + archive_inbox_num + ')');
		else
			jQuery('#archive_inbox_num').html("");
	}
	<%  //for jboss portal
	if(defaultPath.contains("default")) { %>
	function navigateToPage(naviType) {		
		var url = "<%= defaultPath %>" + "/Message" + "<%= actionParam %>" + '&naviType=' + naviType;	
		window.location = url;
	}
	<%}else{ // for gatein portal %>
	function navigateToPage(naviType) {
		var url = "<%= naviRenderURL %>";	
		url = formatUrl(url);
		
		jQuery.post(url, {'newNaviType':naviType}, function(data) {
			var obj = eval('(' + data + ')');
			var json = obj.response;
			var renderUrl = json["renderUrl"];
			var temp = renderUrl.split("?");
			var naviUrl = "<%= defaultPath %>" + "/Message?" + temp[1] + "&NAVI_TYPE=" + naviType;
			window.location = naviUrl;
		});		
	}
	<%}%>
	/**
	 * Shows/hides search user interface
	 */
	function showArchiveUI() {

		jQuery('#archive-part').toggle('fast');
	}

</script>


<div id="koku-navigation">
	<a href="#"><img src="<%= request.getContextPath() %>/images/kklogo.jpg" width="189"
		height="59" />
	</a>
	<ul class="main">
	
		<!--  VIESTIT -->
		<li><a href="javascript:void(0)" >Etusivu</a></li>
		<!-- For citizen in Gatein portal-->
		<c:if test="${fn:contains(naviURL, '/classic/')}">
		<li id="kks"><a href="/portal/private/classic/KKS">Sopimukset ja suunnitelmat</a>
		<li id="pyh"><a href="/portal/private/classic/PYH" >Omat tiedot</a></li>
		<li id="lok"><a href="/portal/private/classic/LOK" >Lokihallinta</a></li>
		</c:if>
		<!-- For employee in Jboss portal -->
		<c:if test="${fn:contains(naviURL, '/default/')}">
		<li id="kks"><a href="/portal/auth/portal/default/KKS">Sopimukset ja suunnitelmat</a>
		<li id="pyh"><a href="/portal/auth/portal/default/PYH" >Omat tiedot</a></li>
		<li id="lok"><a href="/portal/auth/portal/default/LOK" >Lokihallinta</a></li>
		</c:if>
		
		<li><a href="javascript:void(0)" onclick="navigateToPage('msg_inbox')" >Viestit</a>
			<ul class="child">
				<li id="msg_new"><a href="<%= defaultPath %>/Message/NewMessage">Uusi viesti</a> </li>
				<li id="msg_inbox"><a href="javascript:void(0)" onclick="navigateToPage('msg_inbox')">Saapuneet <span id="inbox_num" class="message_num"></span></a></li>
				<li id="msg_outbox"><a href="javascript:void(0)" onclick="navigateToPage('msg_outbox')">L�hetetyt</a></li>
				<li><a href="javascript:void(0)" onclick="showArchiveUI()">Arkistoidut</a>
					<ul id="archive-part">
						<li id="msg_archive_inbox"><a href="javascript:void(0)" onclick="navigateToPage('msg_archive_inbox')">Saapuneet <span id="archive_inbox_num" class="message_num"></span></a></li>
						<li id="msg_archive_outbox"><a href="javascript:void(0)" onclick="navigateToPage('msg_archive_outbox')">L�hetetyt</a></li>
					</ul>
				</li>
			</ul>
		</li>
			
			
		<!--  PYYNN�T -->
		<!-- For citizen in Gatein portal-->
		<c:if test="${fn:contains(naviURL, '/classic/')}">
		<li><a href="#">Pyynn�t</a>
			<ul class="child">
				<li id="req_valid_request"><a href="<%= defaultPath %>/Message/ValidRequest">Saapuneet</a></li>
				<li><a href="#">Vastatut</a></li>
				<li><a href="#">Vanhat</a></li>
			</ul></li>
		</c:if>
		<!-- For employee in Jboss portal -->
		<c:if test="${fn:contains(naviURL, '/default/')}">
		<li><a href="#">Pyynn�t</a>
			<ul class="child">
				<li id="req_new"><a href="<%= defaultPath %>/Message/NewRequest">Uusi pyynt�</a></li>
				<li id="luopohja"><a href="<%= defaultPath %>/Message/LuoPohja">Luo uusi pohja</a></li>
				<li>L�hetetyt
					<ul class="child">
						<li id="req_valid"><a href="javascript:void(0)" onclick="navigateToPage('req_valid')">Avoimet</a></li>
						<li><a href="#">Valmiit</a></li>
					</ul>
				</li>
			</ul>
		</li>
		</c:if>
		
		<!-- TAPAAMISET -->
		<!-- For citizen in Gatein portal-->
		<c:if test="${fn:contains(naviURL, '/classic/')}">
		<li><a href="#">Tapaamiset</a>
			<ul class="child">
				<li id="app_inbox_citizen"><a href="javascript:void(0)" onclick="navigateToPage('app_inbox_citizen')">Vastausta odottavat</a></li>
				<li id="app_response_citizen"><a href="javascript:void(0)" onclick="navigateToPage('app_response_citizen')">Vastatut</a></li>
				<li id="app_response_old_citizen"><a href="javascript:void(0)" onclick="navigateToPage('app_response_citizen')">Vanhat</a></li>
				
			</ul>
		</li>
		</c:if>
		<!-- For employee in Jboss portal-->
		<c:if test="${fn:contains(naviURL, '/default/')}">
		<li><a href="#">Tapaamiset</a>
			<ul class="child">
				<li id="app_new"><a href="<%= defaultPath %>/Message/NewAppointment">Uusi tapaaminen</a></li>
				<li id="app_inbox_employee"><a href="javascript:void(0)" onclick="navigateToPage('app_inbox_employee')">Avoimet</a></li>
				<li id="app_response_employee"><a href="javascript:void(0)" onclick="navigateToPage('app_response_employee')">Valmiit</a></li>
			</ul></li>
		</c:if>			
		
		<!--  SUOSTUMUKSET -->
		<!-- For citizen in Gatein portal-->
		<c:if test="${fn:contains(naviURL, '/classic/')}">
			<li><a href="#">Suostumukset</a>
				<ul class="child">
					<li id="cst_assigned_citizen"><a href="javascript:void(0)" onclick="navigateToPage('cst_assigned_citizen')">Saapuneet pyynn�t</a></li>
					<li id="cst_own_citizen"><a href="javascript:void(0)" onclick="navigateToPage('cst_own_citizen')">Vastatut</a></li>
					<li id="cst_own_citizen_old"><a href="javascript:void(0)" onclick="navigateToPage('cst_own_citizen')">Vanhat</a></li>					
				</ul>
			</li>
				
			<li><a href="#">Valtakirjat</a>
				<ul class="child">
					<li id="valtakirjaconsent"><a href="<%= defaultPath %>/Message/ValtakirjaConsent">Anna valtakirja</a></li>
					<li id="selaavaltakirjoja"><a href="#">Omat valtakirjat</a>
						<ul class="child">
							<li id="selaaOmiaValtakirjoja"><a href="<%= defaultPath %>/Message/SelaaValtakirjoja">Valtuuttajana</a></li>
							<li id="selaaOmiaValtakirjoja"><a href="<%= defaultPath %>/Message/SelaaValtakirjoja">Valtuutettuna</a></li>
						</ul>
					</li>
				</ul>
			</li>
		</c:if>
		<!-- For employee in Jboss portal-->
		<c:if test="${fn:contains(naviURL, '/default/')}">
		<li><a href="#">Suostumukset</a>
			<ul class="child">
				<li id="cst_new"><a href="<%= defaultPath %>/Message/NewConsent">Uusi suostumuspohja</a></li>
				<li id="sendconsent"><a href="<%= defaultPath %>/Message/SendConsent">Uusi suostumuspyynt�</a></li>
				<li id="cst_own_employee"><a href="javascript:void(0)" onclick="navigateToPage('cst_own_employee')">L�hetetyt suostumuspyynn�t</a></li>
			</ul>
		</li>
		<li><a href="#">Valtakirjat</a>
			<ul class="child">
				<%-- <li id="valtakirjaconsent"><a href="<%= defaultPath %>/Message/ValtakirjaConsent">Asiakkaan valtakirjat </a></li>  --%>
				<li id="cst_browse_customer_consents"><a href="javascript:void(0)" onclick="navigateToPage('cst_browse_customer_consents')">Asiakkaan valtakirjat </a></li> 
				<li id="selaavaltakirjoja"><a href="<%= defaultPath %>/Message/SelaaValtakirjoja">Asian valtakirjat</a></li>
			</ul>
		</li>
			
		<!--  TIETOPYYNN�T -->
		<li><a href="javascript:void(0)">Tietopyynn�t</a>
			<ul class="child">
				<li id="newinformation"><a href="<%= defaultPath %>/Message/NewInformation">Uusi tietopyynt�</a></li>
				<li id="informationbox"><a href="<%= defaultPath %>/Message/InformationBox">Saapuneet</a></li>
				<li id="ilmoitukset"><a href="<%= defaultPath %>/Message/Ilmoitukset">L�hetetyt</a>
					<ul class="child">
						<li id="cst_new"><a href="<%= defaultPath %>/Message/Ilmoitukset">Avoimet</a></li>
						<li id="sendconsent"><a href="<%= defaultPath %>/Message/Ilmoitukset">Valmiit</a></li>
					</ul>
				</li>
			</ul>
		</li>	
		</c:if>	
		<!--  show only citizens -->
		<c:if test="${fn:contains(naviURL, '/classic/')}">
		<li><a href="#">Asiointipalvelut</a>
			<ul class="child">
				<li><a href="#">Palveluhakemukset</a>
					<ul class="child">
						<li id="kid_new"><a href="<%= defaultPath %>/Message/NewKindergarten">P�iv�hoitohakemus</a></li>				
					</ul>
				</li>
				<li><a href="#">Voimassaolevat palvelut</a></li>
				<li><a href="#">Ajanvaraustiedot</a></li>
			</ul>
		</li>
		</c:if>	
		<li><a href="#">Ohjeet</a></li>
	</ul>
</div>