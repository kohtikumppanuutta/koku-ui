<%@page import="com.ixonos.koku.pyh.util.Role"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<portlet:defineObjects />

<c:set var="dp" value="<%=com.ixonos.koku.pyh.util.Role.DEPENDANT%>" />

<portlet:renderURL var="editFamilyInformation">
                            <portlet:param name="action" value="editFamilyInformation" />
</portlet:renderURL>

<portlet:renderURL var="home">
    <portlet:param name="action" value="" />
</portlet:renderURL>


<div class="koku-pyh">
<div class="portlet-section-body">

<div class="pyh-temp">
  <span> <a
            href="${home}"> Vaihda käyttäjää</a> </span>
   <span class="pyh-right"> <a
            href="${editFamilyInformation}">
                <spring:message code="ui.pyh.modify.info" /></a> </span>
          
</div>

<div class="pyh-reset-floating"></div>

	<h1 class="portlet-section-header">
		<spring:message code="ui.pyh.own.info" />
	</h1>

	<c:if test="${not empty user}">
		<div class="name">${user.firstname} ${user.surname} </div>
		<div class="email"><spring:message code="ui.pyh.email" />  ${user.email}</div>
	</c:if>

</br>

<c:if test="${not empty dependants or not empty otherFamilyMembers}">
    <div class="pyh-family">
        <table class="portlet-table-body" width="100%">
            <tr>
                <th><spring:message code="ui.pyh.name" /></th>
                <th><spring:message code="ui.pyh.hetu" /></th>
                <th><spring:message code="ui.pyh.role" /></th>
            </tr>
            
            <c:forEach var="child" items="${dependants}">
            <tr>
                <td> ${child.fullName} </td>
                <td> ${child.ssn} </td>
                <td> ${dp.text} 
                    <c:if test="${child.memberOfUserFamily}">
                     (<spring:message code="ui.pyh.added.into.family" />)
                    </c:if>   
                    <c:if test="${!child.memberOfUserFamily}">
                     (<spring:message code="ui.pyh.not.added.into.family" />)
                    </c:if>               
                </td>
            </tr>
            </c:forEach>
            
            <c:forEach var="familyMember" items="${otherFamilyMembers}">
             <tr>
                <td>${familyMember.fullName} </td>
                <td>${familyMember.ssn} </td>
                <td>${familyMember.role.text}</td>
            </tr>
            </c:forEach>
        </table>    
    
    </div>
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
                
                <span class="pyh-right"> 

                    <span class="pyh-right"> 
                    
                    <portlet:actionURL var="reject">
                            <portlet:param name="action" value="rejectMessage" />
                            <portlet:param name="messageId" value="${message.id}" />
                    </portlet:actionURL>
                    <form:form name="reject" method="post" action="${reject}">
                        <input type="submit" class="portlet-form-button" value="<spring:message code="ui.pyh.deny" />"/>                        
                    </form:form>     
                    </span>  
                                        
                    <span class="pyh-right"> 
                    <portlet:actionURL var="accept">
                            <portlet:param name="action" value="acceptMessage" />
                            <portlet:param name="readerId" value="${user.ssn}" />
                            <portlet:param name="messageId" value="${message.id}" />
                    </portlet:actionURL>
                    <form:form name="accept" method="post" action="${accept}">
                        <input type="submit" class="portlet-form-button" value="<spring:message code="ui.pyh.accept" />"/>                      
                    </form:form>
                    </span>
                          
                 </span> 
                 <div class="pyh-reset-floating"></div>
            
            
            <div class="portlet-section-text">
                ${message.description}               
            
            <span class="pyh-mail">
            <form:form name="accept" method="post" action="${accept}">
                
                <a href="mailto:yllapito@kohtikumppanuutta.fi?subject=Asiaton perheyhteyspyyntö (Viesti ID:${ message.id })">Ilmoita asiaton perheyhteyspyyntö</a> 
                </form:form>
                </span>
                </div>
            </div>
            
        </c:forEach>
    </c:if>

<div class="pyh-reset-floating"></div>
</br>
</div>
</div>


