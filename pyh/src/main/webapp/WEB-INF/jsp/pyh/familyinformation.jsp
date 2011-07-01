<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<portlet:defineObjects/>

<c:set var="guardian" value="${true}" scope="session"/>

<div>

<h1>Omat tiedot</h1> 

<p>
<c:if test="${not empty user}">
	${user.firstname} ${user.surname} <br/>
	sähköposti: ${user.email} <br/>
</c:if>
<br/>

Huollettavat lapset:<br/>

<c:if test="${not empty guardedChildren}">
	<c:forEach var="child" items="${guardedChildren}">
		${child.firstname} ${child.surname} ${child.ssn} <br/>
	</c:forEach>
</c:if> 

<br/>

Perheyhteisön muut jäsenet<br/>

ETUNIMI SUKUNIMI 311205-123C (rooli)<br/>

<br/>

</p>

<portlet:renderURL var="editFamilyInformation">
	<portlet:param name="action" value="editFamilyInformation"/>
</portlet:renderURL>

<div>
	<form:form name="editFamilyInformation" method="post" action="${editFamilyInformation}">
		<input type="submit" value="Muokkaa omia tietoja"/>
	</form:form>
</div>

<%--
<p>
*<span class="wait">ETUNIMI SUKUNIMI 121279-123A</span>
Uusi perheyhteisötieto.
<input type="submit" value="Hyväksy">
<input type="submit" value="Hylkää">
</p>

<p>
*<span class="wait">Käyttäjä ETUNIMI2 SUKUNIMI2 on lisännyt sinut perheyhteisön muuksi jäseneksi.<br/>
Kaikkien opsapuolten on hyväksyttävä uuden jäsenen liittäminen perheyhteisöön.</span>
</p>
--%>

</div>
