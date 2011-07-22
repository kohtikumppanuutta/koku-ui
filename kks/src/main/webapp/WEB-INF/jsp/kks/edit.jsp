<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<portlet:renderURL var="Resetoi">
    <portlet:param name="toiminto" value="resetoiMalli" />
    <portlet:param name="viesti" value="---" />
</portlet:renderURL>

<div>

    <div style="vertical-align: middle;">
        <div>
            <form:form name="reset" method="post" action="${Resetoi}">
                <input type="submit" value="Resetoi malli" />
            </form:form>
        </div>
        <div>
            ${viesti}
        </div>


    <div style="clear:both"></div>
</div>
</div>
