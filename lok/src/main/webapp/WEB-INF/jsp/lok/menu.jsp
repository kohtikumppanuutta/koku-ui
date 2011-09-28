<%@ include file="imports.jsp" %>

<portlet:renderURL var="archiveURL">
	<portlet:param name="action" value="archiveLog" />
	<portlet:param name="user" value="${user}" />
	    <portlet:param name="useruid" value="${useruid}" />
</portlet:renderURL>

<portlet:renderURL var="searchUserURL">
	<portlet:param name="action" value="searchUser" />
	<portlet:param name="user" value="${user}" />
	    <portlet:param name="useruid" value="${useruid}" />
</portlet:renderURL>

<portlet:renderURL var="searchLogViewsURL">
	<portlet:param name="action" value="viewLog" />
	<portlet:param name="user" value="${user}" />
    <portlet:param name="useruid" value="${useruid}" />
</portlet:renderURL>

<div class="koku-lok">
<p>
		<c:choose>
			<c:when test="${useruid == 'admin'}">
	
	<a href="${searchUserURL}"><spring:message code="koku.common.search"/></a>
		<p>
		
		<a href="${archiveURL}"><spring:message code="koku.common.archive"/></a>
		
			<%-- 
			<div style="float: left;">
				<form:form name="searchForm" method="post" action="${searchUserURL}">
					<input type="submit"
						value="<spring:message code="koku.common.search"/>" />
				</form:form>


				<form:form name="logForm" method="post" action="${archiveURL}">
					<input type="submit"
						value="<spring:message code="koku.common.archive"/>" />
				</form:form>
--%>
			</c:when>

			<c:when test="${useruid == 'superadmin'}">
				<a href="${searchLogViewsURL}"><spring:message code="koku.lok.view"/></a>
				
	<%-- 		<form:form name="viewForm" method="post"
					action="${searchLogViewsURL}">
					<input type="submit" value="<spring:message code="koku.lok.view"/>" />
				</form:form>
				--%>	
				
			</c:when>
		</c:choose>
<p>
		<%-- 
<h1 class="portlet-section-header">
		<spring:message code="koku.lok.portlet.title" />
</h1>
	<div style="float: left;">
		<c:if test="${not empty admin}">
			<h3>
				<spring:message code="koku.lok.admin.actions" />
			</h3>

			<form:form name="searchForm" method="post" action="${searchUserURL}">
				<input type="submit"
					value="<spring:message code="koku.common.search"/>" />
			</form:form>

			<form:form name="logForm" method="post" action="${archiveURL}">
				<input type="submit"
					value="<spring:message code="koku.common.archive"/>" />
			</form:form>
		</c:if>
		<c:if test="${not empty superadmin}">
			<h3>
				<spring:message code="koku.lok.superadmin.actions" />
			</h3>
			<form:form name="viewForm" method="post"
				action="${searchLogViewsURL}">
				<input type="submit" value="<spring:message code="koku.lok.view"/>" />
			</form:form>
		</c:if>

	</div>

--%>
	<div style="clear: both"></div>

</div><!-- end of koku-lok-div -->