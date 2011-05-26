<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<portlet:defineObjects />

<portlet:renderURL var="kotiUrl">
	<portlet:param name="toiminto" value="choose" />
</portlet:renderURL>

<c:set var="ammattilainen" value="${false}" scope="session"/>  
    
<div>

	<div class="home">
		<a href="${kotiUrl}">Takaisin</a>
	</div>

	<div id="main" class="wide">
		<h1><spring:message code="ui.kks.otsikko"/></h1>
		<spring:message code="ui.kks.kuvaus"/>
		<p><spring:message code="ui.valitse.lapsi"/></p>
	</div>

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
						<strong>${ lapsi.sukunimi }, ${lapsi.etunimi}</strong> </a> <span
						class="hetu">${lapsi.hetu}</span>
				</div>
			</c:forEach>
		</c:if>
	</div>

</div>