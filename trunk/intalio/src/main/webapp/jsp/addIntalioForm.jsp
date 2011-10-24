<%@ include file="/jsp/init.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ page import="fi.arcusys.koku.palvelut.controller.ViewController"%>
<%@ page import="fi.arcusys.koku.palvelut.controller.EditController"%>

<jsp:include page="/jsp/breadCrumb.jsp" />

<form action="
	<portlet:actionURL>
		<portlet:param name="action" value="edit" />
		<portlet:param name="<%= EditController.EDIT_TYPE %>" value="<%= EditController.INSERT_INTALIO_FORM %>" />
	</portlet:actionURL> " method="post" >
	
	<span id="formLabel">Valitse lisättävä lomake:</span>
	<select name="intalioList">
		<c:forEach var="form" items="${formList}">
			<option value="${form.task.description}">${form.task.description}</option>
		</c:forEach>
	</select>
	<input type="hidden" name="<%= EditController.CURRENT_CATEGORY %>" value="${categoryId}" />
	<input type="submit" value="Lisää" name="edit"/>

	<portlet:renderURL var="cancelURL">
		<portlet:param name="action" value="view" />
		<portlet:param name="<%= ViewController.VIEW_CURRENT_FOLDER %>" value="${categoryId}" />
	</portlet:renderURL>
	<input type="button" onclick="self.location = '${cancelURL}';" value="Peruuta" class="button" />
	
</form>