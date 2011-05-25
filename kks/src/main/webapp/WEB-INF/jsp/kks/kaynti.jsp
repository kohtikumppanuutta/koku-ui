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
	<portlet:param name="toiminto" value="muokkaaKayntia" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
	<portlet:param name="kaynti" value="${kaynti.id}" />
	<portlet:param name="vanha" value="${kaynti.id}" />
</portlet:actionURL>

<div>

	<div class="home">
		<a href="${kotiUrl}"><spring:message code="ui.takaisin" /> </a>
	</div>

</div>


<div class="kokoelma">

			<div class="tietokentta ">
				<form:form name="muokkaaRuokaForm" commandName="kaynti"
					method="post" action="${muokkaaActionUrl}">


					<h2>
						<spring:message code="ui.kaynti" />
					</h2>

					<form:select path="properties[kaynti].firstValue" class="kokoelmavalinta">

						<form:option value="NEUVOLA" label="Neuvola" />

						<form:option value="TERVEYDEN_HOITAJA" label="Terveydenhoitaja" />
						
					</form:select>

					<h2>
						<spring:message code="ui.lisaa.uusi.nimi" />
					</h2>
					<form:input class="add" path="nimi"></form:input>
					<span class="errors"><form:errors path="nimi" /> </span>
					<h2>
						<spring:message code="ui.kaynti.kuvaus" />
					</h2>
					<form:textarea class="add" path="properties[kuvaus].firstValue"></form:textarea>
					<span class="errors"><form:errors
							path="properties[kuvaus].firstValue" /> </span>



					<input type="submit"
						value="<spring:message code="ui.tallenna.tieto"/>"
						class="tallenna">
				</form:form>
			</div>
		</div>