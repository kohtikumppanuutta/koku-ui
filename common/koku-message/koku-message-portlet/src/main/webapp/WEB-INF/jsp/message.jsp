<%@ include file="init.jsp" %>

<portlet:resourceURL var="ajaxURL" id="getTask">
</portlet:resourceURL>

<portlet:resourceURL var="archiveURL" id="archiveMessage">
</portlet:resourceURL>

<portlet:resourceURL var="deleteURL" id="deleteMessage">
</portlet:resourceURL>

<portlet:renderURL var="messageURL" windowState="<%= WindowState.MAXIMIZED.toString() %>" >
	<portlet:param name="myaction" value="showMessage" />
	<portlet:param name="messageId" value= "CONSTANT_TASK_MESSAGE_ID" />
	<portlet:param name="currentPage" value= "CONSTANT_TASK_CURRENT_PAGE" />
	<portlet:param name="taskType" value= "CONSTANT_TASK_TASK_TYPE" />
	<portlet:param name="keyword" value= "CONSTANT_TASK_KEYWORD" />
	<portlet:param name="orderType" value= "CONSTANT_TASK_ORDER_TYPE" />
</portlet:renderURL>

<portlet:renderURL var="requestURL" windowState="<%= WindowState.MAXIMIZED.toString() %>" >
	<portlet:param name="myaction" value="showRequest" />
	<portlet:param name="requestId" value= "CONSTANT_TASK_MESSAGE_ID" />
	<portlet:param name="currentPage" value= "CONSTANT_TASK_CURRENT_PAGE" />
	<portlet:param name="taskType" value= "CONSTANT_TASK_TASK_TYPE" />
	<portlet:param name="keyword" value= "CONSTANT_TASK_KEYWORD" />
	<portlet:param name="orderType" value= "CONSTANT_TASK_ORDER_TYPE" />
</portlet:renderURL>

<script type="text/javascript"> 
/*
 * Handle action for task manager
 * @Author: Jinhua Chen
 */
	var refreshTimer; // global refresh timer
	var configObj = new config();
	var pageObj = new paging();	
	
	jQuery(document).ready(function(){		
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
		
		//changeTasks();
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
		
		pageObj.taskType = getTaskTypeFromNavi();	
		var url="<%= ajaxURL %>";

		jQuery.post(url, {page:pageObj.currentPage, taskType:pageObj.taskType, 
			keyword:pageObj.keyword, orderType:pageObj.orderType, field:pageObj.field}, function(data) {
			var obj = eval('(' + data + ')');
			var json = obj.response;
			pageObj.loginStatus = json["loginStatus"];
			
			if(pageObj.loginStatus == 'VALID') {
				pageObj.totalPages = json["totalPages"];
				pageObj.totalItems = json["totalItems"];
				var tasks = json["tasks"];
				if(pageObj.taskType == 'valid_request')
					var taskHtml = createRequestsTable(tasks);
				else
					var taskHtml = createTasksTable(tasks);
				jQuery('#task-manager-tasklist').html(taskHtml);
				decorateTable();
				var pageHtml = createTasksPage();
				jQuery('#task-manager-operation-page').html(pageHtml);
			}else {
				var message = "<spring:message code="error.unLogin" />";
				showErrorMessage(message);
			}
			

		});
		
	}

	/**
	 * Gets message type from the global variable 'koku_navi_type' in navi portlet
	 */
	function getTaskTypeFromNavi() {
		var type = "inbox"; // default is inbox
		
		if(typeof koku_navi_type == 'undefined' || koku_navi_type == '')
			return type;
		else
			return koku_navi_type;
	}
	
	/**
	 * Create tasks table in Html
	 */
	function createTasksTable(tasks) {
		var taskHtml = "";
		var formLink = "";
		
		taskHtml = '<table class="task-manager-table">'
				+ '<tr class="task-manager-table trheader">'
				+ '<td class="choose"><spring:message code="message.choose" /></td>'
				+ '<td class="from">'+ '<strong>' + formatSender() + '</strong>' + '</td>'
				+ '<td>' + '<strong>' + '<spring:message code="message.subject" />'+ '</strong>' + '</td>'
				+ '<td class="date"><spring:message code="message.received" /></td>'
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
					 + '<td class="date messageItem" onclick="showMessage(\''+ tasks[i]["messageId"] + '\')" >' + tasks[i]["creationDate"] + '</td>'
					 + '</tr>';
		}

		taskHtml += '</table>';

		return taskHtml;
	}
	
	function createRequestsTable(tasks) {
		var taskHtml = "";
		var formLink = "";
		
		taskHtml = '<table class="task-manager-table">'
				+ '<tr class="task-manager-table trheader">'
				+ '<td class="choose"><spring:message code="message.choose" /></td>'
				+ '<td>'+ '<strong>' + 'Subject'+ '</strong>' + '</td>'
				+ '<td>' + '<strong>' + 'Responded'+ '</strong>' + '</td>'
				+ '<td>' + '<strong>' + 'Missed'+ '</strong>' + '</td>'
				+ '<td>' + '<strong>' + 'Start'+ '</strong>' + '</td>'
				+ '<td>' + '<strong>' + 'End'+ '</strong>' + '</td>'
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
	
	function formatSender() {
		
		if(pageObj.taskType == "inbox" || pageObj.taskType == "archive_inbox")
			return  "<spring:message code="message.from" />";
		else 
			return "<spring:message code="message.receiver" />";		
	}
	
	function formatUser(task) {
		
		if(pageObj.taskType == "inbox" || pageObj.taskType == "archive_inbox")
			return task["sender"];
		else 
			return task["recipients"];
		
	}
	
	function formatSubject(subject) {
		if(subject == "")		
			return  "<spring:message code="message.noSubject" />";
		else
			return subject;
	}
	
	function showMessage(messageId) {
		var formUrl = "<%= messageURL %>";
		var formUrl = formUrl.replace("CONSTANT_TASK_MESSAGE_ID", messageId);
		var formUrl = formUrl.replace("CONSTANT_TASK_CURRENT_PAGE", pageObj.currentPage);
		var formUrl = formUrl.replace("CONSTANT_TASK_TASK_TYPE", pageObj.taskType);
		var formUrl = formUrl.replace("CONSTANT_TASK_KEYWORD", pageObj.keyword+'|'+pageObj.field);
		var formUrl = formUrl.replace("CONSTANT_TASK_ORDER_TYPE", pageObj.orderType);
		
		window.location = formUrl;
	}
	
	function showRequest(requestId) {
		var formUrl = "<%= requestURL %>";
		var formUrl = formUrl.replace("CONSTANT_TASK_MESSAGE_ID", requestId);
		var formUrl = formUrl.replace("CONSTANT_TASK_CURRENT_PAGE", pageObj.currentPage);
		var formUrl = formUrl.replace("CONSTANT_TASK_TASK_TYPE", pageObj.taskType);
		var formUrl = formUrl.replace("CONSTANT_TASK_KEYWORD", pageObj.keyword+'|'+pageObj.field);
		var formUrl = formUrl.replace("CONSTANT_TASK_ORDER_TYPE", pageObj.orderType);
		
		window.location = formUrl;
	}
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
	
	
	/**
	 * Create initial tasks table Html
	 */
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
		var fields = field.split(' ');
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
		/* 3 types: task, notification, process */
		this.taskType = 'inbox';
		//this.taskType = parseParameter('naviType');
		/* keywords for searching and filter fields */
		this.keyword = '';
		this.field = '1 2 3 4';
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
		pageHtml += '<li><input type="button" value="<spring:message code="message.search"/>"  onclick="showSearchUI()" /></li>';
		
		if(pageObj.taskType == 'inbox' || pageObj.taskType == 'outbox')
			pageHtml += '<li><input type="button" value="<spring:message code="page.archive"/>"  onclick="archiveMessages()" /></li>';
			
			pageHtml +='<li><input type="button" value="<spring:message code="page.removeSelected"/>"  onclick="deleteMessages()" /></li>'
					 + '<li><a><img src="<%= request.getContextPath() %>/images/first.gif" onclick="movePage(\'first\')"/></a></li>'
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
	 * Create 'Return' button Html
	 */
	function createFormPage() {
		var pageHtml = '<ul><li><input type="button" value="Return" onclick="returnPage()" /></li></ul>';
		return pageHtml;
	}
	
	/**
	 * Return from task form page to main page
	 */
	function returnPage() {
		setRefreshTimer();
		ajaxGetTasks();
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
		
		var conf = confirm("Are you sure to delete messages?");
		if(!conf)	return;
		
		var url="<%= deleteURL %>";
		
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
		jQuery('#task-manager-search').toggle('fast');
	}
	
	function showAdvancedSearchUI() {
		jQuery('#task-manager-search .basic-search').hide();
		jQuery('#task-manager-search .advanced-search').show();
	}
	
	function hideAdvancedSearchUI() {
		jQuery('#task-manager-search .basic-search').show();
		jQuery('#task-manager-search .advanced-search').hide();
	}
	
	/**
	 * Perform search tasks
	 */
	function searchTasks() {
		var keyword = jQuery("input#keyword").val();
		pageObj.keyword = keyword;	
		var field = '';
		jQuery('input:checkbox[name="field"]:checked').each(function(){
			field += jQuery(this).val() + ' ';
		});
		/* get rid of the last space letter*/
		if(field.length > 0)  field = field.substring(0, field.length-1);
		else return false;
		
		pageObj.field = field;	
		ajaxGetTasks();
		
		return false;
	}
	
	function advancedSearchTasks() {
		var from = jQuery('input:text[name="keyword_from"]').val();
		var to = jQuery('input:text[name="keyword_to"]').val();
		var subject = jQuery('input:text[name="keyword_subject"]').val();
		var content = jQuery('input:text[name="keyword_content"]').val();		
		pageObj.keywords = [from, to, subject, content];			
		ajaxGetTasks();
		
		return false;
	}
 	/**
 	 * Reset the search result and clear the keyword
 	 */
	function resetSearch() {
		jQuery("input#keyword").val('');
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
		<div class="basic-search">
			<form name="searchForm" onsubmit="searchTasks(); return false;">		
				<span class="message-keyword" ><spring:message code="message.searchKeyword" /></span>
				<input type="text" name="keyword" id="keyword" style="width:160px;" />
				<input type="submit" value="<spring:message code="message.search"/>" />
				<input type="button" value="<spring:message code="message.searchReset"/>" onclick="resetSearch()" />
				<span id="search-fields" >
					<input type="checkbox" checked="checked" name="field" value="1" />From
					<input type="checkbox" checked="checked" name="field" value="2" />To
					<input type="checkbox" checked="checked" name="field" value="3" />Subject
					<input type="checkbox" checked="checked" name="field" value="4" />Content
				</span>	
			</form>
		</div>
		<!-- 
		<span><a href="javascript:void(0)" onclick="showAdvancedSearchUI()">Advanced</a></span>
		<div class="advanced-search">
			<form name="searchForm" onsubmit="advancedSearchTasks(); return false;">			
				<table class="search-table">
					<tr><td class="search-td-1">From</td><td class="search-td-2"><input type="text" name="keyword_from" style="width:100%;" /></td>
						<td class="search-td-1">Subject</td><td class="search-td-2"><input type="text" name="keyword_subject" style="width:100%;" /></td>
					</tr>
					<tr><td class="search-td-1">To</td><td class="search-td-2"><input type="text" name="keyword_to" style="width:100%;" /></td>
						<td class="search-td-1">Content</td><td class="search-td-2"><input type="text" name="keyword_content" style="width:100%;" /></td>
					</tr>
				</table>		
				<input type="submit" value="<spring:message code="message.search"/>" />
				<input type="button" value="<spring:message code="message.searchReset"/>" onclick="resetSearch()" />
				<span><a href="javascript:void(0)" onclick="hideAdvancedSearchUI()">Hide search options</a></span>	
			</form>
		</div>
		 -->
	</div>
	
	<div id="task-manager-operation" class="task-manager-operation-part">
		<div id="task-manager-operation-page"></div>
		<div id="task-manager-operation-loading"><spring:message code="page.loading"/></div>
	</div>
</div>
