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

<div class="koku-kks"> 
<div class="portlet-section-body">

	<div class="kks-home">
		<a href="${kotiUrl}">Takaisin</a>
	</div>
	
	<div class="kks-reset-floating"></div>

	<div class="title">
		<h1 class="portlet-section-header"><spring:message code="ui.kks.otsikko"/></h1>
		<spring:message code="ui.kks.kuvaus"/>
		
		<h3 class="portlet-section-subheader">
		   <spring:message code="ui.valitse.lapsi"/>
		 </h3>
	</div>

	<div class="kokoelma">
		<c:if test="${not empty lapset}">
			<c:forEach var="lapsi" items="${lapset}">
				<span class="linkki">
					<a
						href="
						<portlet:actionURL>
							<portlet:param name="toiminto" value="lapsenTietoihin" />
							<portlet:param name="hetu" value="${lapsi.hetu}" />
						</portlet:actionURL>">
						 <strong>${ lapsi.sukunimi }, ${lapsi.etunimi} </strong> </a>  ${lapsi.hetu}
				</span></br>
			</c:forEach>
		</c:if>
	</div>
	<div class="spacer"></br></div>

</div>
</div>

