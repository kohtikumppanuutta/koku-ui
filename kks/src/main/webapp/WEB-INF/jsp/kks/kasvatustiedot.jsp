<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<fmt:setBundle basename="com.ixonos.eservices.koku.bundle.KokuBundle" />


<portlet:defineObjects />

<portlet:renderURL var="kotiUrl">
	<portlet:param name="toiminto" value="naytaLapsi" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
</portlet:renderURL>
<portlet:actionURL var="muokkaaActionUrl">
	<portlet:param name="toiminto" value="muokkaaKasvatusTietoa" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
	<portlet:param name="tieto" value="${tieto.tyyppi}" />
</portlet:actionURL>

<div>

	<div class="home">
		<a href="${kotiUrl}"><spring:message code="ui.takaisin" /> </a>
	</div>

</div>

<div id="main" class="wide">
	<h1>${lapsi.nimi} ${tieto.nimi}</h1>

	<div id="page">

		<c:if test="${ not sessionScope.ammattilainen }">
			<div class="edit">
				<div class="tietokentta ">
					<form:form name="muokkaaForm" commandName="tieto" method="post"
						action="${muokkaaActionUrl}">

						<h2>
							<spring:message
								code="ui.kasvatus.tieto.vanhemmille.tarkeat.asiat" />
						</h2>
						<form:textarea class="add"
							path="properties[tarkeat_asiat].firstValue"></form:textarea>
						<span class="errors"><form:errors
								path="properties[tarkeat_asiat].firstValue" /> </span>
						<h2>
							<spring:message
								code="ui.kasvatus.tieto.vanhempien.kasvatukselliset.tavoitteet" />
						</h2>
						<form:textarea class="add"
							path="properties[tavoitteet].firstValue"></form:textarea>
						<span class="errors"><form:errors
								path="properties[tavoitteet].firstValue" /> </span>

						<input type="submit"
							value="<spring:message code="ui.tallenna.tieto"/>"
							class="tallenna">
					</form:form>
				</div>
			</div>
		</c:if>


		<c:if test="${ sessionScope.ammattilainen }">
			<div class="read">
				<h2>
					<spring:message code="ui.kasvatus.tieto.vanhemmille.tarkeat.asiat" />
				</h2>
				<c:out value="${ tieto.properties['tarkeat_asiat'].firstValue }" />

				<h2>
					<spring:message
						code="ui.kasvatus.tieto.vanhempien.kasvatukselliset.tavoitteet" />
				</h2>
				<c:out value="${ tieto.properties['tarkeat_asiat'].firstValue  }" />

				<br />
			</div>
		</c:if>
	</div>
</div>