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

<portlet:actionURL var="searchActionUrl">
	<portlet:param name="op" value="searchLog" />
</portlet:actionURL>

<portlet:renderURL var="homeUrl">
	<portlet:param name="op" value="choose" />
</portlet:renderURL>

<%! Calendar starttime = Calendar.getInstance(); %>  
<!--  default endtime is now -->
<%! Calendar endtime = Calendar.getInstance(); %>
 <!--  default starttime is 1 year ago -->
<% starttime.set(Calendar.YEAR, endtime.get(Calendar.YEAR)-1); %>

<%! SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT); %>

<div>

<div class="home">
		<a href="${homeUrl}"><spring:message code="koku.common.back" /></a>
</div>
	

	
	<h1 class="portlet-section-header"><spring:message code="koku.lok.header"/></h1>
	
	<%-- leave portlet-menu and Tarkastele and Arkistoi buttons out for now --%>
	
<div class="portlet-form-field">	
<div class="portlet-form-field-label"><spring:message code="koku.lok.search.parameters"/>
</div>	
<%-- <div class="log-search-criteria"> --%>

  <form:form name="logSearchForm" commandName="logSearchCriteria" method="post" action="${searchActionUrl}">
    <span class="form-field-label"><spring:message code="koku.common.pic" /> </span>
	<form:input path="pic" />
	<span class="errors"><form:errors path="pic" /></span>

    <span class="form-field-label"><spring:message code="koku.common.concept" /> </span>
	<form:input path="concept" />
	<span class="errors"><form:errors path="concept" /></span>
	
<!--  TODO: Javascript date picker or something like that will be added here! -->
    <span class="form-field-label"><spring:message code="koku.common.startTime" /> </span>
	<form:input path="from" value="<%=df.format(starttime.getTime()) %>"/>
	<span class="errors"><form:errors path="from" /></span>

    <span class="form-field-label"><spring:message code="koku.common.endTime" /> </span>
	<form:input path="to" value="<%=df.format(endtime.getTime()) %>"/>
	<span class="errors"><form:errors path="to" /></span>

	<input type="submit" value="<spring:message code="koku.common.search"/>" >

	<div class="clear" />
  </form:form>




<%-- th { text-align: center; font-weight: bold } --%>
<c:if test="${not empty entries}">
<h2 class="portlet-section-subheader"><spring:message code="koku.common.searchResults"/></h2>
${searchParams.pic}, ${searchParam.concept}, ${searchParams.from}, ${searchParams.to}:
<table class="portlet-table-body" width="100%" border="0">
<%--<tr>
<th width=20% scope="col">Aika</th>
<th width=20% scope="col">Käyttäjä</th>
<th width=20% scope="col">Tapahtumatyyppi</th>
<th width=40% scope="col">Käsitelty tieto</th>
</tr> --%>
<tr>
<%-- TODO! when using <th> the text won't align to left, that's why we use <td> and <b> here now.
This should be changed! --%>

<td width=20% scope="col"><b>Aika</b></th>
<td width=20% scope="col"><b>Käyttäjä</b></th>
<td width=20% scope="col"><b>Tapahtumatyyppi</b></th>
<td width=40% scope="col"><b>Käsitelty tieto</b></th>
</tr>

<c:forEach var="e" items="${entries}">
<tr><td width=20%>${e.timestamp}</td>
<td width=20%>${e.user}</td>
<td width=20%>${e.event_type}</td>
<td width=40%>${e.event_description}</td>
</tr>
</c:forEach>
</table>
</c:if>
</div>
<%-- 
<ul>
  <c:forEach var="e" items="${entries}">
    ${e.message}&nbsp;<br>
  </c:forEach>
  </ul>
  --%>

<c:if test="${empty entries}">
<p><spring:message code="koku.common.noResults"/></p>
</c:if>

<p>

</div>
</div>
