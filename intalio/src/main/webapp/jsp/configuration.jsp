<%@ include file="/jsp/init.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	isELIgnored="false"%>
<%
	PortletPreferences prefs = (PortletPreferences)request.getAttribute("prefs");
%>

<%@page import="javax.portlet.PortletPreferences"%>

<div class="configuration">
	<form action="
	<portlet:actionURL>
		<portlet:param name="action" value="edit" />
	</portlet:actionURL> "
	method="post">
	
		<span>Näytettävä lomake</span>
		<select name="showOnlyForm">
			<c:forEach var="form" items="${formList}">
				<c:set value="<%=prefs.getValue(\"showOnlyForm\", null) %>" var="formId"></c:set>
				<c:choose>
					<c:when test="${formId eq form.task.ID}">
						<option value="${form.task.ID}" selected="selected">${form.task.description}</option>
					</c:when>
					<c:otherwise>
						<option value="${form.task.ID}">${form.task.description}</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
		<span>Näytä vain lomake: </span>
		<c:choose>
			<c:when test="<%=prefs.getValue(\"showOnlyChecked\", null) != null %>">
				<input type="checkbox" name="showOnlyChecked" checked="checked"/>
			</c:when>
			<c:otherwise>
				<input type="checkbox" name="showOnlyChecked"/ >
			</c:otherwise>
		</c:choose>
		<input type="submit" value="Submit">  
	</form>
</div>