<%@ include file="imports.jsp" %>

<fmt:setBundle basename="com.ixonos.eservices.koku.bundle.KokuBundle" />

<portlet:defineObjects />

<portlet:renderURL var="homeUrl">
	<portlet:param name="action" value="showChild" />
	<portlet:param name="pic" value="${child.pic}" />
</portlet:renderURL>

<div class="koku-kks"> 
<div class="portlet-section-body">

		<div class="kks-home">
			<a href="${homeUrl}"><spring:message code="ui.kks.back" /> </a>
		</div>
		<div class="kks-reset-floating"></div>


	<h1 class="portlet-section-header"><c:out value="${child.name}"/> <spring:message code="${description}"/> </h1>

    <br></br>

	<div class="kks-content">

		<c:if test="${empty collections }">
			<spring:message code="ui.kks.no.entries" />				
		</c:if>
		
			
		<c:forEach var="result" items="${collections}">
		
			<c:if test="${not empty result.name}">
				<h3 class="portlet-section-subheader">
					<c:out value="${result.name}"/>
		
					<c:if test="${ not result.state.active }">
						<span class="lukittu"> <strong> (<spring:message
									code="ui.kks.locked" />) </strong> </span>
					</c:if>
		
					<c:if test="${ result.state.active }">
						<span class="kks-link kks-right" > <a
							href="
		                     <portlet:renderURL>
		                         <portlet:param name="action" value="showCollection" />
		                         <portlet:param name="pic" value="${child.pic}" />
		                         <portlet:param name="collection" value="${result.id }" />
		                     </portlet:renderURL>">
								<spring:message code="ui.kks.modify" />  </a> </span>
		
					</c:if>
				</h3>
			</c:if>
			<c:forEach var="entry" items='${result.entryValues}'>
				<div class="kks-content">
					<strong>${entry.type.name}</strong> 
					<c:forEach var="val" items='${entry.entryValues}'>
						<div class="kks-read-only-text">
							${val.valuesAsText } 
						</div>
					</c:forEach>
				</div>
			</c:forEach>
			<br></br>
		</c:forEach>
	</div>
</div>

<br/>

</div>
