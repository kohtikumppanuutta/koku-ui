<%@page import="com.ixonos.eservices.koku.kks.utils.enums.UIField"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<portlet:defineObjects />

<portlet:renderURL var="homeUrl">
	<portlet:param name="myaction" value="choose" />
</portlet:renderURL>

<div>

	<div class="home">
		<a href="${homeUrl}">Takaisin</a>
	</div>

	<div id="main" class="wide">
		<h1><spring:message code="ui.kks.title"/></h1>
		<spring:message code="ui.kks.description"/>
		<p><spring:message code="ui.choose.child"/></p>
	</div>

	<div id="childs">
		<c:if test="${not empty childs}">
			<c:forEach var="child" items="${childs}">
				<div class="child.name">
					<a
						href="
						<portlet:actionURL>
							<portlet:param name="myaction" value="routeToChild" />
							<portlet:param name="socialSecurityNumber" value="${child.socialSecurityNumber}" />
						</portlet:actionURL>">
						<strong>${child.lastName }, ${child.firstName}</strong> </a> <span
						class="hetu">${child.socialSecurityNumber}</span>
				</div>
			</c:forEach>
		</c:if>
	</div>

</div>