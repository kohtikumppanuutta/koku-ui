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
<portlet:actionURL var="lisaaActionUrl">
	<portlet:param name="toiminto" value="lisaaTukitarve" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
</portlet:actionURL>

<div>

	<div class="home">
		<a href="${kotiUrl}"><spring:message code="ui.takaisin" /> </a>
	</div>

</div>


	<h1>
		${lapsi.nimi}
		<spring:message code="ui.tukitarpeet.otsikko" />
	</h1>

	<div id="page">
		<table border="0">
			<tr>
				<th>TUKITARVE
				</th>
				<th>VIIMEISIN KIRJAUS</th>
			</tr>
			<c:if test="${not empty tukitarpeet}">
				<c:forEach var="tarve" items="${tukitarpeet}">
					<tr>
						<td><span class="kokoelma"> <a
								href="
							<portlet:renderURL>
							<portlet:param name="toiminto" value="naytaTukitarve" />
							<portlet:param name="hetu" value="${lapsi.hetu}" />
							<portlet:param name="tarve" value="${tarve.id}" />
						</portlet:renderURL>">
									<strong>${tarve.nimi }</strong> </a> </span>
						</td>
						<td>${ tarve.muokkaaja } ${ tarve.muokkausPvm }</td>

					</tr>
				</c:forEach>
			</c:if>

		</table>
		<div>

			<div>
				<a class="tieto"> <spring:message
						code="ui.lisaa.uusi.tukitarve" /><span class="sulje"><spring:message
							code="ui.piilota" />
				</span> </a>
				<div class="tietokentta ">
					<form:form name="lisaaTukitarveForm" commandName="tarve" method="post"
						action="${lisaaActionUrl}">
						<h2><spring:message code="ui.lisaa.uusi.tukitarve.nimi" /></h2>
						<form:input class="add" path="nimi"></form:input>
						<span class="errors"><form:errors path="nimi" /> </span>
						<h2><spring:message code="ui.lisaa.uusi.tukitarve.kuvaus" /></h2>
						<form:textarea class="add" path="properties[kuvaus].firstValue"></form:textarea>
						<span class="errors"><form:errors path="properties[kuvaus].firstValue" /> </span>
						<h2><spring:message code="ui.lisaa.uusi.tukitarve.tehtavat" /></h2>
						<form:textarea class="add" path="properties[tehtavat].firstValue"></form:textarea>
						<span class="errors"><form:errors path="properties[tehtavat].firstValue" /> </span>
						
						<input type="submit" value="<spring:message code="ui.tallenna.tieto"/>"
							class="tallenna">
					</form:form>
				</div>
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