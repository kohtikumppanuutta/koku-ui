<%--
	View for editing user's family information.
	
	author: Mikko Hurula
--%>

<%@ include file="imports.jsp" %>

<c:set var="CHILD" value="<%=fi.koku.pyh.model.CommunityRole.CHILD%>" />
<c:set var="PARENT" value="<%=fi.koku.pyh.model.CommunityRole.PARENT%>" />
<c:set var="MEMBER"	value="<%=fi.koku.pyh.model.CommunityRole.FAMILY_MEMBER%>" />
<c:set var="DEPENDANT" value="<%=fi.koku.pyh.model.CommunityRole.DEPENDANT%>" />
<c:set var="FATHER" value="<%=fi.koku.pyh.model.CommunityRole.FATHER%>" />
<c:set var="MOTHER" value="<%=fi.koku.pyh.model.CommunityRole.MOTHER%>" />

<portlet:defineObjects />

<portlet:actionURL var="addUsersToFamily">
	<portlet:param name="action" value="addUsersToFamily" />
</portlet:actionURL>

<portlet:renderURL var="backURL">
	<portlet:param name="action" value="guardianFamilyInformation" />
</portlet:renderURL>

<div class="koku-pyh">
<div class="portlet-section-body" id="portlet-section-body">

<div class="pyh-temp">
  <span class="pyh-right"> <a href="${backURL}"> <spring:message code="ui.pyh.back"/></a> </span>
</div>

<div class="pyh-reset-floating"></div>

	<h1 class="portlet-section-header">
		<spring:message code="ui.pyh.family.info" /> 
	</h1>

	<c:if test="${not empty user}">
		<div class="name">
			<c:out value="${user.firstname} ${user.surname}"/> <br />
		</div>
        <div class="email"><spring:message code="ui.pyh.econtactinfo" /> <c:out value="${user.econtactinfo}"/> </div>
		<br />
	</c:if>
	
	<c:if test="${not empty dependants or not empty otherFamilyMembers}">
	    <div class="pyh-family">
        <table class="portlet-table-body" width="100%">
            <tr>
                <th><spring:message code="ui.pyh.name" /></th>
                <th><spring:message code="ui.pyh.hetu" /></th>
                <th><spring:message code="ui.pyh.role" /></th>
                <th><spring:message code="ui.pyh.action" /></th>
            </tr>
            
            <c:forEach var="child" items="${dependants}">
            <tr>
                <td> <c:out value="${child.fullName}"/> </td>
                <td> <c:out value="${child.pic}"/> </td>
                <td><spring:message code="${DEPENDANT.bundleId}"/><c:if test="${child.memberOfUserFamily}">,&nbsp;<spring:message code="ui.pyh.added.into.family" /></c:if><c:if test="${!child.memberOfUserFamily}">,&nbsp;<spring:message code="ui.pyh.not.added.into.family" /></c:if></td>
                <td> 
                <span class="actions">
                        <span class="pyh-link"> <c:choose>
		                        
                                <c:when test="${child.memberOfUserFamily}">
                                   <span class="pyh-link"> 
                                        <portlet:actionURL var="removeFamilyMember">
                                        <portlet:param name="action" value="removeDependant" />
                                        <portlet:param name="familyMemberPic" value="${child.pic}" />
                                    </portlet:actionURL>
                                    <a href="${removeFamilyMember}" onclick="return doRemoveDependantConfirmation()"><spring:message code="ui.pyh.remove.family" /></a> </span> 
                                </c:when>
                                <c:otherwise>
                                    <portlet:actionURL var="addDependantAsFamilyMember">
                                        <portlet:param name="action"
                                            value="addDependantAsFamilyMember" />
                                        <portlet:param name="dependantPic" value="${child.pic}" />
                                    </portlet:actionURL>
                                    <a href="${addDependantAsFamilyMember}" onclick="return doAddDependantConfirmation()"><spring:message code="ui.pyh.add.into.family" /></a>
                                </c:otherwise>
                            </c:choose> </span> </span>
                </td>
            </tr>
            </c:forEach>
            
            <c:forEach var="familyMember" items="${otherFamilyMembers}">
                <tr>
                <td> <c:out value="${familyMember.fullName}"/> </td>
                <td> <c:out value="${familyMember.pic}"/> </td>
                <td><spring:message code="${familyMember.role.bundleId}"/></td>
                <td>
                	<span class="pyh-linkki">
                    <portlet:actionURL var="removeFamilyMember">
	                    <portlet:param name="action" value="removeFamilyMember" />
	                    <portlet:param name="familyMemberPic" value="${familyMember.pic}" />
                	</portlet:actionURL>
                	<a href="${removeFamilyMember}" onclick="return doRemoveFamilyMemberConfirmation()"><spring:message code="ui.pyh.remove.family" /></a>
                    </span>
               	</td>
            </tr>
            </c:forEach>
        </table>
    
    
    </div>
    </br>
    </c:if>

	
   <c:if test="${not empty messages}">
        <h3 class="portlet-section-subheader"><spring:message code="ui.pyh.sent.messages" />
        </h3>
        <c:forEach var="message" items="${messages}">            
            
            <div class="pyh-message">
                <c:out value="${message.text}"/>
            </div>
            
        </c:forEach>
        </br>
    </c:if>
    
	
	<div class="pyh-add-family">
                    
		<h3 class="portlet-section-subheader"><spring:message code="ui.pyh.add.family" /></h3>

		<portlet:actionURL var="searchUsers">
			<portlet:param name="action" value="searchUsers" />
		</portlet:actionURL>

		<form:form name="searchUsersForm" method="post"
			action="${searchUsers}">
			
			<span class="portlet-form-field-label"><spring:message code="ui.pyh.form.last.name" /></span>
			<span class="portlet-form-input-field"> <input name="searchSurname" /> </span>
			
			<span class="portlet-form-field-label"><spring:message code="ui.pyh.table.pic" />: </span>
			<span class="portlet-form-input-field"> <input name="searchPic" /> </span>
			<input class="portlet-form-button" type="submit" value="<spring:message code="ui.pyh.seek"/>" />
		</form:form>
	</div>


	<c:choose>
		<c:when test="${not empty searchedUsers}">
			<form:form name="addUsersToFamily" method="post" action="${addUsersToFamily}" id="addUsersToFamilyForm">
			
				<table class="portlet-table-body" width="100%" border="0">
	
					<tr class="portlet-table-body th">
						<th width="38%"><spring:message code="ui.pyh.table.name" /></th>
						<th width="26%"><spring:message code="ui.pyh.table.pic" /></th>
						<%-- hide text for checkbox column --%>
						<th width="10%"> <%-- <spring:message code="ui.pyh.table.add" /> --%> </th>
						<th width="26%"><spring:message code="ui.pyh.table.role" /></th>
					</tr>
					
					<%-- <c:set var="userVar" value="1" /> --%>
					
					
					<input name="familyCommunityId" type="hidden" value="${familyCommunityId}"/>
					
					<c:forEach var="user" items="${searchedUsers}">
						
						<tr>
							<td>
								<c:out value="${user.firstname} ${user.surname}"/>
							</td>
							<td> 
								<c:out value="${user.pic}"/>
								<input name="userPic" type="hidden" value="${user.pic}" />
								<%-- <input id="user_pic_${userVar}" name="userPic_${userVar}" type="hidden" value="${user.pic}" /> --%>
							</td>
							
							<td>
							<%-- hack: check box is hidden because at the moment we have only one search result by PIC --%>
							<%--
							<div style="visibility: hidden;">
							<input name="addUserCheckbox_${userVar}" value="${userVar}" type="checkbox" />
							</div>
							--%>
							</td>
							
							<td>
								<%-- <select id="user_role_${userVar}" class="syntmaika"> --%>
								<select name="userRole" class="syntmaika">
									<option value="${MEMBER}"><spring:message code="${MEMBER.bundleId}"/></option>
									<option value="${CHILD}"><spring:message code="${CHILD.bundleId}"/></option>
									
									<c:if test="${not parentsFull}">
										<option value="${FATHER}"><spring:message code="${FATHER.bundleId}"/></option>
										<option value="${MOTHER}"><spring:message code="${MOTHER.bundleId}"/></option>
										<option value="${PARENT}"><spring:message code="${PARENT.bundleId}"/></option>
									</c:if>
								</select>
							</td>
						</tr>
						
						<%-- <c:set var="userVar" value="${userVar + 1}" /> --%>
					</c:forEach>
					
				</table>
				
				<p>&nbsp;</p>
				
				<input class="portlet-form-button" type="submit" value="<spring:message code="ui.pyh.save" />" class="tallenna"/>
			</form:form>
			
			<%--
			<form:form name="addUsersToFamily" method="post" action="${addUsersToFamily}" id="addUsersToFamilyForm">
				<input name="familyCommunityId" type="hidden" value="${familyCommunityId}"/>
				
				<%-- user information is added to this form dynamically with jQuery before submitting the form --%>
				<%--
				<input class="portlet-form-button" type="button"
					value="<spring:message code="ui.pyh.save" />" class="tallenna" onclick="doSubmitForm()" />
			</form:form>
			--%>
			
		</c:when>
		<c:otherwise>
			<c:if test="${search}">
				<p><spring:message code="ui.pyh.no.results" /></p>
			</c:if>
		</c:otherwise>
	</c:choose>
	
	<c:if test="${childsGuardianshipInformationNotFound}">
		<p><spring:message code="ui.pyh.cannot.send.request"/></p>
	</c:if>

</div>
</div>

<%-- script source URLs will be localized later --%>
<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="http://gsgd.co.uk/sandbox/jquery/easing/jquery.easing.1.4.js"></script>
<script type="text/javascript" language="JavaScript">

	<%--
	function addUserToForm(user) {
		$('#addUsersToFamilyForm').append($('#user_pic_' + user));
		var userRole = $('#user_role_' + user + ' option:selected').val();
		$('#addUsersToFamilyForm').append('<input name="userRole_' + user + '" type="hidden" value="' + userRole + '"/>');
	}
	--%>
	<%--
	function doSubmitForm() {
		<%--
		var $checkboxes = $('input[name^="addUserCheckbox_"]').filter(
				":checked");
		--%>
		<%-- select all checkboxes. *at the moment* we have only one search result and it must be selected always. --%>
		<%--
		var $checkboxes = $('input[name^="addUserCheckbox_"]');

		$checkboxes.each(function() {
			addUserToForm($(this).val());
		});

		$('#addUsersToFamilyForm').submit();
	}
	--%>
	
	function doAddDependantConfirmation() {
		return confirm("<spring:message code="ui.pyh.add.dependant.family.member.question"/>");
	}
	
	function doRemoveDependantConfirmation() {
		return confirm("<spring:message code="ui.pyh.remove.dependant.family.member.question"/>");
	}
	
	function doRemoveFamilyMemberConfirmation() {
		return confirm("<spring:message code="ui.pyh.remove.family.member.question"/>");
	}
	
</script>

