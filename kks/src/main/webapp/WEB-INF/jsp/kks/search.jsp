<%--
 Copyright 2012 Ixonos Plc, Finland. All rights reserved.
  
 This file is part of Kohti kumppanuutta.
 
 This file is licensed under GNU LGPL version 3.
 Please see the 'license.txt' file in the root directory of the package you received.
 If you did not receive a license, please contact the copyright holder
 (http://www.ixonos.com/).
--%>
<%@ include file="imports.jsp" %>


<portlet:renderURL var="homeUrl">
	<portlet:param name="action" value="choose" />
</portlet:renderURL>
<portlet:actionURL var="searchActionUrl">
	<portlet:param name="action" value="searchChild" />
</portlet:actionURL>

<c:set var="municipal_employee" value="${true}" scope="session" />

<div class="koku-kks"> 
<div  class="portlet-section-body">

	<div class="kks-home">
		<a href="${homeUrl}"><spring:message code="ui.kks.back" /></a>
	</div>

    <div class="kks-reset-floating"></div>
    
	<c:if test="${not empty error}"><div class="error"><spring:message code="${error}"></spring:message> </div></c:if>

	<div class="kks-content">
		<h1 class="portlet-section-header">
			<spring:message code="ui.kks.title" />
		</h1>
        <div class="search">
		<form:form name="searchChildForm" commandName="child" method="post"
			action="${searchActionUrl}">
			
			<h3 class="portlet-section-subheader"><spring:message code="ui.kks.search.child.info" /></h3>

            <div class="kks-left">
            <span class="portlet-form-field-label"> 
                <spring:message code="ui.kks.form.pic" />
            </span>
            
			<span class="portlet-form-field"> 
			 <form:input class="defaultText"  path="pic" /> 
			 <input type="submit" class="portlet-form-button" value="<spring:message code="ui.kks.search.info"/>"> 
			</span>
					
			</div>
			

			<div class="kks-reset-floating"></div>
		</form:form>
		</div>
		<br></br>
		<div class="kks-collection">
			<c:if test="${not empty childs}">
				<c:forEach var="child" items="${childs}">
					<span class="kks-link"> <a
						href="
                        <portlet:actionURL>
                            <portlet:param name="action" value="toChildInfo" />
                            <portlet:param name="pic" value="${child.pic}" />
                        </portlet:actionURL>">
							<c:out value="${child.lastName }"/>, <c:out value="${child.firstName}"/></a> <span>${child.pic}</span>
					</span>
				</c:forEach>
			</c:if>
			<c:if test="${empty childs && not empty search}">
				<span class="kks-search-result"><strong><spring:message code="ui.kks.no.search.results" /></strong>
				</span>
			</c:if>
		</div>
	</div>

	<div class="kks-spacer">
		<br></br>
	</div>
</div>
</div>

