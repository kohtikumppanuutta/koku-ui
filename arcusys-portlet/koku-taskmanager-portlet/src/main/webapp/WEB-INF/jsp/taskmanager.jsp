<%@ include file="init.jsp" %>

<portlet:resourceURL var="ajaxURL" id="getTask">
</portlet:resourceURL>

<portlet:renderURL var="formURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="taskform" />
	<portlet:param name="tasklink" value= "CONSTANT_TASK_FORM_LINK" />
	<portlet:param name="currentPage" value= "CONSTANT_TASK_CURRENT_PAGE" />
	<portlet:param name="taskType" value= "CONSTANT_TASK_TASK_TYPE" />
	<portlet:param name="keyword" value= "CONSTANT_TASK_KEYWORD" />
	<portlet:param name="orderType" value= "CONSTANT_TASK_ORDER_TYPE" />
</portlet:renderURL>

<portlet:resourceURL var="popupURL" id="getForm">
	<portlet:param name="myaction" value="popup" />
	<portlet:param name="tasklink" value= "CONSTANT_TASK_FORM_LINK" />
</portlet:resourceURL>

<portlet:resourceURL var="formRenderURL" id="createFormRenderUrl">
</portlet:resourceURL>

<portlet:resourceURL var="popupRenderURL" id="createPopupRenderUrl">
</portlet:resourceURL>

<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>

<%
	/* Parses the parent path url from the portlet ajaxURL */
	
	String defaultPath = "";

	int pos = ajaxURL.indexOf("default");
	if (pos > -1) { // for Jboss portal
		defaultPath = ajaxURL.substring(0, pos+7);		
	} else { // for Gatein portal
		int pos1 = ajaxURL.indexOf("classic");
		defaultPath = ajaxURL.substring(0, pos1+7);
	}
%>

<%@ include file="js_koku_navigation_helper.jspf" %>

<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.5.2.min.js"></script>
<script type="text/javascript"> 
/*
 * Handle action for task manager
 * @Author: Jinhua Chen
 */
 	var tasksPerPage = <%= TaskUtil.PAGE_NUMBER %>
	var tokenStatus = "${tokenStatus}";
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
		if (tokenStatus == 'VALID') {
			ajaxGetTasks();		
			resetRefreshTimer();
		} else {
			var message = "<spring:message code="error.invalidToken" />";
			showErrorMessage(message);
		}
		
		/* remove the timer when user is operating on the page */
		jQuery('#task-manager-wrap').click(function(){
			resetRefreshTimer();
	    });
				
	});
	
	/**
	 * Handle the event when user clicks the field to change task type. Add the
	 * onmousemove and onmouse out events for changing background, and click 
	 * event to start a new task query
	 */
	function changeTasks() {
		var taskLi = jQuery('#task-manager-navi ul li');

		for ( var i = 0; i < taskLi.length; i++) {
			jQuery(taskLi[i]).addClass('task-bg-normal');
			
			if(i == pageObj.taskTypeNum)
				jQuery(taskLi[i]).addClass('task-bg-focus');
			taskLi[i].i = i;
			
			taskLi[i].onmousemove = function() {
				if(this.i != pageObj.taskTypeNum)
					jQuery(this).addClass('task-bg-focus');
			}

			taskLi[i].onmouseout = function() {
				if(this.i != pageObj.taskTypeNum)				
					jQuery(this).removeClass('task-bg-focus');
			}
			
			taskLi[i].onclick = function() {
				jQuery(taskLi).removeClass('task-bg-focus');
				jQuery(this).addClass('task-bg-focus');
				getTasks(this.i);
			}
		}
	}

	/**
	 * Set task query parameters and execute task query in Ajax way
	 */
	function getTasks(type) {
		pageObj.setTaskParams(type); // set taskType and related initialization		
		ajaxGetTasks();
	}
	
	function escapeHTML(value) {
	    return value.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
	}

	/**
	 * Execute ajax query in Post way, and parse the Json format response, and
	 * then create tasks in table and task page filed.
	 */
	function ajaxGetTasks() {
		if(tokenStatus != 'VALID') {
			return;
		}
		
		var url="<%= ajaxURL %>";
		url = formatUrl(url);

		jQuery.post(url, {page:pageObj.currentPage, taskType:pageObj.taskType, 
			keyword:pageObj.keyword, orderType:pageObj.orderType}, function(data) {
			var obj = jQuery.parseJSON(data);
			var json = obj.response;
			tokenStatus = json["tokenStatus"];
			
			if (tokenStatus == 'VALID') {
				pageObj.totalPages = json["totalPages"];
				pageObj.totalItems = json["totalItems"];
				var tasks = json["tasks"];
				var editable = json["editable"];
				var taskHtml = createTasksTable(tasks, editable);
				jQuery('#task-manager-tasklist').html(taskHtml);
				decorateTable();
				var pageHtml = createTasksPage(pageObj, tasksPerPage);
				jQuery('#task-manager-operation-page').html(pageHtml);
			} else {
				var message = "<spring:message code="error.invalidToken" />";
				showErrorMessage(message);
			}
		});
	}

	/**
	 * Create tasks table in Html
	 */
	function createTasksTable(tasks, editable) {
		var taskHtml = "";
		var formLink = "";
		
		taskHtml = '<table class="task-manager-table">'
				+ '<tr class="task-manager-table trheader">'
				+ '<td class="messgeItem senderName"><a href="javascript:void(0)" onclick="orderTask(\'senderName\')"><spring:message code="task.senderName" /></a></td>'
				+ '<td class="messgeItem description"><a href="javascript:void(0)" onclick="orderTask(\'description\')"><spring:message code="task.description" /></a></td>';
				
		<c:if test="<%= Boolean.valueOf(editable) %>">
			taskHtml += '<td><spring:message code="task.edit" /></td>';
		</c:if>
		
		taskHtml += '<td class="messageItem creationDate"><a href="javascript:void(0)" onclick="orderTask(\'creationDate\')"><spring:message code="task.creationDate" /></a></td>'								
				 + '</tr> ';
				 
		for ( var i = 0; i < tasks.length; i++) {
			formLink = createFormLink(tasks[i]["link"], tasks[i]["description"]);
			
			if((i+1)%2 == 0) {
				taskHtml += '<tr class="evenRow">';
			}else {
				taskHtml += '<tr>';
			}
			taskHtml += '<td class="messgeItem senderName">' + tasks[i]["senderName"] + '</td>';
			taskHtml += '<td class="messgeItem description">' + formLink + '</td>';
			if (editable == true) {
				taskHtml += '<td><div><spring:message code="task.edit" /></div></td>';	
			}
			
			taskHtml += '<td class="messageItem creationDate">' + escapeHTML(tasks[i]["creationDate"]) + '</td> </tr>';
		}

		taskHtml += '</table>';

		return taskHtml;
	}
	
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
			
			tr[i].onclick = function() {
				tr.removeClass('clickRow');
				jQuery(this).addClass('clickRow');
			}
		}
	}
	
	/**
	 * Return task state string for different language
	 */
	function getTaskState(state) {

		//var lang = "${pageContext.request.locale.language}";
		var newState = '';
		
		if(state == 'READY') {
			newState = "<spring:message code="task.readyState" />";	
		}
		
		return newState;
	}
	
	/**
	 * Create initial tasks table Html
	 */
	function createInitTable() {
		var taskHtml = "";
		taskHtml = '<table class="task-manager-table">'
				+ '<tr class="task-manager-table trheader">'
				+ '<td class="messageItem senderName"><spring:message code="task.senderName" /></td>';
				+ '<td class="messageItem description"><spring:message code="task.description" /></td>';
				
		<c:if test="<%= Boolean.valueOf(editable) %>">
			taskHtml += '<td><spring:message code="task.edit" /></td>';
		</c:if>
		taskHtml += '<td class="messageItem creationDate"><spring:message code="task.creationDate" /></td>'								
				  + '</tr></table>';
				
		return taskHtml;
	}
	
	<%  //for jboss portal
	if(defaultPath.contains("default")) { %>
	
	function showForm(link) {
		var formUrl = "<%= formURL %>";
		var formUrl = formUrl.replace("CONSTANT_TASK_FORM_LINK", escape(link));
		var formUrl = formUrl.replace("CONSTANT_TASK_CURRENT_PAGE", pageObj.currentPage);
		var formUrl = formUrl.replace("CONSTANT_TASK_TASK_TYPE", pageObj.taskType);
		var formUrl = formUrl.replace("CONSTANT_TASK_KEYWORD", pageObj.keyword);
		var formUrl = formUrl.replace("CONSTANT_TASK_ORDER_TYPE", pageObj.orderType);
		
		window.location = formUrl;	
	}
	
	/**
	 * Show task form in pop up window
	 */
	function popupTaskForm(formLink) {
		var w = 900;
		var h = 650;
		var left = (screen.width/2)-(w/2);
		var top = (screen.height/2)-(h/2);
		
		var popupUrl = "<%= popupURL %>";
		var popupUrl = popupUrl.replace("CONSTANT_TASK_FORM_LINK", escape(formLink));
		var pWindow = window.open(popupUrl, 'popwindow','scrollbars=no, resizable=yes, width='+w+', height='+h+', top='+top+', left='+left);
		var popupObj = new popupWindow(pWindow);
		popupObj.run();
	}
	
	<%}else{ // for gatein %>
		// Creates a renderURL by ajax, to show the detailed message page 
		function showForm(formLink) {
			var url="<%= formRenderURL %>";
			url = formatUrl(url);
			
			jQuery.post(url, {tasklink:formLink, currentPage:pageObj.currentPage, taskType:pageObj.taskType, 
				keyword:pageObj.keyword, orderType:pageObj.orderType}, function(data) {
				var obj = jQuery.parseJSON(data);
				var json = obj.response;
				var renderUrl = json["renderUrl"];
				window.location = renderUrl;
			});
		}
		
		function popupTaskForm(formLink) {
			var w = 900;
			var h = 650;
			var left = (screen.width/2)-(w/2);
			var top = (screen.height/2)-(h/2);
			var url = "<%= popupRenderURL %>";
			url = formatUrl(url);
			
			jQuery.post(url, {tasklink:formLink, currentPage:pageObj.currentPage, taskType:pageObj.taskType, 
				keyword:pageObj.keyword, orderType:pageObj.orderType}, function(data) {
				var obj = jQuery.parseJSON(data);
				var json = obj.response;
				var renderUrl = json["renderUrl"];
				var pWindow = window.open(renderUrl, 'popwindow','scrollbars=no, resizable=yes, width='+w+', height='+h+', top='+top+', left='+left);
				var popupObj = new popupWindow(pWindow);
				popupObj.run();
			});
		}
	
	<%}%>
	/**
	 * Create task form Html link for different open form types
	 */
	function createFormLink(link, description) {
		var linkHtml;

		/* 3 type values, 1: in portlet, 2: new window, 3: pop-up window */
		if(configObj.openForm == '1') {
			/* save the page parameters in session for returning back in order to keep the page unchanged*/
			linkHtml = '<a href="javascript:void(0)" onclick="showForm(\''+ link + '\')">';
		}else if(configObj.openForm == '2') {
			linkHtml = '<a href="' + link + '" target="_blank">';
		}else if(configObj.openForm == '3' || configObj.openForm == 'task') {
			linkHtml = '<a href="javascript:void(0)" onclick="popupTaskForm(\'' + link + '\')">';
		}
		
		if(description == '') { // no description for the task
			description = "<spring:message code="task.noDescription" />";
		}
		
		linkHtml += escapeHTML(description) + '</a>';
		
		return linkHtml;
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
		
		if(currentPage != '') {
			pageObj.currentPage = parseInt(currentPage);
			pageObj.taskType = taskType;
			pageObj.keyword = keyword;
			pageObj.orderType = orderType;
			pageObj.taskTypeNum = getTaskTypeNum(taskType);
		}
		
	}
	
	/**
	 * Get task type number for the value in tasks type tab 
	 */
	function getTaskTypeNum(taskType) {		
		var taskTypeNum = 0;
		
		if(taskType == 'task') {
			taskTypeNum = 0;
		}else if(taskType == 'notification') {
			taskTypeNum = 1;
		}else if(taskType == 'process') {
			taskTypeNum = 2;
		}
		
		return taskTypeNum;
	}
	/**
	 * Config object to handle parameters in configuration mode
	 */	
	 function config() {
			this.taskFilter = "<%= taskFilter %>";
			this.notifFilter = "<%= notifFilter %>";
			this.refreshDuration = "<%= refreshDuration %>";
			/* 3 type values, 1: in portlet, 2: new window, 3: pop-up window */
			this.openForm = "<%= openForm %>";
		}
	/**
	 * paging object to handle page operations
	 */
	function paging() {
		this.currentPage = 1;
		this.totalPages = 1;
		this.totalItems;
		/* 3 types: task, notification, process */
		this.taskType = '<%= defaultTaskType %>';
		/* 0:task, 1:notification, 2: process */
		this.taskTypeNum = 0;
		/* keyword for searching and filter */
		this.keyword = configObj.taskFilter;
		/* 6 types: by description_desc, by description_asc, by state_desc, 
		by state_asc, by creationDate_desc, by creationDate_asc */
		this.orderType = 'creationDate_desc'; 
	}

	/**
	 * Set task type and do initialization to update task list
	 */
	paging.prototype.setTaskParams = function(type) {
		this.taskTypeNum = type;
		
		if (this.taskTypeNum == 0) {
			this.taskType = 'task';
			this.keyword = configObj.taskFilter;
		} else if (this.taskTypeNum == 1) {
			this.taskType = 'notification';
			this.keyword = configObj.notifFilter;
		} else if (this.taskTypeNum == 2) {
			this.taskType = 'process';
			this.keyword = '';
			this.setProcessOrderType();
		}
		
		this.currentPage = 1;
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
	 * Show/hide search user interface
	 */
	function showSearchUI() {
		jQuery('#task-manager-search').toggle(100);
	}

	/**
	 * Perform search tasks
	 */
	function searchTasks() {
		var keyword = jQuery("input#keyword").val();
		pageObj.keyword = keyword;
		ajaxGetTasks();
		return false;
	}
	
 	/**
 	 * Reset the search result and clear the keyword
 	 */
	function resetSearch() {
		jQuery("input#keyword").val('');
		var taskHtml = createInitTable();
		jQuery('#task-manager-tasklist').html(taskHtml);
	}
	
	/**
	 * Popup window object, which checks the status itself. If the popup window
	 * is closed, then refresh the task list to be updated.
	 */
	 function popupWindow(pWindow) {
		this.pWindow = pWindow;
		this.timer;
		this.duration = 500;
		this.check = popupCheck;
		this.run = popupRun;
	}
		
	function popupRun() {
		var self = this;
		this.timer = setInterval(function(){self.check();}, this.duration);
	}

	function popupCheck() {
		if(this.pWindow.closed == true) {
			ajaxGetTasks();
			clearInterval(this.timer);
		}
	}
	
	/**
	 * Create task manager operation part including changing page number and search field
	 */
	function createTasksPage(pageObj, tasksPerPage) {
		var pageHtml = '<ul>'
					 + '<li><input type="button" value="<spring:message code="task.search"/>"  onclick="showSearchUI()" /></li>'
					 + '<li><a><img src="<%= request.getContextPath() %>/images/first.gif" onclick="movePage(\'first\')"/></a></li>'
					 + '<li><a><img src="<%= request.getContextPath() %>/images/prev.gif" onclick="movePage(\'previous\')"/></a></li>'
					 + '<li><spring:message code="task.page"/> ' + pageObj.currentPage + '/' + pageObj.totalPages + '</li>'
					 + '<li><a><img src="<%= request.getContextPath() %>/images/next.gif" onclick="movePage(\'next\')"/></a></li>'
					 + '<li><a><img src="<%= request.getContextPath() %>/images/last.gif" onclick="movePage(\'last\')"/></a></li>'
					 + '<li><spring:message code="task.displaying"/> ' + createDisplayingTasksNum(pageObj, tasksPerPage)  + '</li>'
					 + '</ul>';
		return pageHtml;
	}
	
	/**
	 * Show error message to inform user
	 */
	function showErrorMessage(message) {
		var msgHtml = '<div class="task-error-message" >' + escapeHTML(message) + '</div>';
		jQuery('#task-manager-operation-page').html(msgHtml);
	}
	
	/**
	 * Create task statistics information
	 */
	function createDisplayingTasksNum(pageObj, numPerPage) {
		var displayTask;
		var startid, endid;
		// var numPerPage = 5;
		
		if(parseInt(pageObj.totalItems) == 0) {
			return "<spring:message code="task.noItems"/>";
		}
		
		startid = (pageObj.currentPage-1)*numPerPage + 1;
		
		if(pageObj.currentPage < pageObj.totalPages) {
			endid = startid + (numPerPage-1);
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
	 * Order tasks operation
	 */
	function orderTask(type) {
		var orderType = pageObj.orderType;
		var newOrderType;
		
		if (type == 'description') {
			if(orderType == 'description_asc') {
				newOrderType = 'description_desc';
			}else {
				newOrderType = 'description_asc';
			}			
		} else if(type == 'state') {
			if(orderType == 'state_asc') {
				newOrderType = 'state_desc';
			}else {
				newOrderType = 'state_asc';
			}
		} else if(type == 'creationDate') {
			if(orderType == 'creationDate_desc') {
				newOrderType = 'creationDate_asc';
			}else {
				newOrderType = 'creationDate_desc';
			}
		}
		
		pageObj.orderType = newOrderType;
		
		ajaxGetTasks();
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
</script>

<div id="task-manager-wrap">
	<div id="task-manager-tasklist">
		<table class="task-manager-table">
			<tr class="task-manager-table trheader">
				<td class="messgeItem senderName"><spring:message code="task.senderName" /></td>
				<td class="messgeItem description"><spring:message code="task.description" /></td>
				<c:if test="<%= Boolean.valueOf(editable) %>">
					<td><spring:message code="task.edit" /></td>
				</c:if>
				<%-- <td><spring:message code="task.state" /></td> --%>
				<td class="messgeItem creationDate"><spring:message code="task.creationDate" /></td>								
			</tr> 
		</table>
	</div>
	<div id="task-manager-search" class="task-manager-operation-part">
		<form name="searchForm" onsubmit="searchTasks(); return false;">		
			<spring:message code="task.searchKeyword" />
			<input type="text" name="keyword" id="keyword" style="width: 160px;" /> 
			<input type="submit" value="<spring:message code="task.search"/>" />
			<input type="button" value="<spring:message code="task.searchReset"/>" onclick="resetSearch()" />	
		</form>
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<div id="task-manager-operation-page">
		</div>
		<div id="task-manager-operation-loading"><spring:message code="taskPage.loading"/></div>
	</div>
</div>
