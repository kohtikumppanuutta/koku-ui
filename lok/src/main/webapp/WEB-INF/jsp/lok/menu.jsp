<%@ include file="imports.jsp" %>

<portlet:renderURL var="archiveURL">
	<portlet:param name="action" value="archiveLog" />
</portlet:renderURL>

<portlet:renderURL var="searchUserURL">
	<portlet:param name="action" value="searchUser" />
</portlet:renderURL>

<portlet:renderURL var="searchLogViewsURL">
	<portlet:param name="action" value="viewLog" />
</portlet:renderURL>

<div class="koku-lok">
	<h1 class="portlet-section-header">
		<spring:message code="koku.lok.portlet.title" />
	</h1>

	<div style="float: left;">

		<div>
			<h3><spring:message code="koku.lok.admin.actions" /></h3>


			<form:form name="searchForm" method="post" action="${searchUserURL}">
				<input type="submit"
					value="<spring:message code="koku.common.search"/>" />
			</form:form>

			<form:form name="logForm" method="post" action="${archiveURL}">
				<input type="submit"
					value="<spring:message code="koku.common.archive"/>" />
			</form:form>

		</div>

		<div>
			<h3><spring:message code="koku.lok.superadmin.actions" /></h3>
			<form:form name="viewForm" method="post" action="${searchLogViewsURL}">
				<input type="submit" value="<spring:message code="koku.lok.view"/>" />
			</form:form>
		</div>
	</div>


	<div style="clear: both"></div>

</div><!-- end of koku-lok-div -->