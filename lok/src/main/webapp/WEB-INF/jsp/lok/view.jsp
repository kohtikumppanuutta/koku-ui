<%@ include file="imports.jsp" %>

<portlet:defineObjects />

<portlet:actionURL var="viewActionURL">
	<portlet:param name="action" value="viewLog" />
	<portlet:param name="visited" value="---" />
</portlet:actionURL>

<portlet:renderURL var="homeURL">
    <portlet:param name="action" value="home" />
</portlet:renderURL>

<div class="koku-lok">
	<div class="portlet-section-body">

		<div class="home">
			<a href="${homeURL}"><spring:message code="koku.common.back" />
			</a>
		</div>

		<h1 class="portlet-section-header">
			<spring:message code="koku.lok.header.view" />
		</h1>


		<div class="portlet-form-field">
			<div class="portlet-form-field-label">
				<spring:message code="koku.lok.search.for.view" />
			</div>

			<p>
				<form:form name="logSearchForm" commandName="logSearchCriteria"
					method="post" action="${viewActionURL}">


					<!--  TODO: Javascript date picker will be added here! -->

					<span class="form-field-label"><spring:message
							code="koku.common.startTime" /> </span>
					<form:input path="from"
						value="${startDate}" />
					<span class="errors"><form:errors path="from" />
					</span>

					<span class="form-field-label"><spring:message
							code="koku.common.endTime" /> </span>
					<form:input path="to" value="${endDate}" />
					<span class="errors"><form:errors path="to" />
					</span>

					<input type="submit"
						value="<spring:message code="koku.common.search"/>">

					<div class="clear"></div>
				</form:form>


				<%-- th { text-align: center; font-weight: bold } --%>
				<c:if test="${not empty entries}">
					<c:if test="${not empty visited}">
					<h2 class="portlet-section-subheader">
						<spring:message code="koku.lok.view.results.header" />
						<fmt:formatDate pattern="dd.MM.yyyy" value="${searchParams.from}" />
						-
						<fmt:formatDate pattern="dd.MM.yyyy" value="${searchParams.to}" />
						:
					</h2>

					<%-- TODO: Nämä kentät tarvitaan:
						aikaleima
						käsittelijän nimi
						tapahtumatyyppi
						käsitelty tieto (tapahtumakuvaus ja kohde)
 					--%>
					<table class="portlet-table-body" width="100%" border="0">

						<tr>
							<%-- TODO! when using <th> the text won't align to left, that's why we use <td> and <b> here now.
								This should be changed! --%>

							<th width=20% scope="col"><b><spring:message code="ui.lok.timestamp" /></b>
							</th>
							<th width=20% scope="col"><b><spring:message code="ui.lok.koku.user" /></b>
							</th>
							<th width=20% scope="col"><b><spring:message code="ui.lok.event.type" /></b>
							</th>
							<th width=40% scope="col"><b><spring:message code="ui.lok.event.description" /></b>
							</th>
						</tr>

						<c:forEach var="e" items="${entries}">
							<tr>
								<td width=20%><c:out value="${e.timestamp}"/></td>
								<td width=20%><c:out value="${e.user}"/></td>
								<td width=20%><c:out value="${e.eventType}"/></td>
								<td width=40%><c:out value="${e.eventDescription}"/></td>
							</tr>
						</c:forEach>
					</table>
				</c:if>
				</c:if>
		</div>

		<c:if test="${empty entries}">
			<c:if test="${not empty visited}">
				<%-- do not show this on the first visit to this page --%>
				<p>
					<spring:message code="koku.common.noResults" />
				</p>
			</c:if>
		</c:if>

		<br />
	</div>
</div>
<!-- end of koku-lok-div -->