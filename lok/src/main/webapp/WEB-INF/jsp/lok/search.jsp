<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


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

	<input type="submit" value="search"/>

	<div class="clear" />
  </form:form>

</div>

<div class="log-search-results">
<p>search results here</p>
</div>

</div>
