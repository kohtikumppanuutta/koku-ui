<%@page
	import="com.ixonos.koku.kks.utils.enums.SupportActivity"%>
<%@page
	import="com.ixonos.koku.kks.utils.enums.HealthCondition"%>
<%@page import="com.ixonos.koku.kks.utils.enums.ChildInfo"%>
<%@page
	import="com.ixonos.koku.kks.utils.enums.AdvancementType"%>
<%@page
	import="com.ixonos.koku.kks.utils.enums.AdvancementField"%>
<%@page import="com.ixonos.koku.kks.utils.enums.UIField"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<fmt:setBundle basename="com.ixonos.eservices.koku.bundle.KokuBundle"/>

<c:set var="advancementFieldValues"
	value="<%=AdvancementField.values()%>" />
<c:set var="advancementTypeValues" value="<%=AdvancementType.values()%>" />
<c:set var="childInfoValues" value="<%=ChildInfo.values()%>" />
<c:set var="healthConditionValues" value="<%=HealthCondition.values()%>" />
<c:set var="supportActivityValues" value="<%=SupportActivity.values()%>" />
<c:set var="uiField" value="<%=UIField.ALL%>" />


<portlet:defineObjects />

<portlet:renderURL var="kotiUrl">
	<portlet:param name="toiminto" value="naytaLapset" />
</portlet:renderURL>
<portlet:actionURL var="lisaaActionUrl">
	<portlet:param name="toiminto" value="lisaaTieto" />
	<portlet:param name="hetu"
		value="${lapsi.hetu}" />
</portlet:actionURL>
<portlet:actionURL var="hakuActionUrl">
	<portlet:param name="toiminto" value="hae" />
	<portlet:param name="hetu"
		value="${lapsi.hetu}" />
</portlet:actionURL>


<div>

	<div class="home">
		<a href="${kotiUrl}"><spring:message code="ui.takaisin"/></a>
	</div>

</div>

<div id="page">


	<div id="main" class="wide">
		<h1>${child.fullName} <spring:message code="ui.kks.otsikko"/></h1>
		<a class="tieto"> <spring:message code="ui.lisaa.uusi.kehitystieto"/><span class="sulje"><spring:message code="ui.piilota"/></span>
		</a>
		<div class="tietokentta ">
			<form:form name="addEntryForm" commandName="tieto" method="post"
				action="${lisaaActionUrl}">
				<form:textarea class="add" path="kuvaus"></form:textarea>
				<span class="errors"><form:errors path="kuvaus" /> </span>
			<spring:message code="ui.luokittele.kirjattava.tieto"/>
			<div style="height: 300px;">
					<div class="choose">
						<spring:message code="ui.kehitystieto.kokoelma"/><br>
						<c:forEach items="${advancementFieldValues}" var="afv">
						<spring:message code="${afv.bundleId}" var="afvText"/>
					 	<form:checkbox path="kentat" value="${ afv.id }" label="${afvText}" />
					 	<br/>
					 	</c:forEach>
					</div>
					<div class="choose">
						<spring:message code="ui.lapsen.tiedot"/><br>
						<c:forEach items="${childInfoValues}" var="civ">
						<spring:message code="${civ.bundleId}" var="civText"/>
					 	<form:checkbox path="kentat" value="${ civ.id }" label="${civText}" />
					 	<br/>
					 </c:forEach>
						<span class="errors"><form:errors path="kentat" /> </span>
					</div>
				</div>
				<input type="submit" value="<spring:message code="ui.tallenna.tieto"/>" class="tallenna">
			</form:form>
		</div>

		<form:form name="searchForm" commandName="haku" method="post"
			action="${hakuActionUrl}">
			<span class="pvm"> <spring:message code="ui.nayta.kokoelma"/> <form:select
					path="paaKentta" class="kokoelmavalinta">
					
					<spring:message code="${uiField.bundleId}" var="allText"/>
					<form:option value="${uiField.id}"
						label="${allText}" />
					
					<c:forEach items="${advancementFieldValues}" var="current">
						<spring:message code="${current.bundleId}" var="afText"/>
					 	<form:option value="${current.id}"
						label="${ afText }" />
					 </c:forEach>

				</form:select> </span>
			<a class="tieto small"><spring:message code="ui.tarkempi.haku"/><span class="sulje"><spring:message code="ui.piilota"/></span>
			</a>

			<div class="tietokentta">
				<div style="height: 300px;">
					<div class="choose">
						<spring:message code="ui.kehitystieto.kokoelma"/><br>
						
					<c:forEach items="${advancementFieldValues}" var="afv">
						<spring:message code="${afv.bundleId}" var="afvText"/>
					 	<form:checkbox path="kentat" value="${ afv.id }" label="${afvText}" />
					 	<br/>
					 </c:forEach>
						
					</div>
					<div class="choose">
						<spring:message code="ui.lapsen.tiedot"/><br>
						
						<c:forEach items="${childInfoValues}" var="civ">
						<spring:message code="${civ.bundleId}" var="civText"/>
					 	<form:checkbox path="kentat" value="${ civ.id }" label="${civText}" />
					 	<br/>
					 </c:forEach>
						<span class="errors"><form:errors path="kentat" /> </span>
					</div>
				</div>
				<input type="submit" value="<spring:message code="ui.hae.tiedot"/>" class="tallenna">
			</div>

		</form:form>

		<div id="child">

			<c:if test="${not empty kentat}">

				<c:forEach var="kentta" items="${kentat}">

					<div id="kentta">

						<span class="entry.title"> <strong><fmt:formatDate
									value="${kentta.pvm}" /> ${kentta.lapsi.sukunimi}, ${
								kentta.lapsi.etunimi} </strong> </span>
						<div class="entry.description">
							<c:out value="${kentta.kuvaus}" />
						</div>

						<c:forEach items="${kentta.kentat}" var="tmp" varStatus="status">						
							<spring:message code="${tmp.bundleId}" var="tmp2"/>
							${fn:toUpperCase(tmp2)}
							${not status.last ? ',' : ''}
						</c:forEach>

						<div class="clearer">
							<br>
						</div>
					</div>
				</c:forEach>


			</c:if>

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