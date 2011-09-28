<%@ include file="imports.jsp" %>

<portlet:defineObjects />

<portlet:renderURL var="homeURL">
	<portlet:param name="action" value="choose" />
	<portlet:param name="user" value="${user}" />
</portlet:renderURL>

<div class="koku-lok">
	<div class="portlet-section-body">
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
	</div>
</div><!-- end of koku-lok-div -->