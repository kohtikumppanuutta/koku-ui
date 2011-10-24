<%@ include file="init.jsp" %>

<portlet:renderURL var="naviURL" >
	<portlet:param name="myaction" value="showNavi" />
</portlet:renderURL>

<%	
	String defaultPath = "";	
	String subPage = "koku";
	int currentPathPosition = naviURL.indexOf(subPage);
	if (currentPathPosition > -1) {
		defaultPath = naviURL.substring(0, currentPathPosition+subPage.length());
// 		System.out.println("------------------------------------------------------------------------------------------------------");
// 		System.out.println("defaultPath: '"+defaultPath+"'");
// 		System.out.println("defaultPath: '"+defaultPath+"/Message'");
// 		System.out.println("------------------------------------------------------------------------------------------------------");	
	}

	
%>
	<div class="kokuRedirect">Ohjataan sivulle Kohtikumppanuutta.. </div>
	<script type="text/javascript">
		if ('<%=defaultPath%>' != '') {
			window.location = '<%=defaultPath%>/Message';		
		}
	</script>
