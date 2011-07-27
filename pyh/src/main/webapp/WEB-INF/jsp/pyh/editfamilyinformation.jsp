<%@page import="com.ixonos.koku.pyh.util.Role"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>



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

<div class="portlet-section-body">

	<h1 class="portlet-section-header">
		Perhetiedot <span class="takaisin"><a class="takaisin"
			href="${backURL}"><spring:message code="ui.hide" /></a>
		</span>
	</h1>

	<c:if test="${not empty user}">
		<div class="name">
			${user.firstname} ${user.surname} <br />
		</div>

		<div class="email">
			<form:form name="changeEmail" method="post" action="${changeEmail}"
				id="emailForm">

				<span class="portlet-form-field"><input name="email"
					type="text" value="${user.email}" />
				</span>

				<input class="portlet-form-button" type="submit" value="Tallenna"
					onclick="doSubmitForm()" />
			</form:form>
		</div>
		<br />
	</c:if>

	<c:if test="${not empty dependants}">
		<div class="dependants">
			<h3 class="portlet-section-subheader">Huollettavat lapset</h3>
			<c:forEach var="dependant" items="${dependants}">
				<div class="dependant">
					<span class="name">${dependant.firstname}
						${dependant.surname} ${dependant.ssn}</span> <span class="actions">


						<span class="link"> <c:choose>
								<c:when test="${dependant.memberOfUserFamily}">
					               (lisätty perheenjäseneksi)
					               <span class="link"> 
                                         <portlet:actionURL var="removeFamilyMember">
                                        <portlet:param name="action" value="removeFamilyMember" />
                                        <portlet:param name="familyMemberSSN" value="${dependant.ssn}" />
                                    </portlet:actionURL>
                            <a href="${removeFamilyMember}">Poista jäsenyys</a> </span> 
								</c:when>
								<c:otherwise>
									<portlet:actionURL var="addDependantAsFamilyMember">
										<portlet:param name="action"
											value="addDependantAsFamilyMember" />
										<portlet:param name="dependantSSN" value="${dependant.ssn}" />
									</portlet:actionURL>
									<a href="${addDependantAsFamilyMember}">Lisää
										perheenjäseneksi</a>
								</c:otherwise>
							</c:choose> </span> </span>
				</div>
			</c:forEach>
		</div>
	</c:if>


	<!-- 
+ LISÄÄ UUSI LAPSI <br/>

<portlet:actionURL var="addNewDependant">
	<portlet:param name="action" value="addNewDependant"/>
</portlet:actionURL>

<form:form name="addNewDependantForm" commandName="newDependant" method="post" action="${addNewDependant}">
	
	<span class="pvm">
	<span class="portlet-form-field-label">Etunimi:</span>  <span class="portlet-form-input-field"><form:input path="firstname"/> </span>
	<span class="portlet-form-field-label">Toinen nimi:</span>  <span class="portlet-form-input-field"><form:input path="middlename"/></span>
	<span class="portlet-form-field-label">Sukunimi:</span>  <span class="portlet-form-input-field"><form:input path="surname"/></span>
	</span>
	
	<span class="pvm">
	<span class="portlet-form-field-label">Syntymäaika:</span>
	
	<span class="portlet-form-input-field">
	<form:select path="birthday" class="syntmaika">
	<form:option value="01"/>
	<form:option value="02"/>
	<form:option value="03"/>
	<form:option value="04"/>
	<form:option value="05"/>
	<form:option value="06"/>
	<form:option value="07"/>
	<form:option value="08"/>
	<form:option value="09"/>
	<form:option value="10"/>
	<form:option value="11"/>
	<form:option value="12"/>
	<form:option value="13"/>
	<form:option value="14"/>
	<form:option value="15"/>
	<form:option value="16"/>
	<form:option value="17"/>
	<form:option value="18"/>
	<form:option value="19"/>
	<form:option value="20"/>
	<form:option value="21"/>
	<form:option value="22"/>
	<form:option value="23"/>
	<form:option value="24"/>
	<form:option value="25"/>
	<form:option value="26"/>
	<form:option value="27"/>
	<form:option value="28"/>
	<form:option value="29"/>
	<form:option value="30"/>
	<form:option value="31"/>
	</form:select>
	
	<form:select path="birthmonth" class="syntmaika">
	<form:option value="01"/>
	<form:option value="02"/>
	<form:option value="03"/>
	<form:option value="04"/>
	<form:option value="05"/>
	<form:option value="06"/>
	<form:option value="07"/>
	<form:option value="08"/>
	<form:option value="09"/>
	<form:option value="10"/>
	<form:option value="11"/>
	<form:option value="12"/>
	</form:select>
	
	<form:select path="birthyear" class="syntmaika">
	<form:option value="2000"/>
	<form:option value="2001"/>
	<form:option value="2002"/>
	<form:option value="2003"/>
	<form:option value="2004"/>
	<form:option value="2005"/>
	<form:option value="2006"/>
	<form:option value="2007"/>
	<form:option value="2008"/>
	<form:option value="2009"/>
	<form:option value="2010"/>
	<form:option value="2011"/>
	</form:select>
	</span>
	
	<span class="portlet-form-field-label">SOTU:</span> 
	<span class="portlet-form-input-field">
	<form:input path="ssn"/>
	</span>
	
	<input class="portlet-form-button" type="submit" value="Lisää &gt;"/>
	
	</span>
</form:form> -->
	<br />

	<c:if test="${not empty otherFamilyMembers}">
		<div class="members">
			<h3 class="portlet-section-subheader">Perheyhteisön muut jäsenet</h3>
			<c:forEach var="familyMember" items="${otherFamilyMembers}">

				<portlet:actionURL var="removeFamilyMember">
					<portlet:param name="action" value="removeFamilyMember" />
					<portlet:param name="familyMemberSSN" value="${familyMember.ssn}" />
				</portlet:actionURL>

				<div class="member">
					<span class="name">${familyMember.firstname}
						${familyMember.surname} ${familyMember.ssn}
						(${familyMember.role.text}) </span> <span class="linkki"><a
						href="${removeFamilyMember}">Poista perheenjäsen</a>
					</span>
				</div>

			</c:forEach>
		</div>
	</c:if>
	
	
	<div class="add">
                    
		<h3 class="portlet-section-subheader">LISÄÄ KÄYTTÄJIÄ PERHEYHTEISÖÖSI</h3>

		<portlet:actionURL var="searchUsers">
			<portlet:param name="action" value="searchUsers" />
		</portlet:actionURL>

		<form:form name="searchUsersForm" method="post"
			action="${searchUsers}">
			<span class="portlet-form-field-label"><spring:message code="ui.form.first.name" /></span>
			<span class="portlet-form-input-field"> <input
				name="searchFirstname" /> </span>
			<span class="portlet-form-field-label"><spring:message code="ui.form.last.name" /></span>
			<span class="portlet-form-input-field"> <input
				name="searchSurname" /> </span>
			<span class="portlet-form-field-label"><spring:message code="ui.table.ssn" />: </span>
			<span class="portlet-form-input-field"> <input
				name="searchSSN" /> </span>
			<input class="portlet-form-button" type="submit" value="HAE &gt;" />
		</form:form>
	</div>


	<c:choose>
		<c:when test="${not empty searchedUsers}">
			<table class="portlet-table-body" width="100%" border="0">

				<tr class="portlet-table-body th">
					<th width="38%"><spring:message code="ui.table.name" /></th>
					<th width="26%"><spring:message code="ui.table.ssn" /></th>
					<th width="10%"><spring:message code="ui.table.add" /></th>
					<th width="26%"><spring:message code="ui.table.role" /></th>
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
					value="Tallenna tiedot" class="tallenna" onclick="doSubmitForm()" />
			</form:form>
		</c:when>
		<c:otherwise>
			<c:if test="${search}">
				<p>Ei hakutuloksia.</p>
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

