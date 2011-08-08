<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<portlet:defineObjects />

<portlet:renderURL var="home">
    <portlet:param name="op" value="home" />
</portlet:renderURL>

<portlet:renderURL var="archiveURL">
	<portlet:param name="op" value="archiveLog" />
</portlet:renderURL>

<div class="portlet-section-body">

	<div class="home">
			<a href="${home}"><spring:message code="koku.common.back" /></a>
	</div>


	<h1 class="portlet-section-header">
		<spring:message code="koku.lok.header" />
	</h1>	
	
	<div class="portlet-menu">
			<ul>

				<li class="portlet-menu-item-selected"><spring:message code="koku.lok.menu.item.search"/></li>
				<li class="portlet-menu-item"><a href="${archiveURL}"><spring:message code="koku.lok.menu.item.archive"/></a></li>
			</ul>
		</div>
	
	<div class="add">
                    
		<h3 class="portlet-section-subheader"><spring:message code="ui.lok.search.user" /></h3>

		<portlet:actionURL var="searchUserParams">
			<portlet:param name="op" value="searchUserWithParams" />
		</portlet:actionURL>

		<form name="searchUserParams" method="post"
			action="${searchUserParams}">
			<span class="portlet-form-field-label"><spring:message code="ui.lok.first.name" /></span>
			<span class="portlet-form-input-field"> <input type="text" name="fn" id="fn" /> </span>
			<span class="portlet-form-field-label"><spring:message code="ui.lok.last.name" /></span>
			<span class="portlet-form-input-field"> <input type="text" name="sn" /> </span>
			<span class="portlet-form-field-label"><spring:message code="ui.lok.table.ssn" />: </span>
			<span class="portlet-form-input-field"> <input type="text" name="ssn" /> </span>
			<input class="portlet-form-button" type="submit" value="<spring:message code="ui.lok.seek"/>" />
		</form>
	</div>


	<c:choose>
		<c:when test="${not empty searchedUsers}">
			<table class="portlet-table-body" width="100%" border="0">

				<tr class="portlet-table-body th">
					<th width="38%"><spring:message code="ui.lok.table.name" /></th>
					<th width="26%"><spring:message code="ui.lok.table.ssn" /></th>
					<th width="26%"></th>
				</tr>

				<c:forEach var="user" items="${searchedUsers}">

					<portlet:renderURL var="showLogSearchFormURL">
						<portlet:param name="op" value="searchLog" />
						<portlet:param name="ssn" value="${user.ssn}" />
					</portlet:renderURL>

					<tr>
						<td>${user.fname} ${user.sname}</td>
						<td>${user.ssn} </td>
						<td><a href="${showLogSearchFormURL}"><spring:message code="ui.lok.choose.user"/></a></td>
					</tr>

				</c:forEach>
			</table>

			<p>&nbsp;</p>


<%--
			<form:form name="addUsersToFamily" method="post"
				action="${addUsersToFamily}" id="addUsersToFamilyForm">
								<input class="portlet-form-button" type="button"
					value="<spring:message code="ui.pyh.save" />" class="tallenna" onclick="doSubmitForm()" />
			</form:form>
--%>			
		</c:when>
		<c:otherwise>
			<c:if test="${search}">
				<p><spring:message code="ui.lok.no.results" /></p>
			</c:if>
		</c:otherwise>
	</c:choose>

</div>

<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="http://gsgd.co.uk/sandbox/jquery/easing/jquery.easing.1.4.js"></script>
<script type="text/javascript" language="JavaScript">

</script>

