<%@page import="fi.koku.kks.ui.common.DataType"%>
<%@ include file="imports.jsp" %>


<c:set var="free_text" value="<%=DataType.FREE_TEXT%>" />
<c:set var="text" value="<%=DataType.TEXT%>" />
<c:set var="multi_select" value="<%=DataType.MULTI_SELECT%>" />
<c:set var="select" value="<%=DataType.SELECT%>" />


<fmt:setBundle basename="com.ixonos.eservices.koku.bundle.KokuBundle" />


<portlet:defineObjects />

<portlet:renderURL var="homeUrl">
	<portlet:param name="action" value="showCollection" />
	<portlet:param name="pic" value="${child.pic}" />
	<portlet:param name="collection" value="${collection.id}" />
</portlet:renderURL>
<portlet:actionURL var="addMultivalue">
	<portlet:param name="action" value="addMultivalue" />
	<portlet:param name="pic" value="${child.pic}" />
	<portlet:param name="collection" value="${collection.id}" />
	
	<c:if test="${ not empty entryvalue }">
	   <portlet:param name="entryId" value="${entryvalue.id}" />
	</c:if>
</portlet:actionURL>
<portlet:actionURL var="removeMultivalue">
    <portlet:param name="action" value="removeMultivalue" />
    <portlet:param name="pic" value="${child.pic}" />
    <portlet:param name="collection" value="${collection.id}" />
    
    <c:if test="${ not empty entryvalue }">
       <portlet:param name="entryId" value="${entryvalue.id}" />
    </c:if>
</portlet:actionURL>
<portlet:actionURL var="cancelMultivalue">
    <portlet:param name="action" value="cancelMultivalue" />
    <portlet:param name="pic" value="${child.pic}" />
    <portlet:param name="collection" value="${collection.id}" />
</portlet:actionURL>

<div class="koku-kks"> 
<div class="portlet-section-body">
<div>

	<div class="kks-home">
		<a href="${homeUrl}"><spring:message code="ui.kks.back" /> </a>
	</div>

</div>


<h1 class="portlet-section-header">${child.name} ${collection.name}</h1>


	<div class="kks-entry">

		<form:form name="addMultivalue" 
			method="post" action="${addMultivalue}">
			<input type="hidden" name="entryType" value="${type.id }" />		
			         
				<span class="portlet-form-field-label">
				
				<c:if test="${ not empty entryvalue }">
				    <spring:message code="ui.kks.modify.entry" /> ${type.name }
				</c:if>
				
				<c:if test="${ empty entryvalue }">
                    <spring:message code="ui.kks.add" /> ${type.creationDesc }
                </c:if>
				</span>
				<c:if test="${ not empty entryvalue }">
				    <span class="kks-right"> <a href="${removeMultivalue}"><spring:message code="ui.kks.remove" /> </a> </span>
				</c:if>
				
				
				
				<div class="portlet-form-field">

                <c:if test="${ not empty entryvalue }">
                <textarea id="value" class="portlet-form-input-field" title="${type.description }" name="value">${entryvalue.value}</textarea>
                </c:if>
                <c:if test="${ empty entryvalue }">
                <textarea id="value" class="portlet-form-input-field" title="${type.description }" name="value"></textarea>
                </c:if>

            </div>

			<span class="kks-right"> 			
			 <input type="submit" class="portlet-form-button"
				value="<spring:message code="ui.kks.contract.save"/>"> <span class="kks-right" style="padding-left: 5px">
					<a href="${homeUrl}"><spring:message code="ui.kks.cancel" /> </a>  </span> 
			</span>

		</form:form>
	</div>
	
	<div class="kks-reset-floating"></div>

</div>
</div>
