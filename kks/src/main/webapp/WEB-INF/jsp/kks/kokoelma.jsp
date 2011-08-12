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
<portlet:actionURL var="versioiURL">
    <portlet:param name="toiminto" value="versioiKokoelma" />
    <portlet:param name="hetu" value="${lapsi.hetu}" />
</portlet:actionURL>

<div class="koku-kks"> 
<div class="portlet-section-body">

    <div class="kks-left">
    <div class="kokoelma">
                <c:if test="${ sessionScope.ammattilainen && !kokoelma.versioitu }">
                    <a class="create"> <spring:message code="ui.kks.new.version" /><span
                        class="kks-close"><spring:message code="ui.piilota" /> </span> </a>
                    <div class="kks-fields" style="display: none;">
          
                        <form:form name="uusiVersioForm"  method="post" action="${versioiURL}">
                                <input type="hidden" id="id" name="id" value="${ kokoelma.id }"/>
                                <div class="portlet-form-field-label"><spring:message code="ui.sopimus.nimi" /></div>
                                <div class="portlet-form-field" style="width: 98%"><input  class="portlet-form-input-field" type="text" id="nimi" name="nimi" style="width: 100%" value="${ kokoelma.nimi }"/>
                                 
                                </div>
                                <div class="portlet-form-field-label">
                                   <span class="portlet-form-field"><input class="portlet-form-input-field" type="checkbox" id="tyhjenna" name="tyhjenna" value="true"/>
                                    <label class="portlet-form-field-label" for="tyhjenna"> <spring:message code="ui.tyhjenna.kentat" /> </label>
                                   </span>
                                </div>
                                
                                 
                                </span>
                                <span class="kks-right">
                                <input type="submit" class="portlet-form-button"
                                value="<spring:message code="ui.sopimus.luo"/>">
                                </span>

                        </form:form>

                    </div>
                </c:if>
            </div>
    </div>
    
<div class="kks-home">
	<div class="kks-right">
		<a href="${kotiUrl}"><spring:message code="ui.takaisin" /> </a>
	</div>
</div>

<div class="kks-reset-floating"></div>

<div class="kks-left">
<h1 class="portlet-section-header">
    ${lapsi.nimi} ${kokoelma.nimi} 
    <c:if test="${not kokoelma.tila.aktiivinen}">
        (<spring:message code="ui.lukittu" />)
    </c:if>

</h1>
</div>
<div class="kks-right">

         <c:if      
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
        </c:if> 
</div>
<div class="kks-reset-floating"></div>
    <div class="kks-content">

        <c:if test="${not empty kokoelma.tyyppi.kirjausRyhmat}">

            <form:form class="form-wrapper" name="kirjausForm" commandName="kirjaus" method="post"
                action="${tallennaKirjausActionUrl}">

                <c:forEach var="ryhma"
                    items="${kokoelma.tyyppi.sortedGroups}">

                    <c:if test="${not empty ryhma.nimi}">
                        <h2 class="portlet-section-subheader">${ryhma.nimi } t‰ytt‰‰</h2>
                    </c:if>
                    <c:if test="${not empty ryhma.kuvaus}">
                        <div class="portlet-section-text">
                             <span class="kks-read-only-description">${ryhma.kuvaus}</span>
                        </div>
                    </c:if>
                    <c:forEach var="lapsiryhma" items='${ryhma.lapsiryhmat.values}'>

                        <c:if test="${not empty lapsiryhma.nimi}">
                            <h3 class="portlet-section-subheader">${lapsiryhma.nimi }</h3>
                        </c:if>
                        <c:forEach var="tyypit" items='${lapsiryhma.tyypit.values  }'>
                            <c:forEach var="tyyppi" items='${ tyypit }'>
                                <div class="kks-entry">
                                    <span class="portlet-form-field-label">${tyyppi.nimi } <c:if
                                            test="${tyyppi.moniArvoinen && kokoelma.tila.aktiivinen }">
                                            <a
                                                href="javascript:void(0)" onclick="doSubmitNewMulti('${tyyppi.koodi}');">
                                                (<spring:message code="ui.lisaa.moniarvoinen" />) </a>
                                        </c:if>
                                    </span>

                                    <c:if test="${ kokoelma.tila.aktiivinen }">
                                        <c:choose>
                                            <c:when
                                                test="${ tyyppi.tietoTyyppi.nimi eq monivalinta.nimi }">
                                                <div class="portlet-form-field"> <c:forEach
                                                        items="${tyyppi.arvoJoukko}" var="arvo">
                                                        <form:checkbox  class="portlet-form-input-field" title="${tyyppi.kuvaus }"
                                                            path="kirjaukset['${tyyppi.koodi}'].arvot"
                                                            value="${ arvo }" label="${arvo}" />
                                                    </c:forEach> </div>
                                            </c:when>
                                            <c:when test="${ tyyppi.tietoTyyppi.nimi eq valinta.nimi }">
                                                <div class="portlet-form-field"> <c:forEach
                                                        items="${tyyppi.arvoJoukko}" var="arvo">
                                                        <form:radiobutton class="portlet-form-input-field" title="${tyyppi.kuvaus }"
                                                            path="kirjaukset['${tyyppi.koodi}'].arvot"
                                                            value="${ arvo }" label="${arvo}" />
                                                    </c:forEach> </div>
                                            </c:when>
                                            <c:when test="${ tyyppi.tietoTyyppi.nimi eq teksti.nimi }">
                                                <div class="portlet-form-field" >
                                                    <form:input title="${tyyppi.kuvaus }" class="portlet-form-input-field"
                                                        path="kirjaukset['${tyyppi.koodi}'].arvo" />
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="portlet-form-field">
                                                    <c:if test="${not tyyppi.moniArvoinen}">
                                                        <form:textarea class="portlet-form-input-field" title="${tyyppi.kuvaus }" 
                                                            path="kirjaukset['${tyyppi.koodi}'].arvo" />
                                                    </c:if>
                                                    <c:if test="${tyyppi.moniArvoinen}">

                                                        <div class="vapaa.teksti">

                                                            <div class="moniarvoiset">

                                                                <c:if
                                                                    test="${empty kokoelma.moniArvoisetKirjaukset[tyyppi.koodi]}">
                                                                    <div class="portlet-section-text"><spring:message
                                                                            code="ui.ei.merkintoja" />
                                                                    </div>
                                                                </c:if>

                                                                <c:forEach var="moniarvoinen"
                                                                    items='${ kokoelma.moniArvoisetKirjaukset[tyyppi.koodi] }'>
                                                                    
                                                                    <div class="kks-comment">
	                                                                    <span class="kks-entry-value">${moniarvoinen.arvo} <span class="kks-right">
	                                                                    <a
	                                                                            href="javascript:void(0)" onclick="doSubmitForm('${tyyppi.koodi}', '${moniarvoinen.id }' );">
	                                                                                <spring:message code="ui.muokkaa" /> </a>
	                                                                    </span> 
	                                                                    </span>
	                                                                    <div class="portlet-section-text">
	                                                                     <span class="kks-commenter">${moniarvoinen.kirjaaja} <fmt:formatDate type="both" pattern="dd.MM.yyyy hh:mm" value="${moniarvoinen.luontiAika}"/>
	                                                                        </span> 
	                                                                    </div>
                                                                    </div>
                                                                </c:forEach>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>

                                    <c:if test="${ not kokoelma.tila.aktiivinen }">
                                       
                                        <div class="portlet-section-text">
                                        
                                        <c:choose>
                                            <c:when
                                                test="${ empty kokoelma.moniArvoisetKirjaukset[tyyppi.koodi] && empty kokoelma.kirjaukset[tyyppi.koodi].arvo }">
                                                <span class="kks-read-only-text">-</span>
                                            </c:when>
                                            <c:otherwise>
                                             <c:if
                                                    test="${ tyyppi.moniArvoinen }">
                                                    <c:forEach var="moniarvoinen"
                                                        items='${ kokoelma.moniArvoisetKirjaukset[tyyppi.koodi] }'>

                                                        <span class="kks-read-only-text"> ${moniarvoinen.arvo} (${moniarvoinen.kirjaaja} <fmt:formatDate type="both" pattern="dd.MM.yyyy hh:mm" value="${moniarvoinen.luontiAika}"/>)</span>
                                                        
                                                    </c:forEach>
                                                </c:if> <c:if test="${ not tyyppi.moniArvoinen }">
                                                    <span class="kks-read-only-text"> <c:out value="${kokoelma.kirjaukset[tyyppi.koodi].arvo }"></c:out> </span>
                                                </c:if> 
                                                </c:otherwise>
                                        </c:choose>
                                           </div>
                                        
                                    </c:if>
                                </div>
                            </c:forEach>
                        </c:forEach>
                    </c:forEach>
                </c:forEach>

                <div class="kks-right">
                <c:if test="${ kokoelma.tila.aktiivinen }">
                    <input type="submit" class="portlet-form-button"
                        value="<spring:message code="ui.tallenna.tieto"/>" >
                </c:if>
                </div>
                <div class="kks-reset-floating" />
            </form:form>

        </c:if>




    </div>
</div>
<br />

</div>


<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="http://gsgd.co.uk/sandbox/jquery/easing/jquery.easing.1.4.js"></script>
<script type="text/javascript">
	$(document).ready(function() {

		$("a.create").click(function() {
			$(this).toggleClass("active").next().slideToggle("fast");
		});

	});

	function insertSelection() {
	    var index = document.getElementById("aktivoitavaKentta").selectedIndex;
	    alert("text= " + document.getElementById("aktivoitavaKentta").options[index].text);
		}

       function addMultivalueIdToForm( multiId ) {
           $('#kirjaus')
           .append(
                   '<input name="multiValueId" type="hidden" value="' + multiId + '"/>');
       }
       
	   function addTypeToForm(type) {
	        $('#kirjaus')
	                .append(
	                        '<input name="type" type="hidden" value="' + type + '"/>');

	    }

	    function doSubmitNewMulti( tyyppi ) {
	        addTypeToForm(tyyppi);
	        $('#kirjaus').submit();

	    }

	       function doSubmitForm( type, multiId ) {
	            addTypeToForm(type);
	            addMultivalueIdToForm(multiId);
	            $('#kirjaus').submit();

	        }
</script>