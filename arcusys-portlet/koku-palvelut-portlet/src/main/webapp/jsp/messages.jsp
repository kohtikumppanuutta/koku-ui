<%@ include file="/jsp/init.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ page import="fi.arcusys.koku.palvelut.controller.EditController"%>

<%
// UI-messages
String UIMessage = request.getParameter(EditController.UI_MESSAGE);
String resultCode = request.getParameter(EditController.RESULTCODE);

boolean showMessage = (UIMessage != null && resultCode != null);

pageContext.setAttribute("showMessage", showMessage);
pageContext.setAttribute("msg", UIMessage);
pageContext.setAttribute("code", resultCode);

%>

<c:if test="${ showMessage }">
	<c:choose>
		<c:when test='${ code == "0" }'>
			<div class="portlet-msg-success"> ${msg} </div>
		</c:when>
		<c:when test='${ code == "1" }'>
			<div class="portlet-msg-error"> ${msg} </div>
		</c:when>
	</c:choose>
</c:if>
