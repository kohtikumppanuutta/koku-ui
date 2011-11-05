<%--
 Copyright 2011 Ixonos Plc, Finland. All rights reserved.

 You should have received a copy of the license text along with this program.
 If not, please contact the copyright holder (http://www.ixonos.com/).
--%>
<%@ include file="imports.jsp" %>

<fmt:setBundle basename="com.ixonos.eservices.koku.bundle.KokuBundle" />

<portlet:defineObjects />

<portlet:renderURL var="homeUrl">
    <portlet:param name="action" value="showChild" />
    <portlet:param name="pic" value="${child.pic}" />
</portlet:renderURL>

<div class="koku-kks"> 
<div class="portlet-section-body">

<!-- Example page what kind of pegasos integration might be -->
    
<div class="kks-home">
    <div class="kks-right">
        <a href="${homeUrl}"><spring:message code="ui.kks.back" /> </a>
    </div>
</div>

<div class="kks-reset-floating"></div>

    <div class="kks-content">

		<h1 class="portlet-section-header">
		    ${child.name} <spring:message code="ui.kks.basic.healthcare.patient.information"/>
		</h1>
        <div class="kks-entry">
            <h2 class="portlet-section-subheader">Ruoka-aine allergiat ja ruokavaliot</h2>   
            
            <div class="portlet-section-text">                 
                 <span class="kks-read-only-text"> 20.06.2011 Hein‰nuha. Liev‰ hein‰nuha, joka on pahimmillaan touko-kes‰kuussa </span>                 
            </div>
            
            <div class="portlet-section-text">                 
                 <span class="kks-read-only-text"> 10.06.2011 V‰h‰hiilihydraattinen ruokavalio. Noudattaa II-tyypin diabeteksen vuoksi kokeilumieless‰ v‰h‰hiilihydraattista ruokavaliota</span>                 
            </div>                  
        </div>
        
        <div class="kks-entry">
            <h2 class="portlet-section-subheader">Sairaudet ja l‰‰kitykset</h2> 
            
            
            <div class="portlet-section-text">                 
                 <span class="kks-read-only-text"> 02.06.2011 Aloitettu kolesteroli l‰‰kitys</span>                 
            </div>
            
            <div class="portlet-section-text">                 
                 <span class="kks-read-only-text"> 01.06.2010 II-tyypin diabetes. Todettu liev‰ II-tyypin diabetes, jota hoidetaan toistaiseksi ruokavaliolla </span>                 
            </div>
                       
        </div>
        
        <div class="kks-entry">
            <h2 class="portlet-section-subheader">Tukitarpeet</h2>     
            
            <div class="portlet-section-text">                 
                 <span class="kks-read-only-text"> 01.07.2010 Tarvitsee edelleen tukea ja kuntoutusta II-tyypin diabeteksen aiheuttamaan liev‰‰ liikkumiskyvyn heikkenemiseen. Suunnitteilla kuntoutusjakso hoitolaitoksessa. </span>                 
            </div>
           
           <div class="portlet-section-text">                 
                 <span class="kks-read-only-text"> 01.06.2010 Tarvitsee tukea ja kuntoutusta II-tyypin diabeteksen aiheuttamaan liev‰‰ liikkumiskyvyn heikkenemiseen. Hoitona 10xkerran kuntoutusk‰ynnit kiropraktikolla. Saanut lainaan k‰velytuen ja k‰velykepin </span>                 
            </div>
        </div>
        
        
        
</div>
</div>
</div>
<br />
