<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page import="javax.portlet.WindowState" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 

<portlet:defineObjects />

<portlet:resourceURL var="ajaxURL" id="getState">
</portlet:resourceURL>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.5.2.min.js" ></script>
<script type="text/javascript"> 
/* the times that iframe loads different srcs */
var loadingTimes = 0;
/* the source url that iframe loads */
var formUrl = "${tasklink}";

var taskId = getTaskId(formUrl);
var taskType = getTaskType(formUrl);
var refreshTimer;

jQuery(document).ready(function(){
	
	if(taskType == 'PATask' || taskType == 'Notification') {
		removeRefreshTimer();
		setRefreshTimer();
	} else { // for PIPATask process
		jQuery('#taskform').load(function(){
			if(loadingTimes > 1) {
				setTimeout('closeWindow()', 500);
			}
			
			loadingTimes += 1;
		});
	}		
});

/**
 * Set auto refresh timer, which checks the task status automatically
 */
function setRefreshTimer() {
	refreshTimer = setInterval('ajaxGetTaskStatus()', 1000);
}

/**
 * Remove the auto refresh timer
 */
function removeRefreshTimer() {
	clearInterval(refreshTimer);
}

/**
 * Execute ajax query to get task status in Post way, and parse the Json format response
 */
function ajaxGetTaskStatus() {
	var url="<%= ajaxURL %>";
	url = formatUrl(url);

	jQuery.post(url, {taskId:taskId}, function(data) {
		var obj = jQuery.parseJSON(data);
		var json = obj.response;		
		taskStatus = json["taskState"];
		
		if(taskStatus == 'COMPLETED') {
			setTimeout('closeWindow()', 500);
		}
	});
	
}
/**
 * Returns to the main portlet page
 */
function closeWindow() {
	window.close();
}


function getTaskId(url) {
	var from = url.indexOf("id=");
	var to = url.indexOf("type=");	
	var taskId = url.substring(from+3, to-1);
	
	return taskId;
}

function getTaskType(url) {
	var from = url.indexOf("type=");
	var to = url.indexOf("url=");	
	var taskType = url.substring(from+5, to-1);
	
	return taskType;
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
<div id="frameform" style="position:fixed; top:0px; left:0px; right:0px; bottom:0px;">
<iframe src="<c:out value="${tasklink}" />" id="taskform" name="taskform" style="width:100%; height:100%;" frameborder="0" scrolling="auto" ></iframe>
</div>



