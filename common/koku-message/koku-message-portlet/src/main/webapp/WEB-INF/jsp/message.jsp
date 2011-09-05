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

<!-- For gatein Portal -->
<portlet:resourceURL var="messageRenderURL" id="createMessageRenderUrl">
</portlet:resourceURL>

<portlet:resourceURL var="requestRenderURL" id="createRequestRenderUrl">
</portlet:resourceURL> 

<portlet:resourceURL var="appointmentRenderURL" id="createAppointmentRenderUrl">
</portlet:resourceURL> 

<portlet:resourceURL var="consentRenderURL" id="createConsentRenderUrl">
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
/*
 * Handle action for task manager
 * @Author: Jinhua Chen
 */
	var refreshTimer; // global refresh timer
	var configObj = new config();
	var pageObj = new paging();	

	jQuery(document).ready(function(){
		suggestUrl = "<%= suggestURL %>";
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

		jQuery.post(url, {page:pageObj.currentPage, taskType:pageObj.taskType, 
			keyword:pageObj.keyword, orderType:pageObj.orderType, field:pageObj.field}, function(data) {
			var obj = eval('(' + data + ')');
			var json = obj.response;
			pageObj.loginStatus = json["loginStatus"];
			
			if(pageObj.loginStatus == 'VALID') {
				pageObj.totalPages = json["totalPages"];
				pageObj.totalItems = json["totalItems"];
				var tasks = json["tasks"];
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
		if(pageObj.taskType == 'req_valid') // for request
			var taskHtml = createRequestsTable(tasks);
		else if(pageObj.taskType.indexOf('app') == 0) // for appointment
			var taskHtml = createAppoitmentsTable(tasks);
		else if(pageObj.taskType.indexOf('cst') == 0) // for consent
			var taskHtml = createConsentsTable(tasks);
		else // for message
			var taskHtml = createMessagesTable(tasks);
		
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
				+ '<td class="from">' + formatSender() + '</td>'
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
				+ '<td class="from">' + '<spring:message code="message.from" />' + '</td>'
				+ '<td>' + '<spring:message code="message.subject" />' + '</td>'
				+ '<td><spring:message code="message.description" /></td>'
				+ '<td><spring:message code="message.status" /></td>'
				+ '</tr>';
				 
		for ( var i = 0; i < tasks.length; i++) {
			
			if((i+1)%2 == 0) {
				taskHtml += '<tr class="evenRow">';	
			}else {
				taskHtml += '<tr>';
			}
			
			taskHtml += '<td class="choose">' + '<input type="checkbox" name="message" value="' + tasks[i]["appointmentId"] + '_' + tasks[i]["targetPerson"] + '" />' + '</td>'
					 + '<td class="messageItem" onclick="showAppointment(\''+ tasks[i]["appointmentId"] + '\',\'' + tasks[i]["targetPerson"] + '\')" >' + tasks[i]["sender"] + '</td>'
					 + '<td class="messageItem" onclick="showAppointment(\''+ tasks[i]["appointmentId"] + '\',\'' + tasks[i]["targetPerson"] + '\')" >' + formatSubject(tasks[i]["subject"]) + '</td>'
					 + '<td class="messageItem" onclick="showAppointment(\''+ tasks[i]["appointmentId"] + '\',\'' + tasks[i]["targetPerson"] + '\')" >' + tasks[i]["description"] + '</td>'
					 + '<td class="messageItem" onclick="showAppointment(\''+ tasks[i]["appointmentId"] + '\',\'' + tasks[i]["targetPerson"] + '\')" >' + tasks[i]["status"] + '</td>'
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
		
		if(pageObj.taskType == "cst_assigned_citizen") {
			taskHtml = '<table class="task-manager-table">'
				+ '<tr class="task-manager-table trheader">'
				+ '<td>' + '<spring:message code="consent.requester" />' + '</td>'
				+ '<td>' + '<spring:message code="consent.templateName" />'+ '</td>'
				+ '</tr>';
				 
			for ( var i = 0; i < tasks.length; i++) {
			
				if((i+1)%2 == 0) {
					taskHtml += '<tr class="evenRow" onclick="showConsent(\''+ tasks[i]["consentId"] + '\')">';	
				}else {
					taskHtml += '<tr onclick="showConsent(\''+ tasks[i]["consentId"] + '\')">';
				}
			
				taskHtml += '<td class="messageItem">' + tasks[i]["requester"] + '</td>'
					 	 + '<td class="messageItem">' + tasks[i]["templateName"] + '</td>'
					 	 + '</tr>';
			}

			taskHtml += '</table>';
			
		}else if(pageObj.taskType == "cst_own_citizen") {
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
			
				if((i+1)%2 == 0) {
					taskHtml += '<tr class="evenRow">';	
				}else {
					taskHtml += '<tr>';
				}
			
				taskHtml += '<td class="choose">' + '<input type="checkbox" name="message" value="' + tasks[i]["consentId"] + '" />' + '</td>'
						 + '<td class="messageItem" onclick="showConsent(\''+ tasks[i]["consentId"] + '\')" >' + tasks[i]["requester"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ tasks[i]["consentId"] + '\')" >' + tasks[i]["templateName"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ tasks[i]["consentId"] + '\')" >' + tasks[i]["status"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ tasks[i]["consentId"] + '\')" >' + tasks[i]["approvalStatus"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ tasks[i]["consentId"] + '\')" >' + tasks[i]["createType"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ tasks[i]["consentId"] + '\')" >' + tasks[i]["assignedDate"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ tasks[i]["consentId"] + '\')" >' + tasks[i]["validDate"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ tasks[i]["consentId"] + '\')" >' + tasks[i]["anotherPermitterUid"] + '</td>'
					 	 + '</tr>';
			}

			taskHtml += '</table>';
			
		}else if(pageObj.taskType == "cst_own_employee") {
			taskHtml = '<table class="task-manager-table">'
				+ '<tr class="task-manager-table trheader">'
				+ '<td class="choose"><spring:message code="message.choose" /></td>'
				+ '<td>' + '<spring:message code="consent.requester" />' + '</td>'
				+ '<td>' + '<spring:message code="consent.templateName" />' + '</td>'
				+ '<td>' + '<spring:message code="consent.status" />' + '</td>'
				+ '<td>' + '<spring:message code="consent.approvalStatus" />' + '</td>'
				+ '<td>' + '<spring:message code="consent.createType" />' + '</td>'
				+ '<td>' + '<spring:message code="consent.givenDate" />' + '</td>'
				+ '<td>' + '<spring:message code="consent.validDate" />' + '</td>'
				+ '</tr>';
				 
			for ( var i = 0; i < tasks.length; i++) {
			
				if((i+1)%2 == 0) {
					taskHtml += '<tr class="evenRow">';	
				}else {
					taskHtml += '<tr>';
				}
			
				taskHtml += '<td class="choose">' + '<input type="checkbox" name="message" value="' + tasks[i]["consentId"] + '" />' + '</td>'
						 + '<td class="messageItem" onclick="showConsent(\''+ tasks[i]["consentId"] + '\')" >' + tasks[i]["requester"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ tasks[i]["consentId"] + '\')" >' + tasks[i]["templateName"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ tasks[i]["consentId"] + '\')" >' + tasks[i]["status"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ tasks[i]["consentId"] + '\')" >' + tasks[i]["approvalStatus"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ tasks[i]["consentId"] + '\')" >' + tasks[i]["createType"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ tasks[i]["consentId"] + '\')" >' + tasks[i]["assignedDate"] + '</td>'
					  	 + '<td class="messageItem" onclick="showConsent(\''+ tasks[i]["consentId"] + '\')" >' + tasks[i]["validDate"] + '</td>'
					 	 + '</tr>';
			}

			taskHtml += '</table>';
			
		}
		
		return taskHtml;
	}	
	/* Formats sender field */
	function formatSender() {
		
		if(pageObj.taskType == "msg_inbox" || pageObj.taskType == "msg_archive_inbox")
			return  '<span class="from">' + "<spring:message code="message.from" />" + '</span>';
		else 
			return '<span class="from">' + "<spring:message code="message.receiver" />" + '</span>';	
	}
	/* Formats user field */
	function formatUser(task) {
		
		if(pageObj.taskType == "msg_inbox" || pageObj.taskType == "msg_archive_inbox")
			return '<span class="from">' + task["sender"] + '</span>';
		else 
			return '<span class="from">' + task["recipients"] + '</span>';
		
	}
	/* Formats subject field */
	function formatSubject(subject) {
		if(subject == "")		
			return  "<spring:message code="message.noSubject" />";
		else
			return subject;
	}

	<%  //for jboss portal
	if(defaultPath.contains("default")) { %>
	
	/* Shows detailed message page */
	function showMessage(messageId) {
		var url = "<%= messageURL %>";
		url += "&messageId=" + messageId
		+ "&currentPage=" + pageObj.currentPage
		+ "&taskType=" + pageObj.taskType
		+ "&keyword=" + pageObj.keyword+'|'+pageObj.field
		+ "&orderType=" + pageObj.orderType;
		
		window.location = url;
	}
	/* Shows detailed request page */
	function showRequest(requestId) {
		var url = "<%= requestURL %>";
		url += "&requestId=" + requestId
		+ "&currentPage=" + pageObj.currentPage
		+ "&taskType=" + pageObj.taskType
		+ "&keyword=" + pageObj.keyword+'|'+pageObj.field
		+ "&orderType=" + pageObj.orderType;
		
		window.location = url;
	}
	/* Shows detailed appointment page */
	function showAppointment(appointmentId, targetPerson) {
		var url="";
		
		if(pageObj.taskType == "app_response_employee" || pageObj.taskType == "app_response_citizen") {
			url = "<%= appointmentURL %>";
			url += "&appointmentId=" + appointmentId
					+ "&currentPage=" + pageObj.currentPage
					+ "&taskType=" + pageObj.taskType
					+ "&keyword=" + pageObj.keyword+'|'+pageObj.field
					+ "&orderType=" + pageObj.orderType;
		}else if(pageObj.taskType == "app_inbox_employee"){
			url = "<%= defaultPath %>" + "/Message/NewAppointment" + "?FormID=" + appointmentId;			
		}else if(pageObj.taskType == "app_inbox_citizen") {
			url = "<%= defaultPath %>" + "/Message/OpenAppointment" + "?FormID=" + appointmentId + "&arg1=" + targetPerson;				
		}
		
		window.location = url;				
	}
	/* Shows detailed consent page */
	function showConsent(consentId) {
		var url="";
		
		if(pageObj.taskType == "cst_own_citizen" || pageObj.taskType == "cst_own_employee") {
			url = "<%= consentURL %>";
			url += "&consentId=" + consentId
					+ "&currentPage=" + pageObj.currentPage
					+ "&taskType=" + pageObj.taskType
					+ "&keyword=" + pageObj.keyword+'|'+pageObj.field
					+ "&orderType=" + pageObj.orderType;
		}else if(pageObj.taskType == "cst_assigned_citizen"){
			url = "<%= defaultPath %>" + "/Message/NewConsent" + "?FormID=" + consentId;			
		}
				
		window.location = url;
	}
	
	<%}else{ // for gatein %>
	
	/************************For Gatein Portal start***************************/
	// Creates a renderURL by ajax, to show the detailed message page 
	function showMessage(messageId) {
		var url="<%= messageRenderURL %>";
		url = formatUrl(url);
		
		jQuery.post(url, {messageId:messageId, currentPage:pageObj.currentPage, taskType:pageObj.taskType, 
			keyword:pageObj.keyword+'|'+pageObj.field, orderType:pageObj.orderType}, function(data) {
			var obj = eval('(' + data + ')');
			var json = obj.response;
			var renderUrl = json["renderUrl"];
			window.location = renderUrl;
		});
			
	}
	
	//Creates a renderURL by ajax, to show the detailed request page
	function showRequest(requestId) {
		var url = "<%= requestRenderURL %>";
		url = formatUrl(url);
		
		jQuery.post(url, {requestId:requestId, currentPage:pageObj.currentPage, taskType:pageObj.taskType, 
			keyword:pageObj.keyword+'|'+pageObj.field, orderType:pageObj.orderType}, function(data) {
			var obj = eval('(' + data + ')');
			var json = obj.response;
			var renderUrl = json["renderUrl"];
			window.location = renderUrl;
		});
	}	
	//Creates a renderURL by ajax,to show the detailed appointment page
	function showAppointment(appointmentId, targetPerson) {
		var url="";
		
		if(pageObj.taskType == "app_response_employee" || pageObj.taskType == "app_response_citizen") {
			url = "<%= appointmentRenderURL %>";
			url = formatUrl(url);
			
			jQuery.post(url, {appointmentId:appointmentId, currentPage:pageObj.currentPage, taskType:pageObj.taskType, 
				keyword:pageObj.keyword+'|'+pageObj.field, orderType:pageObj.orderType, targetPerson:targetPerson}, function(data) {
				var obj = eval('(' + data + ')');
				var json = obj.response;
				var renderUrl = json["renderUrl"];
				window.location = renderUrl;
			});
		}else if(pageObj.taskType == "app_inbox_employee"){
			url = "<%= defaultPath %>" + "/Message/NewAppointment" + "?FormID=" + appointmentId;
			window.location = url;
		}else if(pageObj.taskType == "app_inbox_citizen") {
			url = "<%= defaultPath %>" + "/Message/OpenAppointment" + "?FormID=" + appointmentId + "&arg1=" + targetPerson;	
			window.location = url;
		}				
	}
	//Creates a renderURL by ajax,to show the detailed consent page
	function showConsent(consentId) {
		var url="";
		
		if(pageObj.taskType == "cst_own_citizen" || pageObj.taskType == "cst_own_employee") {
			url = "<%= consentRenderURL %>";
			url = formatUrl(url);
			
			jQuery.post(url, {consentId:consentId, currentPage:pageObj.currentPage, taskType:pageObj.taskType, 
				keyword:pageObj.keyword+'|'+pageObj.field, orderType:pageObj.orderType}, function(data) {
				var obj = eval('(' + data + ')');
				var json = obj.response;
				var renderUrl = json["renderUrl"];
				window.location = renderUrl;
			});
		}else if(pageObj.taskType == "cst_assigned_citizen"){
			url = "<%= defaultPath %>" + "/Message/NewConsent" + "?FormID=" + consentId;	
			window.location = url;
		}
						
	}

	/************************For Gatein Portal end****************************/
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
		var taskHtml = "";
		taskHtml = '<table class="task-manager-table">'
				+ '<tr class="task-manager-table trheader">'
				+ '<td class="choose"><spring:message code="message.choose" /></td>'
				+ '<td class="from"><spring:message code="message.receiver" /></td>'
				+ '<td><spring:message code="message.subject" /></td>'		
				+ '<td class="date"><spring:message code="message.received" /></td>'	
				+ '</tr></table>';
				
		return taskHtml;
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
		/*  */
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
		
		if(pageObj.taskType.indexOf('msg') > -1 || pageObj.taskType.indexOf('cst') > -1) {
			pageHtml += '<li><input type="button" value="<spring:message code="message.search"/>"  onclick="showSearchUI()" /></li>';
		}
			
		if(pageObj.taskType == 'msg_inbox' || pageObj.taskType == 'msg_outbox') {
			pageHtml += '<li><input type="button" value="<spring:message code="page.archive"/>"  onclick="archiveMessages()" /></li>';
		}
					
		if(pageObj.taskType == 'cst_own_citizen') {
			pageHtml += '<li><input type="button" value="<spring:message code="consent.revokeSelected"/>"  onclick="revokeConsents()" /></li>';
		}else if(pageObj.taskType.indexOf('msg') > -1) {
			pageHtml += '<li><input type="button" value="<spring:message code="page.removeSelected"/>"  onclick="deleteMessages()" /></li>';
		}else if(pageObj.taskType == 'app_response_employee') {
			pageHtml += '<li><input type="button" value="<spring:message code="appointment.edit"/>"  onclick="editAppointments()" /></li>';
			pageHtml += '<li><input type="button" value="<spring:message code="consent.cancel"/>"  onclick="cancelAppointments()" /></li>';
		}else if(pageObj.taskType == 'app_inbox_employee') {
			pageHtml += '<li><input type="button" value="<spring:message code="consent.cancel"/>"  onclick="cancelAppointments()" /></li>';
		}
			
			
		pageHtml     += '<li><a><img src="<%= request.getContextPath() %>/images/first.gif" onclick="movePage(\'first\')"/></a></li>'
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
	

	function editAppointments() {
		var messageList = [];
		jQuery('input:checkbox[name="message"]:checked').each(function(){
			var value = jQuery(this).val();
			var temp = value.split('_');		
		    messageList.push(temp[0]);
		});
		
		if(messageList.length > 1) {
			alert('<spring:message code="appointment.editError" />');
		}else if(messageList.length == 0){
			return;
		}else if(messageList.length == 1){
			var appointmentId = messageList[0];
			url = "<%= defaultPath %>" + "/Message/NewAppointment" + "?FormID=" + appointmentId;
			window.location = url;
		}
	}
	
	/**
	 * Cancels a list of appointments selected by user
	 */
	function cancelAppointments() {		
		var messageList = [];
		var targetPersons = [];
		jQuery('input:checkbox[name="message"]:checked').each(function(){
			var value = jQuery(this).val();
			var temp = value.split('_');		
		    messageList.push(temp[0]);
		    targetPersons.push(temp[1]);
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
		    messageList.push(jQuery(this).val());
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
	 * Show/hide search user interface
	 */
	function showSearchUI() {
		
		if(pageObj.taskType.indexOf('msg') > -1) { // for message
			jQuery('#message-search').show();
			jQuery('#consent-search').hide();
		}else if(pageObj.taskType.indexOf('cst') > -1) { // for consent
			jQuery('#consent-search').show();
			jQuery('#message-search').hide();
			// createSuggestDiv("consent-search", "templateName");
		}else {
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
		pageObj.field = '';
		
		if(currentNum != -1) {
			var templateId = consentTemplates[currentNum]['suostumuspohjaId'];
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
		<div id="consent-search" class="basic-search" style="display:none;">
			<form name="searchForm" onsubmit="searchConsents(); return false;">		
				<span class="text-bold" ><spring:message code="consent.recipients" /></span>
				<input type="text" name="recipient" id="recipient" style="width:160px;" />
				<span class="text-bold" ><spring:message code="consent.templateName" /></span>
				<input type="text" name="templateName" id="templateName" style="width:160px;" autocomplete="off" onkeydown="beKeyDown(event);" onkeyup="beKeyUp(event);"/>
				<input type="submit" value="<spring:message code="message.search"/>" />
				<input type="button" value="<spring:message code="message.searchReset"/>" onclick="resetSearch()" />
			</form>
		</div>
		
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<div id="task-manager-operation-page"></div>
		<div id="task-manager-operation-loading"><spring:message code="page.loading"/></div>
	</div>
	
	<div id="search_suggest" style="position: absolute; left: 368px; top: 402px; width: 164px; background-color:#ffffff;  z-index:1000; display:none;"> </div>
</div>
