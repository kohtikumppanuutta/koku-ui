<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<portlet:defineObjects />

<portlet:actionURL var="searchActionUrl">
	<portlet:param name="op" value="searchLog" />
</portlet:actionURL>

<div>

<div class="log-search-criteria">

  <form:form name="logSearchForm" commandName="logSearchCriteria" method="post" action="${searchActionUrl}">
    <span class="form-field-label"><spring:message code="koku.common.pic" /> </span>
	<form:input path="pic" />
	<span class="errors"><form:errors path="pic" /></span>

    <span class="form-field-label"><spring:message code="koku.common.concept" /> </span>
	<form:input path="concept" />
	<span class="errors"><form:errors path="concept" /></span>

    <span class="form-field-label"><spring:message code="koku.common.startTime" /> </span>
	<form:input path="from" />
	<span class="errors"><form:errors path="from" /></span>

    <span class="form-field-label"><spring:message code="koku.common.endTime" /> </span>
	<form:input path="to" />
	<span class="errors"><form:errors path="to" /></span>

	<input type="submit" value="<spring:message code="koku.common.search"/>" >

	<div class="clear" />
  </form:form>

</div>

<div class="log-search-results">

<c:if test="${not empty entries}">
<p><spring:message code="koku.common.searchResults"/></p>
${searchParams.pic}, ${searchParam.concept}, ${searchParams.from}, ${searchParams.to}:
  <ul>
  <c:forEach var="e" items="${entries}">
    ${e.message}&nbsp;
  </c:forEach>
  </ul>
  
</c:if>
<c:if test="${empty entries}">
<p><spring:message code="koku.common.noResults"/></p>
</c:if>

</div>

</div>
