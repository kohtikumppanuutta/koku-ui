<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<portlet:defineObjects />


<portlet:renderURL var="naytaLisaaTietoUrl">
	<portlet:param name="toiminto" value="naytaTyontekija" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
</portlet:renderURL>
<portlet:renderURL var="kotiUrl">
	<portlet:param name="toiminto" value="choose" />
</portlet:renderURL>
<portlet:actionURL var="hakuActionUrl">
	<portlet:param name="toiminto" value="haeLapsi" />
</portlet:actionURL>

<c:set var="ammattilainen" value="${true}" scope="session" />

<div>
	<div class="home">
		<a href="${kotiUrl}">Takaisin</a>
	</div>


	<div id="main" class="wide">
		<h1>
			<spring:message code="ui.kks.otsikko" />
		</h1>

		<form:form name="haeLapsiForm" commandName="lapsi" method="post"
			action="${hakuActionUrl}">
			<span class="pvm"> <spring:message code="ui.hae.lapsen.tiedot" /> </span>

			<span class="pvm"> <spring:message code="ui.form.etunimi" />
				<form:input path="etunimi" /> <span class="errors"><form:errors
						path="etunimi" /> </span> <spring:message code="ui.form.sukunimi" /> <form:input
					path="sukunimi" /> <span class="errors"><form:errors
						path="sukunimi" /> </span> <spring:message code="ui.form.hetu" /> <form:input
					path="hetu" /> <span class="errors"><form:errors
						path="hetu" /> </span> <input type="submit" value="<spring:message code="ui.hae.tiedot"/>" >
			</span>

			<div class="clear" />
		</form:form>

		<br />
		<div>
			<c:if test="${not empty lapset}">
				<c:forEach var="lapsi" items="${lapset}">
					<span class="pvm"> <a
						href="
                        <portlet:actionURL>
                            <portlet:param name="toiminto" value="lapsenTietoihin" />
                            <portlet:param name="hetu" value="${lapsi.hetu}" />
                        </portlet:actionURL>">
							${lapsi.sukunimi }, ${lapsi.etunimi} </a> <span>${lapsi.hetu}</span>
					</span>
				</c:forEach>
			</c:if>
			<c:if test="${empty lapset && not empty haku}">
				<span class="hakutulos"><strong><spring:message code="ui.ei.hakutuloksia" /></strong>
				</span>
			</c:if>
		</div>
	</div>


	<div>
		</br>
	</div>
</div>