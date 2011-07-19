<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<portlet:defineObjects/>

<h1>Perhetiedot</h1>

<portlet:renderURL var="backURL">
	<portlet:param name="action" value="guardianFamilyInformation"/>
</portlet:renderURL>
<a class="takaisin" href="${backURL}">&lt;&lt; Takaisin</a>

<p>
<c:if test="${not empty user}">
	${user.firstname} ${user.surname} <br/>
	Sähköposti: ${user.email} <br/>
	<br/>
</c:if>

<c:if test="${not empty dependants}">
	Huollettavat lapset: <br/>
	<c:forEach var="dependant" items="${dependants}">
		
		<%-- this feature is not used, do not remove the commented code before we are sure we don't need this --%>
		<%--
		<portlet:actionURL var="removeDependant">
			<portlet:param name="action" value="removeDependant"/>
			<portlet:param name="dependantSSN" value="${dependant.ssn}"/>
		</portlet:actionURL>
		${dependant.firstname} ${dependant.surname} ${dependant.ssn} <a href="${removeDependant}">poista huoltajuus</a> <br/>
		--%>
		
		${dependant.firstname} ${dependant.surname} ${dependant.ssn}
		<c:choose>
			<c:when test="${dependant.memberOfUserFamily}">
				lisätty perheenjäseneksi
			</c:when>
			<c:otherwise>
				<portlet:actionURL var="addDependantAsFamilyMember">
					<portlet:param name="action" value="addDependantAsFamilyMember"/>
					<portlet:param name="dependantSSN" value="${dependant.ssn}"/>
				</portlet:actionURL>
				<a href="${addDependantAsFamilyMember}">lisää perheenjäseneksi</a> <br/>
			</c:otherwise>
		</c:choose>
		<br/>
		
	</c:forEach>
</c:if>
</p>

<p>
+ LISÄÄ UUSI LAPSI <br/>

<portlet:actionURL var="addNewChild">
	<portlet:param name="action" value="addNewChild"/>
</portlet:actionURL>

<form:form name="addNewChildForm" commandName="newChild" method="post" action="${addNewChild}">
	
	<span class="pvm">
	Etunimi: <form:input path="firstname"/> 
	Toinen nimi: <form:input path="middlename"/>
	Sukunimi: <form:input path="surname"/>
	</span>
	
	<span class="pvm">
	Syntymäaika:
	
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
	
	HETU: <form:input path="ssn"/>
	
	<input type="submit" value="Lisää &gt;"/>
	
	</span>
</form:form>
<br/>

<c:if test="${not empty otherFamilyMembers}">
	Perheyhteisön muut jäsenet <br/>
	<c:forEach var="familyMember" items="${otherFamilyMembers}">
		
		<portlet:actionURL var="removeFamilyMember">
			<portlet:param name="action" value="removeFamilyMember"/>
			<portlet:param name="familyMemberSSN" value="${familyMember.ssn}"/>
		</portlet:actionURL>
		
		${familyMember.firstname} ${familyMember.surname} ${familyMember.ssn} (${familyMember.role}) 
		<a href="${removeFamilyMember}">poista jäsenyys</a> <br/>
		
	</c:forEach>
<br/>
</c:if>
</p>

<p>
<span class="pvm">
LISÄÄ KÄYTTÄJIÄ PERHEYHTEISÖÖSI <br/>
<br/>

<portlet:actionURL var="searchUsers">
	<portlet:param name="action" value="searchUsers"/>
</portlet:actionURL>

<form:form name="searchUsersForm" method="post" action="${searchUsers}">
	Etunimi: <input name="searchFirstname"/>
	Sukunimi: <input name="searchSurname"/>
	SOTU: <input name="searchSSN"/>
	<input type="submit" value="HAE &gt;"/>
</form:form>
</span>
</p>

<c:choose>
	<c:when test="${not empty searchedUsers}">
		<table width="100%" border="0">
			
			<tr>
			<td width="38%">NIMI</td>
			<td width="26%">HETU</td>
			<td width="10%">LISÄÄ</td>
			<td width="26%"> VALITSE LISÄTTÄVÄN ROOLI</td>
			</tr>
			
			<c:set var="userVar" value="1"/>
			<c:forEach var="user" items="${searchedUsers}">
				
				<tr>
				<td>${user.firstname} ${user.surname}</td>
				<td>${user.ssn}</td>
				<input id="user_ssn_${userVar}" name="userSSN_${userVar}" type="hidden" value="${user.ssn}" />
				
				<td>
					<input name="addUserCheckbox_${userVar}" value="${userVar}" type="checkbox"/>
				</td>
				
				<td>
				<select id="user_role_${userVar}" class="syntmaika">
				<option value="">VALITSE ROOLI</option>
				<option value="aiti">Äiti</option>
				<option value="isa">Isä</option>
				<option value="lapsi">Lapsi</option>
				</select>
				</td>
				</tr>
				
				<c:set var="userVar" value="${userVar + 1}"/>
			</c:forEach>
		</table>
		
		<p>&nbsp;</p>
		
		<portlet:actionURL var="addUsersToFamily">
			<portlet:param name="action" value="addUsersToFamily"/>
		</portlet:actionURL>
		
		<form:form name="addUsersToFamily" method="post" action="${addUsersToFamily}" id="addUsersToFamilyForm">
			
			<%-- user information is added to this form dynamically with jQuery before submitting the form --%>
			
			<input type="button" value="Tallenna tiedot" class="tallenna" onclick="doSubmitForm()"/>
		</form:form>
	</c:when>
	<c:otherwise>
		<c:if test="${search}">
			<p>Ei hakutuloksia.</p>
		</c:if>
	</c:otherwise>
</c:choose>


<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="http://gsgd.co.uk/sandbox/jquery/easing/jquery.easing.1.4.js"></script>
<script type="text/javascript" language="JavaScript">
	
	function addUserToForm(user) {
		
		$('#addUsersToFamilyForm').append( $('#user_ssn_' + user) );
		var userRole = $('#user_role_' + user + ' option:selected').val();
		$('#addUsersToFamilyForm').append('<input name="userRole_' + user + '" type="hidden" value="' + userRole + '"/>');
		
	}
	
	function doSubmitForm() {
		
		var $checkboxes = $('input[name^="addUserCheckbox_"]').filter(":checked");
		
		$checkboxes.each(
			function() {
				addUserToForm( $(this).val() );
			}
		);
		
		$('#addUsersToFamilyForm').submit();
		
	}
	
</script>




