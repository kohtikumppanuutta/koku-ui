<%@ include file="init.jsp" %>

<portlet:resourceURL var="ajaxURL" id="update">
</portlet:resourceURL>

<portlet:renderURL var="naviURL" >
	<portlet:param name="myaction" value="showNavi" />
</portlet:renderURL>

<%
	/* Parses the parent path url from the portlet renderURL */
	int pos = naviURL.indexOf("default");
	String defaultPath = naviURL.substring(0, pos+7);
	
	int pos1 = naviURL.lastIndexOf("/");
	String currentPath = naviURL.substring(0, pos1);
	
	int pos2 = currentPath.lastIndexOf("/");
	String parentPath = currentPath.substring(0, pos2);	
	String currentPage = currentPath.substring(pos2+1);
	
	String actionParam = naviURL.substring(pos1);
	
%>
<script type="text/javascript"> 
/*
 * Handle action message navigation
 * @Author: Jinhua Chen
 */
 
 	var koku_navi_type = "${naviType}";
 	var defaultPath = "<%= defaultPath %>";
 	var naviRefreshTimer;
 	
	jQuery(document).ready(function(){
		var currentPage = "<%= currentPage %>";
		
		if(koku_navi_type != "") {
			var obj = "#message_" + koku_navi_type;
			jQuery(obj).css("font-weight", "bold");
		}else if(currentPage != ""){
			if(currentPage == 'Message')
				jQuery("#message_inbox").css("font-weight", "bold");
			else if(currentPage == 'NewMessage')
				jQuery("#message_new").css("font-weight", "bold");

		}
		
		ajaxUpdate();		
		clearInterval(naviRefreshTimer);
		var duration = 5 * 1000; // 5 seconds
		naviRefreshTimer = setInterval('ajaxUpdate()', duration);		
	});

	/**
	 * Execute ajax query in Post way, and parse the Json format response, and
	 * then create tasks in table and task page filed.
	 */
	function ajaxUpdate() {

		var url="<%= ajaxURL %>";

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
	
	function getMessage(naviType) {
		
		if(naviType == 'new') {
			var url = "<%= defaultPath %>" + "/Message/NewMessage" + "<%= actionParam %>" + '&naviType=' + naviType;;
		}else {
			var url = "<%= defaultPath %>" + "/Message" + "<%= actionParam %>" + '&naviType=' + naviType;
		}
			
		window.location = url;
	}
	
	/**
	 * Show/hide search user interface
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
		<li><a href="javascript:void(0)" >Etusivu</a></li>
		<li><a href="<%= defaultPath %>/TaskManager">Kasvun ja kehityksen suunnitelma</a>
			<ul class="child">
				<li><a href="#">Lapsen_001_nimi</a></li>
				<li><a href="#">Lapsen_002_nimi</a></li>
			</ul></li>
		<li><a href="javascript:void(0)" >Omat tiedot</a></li>
		<li><a href="javascript:void(0)" onclick="getMessage('inbox')" >Viestit</a>
			<ul class="open child">
				<li id="message_new"><a href="javascript:void(0)" onclick="getMessage('new')">Uusi viesti</a> </li>
				<li id="message_inbox"><a href="javascript:void(0)" onclick="getMessage('inbox')">Saapuneet <span id="inbox_num" class="message_num"></span></a></li>
				<li id="message_outbox"><a href="javascript:void(0)" onclick="getMessage('outbox')">Lähetetyt</a></li>
				<li><a href="javascript:void(0)" onclick="showArchiveUI()">Arkistoidut</a>
					<ul id="archive-part">
						<li id="message_archive_inbox"><a href="javascript:void(0)" onclick="getMessage('archive_inbox')">Saapuneet <span id="archive_inbox_num" class="message_num"></span></a></li>
						<li id="message_archive_outbox"><a href="javascript:void(0)" onclick="getMessage('archive_outbox')">Lähetetyt</a></li>
					</ul>
				</li>
			</ul></li>
		<li><a href="#">Pyynnöt</a>
			<ul class="child">
				<li><a href="#">Uudet</a></li>
				<li><a href="<%= defaultPath %>/Message/ValidRequest">Voimassaolevat</a></li>
				<li><a href="#">Vanhentuneet</a></li>
			</ul></li>
		<li><a href="#">Pyynnöt (Työntekijä)</a>
			<ul class="child">
				<li><a href="#">Uudet</a></li>
				<li><a href="#">Voimassaolevat</a></li>
				<li><a href="#">Vanhentuneet</a></li>
			</ul></li>			
		<li><a href="#">Asiointipalvelut</a>
			<ul class="child">
				<li><a href="#">Palveluhakemukset</a></li>
				<li><a href="#">Voimassaolevat palvelut</a></li>
				<li><a href="#">Ajanvaraustiedot</a></li>
			</ul></li>
		<li><a href="#">Ohjeet</a></li>
	</ul>
</div>
