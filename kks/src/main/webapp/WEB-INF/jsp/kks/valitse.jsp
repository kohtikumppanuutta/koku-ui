<%@ include file="imports.jsp" %>

<portlet:renderURL var="viewChilds">
	<portlet:param name="toiminto" value="naytaLapset" />
</portlet:renderURL>

<portlet:renderURL var="showPro">
	<portlet:param name="toiminto" value="naytaTyontekija" />
	<portlet:param name="lapset" value="" />
</portlet:renderURL>


<portlet:renderURL var="luokittelu">
	<portlet:param name="toiminto" value="naytaLuokittelu" />
</portlet:renderURL>

<div class="koku-kks"> 
<div class="portlet-section-body">

	<div class="kks-left">
		<div >
			<form:form name="guardianForm" method="post" action="${viewChilds}">
				<input type="submit" value="<spring:message code="ui.nayta.huoltaja"/>" />
			</form:form>
		</div>

		<div >
			<form:form name="proForm" method="post" action="${showPro}">
				<input type="submit" value="<spring:message code="ui.nayta.tyontekija"/>" />
			</form:form>
		</div>

	</div>

	<div class="kks-right">
		<form:form name="luokitteluForm" method="post" action="${luokittelu}">
		
		      
			<input type="submit" value="<spring:message code="ui.nayta.luokittelu"/>" />
		</form:form>
	</div>

    <div class="kks-reset-floating"></div>

</div>
</div>
