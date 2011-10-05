<%@ include file="imports.jsp" %>

<portlet:defineObjects />

<portlet:renderURL var="homeURL">
	<portlet:param name="action" value="choose" />
	<portlet:param name="user" value="${user}" />
	<portlet:param name="userRole" value="${userRole}" />
</portlet:renderURL>

<div class="koku-lok">
	<div class="portlet-section-body">
	<c:choose>
	<c:when test="${userRole == 'ROLE_LOK_ADMIN'}">
	
		<p>
			<spring:message code="koku.lok.archivingstatus.success" />
		</p>


		<div class="home">
			<form:form method="post" action="${homeURL}">
				<input type="submit"
					value="<spring:message code="koku.common.lok.begin"/>">
			</form:form>
		</div>
		<br />
<%-- 	<c:if test="${not empty error}">
				<%-- do not show this on the first visit to this page --%>
<%-- 				<p>
					<spring:message code="koku.lok.archive.nothing.to.archive" />
				</p>
	</c:if>--%>
	
	</c:when>

<c:otherwise>
	<spring:message code="ui.lok.no.user.rights" />
</c:otherwise>
</c:choose>

	</div>
</div><!-- end of koku-lok-div -->