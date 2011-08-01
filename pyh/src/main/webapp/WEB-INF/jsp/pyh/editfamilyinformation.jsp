<%@page import="com.ixonos.koku.pyh.util.Role"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:set var="dp" value="<%=com.ixonos.koku.pyh.util.Role.DEPENDANT%>" />
<c:set var="CHILD" value="<%=com.ixonos.koku.pyh.util.Role.CHILD%>" />
<c:set var="PARENT" value="<%=com.ixonos.koku.pyh.util.Role.PARENT%>" />
<c:set var="MEMBER"
	value="<%=com.ixonos.koku.pyh.util.Role.FAMILY_MEMBER%>" />
<c:set var="DEPENDANT"
	value="<%=com.ixonos.koku.pyh.util.Role.DEPENDANT%>" />

<portlet:defineObjects />

<portlet:actionURL var="addUsersToFamily">
	<portlet:param name="action" value="addUsersToFamily" />
</portlet:actionURL>

<portlet:actionURL var="changeEmail">
	<portlet:param name="action" value="changeEmail" />
</portlet:actionURL>

<portlet:renderURL var="backURL">
	<portlet:param name="action" value="guardianFamilyInformation" />
</portlet:renderURL>


<portlet:renderURL var="home">
    <portlet:param name="action" value="" />
</portlet:renderURL>



<div class="portlet-section-body">

<div class="back">
  <span> <a
            href="${home}"> Vaihda k‰ytt‰j‰‰</a> </span>
</div>

	<h1 class="portlet-section-header">
		<spring:message code="ui.pyh.family.info" /> <span class="takaisin"><a class="takaisin"
			href="${backURL}"><spring:message code="ui.pyh.back" /></a>
		</span>
	</h1>

	<c:if test="${not empty user}">
		<div class="name">
			${user.firstname} ${user.surname} <br />
		</div>
        <div class="email"><spring:message code="ui.pyh.email" />  ${user.email}</div>
		<br />
	</c:if>
	
	<c:if test="${not empty dependants or not empty otherFamilyMembers}">
	    <div class="family">
        <table class="portlet-table-body" width="100%">
            <tr>
                <th><spring:message code="ui.pyh.name" /></th>
                <th><spring:message code="ui.pyh.hetu" /></th>
                <th><spring:message code="ui.pyh.role" /></th>
                <th><spring:message code="ui.pyh.action" /></th>
            </tr>
            
            <c:forEach var="child" items="${dependants}">
            <tr>
                <td> ${child.fullName} </td>
                <td> ${child.ssn} </td>
                <td> ${dp.text} 
                
                <c:if test="${child.memberOfUserFamily}">
                   (<spring:message code="ui.pyh.added.into.family" />)
                </c:if>
                </td>
                <td> 
                <span class="actions">
                        <span class="link"> <c:choose>
		                        <c:when test="${child.requestBending}">
		                          //nothing at the moment
		                          Odottaa hyv‰ksynt‰‰
		                        </c:when>
                                <c:when test="${child.memberOfUserFamily}">
                                   <span class="link"> 
                                         <portlet:actionURL var="removeFamilyMember">
                                        <portlet:param name="action" value="removeDependant" />
                                        <portlet:param name="familyMemberSSN" value="${child.ssn}" />
                                    </portlet:actionURL>
                            <a href="${removeFamilyMember}"><spring:message code="ui.pyh.remove.family" /></a> </span> 
                                </c:when>
                                <c:otherwise>
                                    <portlet:actionURL var="addDependantAsFamilyMember">
                                        <portlet:param name="action"
                                            value="addDependantAsFamilyMember" />
                                        <portlet:param name="dependantSSN" value="${child.ssn}" />
                                    </portlet:actionURL>
                                    <a href="${addDependantAsFamilyMember}"><spring:message code="ui.pyh.add.into.family" /></a>
                                </c:otherwise>
                            </c:choose> </span> </span>
                </td>
            </tr>
            </c:forEach>
            
            <c:forEach var="familyMember" items="${otherFamilyMembers}">
                <tr>
                <td>${familyMember.fullName} </td>
                <td>${familyMember.ssn} </td>
                <td>${familyMember.role.text}</td>
                <td><span class="linkki">
                    <portlet:actionURL var="removeFamilyMember">
                    <portlet:param name="action" value="removeFamilyMember" />
                    <portlet:param name="familyMemberSSN" value="${familyMember.ssn}" />
                </portlet:actionURL>
                        <a
                        href="${removeFamilyMember}"><spring:message code="ui.pyh.remove.family" /></a>
                    </span></td>
            </tr>
            </c:forEach>
        </table>
    
    
    </div>
    </br>
    </c:if>
<!-- 
	<c:if test="${not empty dependants}">
		<div class="dependants">
			<h3 class="portlet-section-subheader"><spring:message code="ui.pyh.dependants" /></h3>
			<c:forEach var="dependant" items="${dependants}">
				<div class="dependant">
					<span class="name">${dependant.firstname}
						${dependant.surname} ${dependant.ssn}</span> 
				</div>
			</c:forEach>
		</div>
		<br />
	</c:if>

	<c:if test="${not empty otherFamilyMembers}">
		<div class="members">
			<h3 class="portlet-section-subheader"><spring:message code="ui.pyh.other.family" /></h3>
			<c:forEach var="familyMember" items="${otherFamilyMembers}">

				<portlet:actionURL var="removeFamilyMember">
					<portlet:param name="action" value="removeFamilyMember" />
					<portlet:param name="familyMemberSSN" value="${familyMember.ssn}" />
				</portlet:actionURL>

				<div class="member">
					<span class="name">${familyMember.firstname}
						${familyMember.surname} ${familyMember.ssn}
						(${familyMember.role.text}) </span> <span class="linkki"><a
						href="${removeFamilyMember}"><spring:message code="ui.pyh.remove.family" /></a>
					</span>
				</div>

			</c:forEach>
		</div>
	</c:if>
	
	 -->
	
	    <c:if test="${not empty messages}">
        <h3 class="portlet-section-subheader"><spring:message code="ui.pyh.sent.messages" />
        </h3>
        <c:forEach var="message" items="${messages}">            
            
            <div class="message">
                ${message.text}  <spring:message code="ui.pyh.waiting.approval" />
            </div>
            
        </c:forEach>
        </br>
    </c:if>
    
	
	<div class="add">
                    
		<h3 class="portlet-section-subheader"><spring:message code="ui.pyh.add.family" /></h3>

		<portlet:actionURL var="searchUsers">
			<portlet:param name="action" value="searchUsers" />
		</portlet:actionURL>

		<form:form name="searchUsersForm" method="post"
			action="${searchUsers}">
			<span class="portlet-form-field-label"><spring:message code="ui.pyh.form.first.name" /></span>
			<span class="portlet-form-input-field"> <input
				name="searchFirstname" /> </span>
			<span class="portlet-form-field-label"><spring:message code="ui.pyh.form.last.name" /></span>
			<span class="portlet-form-input-field"> <input
				name="searchSurname" /> </span>
			<span class="portlet-form-field-label"><spring:message code="ui.pyh.table.ssn" />: </span>
			<span class="portlet-form-input-field"> <input
				name="searchSSN" /> </span>
			<input class="portlet-form-button" type="submit" value="<spring:message code="ui.pyh.seek"/>" />
		</form:form>
	</div>


	<c:choose>
		<c:when test="${not empty searchedUsers}">
			<table class="portlet-table-body" width="100%" border="0">

				<tr class="portlet-table-body th">
					<th width="38%"><spring:message code="ui.pyh.table.name" /></th>
					<th width="26%"><spring:message code="ui.pyh.table.ssn" /></th>
					<th width="10%"><spring:message code="ui.pyh.table.add" /></th>
					<th width="26%"><spring:message code="ui.pyh.table.role" /></th>
				</tr>

				<c:set var="userVar" value="1" />
				<c:forEach var="user" items="${searchedUsers}">

					<tr>
						<td>${user.firstname} ${user.surname}</td>
						<td>${user.ssn} <input id="user_ssn_${userVar}"
							name="userSSN_${userVar}" type="hidden" value="${user.ssn}" />
						</td>


						<td><input name="addUserCheckbox_${userVar}"
							value="${userVar}" type="checkbox" /></td>

						<td><select id="user_role_${userVar}" class="syntmaika">
								<option value="${ MEMBER }">${MEMBER.text}</option>
								<option value="${ DEPENDANT }">${DEPENDANT.text}</option>
								<option value="${ CHILD }">${ CHILD.text }</option>
								
								<c:if test="${ not parentsFull }">
								<option value="${ PARENT }">${ PARENT.text }</option>
								</c:if>
						</select></td>
					</tr>

					<c:set var="userVar" value="${userVar + 1}" />
				</c:forEach>
			</table>

			<p>&nbsp;</p>



			<form:form name="addUsersToFamily" method="post"
				action="${addUsersToFamily}" id="addUsersToFamilyForm">

				<%-- user information is added to this form dynamically with jQuery before submitting the form --%>

				<input class="portlet-form-button" type="button"
					value="<spring:message code="ui.pyh.save" />" class="tallenna" onclick="doSubmitForm()" />
			</form:form>
		</c:when>
		<c:otherwise>
			<c:if test="${search}">
				<p><spring:message code="ui.pyh.no.results" /></p>
			</c:if>
		</c:otherwise>
	</c:choose>

</div>

<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="http://gsgd.co.uk/sandbox/jquery/easing/jquery.easing.1.4.js"></script>
<script type="text/javascript" language="JavaScript">

	function addUserToForm(user) {

		$('#addUsersToFamilyForm').append($('#user_ssn_' + user));
		var userRole = $('#user_role_' + user + ' option:selected').val();
		$('#addUsersToFamilyForm')
				.append(
						'<input name="userRole_' + user + '" type="hidden" value="' + userRole + '"/>');

	}

	function doSubmitForm() {

		var $checkboxes = $('input[name^="addUserCheckbox_"]').filter(
				":checked");

		$checkboxes.each(function() {
			addUserToForm($(this).val());
		});

		$('#addUsersToFamilyForm').submit();

	}
</script>

