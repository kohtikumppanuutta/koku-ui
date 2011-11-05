<%--
 Copyright 2011 Ixonos Plc, Finland. All rights reserved.

 You should have received a copy of the license text along with this program.
 If not, please contact the copyright holder (http://www.ixonos.com/).
--%>
<%@ include file="imports.jsp" %>


<portlet:renderURL var="resetURL">
    <portlet:param name="action" value="reset" />
    <portlet:param name="note" value="---" />
</portlet:renderURL>

<div class="koku-pyh">
	<div  class="portlet-section-body">
	
	        <div id="pyh-reset-buttons" class="portlet-section-text">
	            <form:form name="reset" method="post" action="${resetURL}">
	                <input class="portlet-form-button" type="submit" value="Resetoi malli" />
	            </form:form>
	        </div>
	        <div class="pyh-text">
	           <c:out value="${note}"/>
	        </div>
	
	
	    <div class="reset"></div>
	</div>
</div>
