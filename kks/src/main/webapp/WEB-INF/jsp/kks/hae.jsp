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

<div  class="portlet-section-body">
	<div class="home">
		<a href="${kotiUrl}">Takaisin</a>
	</div>


	<div class="main">
		<h1 class="portlet-section-header">
			<spring:message code="ui.kks.otsikko" />
		</h1>
        <div class="search">
		<form:form name="haeLapsiForm" commandName="lapsi" method="post"
			action="${hakuActionUrl}">
			
			<h3 class="portlet-section-subheader"><spring:message code="ui.hae.lapsen.tiedot" /></h3>

            <div class="left">
			<span class="portlet-form-field"> 
			 <form:input class="defaultText" title="Sosiaaliturvatunnus" path="hetu" /> 
			</span>
			</div>
			<span class="left"> <input type="submit" class="portlet-form-button" value="<spring:message code="ui.hae.tiedot"/>" ></span>

			<div class="reset-floating" />
		</form:form>
		</div>
</br>
		<div class="kokoelma">
			<c:if test="${not empty lapset}">
				<c:forEach var="lapsi" items="${lapset}">
					<span class="linkki"> <a
						href="
                        <portlet:actionURL>
                            <portlet:param name="toiminto" value="lapsenTietoihin" />
                            <portlet:param name="hetu" value="${lapsi.hetu}" />
                        </portlet:actionURL>">
							<strong>${lapsi.sukunimi }, ${lapsi.etunimi} </strong></a> <span>${lapsi.hetu}</span>
					</span>
				</c:forEach>
			</c:if>
			<c:if test="${empty lapset && not empty haku}">
				<span class="hakutulos"><strong><spring:message code="ui.ei.hakutuloksia" /></strong>
				</span>
			</c:if>
		</div>
	</div>


	<div class="spacer">
		</br>
	</div>
</div>

<script type="text/javascript"
    src="http://code.jquery.com/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
    src="http://gsgd.co.uk/sandbox/jquery/easing/jquery.easing.1.4.js"></script>
<script language="javascript">
$(document).ready(function()
{
    $(".defaultText").focus(function(srcc)
    {
        if ($(this).val() == $(this)[0].title)
        {
            $(this).removeClass("defaultTextActive");
            $(this).val("");
        }
    });
    
    $(".defaultText").blur(function()
    {
        if ($(this).val() == "")
        {
            $(this).addClass("defaultTextActive");
            $(this).val($(this)[0].title);
        }
    });
    
    $(".defaultText").blur();        
});
</script>