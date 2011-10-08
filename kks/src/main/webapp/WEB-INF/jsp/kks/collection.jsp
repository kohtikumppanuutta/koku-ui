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
    		<div class="error-bindings">
				<spring:hasBindErrors name="version">
			     <spring:bind path="version.*">
			       <c:forEach var="error" items="${status.errorMessages}">
			         <div class="error"><c:out value="${error}"/></div>
			       </c:forEach>
			     </spring:bind>
				</spring:hasBindErrors>
			</div>
                <c:if test="${ sessionScope.ammattilainen && !collection.versioned }">
                    <a class="create"> <spring:message code="ui.kks.new.version" /><span
                        class="kks-close"><spring:message code="ui.kks.hide" /> </span> </a>
                    <div class="kks-fields" style="display: none;">
          
                        <form:form name="newVersionForm" commandName="version" method="post" action="${createVersionURL}">
                                <input type="hidden" id="id" name="id" value="${ collection.id }"/>
                                <div class="portlet-form-field-label"><spring:message code="ui.kks.contract.name" /></div>
                                <div class="portlet-form-field">
                                	<form:input path="name" maxlength="250" size="70" class="portlet-form-input-field"  />                                 
                                </div>
                                <div class="portlet-form-field-label">
                                   <span class="portlet-form-field"><form:checkbox path="clear" class="portlet-form-input-field" />
                                    <label class="portlet-form-field-label" for="clear"> <spring:message code="ui.kks.clear.fields" /> </label>
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
    
        <c:if test="${ empty_collection }"><spring:message code="ui.kks.no.authorization" /></c:if>

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
                                    
                                        <c:if test="${type.multiValue}">                                        
                                            <div class="kks-free-text">

                                                <div class="kks-multi-value-entries">

                                                  <c:if
                                                      test="${empty collection.entries[type.id]}">
                                                      <div class="portlet-section-text"><spring:message
                                                              code="ui.kks.no.entries" />
                                                      </div>
                                                  </c:if>

                                                  <c:forEach var="multivalue" items='${ collection.entries[type.id].entryValues }'>
                                                      
                                                      <div class="kks-comment">
                                                       <span class="kks-entry-value"><p><c:out value="${multivalue.value}"/> </p><span class="kks-right">
                                                       <a
                                                               href="javascript:void(0)" onclick="doSubmitForm('${type.id}', '${collection.entries[type.id].id}', '${multivalue.id}' );">
                                                                   <spring:message code="ui.kks.modify" /> </a>
                                                       </span> 
                                                       </span>
                                                       <div class="portlet-section-text">
                                                        <span class="kks-commenter">${multivalue.modifierFullName} <fmt:formatDate type="both" pattern="dd.MM.yyyy HH:mm:ss" value="${multivalue.modified}"/>
                                                           </span> 
                                                       </div>
                                                      </div>
                                                  </c:forEach>
                                                </div>
                                            </div>                                        
                                        </c:if>
                                        <c:if test="${not type.multiValue}">
	                                        <c:choose>
	                                            <c:when
	                                                test="${ type.dataType eq multi_select.name }">
	                                                <div class="portlet-form-field"> <c:forEach
	                                                        items="${type.valueSpaces.valueSpace }" var="value">
	                                                        <form:checkbox  class="portlet-form-input-field" title="${type.description }"
	                                                            path="entries['${type.id}'].firstValue.values"
	                                                            value="${ value }" label="${value}" />
	                                                    </c:forEach> </div>
	                                            </c:when>
	                                            <c:when test="${ type.dataType eq select.name }">
	                                                <div class="portlet-form-field"> 
	                                                <c:forEach items="${type.valueSpaces.valueSpace}" var="value">
	                                                        <form:radiobutton class="portlet-form-input-field" title="${type.description }"
	                                                            path="entries['${type.id}'].firstValue.values"
	                                                            value="${ value }" label="${value}" />
	                                                    </c:forEach> </div>
	                                            </c:when>
	                                            <c:when test="${ type.dataType eq text.name }">
	                                                <div class="portlet-form-field" >
	                                                    <form:input maxlength="2000" title="${type.description }" class="portlet-form-input-field"
	                                                        path="entries['${type.id}'].firstValue.value" />
	                                                </div>
	                                            </c:when>
	                                            <c:otherwise>
	                                                <div class="portlet-form-field">                                                    
	                                                        <form:textarea maxlength="2000" class="portlet-form-input-field" title="${type.description}" 
	                                                            path="entries['${type.id}'].firstValue.value" /> 
	                                                </div>
	                                            </c:otherwise>
	                                        </c:choose>
                                        </c:if>
                                    </c:if>

                                    <c:if test="${ not collection.state.active }">
                                       
                                        <div class="portlet-section-text">
                                        
                                        <c:choose>
                                            <c:when
                                                test="${ empty collection.entries[type.id] || empty collection.entries[type.id].firstValue.value }">
                                                <span class="kks-read-only-text">-</span>
                                            </c:when>
                                            <c:otherwise>
	                                             <c:if test="${ type.multiValue }">
	                                                    <c:forEach var="multivalue" items='${ collection.entries[type.id].entryValues }'>
	                                                        <span class="kks-read-only-text"><c:out value="${multivalue.value}"/><c:out value="(${multivalue.modifierFullName}"/> <fmt:formatDate type="both" pattern="dd.MM.yyyy hh:mm" value="${multivalue.modified}"/>)</span>                                                       
	                                                    </c:forEach>
	                                                </c:if> <c:if test="${ not type.multiValue }">
	                                                    <span class="kks-read-only-text"><c:out value="${collection.entries[type.id].firstValue.value}"></c:out> </span>
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

window.onload = function() { 
	  var txts = document.getElementsByTagName('TEXTAREA') 

	  for(var i = 0, l = txts.length; i < l; i++) {
	    if(/^[0-9]+$/.test(txts[i].getAttribute("maxlength"))) { 
	      var func = function() { 
	        var len = parseInt(this.getAttribute("maxlength"), 10); 

	        if(this.value.length > len) { 
	          this.value = this.value.substr(0, len); 
	          return false; 
	        } 
	      }

	      txts[i].onkeyup = func;
	      txts[i].onblur = func;
	    } 
	  } 
	}

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