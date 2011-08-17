<%@ include file="/jsp/init.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ page import="fi.arcusys.koku.palvelut.controller.ViewController"%>
<%@ page import="fi.arcusys.koku.palvelut.controller.EditController"%>

<form action="
	<portlet:actionURL>
		<portlet:param name="action" value="edit" />
		<portlet:param name="<%= EditController.EDIT_TYPE %>" value="<%= EditController.INSERT_CATEGORY %>" />
	</portlet:actionURL> " method="post" >

	<div class="breadcrumbs"><a href="#TODO">${categoryId}</a></div>
	<table class="liferay-table">
		<tbody>
			<tr>
				<td style="width: 150px;" class="question required">
					<label for="">Kansion nimi 
					</label>
				</td>
				<td class="answer">
					<input type="text" onkeypress="Liferay.Util.checkMaxLength(this, 75);"
						value=""
						style="width: 350px;" name="categoryName" />
				</td>
			</tr>
			<tr>
				<td style="width: 150px;" class="question">
					<label for="categoryDescription">Kuvaus</label>
				</td>
				<td class="answer">
					<input type="text" value="${editCategory.description}" style="width: 350px;"
					onkeypress="Liferay.Util.checkMaxLength(this, 512);"
					name="categoryDescription" id="categoryDescription"/>
				</td>
			</tr>
		</tbody>
	</table>
	<br />
	<input type="submit" value="Tallenna" class="submit" />
	<input type="hidden" name="<%= EditController.CURRENT_CATEGORY %>" value="${categoryId}" />
	
	<portlet:renderURL var="cancelURL">
		<portlet:param name="action" value="view" />
		<portlet:param name="<%= ViewController.VIEW_CURRENT_FOLDER %>" value="${categoryId}" />
	</portlet:renderURL>
	<input type="button" onclick="self.location = '${cancelURL}';" value="Peruuta" class="button" />
</form>