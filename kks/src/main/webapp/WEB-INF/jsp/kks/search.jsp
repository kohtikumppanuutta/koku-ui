<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<portlet:defineObjects />


<portlet:renderURL var="showAddEntryUrl">
	<portlet:param name="myaction" value="addEntryForm" />
	<portlet:param name="socialSecurityNumber"
		value="${child.socialSecurityNumber}" />
</portlet:renderURL>
<portlet:renderURL var="homeUrl">
	<portlet:param name="myaction" value="choose" />
</portlet:renderURL>
<portlet:actionURL var="searchActionUrl">
	<portlet:param name="myaction" value="searchChild" />
</portlet:actionURL>


<div>
	<div class="home">
		<a href="${homeUrl}">Takaisin</a>
	</div>

	<div id="search">

		<div id="main" class="wide">
			<h1><spring:message code="ui.kks.title"/></h1>

			<form:form name="addEntryForm" commandName="search" method="post"
				action="${searchActionUrl}">
				<span class="pvm"> HAE LAPSEN TIEDOT: </span>
				<br />
				<span class="pvm"> <spring:message code="ui.form.firstname"/><form:input path="firstName" /><span
					class="errors"><form:errors path="firstName" />
				</span> <spring:message code="ui.form.lastname"/><form:input path="lastName" /><span class="errors"><form:errors
							path="firstName" />
				</span> </span>
				<span class="pvm"><spring:message code="ui.form.ssc"/><form:input
						path="socialSecurityNumber" /><span class="errors"><form:errors
							path="socialSecurityNumber" />
				</span> <br />
				<input value="Hae tiedot" class="tallenna" type="submit"> </span>
				<br>
			</form:form>
		</div>
	</div>
	<br />
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