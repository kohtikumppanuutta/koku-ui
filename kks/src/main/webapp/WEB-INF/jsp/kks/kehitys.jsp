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

<portlet:defineObjects />

<portlet:renderURL var="kotiUrl">
	<portlet:param name="toiminto" value="naytaLapsi" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
</portlet:renderURL>
<portlet:actionURL var="lisaaActionUrl">
	<portlet:param name="toiminto" value="lisaaKehitysAsia" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
</portlet:actionURL>


<div>

	<div class="home">
		<a href="${kotiUrl}"><spring:message code="ui.takaisin" /> </a>
	</div>

</div>

<div id="main" class="wide">
	<h1>
		${lapsi.nimi}
		<spring:message code="ui.kehitys.otsikko" />
	</h1>

	<div id="mittaus">

		<h2>
			<spring:message code="ui.mittaukset" />
		</h2>

		<c:if test="${not empty mittaukset}">
			<c:forEach var="mittaus" items="${mittaukset}">
				<div class="kokoelma">
					<a
						href="
							<portlet:renderURL>
							<portlet:param name="toiminto" value="naytaKehitysAsia" />
							<portlet:param name="hetu" value="${lapsi.hetu}" />
							<portlet:param name="kehitys" value="${mittaus.id}" />
							
							<c:if test="${ aktiivinen }">
								<portlet:param name="aktiivinen" value="${true}" />
							</c:if>
						</portlet:renderURL>">
						<strong>${mittaus.nimi }</strong> </a> ${ mittaus.muokkaaja } ${
					mittaus.muokkausPvm }
				</div>

			</c:forEach>
		</c:if>



	</div>

	<div id="havainnot">

		<h2>
			<spring:message code="ui.havainnot" />
		</h2>

		<c:if test="${not empty havainnot}">
			<c:forEach var="havainto" items="${havainnot}">
				<div class="kokoelma">
					<a
						href="
							<portlet:renderURL>
							<portlet:param name="toiminto" value="naytaKehitysAsia" />
							<portlet:param name="hetu" value="${lapsi.hetu}" />
							<portlet:param name="kehitys" value="${havainto.id}" />
							<c:if test="${ aktiivinen }">
								<portlet:param name="aktiivinen" value="${true}" />
							</c:if>
						</portlet:renderURL>">
						<strong>${havainto.nimi }</strong> </a> ${ havainto.muokkaaja } ${
					havainto.muokkausPvm }
				</div>

			</c:forEach>
		</c:if>



	</div>

	<div id="arviot">

		<h2>
			<spring:message code="ui.arviot" />
		</h2>

		<c:if test="${not empty arviot}">
			<c:forEach var="arvio" items="${arviot}">
				<div class="kokoelma">
					<a
						href="
							<portlet:renderURL>
							<portlet:param name="toiminto" value="naytaKehitysAsia" />
							<portlet:param name="hetu" value="${lapsi.hetu}" />
							<portlet:param name="kehitys" value="${arvio.id}" />
							
							<c:if test="${ aktiivinen }">
								<portlet:param name="aktiivinen" value="${true}" />
							</c:if>
							
						</portlet:renderURL>">
						<strong>${arvio.nimi }</strong> </a> ${ arvio.muokkaaja } ${
					arvio.muokkausPvm }
				</div>

			</c:forEach>
		</c:if>



	</div>

	<br />


	<div>

		<div class="kokoelma">
			<c:if test="${ sessionScope.ammattilainen && aktiivinen }">
				<a class="tieto"> <spring:message code="ui.lisaa.uusi" /><span
					class="sulje"><spring:message code="ui.piilota" /> </span> </a>
				<div class="tietokentta ">
					<form:form name="lisaaKehitysForm" commandName="kehitys"
						method="post" action="${lisaaActionUrl}">


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

		</div>

	</div>
</div>

<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="http://gsgd.co.uk/sandbox/jquery/easing/jquery.easing.1.4.js"></script>
<script type="text/javascript">
	$(document).ready(function() {

		$(".tietokentta").hide();

		$("a.tieto").click(function() {
			$(this).toggleClass("active").next().slideToggle("fast");
		});

	});
</script>