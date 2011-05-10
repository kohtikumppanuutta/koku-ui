<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<portlet:renderURL var="viewChilds">
	<portlet:param name="myaction" value="showChilds" />
</portlet:renderURL>

<portlet:renderURL var="showPro">
	<portlet:param name="myaction" value="showSpecialist" />
	<portlet:param name="childs" value="" />
</portlet:renderURL>


<div>
	<div id="selection">
		<table width="100%">
			<tr>
				<td width="100%"><form:form name="guardianForm" method="post"
						action="${viewChilds}">
						<input type="submit" value="Huoltaja" />
					</form:form></td>
			</tr>
			<tr>
				<td width="100%"><form:form name="proForm" method="post"
						action="${showPro}">
						<input type="submit" value="Ammattilainen" />
					</form:form></td>
			</tr>
		</table>

	</div>
</div>