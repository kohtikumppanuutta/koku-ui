<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.text.*"%>
<%@ page import="com.ixonos.koku.lok.*"%>

<portlet:defineObjects />

<portlet:actionURL var="viewActionUrl">
	<portlet:param name="op" value="viewLog" />
</portlet:actionURL>

<portlet:renderURL var="homeUrl">
	<portlet:param name="op" value="choose" />
</portlet:renderURL>

<%!Calendar starttime = Calendar.getInstance();%>
<!--  default endtime is now -->
<%!Calendar endtime = Calendar.getInstance();%>
<!--  default starttime is 1 year ago -->
<%
  starttime.set(Calendar.YEAR, endtime.get(Calendar.YEAR) - 1);
%>

<%!SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);%>

<div class="koku-lok">
	<div class="portlet-section-body">

		<div class="home">
			<a href="${homeUrl}"><spring:message code="koku.common.back" />
			</a>
		</div>

		<h1 class="portlet-section-header">
			<spring:message code="koku.lok.header.view" />
		</h1>


		<div class="portlet-form-field">
			<div class="portlet-form-field-label">
				<spring:message code="koku.lok.search.for.view" />
			</div>

			<p>
				<form:form name="logSearchForm" commandName="logSearchCriteria"
					method="post" action="${viewActionUrl}">


					<!--  TODO: Javascript date picker will be added here! -->

					<span class="form-field-label"><spring:message
							code="koku.common.startTime" /> </span>
					<form:input path="from"
						value="<%=df.format(starttime.getTime()) %>" />
					<span class="errors"><form:errors path="from" />
					</span>

					<span class="form-field-label"><spring:message
							code="koku.common.endTime" /> </span>
					<form:input path="to" value="<%=df.format(endtime.getTime()) %>" />
					<span class="errors"><form:errors path="to" />
					</span>

					<input type="submit"
						value="<spring:message code="koku.common.search"/>">

					<div class="clear"></div>
				</form:form>


				<%-- th { text-align: center; font-weight: bold } --%>
				<c:if test="${not empty entries}">
					<h2 class="portlet-section-subheader">
						<spring:message code="koku.lok.view.results.header" />
						<fmt:formatDate pattern="dd.MM.yyyy" value="${searchParams.from}" />
						-
						<fmt:formatDate pattern="dd.MM.yyyy" value="${ searchParams.to }" />
						:
					</h2>

					<%-- TODO: Nämä kentät tarvitaan:
						aikaleima
						käsittelijän nimi
						tapahtumatyyppi
						käsitelty tieto (tapahtumakuvaus ja kohde)
 					--%>
					<table class="portlet-table-body" width="100%" border="0">

						<tr>
							<%-- TODO! when using <th> the text won't align to left, that's why we use <td> and <b> here now.
								This should be changed! --%>

							<th width=20% scope="col"><b>Aikaleima</b>
							</th>
							<th width=20% scope="col"><b>Käsittelijä</b>
							</th>
							<th width=20% scope="col"><b>Tapahtumatyyppi</b>
							</th>
							<th width=40% scope="col"><b>Käsitelty tieto</b>
							</th>
						</tr>

						<c:forEach var="e" items="${entries}">
							<tr>
								<td width=20%>${e.timestamp}</td>
								<td width=20%>${e.user}</td>
								<td width=20%>${e.eventType}</td>
								<td width=40%>${e.eventDescription}</td>
							</tr>
						</c:forEach>
					</table>
				</c:if>
		</div>

		<c:if test="${empty entries}">
			<c:if test="${not empty visited}">
				<%-- do not show this on the first visit to this page --%>
				<p>
					<spring:message code="koku.common.noResults" />
				</p>
			</c:if>
		</c:if>

		<br />
	</div>
</div>
<!-- end of koku-lok-div -->