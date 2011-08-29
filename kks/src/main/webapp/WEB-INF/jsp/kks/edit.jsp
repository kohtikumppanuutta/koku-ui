<%@ include file="imports.jsp" %>


<portlet:renderURL var="resetURL">
    <portlet:param name="action" value="resetModel" />
    <portlet:param name="message" value="---" />
</portlet:renderURL>

<div class="koku-kks"> 
<div  class="portlet-section-body">

        <div id="kks-entry.tyypit" class="portlet-section-text">
            <form:form name="reset" method="post" action="${resetURL}">
                <input class="portlet-form-button" type="submit" value="Resetoi malli" />
            </form:form>
        </div>
        <div class="kks-read-only-text">
            <c:out value="${message}"/>
        </div>


    <div class="kks-spacer"></div>
</div>
</div>

