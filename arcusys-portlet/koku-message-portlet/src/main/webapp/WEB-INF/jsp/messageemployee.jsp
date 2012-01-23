<%@ include file="init.jsp" %>

<portlet:resourceURL var="ajaxURL" id="getTask">
</portlet:resourceURL>

<portlet:resourceURL var="suggestURL" id="getSuggestion">
</portlet:resourceURL>

<portlet:resourceURL var="archiveURL" id="archiveMessage">
</portlet:resourceURL>

<portlet:resourceURL var="archiveOldURL" id="archiveMessageOld">
</portlet:resourceURL>

<portlet:resourceURL var="deleteURL" id="deleteMessage">
</portlet:resourceURL>

<portlet:resourceURL var="revokeURL" id="revokeConsent">
</portlet:resourceURL>

<portlet:resourceURL var="revokeWarrantURL" id="revokeWarrants">
</portlet:resourceURL>

<portlet:resourceURL var="cancelURL" id="cancelAppointment">
</portlet:resourceURL>

<portlet:renderURL var="messageURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="showMessage" />
</portlet:renderURL>

<portlet:renderURL var="requestURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="showRequest" />
</portlet:renderURL>

<portlet:renderURL var="appointmentURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="showAppointment" />
</portlet:renderURL>

<portlet:renderURL var="consentURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="showConsent" />
</portlet:renderURL>

<portlet:renderURL var="citizenWarrantURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="showWarrant" />
</portlet:renderURL>

<portlet:renderURL var="tipyURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="showTipy" />
</portlet:renderURL>

<portlet:renderURL var="applicationKindergartenURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="showApplicationKindergarten" />
</portlet:renderURL>


<portlet:resourceURL var="messageRenderURL" id="createMessageRenderUrl">
</portlet:resourceURL>

<portlet:resourceURL var="requestRenderURL" id="createRequestRenderUrl">
</portlet:resourceURL> 

<portlet:resourceURL var="responseRenderURL" id="createResponseRenderUrl">
</portlet:resourceURL> 

<portlet:resourceURL var="appointmentRenderURL" id="createAppointmentRenderUrl">
</portlet:resourceURL> 

<portlet:resourceURL var="consentRenderURL" id="createConsentRenderUrl">
</portlet:resourceURL>

<portlet:resourceURL var="warrantRenderURL" id="createWarrantRenderUrl">
</portlet:resourceURL>


<%
	/* Parses the parent path url from the portlet ajaxURL */
	
// 	final int currentPathPosition = ajaxURL.indexOf("Message");
// 	final String defaultPath = ajaxURL.substring(0, currentPathPosition+7);
	final String defaultPath = portletPath;
%>

<script type="text/javascript">

	/* Global objects */
	var kokuConfig = null;
	var pageObj = null;
	var kokuAjax = null;
	var kokuSuggestionBox = null;
	var kokuTableNavigation = null;
	
	
	/**
	 *	URLs for ajaxCalls
	 */
	var ajaxUrls = {
	 	
	 	defaultUrl : "<%= defaultPath %>",

		/* Actions or somethings? (portlet:resourceURL)*/
	 	ajaxTaskUrl : "<%= ajaxURL %>",
		suggestUrl : "<%= suggestURL %>",
		archiveUrl : "<%= archiveURL %>",
		archiveOldUrl : "<%= archiveOldURL %>",
		deleteUrl : "<%= deleteURL %>", 
		revokeUrl : "<%=  revokeURL %>",
		revokeWarrantUrl : "<%= revokeWarrantURL %>", 
		cancelUrl : "<%= cancelURL %>", 
	 		 	
	 	/* Urls JBoss Loora  (portlet:resourceURL) */
	 	messageUrl : "<%= messageURL %>",
	 	requestUrl :"<%= requestURL %>",
	 	appointmentUrl : "<%= appointmentURL %>",
	 	responseRenderUrl :  "<%= responseRenderURL %>",
	 	consentUrl : "<%= consentURL %>",
		citizenWarrantUrl : "<%= citizenWarrantURL %>",
		tipyUrl : "<%= tipyURL %>",
		applicationKindergartenUrl : "<%= applicationKindergartenURL %>",
	};
	
	
	
	<%-- Loading JS from separate jspf files. Thanks to "ugly" Gatein portal. --%>
	<%-- Note that loading order in here is important! --%>
	<%@ include file="js_koku_config.jspf" %>
	<%@ include file="js_koku_utils.jspf" %>
	<%@ include file="js_koku_table.jspf" %>
	<%@ include file="js_koku_table_employee.jspf" %>
	<%@ include file="js_koku_ajax.jspf" %>
	<%@ include file="js_koku_ajax_employee.jspf" %>
	<%@ include file="js_koku_suggestion.jspf" %>
	<%@ include file="js_koku_table_navigation.jspf" %>
	<%@ include file="js_koku_table_navigation_employee.jspf" %>

</script>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.datepick.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.datepick-fi.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jgrowl_minimized.js"></script>

<script type="text/javascript"> 
/*
 * Handle action for task manager
 * @Author: Jinhua Chen
 */		
jQuery(document).ready(function() {
	
	/**
	 * Get the parameters stored in session when returns from the task form page,
	 * which is in order to keep the page unchanged 
	 */
	function checkPageSession() {
		
		/**
		 * extract keyword from keyword string, which consists of keyword and field in session, e.g. 'test|1 2 3 4' 
		 */
		function extractKeyword(keywordStr) {
			var temp = keywordStr.split("|");
			var keyword = temp[0];				
			return keyword;
		}
		
		/**
		 * extract keyword from keyword string, which consists of keyword and field in session, e.g. 'test|1 2 3 4' 
		 */
		function extractField(keywordStr) {
			var temp = keywordStr.split("|");
			var field = temp[1];
			var fields = field.split('_');
			jQuery('input:checkbox[name="field"]').attr('checked', false);
			for(var i=0; i < fields.length; i++) {
				jQuery('input:checkbox[name="field"][value="' + fields[i] + '"]').attr('checked', true);
			}		
			return field;
		}
		
		var currentPage = "${currentPage}";
		var taskType = "${taskType}";
		var keyword = "${keyword}";
		var orderType = "${orderType}";
		
		if(currentPage != '' && currentPage != 'null') {
			pageObj.currentPage = parseInt(currentPage);
			pageObj.taskType = taskType;
			pageObj.keyword = extractKeyword(keyword);
			pageObj.field = extractField(keyword);
			pageObj.orderType = orderType;
		}
	};
	
	function datePickerInit() {
		/* Attach datepickers */
		jQuery.datepick.setDefaults($.datepick.regional['fi']);			
	 	jQuery(function() {
	 		jQuery('input#tipyTimeRangeFrom').datepick({showTrigger: '#calImg'});
	 		jQuery('input#tipyTimeRangeTo').datepick({showTrigger: '#calImg'});
	 	});		 	
	};
	jQuery.jGrowl.defaults.position = 'top-right';
	
	
	kokuConfig = new Config("<%= refreshDuration %>", "<%= messageType %>");
	pageObj = new Paging();
	kokuAjax = new KokuAjaxEmployee(ajaxUrls);
	kokuSuggestionBox = new SuggestionBox(ajaxUrls);
	kokuTableNavigation = new KokuTableNavigationEmployee(kokuAjax);
	
	datePickerInit();		
	checkPageSession();
	/* Ajax activity support call. Show the ajax loading icon */
    jQuery('#task-manager-operation-loading')
	    .hide()  // hide it initially
	    .ajaxStart(function() {
			jQuery(this).show();
	    })
	    .ajaxStop(function() {
	    	jQuery(this).hide();
	    });
	
	/* User is logged in and participant token for intalio is valid */
	if (pageObj.loginStatus == 'VALID') {
		kokuAjax.ajaxGetTasks(pageObj, presentTasks);
		KokuUtil.timer.resetRefreshTimer(kokuConfig);
	} else {
		var message = "<spring:message code="error.unLogin" />";
		KokuUtil.errorMsg.showErrorMessage(message);
	}
	
	/* remove the timer when user is operating on the page */
	jQuery('#task-manager-wrap').click(function(){
		KokuUtil.timer.resetRefreshTimer(kokuConfig);
    });
			
});

/**
 * Represents the tasks in table list view and creates page operatonal part
 */
function presentTasks(tasks) {
	
	/**
	 * Decorate the table by adding background class when mousemove, mouseout, etc
	 */
	function decorateTable() {
		
		var tr = jQuery('.task-manager-table tr');

		for ( var i = 1; i < tr.length; i++) {			
			tr[i].onmousemove = function() {
				jQuery(this).addClass('focusRow');
			}

			tr[i].onmouseout = function() {			
				jQuery(this).removeClass('focusRow');
			}
			/*
			tr[i].onclick = function() {
				tr.removeClass('clickRow');
				jQuery(this).addClass('clickRow');
			}
			*/
		}
		
		var checkboxes = jQuery('.task-manager-table tr input:checkbox');
		
		checkboxes.click(function(){
			if(this.checked)
				jQuery(this).parent().parent().addClass('clickRow');
			else
				jQuery(this).parent().parent().removeClass('clickRow');
		});

	}
	
	var table = new KokuEmployeeTable();
	kokuSuggestionBox.setSuggestType('<%= Constants.SUGGESTION_NO_TYPE %>');

	var taskHtml = "";
	if (pageObj.taskType.indexOf('req_') == 0) { // for request
		if (pageObj.taskType == "<%= Constants.TASK_TYPE_REQUEST_DONE_EMPLOYEE %>" || pageObj.taskType == "<%= Constants.TASK_TYPE_REQUEST_VALID_EMPLOYEE %>") {
			taskHtml += table.createRequestsEmployeeTable(tasks);
		} else if (pageObj.taskType == "<%= Constants.TASK_TYPE_REQUEST_REPLIED %>" || pageObj.taskType == "<%= Constants.TASK_TYPE_REQUEST_OLD %>") {
			taskHtml += table.createRequestReplied(tasks);
		}		
	} else if(pageObj.taskType.indexOf('app_') == 0) { // for appointment
		// taskHtml += table.createAppoitmentsTable(tasks);
		if (pageObj.taskType == "<%= Constants.TASK_TYPE_APPOINTMENT_INBOX_CITIZEN%>") {
			taskHtml +=  table.createAppoitmentsInboxCitizenTable(tasks);		
		} else {
			taskHtml +=  table.createAppoitmentsTable(tasks);	
		}
	} else if(pageObj.taskType.indexOf('cst_') == 0) { // for consent
		if (pageObj.taskType == "<%= Constants.TASK_TYPE_CONSENT_EMPLOYEE_CONSENTS%>") {			
			kokuSuggestionBox.setSuggestType('<%= Constants.SUGGESTION_CONSENT %>');
			taskHtml += table.createBrowseEmployeeOwnConsents(tasks);
		} else if (pageObj.taskType == "<%= Constants.TASK_TYPE_WARRANT_LIST_CITIZEN_CONSENTS%>") {
			taskHtml += table.createBrowseUserWarrantsTable(tasks);
		} else if (pageObj.taskType == "<%= Constants.TASK_TYPE_WARRANT_LIST_SUBJECT_CONSENTS%>") {
			// suggestType = '<%= Constants.SUGGESTION_WARRANT %>';
			kokuSuggestionBox.setSuggestType('<%= Constants.SUGGESTION_WARRANT %>');
			taskHtml += table.createBrowseUserWarrantsTable(tasks);
		}
	} else if (pageObj.taskType.indexOf('info_req_') == 0) { // for infoRequests (tietopyyntö)
		taskHtml += table.createInfoRequestsTable(tasks);		
	} else if (pageObj.taskType.indexOf('application_') == 0) { // for applications (hakemukset)
		kokuSuggestionBox.setSuggestType('<%= Constants.SUGGESTION_APPLICATION_KINDERGARTEN %>');
		taskHtml += table.createApplicationsTable(tasks);	
	} else {											// for message
		taskHtml += table.createMessagesTable(tasks);
	}
	 
	jQuery('#task-manager-tasklist').html(taskHtml);
	decorateTable();
	// var pageHtml = createTasksPage(pageObj);
	var pageHtml = kokuTableNavigation.createTasksPage(pageObj);
	jQuery('#task-manager-operation-page').html(pageHtml);
}
 	
	
</script>
<div id="task-manager-outer-wrap">
	<div id="task-manager-wrap">
		<div id="task-manager-tasklist">
			<table class="task-manager-table">
			</table>
		</div>
		
		<div class="taskmanager-operation-part-wrapper">
			<div id="task-manager-search" class="task-manager-operation-part">
			
				<div id="message-search" class="basic-search" style="display:none;">
					<form name="searchForm" onsubmit="kokuTableNavigation.searchTasks(); return false;">		
						<span class="text-bold" ><spring:message code="message.searchKeyword" /></span>
						<input type="text" name="keyword" id="keyword" style="width:160px;" />
						<input type="submit" value="<spring:message code="message.search"/>" />
						<input type="button" value="<spring:message code="message.searchReset"/>" onclick="kokuTableNavigation.resetSearch()" />
						<span id="search-fields" >
							<input type="checkbox" checked="checked" name="field" value="1" /><spring:message code="message.from" />
							<input type="checkbox" checked="checked" name="field" value="2" /><spring:message code="message.to" />
							<input type="checkbox" checked="checked" name="field" value="3" /><spring:message code="message.subject" />
							<input type="checkbox" checked="checked" name="field" value="4" /><spring:message code="message.content" />
						</span>	
					</form>
				</div>
				
				<div id="consent-search" class="basic-search" style="display:none; position:relative;">
					<form name="searchForm" onsubmit="kokuTableNavigation.searchConsents(); return false;">		
						<span class="text-bold" ><spring:message code="consent.recipients" /></span>
						<input type="text" name="recipient" id="recipient" style="width:100px;" />
						<span class="text-bold" ><spring:message code="consent.templateName" /></span>
						<input type="text" name="templateName" id="templateName" style="width:160px;" autocomplete="off" onkeydown="kokuSuggestionBox.beKeyDown(event)" onkeyup="kokuSuggestionBox.beKeyUp(event)" onclick="kokuSuggestionBox.createSuggestDiv('consent-search', 'templateName')" />
						<input type="submit" value="<spring:message code="message.search"/>" />
						<input type="button" value="<spring:message code="message.searchReset"/>" onclick="kokuTableNavigation.resetSearch()" />
					</form>
				</div>
				
				<!-- Employee appointments search -->
				<div id="employeeAppointment-search" class="basic-search" style="display:none; position:relative;">
					<form name="searchForm" onsubmit="kokuTableNavigation.searchAppointmentsByTargetPersonSsn(); return false;">		
						<span class="text-bold" ><spring:message code="appointment.targetPerson" /></span>
						<input type="text" name="appointmentTargetPerson" id="appointmentTargetPerson" style="width:100px;" />
						<input type="submit" value="<spring:message code="message.search"/>" />
						<input type="button" value="<spring:message code="message.searchReset"/>" onclick="kokuTableNavigation.resetSearch()" />
					</form>
				</div>
				
				<!-- TIVA-13 Selaa asiakkaan suostumuksia -->
				<div id="warrants-search-citizens" class="basic-search" style="display:none; position:relative;">
					<form name="searchForm" onsubmit="kokuTableNavigation.searchWarrantsByCitizen(); return false;">		
						<span class="text-bold" ><spring:message code="warrant.recievedWarrants" /></span>
						<input type="text" name="userIdRecieved" id="userIdRecieved" style="width:100px;" />
						<span class="text-bold" ><spring:message code="warrant.sendedWarrants" /></span>
						<input type="text" name="userIdSent" id="userIdSent" style="width:100px;" />
						<span class="text-bold" ><spring:message code="warrant.targetPerson" /></span>
						<input type="text" name="targetPersonUid" id="targetPersonUid" style="width:100px;" />
		<%-- 				<span class="text-bold" ><spring:message code="warrant.templateName" /></span> --%>
		<!-- 				<input type="text" name="warrantTemplateNameCitizen" id="warrantTemplateNameCitizen" style="width:160px;" autocomplete="off" onkeydown="createSuggestDiv.beKeyDown(event)" onkeyup="createSuggestDiv.beKeyUp(event)" onclick="createSuggestDiv.createSuggestDiv('warrants-search-citizens', 'warrantTemplateNameCitizen')" /> -->
						<input type="submit" value="<spring:message code="message.search"/>" />
						<input type="button" value="<spring:message code="message.searchReset"/>" onclick="kokuTableNavigation.resetSearch()" />
					</form>
				</div>
				
				<!-- TIVA-14 Selaa asian suostumuksia -->
				<div id="warrants-search-warrants" class="basic-search" style="display:none; position:relative;">
					<form name="searchForm" onsubmit="kokuTableNavigation.searchWarrantsByTemplate(); return false;">		
						<span class="text-bold" ><spring:message code="warrant.templateName" /></span>
						<input type="text" name="warrantTemplateName" id="warrantTemplateName" style="width:160px;" autocomplete="off" onkeydown="kokuSuggestionBox.beKeyDown(event)" onkeyup="kokuSuggestionBox.beKeyUp(event)" onclick="kokuSuggestionBox.createSuggestDiv('warrants-search-warrants', 'warrantTemplateName')" />
						
		<%-- 				<span style="display: hidden;" class="text-bold" ><spring:message code="warrant.groupFilter" /></span> --%>
		<%-- 				<input style="display: hidden;" type="text" name="warrantGroupFilter" id="warrantGroupFilter" style="width:100px;" /> --%>
						<input type="submit" value="<spring:message code="message.search"/>" />
						<input type="button" value="<spring:message code="message.searchReset"/>" onclick="kokuTableNavigation.resetSearch()" />
					</form>
				</div>
				
				<!-- TIVA-18 Selaa tietopyyntöjä -->
				<div id="infoRequestsSearch" class="basic-search" style="display:none; position:relative;">
					<form name="searchForm" onsubmit="kokuTableNavigation.searchInfoRequests(); return false;">		
					
						<p class="searchTimeRange">
							<span id="tipyCreateTime">
								<span class="text-bold searchTitle" ><spring:message code="tipy.search.timeRange" /></span>
								<input class="searchTime" type="text" name="tipyTimeRangeFrom" id="tipyTimeRangeFrom"  /> - 
								<input class="searchTime" type="text" name="tipyTimeRangeTo" id="tipyTimeRangeTo" />
							</span>
						</p>
						
						<p class="searchMisc">
							<span class="text-bold searchTitle"><spring:message code="tipy.search.targetPerson" /></span>
							<input type="text" name="tipyTargetPerson" id="tipyTargetPerson" style="width:200px;" />
							
							<span class="text-bold searchTitle"><spring:message code="tipy.search.requester" /></span>
							<input type="text" name="tipyRequester" id="tipyRequester" style="width:200px;" />
						</p>
		
						<p class="searchMisc">					
							<span class="text-bold searchTitle"><spring:message code="tipy.search.handOver" /></span>
							<input type="text" name="tipyHandOver" id="tipyHandOver" style="width:200px;" />
		
							<span class="text-bold searchTitle"><spring:message code="tipy.search.information" /></span>
							<input type="text" name="tipyInformation" id="tipyInformation" style="width:200px;" />
						</p>
						<p class="searchMisc">
							<span class="text-bold searchTitle"><spring:message code="tipy.search.freeTextSearch" /></span>
							<input type="text" name="tipyFreeTextSearch" id="tipyFreeTextSearch" style="width:200px;" />
						</p>
						<p class="searchMisc searchButtonArea">
							<input type="submit" value="<spring:message code="message.search"/>" />
							<input type="button" value="<spring:message code="message.searchReset"/>" onclick="kokuTableNavigation.resetSearch()" />
						</p>
					</form>
				</div>
				
				<!-- HAK-2 Selaa päiväkotihakemuksia -->
				<div id="applicationKindergartenBrowse" class="basic-search" style="display:none; position:relative;">
					<form name="searchForm" onsubmit="kokuTableNavigation.searchKindergartenApplications(); return false;">		
									
						<p class="searchMisc kindergartenSearch">
							<span class="text-bold searchTitle"><spring:message code="application.kindergarten.search.kindergarten" /></span>
							<input type="text" name="applicationKindergartenName" id="applicationKindergartenName" style="width:160px;"  autocomplete="off" onkeydown="kokuSuggestionBox.beKeyDown(event)" onkeyup="kokuSuggestionBox.beKeyUp(event)" onclick="kokuSuggestionBox.createSuggestDiv('applicationKindergartenBrowse', 'applicationKindergartenName')" />					
						</p>
		
						<p class="searchMisc kindergartenSelections">					
							<span class="text-bold searchTitle"><spring:message code="application.kindergarten.search.guardianAccpeted" /></span>
						  	<select class="yesNoSelect" name="applicationKindergartendGuardianAccepted" id="applicationKindergartendGuardianAccepted">
						  	  <option selected="selected"></option>
							  <option value="true">Kyllä</option>
							  <option value="false">Ei</option>
							</select>
							<span class="text-bold searchTitle"><spring:message code="application.kindergarten.search.placeAccepted" /></span>
							<select class="yesNoSelect" name="applicationKindergartendPlaceCancelled" id="applicationKindergartendPlaceCancelled" >
							  <option selected="selected"></option>
							  <option value="true">Kyllä</option>
							  <option value="false">Ei</option>
							</select>					
							<span class="text-bold searchTitle"><spring:message code="application.kindergarten.search.guardianAcceptedHighestIncome" /></span>
							<select class="yesNoSelect" name="applicationKindergartendHighestIncome" id="applicationKindergartendHighestIncome">
							  <option selected="selected"></option>
							  <option value="true">Kyllä</option>
							  <option value="false">Ei</option>
							</select>				
						</p>
						<p class="searchMisc searchButtonArea">
							<input type="submit" value="<spring:message code="message.search"/>" />
							<input type="button" value="<spring:message code="message.searchReset"/>" onclick="kokuTableNavigation.resetSearch()" />
						</p>
					</form>
				</div>
			</div>
		</div>
		<div class="taskmanager-operation-part-wrapper">		
			<div id="task-manager-operation" class="task-manager-operation-part">
				<div id="task-manager-operation-page"></div>
				<div id="task-manager-operation-loading"><spring:message code="page.loading"/></div>
			</div>
		</div>
	</div>
</div>

