<%@ include file="imports.jsp" %>

<portlet:defineObjects />

<portlet:actionURL var="archiveActionURL">
	<portlet:param name="action" value="archiveLog" />
	<portlet:param name="visited" value="---" />
	<portlet:param name="user" value="${user}" />
	<portlet:param name="userRole" value="${userRole}" />
</portlet:actionURL>

<portlet:actionURL var="changeActionURL">
	<portlet:param name="action" value="archiveLog" />
	<portlet:param name="change" value="change" />
	<portlet:param name="visited" value="---" />
	<portlet:param name="user" value="${user}" />
	<portlet:param name="userRole" value="${userRole}" />
</portlet:actionURL>

<portlet:actionURL var="startArchiveActionURL">
	<portlet:param name="action" value="startArchiveLog" />
	<portlet:param name="user" value="${user}" />
	<portlet:param name="userRole" value="${userRole}" />
</portlet:actionURL>

<portlet:renderURL var="homeURL">
	<portlet:param name="action" value="choose" />
	<portlet:param name="user" value="${user}" />
	<portlet:param name="userRole" value="${userRole}" />
</portlet:renderURL>

<portlet:renderURL var="searchUserURL">
	<portlet:param name="action" value="searchUser" />
	<portlet:param name="user" value="${user}" />
	<portlet:param name="userRole" value="${userRole}" />
</portlet:renderURL>


<div class="koku-lok">
<div class="portlet-section-body">
<c:choose>
	<c:when test="${userRole == 'ROLE_LOK_ADMIN'}">
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
		
			<div class="log-archive">
				<form:form name="logArchiveForm" commandName="logArchiveDate"
					method="post" action="${archiveActionURL}">

					<%-- TODO: Add error handling! Now the user can give any date and the
					parsing error is shown on the web page. --%> 
					<%-- TODO: Add a javascript date picker here? --%>
					<span class="form-field-label"><spring:message
							code="koku.lok.archiveDate" /> </span>
							<form:input path="endDate" value="${endDate}" 
							maxlength="10" size="10"/>
					<span class="errors"><form:errors path="endDate" />
					</span>
						<input type="submit"
						value="<spring:message code="koku.lok.button.archive"/>">

					<div class="clear"></div>
				</form:form>			
			</div>
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
				<br />
				<br />

				<c:if test="${not empty error}">
	<%-- do not show this on the first visit to this page --%>
				<p>
			
				<spring:message code="${error}" /> 
<%-- 					<spring:message code="koku.lok.archive.nothing.to.archive" />--%>
				</p>
	</c:if>
</c:when>
<c:otherwise>
	<spring:message code="ui.lok.no.user.rights" />
</c:otherwise>
</c:choose>	
	</div>
	

</div><!-- end of koku-lok-div -->
