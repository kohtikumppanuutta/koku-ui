<%@page import="com.ixonos.koku.kks.utils.enums.Tietotyyppi"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="vapaa_teksti" value="<%=Tietotyyppi.VAPAA_TEKSTI%>" />
<c:set var="monivalinta" value="<%=Tietotyyppi.MONIVALINTA%>" />
<c:set var="valinta" value="<%=Tietotyyppi.VALINTA%>" />


<fmt:setBundle basename="com.ixonos.eservices.koku.bundle.KokuBundle" />


<portlet:defineObjects />

<portlet:renderURL var="kotiUrl">
	<portlet:param name="toiminto" value="naytaLapsi" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
</portlet:renderURL>
<portlet:actionURL var="tallennaKirjausActionUrl">
	<portlet:param name="toiminto" value="tallennaKokoelma" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
	<portlet:param name="kokoelma" value="${kokoelma.nimi}" />
</portlet:actionURL>
<div>

	<div class="home">
		<a href="${kotiUrl}"><spring:message code="ui.takaisin" /> </a>
	</div>

</div>


<h1>${lapsi.nimi} ${kokoelma.nimi}</h1>


<div id="kirjaus.tyypit">
	<c:if test="${not empty kokoelma.tyyppi.kirjausTyypit}">

		<form:form name="kirjausForm" commandName="kirjaus" method="post"
			action="${tallennaKirjausActionUrl}">

			<c:forEach var="tyyppi" items="${kokoelma.tyyppi.kirjausTyypit}">

				<h2>${tyyppi.nimi }</h2>
				<c:choose>
					<c:when test="${ tyyppi.tietoTyyppi.nimi eq monivalinta.nimi }">
						<div class="monivalinta">
							<c:forEach items="${tyyppi.arvoJoukko}" var="arvo">
								<form:checkbox path="kirjaukset['${tyyppi.koodi}'].arvot"
									value="${ arvo }" label="${arvo}" />
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>
						<div class="vapaa.teksti">
							<form:textarea class="add" path="kirjaukset['${tyyppi.koodi}'].arvo"/>
						</div>
					</c:otherwise>
				</c:choose>
			</c:forEach>

			<input type="submit"
				value="<spring:message code="ui.tallenna.tieto"/>" class="tallenna">

		</form:form>

	</c:if>




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