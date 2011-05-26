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
	<portlet:param name="toiminto" value="naytaTukitoimet" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
</portlet:renderURL>
<portlet:actionURL var="muokkaaActionUrl">
	<portlet:param name="toiminto" value="muokkaaTukitarvetta" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
	<portlet:param name="tarve" value="${tukitarve.id}" />
	<portlet:param name="vanhaNimi" value="${tukitarve.id}" />
	<portlet:param name="aktiivinen" value="${aktiivinen}" />
</portlet:actionURL>

<div>

	<div class="home">
		<a href="${kotiUrl}"><spring:message code="ui.takaisin" /> </a>
	</div>

</div>

<div id="main" class="wide">
	<h1>${lapsi.nimi} ${tukitarve.nimi}</h1>

	<div id="page">

		<div>
			<div class="tietokentta ">

				<c:if test="${ sessionScope.ammattilainen && aktiivinen }">
					<div class="edit">
						<form:form name="muokkaaTukitarveForm" commandName="tukitarve"
							method="post" action="${muokkaaActionUrl}">
							<h2>
								<spring:message code="ui.lisaa.uusi.tukitarve.nimi" />
							</h2>
							<form:input class="add" path="nimi"></form:input>
							<span class="errors"><form:errors path="nimi" /> </span>
							<h2>
								<spring:message code="ui.lisaa.uusi.tukitarve.kuvaus" />
							</h2>
							<form:textarea class="add" path="properties[kuvaus].firstValue"></form:textarea>
							<span class="errors"><form:errors
									path="properties[kuvaus].firstValue" /> </span>
							<h2>
								<spring:message code="ui.lisaa.uusi.tukitarve.tehtavat" />
							</h2>
							<form:textarea class="add" path="properties[tehtavat].firstValue"></form:textarea>
							<span class="errors"><form:errors
									path="properties[tehtavat].firstValue" /> </span>

							<input type="submit"
								value="<spring:message code="ui.tallenna.tieto"/>"
								class="tallenna">
						</form:form>
					</div>
				</c:if>

				<c:if test="${ not sessionScope.ammattilainen || not aktiivinen  }">
					<div class="read">
					
						<h2>
							<spring:message code="ui.lisaa.uusi.tukitarve.kuvaus" />
						</h2>
						<c:out value="${ tukitarve.properties['kuvaus'].firstValue  }" />

						<h2>
							<spring:message code="ui.lisaa.uusi.tukitarve.tehtavat" />
						</h2>
						<c:out value="${ tukitarve.properties['tehtavat'].firstValue  }" />

						<br />
					</div>
				</c:if>
			</div>
		</div>