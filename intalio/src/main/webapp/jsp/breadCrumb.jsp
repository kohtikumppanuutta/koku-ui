<%@ include file="/jsp/init.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@page import="fi.arcusys.koku.palvelut.model.client.VeeraCategory"%>
<%@page import="fi.arcusys.koku.palvelut.controller.EditController"%>
<%@page import="fi.arcusys.koku.palvelut.controller.ViewController"%>
<%@page import="fi.arcusys.koku.palvelut.util.CategoryUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.*"%>

<!-- Breadcrumb -->

<%
List<VeeraCategory> path = (List<VeeraCategory>)request.getAttribute("breadcrumb");
pageContext.setAttribute("path", path);
pageContext.setAttribute("displayBreadcrumbs", path != null && !path.isEmpty());

int c = 1;
%>

<div class="breadcrumbs">
	<c:if test="${displayBreadcrumbs}">
		<c:forEach var="cat" items="${path}">
			<portlet:renderURL var="catLink" windowState="<%= WindowState.NORMAL.toString() %>">
				<portlet:param name="folderId" value="${cat.entryId}"></portlet:param>
			</portlet:renderURL>
			<%= c++ == 1 ? "" : " » " %>
			<a href="${catLink}">${cat.name}</a> 
		</c:forEach>		
	</c:if>
</div>
