<%@ include file="imports.jsp" %>


<portlet:renderURL var="homeUrl">
	<portlet:param name="action" value="choose" />
</portlet:renderURL>
<portlet:actionURL var="searchActionUrl">
	<portlet:param name="action" value="searchChild" />
</portlet:actionURL>

<c:set var="ammattilainen" value="${true}" scope="session" />

<div class="koku-kks"> 
<div  class="portlet-section-body">
	<div class="kks-home">
		<a href="${homeUrl}">Takaisin</a>
	</div>

    <div class="kks-reset-floating"></div>
    
	<div class="kks-content">
		<h1 class="portlet-section-header">
			<spring:message code="ui.kks.otsikko" />
		</h1>
        <div class="search">
		<form:form name="searchChildForm" commandName="child" method="post"
			action="${searchActionUrl}">
			
			<h3 class="portlet-section-subheader"><spring:message code="ui.hae.lapsen.tiedot" /></h3>

            <div class="kks-left">
            <span class="portlet-form-field-label"> 
                <spring:message code="ui.form.hetu" />
            </span>
            
			<span class="portlet-form-field"> 
			 <form:input class="defaultText"  path="pic" /> 
			</span>
			</div>
			<span class="kks-left"> <input type="submit" class="portlet-form-button" value="<spring:message code="ui.hae.tiedot"/>" ></span>

			<div class="kks-reset-floating" />
		</form:form>
		</div>
</br>
		<div class="collection">
			<c:if test="${not empty childs}">
				<c:forEach var="child" items="${childs}">
					<span class="kks-link"> <a
						href="
                        <portlet:actionURL>
                            <portlet:param name="action" value="toChildInfo" />
                            <portlet:param name="pic" value="${child.pic}" />
                        </portlet:actionURL>">
							<strong>${child.lastName }, ${child.firstName } </strong></a> <span>${child.pic}</span>
					</span>
				</c:forEach>
			</c:if>
			<c:if test="${empty childs && not empty search}">
				<span class="searchResult"><strong><spring:message code="ui.ei.hakutuloksia" /></strong>
				</span>
			</c:if>
		</div>
	</div>


	<div class="kks-spacer">
		</br>
	</div>
</div>
</div>

