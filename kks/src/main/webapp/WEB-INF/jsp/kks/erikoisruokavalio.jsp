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
	<portlet:param name="toiminto" value="muokkaaRuokavaliota" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
	<portlet:param name="ruokavalio" value="${ruokavalio.id}" />
	<portlet:param name="vanha" value="${ruokavalio.id}" />
	<portlet:param name="aktiivinen" value="${aktiivinen}" />
</portlet:actionURL>

<div>

	<div class="home">
		<a href="${kotiUrl}"><spring:message code="ui.takaisin" /> </a>
	</div>

</div>


<div class="kokoelma">

		<h1>${lapsi.nimi} ${ruokavalio.nimi}</h1>
		
	<div class="tietokentta ">

		<c:if test="${ sessionScope.ammattilainen && aktiivinen }">
			<div class="edit">
				<form:form name="muokkaaRuokaForm" commandName="ruokavalio"
					method="post" action="${muokkaaActionUrl}">


					<h2>
						<spring:message code="ui.erikoisruokavalio.syy" />
					</h2>

					<form:select path="properties[peruste].firstValue"
						class="kokoelmavalinta">

						<form:option value="ALLERGIA" label="Allergia" />

						<form:option value="SAIRAUS" label="Sairaus" />

						<form:option value="VAKAUMUKSELLINEN" label="Vakaumuksellinen" />

					</form:select>
					<h2>
						<spring:message code="ui.lisaa.uusi.nimi" />
					</h2>

					<form:input class="add" path="nimi"></form:input>

					<span class="errors"><form:errors path="nimi" /> </span>
					<h2>
						<spring:message code="ui.erikoisruokavalio.kuvaus" />
					</h2>

					<form:textarea class="add" path="properties[kuvaus].firstValue"></form:textarea>
					<span class="errors"><form:errors
							path="properties[kuvaus].firstValue" /> </span>


					<input type="submit"
						value="<spring:message code="ui.tallenna.tieto"/>"
						class="tallenna">
				</form:form>

			</div>
		</c:if>

		<c:if test="${ not sessionScope.ammattilainen }">
			<div class="read">

				<h2>
					<spring:message code="ui.erikoisruokavalio.syy" />
				</h2>
				<c:out value="${ ruokavalio.properties['peruste'].firstValue }" />

				<h2>
					<spring:message code="ui.erikoisruokavalio.kuvaus" />
				</h2>
				<c:out value="${ ruokavalio.properties['kuvaus'].firstValue }" />
				<br />
			</div>
		</c:if>
	</div>
</div>