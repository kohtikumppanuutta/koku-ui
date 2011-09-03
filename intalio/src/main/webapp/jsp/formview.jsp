<%@ include file="/jsp/init.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<portlet:resourceURL var="ajax" id="intalioAjax"></portlet:resourceURL>

<%-- Load jQuery --%>
<%-- This just temporary fix, because jQuery should be made available from theme or by portal not in portlet. --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.5.2.min.js"></script>

<script type="text/javascript">

	var timerOn = 0;
	var iFramePreviousHeight = 0;
	var minHeight = 700;
	var iFrameId = '<portlet:namespace />xforms_iframe';

	function startResizer() {
		if (!timerOn) {
			timerOn = 1;
			resizeTimer();
		}
	}
	
	/* Meansure IFrame size */
	function resizeTimer() {
		var iFrameContentHeight = getIFrameBodyHeight(iFrameId);
		if (iFramePreviousHeight != iFrameContentHeight) {
			var newHeight = iFrameContentHeight + 20;
			resizeIFrame(iFrameId, newHeight);
			iFramePreviousHeight = getIFrameBodyHeight(iFrameId);
		}		
		setTimeout("resizeTimer()", 500 );
	}
		
	function resizeIFrame(id, height) {
		if (height < minHeight) {
			height = minHeight;
		}
		document.getElementById(id).style.height = height+'px';
	}
	
	function getIFrameBodyHeight(id) {
		var iFrame =  document.getElementById(id);
		if (iFrame == null) {
			return minHeight;
		}
		var iFrameBody;
		 var oBody;
		 var oFrame;
		 
		if (!isIE()) { 
			 /* Firefox, Safari, Chrome etc. */
			 //iFrameBody = iFrame.contentDocument.getElementsById('IntalioInternal_jsxmain');
			  iFrameBody = iFrame.contentDocument.getElementsByTagName('body')[0];
		} else { 
			 /*  InternetExplorer */
			<%-- //iFrameBody = iFrame.contentWindow.document.getElementsById('IntalioInternal_jsxmain');
			  //iFrameBody = iFrame.contentWindow.document.getElementsByTagName('body')[0];
			  //var iFrameDoc = window.frames[id].document;
			  //iFrameBody = iFrameDoc.getElementsByTagName('body')[0];
			 --%>			  
			 oBody	= iFrame.document.body;
		}
		
		/* IE doesn't seem to be working correctly */
		if (isIE()) {
			if (oBody == undefined) {
				return iFramePreviousHeight;
			}
			return oBody.scrollHeight + (oBody.offsetHeight - oBody.clientHeight);

			<%--//iFrameBody.scrollHeight;
			// return iFrameBody.scrollHeight; --%>
		} else {
			return iFrameBody.scrollHeight;		
		}
		
	}
	
	function isIE() {
		if (navigator.appName == 'Microsoft Internet Explorer') {
			return true;
		} else {
			return false;
		}
	}

	/* Simple function to send some example ajax data */
	function ajaxSampleData() {	
		var command = jQuery('#ajaxCommand').val();
		var data = jQuery('#ajaxData').val();		
		var result = sendDataToPortlet(command, data);
		jQuery(".test").append("<div><pre>"+result+"</pre></div>");
	}
	
	function doSomething(data) {
		var obj = JSON.parse(data);
		var json = obj.response;
		var test1 = json["result"];
		jQuery(".test").append("<div><pre>"+test1+"</pre></div>");
	}	
	
	/**
	 * Simple function to send some example ajax data 
	 * 
	 * @param service ServiceName (e.g AppoimentService)
	 * @param data XML-data
	 */
	function sendDataToPortlet(service, data) {	
		var url="<%= ajax %>";		
		var ajaxObject = {
							"service":service,
							"data":data
						};
		
		return jQuery.ajax( {
			url: url,  
			type: "POST", 
			data: ajaxObject, 
		    dataType: "html",
			async: false 
		}).responseText;
	}

	 
	/* add the formId to the intalio form for editting appointment form */
	window.onload = function() {
		var global_url = document.URL;

		 if(global_url.indexOf("FormID=") > 0) {
			var newUrl = "${formholder.url}";
			var temp = global_url.split("FormID=");
			var param = temp[1];
			newUrl += "&FormID=" + param;			
			jQuery('#<portlet:namespace />xforms_iframe').attr('src', newUrl);
		}else {
			jQuery('#<portlet:namespace />xforms_iframe').attr('src', "${formholder.url}");
		} 
		
		 /* Make sure that IFrame height is correct. We do not want to 
		  *	see any extra scrollbars. */
		resizeIFrame(iFrameId, minHeight);
	 
	 
	 <%-- Temporary solution 
		var formUrl = "${formholder.url}";
		if(koku_currentUrl.indexOf("FormID=") > 0) {
			var temp = koku_currentUrl.split("FormID=");
			var formId = temp[1];
			formUrl = formUrl.replace('/palvelut-portlet/ajaxforms','http://intalio.intra.arcusys.fi:8080/gi');
			formUrl = formUrl.replace('%2Fpalvelut-portlet%2Fajaxforms%2F','%2Fgi%2F');
			jQuery('#<portlet:namespace />xforms_iframe').attr('src', formUrl + "&FormID=" + formId);
		}else {
			formUrl = formUrl.replace('/palvelut-portlet/ajaxforms','http://intalio.intra.arcusys.fi:8080/gi');
			formUrl = formUrl.replace('%2Fpalvelut-portlet%2Fajaxforms%2F','%2Fgi%2F');
			jQuery('#<portlet:namespace />xforms_iframe').attr('src', formUrl);
		}		
	--%>
	}

        function scrollToTop() {
// 		var iframe = document.getElementById('hiddenIframe');
//                 iframe.src = 'https://intalio:8443/xssEnabler.html?action=' + 'scrollToTop';
        };

	function modifyButton() {
		jQuery('#<portlet:namespace />xforms_iframe').contents().find('span[label=IntalioInternal_StartButton]').click(function() { scrollToTop(); });
	}

	jQuery(document).ready( function() {window.setInterval(modifyButton, 5000);
 } );
	
</script>

<portlet:renderURL var="viewURL" windowState="<%= WindowState.MAXIMIZED.toString() %>">
	<portlet:param name="action" value="view"/>
</portlet:renderURL>


 <%-- Disabled for security reasons 
 
 <div class="test" style="display: none;">
	<div class="testTextAreas">
		<textarea id="ajaxCommand" rows="3" cols="15" name="Command:"></textarea>
		<textarea id="ajaxData" rows="3" cols="50" name="Data:"></textarea>
	</div>	
	<button type="button" id="ajaxTest" name="Send data" onclick="ajaxSampleData()">Send data</button>
</div>

 --%>

<div id="form_wrap" style="margin:5px; position:relative; min-width: 720px;">
<div class="veera_form_window_header">
	<c:if test="${helpContent != null}">
		<span class="form_help_link">
			<a href="#" onclick="
				var html = '<c:out value="${helpContent}"/>';
				var popup = new Liferay.Popup(
					{
						header: 'Ohje',
						position:[150,150],
						modal:false,
						handles: '',
						width:600,
						height:600,
						xy: ['center', 100],
						message: html
					}
				);">Ohje</a>
		</span>
	</c:if>
	
	<span>
		<jsp:include page="/jsp/breadCrumb.jsp" />
		<span style="display: inline">» ${formholder.name}</span>
	</span>

</div>

<!-- <iframe id="hiddenIframe" width="0" height="0" src="https://intalio:8443/xssEnabler.html" style="display: none;"></iframe> -->

<iframe src="" id="<portlet:namespace />xforms_iframe" class="xforms_container_iframe" frameborder="0" scrolling="no" style="height: 700px; width:100%;"></iframe>

<script type="text/javascript">
	startResizer();
</script>

