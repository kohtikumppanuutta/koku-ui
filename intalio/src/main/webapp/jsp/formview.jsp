<%@ include file="/jsp/init.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<portlet:resourceURL var="ajax" id="intalioAjax"></portlet:resourceURL>

<script type="text/javascript">

	/* Simple function to send some example ajax data */
	function ajaxSampleData() {	
		var command = jQuery('#ajaxCommand').val();
		var data = jQuery('#ajaxData').val();		
		ajaxData(command, data, doSomething);
	}
	
	function doSomething(data) {
		var obj = JSON.parse(data);
		var json = obj.response;
		var test1 = json["result"];
		jQuery(".test").append("<div><pre>"+test1+"</pre></div>")
	}
	
	
	/**
	 * Simple function to send some example ajax data 
	 * 
	 * @param service ServiceName (e.g AppoimentService)
	 * @param data XML-data
	 */
	function ajaxData(service, data, returnFunction) {	
		var url="<%= ajax %>";		
		var ajaxObject = {
							"service":service,
							"data":data
						};
		
		jQuery.post(url, ajaxObject, returnFunction);
	}

	 
	/* add the formId to the intalio form for editting appointment form */
	window.onload = function() {
	/* 	
		 if(koku_currentUrl.indexOf("FormID=") > 0) {
			var temp = koku_currentUrl.split("FormID=");
			var formId = temp[1];
			
			jQuery('#<portlet:namespace />xforms_iframe').attr('src', "${formholder.url}" + "&FormID=" + formId);
		}else {
			jQuery('#<portlet:namespace />xforms_iframe').attr('src', "${formholder.url}");
		} 
	 */	
	 
	 <%-- Temporary solution --%>
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


<div class="test" style="display: none;">
	<div class="testTextAreas">
		<textarea id="ajaxCommand" rows="3" cols="15" name="Command:"></textarea>
		<textarea id="ajaxData" rows="3" cols="50" name="Data:"></textarea>
	</div>	
	<button type="button" id="ajaxTest" name="Send data" onclick="ajaxSampleData()">Send data</button>
</div>

<div id="form_wrap" style="margin:5px; position:relative;">
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

<iframe src="" id="<portlet:namespace />xforms_iframe" class="xforms_container_iframe" frameborder="0" scrolling="auto" style="height: 1000px; width:100%;"></iframe>
