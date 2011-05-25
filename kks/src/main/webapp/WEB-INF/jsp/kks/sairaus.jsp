<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<fmt:setBundle basename="com.ixonos.eservices.koku.bundle.KokuBundle" />


<portlet:renderURL var="kotiUrl">
	<portlet:param name="toiminto" value="naytaTerveys" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
</portlet:renderURL>
<portlet:actionURL var="muokkaaActionUrl">
	<portlet:param name="toiminto" value="muokkaaSairautta" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
	<portlet:param name="sairaus" value="${sairaus.id}" />
	<portlet:param name="vanha" value="${sairaus.id}" />
</portlet:actionURL>

<div>

	<div class="home">
		<a href="${kotiUrl}"><spring:message code="ui.takaisin" /> </a>
	</div>

</div>


<div class="kokoelma">

			<div class="tietokentta ">
				<form:form name="muokkaaSairausForm" commandName="sairaus"
					method="post" action="${muokkaaActionUrl}">

					<h2>
						<spring:message code="ui.lisaa.uusi.nimi" />
					</h2>
					<form:input class="add" path="nimi"></form:input>
					<span class="errors"><form:errors path="nimi" /> </span>
					<h2>
						<spring:message code="ui.sairaus.oireet" />
					</h2>
					<form:textarea class="add" path="properties[oireet].firstValue"></form:textarea>
					<span class="errors"><form:errors
							path="properties[oireet].firstValue" /> </span>

					<h2>
						<spring:message code="ui.sairaus.hoito" />
					</h2>
					<form:textarea class="add" path="properties[hoito].firstValue"></form:textarea>
					<span class="errors"><form:errors
							path="properties[hoito].firstValue" /> </span>
							
							
					<h2>
						<spring:message code="ui.sairaus.laake.hoito" />
					</h2>
					<form:textarea class="add" path="properties[laake].firstValue"></form:textarea>
					<span class="errors"><form:errors
							path="properties[laake].firstValue" /> </span>

					<h2>
						<spring:message code="ui.sairaus.toiminta" />
					</h2>
					<form:textarea class="add" path="properties[toimintasuunnitelma].firstValue"></form:textarea>
					<span class="errors"><form:errors
							path="properties[toimintasuunnitelma].firstValue" /> </span>
							
					<input type="submit"
						value="<spring:message code="ui.tallenna.tieto"/>"
						class="tallenna">
				</form:form>
			</div>
		</div>