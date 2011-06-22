<%@ include file="init.jsp"%>

<portlet:renderURL var="homeURL" windowState="<%= WindowState.NORMAL.toString() %>" >
	<portlet:param name="myaction" value="home" />
</portlet:renderURL>

<script type="text/javascript"> 

/**
 * Returns to the main portlet page
 */
function returnMainPage() {
	window.location="<%= homeURL %>";
}

</script>
<div id="task-manager-wrap">
	<div id="show-message" style="padding:12px">
	<c:out value="${request.subject}" /> <br />
	Valid: <c:out value="${request.creationDate}" /> to <c:out value="${request.endDate}" /> <br />
	<h3>Responded:</h3>
	<table>
	  <tr style="font-weight:bold;"><td width=200>NAME</td><td width=100>ANSWER</td><td>COMMENT</td></tr>
      <c:forEach var="response" items="${request.respondedList}">
        <tr>
          <td>${response.name}</td>
          <td>${response.answers[0].answer}</td>
          <td>${response.answers[0].comment}</td>
        </tr>
      </c:forEach>
    </table>
    <br />
    <h3>Missed:</h3>
	<table>
	  <tr style="font-weight:bold;"><td>NAME</td></tr>
      <c:forEach var="unresponse" items="${request.unrespondedList}">
        <tr>
          <td>${unresponse}</td>
        </tr>
      </c:forEach>
    </table>
    
	</div>
	<div id="task-manager-operation" class="task-manager-operation-part">
		<input type="button" value="<spring:message code="page.return"/>" onclick="returnMainPage()" />
	</div>
</div>

