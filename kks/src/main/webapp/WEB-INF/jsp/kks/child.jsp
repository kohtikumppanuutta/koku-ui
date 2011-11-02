<%@ include file="imports.jsp" %>

<fmt:setBundle basename="com.ixonos.eservices.koku.bundle.KokuBundle" />

<portlet:renderURL var="homeUrl">
	<c:if test="${ not sessionScope.municipal_employee }">
		<portlet:param name="action" value="showChildrens" />
	</c:if>
	<c:if test="${ sessionScope.municipal_employee }">
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
<portlet:actionURL var="sendConsentURL">
	<portlet:param name="action" value="sendConsentRequest" />
	<portlet:param name="pic" value="${child.pic}" />
</portlet:actionURL>

<div class="koku-kks"> 
<div class="portlet-section-body">

	<div class="kks-home">
		<a href="${homeUrl}"><spring:message code="ui.kks.back" /> </a>
	</div>

    <div class="kks-reset-floating"></div>
<div >
	<h1 class="portlet-section-header">
		<c:out value="${child.name}"/><c:out value=" "/><spring:message code="ui.kks.title" />
	</h1>

	<div class="kks-error-bindings">    		
    	<c:if test="${not empty error}"><div class="error"><spring:message code="${error}"></spring:message> </div></c:if>
		<spring:hasBindErrors name="creation">
	     <spring:bind path="creation.*">
	       <c:forEach var="error" items="${status.errorMessages}">
	         <div class="error"><c:out value="${error}"/></div>
	       </c:forEach>
	     </spring:bind>
		</spring:hasBindErrors>
	</div>
	<c:if test="${not empty message}">
		<div class="kks-read-only-text"><spring:message code="${message}"></spring:message> 
		</div>
	</c:if>
	<div class="kks-table">	

		<table class="portlet-table-body" width="100%" border="0">
			<tr>
				<th><spring:message code="ui.kks.collection" /></th>
				<th><spring:message code="ui.kks.last.entry" />
				</th>

				<c:if test="${ sessionScope.municipal_employee }">
					<th><spring:message code="ui.kks.entry.state" />
					</th>
					<th><spring:message code="ui.kks.consents" />
					</th>
				</c:if>
			</tr>

			<c:if test="${not empty collections}">
				<c:forEach var="collection" items="${collections}">

					<c:if
						test="${ sessionScope.municipal_employee || not collection.versioned }">
						<tr>
							<td><span class="collection"> <a
									href="
						<portlet:renderURL>
							<portlet:param name="action" value="showCollection" />
							<portlet:param name="pic" value="${child.pic}" />
							<portlet:param name="collection" value="${collection.id}" />
						</portlet:renderURL>">
										<strong><c:out value="${ collection.name }"/></strong> </a> </span></td>
							<td><c:out value="${collection.modifierFullName}"/><c:out value=" "/><fmt:formatDate
									pattern="dd.MM.yyyy" value="${collection.creationTime}" />
							</td>

							<c:if test="${ sessionScope.municipal_employee }">
								<td><c:choose>
										<c:when test="${collection.state.active}">
											<spring:message code="ui.kks.active" />
											<span class="kks-link"> <a
												href="
							                        <portlet:actionURL>
							                            <portlet:param name="action" value="lock" />
							                            <portlet:param name="pic" value="${child.pic}" />
							                            <portlet:param name="collection" value="${collection.id}" />
							                        </portlet:actionURL>">
													<spring:message code="ui.kks.lock" />  </a> </span>
										</c:when>
										<c:otherwise>
											<c:if test="${ not collection.versioned }">
												<spring:message code="ui.kks.locked" />
												<span class="kks-link"> <a
													href="
	                                                    <portlet:actionURL>
	                                                        <portlet:param name="action" value="activate" />
	                                                        <portlet:param name="pic" value="${child.pic}" />
	                                                        <portlet:param name="collection" value="${collection.id}" />
	                                                    </portlet:actionURL>">
														<spring:message code="ui.kks.activate" /> </a> </span>
											</c:if>
											<c:if test="${ collection.versioned }">
												<spring:message code="ui.kks.versioned" />
											</c:if>
										</c:otherwise>
									</c:choose></td>
									<td>
										<c:if test="${not collection.consentRequested}">
					                        <form:form name="sendForm-${collection.id}"  method="post" action="${sendConsentURL}">
					                                <input type="hidden" id="collectionId" name="collectionId" value="${ collection.id }"/>
					                                <input type="hidden" id="consent" name="consent" value="${ collection.collectionClass.consentType }"/>
					                                
					                                <span>
					                                	<input type="submit" class="portlet-form-button" value="<spring:message code="ui.kks.consent.send"/>">
					                                </span>
					
					                        </form:form>
				                        </c:if>
									</td>
							</c:if>
						</tr>
					</c:if>
				</c:forEach>
			</c:if>

		</table>
		<br />
		<div class="kks-collection">
			<div class="kks-link"> <a
				href="
                        <portlet:renderURL>
                            <portlet:param name="action" value="showPegasos" />
                            <portlet:param name="pic" value="${child.pic}" />
                        </portlet:renderURL>">
					<spring:message code="ui.kks.patient.healthcare" /> </a> </div>

            <div class="kks-link"> <a
                href="
                        <portlet:actionURL>
                            <portlet:param name="action" value="searchEntries" />
                            <portlet:param name="pic" value="${child.pic}" />
                            <portlet:param name="classification" value="terveydentila" />
                            <portlet:param name="description" value="ui.kks.terveydentila.query" />
                        </portlet:actionURL>">
                    <spring:message code="ui.kks.healthcare" />  </a> </div>
                    
			<div class="kks-link"> <a
				href="
                        <portlet:actionURL>
                            <portlet:param name="action" value="searchEntries" />
                            <portlet:param name="pic" value="${child.pic}" />
                            <portlet:param name="classification" value="mittaus" />
                            <portlet:param name="description" value="ui.kks.mittaukset.query" />
                        </portlet:actionURL>">
					<spring:message code="ui.kks.measurement" /> </a> </div>
			 <div
				class="kks-link"> <a
				href="
                        <portlet:actionURL>
                            <portlet:param name="action" value="searchEntries" />
                            <portlet:param name="pic" value="${child.pic}" />
                            <portlet:param name="classification" value="koti" />
                            <portlet:param name="description" value="ui.kks.kasvatusta.ohjaavat.tiedot.query" />
                        </portlet:actionURL>">
					<spring:message code="ui.kks.child.rase" /> </a> </div>
			<div class="kks-link"> <a
				href="
                        <portlet:actionURL>
                            <portlet:param name="action" value="searchEntries" />
                            <portlet:param name="pic" value="${child.pic}" />
                            <portlet:param name="classification" value="tuen_tarve, huolenaiheet" />
                            <portlet:param name="description" value="ui.kks.tuen.tarve.query" />
                        </portlet:actionURL>">
					<spring:message code="ui.kks.support.needs" /> </a> </div> 

			<c:if test="${ sessionScope.municipal_employee }">

				<span class="kks-link"> <a
					href="
                        <portlet:actionURL>
                            <portlet:param name="action" value="searchEntries" />
                            <portlet:param name="pic" value="${child.pic}" />
                            <portlet:param name="classification" value="palaute" />
                            <portlet:param name="description" value="ui.kks.palautteet.query" />
                        </portlet:actionURL>">
						<spring:message code="ui.kks.feedback" />  </a> </span>
				<br />


				<span class="kks-link"> <a
					href="
                        <portlet:actionURL>
                            <portlet:param name="action" value="searchEntries" />
                            <portlet:param name="pic" value="${child.pic}" />
                            <portlet:param name="classification" value="toive" />
                            <portlet:param name="description" value="ui.kks.toiveet.query" />
                        </portlet:actionURL>">
						<spring:message code="ui.kks.wishes" />  </a> </span>
				<br />


			</c:if>
		</div>

		<br />


		<div class="activate.collection">

			<div class="collection">
				<c:if test="${ sessionScope.municipal_employee }">
					<a class="create"> <spring:message code="ui.kks.new.contract" /><span
						class="kks-close"><spring:message code="ui.kks.hide" /> </span> </a>
					<div class="kks-fields" style="display: none;">

						<form:form name="creationForm" commandName="creation"
							method="post" action="${creationActionUrl}">
   
								<div class="portlet-form-field-label"><spring:message code="ui.kks.contract.type" />
								</div>

    
								<span class="portlet-form-field"> <form:select id="kks.select"
										path="field" onchange="insertSelection();"
										 >

										<form:option class="portlet-form-input-field" value="" label="" />
										<c:forEach var="creatable" items="${creatables}">
											<c:if test="${creatable.needsVersioning}">
												<form:option value="${creatable.asText}"
													label="${creatable.name}" />
											</c:if>
											<c:if test="${not creatable.needsVersioning}">
                                            <form:option class="portlet-form-input-field" value="${creatable.asText}" label="${creatable.name}" />
                                            </c:if>

										</c:forEach>
									</form:select> </span>
								<div class="portlet-form-field-label"><spring:message code="ui.kks.contract.name" />
								</div>
								<span class="portlet-form-field"><form:input maxlength="250" id="kks.name" path="name" size="70" />
								</span>
							

							<span class="kks-right"> <input type="submit" class="portlet-form-button"
								value="<spring:message code="ui.kks.contract.save"/>"> </span>
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
	
	function insertSelection() {
		var str = document.getElementById("kks.select").value;

		if (str == "") {
			document.getElementById("kks.name").value = "";
		} else {
			document.getElementById("kks.name").value = str.split("#", 10)[2];
		}
	}
</script>
