<%--
 Copyright 2012 Ixonos Plc, Finland. All rights reserved.
  
 This file is part of Kohti kumppanuutta.
 
 This file is licensed under GNU LGPL version 3.
 Please see the 'license.txt' file in the root directory of the package you received.
 If you did not receive a license, please contact the copyright holder
 (http://www.ixonos.com/).
--%>
<%@ include file="imports.jsp" %>

<portlet:defineObjects />

<portlet:renderURL var="homeURL">
	<portlet:param name="action" value="choose" />
</portlet:renderURL>

<div class="koku-lok">
	<div class="portlet-section-body">
	<c:choose>
	<c:when test="${not empty requestScope.allowedToView}">
	
		<p>
			<spring:message code="koku.lok.archive.success" />
		</p>

		<div class="home">
			<form:form method="post" action="${homeURL}">
				<input type="submit"
					value="<spring:message code="koku.lok.begin"/>">
			</form:form>
		</div>
		<br />
	
	</c:when>

<c:otherwise>
	<spring:message code="koku.lok.no.user.rights" />
</c:otherwise>
</c:choose>

	</div>
</div><!-- end of koku-lok-div -->
