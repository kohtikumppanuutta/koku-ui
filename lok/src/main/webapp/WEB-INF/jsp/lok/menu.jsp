<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<portlet:renderURL var="archive">
	<portlet:param name="op" value="archiveLog" />
</portlet:renderURL>

<portlet:renderURL var="searchArchive">
	<portlet:param name="op" value="searchLog" />
</portlet:renderURL>

<portlet:renderURL var="searchLogViews">
	<portlet:param name="op" value="viewLog" />
</portlet:renderURL>
<div>


<h1 class="portlet-section-header"><spring:message code="koku.lok.portlet.title"/></h1>

	<div style="float: left;">
	
		<div>
			<form:form name="logForm" method="post" action="${archive}">
				<input type="submit" value="<spring:message code="koku.common.archive"/>" />
			</form:form>
		</div>

		<div>
			<form:form name="searchForm" method="post" action="${searchArchive}">
				<input type="submit" value="<spring:message code="koku.common.search"/>" />
			</form:form>
		</div>

		<div><form:form name="viewForm" method="post" action="${searchLogViews}">
				<input type="submit" value="<spring:message code="koku.lok.view"/>" />
			</form:form></div>
	</div>

	

    <div style="clear:both"></div>

</div>

<p>