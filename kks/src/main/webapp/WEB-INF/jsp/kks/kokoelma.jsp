<%@page import="com.ixonos.koku.kks.utils.enums.Tietotyyppi"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="vapaa_teksti" value="<%=Tietotyyppi.VAPAA_TEKSTI%>" />
<c:set var="teksti" value="<%=Tietotyyppi.TEKSTI%>" />
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
	<portlet:param name="kokoelma" value="${kokoelma.id}" />
</portlet:actionURL>
<portlet:actionURL var="lisaaMoniArvoinen">
	<portlet:param name="toiminto" value="lisaaMoniarvoinen" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
	<portlet:param name="kokoelma" value="${kokoelma.id}" />
</portlet:actionURL>
<div>

	<div class="home">
		<a href="${kotiUrl}"><spring:message code="ui.takaisin" /> </a>
	</div>
</div>


<h1>
	${lapsi.nimi} ${kokoelma.nimi} 
	<c:if test="${not kokoelma.tila.aktiivinen}">
		(<spring:message code="ui.lukittu" />)
	</c:if>
	 <span style="float: right;">  <c:if	  
			test="${ kokoelma.pohjautuuToiseen }">
			
			<a
				href="
                        <portlet:renderURL>
                            <portlet:param name="toiminto" value="naytaKokoelma" />
                            <portlet:param name="hetu" value="${lapsi.hetu}" />
                            <portlet:param name="kokoelma" value="${kokoelma.edellinenVersio}" />
                        </portlet:renderURL>">
				<strong><spring:message code="ui.edellinen.versio" />
			</strong> </a>
		</c:if> <c:if test="${kokoelma.versioitu}">
			<c:if      
            test="${ kokoelma.pohjautuuToiseen }">
            </br>
            </c:if>
			<a
				href="
                        <portlet:renderURL>
                            <portlet:param name="toiminto" value="naytaKokoelma" />
                            <portlet:param name="hetu" value="${lapsi.hetu}" />
                            <portlet:param name="kokoelma" value="${kokoelma.uudempiVersio}" />
                        </portlet:renderURL>">
				<strong><spring:message code="ui.seuraava.versio" />
			</strong> </a>
		</c:if> </span>
</h1>


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
									<strong>${tyyppi.nimi } <c:if
											test="${tyyppi.moniArvoinen && kokoelma.tila.aktiivinen }">
											<a
												href="
                                                          <portlet:renderURL>
                                                              <portlet:param name="toiminto" value="naytaMoniarvoinen" />
                                                              <portlet:param name="hetu" value="${lapsi.hetu}" />
                                                              <portlet:param name="kokoelma" value="${kokoelma.id}" />
                                                              <portlet:param name="kirjausTyyppi" value="${tyyppi.koodi}" />
                                                          </portlet:renderURL>">
												(<spring:message code="ui.lisaa.moniarvoinen" />...) </a>
										</c:if>
									</strong>

									<c:if test="${ kokoelma.tila.aktiivinen }">
										<c:choose>
											<c:when
												test="${ tyyppi.tietoTyyppi.nimi eq monivalinta.nimi }">
												<span class="monivalinta"> <c:forEach
														items="${tyyppi.arvoJoukko}" var="arvo">
														<form:checkbox title="${tyyppi.kuvaus }"
															path="kirjaukset['${tyyppi.koodi}'].arvot"
															value="${ arvo }" label="${arvo}" />
													</c:forEach> </span>
											</c:when>
											<c:when test="${ tyyppi.tietoTyyppi.nimi eq valinta.nimi }">
												<span class="monivalinta"> <c:forEach
														items="${tyyppi.arvoJoukko}" var="arvo">
														<form:radiobutton title="${tyyppi.kuvaus }"
															path="kirjaukset['${tyyppi.koodi}'].arvot"
															value="${ arvo }" label="${arvo}" />
													</c:forEach> </span>
											</c:when>
											<c:when test="${ tyyppi.tietoTyyppi.nimi eq teksti.nimi }">
												<div class="teksti">
													<form:input title="${tyyppi.kuvaus }" class="add"
														path="kirjaukset['${tyyppi.koodi}'].arvo" />
												</div>
											</c:when>
											<c:otherwise>
												<div class="vapaa.teksti">

													<c:if test="${not tyyppi.moniArvoinen}">
														<form:textarea title="${tyyppi.kuvaus }" class="add"
															path="kirjaukset['${tyyppi.koodi}'].arvo" />
													</c:if>
													<c:if test="${tyyppi.moniArvoinen}">

														<div class="vapaa.teksti">

															<div class="moniarvoiset">

																<c:if
																	test="${empty kokoelma.moniArvoisetKirjaukset[tyyppi.koodi]}">
																	<span class="teksti"><spring:message
																			code="ui.ei.merkintoja" />
																	</span>
																</c:if>

																<c:forEach var="moniarvoinen"
																	items='${ kokoelma.moniArvoisetKirjaukset[tyyppi.koodi] }'>

																	<span class="teksti">${moniarvoinen.arvo} <span><a
																			href="
                                                                            <portlet:renderURL>
                                                                                <portlet:param name="toiminto" value="naytaMoniarvoinen" />
                                                                                <portlet:param name="hetu" value="${lapsi.hetu}" />
                                                                                <portlet:param name="kokoelma" value="${kokoelma.id}" />
                                                                                <portlet:param name="kirjausTyyppi" value="${tyyppi.koodi}" />
                                                                                <portlet:param name="kirjausId" value="${moniarvoinen.id }" />
                                                                            </portlet:renderURL>">
																				(<spring:message code="ui.muokkaa" />...) </a>
																	</span> </span>
																	</br>
																</c:forEach>
															</div>
														</div>
													</c:if>
												</div>
											</c:otherwise>
										</c:choose>
									</c:if>

									<c:if test="${ not kokoelma.tila.aktiivinen }">

										<div class="vapaa.teksti">


											<c:if
												test="${ empty kokoelma.moniArvoisetKirjaukset[tyyppi.koodi] && empty kokoelma.kirjaukset[tyyppi.koodi].arvo }">
												<span class="teksti">-</span>
											</c:if>

											<span class="teksti"> <c:if
													test="${ tyyppi.moniArvoinen }">
													<c:forEach var="moniarvoinen"
														items='${ kokoelma.moniArvoisetKirjaukset[tyyppi.koodi] }'>

														<span class="teksti"> ${moniarvoinen.arvo} </span>
														</br>
													</c:forEach>
												</c:if> <c:if test="${ not tyyppi.moniArvoinen }">
													<c:out value="${kokoelma.kirjaukset[tyyppi.koodi].arvo }"></c:out>
												</c:if> </span>

										</div>
									</c:if>
								</div>
							</c:forEach>
						</c:forEach>
					</c:forEach>
				</c:forEach>

				<div class="clear" />
				<c:if test="${ kokoelma.tila.aktiivinen }">
					<input type="submit"
						value="<spring:message code="ui.tallenna.tieto"/>" class="hae">
				</c:if>
				<div style="clear: both" class="clear" />
			</form:form>

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

	function insertSelection() {
	    var index = document.getElementById("aktivoitavaKentta").selectedIndex;
	    alert("text= " + document.getElementById("aktivoitavaKentta").options[index].text);
		}
</script>