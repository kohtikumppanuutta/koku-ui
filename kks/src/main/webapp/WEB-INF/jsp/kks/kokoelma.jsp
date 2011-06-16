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


<div id="main" class="wide">
<div id="kirjaus.tyypit">

	<c:if test="${not empty kokoelma.tyyppi.kirjausRyhmat}">


		<form:form name="kirjausForm" commandName="kirjaus" method="post"
			action="${tallennaKirjausActionUrl}">

			<c:forEach var="ryhma"
				items="${kokoelma.tyyppi.kirjausRyhmat.values}">

				<c:if test="${not empty ryhma.nimi}">
					<h2>${ryhma.nimi } t‰ytt‰‰</h2>
				</c:if>
				<c:forEach var="lapsiryhma" items='${ryhma.lapsiryhmat.values}'>

					<c:if test="${not empty lapsiryhma.nimi}">
						<h3>${lapsiryhma.nimi }</h3>
					</c:if>
					<c:forEach var="tyypit" items='${lapsiryhma.tyypit.values  }'>
						<c:forEach var="tyyppi" items='${ tyypit }'>
							<div class="kirjaus">
								<strong>${tyyppi.nimi }</strong>
								<c:choose>
									<c:when test="${ tyyppi.tietoTyyppi.nimi eq monivalinta.nimi }">
										<span class="monivalinta"> <c:forEach
												items="${tyyppi.arvoJoukko}" var="arvo">
												<form:checkbox title="${tyyppi.kuvaus }" path="kirjaukset['${tyyppi.koodi}'].arvot"
													value="${ arvo }" label="${arvo}" />
											</c:forEach> </span>
									</c:when>
									<c:when test="${ tyyppi.tietoTyyppi.nimi eq valinta.nimi }">
                                        <span class="monivalinta"> <c:forEach
                                                items="${tyyppi.arvoJoukko}" var="arvo">
                                                <form:radiobutton title="${tyyppi.kuvaus }" path="kirjaukset['${tyyppi.koodi}'].arvot"
                                                    value="${ arvo }" label="${arvo}" />
                                            </c:forEach> </span>
                                    </c:when>
									<c:otherwise>
										<div class="vapaa.teksti">
											<form:textarea title="${tyyppi.kuvaus }" class="add"
												path="kirjaukset['${tyyppi.koodi}'].arvo" />
										</div>
									</c:otherwise>
								</c:choose>
							</div>
						</c:forEach>
					</c:forEach>
				</c:forEach>
			</c:forEach>

            <div class="clear"/>
			<input type="submit"
				value="<spring:message code="ui.tallenna.tieto"/>" class="hae">

<div class="clear"/>
		</form:form>

	</c:if>




</div>
</div>

<br />




<script type="text/javascript">
	$(document).ready(function() {

		$(".tietokentta").hide();

		$("a.tieto").click(function() {
			$(this).toggleClass("active").next().slideToggle("fast");
		});

	});
</script>