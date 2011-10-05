<%@ include file="imports.jsp" %>

<portlet:renderURL var="archiveURL">
	<portlet:param name="action" value="archiveLog" />
	<portlet:param name="user" value="${user}" />
	<portlet:param name="userRole" value="${userRole}" />
</portlet:renderURL>

<portlet:renderURL var="searchUserURL">
	<portlet:param name="action" value="searchUser" />
	<portlet:param name="user" value="${user}" />
	<portlet:param name="userRole" value="${userRole}" />
</portlet:renderURL>

<portlet:renderURL var="searchLogViewsURL">
	<portlet:param name="action" value="viewLog" />
	<portlet:param name="user" value="${user}" />
    <portlet:param name="userRole" value="${userRole}" />
</portlet:renderURL>

<div class="koku-lok">
<p>

		<c:choose>
			<c:when test="${userRole == 'ROLE_LOK_ADMIN'}">
	
		<a href="${searchUserURL}"><spring:message code="koku.lok.search"/></a>
		<p>
		
		<a href="${archiveURL}"><spring:message code="koku.lok.archive"/></a>	
			</c:when>

			<c:when test="${userRole == 'ROLE_LOK_LOG_ADMIN'}">
				<script type="text/javascript">
					window.location.href = "${searchLogViewsURL}";
				</script>

			</c:when>
			<%-- 	<a href="${searchLogViewsURL}"><spring:message code="koku.lok.view"/></a>  --%>	
			<c:otherwise>
			<spring:message code="ui.lok.no.user.rights" />
			</c:otherwise>
		</c:choose>
<p>
		
	<div style="clear: both"></div>

</div><!-- end of koku-lok-div -->