<%@ include file="imports.jsp" %>

<fmt:setBundle basename="com.ixonos.eservices.koku.bundle.KokuBundle" />


<portlet:renderURL var="kotiUrl">

</portlet:renderURL>

<div class="koku-kks"> 
<div class="portlet-section-body">

	<div class="kks-home">
		<a href="${kotiUrl}"><spring:message code="ui.kks.back" /> </a>
	</div>
	<div class="kks-reset-floating"></div>

<div>
	<h1 class="portlet-section-header">OLEMASSAOLEVAT KOKOELMAT</h1>

        
        <div class="custom" style="float: kks-left;padding: 2px">
                <h3 class="portlet-section-subheader">Luokittelut</h3>                
                <span>${classifications}</span>
        </div>
        
        <div class="custom" style="float: kks-left; padding: 2px">
                <h3 class="portlet-section-subheader">Kehitysasialajit</h3>                
                <span>${developmentIssues}</span>
        </div>

        
        <div class="kks-reset-floating"></div>        
            <h1 class="portlet-section-header">KOKOELMIEN LUOKITUKSET</h1>
		<div>
			<c:forEach var="type" items="${collectionTypes}">

				<h2 class="portlet-section-subheader"><c:out value="${ type.name }"/></h2>

				<table class="portlet-table-body" width="100%" border="0">
					<tr>
						<th align="kks-left"><h3>Kentän nimi</h3></th>
						<th align="kks-left"><h3>Tietotyyppi</h3></th>
						<th align="kks-left"><h3>Kehitysasialaji</h3></th>
						<th align="kks-left"><h3>Luokittelu</h3></th>
						
						<th align="kks-left"><h3>Rekisteri</h3></th>
						<th align="kks-left"><h3>Arvojoukko</h3></th>

					</tr>

					<c:forEach var="entry" items="${type.entryTypes}">


						<tr style="padding:5px">
							<td style="padding:2px"><strong>${entry.name}</strong></td>
							<td style="padding:2px">${entry.dataType}</td>
							<td style="padding:2px">${entry.developmentTypesAsText}</td>
							<td style="padding:2px"><strong>${entry.classificationsAsText}</strong></td>
							
							<td style="padding:2px">${entry.register}</td>
							<td style="padding:2px">${entry.valuesAsText}</td>
						</tr>
					</c:forEach>


				</table>
			</c:forEach>
		</div>
		
		<div></br></div>
</div>
</div>
</div>
