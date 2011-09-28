<%@page import="fi.koku.kks.ui.common.DataType"%>
<%@ include file="imports.jsp" %>

<c:set var="free_text" value="<%=DataType.FREE_TEXT%>" />
<c:set var="text" value="<%=DataType.TEXT%>" />
<c:set var="multi_select" value="<%=DataType.MULTI_SELECT%>" />
<c:set var="select" value="<%=DataType.SELECT%>" />


<fmt:setBundle basename="com.ixonos.eservices.koku.bundle.KokuBundle" />


<portlet:defineObjects />

<portlet:renderURL var="homeUrl">
	<portlet:param name="action" value="showChild" />
	<portlet:param name="pic" value="${child.pic}" />
</portlet:renderURL>
<portlet:actionURL var="saveActionUrl">
	<portlet:param name="action" value="saveCollection" />
	<portlet:param name="pic" value="${child.pic}" />
	<portlet:param name="collection" value="${collection.id}" />
</portlet:actionURL>
<portlet:actionURL var="addMultivalue">
	<portlet:param name="action" value="addMultivalue" />
	<portlet:param name="pic" value="${child.pic}" />
	<portlet:param name="collection" value="${collection.id}" />
</portlet:actionURL>
<portlet:actionURL var="createVersionURL">
    <portlet:param name="action" value="createNewVersion" />
    <portlet:param name="pic" value="${child.pic}" />
</portlet:actionURL>

<div class="koku-kks"> 
<div class="portlet-section-body">

    <div class="kks-left">
    <div class="kks-collection">
                <c:if test="${ sessionScope.ammattilainen && !collection.versioned }">
                    <a class="create"> <spring:message code="ui.kks.new.version" /><span
                        class="kks-close"><spring:message code="ui.kks.hide" /> </span> </a>
                    <div class="kks-fields" style="display: none;">
          
                        <form:form name="newVersionForm"  method="post" action="${createVersionURL}">
                                <input type="hidden" id="id" name="id" value="${ collection.id }"/>
                                <div class="portlet-form-field-label"><spring:message code="ui.kks.contract.name" /></div>
                                <div class="portlet-form-field" style="width: 98%"><input  class="portlet-form-input-field" type="text" id="name" name="name" style="width: 100%" value="${ collection.name }"/>
                                 
                                </div>
                                <div class="portlet-form-field-label">
                                   <span class="portlet-form-field"><input class="portlet-form-input-field" type="checkbox" id="clean" name="clean" value="true"/>
                                    <label class="portlet-form-field-label" for="clean"> <spring:message code="ui.kks.clear.fields" /> </label>
                                   </span>
                                </div>
                                                                 
                                <span class="kks-right">
                                <input type="submit" class="portlet-form-button"
                                value="<spring:message code="ui.kks.contract.create"/>">
                                </span>

                        </form:form>

                    </div>
                </c:if>
            </div>
    </div>
    
<div class="kks-home">
	<div class="kks-right">
		<a href="${homeUrl}"><spring:message code="ui.kks.back" /> </a>
	</div>
</div>

<div class="kks-reset-floating"></div>

<div class="kks-left">
<h1 class="portlet-section-header">
    <c:out value="${child.name}"/> <c:out value="${collection.name}"/> 
    <c:if test="${not collection.state.active}">
        (<spring:message code="ui.kks.locked" />)
    </c:if>

</h1>
</div>
<div class="kks-right">
         <c:if      
            test="${ not empty collection.prevVersion }">            
            <a
                href="
                        <portlet:renderURL>
                            <portlet:param name="action" value="showCollection" />
                            <portlet:param name="pic" value="${child.pic}" />
                            <portlet:param name="collection" value="${collection.prevVersion}" />
                        </portlet:renderURL>">
                <strong><spring:message code="ui.kks.prev.version" />
            </strong> </a>
        </c:if> <c:if test="${not empty collection.nextVersion }">
            <c:if test="${ not empty collection.prevVersion }">
            </br>
            </c:if>
            <a
                href="
                        <portlet:renderURL>
                            <portlet:param name="action" value="showCollection" />
                            <portlet:param name="pic" value="${child.pic}" />
                            <portlet:param name="collection" value="${collection.nextVersion}" />
                        </portlet:renderURL>">
                <strong><spring:message code="ui.kks.next.version" />
            </strong> </a>
        </c:if> 
</div>
<div class="kks-reset-floating"></div>
    <div class="kks-content">

        <c:if test="${not empty collection.collectionClass }">

            <form:form class="form-wrapper" name="entryForm" commandName="entry" method="post"
                action="${saveActionUrl}">

                <c:forEach var="group" items="${collection.collectionClass.kksGroups.kksGroup }">
					<c:if test="${not empty authorized[group.register] || master }">	
                    <c:if test="${not empty group.name}">
                        <h2 class="portlet-section-subheader"><c:out value="${group.name}"/></h2>
                    </c:if>
                    <c:if test="${not empty group.description}">
                        <div class="portlet-section-text">
                             <span class="kks-read-only-description"><c:out value="${group.description}"/></span>
                        </div>
                    </c:if>
                    
                    <c:forEach var="childGroup" items='${group.subGroups.kksGroup}'>
						<c:if test="${not empty authorized[childGroup.register] || master }"> 
                        <c:if test="${not empty childGroup.name}">
                            <h3 class="portlet-section-subheader"><c:out value="${childGroup.name}"/></h3>
                        </c:if>
                        
                        <c:if test="${not empty childGroup.description}">
	                        <div class="portlet-section-text">
	                             <span class="kks-read-only-description"><c:out value="${childGroup.description}"/></span>
	                        </div>
                    	</c:if>
                            <c:forEach var="type" items='${ childGroup.kksEntryClasses.kksEntryClass  }'>
                                <div class="kks-entry">
                                    <span class="portlet-form-field-label">${type.name} <c:if
                                            test="${type.multiValue && collection.state.active }">
                                            <a
                                                href="javascript:void(0)" onclick="doSubmitNewMulti('${type.id}');">	
                                                (<spring:message code="ui.kks.add.multivalue" />) </a>
                                        </c:if>
                                    </span>
   
                                    <c:if test="${ collection.state.active }">
                                        <c:choose>
                                            <c:when
                                                test="${ type.dataType eq multi_select.name }">
                                                <div class="portlet-form-field"> <c:forEach
                                                        items="${type.valueSpaces.valueSpace }" var="value">
                                                        <form:checkbox  class="portlet-form-input-field" title="${type.description }"
                                                            path="entries['${type.id}'].values"
                                                            value="${ value }" label="${value}" />
                                                    </c:forEach> </div>
                                            </c:when>
                                            <c:when test="${ type.dataType eq select.name }">
                                                <div class="portlet-form-field"> <c:forEach
                                                        items="${type.valueSpaces.valueSpace}" var="value">
                                                        <form:radiobutton class="portlet-form-input-field" title="${type.description }"
                                                            path="entries['${type.id}'].values"
                                                            value="${ value }" label="${value}" />
                                                    </c:forEach> </div>
                                            </c:when>
                                            <c:when test="${ type.dataType eq text.name }">
                                                <div class="portlet-form-field" >
                                                    <form:input title="${type.description }" class="portlet-form-input-field"
                                                        path="entries['${type.id}'].value" />
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="portlet-form-field">
                                                    <c:if test="${not type.multiValue}">
                                                        <form:textarea class="portlet-form-input-field" title="${type.description}" 
                                                            path="entries['${type.id}'].value" />
                                                    </c:if>
                                                    <c:if test="${type.multiValue}">

                                                        <div class="kks-free-text">

                                                            <div class="kks-multi-value-entries">

                                                                <c:if
                                                                    test="${empty collection.multiValueEntries[type.id]}">
                                                                    <div class="portlet-section-text"><spring:message
                                                                            code="ui.kks.no.entries" />
                                                                    </div>
                                                                </c:if>

                                                                <c:forEach var="multivalue"
                                                                    items='${ collection.multiValueEntries[type.id] }'>
                                                                    
                                                                    <div class="kks-comment">
	                                                                    <span class="kks-entry-value">${multivalue.value} <span class="kks-right">
	                                                                    <a
	                                                                            href="javascript:void(0)" onclick="doSubmitForm('${type.id}', '${multivalue.id}', '${multivalue.valueId}' );">
	                                                                                <spring:message code="ui.kks.modify" /> </a>
	                                                                    </span> 
	                                                                    </span>
	                                                                    <div class="portlet-section-text">
	                                                                     <span class="kks-commenter">${multivalue.modifierFullName} <fmt:formatDate type="both" pattern="dd.MM.yyyy hh:mm" value="${multivalue.creationTime}"/>
	                                                                        </span> 
	                                                                    </div>
                                                                    </div>
                                                                </c:forEach>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>

                                    <c:if test="${ not collection.state.active }">
                                       
                                        <div class="portlet-section-text">
                                        
                                        <c:choose>
                                            <c:when
                                                test="${ empty collection.multiValueEntries[type.id] && empty collection.entries[type.id].value }">
                                                <span class="kks-read-only-text">-</span>
                                            </c:when>
                                            <c:otherwise>
                                             <c:if
                                                    test="${ type.multiValue }">
                                                    <c:forEach var="multivalue"
                                                        items='${ collection.multiValueEntries[type.id] }'>

                                                        <span class="kks-read-only-text"> <c:out value="${multivalue.value}"/> <c:out value="(${multivalue.modifierFullName}"/> <fmt:formatDate type="both" pattern="dd.MM.yyyy hh:mm" value="${multivalue.creationTime}"/>)</span>
                                                        
                                                    </c:forEach>
                                                </c:if> <c:if test="${ not type.multiValue }">
                                                    <span class="kks-read-only-text"><c:out value="${collection.entries[type.id].value}"></c:out> </span>
                                                </c:if> 
                                                </c:otherwise>
                                        </c:choose>
                                           </div>
                                        
                                    </c:if>
                                </div>
                  
                            </c:forEach>
                  </c:if>
                    </c:forEach>
                    
                    </c:if>
                </c:forEach>

                <div class="kks-right">
                <c:if test="${ collection.state.active }">
                    <input type="submit" class="portlet-form-button"
                        value="<spring:message code="ui.kks.save"/>" >
                </c:if>
                </div>
                <div class="kks-reset-floating" />
            </form:form>

        </c:if>




    </div>
</div>
<br />

</div>


<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="http://gsgd.co.uk/sandbox/jquery/easing/jquery.easing.1.4.js"></script>
<script type="text/javascript">
	$(document).ready(function() {

		$("a.create").click(function() {
			$(this).toggleClass("active").next().slideToggle("fast");
		});

	});

       function addMultivalueIdToForm( multiId ) {
           $('#entry')
           .append(
                   '<input name="multiValueId" type="hidden" value="' + multiId + '"/>');
       }
       
	   function addValueIdToForm(valueId) {
	        $('#entry')
	                .append(
	                        '<input name="valueId" type="hidden" value="' + valueId + '"/>');

	    }
	   
       
	   function addTypeToForm(type) {
	        $('#entry')
	                .append(
	                        '<input name="type" type="hidden" value="' + type + '"/>');

	    }

	    function doSubmitNewMulti( type ) {
	        addTypeToForm(type);
	        $('#entry').submit();

	    }

	       function doSubmitForm( type, multiId, valueId ) {
	    	   	addValueIdToForm(valueId);
	            addTypeToForm(type);
	            addMultivalueIdToForm(multiId);
	            $('#entry').submit();

	        }
</script>