<%@ include file="imports.jsp" %>

<portlet:defineObjects />

<portlet:renderURL var="homeURL">
	<portlet:param name="action" value="choose" />
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

	</div>
</div><!-- end of koku-lok-div -->