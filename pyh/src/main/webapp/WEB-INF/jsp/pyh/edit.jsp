<%@ include file="imports.jsp" %>


<portlet:renderURL var="Resetoi">
    <portlet:param name="action" value="reset" />
    <portlet:param name="note" value="---" />
</portlet:renderURL>

<div class="koku-pyh">
	<div  class="portlet-section-body">
	
	        <div id="kirjaus.tyypit" class="portlet-section-text">
	            <form:form name="reset" method="post" action="${Resetoi}">
	                <input class="portlet-form-button" type="submit" value="Resetoi malli" />
	            </form:form>
	        </div>
	        <div class="teksti">
	            ${viesti}
	        </div>
	
	
	    <div class="reset"></div>
	</div>
</div>