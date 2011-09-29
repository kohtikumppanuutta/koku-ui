<%@ include file="imports.jsp" %>

<portlet:defineObjects />

<portlet:renderURL var="homeURL">
    <portlet:param name="action" value="home" />
    <portlet:param name="user" value="${user}" />
    <portlet:param name="userRole" value="${userRole}" />
</portlet:renderURL>

<portlet:actionURL var="searchUserParamsURL">
	<portlet:param name="action" value="searchUserWithParams" />
	<portlet:param name="user" value="${user}" />
	    <portlet:param name="userRole" value="${userRole}" />
</portlet:actionURL>

<portlet:renderURL var="archiveURL">
	<portlet:param name="action" value="archiveLog" />
	<portlet:param name="user" value="${user}" />
	    <portlet:param name="userRole" value="${userRole}" />
</portlet:renderURL>

<portlet:renderURL var="showLogSearchFormURL">
	<portlet:param name="action" value="searchLog" />
	<portlet:param name="pic" value="${foundPic}" />
	<portlet:param name="user" value="${user}" />
	    <portlet:param name="userRole" value="${userRole}" />
</portlet:renderURL>

<div class="koku-lok">
<div class="portlet-section-body">

	<div class="home">
			<a href="${homeURL}"><spring:message code="koku.common.back" /></a>
	</div>


	<h1 class="portlet-section-header">
		<spring:message code="koku.lok.header" />
	</h1>	
	
	<div class="portlet-menu">
			<ul>
<%-- TODO: POISTA NÄMÄ? Ja samalla kaikki viitteet tältä sivulta arkistointiin --%>
				<li class="portlet-menu-item-selected"><spring:message code="koku.lok.menu.item.search"/></li>
				<li class="portlet-menu-item"><a href="${archiveURL}"><spring:message code="koku.lok.menu.item.archive"/></a></li>
			</ul>
		</div>
	
	<div class="add">
                    
		<h3 class="portlet-section-subheader"><spring:message code="ui.lok.search.user" /></h3>	

		<form name="searchUserParams" method="post"
			action="${searchUserParamsURL}">
			<span class="portlet-form-field-label"><spring:message code="koku.common.pic" />: </span>
			<span class="portlet-form-input-field"> <input type="text" name="pic" /> </span>
			<input class="portlet-form-button" type="submit" value="<spring:message code="ui.lok.seek"/>" />
		</form>
	</div>
<p>
	<c:choose>
		<c:when test="${not empty searchedUsers}">
					<table class="portlet-table-body" width="100%" border="0">

						<tr class="portlet-table-body th">
							<th><spring:message code="ui.lok.table.name" />
							</th>
							<th><spring:message code="koku.common.pic" />
							</th>
							<th></th>
						</tr>


						<tr>
							<td><c:out value="${foundName}" /></td>
							<td><c:out value="${foundPic}" /></td>
							<td><a href="${showLogSearchFormURL}"><spring:message
										code="ui.lok.choose.user" />
							</a>
							</td>
						</tr>
					</table>

					<p>&nbsp;</p>
	
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

</div><!-- end of koku-lok-div -->
