<%@page import="com.ixonos.koku.kks.utils.enums.KehitysAsiaTyyppi"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<fmt:setBundle basename="com.ixonos.eservices.koku.bundle.KokuBundle" />

<c:set var="mittaus" value="<%=KehitysAsiaTyyppi.MITTAUS%>" />
<c:set var="havainto" value="<%=KehitysAsiaTyyppi.HAVAINTO%>" />
<c:set var="arvio" value="<%=KehitysAsiaTyyppi.ARVIO%>" />

<portlet:renderURL var="kotiUrl">
	<portlet:param name="toiminto" value="naytaKehitys" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
</portlet:renderURL>
<portlet:actionURL var="muokkaaActionUrl">
	<portlet:param name="toiminto" value="muokkaaKehitysAsiaa" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
	<portlet:param name="kehitys" value="${kehitys.id}" />
	<portlet:param name="vanha" value="${kehitys.id}" />
</portlet:actionURL>

<div>

	<div class="home">
		<a href="${kotiUrl}"><spring:message code="ui.takaisin" /> </a>
	</div>

</div>


<div class="kokoelma">

	<h1>${lapsi.nimi} ${kehitys.nimi}</h1>
	
	<div class="tietokentta ">

		<c:if test="${  sessionScope.ammattilainen && aktiivinen  }">
			<div class="edit">
				<form:form name="muokkaaKehitysForm" commandName="kehitys"
					method="post" action="${muokkaaActionUrl}">


					<h2>
						<spring:message code="ui.lisaa.uusi.kehitys.tyyppi" />
					</h2>

					<form:select path="tyyppi" class="kokoelmavalinta">

						<form:option value="MITTAUS" label="Mittaus" />

						<form:option value="ARVIO" label="Arvio" />

						<form:option value="HAVAINTO" label="Havainto" />

					</form:select>

					<h2>
						<spring:message code="ui.lisaa.uusi.mittaus.nimi" />
					</h2>
					<form:input class="add" path="nimi"></form:input>
					<span class="errors"><form:errors path="nimi" /> </span>
					<h2>
						<spring:message code="ui.lisaa.uusi.mittaus.kuvaus" />
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

		<c:if test="${ not sessionScope.ammattilainen || not aktiivinen  }">
			<div class="read">

				<h2>
					<spring:message code="ui.lisaa.uusi.kehitys.tyyppi" />
				</h2>

				<c:out value="${ kehitys.tyyppi}" />

				<h2>
					<spring:message code="ui.kaynti.kuvaus" />
				</h2>
				<c:out value="${ kehitys.properties['kuvaus'].firstValue  }" />

				<br />
			</div>
		</c:if>

	</div>
</div>