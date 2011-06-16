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

<div id="main" class="wide">
		<table width="100%">
			<tr>
				<td width="100%"><form:form name="guardianForm" method="post"
						action="${viewChilds}">
						<input type="submit" value="Lapsen Huoltaja" />
					</form:form></td>
			</tr>
			<tr>
				<td width="50%"><form:form name="proForm" method="post"
						action="${showPro}">
						<input type="submit" value="Kunnan työntekijä" />
					</form:form></td>
					                <td style="halign:right" width="50%"><form:form name="luokitteluForm" method="post"
                        action="${luokittelu}">
                        <input type="submit" value="Näytä luokittelu" />
                    </form:form></td>
			</tr>
			
			     <tr>

            </tr>
		</table>

	</div>
