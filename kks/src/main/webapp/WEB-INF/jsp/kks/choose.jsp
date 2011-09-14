<%@ include file="imports.jsp" %>

<portlet:renderURL var="viewChilds">
	<portlet:param name="action" value="showChildrens" />
</portlet:renderURL>

<portlet:renderURL var="showPro">
	<portlet:param name="action" value="showEmployee" />
	<portlet:param name="childs" value="" />
</portlet:renderURL>


<portlet:renderURL var="classification">
	<portlet:param name="action" value="showClassifications" />
</portlet:renderURL>

<div class="koku-kks"> 
<div class="portlet-section-body">

	<div class="kks-left">
		<div >
			<form:form name="guardianForm" method="post" action="${viewChilds}">
				<input type="submit" value="<spring:message code="ui.kks.show.guardian"/>" />
			</form:form>
		</div>

		<div >
			<form:form name="proForm" method="post" action="${showPro}">
				<input type="submit" value="<spring:message code="ui.kks.show.employee"/>" />
			</form:form>
		</div>

	</div>

	<div class="kks-right">
		<form:form name="classificationForm" method="post" action="${classification}">
		
		      
			<input type="submit" value="<spring:message code="ui.kks.show.classification"/>" />
		</form:form>
	</div>

    <div class="kks-reset-floating"></div>

</div>
</div>
