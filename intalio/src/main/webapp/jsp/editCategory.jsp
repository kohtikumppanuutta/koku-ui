<%@ include file="/jsp/init.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@page import="fi.arcusys.koku.palvelukanava.palvelut.model.VeeraCategory"%>
<%@ page import="fi.arcusys.koku.palvelut.controller.ViewController"%>
<%@ page import="fi.arcusys.koku.palvelut.controller.EditController"%>
<%@ page import="fi.arcusys.koku.palvelut.util.MigrationUtil"%>
<%response.setContentType("text/html;charset=utf-8");%>
<%request.setCharacterEncoding("utf-8");%> 
<%
/** This page works as category edit & add page **/
boolean addNew = (request.getAttribute("editCategory") == null);
pageContext.setAttribute("addNew", addNew);
%>

<jsp:include page="/jsp/messages.jsp" />

<jsp:include page="/jsp/breadCrumb.jsp" />

<form action="
	<portlet:actionURL>
		<portlet:param name="action" value="edit" />
		<portlet:param name="<%= EditController.EDIT_TYPE %>" value="<%= addNew ? EditController.INSERT_CATEGORY : EditController.UPDATE_CATEGORY %>" />
	</portlet:actionURL> " method="post"  accept-charset="UTF-8">

	<!-- Current category -->
	<input type="hidden" name="<%= EditController.CURRENT_CATEGORY %>" value="${categoryId}" />
	
	<c:if test="${!addNew}">
		<!-- Entry to be updated -->
		<input type="hidden" name="entryId" value="${editCategory.entryId}"/>
	</c:if>

	<table class="liferay-table">
		<tbody>
			<tr>
				<td style="width: 150px;" class="question required">
					<label for="categoryName">Kansion nimi</label>
				</td>
				<td class="answer">
					<input type="text" onkeypress="Liferay.Util.checkMaxLength(this, 75);"
						value="${editCategory.name}"
						style="width: 350px;" name="categoryName" id="categoryName"/>
				</td>
			</tr>
			<tr>
				<td style="width: 150px;" class="question">
					<label for="categoryDescription">Kuvaus</label>
				</td>
				<td class="answer">
					<%
						String escapedDescription = "";
						if(!addNew) {
							VeeraCategory category = (VeeraCategory)request.getAttribute("editCategory"); 
							String description = category.getDescription();
							if (description == null) description = "";
							escapedDescription = HtmlUtil.escape(description);
						}
					%>
					<input type="text" value="<%= escapedDescription %>" style="width: 350px;"
					onkeypress="Liferay.Util.checkMaxLength(this, 512);"
					name="categoryDescription" id="categoryDescription"/>
				</td>
			</tr>
		</tbody>
	</table>
	<br />
	<input type="submit" value="Tallenna" class="submit" />
	
	<portlet:renderURL var="cancelURL">
		<portlet:param name="action" value="view" />
		<portlet:param name="<%= ViewController.VIEW_CURRENT_FOLDER %>" value="${categoryId}" />
	</portlet:renderURL>
	<input type="button" onclick="self.location = '${cancelURL}';" value="Peruuta" class="button" />
</form>