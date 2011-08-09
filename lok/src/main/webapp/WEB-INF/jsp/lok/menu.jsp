<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<portlet:renderURL var="archive">
	<portlet:param name="op" value="archiveLog" />
</portlet:renderURL>

<portlet:renderURL var="searchUser">
	<portlet:param name="op" value="searchUser" />
</portlet:renderURL>

<portlet:renderURL var="searchArchive">
	<portlet:param name="op" value="searchLog" />
</portlet:renderURL>

<portlet:renderURL var="searchLogViews">
	<portlet:param name="op" value="viewLog" />
</portlet:renderURL>

<div class="koku-lok">
	<h1 class="portlet-section-header">
		<spring:message code="koku.lok.portlet.title" />
	</h1>

	<div style="float: left;">

		<div>
			<h3>Järjestelmänvalvojan toiminnot</h3>


			<form:form name="searchForm" method="post" action="${searchUser}">
				<input type="submit"
					value="<spring:message code="koku.common.search"/>" />
			</form:form>

			<form:form name="logForm" method="post" action="${archive}">
				<input type="submit"
					value="<spring:message code="koku.common.archive"/>" />
			</form:form>

		</div>

		<div>
			<h3>Lokinkäsittelyn valvojan toiminnot</h3>
			<form:form name="viewForm" method="post" action="${searchLogViews}">
				<input type="submit" value="<spring:message code="koku.lok.view"/>" />
			</form:form>
		</div>
	</div>



	<div style="clear: both"></div>

</div><!-- end of koku-lok-div -->