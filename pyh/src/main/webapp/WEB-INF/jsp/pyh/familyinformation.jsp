<%--
 Copyright 2011 Ixonos Plc, Finland. All rights reserved.

 You should have received a copy of the license text along with this program.
 If not, please contact the copyright holder (http://www.ixonos.com/).
--%>
<%--
	The main view in PYH. Page shows user's family information.
	
	author: Mikko Hurula
--%>

<%@page
	import="fi.koku.services.entity.customerservice.model.CommunityRole"%>
<%@ include file="imports.jsp"%>

<portlet:defineObjects />

<c:set var="DEPENDANT"
	value="<%=fi.koku.services.entity.customerservice.model.CommunityRole.DEPENDANT%>" />

<portlet:renderURL var="editFamilyInformation">
	<portlet:param name="action" value="editFamilyInformation" />
</portlet:renderURL>

<div class="koku-pyh">
	<div class="portlet-section-body">

		<c:choose>
			<c:when test="${empty user}">

				<spring:message code="ui.pyh.no.person.info" />

			</c:when>
			<c:otherwise>

				<div class="pyh-temp">
					<span class="pyh-right"> <a href="${editFamilyInformation}">
							<spring:message code="ui.pyh.modify.info" />
					</a>
					</span>
				</div>

				<div class="pyh-reset-floating"></div>

				<h1 class="portlet-section-header">
					<spring:message code="ui.pyh.own.info" />
				</h1>

				<c:if test="${not empty user}">
					<div class="name">
						<c:out value="${user.firstnames} ${user.surname}" />
					</div>
					<div class="email">
						<spring:message code="ui.pyh.econtactinfo" />
						<c:out value="${user.econtactinfo}" />
					</div>
				</c:if>

				</br>

				<c:if test="${not empty dependants or not empty otherFamilyMembers}">
					<div class="pyh-family">
						<table class="portlet-table-body" width="100%">
							<tr>
								<th><spring:message code="ui.pyh.name" /></th>
								<th><spring:message code="ui.pyh.hetu" /></th>
								<th><spring:message code="ui.pyh.role" /></th>
							</tr>

							<c:forEach var="child" items="${dependants}">
								<tr>
									<td><c:out value="${child.fullName}" /></td>
									<td><c:out value="${child.pic}" /></td>
									<td><spring:message code="ui.pyh.${DEPENDANT.roleID}" />
										<c:if test="${child.memberOfUserFamily}">,&nbsp;<spring:message
												code="ui.pyh.added.into.family" />
										</c:if> <c:if test="${!child.memberOfUserFamily}">,&nbsp;<spring:message
												code="ui.pyh.not.added.into.family" />
										</c:if></td>
								</tr>
							</c:forEach>

							<c:forEach var="familyMember" items="${otherFamilyMembers}">
								<tr>
									<td><c:out value="${familyMember.fullName}" /></td>
									<td><c:out value="${familyMember.pic}" /></td>
									<td><spring:message
											code="ui.pyh.${familyMember.role.roleID}" /></td>
								</tr>
							</c:forEach>
						</table>

					</div>
				</c:if>

				<c:if test="${not empty sentMessages}">
					<h3 class="portlet-section-subheader">
						<spring:message code="ui.pyh.sent.messages" />
					</h3>
					<c:forEach var="sentMessage" items="${sentMessages}">

						<div class="sentMessage">
						
							<div class="sentMessage-left">
								<c:out value="${sentMessage.text}" />
							</div>
							<div class="sentMessage-right">
							<span> <portlet:actionURL var="reject">
										<portlet:param name="action" value="rejectMessage" />
										<portlet:param name="userPic" value="${user.pic}" />
										<portlet:param name="messageId" value="${sentMessage.id}" />
									</portlet:actionURL> <form:form name="reject" method="post" action="${reject}">
										<input type="submit" class="portlet-form-button"
										value="<spring:message code="ui.pyh.sent.messages.remove" />" /></span>
								</form:form>
							</div>						
                        <div class="pyh-reset-floating"></div>
                        </div>

					</c:forEach>
				</c:if>

				<c:if test="${not empty messages}">
					<h3 class="portlet-section-subheader">
						<spring:message code="ui.pyh.messages" />
					</h3>
					<c:forEach var="message" items="${messages}">
						<div class="message">
							<strong> <c:out value="${message.text}" />
							</strong> <span class="pyh-right"> <span class="pyh-right">

									<portlet:actionURL var="reject">
										<portlet:param name="action" value="rejectMessage" />
										<portlet:param name="userPic" value="${user.pic}" />
										<portlet:param name="messageId" value="${message.id}" />
									</portlet:actionURL> <form:form name="reject" method="post" action="${reject}">
										<input type="submit" class="portlet-form-button"
											value="<spring:message code="ui.pyh.deny" />" />
									</form:form>
							</span> <c:if test="${!message.twoParentsInFamily}">
									<span class="pyh-right"> <portlet:actionURL var="accept">
											<portlet:param name="action" value="acceptMessage" />
											<portlet:param name="userPic" value="${user.pic}" />
											<portlet:param name="messageId" value="${message.id}" />
											<portlet:param name="currentFamilyId"
												value="${currentFamilyId}" />
											<portlet:param name="removeCurrentFamily"
												value="${message.memberToAddPic == user.pic}" />
											<portlet:param name="requesterPic" value="${message.from}" />
										</portlet:actionURL> <form:form name="accept" method="post" action="${accept}">
											<input type="submit" class="portlet-form-button"
												value="<spring:message code="ui.pyh.accept" />" />
										</form:form>
									</span>
								</c:if>

							</span>
							<div class="pyh-reset-floating"></div>

							<div class="portlet-section-text">

								<span class="pyh-mail"> <form:form name="accept"
										method="post" action="${accept}">

										<a
											href="mailto:${supportEmailAddress}?subject=<spring:message code="ui.pyh.inappropriate.request.email.subject" />:${message.id}"><spring:message
												code="ui.pyh.report.inappropriate.request" /></a>
									</form:form>
								</span>
							</div>
						</div>

					</c:forEach>
				</c:if>

			</c:otherwise>
		</c:choose>

		<div class="pyh-reset-floating"></div>
		</br>
	</div>
</div>


