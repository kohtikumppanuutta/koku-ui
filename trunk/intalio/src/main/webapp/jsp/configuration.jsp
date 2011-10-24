<%@ include file="/jsp/init.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"	isELIgnored="false"%>
<%
	PortletPreferences prefs = (PortletPreferences)request.getAttribute(Constants.ATTR_PREFERENCES);
	String portalInfo = (String)request.getAttribute(Constants.ATTR_PORTAL_ID);
%>

<%@page import="javax.portlet.PortletPreferences"%>

<div class="configuration">	
	<c:set value="<%= request.getAttribute(Constants.ATTR_PORTAL_ID) %>" var="portalVersion"></c:set>
	<c:choose>
		<c:when test="<%= portalInfo.equals(Constants.PORTAL_JBOSS) %>">		
		<!-- JBoss	  -->
			 <c:set value="<%=prefs.getValue(\"showOnlyForm\", null) %>" var="formId"></c:set>
			 <c:set value="<%=prefs.getValue(\"showOnlyFormByDescription\", null) %>" var="formDescription"></c:set>
		 	 <c:set value="<%=prefs.getValue(\"useTaskId\", null) %>" var="selectFormById"></c:set>
			 <div class="CurrentForm">Tällähetkellä valittu lomake:
			 <% Boolean isTaskIdEnabled = Boolean.valueOf(prefs.getValue(Constants.SHOW_TASKS_BY_ID, null)); %>
			 <c:choose>	 
			 	<c:when test="<%= (isTaskIdEnabled) %>">
					'${formDescription}' - <b>'${formId}'</b>
			 	</c:when>
				<c:otherwise>
			  		<b>'${formDescription}'</b> - '${formId}'
				</c:otherwise>
			 </c:choose>
			 </div>	 
					<div class="FormValuesHeader">Lomakkeet:</div> 
					<table class="FormValues">
						<tbody>
						<tr class="headerRow">
							<th class="description rowHeader">Lomake:</th>
							<th class="value rowHeader">ID:</th>
						</tr>
						<c:forEach var="form" items="${formList}">
							<tr class="row">
								<td class="description">${form.task.description}</td>
								<td class="value">${form.task.ID}</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
					<div class="FormValuesHelp">Lomakkeen sisältöä ei voi muuttaa täältä. Muutokset tähän portletti instanssiin 
					täytyy tehdä portaalin hallintapaneelin kautta (Admin -> Portlet instances).</div>
					
	 	</c:when>
		<c:otherwise>
	
		<!--  GateIn -->
		<form action="<portlet:actionURL portletMode="edit"><portlet:param name="action" value="config" /></portlet:actionURL>" method="post">	
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
		</c:otherwise>
	</c:choose>
</div>


