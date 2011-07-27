<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<portlet:defineObjects />

<portlet:renderURL var="editFamilyInformation">
                            <portlet:param name="action" value="editFamilyInformation" />
</portlet:renderURL>

<div class="portlet-section-body">

	<h1 class="portlet-section-header">
		Omat tiedot <span class="takaisin"> <a
			href="${editFamilyInformation}">
				Muokkaa omia tietoja</a> </span>
	</h1>

	<c:if test="${not empty user}">
		<div class="name">${user.firstname} ${user.surname}</div>
		<div class="email">sähköposti: ${user.email}</div>
	</c:if>

	<c:if test="${not empty dependants}">


		<h3 class="portlet-section-subheader">Huollettavat lapset:</h3>
		<c:forEach var="child" items="${dependants}">
			<div class="name">
				${child.firstname} ${child.surname} ${child.ssn} <br />
			</div>
		</c:forEach>

	</c:if>

	<c:if test="${not empty otherFamilyMembers}">
		<h3 class="portlet-section-subheader">Perheyhteisön muut jäsenet
		</h3>
		<c:forEach var="familyMember" items="${otherFamilyMembers}">
			<div class="name">
				${familyMember.firstname} ${familyMember.surname}
				${familyMember.ssn} (${familyMember.role.text}) <br />
			</div>
		</c:forEach>

	</c:if>

</div>
