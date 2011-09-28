<%@ include file="init.jsp" %>

<portlet:resourceURL var="ajaxURL" id="getTask">
</portlet:resourceURL>

<portlet:resourceURL var="suggestURL" id="getSuggestion">
</portlet:resourceURL>

<portlet:resourceURL var="archiveURL" id="archiveMessage">
</portlet:resourceURL>

<portlet:resourceURL var="deleteURL" id="deleteMessage">
</portlet:resourceURL>

<portlet:resourceURL var="revokeURL" id="revokeConsent">
</portlet:resourceURL>

<portlet:resourceURL var="revokeWarrantURL" id="revokeWarrants">
</portlet:resourceURL>

<portlet:resourceURL var="cancelURL" id="cancelAppointment">
</portlet:resourceURL>

<portlet:renderURL var="messageURL" windowState="<%= WindowState.MAXIMIZED.toString() %>" >
	<portlet:param name="myaction" value="showMessage" />
</portlet:renderURL>

<portlet:renderURL var="requestURL" windowState="<%= WindowState.MAXIMIZED.toString() %>" >
	<portlet:param name="myaction" value="showRequest" />
</portlet:renderURL>

<portlet:renderURL var="appointmentURL" windowState="<%= WindowState.MAXIMIZED.toString() %>" >
	<portlet:param name="myaction" value="showAppointment" />
</portlet:renderURL>

<portlet:renderURL var="consentURL" windowState="<%= WindowState.MAXIMIZED.toString() %>" >
	<portlet:param name="myaction" value="showConsent" />
</portlet:renderURL>

<portlet:renderURL var="citizenWarrantURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="showWarrant" />
</portlet:renderURL>

<portlet:renderURL var="tipyURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="showTipy" />
</portlet:renderURL>


<!-- Not in use currently, but reserved for future use -->
<portlet:renderURL var="intalioFormURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="showIntalioForm" />
</portlet:renderURL>

<!-- For gatein Portal -->
<portlet:resourceURL var="messageRenderURL" id="createMessageRenderUrl">
</portlet:resourceURL>

<portlet:resourceURL var="requestRenderURL" id="createRequestRenderUrl">
</portlet:resourceURL> 

<portlet:resourceURL var="appointmentRenderURL" id="createAppointmentRenderUrl">
</portlet:resourceURL> 

<portlet:resourceURL var="consentRenderURL" id="createConsentRenderUrl">
</portlet:resourceURL>

<portlet:resourceURL var="warrantRenderURL" id="createWarrantRenderUrl">
</portlet:resourceURL>


<!-- For gatein Portal ends-->

<%
	/* Parses the parent path url from the portlet ajaxURL */
	
	String defaultPath = "";

	int pos = ajaxURL.indexOf("default");
	if(pos > -1) { // for Jboss portal
		defaultPath = ajaxURL.substring(0, pos+7);		
	}else { // for Gatein portal
		int pos1 = ajaxURL.indexOf("classic");
		defaultPath = ajaxURL.substring(0, pos1+7);
	}
%>

<script type="text/javascript">

	/* JS constants */
	var CONSENT_SUGGESTION_URL = "<%= suggestURL %>";

</script>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.datepick.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.datepick-fi.js"></script>



<%-- <script type="text/javascript" src="<%=defaultPath%>/js/main.js" /> --%>
<script type="text/javascript"> 
/*
 * Handle action for task manager
 * @Author: Jinhua Chen
 */
	var refreshTimer; // global refresh timer
	var configObj = new config();
	var pageObj = new paging();
	
	
	
	function showDate(date) {
		alert('The date chosen is ' + date);
	}
	
	jQuery(document).ready(function(){
		<%-- % suggestUrl = "<%= suggestURL %>"; --%>
		
		/* Attach datepickers */
		jQuery.datepick.setDefaults($.datepick.regional['fi']);
		
	 	jQuery(function() {
	 		jQuery('input#tipyCreatedTimeRangeFrom').datepick({showTrigger: '#calImg'});
	 		jQuery('input#tipyCreatedTimeRangeTo').datepick({showTrigger: '#calImg'});
	 		jQuery('input#tipyRepliedTimeRangeFrom').datepick({showTrigger: '#calImg'});
	 		jQuery('input#tipyRepliedTimeRangeTo').datepick({showTrigger: '#calImg'});
	 	});
	
		
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
		if(pageObj.loginStatus == 'VALID') {
			ajaxGetTasks();		
			resetRefreshTimer();
		}else {
			var message = "<spring:message code="error.unLogin" />";
			showErrorMessage(message);
		}
		
		/* remove the timer when user is operating on the page */
		jQuery('#task-manager-wrap').click(function(){
			resetRefreshTimer();
	    });
				
	});
		
	


	/**
	 * Execute ajax query in Post way, and parse the Json format response, and
	 * then create tasks in table and task page filed.
	 */
	function ajaxGetTasks() {
		
		if(pageObj.loginStatus != 'VALID') {
			return;
		}
		
		var url="<%= ajaxURL %>";
		url = formatUrl(url);

		pageObj.taskType = getTaskTypeFromNavi();
		
		jQuery.post(url, {page:pageObj.currentPage, taskType:pageObj.taskType, 
			keyword:pageObj.keyword, orderType:pageObj.orderType, field:pageObj.field}, function(data) {
			var obj = eval('(' + data + ')');
			var json = obj.response;
			pageObj.loginStatus = json["loginStatus"];
			
			if(pageObj.loginStatus == 'VALID') {
				pageObj.totalPages = json["totalPages"];
				pageObj.totalItems = json["totalItems"];
				var tasks = json["tasks"];
				pageObj.tasks = tasks;
				presentTasks(tasks);				
			}else {
				var message = "<spring:message code="error.unLogin" />";
				showErrorMessage(message);
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
	 * Represents the tasks in table list view and creates page operatonal part
	 */
	function presentTasks(tasks) {
		var taskHtml = "";
		if(pageObj.taskType == 'req_valid') // for request
			taskHtml += createRequestsTable(tasks);
		else if(pageObj.taskType.indexOf('app') == 0) // for appointment
			taskHtml += createAppoitmentsTable(tasks);
		else if(pageObj.taskType.indexOf('cst') == 0) // for consent
			taskHtml += createConsentsTable(tasks);
		else // for message
			taskHtml += createMessagesTable(tasks);
		 
		jQuery('#task-manager-tasklist').html(taskHtml);
		decorateTable();
		var pageHtml = createTasksPage();
		jQuery('#task-manager-operation-page').html(pageHtml);
	}
	
	/**
	 * Gets message type from the global variable 'koku_navi_type' in navi portlet
	 */
	function getTaskTypeFromNavi() {
		var type;
		
		if(typeof koku_navi_type == 'undefined' || koku_navi_type == ''){
			type = "msg_inbox"; // default is inbox
		}else {
			type = koku_navi_type;
		}
		
		return type;
	}
	
	/**
	 * Create messages table in Html
	 */
	function createMessagesTable(tasks) {
		var taskHtml = "";
		var formLink = "";
		
		taskHtml = '<table class="task-manager-table">'
				+ '<tr class="task-manager-table trheader">'
				+ '<td class="choose"><spring:message code="message.choose" /></td>'
				+ '<td>' + formatSender() + '</td>'
				+ '<td>' + '<spring:message code="message.subject" />' + '</td>'
				+ '<td><spring:message code="message.received" /></td>'
				+ '</tr>';
				 
		for ( var i = 0; i < tasks.length; i++) {
			
			if((i+1)%2 == 0) {
				if(tasks[i]["messageStatus"] == 'unread') // for new messages
					taskHtml += '<tr class="evenRow new">';
				else
					taskHtml += '<tr class="evenRow">';	
			}else {
				if(tasks[i]["messageStatus"] == 'unread')
					taskHtml += '<tr class="new">';
				else
					taskHtml += '<tr>';
			}
			
			taskHtml += '<td class="choose">' + '<input type="checkbox" name="message" value="' + tasks[i]["messageId"] + '" />' + '</td>'
					 + '<td class="messageItem" onclick="showMessage(\''+ tasks[i]["messageId"] + '\')" >' + formatUser(tasks[i]) + '</td>'
					 + '<td class="messageItem" onclick="showMessage(\''+ tasks[i]["messageId"] + '\')" >' + formatSubject(tasks[i]["subject"]) + '</td>'
					 + '<td class="messageItem" onclick="showMessage(\''+ tasks[i]["messageId"] + '\')" >' + tasks[i]["creationDate"] + '</td>'
					 + '</tr>';
		}

		taskHtml += '</table>';

		return taskHtml;
	}
	
	/**
	 * Create requests table in Html
	 */
	function createRequestsTable(tasks) {
		var taskHtml = "";
		var formLink = "";
		
		taskHtml = '<table class="task-manager-table">'
				+ '<tr class="task-manager-table trheader">'
				+ '<td class="choose"><spring:message code="message.choose" /></td>'
				+ '<td>' + '<spring:message code="message.subject" />' + '</td>'
				+ '<td>' + '<spring:message code="request.responded"/>' + '</td>'
				+ '<td>' + '<spring:message code="request.missed"/>'+ '</td>'
				+ '<td>' + '<spring:message code="request.start"/>'+ '</td>'
				+ '<td>' + '<spring:message code="request.end"/>'+ '</td>'
				+ '</tr>';
				 
		for ( var i = 0; i < tasks.length; i++) {
			
			if((i+1)%2 == 0) {
				if(tasks[i]["messageStatus"] == 'unread') // for new messages
					taskHtml += '<tr class="evenRow new">';
				else
					taskHtml += '<tr class="evenRow">';	
			}else {
				if(tasks[i]["messageStatus"] == 'unread')
					taskHtml += '<tr class="new">';
				else
					taskHtml += '<tr>';
			}
			
			taskHtml += '<td class="choose">' + '<input type="checkbox" name="message" value="' + tasks[i]["requestId"] + '" />' + '</td>'
					 + '<td class="messageItem" onclick="showRequest(\''+ tasks[i]["requestId"] + '\')" >' + tasks[i]["subject"] + '</td>'
					 + '<td class="messageItem" onclick="showRequest(\''+ tasks[i]["requestId"] + '\')" >' + tasks[i]["respondedAmount"] + '</td>'
					 + '<td class="messageItem" onclick="showRequest(\''+ tasks[i]["requestId"] + '\')" >' + tasks[i]["missedAmount"] + '</td>'
					 + '<td class="messageItem" onclick="showRequest(\''+ tasks[i]["requestId"] + '\')" >' + tasks[i]["creationDate"] + '</td>'
					 + '<td class="messageItem" onclick="showRequest(\''+ tasks[i]["requestId"] + '\')" >' + tasks[i]["endDate"] + '</td>'
					 + '</tr>';
		}

		taskHtml += '</table>';

		return taskHtml;
	}
	
	/**
	 * Create appointments table in Html
	 */
	function createAppoitmentsTable(tasks) {
		var taskHtml = "";
		var formLink = "";
		
		taskHtml = '<table class="task-manager-table">'
				+ '<tr class="task-manager-table trheader">'
				+ '<td class="choose"><spring:message code="message.choose" /></td>'
				+ '<td>' + '<spring:message code="message.from" />' + '</td>'
				+ '<td>' + '<spring:message code="message.subject" />' + '</td>'
				+ '<td><spring:message code="message.description" /></td>'
				+ '<td><spring:message code="message.status" /></td>'
				+ '</tr>';
				 
		for ( var i = 0; i < tasks.length; i++) {
			
			taskHtml += generateRowTr(i);			
			taskHtml += '<td class="choose">' + '<input type="checkbox" name="message" value="' + i + '" />' + '</td>'
					 + '<td class="messageItem" onclick="showAppointment(\''+ i + '\')" >' + tasks[i]["sender"] + '</td>'
					 + '<td class="messageItem" onclick="showAppointment(\''+ i + '\')" >' + formatSubject(tasks[i]["subject"]) + '</td>'
					 + '<td class="messageItem" onclick="showAppointment(\''+ i + '\')" >' + tasks[i]["description"] + '</td>'
					 + '<td class="messageItem" onclick="showAppointment(\''+ i + '\')" >' + tasks[i]["status"] + '</td>'
					 + '</tr>';
		}

		taskHtml += '</table>';

		return taskHtml;
	}
	
	/**
	 * Create consents table in Html
	 */
	function createConsentsTable(tasks) {
		var taskHtml = "";
		var formLink = "";
		
		/* Default suggestType */
		suggestType = '<%= Constants.SUGGESTION_CONSENT %>';
		
		if(pageObj.taskType == "<%= Constants.TASK_TYPE_CONSENT_ASSIGNED_CITIZEN %>") {
			taskHtml = '<table class="task-manager-table">'
				+ '<tr class="task-manager-table trheader">'
				+ '<td>' + '<spring:message code="consent.requester" />' + '</td>'
				+ '<td>' + '<spring:message code="consent.templateName" />'+ '</td>'
				+ '</tr>';
				 
			for ( var i = 0; i < tasks.length; i++) {
			
				if((i+1)%2 == 0) {
					taskHtml += '<tr class="evenRow" onclick="showConsent(\''+ i + '\')">';	
				}else {
					taskHtml += '<tr onclick="showConsent(\''+ i + '\')">';
				}
			
				taskHtml += '<td class="messageItem">' + tasks[i]["requester"] + '</td>'
					 	 + '<td class="messageItem">' + tasks[i]["templateName"] + '</td>'
					 	 + '</tr>';
			}

			taskHtml += '</table>';
			
		} else if(pageObj.taskType == "<%= Constants.TASK_TYPE_CONSENT_CITIZEN_CONSENTS %>") {
			taskHtml = '<table class="task-manager-table">'
				+ '<tr class="task-manager-table trheader">'
				+ '<td class="choose"><spring:message code="message.choose" /></td>'
				+ '<td>' + '<spring:message code="consent.requester" />' + '</td>'
				+ '<td style="width:250px;">' + '<spring:message code="consent.templateName" />' + '</td>'
				+ '<td>' + '<spring:message code="consent.status" />' + '</td>'
				+ '<td>' + '<spring:message code="consent.approvalStatus" />' + '</td>'
				+ '<td>' + '<spring:message code="consent.createType" />' + '</td>'
				+ '<td>' + '<spring:message code="consent.givenDate" />' + '</td>'
				+ '<td>' + '<spring:message code="consent.validDate" />' + '</td>'
				+ '<td>' + '<spring:message code="consent.secondApprover" />' + '</td>'
				+ '</tr>';
				 
			for ( var i = 0; i < tasks.length; i++) {
			
				taskHtml += generateRowTr(i);			
				taskHtml += '<td class="choose">' + '<input type="checkbox" name="message" value="' + i + '" />' + '</td>'
						 + '<td class="messageItem" onclick="showConsent(\''+ i + '\')" >' + tasks[i]["requester"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ i + '\')" >' + tasks[i]["templateName"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ i + '\')" >' + tasks[i]["status"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ i + '\')" >' + tasks[i]["approvalStatus"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ i + '\')" >' + tasks[i]["createType"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ i + '\')" >' + tasks[i]["assignedDate"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ i + '\')" >' + tasks[i]["validDate"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ i + '\')" >' + tasks[i]["anotherPermitterUid"] + '</td>'
					 	 + '</tr>';
			}

			taskHtml += '</table>';
			
		} else if(pageObj.taskType == "<%= Constants.TASK_TYPE_CONSENT_EMPLOYEE_CONSENTS%>") {			
			taskHtml += createBrowseEmployeeOwnConsents(tasks);
		} else if (pageObj.taskType == "<%= Constants.TASK_TYPE_WARRANT_LIST_CITIZEN_CONSENTS%>") {
			taskHtml += createBrowseUserWarrantsTable(tasks);
		} else if (pageObj.taskType == "<%= Constants.TASK_TYPE_WARRANT_LIST_SUBJECT_CONSENTS%>") {
			suggestType = '<%= Constants.SUGGESTION_WARRANT %>';
			taskHtml += createBrowseUserWarrantsTable(tasks);
		} else if (pageObj.taskType == "<%= Constants.TASK_TYPE_WARRANT_BROWSE_RECEIEVED %>") {
			taskHtml += createBrowseWarrantsToMe(tasks);
		} else if (pageObj.taskType == "<%= Constants.TASK_TYPE_WARRANT_BROWSE_SENT %>") {
			taskHtml += createBrowseWarrantsFromMe(tasks);
		} else if (pageObj.taskType == "<%= Constants.TASK_TYPE_CONSENT_BROWSE %>") {
			taskHtml += createBrowseAllConsents(tasks);	
		}
		return taskHtml;
	}
	
	function createBrowseAllConsents(tasks) {
		
		if (tasks == undefined || tasks == null || tasks.length == 0) {
			return showErrorMsgYouDontHaveAnyTipys();
		}
		
		var columnNames = ["<spring:message code="message.choose"/>",
		                   "<spring:message code="tipy.receiver"/>",
		                   "<spring:message code="tipy.sender"/>",
		                   "<spring:message code="tipy.targetPerson"/>",
		                   "<spring:message code="tipy.title"/>",
		                   "<spring:message code="tipy.status"/>",
		                   "<spring:message code="tipy.validTill.short"/>"
		                  ];
		
		var columnIds = ["recieverName",
		                 "senderName",
		                 "targetPersonName",
		                 "title",
		                 "localizedStatus",
		                 "validTill"];
				
		return createTable("showTipy", "createBrowseAllConsensts", columnNames, columnIds, tasks);
	}
	
	
	function createBrowseWarrantsToMe(tasks) {
		if (tasks == undefined || tasks == null || tasks.length == 0) {
			return showErrorMsgYouDontHaveAnyConsents();
		}
		
		var columnNames = ["<spring:message code="message.choose"/>",
		                   "<spring:message code="warrant.templateName"/>",
		                   "<spring:message code="warrant.status"/>",
// 		                   "<spring:message code="consent.approvalStatus"/>",
// 		                   "<spring:message code="consent.createType"/>",
// 		                   "<spring:message code="consent.givenDate"/>",
// 		                   "<spring:message code="consent.validDate"/>"
		                   "<spring:message code="warrant.validTill.short"/>"
		                  ];
		
		var columnIds = [
		                 "templateNameWithDescription",
		                 "localizedStatus",
// 		                 "approvalStatus",
// 		                 "createType",
// 		                 "assignedDate",
// 		                 "validDate"
						 "validTill"
		                 ];
		
		flattenTasksContent(tasks);

		return createTable("showWarrant", "createBrowseWarrantsToMe" , columnNames, columnIds, tasks);
	}
	
	function createBrowseWarrantsFromMe(tasks) {
		if (tasks == undefined || tasks == null || tasks.length == 0) {
			return showErrorMsgYouDontHaveAnyConsents();
		}
		
		var columnNames = ["<spring:message code="message.choose"/>",
		                   "<spring:message code="warrant.templateName"/>",
		                   "<spring:message code="warrant.status"/>",
// 		                   "<spring:message code="consent.approvalStatus"/>",
// 		                   "<spring:message code="consent.createType"/>",
// 		                   "<spring:message code="consent.givenDate"/>",
// 		                   "<spring:message code="consent.accepted"/>",
// 		                   "<spring:message code="consent.validDate"/>",
		                   "<spring:message code="warrant.validTill.short"/>",
		                   "<spring:message code="warrant.edit"/>"
		                  ];
		
		var columnIds = [
		                 "templateNameWithDescription",
		                 "localizedStatus",
// 		                 "approvalStatus",
// 		                 "createType",
// 		                 "assignedDate",
// 		                 "acceptedDate",
// 		                 "validDate",
						 "validTill",
		                 "editLink"
		                 ];
		flattenTasksContent(tasks);
		createEditConsentColumn(tasks);

		return createTable("showWarrant", "createBrowseWarrantsFromMe", columnNames, columnIds, tasks);
	}
	
	function createEditConsentColumn(tasks) {
		for (var i = 0; i < tasks.length; i++)  {
			if (tasks[i].status != 'REVOKED') {
				var url = "<%= defaultPath %>/Message/ValtakirjaEditConsent?FormID="+ tasks[i].authorizationId;
				tasks[i]["editLink"] = "<a href="+ url+ "><spring:message code="consent.edit"/></a>";
			} else {
				tasks[i]["editLink"] = "";
			}
		}
	}
	
	function flattenTasksContent(tasks) {
		for (var i = 0; i < tasks.length; i++)  {			
			tasks[i]["templateDescription"] = tasks[i].template.description;
			tasks[i]["templateId"] = tasks[i].template.templateId;
			tasks[i]["templateName"] = tasks[i].template.templateName;
			tasks[i]["templateNameWithDescription"] = "<abbr class='valueWithDesc' title='"+ tasks[i].template.description +"' >" + tasks[i].template.templateName +"</abbr>";
		}
	}

		
	function createBrowseEmployeeOwnConsents(tasks) {
		if (tasks == undefined || tasks == null || tasks.length == 0) {
			return showErrorMsgNoConsents();
		}
		
		var columnNames = ["<spring:message code="message.choose"/>",
		                   "<spring:message code="consent.requester"/>",
		                   "<spring:message code="consent.templateName"/>",
		                   "<spring:message code="consent.status"/>",
		                   "<spring:message code="consent.approvalStatus"/>",
		                   "<spring:message code="consent.createType"/>",
		                   "<spring:message code="consent.givenDate"/>",
		                   "<spring:message code="consent.validDate"/>"
		                  ];
		
		var columnIds = ["requester",
		                 "templateName",
		                 "status",
		                 "approvalStatus",
		                 "createType",
		                 "assignedDate",
		                 "validDate"];
		return createTable("showConsent", "createBrowseEmployeeOwnConsents", columnNames, columnIds, tasks);
	}
	
	
	function createBrowseUserWarrantsTable(tasks) {
		if (tasks == undefined || tasks == null || tasks.length == 0) {
			return showErrorMsgNoConsents();
		}
		var columnNames = ["<spring:message code="message.choose"/>",
		                   "<spring:message code="warrant.sender"/>",
		                   "<spring:message code="warrant.reciever"/>",
		                   "<spring:message code="warrant.targetPersonName"/>",
		                   "<spring:message code="warrant.status"/>",
		                   "<spring:message code="warrant.templateName"/>",
		                   "<spring:message code="warrant.givenDate.short"/>",
		                   "<spring:message code="warrant.validTill.short"/>"
		                  ];
		
		var columnIds = [
		                 "senderName",
		                 "recieverName",
		                 "targetPersonName",
		                 "localizedStatus",
		                 "templateNameWithDescription",
		                 "givenAt",
		                 "validTill"];
		flattenTasksContent(tasks);
		createPersonSearchLinks(tasks);
		return createTable("showWarrant", "browseUserConsentsTable", columnNames, columnIds, tasks);
	}
	
	function createPersonSearchLinks(tasks) {
		for (var i = 0; i < tasks.length; i++)  {			
// 			tasks[i]["senderName"] = '<a href="javascript:void(0)" class="personLink" onclick="searchPersonWarrantsByRecieverUserId(\'' + tasks[i]["senderUid"] + '\')">'+ tasks[i]["senderName"] +'</a>';
// 			tasks[i]["recieverName"] = '<a href="javascript:void(0)" class="personLink" onclick="searchPersonWarrantsByRecieverUserId(\'' + tasks[i]["receiverUid"] + '\')">'+ tasks[i]["recieverName"] +'</a>';
// 			tasks[i]["targetPersonName"] = '<a href="javascript:void(0)" class="personLink" onclick="searchPersonWarrantsByRecieverUserId(\'' + tasks[i]["targetPersonUid"] + '\')">'+ tasks[i]["targetPersonName"] +'</a>';
			tasks[i]["templateNameWithDescription"] = '<abbr class="valueWithDesc" title=\''+ tasks[i].template.description +'\' >' + tasks[i].template.templateName +'</abbr>'; 
		}
	}
	
// 	function searchPersonWarrantsByRecieverUserId(uid) {
// 		pageObj.keyword = uid;
// 		pageObj.field = -1;
// 		ajaxGetTasks();
// 	}


	
	function showErrorMsgNoConsents() {
		return "<div class='errorMsg noConsents'><spring:message code="consent.errorMsg.noWarrants2"/></div>";
	}
	
	function showErrorMsgYouDontHaveAnyConsents() {
		return "<div class='errorMsg noConsents'><spring:message code="consent.errorMsg.noWarrants"/></div>";
	}
	
	function showErrorMsgYouDontHaveAnyTipys() {
		return "<div class='errorMsg noConsents'><spring:message code="tipy.errorMsg.noData"/></div>";
	}

	
	/**
	 * General use table generator
	 *  
	 * @param {string} jsFunctionName
	 * @param {string} styleName for table
	 * @param Array[String] columnNames (for spring)
	 * @param Array[String] columnIds. ColumnIds.length < columnNames.length
	 * @param {object} tasks
	 */
	function createTable(jsFunctionName, tableStyleName, columnNames, columnIds, tasks) {
		var taskHtml = "";		
		taskHtml = "<table class='task-manager-table "+ tableStyleName + "'>"
				+ '<tr class="task-manager-table trheader">';
								
		for (var i = 0; i < columnNames.length; i++)  {
			taskHtml += '<td>' + columnNames[i] + '</td>';
		}
				
		taskHtml += '</tr>';
		taskHtml += generateTableContent(jsFunctionName, columnIds, tasks);
		taskHtml += '</table>';
		return taskHtml;		
	}	
	
	/**
	 * Generates table rows 
	 *
	 * @param {string] functionName
	 * @oaram {Array[String]} columnNames.
	 * @param {Object} tasks
	 * @return {string} table 
	 */
	function generateTableContent(jsFunctionName, columnIds, tasks) {
		var taskHtml = "";
		for ( var i = 0; i < tasks.length; i++) {
			taskHtml += generateRowTr(i);
			taskHtml += '<td class="choose">' + '<input type="checkbox" name="message" value="' + i + '" />' + '</td>'
			
			/* Generate columns */
			for (var j = 0; j < columnIds.length; j++)  {
				taskHtml += '<td class="messageItem" onclick="'+ jsFunctionName + '(\''+ i + '\')" >' + tasks[i][columnIds[j]] + '</td>';
			}
			taskHtml +=	 '</tr>';
		}
		return taskHtml;
	}
	
	/**
	 * Generates tr-element with style "evenRow" if row is even 
	 * and "oddRow" if row is odd.
	 *
	 * @param {number} row
	 * @return {string} 
	 */
	function generateRowTr(row) {
		var taskHtml = "";
		if((row+1)%2 == 0) {
			taskHtml += '<tr class="evenRow">';	
		}else {
			taskHtml += '<tr class="oddRow">';
		}
		return taskHtml;
	}
		
	
	/* Formats sender field */
	function formatSender() {
		
		if(pageObj.taskType == "msg_inbox" || pageObj.taskType == "msg_archive_inbox")
			return  "<spring:message code="message.from" />";
		else 
			return "<spring:message code="message.receiver" />";	
	}
	/* Formats user field */
	function formatUser(task) {
		
		if(pageObj.taskType == "msg_inbox" || pageObj.taskType == "msg_archive_inbox")
			return task["sender"];
		else 
			return task["recipients"];
		
	}
	/* Formats subject field */
	function formatSubject(subject) {
		if(subject == "")		
			return  "<spring:message code="message.noSubject" />";
		else
			return subject;
	}

	<% if(defaultPath.contains("default")) { 	
		// for jboss %>
		<%@ include file="message_jboss.jspf" %> 
	<% } else { 
		// for gatein %>	
		<%@ include file="message_gatein.jspf" %> 
   <%}%>
		
	/*
	function formKeywords(keywordArray) {
		var keywords = '';
		
		for(var i=0; i < keywordArray.length; i++) {
			keywords += keywordArray[i] + '|';
		}
		
		return keywords;
	}
	*/
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
	/* Create initial tasks table Html */
	function createInitTable() {
		return '<table class="task-manager-table">'
				+ '<tr class="task-manager-table trheader">'
				+ '<td class="choose"><spring:message code="message.choose" /></td>'
				+ '<td><spring:message code="message.receiver" /></td>'
				+ '<td><spring:message code="message.subject" /></td>'		
				+ '<td class="date"><spring:message code="message.received" /></td>'	
				+ '</tr></table>';
	}

	/**
	 * Set auto refresh timer, which updates the task list automatically
	 */
	function setRefreshTimer() {
		var duration = parseInt(configObj.refreshDuration) * 1000; // convert to ms
		refreshTimer = setInterval('ajaxGetTasks()', duration);
	}
	
	/**
	 * Remove the auto refresh timer
	 */
	function removeRefreshTimer() {
		clearInterval(refreshTimer);
	}
	
	/**
	 * Reset the auto refresh timer
	 */
	function resetRefreshTimer() {
		removeRefreshTimer();
		setRefreshTimer();
	}
	
	/**
	 * Get the parameters stored in session when returns from the task form page,
	 * which is in order to keep the page unchanged 
	 */
	function checkPageSession() {		
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
	
	/* extract keyword from keyword string, which consists of keyword and field in session, e.g. 'test|1 2 3 4' */
	function extractKeyword(keywordStr) {
		var temp = keywordStr.split("|");
		var keyword = temp[0];
		
		return keyword;
	}
	
	/* extract keyword from keyword string, which consists of keyword and field in session, e.g. 'test|1 2 3 4' */
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
	/**
	 * Config object to handle parameters in configuration mode
	 */	
	 function config() {
			this.refreshDuration = "<%= refreshDuration %>";
			/* outbox, inbox, archive */
			this.messageType = "<%= messageType %>";
		}
	/**
	 * paging object to handle page operations
	 */
	function paging() {
		this.currentPage = 1;
		this.totalPages = 1;
		this.totalItems;
		this.tasks;
		this.taskType = getTaskTypeFromNavi();
		//this.taskType = parseParameter('naviType');
		/* keywords for searching and filter fields */
		this.keyword = '';
		if(this.taskType.indexOf('msg') > -1) {
			this.field = '1_2_3_4';
		}else {
			this.field = '';
		}		
		/* 6 types: by description_desc, by description_asc, by state_desc, 
		by state_asc, by creationDate_desc, by creationDate_asc */
		this.orderType = 'creationDate_desc';
		this.loginStatus = "${loginStatus}";
	}
	
	/** 
	 * Set process order type. Since process does not have state field, set 
	 * default order type creationDate_desc for process if current order type
	 * is related to state
	 */
	paging.prototype.setProcessOrderType = function() {
		if(this.orderType == 'state_desc' || this.orderType == 'state_asc') {
			this.orderType = 'creationDate_desc';
		}
	}
	
	/* get the first page number */
	paging.prototype.getFirstPage = function() {
		var firstPage = this.currentPage != 1 ? 1 : null;
		return firstPage;
	}
	/* get the previous page number */
	paging.prototype.getPrePage = function() {
		var prePage = this.currentPage > 1 ? this.currentPage - 1 : null;
		return prePage;
	}
	/* get the next page number */
	paging.prototype.getNextPage = function() {
		var nextPage = this.currentPage < this.totalPages ? this.currentPage + 1 : null;
		return nextPage;
	}
	/* get the last page number */
	paging.prototype.getLastPage = function() {
		var lastPage = this.currentPage != this.totalPages ? this.totalPages : null;
		return lastPage;
	}
	
	/* move to the the first page */
	paging.prototype.moveFirst = function() {
		var firstPage = this.getFirstPage();
		
		if(firstPage != null) {
			this.currentPage = firstPage;
			ajaxGetTasks();
		}
	}
	/* move to the the previous page */
	paging.prototype.movePre = function() {
		var prePage = this.getPrePage();
		
		if(prePage != null) {
			this.currentPage = prePage;
			ajaxGetTasks();
		}
	}
	/* move to the the next page */
	paging.prototype.moveNext = function() {
		var nextPage = this.getNextPage();
		
		if(nextPage != null) {
			this.currentPage = nextPage;
			ajaxGetTasks();
		}
	}
	/* move to the the last page */
	paging.prototype.moveLast = function() {
		var lastPage = this.getLastPage();
		
		if(lastPage != null) {
			this.currentPage = lastPage;
			ajaxGetTasks();
		}
	}
	
	/**
	 * handle the page changing in Html page
	 */
	function movePage(pageNum) {
		switch (pageNum) {
		case 'first':
			pageObj.moveFirst();
			break;
		case 'previous':
			pageObj.movePre();
			break;
		case 'next':
			pageObj.moveNext();
			break;
		case 'last':
			pageObj.moveLast();
			break;
		}
	}
	
	/**
	 * Create task manager operation part including changing page number and search field
	 */
	function createTasksPage() {
		var pageHtml = '<ul>';
		
		if (pageObj.taskType.indexOf('msg') > -1
				|| pageObj.taskType.indexOf('<%= Constants.TASK_TYPE_CONSENT_EMPLOYEE_CONSENTS %>') > -1 
				|| pageObj.taskType.indexOf('<%= Constants.TASK_TYPE_WARRANT_LIST_CITIZEN_CONSENTS %>') > -1
				|| pageObj.taskType.indexOf('<%= Constants.TASK_TYPE_CONSENT_BROWSE %>') > -1
				|| pageObj.taskType.indexOf('<%= Constants.TASK_TYPE_WARRANT_LIST_SUBJECT_CONSENTS %>') > -1) {
			pageHtml += '<li><input type="button" value="<spring:message code="message.search"/>"  onclick="showSearchUI()" /></li>';
		}
		
		if (pageObj.taskType == 'msg_inbox' || pageObj.taskType == 'msg_outbox') {
			pageHtml += '<li><input type="button" value="<spring:message code="page.archive"/>"  onclick="archiveMessages()" /></li>';
		}
		
		if (pageObj.taskType == '<%= Constants.TASK_TYPE_CONSENT_CITIZEN_CONSENTS %>') {
			pageHtml += '<li><input type="button" value="<spring:message code="consent.revokeSelected"/>"  onclick="revokeConsents()" /></li>';
		} else if ( pageObj.taskType == '<%= Constants.TASK_TYPE_WARRANT_BROWSE_SENT %>') {
			pageHtml += '<li><input type="button" value="<spring:message code="consent.revokeSelected"/>"  onclick="revokeWarrants()" /></li>';
		} else if(pageObj.taskType.indexOf('msg') > -1) {
			pageHtml += '<li><input type="button" value="<spring:message code="page.removeSelected"/>"  onclick="deleteMessages()" /></li>';
		} else if( pageObj.taskType == '<%= Constants.TASK_TYPE_APPOINTMENT_INBOX_EMPLOYEE %>' 
				|| pageObj.taskType == '<%= Constants.TASK_TYPE_APPOINTMENT_RESPONSE_EMPLOYEE %>' 
				|| pageObj.taskType == '<%= Constants.TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN %>') {
			pageHtml += '<li><input type="button" value="<spring:message code="consent.cancel"/>"  onclick="cancelAppointments()" /></li>';
		}
			
		pageHtml += '<li><a><img src="<%= request.getContextPath() %>/images/first.gif" onclick="movePage(\'first\')"/></a></li>'
				 + '<li><a><img src="<%= request.getContextPath() %>/images/prev.gif" onclick="movePage(\'previous\')"/></a></li>'
				 + '<li><spring:message code="page.page"/> ' + pageObj.currentPage + '/' + pageObj.totalPages + '</li>'
				 + '<li><a><img src="<%= request.getContextPath() %>/images/next.gif" onclick="movePage(\'next\')"/></a></li>'
				 + '<li><a><img src="<%= request.getContextPath() %>/images/last.gif" onclick="movePage(\'last\')"/></a></li>'
				 + '<li><spring:message code="page.displaying"/> ' + createDisplayingTasksNum()  + '</li>'
				 + '</ul>';
		return pageHtml;
	}
	
	/**
	 * Show error message to inform user
	 */
	function showErrorMessage(message) {
		var msgHtml = '<div class="task-error-message" >' + message + '</div>';
		jQuery('#task-manager-operation-page').html(msgHtml);
	}
	
	/**
	 * Create task statistics information
	 */
	function createDisplayingTasksNum() {
		var displayTask;
		var startid, endid;
		var numPerPage = 10;	
		
		if(parseInt(pageObj.totalItems) == 0) {
			return "<spring:message code="page.noItems"/>";
		}
		
		startid = (pageObj.currentPage-1)*numPerPage + 1;
		
		if(pageObj.currentPage < pageObj.totalPages) {
			endid = startid + 9;
		}else {
			endid = startid + pageObj.totalItems - (pageObj.currentPage-1)* numPerPage -1;
		}
		
		displayTask = startid + '-' + endid + '/' + pageObj.totalItems;
		
		return displayTask;
	}
		
	/**
	 * Parses the value with given parameter from page url, which is a global variable:koku_currentUrl 
	 */
	function parseParameter(param) {
		 var pos1 = koku_currentUrl.indexOf(param +"=");
		 var value = "";
		 
		 if(pos1 != -1) {
			 var pos2 = koku_currentUrl.indexOf("&", pos1);
			 var paramLen = param.length + 1;
			 
			 if(pos2 != -1) {
				 value = koku_currentUrl.substring(pos1+paramLen, pos2);
			 }
			 else { // the value is in the last of url
				 var totalLen = koku_currentUrl.length;
				 value = koku_currentUrl.substring(pos1+paramLen, totalLen);
			 }
		 }
		 
		 return value;		 		
	}
	
	/**
	 * Archives a list of messages selected by user
	 */
	function archiveMessages() {			
		var messageList = [];		
		jQuery('input:checkbox[name="message"]:checked').each(function(){
		    messageList.push(jQuery(this).val());
		});
		
		if(messageList.length == 0) return; // no message selected
		
		var url="<%= archiveURL %>";
		url = formatUrl(url);

		jQuery.post(url, {'messageList':messageList}, function(data) {
			var obj = eval('(' + data + ')');
			var json = obj.response;
			var result = json["result"];
			
			if(result == 'OK') {
				ajaxGetTasks();
			}else {
				var message = "<spring:message code="error.unLogin" />";
				showErrorMessage(message);
			}
		});
	}
	
	/**
	 * Deletes a list of messages selected by user
	 */
	function deleteMessages() {		
		var messageList = [];		
		jQuery('input:checkbox[name="message"]:checked').each(function(){
		    messageList.push(jQuery(this).val());
		});
		
		if(messageList.length == 0) return; // no message selected
		
		var conf = confirm("<spring:message code="info.conformWarning"/>");
		if(!conf)	return;
		
		var url="<%= deleteURL %>";
		url = formatUrl(url);
		
		jQuery.post(url, {'messageList':messageList}, function(data) {
			var obj = eval('(' + data + ')');
			var json = obj.response;
			var result = json["result"];
			
			if(result == 'OK') {
				ajaxGetTasks();
			}else {
				var message = "<spring:message code="error.unLogin" />";
				showErrorMessage(message);
			}
		});
	}
	
	/**
	 * Cancels a list of appointments selected by user
	 */
	function cancelAppointments() {		
		var messageList = [];
		var targetPersons = [];
		
		jQuery('input:checkbox[name="message"]:checked').each(function(){
			var index =  jQuery(this).val();
			var task = pageObj.tasks[index];
			var appointmentId = task["appointmentId"];
			var targetPerson = task['targetPerson'];
			var status = task['status'];
			
			if(status != 'Peruutettu') { // check if cancelled or not
				messageList.push(appointmentId);
			    targetPersons.push(targetPerson);
			}		    
		});
		
		if(messageList.length == 0) return; // no message selected
		
		var comment = prompt('<spring:message code="consent.cancelPrompt"/>',"");
		if(comment == null)	return;
		
		var url="<%= cancelURL %>";
		url = formatUrl(url);
		
		jQuery.post(url, {'messageList':messageList, 'targetPersons':targetPersons, 'comment':comment, 'taskType':pageObj.taskType}, function(data) {
			var obj = eval('(' + data + ')');
			var json = obj.response;
			var result = json["result"];
			
			if(result == 'OK') {
				ajaxGetTasks();
			}else {
				var message = "<spring:message code="error.unLogin" />";
				showErrorMessage(message);
			}
		});
	}
	
	/**
	 * Revokes a list of consents selected by user
	 */
	function revokeConsents() {		
		var messageList = [];		
		jQuery('input:checkbox[name="message"]:checked').each(function(){
			var id = jQuery(this).val();
			var consentId = pageObj.tasks[id]['consentId'];
			var consentStatus = pageObj.tasks[id]['status'];
			
			if(consentStatus != 'Mitätöity') { // not revoked yet
				messageList.push(consentId);
			}    	
		});
		
		if(messageList.length == 0) return; // no message selected
		
		var conf = confirm("<spring:message code="info.conformRevokeWarning"/>");
		if(!conf)	return;
		
		var url="<%= revokeURL %>";
		url = formatUrl(url);
		
		jQuery.post(url, {'messageList':messageList}, function(data) {
			var obj = eval('(' + data + ')');
			var json = obj.response;
			var result = json["result"];
			
			if(result == 'OK') {
				ajaxGetTasks();
			}else {
				var message = "<spring:message code="error.unLogin" />";
				showErrorMessage(message);
			}
		});
	}
	
	/**
	 * Revokes a list of warrants selected by user
	 */
	function revokeWarrants() {		
		var messageList = [];		
		jQuery('input:checkbox[name="message"]:checked').each(function(){
			var id = jQuery(this).val();
			var authorizationId = pageObj.tasks[id]['authorizationId'];
			var warrantStatus = pageObj.tasks[id]['status'];
			
			if(warrantStatus != '<spring:message code="ConsentStatus.REVOKED"/>') { // not revoked yet
				messageList.push(authorizationId);
			}    	
		});
		
		if(messageList.length == 0) {
			return; // no message selected
		}
		
		var conf = confirm("<spring:message code="info.conformRevokeWarrantWarning"/>");
		
		if(!conf) {
			return;
		}
		
		var comment = prompt('<spring:message code="warrant.cancelPrompt"/>',"");
		if (comment == null) {
			return;
		}
		
		var url="<%= revokeWarrantURL %>";
		url = formatUrl(url);
		
		jQuery.post(url, {'messageList':messageList, 'comment':comment}, function(data) {
			var obj = eval('(' + data + ')');
			var json = obj.response;
			var result = json["result"];
			
			if (result == 'OK') {
				ajaxGetTasks();
			} else {
				var message = "<spring:message code="error.unLogin" />";
				showErrorMessage(message);
			}
		});
	}
	

	/**
	 * Show/hide search user interface
	 */
	function showSearchUI() {
			
		if(pageObj.taskType.indexOf('msg') > -1) { // for message
			jQuery('#message-search').show();
			jQuery('#consent-search').hide();
		} else if (pageObj.taskType == '<%= Constants.TASK_TYPE_WARRANT_LIST_CITIZEN_CONSENTS %>') {
			jQuery('#warrants-search-citizens').show();
			jQuery('#message-search').hide();
		} else if (pageObj.taskType == '<%= Constants.TASK_TYPE_WARRANT_LIST_SUBJECT_CONSENTS %>') {
			jQuery('#warrants-search-warrants').show();
			jQuery('#message-search').hide();
		} else if (pageObj.taskType == '<%= Constants.TASK_TYPE_CONSENT_BROWSE %>') {
			jQuery('#consentsSearchAllConsents').show();
			jQuery('#message-search').hide();
		} else if(pageObj.taskType.indexOf('cst') > -1) { // for consent
			jQuery('#consent-search').show();
			jQuery('#message-search').hide();
		} else {
			return;
		}	
		
		jQuery('#task-manager-search').toggle('fast');
	}
	
	/**
	 * Perform search tasks
	 */
	function searchTasks() {
		var keyword = jQuery("input#keyword").val();
		pageObj.keyword = keyword;	
		var field = '';
		jQuery('input:checkbox[name="field"]:checked').each(function(){
			field += jQuery(this).val() + '_';
		});
		/* get rid of the last space letter*/
		if(field.length > 0)  field = field.substring(0, field.length-1);
		else return false;
		
		pageObj.field = field;	
		ajaxGetTasks();
		
		return false;
	}
	
	function searchConsents() {
		var keyword = jQuery("input#recipient").val();
		var templateName = jQuery("input#templateName").val();
		
		pageObj.field = '';
		
		if(consentTemplates.length == 0 && templateName != '') {
			pageObj.field = -1;	
		}else if(consentTemplates.length > 0 && currentNum != -1) {
			var templateId = consentTemplates[currentNum]['suostumuspohjaId'];
			pageObj.field = templateId;
		}
		
		pageObj.keyword = keyword;			
		ajaxGetTasks();
		
		return false;
	}
	
	function searchWarrantsByCitizen() {
		var keyword = jQuery("input#userIdRecieved").val();
		keyword += '|' + jQuery("input#userIdSent").val();
		keyword += '|' + jQuery("input#targetPersonUid").val();
// 		keyword += '|' + jQuery("input#warrantTemplateNameCitizen").val();
		
		var templateName = jQuery("input#warrantTemplateNameCitizen").val();
		
		pageObj.field = '';
		
		if (consentTemplates.length == 0 && templateName != '') {
			pageObj.field = -1;	
		} else if(consentTemplates.length > 0 && currentNum != -1) {
			var templateId = consentTemplates[currentNum]['suostumuspohjaId'];
			pageObj.field = templateId;
		}
		
		pageObj.keyword = keyword;			
		ajaxGetTasks();
		
		return false;
	}
	
	function searchWarrantsByTemplate() {
		var keyword = jQuery("input#warrantTemplateName").val();
// 		keyword += '|' + jQuery("input#warrantGroupFilter").val();
		
		var templateName = jQuery("input#warrantTemplateNameCitizen").val();
		
		pageObj.field = '';
		
		if (consentTemplates.length == 0 && templateName != '') {
			pageObj.field = -1;	
		} else if(consentTemplates.length > 0 && currentNum != -1) {
			var templateId = consentTemplates[currentNum]['templateId'];
			pageObj.field = templateId;
		}
		
		pageObj.keyword = keyword;			
		ajaxGetTasks();
		
		return false;
	}
	
	function searchAllConsents() {
		var keyword = jQuery("input#tipyCreatedTimeRangeFrom").val();		
		keyword += '|' + jQuery("input#tipyCreatedTimeRangeTo").val();
		keyword += '|' + jQuery("input#tipyRepliedTimeRangeFrom").val();
		keyword += '|' + jQuery("input#tipyRepliedTimeRangeTo").val();
		
		keyword += '|' + jQuery("input#tipyTargetPerson").val();
		keyword += '|' + jQuery("input#tipyRequester").val();
		keyword += '|' + jQuery("input#tipyHandOver").val();
		keyword += '|' + jQuery("input#tipyInformation").val();
		keyword += '|' + jQuery("input#tipyFreeTextSearch").val();
		
		var templateName = jQuery("input#warrantTemplateNameCitizen").val();
		
		pageObj.field = '';
		
		if (consentTemplates.length == 0 && templateName != '') {
			pageObj.field = -1;	
		} else if (consentTemplates.length > 0 && currentNum != -1) {
			var templateId = consentTemplates[currentNum]['templateId'];
			pageObj.field = templateId;
		}
		
		pageObj.keyword = keyword;			
		ajaxGetTasks();
		
		return false;	
	}
	
 	/**
 	 * Reset the search result and clear the keyword
 	 */
	function resetSearch() {
		jQuery("input#keyword").val('');
		jQuery("input#recipient").val('');
		jQuery("input#customer").val('');
		jQuery("input#templateName").val('');
		jQuery('input:checkbox[name="field"]').attr('checked', true);
		pageObj.keyword = '';
		ajaxGetTasks();
	}
	
</script>

<div id="task-manager-wrap">
	<div id="task-manager-tasklist">
		<table class="task-manager-table">
		</table>
	</div>
	<div id="task-manager-search" class="task-manager-operation-part">
		<div id="message-search" class="basic-search" style="display:none;">
			<form name="searchForm" onsubmit="searchTasks(); return false;">		
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
			<form name="searchForm" onsubmit="searchConsents(); return false;">		
				<span class="text-bold" ><spring:message code="consent.recipients" /></span>
				<input type="text" name="recipient" id="recipient" style="width:100px;" />
				<span class="text-bold" ><spring:message code="consent.templateName" /></span>
				<input type="text" name="templateName" id="templateName" style="width:160px;" autocomplete="off" onkeydown="beKeyDown(event)" onkeyup="beKeyUp(event)" onclick="createSuggestDiv('consent-search', 'templateName')" />
				<input type="submit" value="<spring:message code="message.search"/>" />
				<input type="button" value="<spring:message code="message.searchReset"/>" onclick="resetSearch()" />
			</form>
		</div>
		
		<!-- TIVA-13 Selaa asiakkaan suostumuksia -->
		<div id="warrants-search-citizens" class="basic-search" style="display:none; position:relative;">
			<form name="searchForm" onsubmit="searchWarrantsByCitizen(); return false;">		
				<span class="text-bold" ><spring:message code="warrant.recievedWarrants" /></span>
				<input type="text" name="userIdRecieved" id="userIdRecieved" style="width:100px;" />
				<span class="text-bold" ><spring:message code="warrant.sendedWarrants" /></span>
				<input type="text" name="userIdSent" id="userIdSent" style="width:100px;" />
				<span class="text-bold" ><spring:message code="warrant.targetPerson" /></span>
				<input type="text" name="targetPersonUid" id="targetPersonUid" style="width:100px;" />
<%-- 				<span class="text-bold" ><spring:message code="warrant.templateName" /></span> --%>
<!-- 				<input type="text" name="warrantTemplateNameCitizen" id="warrantTemplateNameCitizen" style="width:160px;" autocomplete="off" onkeydown="beKeyDown(event)" onkeyup="beKeyUp(event)" onclick="createSuggestDiv('warrants-search-citizens', 'warrantTemplateNameCitizen')" /> -->
				<input type="submit" value="<spring:message code="message.search"/>" />
				<input type="button" value="<spring:message code="message.searchReset"/>" onclick="resetSearch()" />
			</form>
		</div>
		
		<!-- TIVA-14 Selaa asian suostumuksia -->
		<div id="warrants-search-warrants" class="basic-search" style="display:none; position:relative;">
			<form name="searchForm" onsubmit="searchWarrantsByTemplate(); return false;">		
				<span class="text-bold" ><spring:message code="warrant.templateName" /></span>
				<input type="text" name="warrantTemplateName" id="warrantTemplateName" style="width:160px;" autocomplete="off" onkeydown="beKeyDown(event)" onkeyup="beKeyUp(event)" onclick="createSuggestDiv('warrants-search-warrants', 'warrantTemplateName')" />
				
<%-- 				<span style="display: hidden;" class="text-bold" ><spring:message code="warrant.groupFilter" /></span> --%>
<%-- 				<input style="display: hidden;" type="text" name="warrantGroupFilter" id="warrantGroupFilter" style="width:100px;" /> --%>
				<input type="submit" value="<spring:message code="message.search"/>" />
				<input type="button" value="<spring:message code="message.searchReset"/>" onclick="resetSearch()" />
			</form>
		</div>
		
		<!-- TIVA-18 Selaa tietopyyntöjä -->
		<div id="consentsSearchAllConsents" class="basic-search" style="display:none; position:relative;">
			<form name="searchForm" onsubmit="searchAllConsents(); return false;">		
			
				<p class="searchTimeRange">
					<span class="text-bold searchTitle" ><spring:message code="tipy.timeRangeSend" /></span>
					<input class="searchTime" type="text" name="tipyCreatedTimeRangeFrom" id="tipyCreatedTimeRangeFrom"  /> - 
					<input class="searchTime" type="text" name="tipyCreatedTimeRangeTo" id="tipyCreatedTimeRangeTo" />
					
					<span class="text-bold searchTitle" ><spring:message code="tipy.timeRangeReplied" /></span>
					<input class="searchTime" type="text" name="tipyRepliedTimeRangeFrom" id="tipyRepliedTimeRangeFrom"  /> - 
					<input class="searchTime" type="text" name="tipyRepliedTimeRangeTo" id="tipyRepliedTimeRangeTo" />
				</p>
				
				<p class="searchMisc">
					<span class="text-bold searchTitle"><spring:message code="tipy.targetPerson" /></span>
					<input type="text" name="tipyTargetPerson" id="tipyTargetPerson" style="width:200px;" />
					
					<span class="text-bold searchTitle"><spring:message code="tipy.requester" /></span>
					<input type="text" name="tipyRequester" id="tipyRequester" style="width:200px;" />
				</p>

				<p class="searchMisc">					
					<span class="text-bold searchTitle"><spring:message code="tipy.handOver" /></span>
					<input type="text" name="tipyHandOver" id="tipyHandOver" style="width:200px;" />

					<span class="text-bold searchTitle"><spring:message code="tipy.information" /></span>
					<input type="text" name="tipyInformation" id="tipyInformation" style="width:200px;" />
				</p>
				<p class="searchMisc">

					<span class="text-bold searchTitle"><spring:message code="tipy.freeTextSearch" /></span>
					<input type="text" name="tipyFreeTextSearch" id="tipyFreeTextSearch" style="width:200px;" />
				</p>
				<p class="searchMisc searchButtonArea">
					<input type="submit" value="<spring:message code="message.search"/>" />
					<input type="button" value="<spring:message code="message.searchReset"/>" onclick="resetSearch()" />
				</p>
			</form>
		</div>
		
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<div id="task-manager-operation-page"></div>
		<div id="task-manager-operation-loading"><spring:message code="page.loading"/></div>
	</div>
</div>
