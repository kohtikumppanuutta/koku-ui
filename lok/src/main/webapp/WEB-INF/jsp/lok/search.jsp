<%@ include file="imports.jsp" %>

<portlet:defineObjects />

<portlet:actionURL var="searchActionURL">
	<portlet:param name="action" value="searchLog" />
	<portlet:param name="visited" value="---" />
	<portlet:param name="user" value="${user}" />
	<portlet:param name="userRole" value="${userRole}" />
</portlet:actionURL>

<portlet:renderURL var="searchUserURL">
	<portlet:param name="action" value="searchUser" />
	<portlet:param name="user" value="${user}" />
	    <portlet:param name="userRole" value="${userRole}" />
</portlet:renderURL>

<portlet:renderURL var="archiveURL">
	<portlet:param name="action" value="archiveLog" />
	<portlet:param name="user" value="${user}" />
	    <portlet:param name="userRole" value="${userRole}" />
</portlet:renderURL>


<div class="koku-lok">
	<div class="portlet-section-body">

		<div class="home">
			<a href="${searchUserURL}"><spring:message code="koku.common.back" />
			</a>
		</div>
		<h1 class="portlet-section-header">
			<spring:message code="koku.lok.header" />
		</h1>

		<div class="portlet-menu">
			<ul>

				<li class="portlet-menu-item-selected"><spring:message
						code="koku.lok.menu.item.search" />
				</li>
				<li class="portlet-menu-item"><a href="${archiveURL}"><spring:message
							code="koku.lok.menu.item.archive" />
				</a>
				</li>
			</ul>
		</div>


		<%-- leave portlet-menu and Tarkastele and Arkistoi buttons out for now --%>

		<%--  <div class="portlet-form-field">	--%>
		<div class="portlet-form-field-label">
			<spring:message code="koku.lok.search.parameters" />
		</div>

		<form:form name="logSearchForm" commandName="logSearchCriteria"
			method="post" action="${searchActionURL}">
		
			<span class="form-field-label"><spring:message
					code="koku.common.pic" /> </span>
					<span>${pic}</span> 
				
	 	<form:input type="hidden" path="pic" value="${pic}" />
			<span class="errors"><form:errors path="pic" /> </span>	

<!--TODO:  TÄHÄN VETOVALIKKO, JOSSA MAHDOLLISET ARVOT -->

			<span class="form-field-label"><spring:message
					code="koku.common.concept" /> </span>
			<form:input path="concept" maxlength="15" size="15" />
			<span class="errors"><form:errors path="concept" /> </span>

<!--  TODO: Javascript date picker will be added here! -->
			<span class="form-field-label"><spring:message
					code="koku.common.startTime" /> </span>
		
				<form:input path="from" value="${startDate}"
				maxlength="10" size="10" />
			<span class="errors"><form:errors path="from" /> </span>

			<span class="form-field-label"><spring:message
					code="koku.common.endTime" /> </span>
	
				<form:input path="to" value="${endDate}"
				maxlength="10" size="10" />
			<span class="errors"><form:errors path="to" /> </span>

			<input type="submit"
				value="<spring:message code="koku.common.search"/>">

			<div class="clear"></div>
		</form:form>

	
		<c:if test="${not empty entries}">
			<c:if test="${not empty visited}">
			<h2 class="portlet-section-subheader">
				<spring:message code="koku.common.searchResults" />
				<c:out value="${searchParams.pic}"/>, <c:out value="${searchParams.concept}"/>
				<fmt:formatDate pattern="dd.MM.yyyy" value="${searchParams.from}" />
				-
				<fmt:formatDate pattern="dd.MM.yyyy" value="${searchParams.to}" />
			</h2>
			<table class="portlet-table-body" width="100%" border="0">
		
				<tr>
<%-- TODO! when using <th> the text won't align to left, that's why we use <td> and <b> here now.
This should be changed! --%>
<!-- TODO: Should the header titles be changed?  -->

					<th width=20% scope="col"><b><spring:message code="ui.lok.time" /></b></th>
					<th width=20% scope="col"><b><spring:message code="ui.lok.end.user" /></b></th>
						<th width=40% scope="col"><b><spring:message code="ui.lok.operation" /></b></th>
					<th width=20% scope="col"><b><spring:message code="ui.lok.data.item.type" /></b></th>
					<th width=20% scope="col"><b><spring:message code="ui.lok.service" /></b></th>
				</tr>
<%--  <fmt:formatDate pattern="dd.MM.yyyy" value="${archiveDateDate}" /> --%>
				<c:forEach var="e" items="${entries}">
					<tr>
					<td width=20%><fmt:formatDate pattern="dd.MM.yyyy hh:mm:ss" value="${e.timestamp}"/></td>
<%-- OLI NÄIN: 					<td width=20%><c:out value="${e.timestamp}"/></td>	--%>	
						<td width=20%><c:out value="${e.user}"/></td>
						<td width=40%><c:out value="${e.operation}"/></td>
						<td width=20%><c:out value="${e.dataItemType}"/></td>
						<td width=20%><c:out value="${e.clientSystemId}"/></td>						
					</tr>
				</c:forEach>
			</table>
		</c:if>
	</c:if>


		<c:if test="${empty entries}">
			<c:if test="${not empty visited}">
				<%-- do not show this on the first visit to this page --%>
				<p>
					<spring:message code="koku.lok.noResults" />
				</p>
			</c:if>
		</c:if>
		<br /> <br />
	</div>
</div><!-- end of koku-lok-div -->