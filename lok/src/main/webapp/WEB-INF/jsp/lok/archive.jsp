<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<portlet:defineObjects />

<portlet:actionURL var="archiveActionUrl">
	<portlet:param name="op" value="archiveLog" />
</portlet:actionURL>

<portlet:actionURL var="startArchiveActionUrl">
	<portlet:param name="op" value="startArchiveLog" />
</portlet:actionURL>

<portlet:renderURL var="homeUrl">
	<portlet:param name="op" value="choose" />
</portlet:renderURL>


<div>

<div class="home">
		<a href="${homeUrl}"><spring:message code="koku.common.back" /></a>
</div>

	<h1 class="portlet-section-header"><spring:message code="koku.lok.header.archive"/></h1>
<div class="log-archive">
<c:if test="${empty archiveDate}">
  <form:form name="logArchiveForm" commandName="logArchiveDate" method="post" action="${archiveActionUrl}">

   <span class="form-field-label"><spring:message code="koku.common.archiveDate" /> </span>
	<form:input path="date" />
	<span class="errors"><form:errors path="date" /></span>

	<input type="submit" value="<spring:message code="koku.common.ok"/>" >

	<div class="clear" />
  </form:form>
 </div>
</c:if>
 </div>
 
  <div>
  	<c:if test="${not empty archiveDate}">
  		<p>Ennen ${archiveDate} tallennetut lokitiedot arkistoidaan.</p>
  		
  	<form:form name="changeArchiveDateForm" commandName="changeArchiveDate" method="post" action="${archiveActionUrl}">
	
		<input type="submit" value="<spring:message code="koku.common.changeDate"/>" >
	</form:form>
		

	 <form:form method="post" action="${startArchiveActionUrl}">
		<input type="submit" value="<spring:message code="koku.common.startArchive"/>" >
	</form:form>
	</c:if>
  </div>

<%--  	
<form:form name="logArchiveDateForm" commandName="startArchiveDate" method="post" action="${startArchiveActionUrl}">
	 --%>
  