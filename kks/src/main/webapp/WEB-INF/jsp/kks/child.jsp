<%@page
	import="com.ixonos.eservices.koku.kks.utils.enums.SupportActivity"%>
<%@page
	import="com.ixonos.eservices.koku.kks.utils.enums.HealthCondition"%>
<%@page import="com.ixonos.eservices.koku.kks.utils.enums.ChildInfo"%>
<%@page
	import="com.ixonos.eservices.koku.kks.utils.enums.AdvancementType"%>
<%@page
	import="com.ixonos.eservices.koku.kks.utils.enums.AdvancementField"%>
<%@page import="com.ixonos.eservices.koku.kks.utils.enums.UIField"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<fmt:setBundle basename="com.ixonos.eservices.koku.bundle.KokuBundle"/>

<c:set var="advancementFieldValues"
	value="<%=AdvancementField.values()%>" />
<c:set var="advancementTypeValues" value="<%=AdvancementType.values()%>" />
<c:set var="childInfoValues" value="<%=ChildInfo.values()%>" />
<c:set var="healthConditionValues" value="<%=HealthCondition.values()%>" />
<c:set var="supportActivityValues" value="<%=SupportActivity.values()%>" />
<c:set var="uiField" value="<%=UIField.ALL%>" />


<portlet:defineObjects />

<portlet:renderURL var="homeUrl">
	<portlet:param name="myaction" value="showChilds" />
</portlet:renderURL>
<portlet:actionURL var="addEntryActionUrl">
	<portlet:param name="myaction" value="addEntry" />
	<portlet:param name="socialSecurityNumber"
		value="${child.socialSecurityNumber}" />
</portlet:actionURL>

<portlet:actionURL var="searchActionUrl">
	<portlet:param name="myaction" value="searchEntries" />
	<portlet:param name="socialSecurityNumber"
		value="${child.socialSecurityNumber}" />
</portlet:actionURL>


<div>

	<div class="home">
		<a href="${homeUrl}"><spring:message code="ui.back"/></a>
	</div>

</div>

<div id="page">


	<div id="main" class="wide">
		<h1>${child.fullName} <spring:message code="ui.kks.title"/></h1>
		<a class="tieto"> <spring:message code="ui.add.new.development.stage"/><span class="sulje"><spring:message code="ui.hide"/></span>
		</a>
		<div class="tietokentta ">
			<form:form name="addEntryForm" commandName="entry" method="post"
				action="${addEntryActionUrl}">
				<form:textarea class="add" path="description"></form:textarea>
				<span class="errors"><form:errors path="description" /> </span>
			<spring:message code="ui.classify.saved.data"/>
			<div style="height: 300px;">
					<div class="choose">
						<spring:message code="ui.development.stage"/><br>
						<c:forEach items="${advancementFieldValues}" var="afv">
						<spring:message code="${afv.bundleId}" var="afvText"/>
					 	<form:checkbox path="fields" value="${ afv.id }" label="${afvText}" />
					 	<br/>
					 	</c:forEach>
					</div>
					<div class="choose">
						<spring:message code="ui.child.info"/><br>
						<c:forEach items="${childInfoValues}" var="civ">
						<spring:message code="${civ.bundleId}" var="civText"/>
					 	<form:checkbox path="fields" value="${ civ.id }" label="${civText}" />
					 	<br/>
					 </c:forEach>
						<span class="errors"><form:errors path="fields" /> </span>
					</div>
				</div>
				<input type="submit" value="<spring:message code="ui.save.info"/>" class="tallenna">
			</form:form>
		</div>

		<form:form name="searchForm" commandName="search" method="post"
			action="${searchActionUrl}">
			<span class="pvm"> <spring:message code="ui.show.collection"/> <form:select
					path="mainField" class="kokoelmavalinta">
					
					<spring:message code="${uiField.bundleId}" var="allText"/>
					<form:option value="${uiField.id}"
						label="${allText}" />
					
					<c:forEach items="${advancementFieldValues}" var="current">
						<spring:message code="${current.bundleId}" var="afText"/>
					 	<form:option value="current.id"
						label="${ afText }" />
					 </c:forEach>

				</form:select> </span>
			<a class="tieto small"><spring:message code="ui.advanced.search"/><span class="sulje"><spring:message code="ui.hide"/></span>
			</a>

			<div class="tietokentta">
				<div style="height: 300px;">
					<div class="choose">
						<spring:message code="ui.development.stage"/><br>
						
					<c:forEach items="${advancementFieldValues}" var="afv">
						<spring:message code="${afv.bundleId}" var="afvText"/>
					 	<form:checkbox path="fields" value="${ afv.id }" label="${afvText}" />
					 	<br/>
					 </c:forEach>
						
					</div>
					<div class="choose">
						<spring:message code="ui.child.info"/><br>
						
						<c:forEach items="${childInfoValues}" var="civ">
						<spring:message code="${civ.bundleId}" var="civText"/>
					 	<form:checkbox path="fields" value="${ civ.id }" label="${civText}" />
					 	<br/>
					 </c:forEach>
						<span class="errors"><form:errors path="fields" /> </span>
					</div>
				</div>
				<input type="submit" value="<spring:message code="ui.fetch.info"/>" class="tallenna">
			</div>

		</form:form>

		<div id="child">

			<c:if test="${not empty entries}">

				<c:forEach var="entry" items="${entries}">

					<div id="entry">

						<span class="entry.title"> <strong><fmt:formatDate
									value="${entry.date}" /> ${entry.child.lastName}, ${
								entry.child.firstName} </strong> </span>
						<div class="entry.description">
							<c:out value="${entry.description}" />
						</div>

						<c:forEach items="${entry.fields}" var="tmp" varStatus="status">						
							<spring:message code="${tmp.bundleId}" var="tmp2"/>
							${fn:toUpperCase(tmp2)}
							${not status.last ? ',' : ''}
						</c:forEach>

						<div class="clearer">
							<br>
						</div>
					</div>
				</c:forEach>


			</c:if>

		</div>
	</div>





	<script type="text/javascript"
		src="http://code.jquery.com/jquery-1.4.4.min.js"></script>
	<script type="text/javascript"
		src="http://gsgd.co.uk/sandbox/jquery/easing/jquery.easing.1.4.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {

			$(".tietokentta").hide();

			$("a.tieto").click(function() {
				$(this).toggleClass("active").next().slideToggle("fast");
			});

		});
	</script>