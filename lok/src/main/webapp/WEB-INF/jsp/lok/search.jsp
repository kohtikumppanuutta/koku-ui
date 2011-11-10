<%--
 Copyright 2011 Ixonos Plc, Finland. All rights reserved.

 You should have received a copy of the license text along with this program.
 If not, please contact the copyright holder (http://www.ixonos.com/).
--%>
<%@ include file="imports.jsp"%>

<portlet:defineObjects />

<portlet:actionURL var="searchActionURL">
	<portlet:param name="action" value="searchLog" />
	<portlet:param name="visited" value="---" />
</portlet:actionURL>

<portlet:renderURL var="searchUserURL">
	<portlet:param name="action" value="searchUser" />
</portlet:renderURL>

<portlet:renderURL var="archiveURL">
	<portlet:param name="action" value="archiveLog" />
</portlet:renderURL>


<div class="koku-lok">
	<div class="portlet-section-body">
		<c:choose>
			<c:when test="${not empty requestScope.allowedToView}">
				<div class="home">
					<a href="${searchUserURL}"><spring:message
							code="koku.common.back" /> </a>
				</div>
				<h1 class="portlet-section-header">
					<spring:message code="koku.lok.header" />
				</h1>

				<div class="portlet-menu">
					<ul>
						<li class="portlet-menu-item-selected"><spring:message
								code="koku.lok.menu.item.search" /></li>
						<li class="portlet-menu-item"><a href="${archiveURL}"><spring:message
									code="koku.lok.menu.item.archive" /> </a></li>
					</ul>
				</div>

				<div class="portlet-form-field-label">
					<spring:message code="koku.lok.search.header" />
					<p>
				</div>

				<form:form name="logSearchForm" commandName="logSearchCriteria"
					method="post" action="${searchActionURL}">

					<span class="form-field-label"><spring:message
							code="koku.common.pic" /> </span>
					<span class="form-static-parameter">${pic}</span>

					<form:input type="hidden" path="pic" value="${pic}" />

					<!--TODO:  TÄHÄN VETOVALIKKO, JOSSA MAHDOLLISET ARVOT -->

					<span class="form-field-label"><spring:message
							code="koku.common.concept" /> </span>
					<form:input path="concept" maxlength="100" size="20" />

					<!--  TODO: Javascript date picker will be added here! -->
					<span class="form-field-label"><spring:message
							code="koku.common.startTime" /> </span>

					<form:input path="from" value="${startDate}" maxlength="10"
						size="10" />

					<span class="form-field-label"><spring:message
							code="koku.common.endTime" /> </span>
					<form:input path="to" value="${endDate}" maxlength="10" size="10" />

					<input type="submit"
						value="<spring:message code="koku.lok.search"/>">
				</form:form>
				<c:if test="${not empty limit}">
					<br>
					<div class="error">
						<spring:message code="${limit}" />
					</div>
				</c:if>
				<c:choose>
					<c:when test="${not empty error}">
						<br>
						<div class="error">
							<spring:message code="${error}" />
						</div>
						<p>
					</c:when>
					<c:otherwise>

						<c:if test="${not empty entries}">
							<c:if test="${not empty visited}">
								<h2 class="portlet-section-subheader">
									<spring:message code="koku.common.searchResults" />
									<c:out value="${searchParams.pic}" />,
									<c:out value="${searchParams.concept}" />
									<fmt:formatDate pattern="dd.MM.yyyy"
										value="${searchParams.from}" />
									-
									<fmt:formatDate pattern="dd.MM.yyyy" value="${searchParams.to}" />
								</h2>
								<table class="portlet-table-body" width="100%" border="0">

									<tr>
										<%-- TODO! when using <th> the text won't align to left, that's why we use <td> and <b> here now.
This should be changed! --%>
										<!-- TODO: Should the header titles be changed?  -->

										<th width=20% scope="col"><b><spring:message
													code="koku.lok.time" /> </b>
										</th>
										<th width=20% scope="col"><b><spring:message
													code="koku.lok.end.user" /> </b>
										</th>
										<th width=10% scope="col"><b><spring:message
													code="koku.lok.operation" /> </b>
										</th>
										<th width=40% scope="col"><b><spring:message
													code="koku.lok.data.item.type" /> </b>
										</th>
										<th width=10% scope="col"><b><spring:message
													code="koku.lok.service" /> </b>
										</th>
									</tr>
									<c:forEach var="e" items="${entries}">
										<tr>
											<td width=20%><fmt:formatDate
													pattern="dd.MM.yyyy HH:mm:ss" value="${e.timestamp}" />
											</td>
											<td width=20%><c:out value="${e.user}" />
											</td>
											<td width=10%><c:out value="${e.operation}" />
											</td>
											<td width=40%><c:out value="${e.message}" />
											</td>
											<td width=10%><c:out value="${e.clientSystemId}" />
											</td>
										</tr>
									</c:forEach>
								</table>
							</c:if>
						</c:if>

						<c:if test="${empty entries}">
							<c:if test="${not empty visited}">
								<%-- do not show this on the first visit to this page --%>
								<p>
								<div class="error">
									<spring:message code="koku.lok.noResults" />
								</div>
								</p>
							</c:if>
						</c:if>

						<c:if test="${not empty error2}">
							<div class="error">
								<spring:message code="${error2}" />
							</div>

						</c:if>

						<c:if test="${not empty error0}">
							<div class="error">
								<spring:message code="${error0}" />
							</div>
						</c:if>

						<c:if test="${not empty error1}">
							<div class="error">
								<spring:message code="${error1}" />
							</div>

						</c:if>

						<c:if test="${not empty error3}">
							<div class="error">
								<spring:message code="${error3}" />
							</div>
						</c:if>
						</br>
					</c:otherwise>
				</c:choose>
			</c:when>


			<c:otherwise>
				<spring:message code="koku.lok.no.user.rights" />
			</c:otherwise>
		</c:choose>
	</div>
</div>
<!-- end of koku-lok-div -->
