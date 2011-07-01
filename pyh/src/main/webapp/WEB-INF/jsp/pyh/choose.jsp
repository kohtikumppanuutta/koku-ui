<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<portlet:renderURL var="guardianFamilyInformation">
	<portlet:param name="action" value="guardianFamilyInformation"/>
</portlet:renderURL>

<portlet:renderURL var="nonguardianFamilyInformation">
	<portlet:param name="action" value="nonguardianFamilyInformation"/>
</portlet:renderURL>

<div>
	
	<div>
		<form:form name="guardian" method="post" action="${guardianFamilyInformation}">
			<input type="submit" value="Huoltaja"/>
		</form:form>
	</div>
	
	<div>
		<form:form name="non-guardian" method="post" action="${nonguardianFamilyInformation}">
			<input type="submit" value="Perheyhteisön jäsen"/>
		</form:form>
	</div>
	
</div>
