<%@ include file="init.jsp"%>
<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>

<script type="text/javascript"> 
	/* the times that iframe loads different srcs */
	var loadingTimes = 0;
	/* the source url that iframe loads at first time */
	var firstUrl = "";
	
	
	/* Global variables for iframe hack */
	var timerOn = 0;
	var iFramePreviousHeight = 0;
	var minHeight = 500;
	
	
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
	
	function startResizer() {
		if (!timerOn) {
			timerOn = 1;
			resizeTimer();
		}
	}
	
	/* Meansure IFrame size */
	function resizeTimer() {
		var iFrameContentHeight = getIFrameBodyHeight();
		if (iFramePreviousHeight != iFrameContentHeight) {
			var newHeight = iFrameContentHeight + 20;
			iFramePreviousHeight = newHeight;
			resizeIFrame(newHeight);			
		}		
		setTimeout("resizeTimer()", 500 );
	}
		
	function resizeIFrame(height) {
		if (height < minHeight) {
			height = minHeight;
		}
		document.getElementById('taskform').style.height = height + "px";
	}
	
	function getIFrameBodyHeight(){
		var iFrame =  document.getElementById('taskform');
		if (iFrame == null) {
			return minHeight;
		}
		var iFrameBody;
		if ( iFrame.contentDocument ) { 
			 /* Firefox */
			 //iFrameBody = iFrame.contentDocument.getElementsById('IntalioInternal_jsxmain');
			  iFrameBody = iFrame.contentDocument.getElementsByTagName('body')[0];
		} else if ( iFrame.contentWindow ) { 
			 /*  InternetExplorer */
			 //iFrameBody = iFrame.contentWindow.document.getElementsById('IntalioInternal_jsxmain');
			  iFrameBody = iFrame.contentWindow.document.getElementsByTagName('body')[0];
		}
		return iFrameBody.scrollHeight;
	}

	
	
</script>
<div id="task-manager-wrap">
	<div id="task-manager-tasklist">
		<iframe src="<c:out value="${tasklink}" />" id="taskform" name="taskform" style="width:100%; height:100%" frameborder="0" scrolling="auto" onload="checkForm()" ></iframe>
		<script type="text/javascript">startResizer();</script>
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="task.return"/>" onclick="returnMainPage()" />
	</div>
</div>

