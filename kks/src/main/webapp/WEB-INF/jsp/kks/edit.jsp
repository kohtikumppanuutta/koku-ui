<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<portlet:renderURL var="viewChilds">
    <portlet:param name="toiminto" value="naytaLapset" />
</portlet:renderURL>

<portlet:renderURL var="showPro">
    <portlet:param name="toiminto" value="naytaTyontekija" />
    <portlet:param name="lapset" value="" />
</portlet:renderURL>


<portlet:renderURL var="showPro">
    <portlet:param name="toiminto" value="naytaTyontekija" />
    <portlet:param name="lapset" value="" />
</portlet:renderURL>


<portlet:renderURL var="luokittelu">
    <portlet:param name="toiminto" value="naytaLuokittelu" />
</portlet:renderURL>

<div>

    <div style="float: left;">
        <div>
            <form:form name="guardianForm" method="post" action="${viewChilds}">
                <input type="submit" value="<spring:message code="ui.nayta.huoltaja"/>" />
            </form:form>
        </div>

        <div>
            <form:form name="proForm" method="post" action="${showPro}">
                <input type="submit" value="<spring:message code="ui.nayta.tyontekija"/>" />
            </form:form>
        </div>

    </div>

    <div style="float: right;">
        <form:form name="luokitteluForm" method="post" action="${luokittelu}">
        
              
            <input type="submit" value="<spring:message code="ui.nayta.luokittelu"/>" />
        </form:form>
    </div>

    <div style="clear:both"></div>

</div>
