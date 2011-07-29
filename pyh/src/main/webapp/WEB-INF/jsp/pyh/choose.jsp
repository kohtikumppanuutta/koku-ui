<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>



<portlet:renderURL var="pekka">
	<portlet:param name="action" value="userSelection"/>
	<portlet:param name="ssn" value="010101-1010"/>
</portlet:renderURL>

<portlet:renderURL var="irina">
    <portlet:param name="action" value="userSelection"/>
    <portlet:param name="ssn" value="020202-2020"/>
</portlet:renderURL>

<div>
	<div>
		<form:form name="guardian" method="post" action="${pekka}">
			<input type="submit" value="Pekka Peltola"/>
		</form:form>
	</div>
	
	<div>
		<form:form name="non-guardian" method="post" action="${irina}">
			<input type="submit" value="Irina Peltola"/>
		</form:form>
	</div>
	
</div>
