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
	<portlet:param name="toiminto" value="naytaLapset" />
</portlet:renderURL>
<portlet:actionURL var="lisaaActionUrl">
	<portlet:param name="toiminto" value="lisaaTieto" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
</portlet:actionURL>
<portlet:actionURL var="aktivointiActionUrl">
	<portlet:param name="toiminto" value="aktivoiKehitystieto" />
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
		<spring:message code="ui.kks.otsikko" />
	</h1>

	<div id="page">
		<table width="100%" border="0">
			<tr>
				<th>TIETOKOKOELMA
				</td>
				<th>VIIMEISIN KIRJAUS
				<th>KIRJAUSTEN TILA
			</tr>

			<c:if test="${not empty tiedot}">
				<c:forEach var="tieto" items="${tiedot}">
					<tr>
						<td><span class="kokoelma"> <a
								href="
						<portlet:actionURL>
							<portlet:param name="toiminto" value="naytaKehitystieto" />
							<portlet:param name="hetu" value="${lapsi.hetu}" />
							<portlet:param name="tyyppi" value="${tieto.tyyppi}" />
						</portlet:actionURL>">
									<strong>${ tieto.nimi }</strong> </a> </span></td>
						<td>${ tieto.muokkaaja } ${ tieto.muokkausPvm }</td>
						<td><c:choose>
								<c:when test="${tieto.tila.aktiivinen}">
						Aktiivinen
					</c:when>
								<c:otherwise>
						Lukittu
					</c:otherwise>
							</c:choose></td>
					</tr>
				</c:forEach>
			</c:if>

		</table>
		<div>

			<form:form name="aktivointiForm" commandName="aktivointi"
				method="post" action="${aktivointiActionUrl}">

				<div>
					<br> AKTIVOI UUSI TIETOKOKOELMA <span class="pvm"> <spring:message
							code="ui.valitse.kokoelma" /> <form:select
							path="aktivoitavaKentta" class="kokoelmavalinta">

							<form:option value="" label="" />


							<c:forEach var="tieto" items="${tiedot}">
								<form:option value="${tieto.tyyppi}" label="${ tieto.nimi }" />
							</c:forEach>
						</form:select> </span>

				</div>

				<div>
					</span>AKTIIVINEN KIRJAUSAIKA: Alkaa (DD/MM/YYYY):

					<form:input path="alkaa" />
					- Loppuu:
					<form:input path="loppuu" />

				</div>
				<span class="viestintiedot"> <input type="submit"
					class="tallenna" value="Aktivoi kokolema"> </span>
			</form:form>
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