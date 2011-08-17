<%@ include file="imports.jsp" %>


<portlet:renderURL var="Resetoi">
    <portlet:param name="action" value="resetModel" />
    <portlet:param name="message" value="---" />
</portlet:renderURL>

<div class="koku-kks"> 
<div  class="portlet-section-body">

        <div id="kks-entry.tyypit" class="portlet-section-text">
            <form:form name="reset" method="post" action="${Resetoi}">
                <input class="portlet-form-button" type="submit" value="Resetoi malli" />
            </form:form>
        </div>
        <div class="teksti">
            ${message}
        </div>


    <div class="reset"></div>
</div>
</div>

