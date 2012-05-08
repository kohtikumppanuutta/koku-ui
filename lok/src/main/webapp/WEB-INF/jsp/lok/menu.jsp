<%--
 Copyright 2012 Ixonos Plc, Finland. All rights reserved.
  
 This file is part of Kohti kumppanuutta.
 
 This file is licensed under GNU LGPL version 3.
 Please see the 'license.txt' file in the root directory of the package you received.
 If you did not receive a license, please contact the copyright holder
 (http://www.ixonos.com/).
--%>
<%@ include file="imports.jsp" %>

<portlet:renderURL var="archiveURL">
	<portlet:param name="action" value="archiveLog" />
</portlet:renderURL>

<portlet:renderURL var="searchUserURL">
	<portlet:param name="action" value="searchUser" />
</portlet:renderURL>

<portlet:renderURL var="searchLogViewsURL">
	<portlet:param name="action" value="viewLog" />
	<portlet:param name="user" value="${user}" />
    <portlet:param name="userRole" value="${userRole}" />
</portlet:renderURL>

<div class="koku-lok">
<p>

		<c:choose>
			<c:when test="${not empty requestScope.showMenu}">

				<a href="${searchUserURL}"><spring:message
						code="koku.lok.search" />
				</a>
				<p>

					<a href="${archiveURL}"><spring:message code="koku.lok.archive" />
					</a>
			</c:when>

			<c:when test="${not empty requestScope.redirectToSearch}">
				<script type="text/javascript">
					window.location.href = "${searchLogViewsURL}";
				</script>

			</c:when>

			<c:otherwise>
				<spring:message code="koku.lok.no.user.rights" />
			</c:otherwise>
		</c:choose><p>
		
	<div style="clear: both"></div>

</div><!-- end of koku-lok-div -->
