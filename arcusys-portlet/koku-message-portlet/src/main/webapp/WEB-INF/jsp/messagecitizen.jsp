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

<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>


<!-- For gatein Portal -->
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


<%-- Do not move navigation helper inside <script> tags --%>
<%@ include file="js_koku_navigation_helper.jspf" %>

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
	 	defaultUrl : "<%= portletPath %>",

		/* Actions or somethings? (portlet:resourceURL)*/
	 	ajaxTaskUrl : "<%= ajaxURL %>",
	 	homeUrl : "<%= homeURL %>",
		suggestUrl : "<%= suggestURL %>",
		archiveUrl : "<%= archiveURL %>",
		archiveOldUrl : "<%= archiveOldURL %>",
		deleteUrl : "<%= deleteURL %>", 
		revokeUrl : "<%=  revokeURL %>",
		revokeWarrantUrl : "<%= revokeWarrantURL %>", 
		cancelUrl : "<%= cancelURL %>", 
	 	
	 	/* RenderUrls GateIn Kunpo (portlet:renderURL) */
	 	messageRenderUrl : "<%= messageRenderURL %>",
	 	requestRenderUrl : "<%= requestRenderURL %>",
	 	responseRenderUrl :  "<%= responseRenderURL %>",
	 	appointmentRenderUrl :  "<%= appointmentRenderURL %>",
	 	consentRenderUrl : 	"<%= consentRenderURL %>",
	 	warrantRenderUrl : "<%= warrantRenderURL %>"
	};

	<%-- Loading JS from separate jspf files instead of .js files. Thanks to "ugly" Gatein portal. --%>
	<%-- Note that loading order in here is important! --%>
	<%@ include file="js_koku_config.jspf" %>
	<%@ include file="js_koku_utils.jspf" %>
	<%@ include file="js_koku_table.jspf" %>
	<%@ include file="js_koku_table_citizen.jspf" %>
	<%@ include file="js_koku_ajax.jspf" %>
	<%@ include file="js_koku_ajax_citizen.jspf" %> 
	<%@ include file="js_koku_suggestion.jspf" %>
	<%@ include file="js_koku_table_navigation.jspf" %>
	<%@ include file="js_koku_table_navigation_citizen.jspf" %>

</script>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jgrowl_minimized.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.qtip.min.js"></script>


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
	}
	
 	jQuery.jGrowl.defaults.position = 'top-right';
	
	kokuConfig = new Config("<%= refreshDuration %>", "<%= messageType %>");
	pageObj = new Paging();
	kokuAjax = new KokuAjaxCitizen(ajaxUrls);
	kokuSuggestionBox = new SuggestionBox(ajaxUrls);
	kokuTableNavigation = new KokuTableNavigationCitizen(kokuAjax);

	
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
	};
	
	var table = new KokuCitizenTable();
	kokuSuggestionBox.setSuggestType('<%= Constants.SUGGESTION_NO_TYPE %>');

	var taskHtml = "";
	
	switch(pageObj.taskType) {
		case "<%= Constants.TASK_TYPE_REQUEST_REPLIED %>" : 
		case "<%= Constants.TASK_TYPE_REQUEST_OLD %>" :
			taskHtml += table.createRequestsTable(tasks);
			break;	
		case "<%= Constants.TASK_TYPE_APPOINTMENT_INBOX_CITIZEN %>" :
			taskHtml +=  table.createAppoitmentsTable().unanswered(tasks);
			break;
		case "<%= Constants.TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN %>" :
			taskHtml +=  table.createAppoitmentsTable().open(tasks);			
			break;
		case "<%= Constants.TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN_OLD %>" :	
			taskHtml +=  table.createAppoitmentsTable().ready(tasks);		
			break;
		case "<%= Constants.TASK_TYPE_CONSENT_ASSIGNED_CITIZEN %>" : 
			taskHtml = table.createConsentsAssignedTable(tasks);
			break;
		case "<%= Constants.TASK_TYPE_CONSENT_CITIZEN_CONSENTS %>" :
			taskHtml = table.createConsentsCurrentTable(tasks);
			break;
		case "<%= Constants.TASK_TYPE_CONSENT_CITIZEN_CONSENTS_OLD %>" :
			taskHtml = table.createConsentsOldTable(tasks);
			break;
		case "<%= Constants.TASK_TYPE_WARRANT_BROWSE_RECEIEVED %>" :
			taskHtml += table.createBrowseWarrantsToMe(tasks);
			break;
		case "<%= Constants.TASK_TYPE_WARRANT_BROWSE_SENT %>" :
			taskHtml += table.createBrowseWarrantsFromMe(tasks);
			break;
		case "<%= Constants.TASK_TYPE_APPLICATION_KINDERGARTEN_BROWSE%>" :
			taskHtml += table.createApplicationsTable(tasks);		
			break;
		default:											// for message
			taskHtml += table.createMessagesTable(tasks, pageObj.taskType);		
			break;
	}
	 
	jQuery('#task-manager-tasklist').html(taskHtml);
	decorateTable();
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
						<input type="button" value="<spring:message code="message.searchReset"/>" onclick="resetSearch()" />
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
						<input type="text" name="templateName" id="templateName" style="width:160px;" autocomplete="off" onkeydown="beKeyDown(event)" onkeyup="beKeyUp(event)" onclick="createSuggestDiv('consent-search', 'templateName')" />
						<input type="submit" value="<spring:message code="message.search"/>" />
						<input type="button" value="<spring:message code="message.searchReset"/>" onclick="resetSearch()" />
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
