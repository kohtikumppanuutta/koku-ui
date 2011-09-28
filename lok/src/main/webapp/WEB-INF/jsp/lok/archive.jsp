<%@ include file="imports.jsp" %>

<portlet:defineObjects />

<portlet:actionURL var="archiveActionURL">
	<portlet:param name="action" value="archiveLog" />
	<portlet:param name="visited" value="---" /> 
</portlet:actionURL>

<portlet:actionURL var="startArchiveActionURL">
	<portlet:param name="action" value="startArchiveLog" />
</portlet:actionURL>

<portlet:renderURL var="homeURL">
	<portlet:param name="action" value="choose" />
</portlet:renderURL>

<portlet:renderURL var="searchUserURL">
	<portlet:param name="action" value="searchUser" />
</portlet:renderURL>


<div class="koku-lok">
<div class="portlet-section-body">
${endDate}
	<div class="home">
		<a href="${homeURL}"><spring:message code="koku.common.back" />
		</a>
	</div>
	
		<h1 class="portlet-section-header">
			<spring:message code="koku.lok.header.archive" />
		</h1>
		
		<div class="portlet-menu">
			<ul>
				<li class="portlet-menu-item"><a href="${searchUserURL}"><spring:message code="koku.lok.menu.item.search"/></a></li>
				<li class="portlet-menu-item-selected"><spring:message code="koku.lok.menu.item.archive"/></li>
			</ul>
		</div>

		<c:if test="${empty visited}">
		empty visited
		${endDate}
			<div class="log-archive">
				<form:form name="logArchiveForm" commandName="logArchiveDate"
					method="post" action="${archiveActionURL}">

					<%-- TODO: Add error handling! Now the user can give any date and the
					parsing error is shown on the web page. --%> 
					<%-- TODO: Add a javascript date picker here? --%>
					<span class="form-field-label"><spring:message
							code="koku.common.archiveDate" /> </span>
							<form:input path="endDate" value="${endDate}" />
					<span class="errors"><form:errors path="endDate" />
					</span>
						<input type="submit"
						value="<spring:message code="koku.lok.button.archive"/>">

					<div class="clear"></div>
				</form:form>			
			</div>
		</c:if>

		<c:if test="${not empty visited}">
		not empty visited, ${endDate}
			<p>
				<spring:message code="koku.lok.archive.confirmation" />
				<fmt:formatDate pattern="dd.MM.yyyy" value="${archiveDateDate}"/>.
				
			</p>

		<%--	<form:form name="changeArchiveDateForm" commandName="logArchiveDate" --%>
		
			<form:form method="post" action="${archiveActionURL}">
			 	<input type="hidden" name="endDate" value="${endDate}" />
				<input type="submit"
					value="<spring:message code="koku.common.changeDate"/>">
			</form:form>

			<form:form method="post" action="${startArchiveActionURL}">
				<input type="hidden" name="endDate" value="${endDate}" />
				<input type="submit"
					value="<spring:message code="koku.common.startArchive"/>">
			</form:form>
		</c:if>
		<br/>
	<br/>
<%-- 	<c:if test="${not empty error}">
				
				<p>tuntematon virhe
									<%--<spring:message code="koku.lok.archive.noResults" /> --%>
<%--				</p>
	</c:if>
	--%>
	<c:if test="${not empty error}">
	<%-- do not show this on the first visit to this page --%>
				<p>
				<%--${error}--%>
				<spring:message code="${error}" /> 
<%-- 					<spring:message code="koku.lok.archive.nothing.to.archive" />--%>
				</p>
	</c:if>
	</div>
	

</div><!-- end of koku-lok-div -->
