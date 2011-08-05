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
	<portlet:param name="toiminto" value="naytaKokoelma" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
	<portlet:param name="kokoelma" value="${kokoelma.id}" />
</portlet:renderURL>
<portlet:actionURL var="lisaaMoniArvoinen">
	<portlet:param name="toiminto" value="lisaaMoniarvoinen" />
	<portlet:param name="hetu" value="${lapsi.hetu}" />
	<portlet:param name="kokoelma" value="${kokoelma.id}" />
	
	<c:if test="${ not empty kirjausArvo }">
	   <portlet:param name="kirjausId" value="${kirjausArvo.id}" />
	</c:if>
</portlet:actionURL>
<portlet:actionURL var="poistaMoniArvoinen">
    <portlet:param name="toiminto" value="poistaMoniarvoinen" />
    <portlet:param name="hetu" value="${lapsi.hetu}" />
    <portlet:param name="kokoelma" value="${kokoelma.id}" />
    
    <c:if test="${ not empty kirjausArvo }">
       <portlet:param name="kirjausId" value="${kirjausArvo.id}" />
    </c:if>
</portlet:actionURL>
<portlet:actionURL var="peruMoniArvoinen">
    <portlet:param name="toiminto" value="peruMoniarvoinen" />
    <portlet:param name="hetu" value="${lapsi.hetu}" />
    <portlet:param name="kokoelma" value="${kokoelma.id}" />
</portlet:actionURL>

<div class="koku-kks"> 
<div class="portlet-section-body">
<div>

	<div class="kks-home">
		<a href="${kotiUrl}"><spring:message code="ui.takaisin" /> </a>
	</div>

</div>


<h1 class="portlet-section-header">${lapsi.nimi} ${kokoelma.nimi}</h1>


	<div class="kirjaus">

		<form:form name="lisaaMoniArvoinen" 
			method="post" action="${lisaaMoniArvoinen}">
			<input type="hidden" name="kirjausTyyppi" value="${tyyppi.koodi }" />		
			         
				<span class="portlet-form-field-label">
				
				<c:if test="${ not empty kirjausArvo }">
				    <spring:message code="ui.muokkaa.kirjausta" /> ${tyyppi.nimi}
				</c:if>
				
				<c:if test="${ empty kirjausArvo }">
                    <spring:message code="ui.lisaa" /> ${tyyppi.luontiKuvaus}
                </c:if>
				</span>
				<c:if test="${ not empty kirjausArvo }">
				    <span class="kks-right"> <a href="${poistaMoniArvoinen}"><spring:message code="ui.poista" /> </a> </span>
				</c:if>
				
				
				
				<div class="portlet-form-field">

                <c:if test="${ not empty kirjausArvo }">
                <textarea id="arvo" class="portlet-form-input-field" title="${tyyppi.kuvaus }" name="arvo">${kirjausArvo.arvo}</textarea>
                </c:if>
                <c:if test="${ empty kirjausArvo }">
                <textarea id="arvo" class="portlet-form-input-field" title="${tyyppi.kuvaus }" name="arvo"></textarea>
                </c:if>

            </div>

			<span class="kks-right"> 			
			 <input type="submit" class="portlet-form-button"
				value="<spring:message code="ui.sopimus.tallenna"/>"> <span class="kks-right" style="padding-left: 5px">
					<a href="${kotiUrl}"><spring:message code="ui.peruuta" /> </a>  </span> 
			</span>

		</form:form>
	</div>
	
	<div class="kks-reset-floating"></div>

</div>
</div>
