<%--
 Copyright 2011 Ixonos Plc, Finland. All rights reserved.

 You should have received a copy of the license text along with this program.
 If not, please contact the copyright holder (http://www.ixonos.com/).
--%>
<%@ include file="imports.jsp" %>

<portlet:renderURL var="homeUrl">
	<portlet:param name="action" value="choose" />
</portlet:renderURL>

<c:set var="municipal_employee" value="${false}" scope="session"/>  

<div class="koku-kks"> 
<div class="portlet-section-body">

	<div class="kks-reset-floating"></div>

	<div class="title">
		<h1 class="portlet-section-header"><spring:message code="ui.kks.title"/></h1>
		<spring:message code="ui.kks.kuvaus"/>
		
		<c:if test="${not empty childs}">
			<h3 class="portlet-section-subheader">
			   <spring:message code="ui.kks.choose.child"/>
			 </h3>
		 </c:if>
		<c:if test="${empty childs}">
			<br></br>
			<span class="kks-read-only-text"><strong><spring:message code="ui.kks.no.dependants"/></strong></span>
		 </c:if>
	</div>

	<div class="kks-collection">
	    
		<c:if test="${not empty childs}">
			<c:forEach var="child" items="${childs}">
				<span class="kks-link">
					<a href="
						<portlet:actionURL>
							<portlet:param name="action" value="toChildInfo" />
							<portlet:param name="pic" value="${child.pic}" />
						</portlet:actionURL>">
						 <c:out value="${child.lastName}"/>, <c:out value="${child.firstName}"/></a>  <c:out value="${child.pic}"/>
				</span>
				<br></br>
			</c:forEach>
		</c:if>
	</div>
	<div class="kks-spacer">
		<br></br>
	</div>

</div>
</div>

