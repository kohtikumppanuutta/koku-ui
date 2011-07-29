<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<portlet:defineObjects />

<portlet:renderURL var="editFamilyInformation">
                            <portlet:param name="action" value="editFamilyInformation" />
</portlet:renderURL>

<portlet:renderURL var="home">
    <portlet:param name="action" value="" />
</portlet:renderURL>



<div class="portlet-section-body">

<div class="back">
  <span> <a
            href="${home}"> Vaihda käyttäjää</a> </span>
</div>

	<h1 class="portlet-section-header">
		<spring:message code="ui.pyh.own.info" /> <span class="takaisin"> <a
			href="${editFamilyInformation}">
				<spring:message code="ui.pyh.modify.info" /></a> </span>
	</h1>

	<c:if test="${not empty user}">
		<div class="name">${user.firstname} ${user.surname}</div>
		<div class="email"><spring:message code="ui.pyh.email" />  ${user.email}</div>
	</c:if>

	<c:if test="${not empty dependants}">


		<h3 class="portlet-section-subheader"><spring:message code="ui.pyh.dependants" /></h3>
		<c:forEach var="child" items="${dependants}">
			<div class="name">
				${child.firstname} ${child.surname} ${child.ssn} <br />
			</div>
		</c:forEach>

	</c:if>

	<c:if test="${not empty otherFamilyMembers}">
		<h3 class="portlet-section-subheader"><spring:message code="ui.pyh.other.family" />
		</h3>
		<c:forEach var="familyMember" items="${otherFamilyMembers}">
			<div class="name">
				${familyMember.firstname} ${familyMember.surname}
				${familyMember.ssn} (${familyMember.role.text}) <br />
			</div>
		</c:forEach>
	</c:if>
	
	
	 <c:if test="${not empty sentMessages}">
        <h3 class="portlet-section-subheader"><spring:message code="ui.pyh.sent.messages" />
        </h3>
        <c:forEach var="sentMessage" items="${sentMessages}">
            
            
            <div class="sentMessage">
                ${sentMessage.text} 
            </div>
            
        </c:forEach>
    </c:if>
    
	
    <c:if test="${not empty messages}">
        <h3 class="portlet-section-subheader"><spring:message code="ui.pyh.messages" />
        </h3>
        <c:forEach var="message" items="${messages}">
            <div class="message">
                <strong> ${message.text} </strong>
                
                <span class="right"> 

                    <span class="right"> 
                    
                    <portlet:actionURL var="reject">
                            <portlet:param name="action" value="rejectMessage" />
                            <portlet:param name="messageId" value="${message.id}" />
                    </portlet:actionURL>
                    <form:form name="reject" method="post" action="${reject}">
                        <input type="submit" class="portlet-form-button" value="<spring:message code="ui.pyh.deny" />"/>                        
                    </form:form>     
                    </span>  
                    
                                        
                    <span class="right"> 
                    <portlet:actionURL var="accept">
                            <portlet:param name="action" value="acceptMessage" />
                            <portlet:param name="messageId" value="${message.id}" />
                    </portlet:actionURL>
                    <form:form name="accept" method="post" action="${accept}">
                        <input type="submit" class="portlet-form-button" value="<spring:message code="ui.pyh.accept" />"/>                      
                    </form:form>
                    </span>
                          
                 </span> 
                 <div class="reset-floating"></div>
            </div>
            
            <div class="portlet-section-text">
                ${message.description} 
            </div>
            
        </c:forEach>
    </c:if>

<div class="reset-floating"></div>
</br>
</div>
