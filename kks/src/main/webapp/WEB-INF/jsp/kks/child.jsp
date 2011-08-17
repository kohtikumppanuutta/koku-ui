<%@ include file="imports.jsp" %>


<fmt:setBundle basename="com.ixonos.eservices.koku.bundle.KokuBundle" />

<script type="text/javascript">
	function insertSelection() {
		var str = document.getElementById("kks.select").value;

		if (str == "") {
			document.getElementById("kks.nimi").value = "";
		} else {
			document.getElementById("kks.nimi").value = str.split("#", 10)[2];
		}
	}
</script>


<portlet:renderURL var="homeUrl">
	<c:if test="${ not sessionScope.ammattilainen }">
		<portlet:param name="action" value="showChildrens" />
	</c:if>
	<c:if test="${ sessionScope.ammattilainen }">
		<portlet:param name="action" value="showEmployee" />
		<portlet:param name="childs" value="${child.pic}" />
	</c:if>
</portlet:renderURL>
<portlet:actionURL var="creationActionUrl">
	<portlet:param name="action" value="createCollection" />
	<portlet:param name="pic" value="${child.pic}" />
</portlet:actionURL>
<portlet:actionURL var="searchUrl">
	<portlet:param name="action" value="searchEntries" />
	<portlet:param name="pic" value="${child.pic}" />
</portlet:actionURL>


<div class="koku-kks"> 
<div class="portlet-section-body">

	<div class="kks-home">
		<a href="${homeUrl}"><spring:message code="ui.takaisin" /> </a>
	</div>

    <div class="kks-reset-floating"></div>
<div >
	<h1 class="portlet-section-header">
		${child.name}
		<spring:message code="ui.kks.otsikko" />
	</h1>

	<div class="table">
		<table class="portlet-table-body" width="100%" border="0">
			<tr>
				<th align="kks-left"><spring:message code="ui.tietokokoelma" /></th>
				<th align="kks-left"><spring:message code="ui.viimeisin.kirjaus" />
				</th>

				<c:if test="${ sessionScope.ammattilainen }">
					<th align="kks-left"><spring:message code="ui.kirjausten.tila" />
					</th>
				</c:if>
			</tr>

			<c:if test="${not empty collections}">
				<c:forEach var="collection" items="${collections}">

					<c:if
						test="${ sessionScope.ammattilainen || collection.state.active }">
						<tr>
							<td><span class="collection"> <a
									href="
						<portlet:renderURL>
							<portlet:param name="action" value="showCollection" />
							<portlet:param name="pic" value="${child.pic}" />
							<portlet:param name="collection" value="${collection.id}" />
						</portlet:renderURL>">
										<strong>${ collection.name }</strong> </a> </span></td>
							<td>${ collection.modifier } <fmt:formatDate
									pattern="dd/MM/yyyy" value="${ collection.creationTime }" />
							</td>

							<c:if test="${ sessionScope.ammattilainen }">
								<td><c:choose>
										<c:when test="${collection.state.active }">
											<spring:message code="ui.aktiivinen" />
											<span class="linkki"> <a
												href="
							                        <portlet:actionURL>
							                            <portlet:param name="action" value="lock" />
							                            <portlet:param name="pic" value="${child.pic}" />
							                            <portlet:param name="collection" value="${collection.id}" />
							                        </portlet:actionURL>">
													<spring:message code="ui.lukittu" />  </a> </span>
										</c:when>
										<c:otherwise>
											<c:if test="${ not collection.versioned }">
												<spring:message code="ui.lukittu" />
												<span class="linkki"> <a
													href="
	                                                    <portlet:actionURL>
	                                                        <portlet:param name="action" value="activate" />
	                                                        <portlet:param name="pic" value="${child.pic}" />
	                                                        <portlet:param name="collection" value="${collection.id}" />
	                                                    </portlet:actionURL>">
														<spring:message code="ui.aktiivinen" /> </a> </span>
											</c:if>
											<c:if test="${ collection.versioned }">
												<spring:message code="ui.versioitu" />
											</c:if>
										</c:otherwise>
									</c:choose></td>
							</c:if>
						</tr>
					</c:if>
				</c:forEach>
			</c:if>

		</table>
		<br />
		<div class="collection">
			<span class="kks-link"> <a
				href="
                        <portlet:actionURL>
                            <portlet:param name="action" value="searchEntries" />
                            <portlet:param name="pic" value="${child.pic}" />
                            <portlet:param name="classification" value="terveydentila" />
                            <portlet:param name="description" value="Terveydentila" />
                        </portlet:actionURL>">
					<spring:message code="ui.terveydentila" />  </a> </span><br />


			<span class="kks-link"> <a
				href="
                        <portlet:actionURL>
                            <portlet:param name="action" value="searchEntries" />
                            <portlet:param name="pic" value="${child.pic}" />
                            <portlet:param name="classification" value="mittaus" />
                            <portlet:param name="description" value="Mittaukset" />
                        </portlet:actionURL>">
					<spring:message code="ui.mittaus" /> </a> </span><br /> <span
				class="kks-link"> <a
				href="
                        <portlet:actionURL>
                            <portlet:param name="action" value="searchEntries" />
                            <portlet:param name="pic" value="${child.pic}" />
                            <portlet:param name="classification" value="koti" />
                            <portlet:param name="description" value="Kasvatusta ohjaavat tiedot" />
                        </portlet:actionURL>">
					<spring:message code="ui.lapsen.kasvatus" /> </a> </span><br />
			<span class="kks-link"> <a
				href="
                        <portlet:actionURL>
                            <portlet:param name="action" value="searchEntries" />
                            <portlet:param name="pic" value="${child.pic}" />
                            <portlet:param name="classification" value="tuen_tarve, huolenaiheet" />
                            <portlet:param name="description" value="Tuen tarve" />
                        </portlet:actionURL>">
					<spring:message code="ui.tuen.tarpeet" /> </a> </span> <br />

			<c:if test="${ sessionScope.ammattilainen }">

				<span class="kks-link"> <a
					href="
                        <portlet:actionURL>
                            <portlet:param name="action" value="searchEntries" />
                            <portlet:param name="pic" value="${child.pic}" />
                            <portlet:param name="classification" value="palaute" />
                            <portlet:param name="description" value="Palautteet" />
                        </portlet:actionURL>">
						<spring:message code="ui.palautteet" />  </a> </span>
				<br />


				<span class="kks-link"> <a
					href="
                        <portlet:actionURL>
                            <portlet:param name="action" value="searchEntries" />
                            <portlet:param name="pic" value="${child.pic}" />
                            <portlet:param name="classification" value="toive" />
                            <portlet:param name="description" value="Toiveet" />
                        </portlet:actionURL>">
						<spring:message code="ui.toiveet" />  </a> </span>
				<br />


			</c:if>
		</div>

		<br />


		<div class="activate.collection">

			<div class="collection">
				<c:if test="${ sessionScope.ammattilainen }">
					<a class="create"> <spring:message code="ui.sopimus.uusi" /><span
						class="kks-close"><spring:message code="ui.piilota" /> </span> </a>
					<div class="kks-fields" style="display: none;">

						<form:form name="creationForm" commandName="creation"
							method="post" action="${creationActionUrl}">
   
								<div class="portlet-form-field-label"><spring:message code="ui.sopimus.tyyppi" /></div>

    
								<span class="portlet-form-field"> <form:select id="kks.select"
										path="field" onchange="insertSelection();"
										 >

										<form:option class="portlet-form-input-field" value="" label="" />
										<c:forEach var="creatable" items="${creatables}">
											<c:if test="${creatable.needsVersioning}">
												<form:option value="${creatable.asText}"
													label="${ creatable.name } (luo uuden version)" />
											</c:if>
											<c:if test="${not creatable.needsVersioning}">
                                            <form:option class="portlet-form-input-field" value="${creatable.asText}" label="${ creatable.name }" />
                                            </c:if>

										</c:forEach>
									</form:select> </span>
								<div class="portlet-form-field-label"><spring:message code="ui.sopimus.nimi" /></div>
								<span class="portlet-form-field"><form:input  id="kks.nimi" path="name" size="40" />
								</span>
							

							<span class="kks-right"> <input type="submit" class="portlet-form-button"
								value="<spring:message code="ui.sopimus.tallenna"/>"> </span>
						</form:form>

					</div>
				</c:if>
			</div>
		</div>


	</div>
</div>



<div></div>
</div>
</div>

<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="http://gsgd.co.uk/sandbox/jquery/easing/jquery.easing.1.4.js"></script>
<script type="text/javascript">
	$(document).ready(function() {

		$("a.create").click(function() {
			$(this).toggleClass("active").next().slideToggle("fast");
		});

	});
</script>
