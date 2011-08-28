<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<portlet:defineObjects />

<portlet:renderURL var="homeUrl">
	<portlet:param name="action" value="choose" />
</portlet:renderURL>

<div class="koku-lok">
	<div class="portlet-section-body">
		<p>
			<spring:message code="koku.lok.archivingstatus.success" />
		</p>


		<div class="home">
			<form:form method="post" action="${homeUrl}">
				<input type="submit"
					value="<spring:message code="koku.common.lok.begin"/>">
			</form:form>
		</div>
		<br />

	</div>
</div><!-- end of koku-lok-div -->