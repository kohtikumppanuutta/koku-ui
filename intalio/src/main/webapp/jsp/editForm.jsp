<%@ include file="/jsp/init.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ page import="fi.arcusys.koku.palvelut.controller.ViewController"%>
<%@ page import="fi.arcusys.koku.palvelut.controller.EditController"%>
<%@ page import="fi.arcusys.koku.palvelut.model.VeeraForm"%>

<%
/** This page works as URL form edit & add page **/
boolean addNew = (request.getAttribute("form") == null);
pageContext.setAttribute("addNew", addNew);
%>

<jsp:include page="/jsp/messages.jsp" />

<jsp:include page="/jsp/breadCrumb.jsp" />

<form action="
	<portlet:actionURL>
		<portlet:param name="action" value="edit" />
		<portlet:param name="<%= EditController.EDIT_TYPE %>" value="<%= addNew ? EditController.INSERT_URL_FORM : EditController.UPDATE_FORM %>" />
	</portlet:actionURL> " method="post" >
	
	<!-- Current category -->
	<input type="hidden" name="<%= EditController.CURRENT_CATEGORY %>" value="${categoryId}"/>
	
	<c:if test="${!addNew}">
		<!-- Entry to be updated -->
		<input type="hidden" name="entryId" value="${form.entryId}"/>
	</c:if>
	
	<input type="hidden" name="formType" value="${form.type}"/>
	
	<table class="liferay-table">
		<tbody>
			<tr>
				<td style="width: 150px;" class="question">
					<label for="formName">
						Palvelu
					</label>
				</td>
				<td class="answer">
					<c:choose>
						<c:when test="${form.type != '2'}">
							<input type="text" value="${form.identity_64}" style="width: 350px;" name="formAddName" id="formName"/>
						</c:when>
						<c:otherwise>
							<input type="hidden" value="${form.identity_64}" name="formAddName"/>
							<input disabled="disabled" type="text" value="${form.identity_64}" style="width: 350px;"
							onkeypress="Liferay.Util.checkMaxLength(this, 300);" id="formName"/>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
				<c:if test="${form.type != '2'}">
					<tr>
						<td style="width: 150px;" class="question required">
							<label for="formURL">
								Linkin URL-osoite
							</label>
						</td>
						<td class="answer">
							<input type="text" value="${form.identity2_64}" style="width: 350px;"
							onkeypress="Liferay.Util.checkMaxLength(this, 4000);"
							name="formAddURL" id="formURL"/>
						</td>
					</tr>
				</c:if>
			<tr>
				<td style="width: 150px;" class="question">
					<label for="formDescription">
						Kuvaus
					</label>
				</td>
				<td class="answer">
					<input type="text" value="${form.description}" style="width: 350px;"
					onkeypress="Liferay.Util.checkMaxLength(this, 512);"
					name="formAddDescription" id="formDescription"/>
				</td>
			</tr>
			<tr>
				<td style="width: 150px;" class="question">
					<label for="formHelpContent">
						Ohje
					</label>
				</td>
				<td class="answer">
					<textarea rows="10" style="width: 350px;"
					onkeypress="Liferay.Util.checkMaxLength(this, 4000);"
					name="formAddHelpContent" id="formHelpContent"><c:out value="${form.helpContent}"/></textarea>
				</td>
			</tr>
		</tbody>
	</table>
	<br />
	<input type="submit" value="Tallenna" class="submit" />
	
	<!-- Cancel -->
	<portlet:renderURL var="cancelURL">
		<portlet:param name="action" value="view" />
		<portlet:param name="<%= ViewController.VIEW_CURRENT_FOLDER %>" value="${categoryId}" />
	</portlet:renderURL>
	<input type="button" onclick="self.location = '${cancelURL}';" value="Peruuta" class="button" />
	
</form>
