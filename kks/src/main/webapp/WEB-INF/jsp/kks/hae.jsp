<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<portlet:defineObjects />


<portlet:renderURL var="naytaLisaaTietoUrl">
	<portlet:param name="toiminto" value="naytaTyontekija" />
	<portlet:param name="hetu"
		value="${lapsi.hetu}" />
</portlet:renderURL>
<portlet:renderURL var="kotiUrl">
	<portlet:param name="toiminto" value="choose" />
</portlet:renderURL>
<portlet:actionURL var="hakuActionUrl">
	<portlet:param name="toiminto" value="haeLapsi" />
</portlet:actionURL>


<div>
	<div class="home">
		<a href="${kotiUrl}">Takaisin</a>
	</div>

	<div id="search">

		<div id="main" class="wide">
			<h1><spring:message code="ui.kks.otsikko"/></h1>

			<form:form name="haeLapsiForm" commandName="lapsi" method="post"
				action="${hakuActionUrl}">
				<span class="pvm"> HAE LAPSEN TIEDOT: </span>
				<br />
				<span class="pvm"> <spring:message code="ui.form.etunimi"/><form:input path="etunimi" /><span
					class="errors"><form:errors path="etunimi" />
				</span> <spring:message code="ui.form.sukunimi"/><form:input path="sukunimi" /><span class="errors"><form:errors
							path="sukunimi" />
				</span> </span>
				<span class="pvm"><spring:message code="ui.form.hetu"/><form:input
						path="hetu" /><span class="errors"><form:errors
							path="hetu" />
				</span> <br />
				<input value="Hae tiedot" class="tallenna" type="submit"> </span>
				<br>
			</form:form>
		</div>
	</div>
	<br />
	<div id="childs">
		<c:if test="${not empty lapset}">
			<c:forEach var="lapsi" items="${lapset}">
				<div class="child.name">
					<a
						href="
						<portlet:actionURL>
							<portlet:param name="toiminto" value="lapsenTietoihin" />
							<portlet:param name="hetu" value="${lapsi.hetu}" />
						</portlet:actionURL>">
						<strong>${lapsi.sukunimi }, ${lapsi.etunimi}</strong> </a> <span
						class="hetu">${lapsi.hetu}</span>
				</div>
			</c:forEach>
		</c:if>
	</div>
</div>