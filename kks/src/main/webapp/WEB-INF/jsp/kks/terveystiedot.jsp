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
	<portlet:param name="toiminto" value="naytaLapsi" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
</portlet:renderURL>
<portlet:actionURL var="lisaaSairausActionUrl">
	<portlet:param name="toiminto" value="lisaaSairaus" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
	<portlet:param name="aktiivinen" value="${aktiivinen}" />
</portlet:actionURL>
<portlet:actionURL var="lisaaErikoisruokaActionUrl">
	<portlet:param name="toiminto" value="lisaaRuokavalio" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
	<portlet:param name="aktiivinen" value="${aktiivinen}" />
</portlet:actionURL>
<portlet:actionURL var="lisaaKayntiActionUrl">
	<portlet:param name="toiminto" value="lisaaKaynti" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
	<portlet:param name="aktiivinen" value="${aktiivinen}" />
</portlet:actionURL>
<div>

	<div class="home">
		<a href="${kotiUrl}"><spring:message code="ui.takaisin" /> </a>
	</div>

</div>


<h1>
	${lapsi.nimi}
	<spring:message code="ui.terveystiedot.otsikko" />
</h1>


<div id="sairaus">

	<h2>
		<spring:message code="ui.sairaudet" />
	</h2>

	<c:if test="${not empty sairaudet}">
		<c:forEach var="sairaus" items="${sairaudet}">
			<div class="kokoelma">
				<a
					href="
							<portlet:renderURL>
							<portlet:param name="toiminto" value="naytaSairaus" />
							<portlet:param name="hetu" value="${lapsi.hetu}" />
							<portlet:param name="sairaus" value="${sairaus.id}" />
							
							<portlet:param name="aktiivinen" value="${ aktiivinen }" />
							
						</portlet:renderURL>">
					<strong>${sairaus.nimi }</strong> </a> ${ sairaus.muokkaaja }
				<fmt:formatDate pattern="dd/MM/yyyy"
					value="${ sairaus.muokkausPvm }" />
			</div>

		</c:forEach>
	</c:if>


	<div>

		<div class="kokoelma">
			<c:if test="${ sessionScope.ammattilainen && aktiivinen }">
				<a class="tieto"> <spring:message code="ui.uusi.sairaus" /><span
					class="sulje"><spring:message code="ui.piilota" /> </span> </a>
				<div class="tietokentta ">
					<form:form name="lisaaSairausForm" commandName="sairaus"
						method="post" action="${lisaaSairausActionUrl}">

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
						<form:textarea class="add"
							path="properties[toimintasuunnitelma].firstValue"></form:textarea>
						<span class="errors"><form:errors
								path="properties[toimintasuunnitelma].firstValue" /> </span>

						<input type="submit"
							value="<spring:message code="ui.tallenna.tieto"/>"
							class="tallenna">
					</form:form>
				</div>
			</c:if>
		</div>
	</div>

</div>

<div id="erikoisruokavaliot">

	<h2>
		<spring:message code="ui.erikoisruokavaliot" />
	</h2>

	<c:if test="${not empty ruokavaliot}">
		<c:forEach var="ruoka" items="${ruokavaliot}">
			<div class="kokoelma">
				<a
					href="
							<portlet:renderURL>
							<portlet:param name="toiminto" value="naytaRuokavalio" />
							<portlet:param name="hetu" value="${lapsi.hetu}" />
							<portlet:param name="ruokavalio" value="${ruoka.id}" />
						
								<portlet:param name="aktiivinen" value="${ aktiivinen }" />
							
						</portlet:renderURL>">
					<strong>${ruoka.nimi }</strong> </a> ${ ruoka.muokkaaja }
				<fmt:formatDate pattern="dd/MM/yyyy"
					value="${ ruoka.muokkausPvm }" />
			</div>

		</c:forEach>
	</c:if>

	<div class="kokoelma">
		<c:if test="${ sessionScope.ammattilainen && aktiivinen }">
			<a class="tieto"> <spring:message
					code="ui.uusi.erikoisruokavalio" /><span class="sulje"><spring:message
						code="ui.piilota" /> </span> </a>
			<div class="tietokentta ">
				<form:form name="lisaaRuokaForm" commandName="ruokavalio"
					method="post" action="${lisaaErikoisruokaActionUrl}">


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
	</div>


</div>

<div id="kaynnit">

	<h2>
		<spring:message code="ui.kaynnit" />
	</h2>

	<c:if test="${not empty kaynnit}">
		<c:forEach var="kaynti" items="${kaynnit}">
			<div class="kokoelma">
				<a
					href="
							<portlet:renderURL>
							<portlet:param name="toiminto" value="naytaKaynti" />
							<portlet:param name="hetu" value="${lapsi.hetu}" />
							<portlet:param name="kaynti" value="${kaynti.id}" />
							
							<portlet:param name="aktiivinen" value="${ aktiivinen }" />
							
						</portlet:renderURL>">
					<strong>${kaynti.nimi }</strong> </a> ${ kaynti.muokkaaja }
				<fmt:formatDate pattern="dd/MM/yyyy" value="${ kaynti.muokkausPvm }" />
			</div>

		</c:forEach>
	</c:if>

	<div class="kokoelma">
		<c:if test="${ sessionScope.ammattilainen && aktiivinen }">
			<a class="tieto"> <spring:message code="ui.uusi.kaynti" /><span
				class="sulje"><spring:message code="ui.piilota" /> </span> </a>
			<div class="tietokentta ">
				<form:form name="muokkaaRuokaForm" commandName="kaynti"
					method="post" action="${lisaaKayntiActionUrl}">


					<h2>
						<spring:message code="ui.kaynti" />
					</h2>

					<form:select path="properties[kaynti].firstValue"
						class="kokoelmavalinta">

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