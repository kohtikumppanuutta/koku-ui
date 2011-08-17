
<%@ include file="/jsp/init.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ page import="fi.arcusys.koku.palvelut.controller.SearchController"%>
<%@ page import="fi.arcusys.koku.palvelut.controller.EditController"%>
<%@page import="java.util.List"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="javax.portlet.WindowState"%>
<%@page import="fi.arcusys.koku.palvelukanava.palvelut.model.VeeraForm"%>

<%

List<VeeraForm> forms = (List<VeeraForm>)request.getAttribute("forms");
int formsSize =  forms.size();
BigDecimal bd = new BigDecimal(formsSize);
int pages = bd.divide(new BigDecimal(5), BigDecimal.ROUND_CEILING).intValue();
pageContext.setAttribute("veeraForms", forms);
String backUrl = (String)request.getAttribute("backUrl");

int c = 0;
Object limits = request.getAttribute(SearchController.SEARCH_RESULT_MORE_THAN_LIMIT);
String limitValue = (limits!=null && limits instanceof String) ?  (String)limits : null;
pageContext.setAttribute("limitvalue",limitValue);


%>


<script type="text/javascript" language="javascript">

		/* Params for LocalizeJQueryPager */
		var params = new Object();
		/*params.pageheaderHTML = '<li id="pages-header-text" style="border: none" class="text-description">Sivu:</li>';*/
		params.xpathselector = 'div#<portlet:namespace/>_pager';
		params.first = 'Ensimmäinen';
		params.prev  = '<< Edellinen';
		params.next  = 'Seuraava >>';
		params.last  = 'Viimeinen';
		
		params.firstIsVisible = 'false';
		params.lastIsVisible  = 'false';
		params.numbersIsVisible = 'false';
		
		/*Create portlet spesific params object */
		var <portlet:namespace/>_params = params;
		params = null;
		

        jQuery(document).ready(function() {
            jQuery("div#<portlet:namespace/>_pager").pager({ pagenumber: 1, pagecount: <%=pages%>, buttonClickCallback: <portlet:namespace/>_PageClick });
            jQuery("div.<portlet:namespace/> div#search-list table tr").hide();
			<portlet:namespace/>_PageClick(1);/*Show 1 page items*/
			
        });

        <portlet:namespace/>_PageClick = function(pageclickednumber) {
            
            jQuery("div.<portlet:namespace/> div#search-list table tr").hide();
			jQuery("div#<portlet:namespace/>_pager").pager({ pagenumber: pageclickednumber, pagecount: <%=pages%>, buttonClickCallback: <portlet:namespace/>_PageClick });
            var itemsPerPage = 5;
			var multirows = 2;
			
		    var minIndex = (pageclickednumber == 1) ? 0 : ((pageclickednumber-1)*itemsPerPage)*2;
			var maxIndex = (pageclickednumber == 1) ? (itemsPerPage-1)*2 : ((pageclickednumber*itemsPerPage)-1)*2;
					
			var rowSelector = 'tr:lt('+(maxIndex+1)+'):gt('+(minIndex-1)+')';
			jQuery('div.<portlet:namespace/> div#search-list table tr').filter(rowSelector).show();
			
			LocalizeJQueryPager(<portlet:namespace/>_params);
        }   
           
              
    </script>

	<div class="<portlet:namespace/>">

	<portlet:renderURL var="viewURL" windowState="<%= WindowState.NORMAL.toString() %>">
		<portlet:param name="action" value="view"/>
	</portlet:renderURL>
	<a id="search-result-backurl" href="<c:out value="${viewURL}" />">&laquo;&nbsp;Takaisin</a>	


	<div class="search_bar">
	
	<form action="
	<portlet:renderURL>
		<portlet:param name="<%= EditController.CURRENT_CATEGORY %>" value="<%= request.getParameter(EditController.CURRENT_CATEGORY) %>" />
		<portlet:param name="action" value="search" />		
	</portlet:renderURL> " method="post" >Hae asiointipalvelua:
		
		<input type="text" class="text-field" value="<c:out value="${searchTextParam}" />" name="<%= SearchController.SEARCH_TEXT_PARAM %>" /> <input type="submit" value="Hae" /> 
		
	</form>

	</div>	
 
 <div id="search-result-title">Hakutulokset</div>
 
<div id="search-list">

<!-- Forms listing -->
	<% if (forms.size() > 0) { %>
	
	<c:if test="${limitvalue ne null}">
						
							<div class="search-list-limited">Hakutulokset on rajoitettu <c:out value="${limitvalue}" escapeXml="true"/> lomakkeeseen. Tarkenna hakua.</div>
						
	</c:if>
	
	
	<table class="taglib-search-iterator">
		<tbody>
			<%--
			<tr class="portlet-section-header">
				<th>Palvelun nimi</th>
				
			</tr>
			 --%>
			<c:forEach var="veeraForm" items="${veeraForms}" varStatus="rowItem">
				
				<tr>
					<td valign="middle" align="left" class="col-1">
						<!-- URL to formview.jsp -->
						<portlet:renderURL var="formViewURL"
							windowState="<%=WindowState.MAXIMIZED.toString() %>">
							<portlet:param name="action" value="formview" />
							<portlet:param name="type" value="${veeraForm.type}" />
							<portlet:param name="entryId" value="${veeraForm.entryId}" />
							<portlet:param name="identity" value="${veeraForm.identity}" />
							<portlet:param name="identity2" value="${veeraForm.identity2}" />
							<portlet:param name="hasHelpContent" value="${veeraForm.helpContent != null}" />
							<portlet:param name="<%= EditController.CURRENT_CATEGORY %>" value="<%= request.getParameter(EditController.CURRENT_CATEGORY) %>" />
						</portlet:renderURL>
						<a href="${formViewURL}">
							<b>${veeraForm.identity_64}</b>
						</a>
					</td>
					
				</tr>
				<tr>
					<c:if test="${veeraForm.description ne null}">
						<td class="veeraFormDescription">
							<c:out value="${veeraForm.description}" escapeXml="true"/>
						</td>
					</c:if>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	
<% }else{ %>

	<div class="search-list-no-results">Hakemaasi palvelua ei löydy tai sitä ei ole palvelukanavassa.</div>

<% } %>	


	
</div>

<%--Show pager if there are more than 5 items --%>
<c:if test="${veeraForms ne null}">
	<c:if test="${fn:length(veeraForms)>5}">
    	<div id="<portlet:namespace/>_pager" class="pager"></div>
    </c:if>
</c:if>
</div>
