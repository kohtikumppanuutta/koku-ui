<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import = "java.util.Calendar" %>
<%@ page import = "java.text.*" %>
<%@ page import = "com.ixonos.koku.lok.*" %>

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

<% Calendar starttime = Calendar.getInstance(); %>  

 <!--  default starttime is 2 years ago -->
<% starttime.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR)-2); %>

<%! SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT); %>

<div>

<div class="home">
		<a href="${homeUrl}"><spring:message code="koku.common.back" /></a>
</div>

	<h1 class="portlet-section-header"><spring:message code="koku.lok.header.archive"/></h1>
<div class="log-archive">
<c:if test="${empty archiveDate}">
  <form:form name="logArchiveForm" commandName="logArchiveDate" method="post" action="${archiveActionUrl}">

<%-- TODO: T‰h‰n tulee nyt aina aluksi default-p‰iv‰m‰‰r‰ eli 2 vuotta sitten. 
Pit‰isikˆ tulla muutettava p‰iv‰m‰‰r‰? --%>
   <span class="form-field-label"><spring:message code="koku.common.archiveDate" /> </span>
	<form:input path="date" value="<%=df.format(starttime.getTime()) %>"/>
	<span class="errors"><form:errors path="date" /></span>

	<input type="submit" value="<spring:message code="koku.common.ok"/>" >

	<div class="clear" />
  </form:form>
 </div>
</c:if>
 </div>
 
  <div>
  	<c:if test="${not empty archiveDate}">
 
  		<p>Olet valinnut arkistoitavaksi kaikki lokitiedot ennen p‰iv‰m‰‰r‰‰ ${archiveDate}.</p>
  		
  	<form:form name="changeArchiveDateForm" commandName="changeArchiveDate" method="post" action="${archiveActionUrl}">
	<input type="hidden" path="date" value="${archiveDate}"> 
		<input type="submit" value="<spring:message code="koku.common.changeDate"/>" >
	</form:form>
		

	 <form:form method="post" action="${startArchiveActionUrl}">
		<input type="submit" value="<spring:message code="koku.common.startArchive"/>" >
	</form:form>
	</c:if>
  </div>
<p>
<%--  	
<form:form name="logArchiveDateForm" commandName="startArchiveDate" method="post" action="${startArchiveActionUrl}">
	 --%>
  