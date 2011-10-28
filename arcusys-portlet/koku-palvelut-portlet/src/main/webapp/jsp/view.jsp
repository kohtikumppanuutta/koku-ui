
<%@ include file="/jsp/init.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page import="fi.arcusys.koku.palvelut.model.client.VeeraCategory"%>
<%@page import="fi.arcusys.koku.palvelut.model.client.VeeraForm"%>
<%@page import="fi.arcusys.koku.palvelut.model.client.FormHolder"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@ page import="fi.arcusys.koku.palvelut.controller.ViewController"%>
<%@ page import="fi.arcusys.koku.palvelut.controller.SearchController"%>
<%@ page import="fi.arcusys.koku.palvelut.controller.EditController"%>
<%@ page import="fi.arcusys.koku.palvelut.util.CategoryUtil"%>
<%@ page import="fi.arcusys.koku.palvelut.util.VeeraCategoryComparator"%>
<%@ page import="fi.arcusys.koku.palvelut.util.VeeraFormComparator"%>
<%@ page import="fi.arcusys.koku.palvelut.util.MigrationUtil" %>


<% 

// Get Liferay user information
String userId = MigrationUtil.getUser(request);
//User user = UserLocalServiceUtil.getUser(userId);
long companyId = MigrationUtil.getCompanyId(request);

// Liferay ThemeDisplay object for getting user group
//ThemeDisplay td = (ThemeDisplay) request.getAttribute("THEME_DISPLAY");
// test3.user group name
String adminGroup = "10204";
Map model = (Map)request.getAttribute("model");

// Get root folder for current companyId

Integer rootFolderId = (Integer)model.get("rootFolderId");

Integer currentFolder = (Integer)model.get("currentFolder");

VeeraCategory currentCategory = (VeeraCategory)model.get("currentCategory");

// Child categories & forms of currently selected category

List<VeeraCategory> categories = (List<VeeraCategory>)model.get("categories");
List<VeeraForm> forms = (List<VeeraForm>)model.get("forms");

Collections.sort(categories, new VeeraCategoryComparator());
Collections.sort(forms, new VeeraFormComparator());

boolean isAdmin = MigrationUtil.hasUserRole(userId, companyId, "VeeraAdmin");

// Divide categories to 2 columns
List<ArrayList<VeeraCategory>> categoryRows = new ArrayList<ArrayList<VeeraCategory>>();

for (int c = 0; c < categories.size(); c++) {
	if (c % 2 == 0) {
		ArrayList<VeeraCategory> veeraRow = new ArrayList<VeeraCategory>();
		veeraRow.add(categories.get(c));
		if ( c + 1 < categories.size() ) {
			veeraRow.add(categories.get(c + 1));
		} else {
			veeraRow.add(null);
		}
		categoryRows.add(veeraRow);
	}
}

// Count number of forms for categories
List<Object[]> formCount = (List<Object[]>)model.get("formCount");

pageContext.setAttribute("veeraCategoryRows", categoryRows);
pageContext.setAttribute("veeraForms", forms);
//pageContext.setAttribute("td", td);
pageContext.setAttribute("admin", adminGroup);
pageContext.setAttribute("isAdmin", isAdmin);
// Counter for table-style
int c = 0;

%>

<%!
/**
 ** Display number of forms under category.
 **/
private static String getCategoryFormCountText(int forms) {
  if (forms > 0) {
    return "Palveluja: " + Long.toString(forms);
  } else {
    return "Tämä palveluryhmä on tyhjä.";
  }
}
%>


<%@page import="javax.portlet.PortletPreferences"%><div class="portlet-linkit-container">

	<!-- UI-messages -->
	<jsp:include page="/jsp/messages.jsp" />
	
	<!-- Portlet Header -->
	
	<div class="portlet-header">
	
		<jsp:include page="/jsp/breadCrumb.jsp" />	
		
		<%// Admin actions 

		if (isAdmin == true) {
	
		%>
			<!-- Add URL Form Button -->
			<div class="btn_lisaa_linkki">
				<portlet:renderURL var="addURLFormURL">
					<portlet:param name="action" value="edit" />
					<portlet:param name="<%= EditController.EDIT_TYPE %>" value="<%= EditController.SHOW_ADD_URL_FORM %>" />
					<portlet:param name="<%= EditController.CURRENT_CATEGORY %>" value="<%= currentCategory.getEntryId().toString() %>" />			 
				</portlet:renderURL>
				<button onclick="self.location = '${addURLFormURL}';" class="basic_w_icon" type="button">
					Lisää lomakelinkki
				</button>
			</div>
			<!-- Add Intalio Form Button -->
			<div class="btn_lisaa_linkki">
				<portlet:renderURL var="addIntalioFormURL">
					<portlet:param name="action" value="edit" />
					<portlet:param name="<%= EditController.EDIT_TYPE %>" value="<%= EditController.SHOW_ADD_INTALIO_FORM %>" />
					<portlet:param name="<%= EditController.CURRENT_CATEGORY %>" value="<%= currentCategory.getEntryId().toString() %>" /> 
				</portlet:renderURL>
				<button onclick="self.location = '${addIntalioFormURL}';" class="basic_w_icon" type="button">
					Lisää lomake
				</button>
			</div>
			<!-- Add Category Button -->
			<div class="btn_luo_kansio">
				<portlet:renderURL var="addCategoryURL">
					<portlet:param name="action" value="edit" />
					<portlet:param name="<%= EditController.EDIT_TYPE %>" value="<%= EditController.SHOW_ADD_CATEGORY %>" />
					<portlet:param name="<%= EditController.CURRENT_CATEGORY %>" value="<%= currentCategory.getEntryId().toString() %>" /> 
				</portlet:renderURL>
				<button onclick="self.location = '${addCategoryURL}';" class="basic_w_icon" type="button">
					Luo kansio
				</button>
			</div>
		<% } %>

		<div class="search_bar">
		
		<form action="
	<portlet:renderURL>
		<portlet:param name="<%= EditController.CURRENT_CATEGORY %>" value="<%= currentCategory.getEntryId().toString() %>" /> 
		<portlet:param name="action" value="search" />	
		<portlet:param name="backurl" value="<%= MigrationUtil.getCurrentURL(request) %>" />
	</portlet:renderURL> " method="post">Hae asiointipalvelua:
		
		<input type="text" class="text-field" name="<%= SearchController.SEARCH_TEXT_PARAM %>" /> <input type="submit" value="Hae" /> 
		
	</form>
	
	</div>	
		<div class="clearer"></div>
	</div>
	
	
	
	<!-- Portlet Content -->
	
	<div>
	
	<!-- Category listing -->

	<table class="veeraCategoryTable">
		<c:forEach var="veeraRow" items="${veeraCategoryRows}">
			<tr>
				<td class="categoryName">
					<portlet:renderURL var="folderViewURL">
						<portlet:param name="<%= ViewController.VIEW_CURRENT_FOLDER %>" value="${veeraRow[0].entryId}" />
					</portlet:renderURL>
					<div class="title">
						<a href="${folderViewURL}">
							<c:out value="${veeraRow[0].name}" />
						</a>
					</div>
					<%
					// Get category id
					Integer categoryLeftId = ((ArrayList<VeeraCategory>)pageContext.getAttribute("veeraRow")).get(0).getEntryId(); 
					int formsCount = ((ArrayList<VeeraCategory>)pageContext.getAttribute("veeraRow")).get(0).getFormCount();
					%>
					<span class="details"><%= getCategoryFormCountText(formsCount) %></span>
				</td>
								
				<c:choose>
					<c:when test="${veeraRow[0].description ne null && fn:length(veeraRow[0].description) > 0}">
						<% 
						String fullDescription = ((ArrayList<VeeraCategory>)pageContext.getAttribute("veeraRow")).get(0).getDescription(); 
						%>
						<td class="categoryInfo" onclick="
							var html = '<c:out value="<%= fullDescription %>"/>';
							var popup = new Liferay.Popup(
							{
								header: 'Kategorian kuvaus',
								position: [150,150],
								modal: false,
								width: 500,
								height: 300,
								xy: ['center', 100],
								message: html
							}
							);" title="Näytä kuvaus">
						</td>
					</c:when>
					<c:otherwise>
						<td></td>
					</c:otherwise>	
				</c:choose>			
				<c:if test="${isAdmin == true}">
					<td>
						<div class="btn_container_admin_actions">
						<% 
							String categoryId = Integer.toString(((ArrayList<VeeraCategory>)pageContext.getAttribute("veeraRow")).get(0).getEntryId());
							String companyIdString = Long.toString(((ArrayList<VeeraCategory>)pageContext.getAttribute("veeraRow")).get(0).getCompanyId());
						%>
						<portlet:renderURL var="editCategoryURL">
							<portlet:param name="action" value="edit" />
							<portlet:param name="<%= EditController.EDIT_TYPE %>" value="<%= EditController.SHOW_EDIT_CATEGORY %>" /> 
							<portlet:param name="editCategoryId" value="<%= categoryId %>" />
							<portlet:param name="editCompanyId" value="<%= companyIdString %>" />
							<portlet:param name="<%= EditController.CURRENT_CATEGORY %>" value="<%= currentCategory.getEntryId().toString() %>" />
						</portlet:renderURL>
						<a href="${editCategoryURL}">Muokkaa</a>
						<portlet:actionURL var="removeCategoryURL">
							<portlet:param name="action" value="edit" />
							<portlet:param name="<%= EditController.EDIT_TYPE %>" value="<%= EditController.ADMIN_REMOVE_CATEGORY_ACTION %>" />
							<portlet:param name="removeCategoryId" value="<%= categoryId %>" />
							<portlet:param name="removeCompanyId" value="<%= companyIdString %>" />
							<portlet:param name="<%= EditController.CURRENT_CATEGORY %>" value="<%= currentCategory.getEntryId().toString() %>" />
						</portlet:actionURL>
						<a href="${removeCategoryURL}">Poista</a>
						</div>
					</td>
				</c:if>
	
				<c:choose>
				<c:when test="${veeraRow[1] != null}">
					<td class="categoryName">
						<portlet:renderURL var="folderViewURL">
							<portlet:param name="<%= ViewController.VIEW_CURRENT_FOLDER %>" value="${veeraRow[1].entryId}" />
						</portlet:renderURL>
						<div class="title">
							<a href="${folderViewURL}">
								<c:out value="${veeraRow[1].name}" />
							</a>
						</div>
						<%
						// Get category id
						Integer categoryRightId = ((ArrayList<VeeraCategory>)pageContext.getAttribute("veeraRow")).get(1).getEntryId();
						int formsCount2 = ((ArrayList<VeeraCategory>)pageContext.getAttribute("veeraRow")).get(1).getFormCount();
						%>
						<span class="details"><%= getCategoryFormCountText(formsCount2) %></span>
					</td>
					
					<c:choose>
						<c:when test="${veeraRow[1].description ne null && fn:length(veeraRow[1].description) > 0}">
							<% 
							String fullDescription = ((ArrayList<VeeraCategory>)pageContext.getAttribute("veeraRow")).get(1).getDescription(); 
							%>
							<td class="categoryInfo" onclick="
								var html = '<c:out value="<%= fullDescription %>"/>';
								var popup = new Liferay.Popup(
								{
									header: 'Kategorian kuvaus',
									position: [150,150],
									modal: false,
									width: 500,
									height: 300,
									xy: ['center', 100],
									message: html
								}
								);" title="Näytä kuvaus">
							</td>
						</c:when>
						<c:otherwise>
							<td></td>
						</c:otherwise>	
					</c:choose>		
					<c:if test="${isAdmin == true}">
						<td>
							<div class="btn_container_admin_actions">
							<% 
								String categoryId = Integer.toString(((ArrayList<VeeraCategory>)pageContext.getAttribute("veeraRow")).get(1).getEntryId());
								String companyIdString = Long.toString(((ArrayList<VeeraCategory>)pageContext.getAttribute("veeraRow")).get(1).getCompanyId());
							%>
							<portlet:renderURL var="editCategoryURL">
								<portlet:param name="action" value="edit" />
								<portlet:param name="<%= EditController.EDIT_TYPE %>" value="<%= EditController.SHOW_EDIT_CATEGORY %>" /> 
								<portlet:param name="editCategoryId" value="<%= categoryId %>" />
								<portlet:param name="editCompanyId" value="<%= companyIdString %>" />
								<portlet:param name="<%= EditController.CURRENT_CATEGORY %>" value="<%= currentCategory.getEntryId().toString() %>" />
							</portlet:renderURL>
							<a href="${editCategoryURL}">Muokkaa</a>
							<portlet:actionURL var="removeCategoryURL">
								<portlet:param name="action" value="edit" />
								<portlet:param name="<%= EditController.EDIT_TYPE %>" value="<%= EditController.ADMIN_REMOVE_CATEGORY_ACTION %>" />
								<portlet:param name="removeCategoryId" value="<%= categoryId %>" />
								<portlet:param name="removeCompanyId" value="<%= companyIdString %>" />
								<portlet:param name="<%= EditController.CURRENT_CATEGORY %>" value="<%= currentCategory.getEntryId().toString() %>" />
							</portlet:actionURL>
							<a href="${removeCategoryURL}">Poista</a>
							</div>
						</td>
					</c:if>
				</c:when>
				<c:otherwise>
					<td class="categoryName"></td>
					<td></td>
				</c:otherwise>
				</c:choose>
			</tr>					
		</c:forEach>
	</table>
	
	<!-- / Category listing -->

    <%
	   if (categories.size() > 0 && forms.size() > 0) {
	%>
	<!-- Separator for folders & forms -->
	<br/><br/>
	<% } %>
	
	<!-- Forms listing -->
	<% if (forms.size() > 0) { %>
	<table class="taglib-search-iterator">
		<tbody>
			<tr class="portlet-section-header">
				<th>Palvelun nimi</th>
				<c:if test="${isAdmin == true}">
					<th>Ylläpito</th>
				</c:if>
			</tr>
			<c:forEach var="veeraForm" items="${veeraForms}">
				<!--<tr class="portlet-section-<%= c++ % 2 == 1 ? "body" : "alternate" %>">-->
				<tr>
					<td valign="middle" align="left" class="col-1">
						<!-- URL to formview.jsp -->
						<portlet:renderURL var="formViewURL"
							windowState="<%= WindowState.MAXIMIZED.toString() %>">
							<portlet:param name="action" value="formview" />
							<portlet:param name="type" value="${veeraForm.type}" />
							<portlet:param name="entryId" value="${veeraForm.entryId}" />
							<portlet:param name="identity" value="${veeraForm.identity}" />
							<portlet:param name="identity2" value="${veeraForm.identity2}" />
							<portlet:param name="hasHelpContent" value="${veeraForm.helpContent != null}" />
							<portlet:param name="<%= EditController.CURRENT_CATEGORY %>" value="<%= currentCategory.getEntryId().toString() %>" />
						</portlet:renderURL>
						<a href="${formViewURL}">
							<b>${veeraForm.identity_64}</b>
						</a>
					</td>
					<%
					// Check that user is an admin
					%>
					<c:if test="${isAdmin == true}">
						<td class="col-2" valign="middle" align="right">						
							<div class="btn_container_admin_actions">
								<% int formIdInt = ((VeeraForm)pageContext.getAttribute("veeraForm")).getEntryId();
								   String formId = Integer.toString(formIdInt);
								%>
								<portlet:renderURL var="editFormURL">
									<portlet:param name="action" value="edit" />
									<portlet:param name="<%= EditController.EDIT_TYPE %>" value="<%= EditController.SHOW_EDIT_FORM %>" /> 
									<portlet:param name="formId" value="<%= formId %>" />
									<portlet:param name="<%= EditController.CURRENT_CATEGORY %>" value="<%= currentCategory.getEntryId().toString() %>" />
								</portlet:renderURL>
								<a href="${editFormURL}">Muokkaa</a>

								<portlet:actionURL var="removeFormURL">
									<portlet:param name="action" value="edit" />
									<portlet:param name="<%= EditController.EDIT_TYPE %>" value="<%= EditController.ADMIN_REMOVE_FORM_ACTION %>" />
									<portlet:param name="formId" value="<%= formId %>" />
									<portlet:param name="<%= EditController.CURRENT_CATEGORY %>" value="<%= currentCategory.getEntryId().toString() %>" />					
								</portlet:actionURL>
								<a href="${removeFormURL}">Poista</a>
							</div>
						</td>
					</c:if>
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
	<% } // Forms if ends
	
	/* Inform if there isn't any folders or forms */
	if (categories.size() == 0 && forms.size() == 0) {
		%>
		<table class="taglib-search-iterator">
			<tbody>
				<tr>
					<td>&nbsp;</td>					
				</tr>
				<tr>
					<td class="noResults">Tässä palveluryhmässä ei ole toistaiseksi sähköisiä
					asiointipalveluja. Tämän palveluryhmän uusista palveluista
					tiedotetaan erikseen.</td>
				</tr>
			</tbody>
		</table>
		<%
	}
	
	%>
	<!-- URL to formview.jsp -->
	<portlet:renderURL var="formViewURL"
		windowState="<%= WindowState.MAXIMIZED.toString() %>">
		<portlet:param name="action" value="formview" />
		<portlet:param name="type" value="${veeraForm.type}" />
		<portlet:param name="identity" value="${veeraForm.identity}" />
		<portlet:param name="identity2" value="${veeraForm.identity2}" />
		<portlet:param name="<%= EditController.CURRENT_CATEGORY %>" value="<%= currentCategory.getEntryId().toString() %>" />
	</portlet:renderURL>
	<a href="${formViewURL}">
		${veeraForm.identity_64}
	</a>
	</div>
</div>
