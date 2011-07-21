<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="com.ixonos.koku.kks.utils.enums.Tietotyyppi"%>

<c:set var="vapaa_teksti" value="<%=Tietotyyppi.VAPAA_TEKSTI%>" />
<c:set var="monivalinta" value="<%=Tietotyyppi.MONIVALINTA%>" />
<c:set var="valinta" value="<%=Tietotyyppi.VALINTA%>" />


<fmt:setBundle basename="com.ixonos.eservices.koku.bundle.KokuBundle" />


<portlet:defineObjects />

<portlet:renderURL var="kotiUrl">
	<portlet:param name="toiminto" value="naytaLapsi" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
</portlet:renderURL>
<div id="main" class="wide">
	<div>

		<div class="home">
			<a href="${kotiUrl}"><spring:message code="ui.takaisin" /> </a>
		</div>

	</div>


	<h1>${lapsi.nimi} ${kuvaus}</h1>


	<div id="kirjaus.tyypit.luku">

		<c:if test="${not empty hakutulos }">

			<c:if test="${empty hakutulos.tulokset}">
				<spring:message code="ui.ei.kirjauksia" />
			</c:if>
			<c:forEach var="tulos" items="${hakutulos.tulokset}">

				<c:if test="${not empty tulos.nimi}">
					<h3>
						${tulos.nimi }

						<c:if test="${ not tulos.kokoelmaAktiivinen }">
							<span class="lukittu"> <strong> (<spring:message
										code="ui.lukittu" />) </strong> </span>
						</c:if>

						<c:if test="${ tulos.kokoelmaAktiivinen }">
							<span class="linkki" style="float: right;"> <a
								href="
                        <portlet:renderURL>
                            <portlet:param name="toiminto" value="naytaKokoelma" />
                            <portlet:param name="hetu" value="${lapsi.hetu}" />
                            <portlet:param name="kokoelma" value="${tulos.kokoelmaId }" />
                        </portlet:renderURL>">
									<strong><spring:message code="ui.muokkaa" /> </strong> </a> </span>

						</c:if>
					</h3>
				</c:if>
				<c:forEach var="kirjaus" items='${tulos.kirjaukset}'>
					<div class="kirjaus">
						<strong>${ kirjaus.tyyppi.nimi }</strong> <span class="teksti">${
							kirjaus.arvo } (<fmt:formatDate value="${ kirjaus.luontiAika }" />
							${ kirjaus.kirjaaja })</span>
					</div>
				</c:forEach>
			</c:forEach>
		</c:if>
	</div>
</div>

<br />



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