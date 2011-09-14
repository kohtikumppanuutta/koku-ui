<%@ include file="imports.jsp" %>

<fmt:setBundle basename="com.ixonos.eservices.koku.bundle.KokuBundle" />

<portlet:defineObjects />

<portlet:renderURL var="homeUrl">
	<portlet:param name="action" value="showChild" />
	<portlet:param name="pic" value="${child.pic}" />
</portlet:renderURL>

<div class="koku-kks"> 
<div class="portlet-section-body">

		<div class="kks-home">
			<a href="${homeUrl}"><spring:message code="ui.kks.back" /> </a>
		</div>
		<div class="kks-reset-floating"></div>


	<h1 class="portlet-section-header"><c:out value="${child.name}"/> <c:out value="${description}"/></h1>

</br>

	<div class="kks-content">

		<c:if test="${not empty searchResult}">

			<c:if test="${empty searchResult.results}">
				<spring:message code="ui.kks.no.entries" />
			</c:if>
			
			<c:forEach var="result" items="${searchResult.results}">

				<c:if test="${not empty result.name}">
					<h3 class="portlet-section-subheader">
						<c:out value="${result.name}"/>

						<c:if test="${ not result.collectionActive }">
							<span class="lukittu"> <strong> (<spring:message
										code="ui.kks.locked" />) </strong> </span>
						</c:if>

						<c:if test="${ result.collectionActive }">
							<span class="kks-link kks-right" > <a
								href="
                        <portlet:renderURL>
                            <portlet:param name="action" value="showCollection" />
                            <portlet:param name="pic" value="${child.pic}" />
                            <portlet:param name="collection" value="${result.collectionId}" />
                        </portlet:renderURL>">
									<spring:message code="ui.kks.modify" />  </a> </span>

						</c:if>
					</h3>
				</c:if>
				<c:forEach var="entry" items='${result.entries}'>
					<div class="kks-content">
						<strong>${entry.type.name}</strong> <div class="kks-read-only-text">${
							entry.value } (<fmt:formatDate value="${entry.creationTime}" />
							${entry.recorder})</div>
					</div>
				</c:forEach>
				</br>
			</c:forEach>
		</c:if>
	</div>
</div>

<br />

</div>
