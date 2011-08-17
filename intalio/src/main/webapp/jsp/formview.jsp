<%@ include file="/jsp/init.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<script type="text/javascript">

/* add the formId to the intalio form for editting appointment form */
window.onload = function() {
	
	if(koku_currentUrl.indexOf("FormID=") > 0) {
		var temp = koku_currentUrl.split("FormID=");
		var formId = temp[1];
		
		jQuery('#<portlet:namespace />xforms_iframe').attr('src', "${formholder.url}" + "&FormID=" + formId);
	}else {
		jQuery('#<portlet:namespace />xforms_iframe').attr('src', "${formholder.url}");
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

<iframe src="" id="<portlet:namespace />xforms_iframe" class="xforms_container_iframe" frameborder="0" scrolling="no" style="height: 2500px;"></iframe>
