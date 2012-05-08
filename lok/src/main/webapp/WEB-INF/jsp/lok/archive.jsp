<%--
 Copyright 2012 Ixonos Plc, Finland. All rights reserved.
  
 This file is part of Kohti kumppanuutta.
 
 This file is licensed under GNU LGPL version 3.
 Please see the 'license.txt' file in the root directory of the package you received.
 If you did not receive a license, please contact the copyright holder
 (http://www.ixonos.com/).
--%>
<%@ include file="imports.jsp" %>

<portlet:defineObjects />

<portlet:actionURL var="archiveActionURL">
	<portlet:param name="action" value="archiveLog" />
	<portlet:param name="visited" value="---" />
</portlet:actionURL>

<portlet:actionURL var="changeActionURL">
	<portlet:param name="action" value="archiveLog" />
	<portlet:param name="change" value="change" />
	<portlet:param name="visited" value="---" />
</portlet:actionURL>

<portlet:actionURL var="startArchiveActionURL">
	<portlet:param name="action" value="startArchiveLog" />
</portlet:actionURL>

<portlet:renderURL var="homeURL">
	<portlet:param name="action" value="choose" />
</portlet:renderURL>

<portlet:renderURL var="searchUserURL">
	<portlet:param name="action" value="searchUser" />
</portlet:renderURL>


<div class="koku-lok">
<div class="portlet-section-body">
<c:choose>
	<c:when test="${not empty requestScope.allowedToView}">
	<div class="home">
		<a href="${homeURL}"><spring:message code="koku.common.back" />
		</a>
	</div>
	
		<h1 class="portlet-section-header">
			<spring:message code="koku.lok.header.archive" />
		</h1>
		
		<div class="portlet-menu">
			<ul>
				<li class="portlet-menu-item"><a href="${searchUserURL}"><spring:message code="koku.lok.menu.item.search"/></a></li>
				<li class="portlet-menu-item-selected"><spring:message code="koku.lok.menu.item.archive"/></li>
			</ul>
		</div>

		<c:if test="${empty visited}">	
				<form:form name="logArchiveForm" commandName="logArchiveDate"
					method="post" action="${archiveActionURL}">

					<%-- TODO: Add a javascript date picker here? --%>
					<span class="form-field-label"><spring:message
							code="koku.lok.archiveDate" /> </span>
							<form:input path="endDate" value="${endDate}" 
							maxlength="10" size="10"/>				
						<input type="submit"
						value="<spring:message code="koku.lok.button.archive"/>">
				</form:form>			
		</c:if>
		
<%-- confirm the date --%>
				<c:if test="${not empty visited}">

					<p>
						<spring:message code="koku.lok.archive.confirmation" />
						<fmt:formatDate pattern="dd.MM.yyyy" value="${archiveDateDate}" />
						<spring:message code="koku.lok.archive.confirmation2" />
					</p>


					<form:form method="post" action="${changeActionURL}">
						<input type="hidden" name="endDate" value="${endDate}" />
						<input type="submit"
							value="<spring:message code="koku.common.changeDate"/>">
					</form:form>

					<c:if test="${empty error}">
						<form:form method="post" action="${startArchiveActionURL}">
							<input type="hidden" name="endDate" value="${endDate}" />
							<input type="submit"
								value="<spring:message code="koku.lok.archive.startArchive"/>">
						</form:form>
					</c:if>
				</c:if>
				<br/>
				<br/>

				<c:if test="${not empty error}">
	<%-- do not show this on the first visit to this page --%>
					<p>
					<div class="error">
						<spring:message code="${error}" />
					</div>
					</p>
				</c:if>
</c:when>
<c:otherwise>
	<spring:message code="koku.lok.no.user.rights" />
</c:otherwise>
</c:choose>	
	</div>
	

</div><!-- end of koku-lok-div -->
