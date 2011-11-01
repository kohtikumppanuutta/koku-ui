<%@ include file="imports.jsp"%>

<portlet:defineObjects />

<portlet:renderURL var="homeURL">
	<portlet:param name="action" value="home" />
</portlet:renderURL>

<portlet:actionURL var="searchUserParamsURL">
	<portlet:param name="action" value="searchUserWithParams" />
</portlet:actionURL>

<portlet:renderURL var="archiveURL">
	<portlet:param name="action" value="archiveLog" />
</portlet:renderURL>

<portlet:renderURL var="showLogSearchFormURL">
	<portlet:param name="action" value="searchLog" />
	<portlet:param name="pic" value="${foundPic}" />
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
					<spring:message code="koku.lok.header" />
				</h1>

				<div class="portlet-menu">
					<ul>
						<%-- TODO: POISTA NÄMÄ? Ja samalla kaikki viitteet tältä sivulta arkistointiin --%>
						<li class="portlet-menu-item-selected"><spring:message
								code="koku.lok.menu.item.search" /></li>
						<li class="portlet-menu-item"><a href="${archiveURL}"><spring:message
									code="koku.lok.menu.item.archive" /> </a></li>
					</ul>
				</div>

				<div class="portlet-form-field-label">
					<spring:message code="koku.lok.search.user" />
				</div>

				<form name="searchUserParams" method="post"
					action="${searchUserParamsURL}">
					<span class="portlet-form-field-label"><spring:message
							code="koku.common.pic" />: </span> <span
						class="portlet-form-input-field"> <input type="text"
						name="pic" /> </span> <input class="portlet-form-button" type="submit"
						value="<spring:message code="koku.lok.seek"/>" />
				</form>

				<p>
					<c:choose>
						<c:when test="${not empty searchedUsers}">
							<table class="portlet-table-body" width="100%" border="0">

								<tr class="portlet-table-body th">
									<th><spring:message code="koku.lok.table.name" />
									</th>
									<th><spring:message code="koku.common.pic" />
									</th>
									<th></th>
								</tr>
								<tr>
									<td><a href="${showLogSearchFormURL}"><c:out
												value="${foundName}" /> </a></td>
									<td><c:out value="${foundPic}" /></td>
								</tr>
							</table>

							<p>&nbsp;</p>

						</c:when>
						<c:otherwise>
							<c:if test="${search}">
								<c:if test="${not empty error}">
									<div class="error">
										<spring:message code="${error}" />
									</div>
									<p>
								</c:if>
							</c:if>
						</c:otherwise>
					</c:choose>
			</c:when>

			<c:otherwise>
				<spring:message code="koku.lok.no.user.rights" />
			</c:otherwise>
		</c:choose>
	</div>

	<script type="text/javascript"
		src="http://code.jquery.com/jquery-1.4.4.min.js"></script>
	<script type="text/javascript"
		src="http://gsgd.co.uk/sandbox/jquery/easing/jquery.easing.1.4.js"></script>
	<script type="text/javascript" language="JavaScript">

</script>

</div>
<!-- end of koku-lok-div -->
