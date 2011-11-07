<%@ include file="init.jsp"%>
<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>

<script type="text/javascript" src="<%= request.getContextPath() %>/js/taskManagerResizer.js"></script>
<script type="text/javascript"> 
	/* the times that iframe loads different srcs */
	var loadingTimes = 0;
	/* the source url that iframe loads at first time */
	var firstUrl = "";	
	
	/**
	 * Returns to the main portlet page
	 */
	function returnMainPage() {
		var url = "<%= homeURL %>";
		url = formatUrl(url);
		window.location = url;
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
	 * Checks the operation of forms finished or not. Usually the form operation is
	 * finished if the source url forwards to another url such as empty.jsp
	 */
	function checkForm() {
		var url = jQuery('#taskform').attr("src");
		
		if(loadingTimes == 0) {
			firstUrl = url;
		}
		
		if(firstUrl != url) {
			returnMainPage();
		}
		
		loadingTimes += 1;
	}
	
	function getKokuServicesEndpoints() {
		var url="/palvelut-portlet/Services";
		return jQuery.ajax( {
			url: url,  
			type: "POST", 
		    dataType: "html",
			async: false 
		}).responseText;
	}
	
	/* In EPP portlet won't expand fully because PORTLET-FRAGMENT has inline style width: 770px 
		Unfortunately there is no easy way to change this size using CSS. */
	function eppHack() {		
		jQuery('#task-manager-wrap').parent().css('width', '100%');		
	}
	
</script>
<div id="task-manager-wrap">
	<div id="task-manager-tasklist">
		<iframe src="<c:out value="${tasklink}" />" id="taskform" name="taskform" style="width:100%; height:100%" frameborder="0" scrolling="no" onload="checkForm()" ></iframe>
		<script type="text/javascript">
			startResizer("taskform");
			eppHack();
		</script>
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="task.return"/>" onclick="returnMainPage()" />
	</div>
</div>

