<%@ include file="imports.jsp" %>

<portlet:renderURL var="homeUrl">
	<portlet:param name="action" value="choose" />
</portlet:renderURL>

<c:set var="ammattilainen" value="${false}" scope="session"/>  

<div class="koku-kks"> 
<div class="portlet-section-body">

	<div class="kks-home">
		<a href="${homeUrl}">Takaisin</a>
	</div>
	
	<div class="kks-reset-floating"></div>

	<div class="title">
		<h1 class="portlet-section-header"><spring:message code="ui.kks.otsikko"/></h1>
		<spring:message code="ui.kks.kuvaus"/>
		
		<h3 class="portlet-section-subheader">
		   <spring:message code="ui.valitse.lapsi"/>
		 </h3>
	</div>

	<div class="kks-collection">
		<c:if test="${not empty childs}">
			<c:forEach var="child" items="${childs}">
				<span class="kks-link">
					<a
						href="
						<portlet:actionURL>
							<portlet:param name="action" value="toChildInfo" />
							<portlet:param name="pic" value="${child.pic}" />
						</portlet:actionURL>">
						 <strong>${ child.lastName }, ${child.firstName} </strong> </a>  ${child.pic}
				</span></br>
			</c:forEach>
		</c:if>
	</div>
	<div class="spacer"></br></div>

</div>
</div>

