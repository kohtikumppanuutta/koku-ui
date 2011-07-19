<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<portlet:defineObjects/>

<%-- this boolean defines if the user is a guardian and whether guardian specific information should be displayed --%> 
<%-- TODO: get user's role and enable this flag if the user is a guardian --%>
<c:set var="guardian" value="${true}" scope="session"/>

<div>

<h1>Omat tiedot</h1> 

<p>

<c:if test="${not empty user}">
	${user.firstname} ${user.surname} <br/>
	sähköposti: ${user.email} <br/>
</c:if>
<br/>

<c:if test="${not empty dependants}">
	Huollettavat lapset:<br/>
	<c:forEach var="child" items="${dependants}">
		${child.firstname} ${child.surname} ${child.ssn} <br/>
	</c:forEach>
	<br/>
</c:if> 

<c:if test="${not empty otherFamilyMembers}">
	Perheyhteisön muut jäsenet <br/>
	<c:forEach var="familyMember" items="${otherFamilyMembers}">
		${familyMember.firstname} ${familyMember.surname} ${familyMember.ssn} (${familyMember.role}) <br/>
	</c:forEach>
	<br/>
</c:if>

</p>

<portlet:renderURL var="editFamilyInformation">
	<portlet:param name="action" value="editFamilyInformation"/>
</portlet:renderURL>

<div>
	<form:form name="editFamilyInformation" method="post" action="${editFamilyInformation}">
		<input type="submit" value="Muokkaa omia tietoja"/>
	</form:form>
</div>

</div>
