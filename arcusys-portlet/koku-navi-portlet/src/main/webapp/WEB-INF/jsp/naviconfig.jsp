<%@ include file="init.jsp"%>


<portlet:actionURL  var="configActionURL" portletMode="EDIT" >
	<portlet:param name="myaction" value="naviconfig"  />
</portlet:actionURL>
<div>

	<% if (portalInfo.startsWith(Constants.PORTAL_JBOSS)) { %>
		<div class="naviEditDoNotEditHere">
			<spring:message code="config.doNotEditHere" />	
		</div>
	<!-- Editing only available Gatein -->
	<% } else if (portalInfo.startsWith(Constants.PORTAL_GATEIN)) { %>
		<form name="configForm" action="${configActionURL}" method="post" >
			<div>
				<div>
					<div class="naviEditHeader">
						<spring:message code="config.navigationPortletMode" />
					</div>
					<div class="naviEditValue">
						<select name="<%= Constants.PREF_NAVI_PORTAL %>" id="<%= Constants.PREF_NAVI_PORTAL %>">
							<option value="<%= PortalNavigationMode.LOORA.toString() %>"><spring:message code="config.navigationPortletMode.loora" /></option>
							<option value="<%= PortalNavigationMode.KUNPO.toString() %>"><spring:message code="config.navigationPortletMode.kunpo" /></option>
						</select>
					</div>
				</div>
<!-- 				<div> -->
<!-- 					<div class="naviEditHeader"> -->
<%-- 						<spring:message code="config.useRelativePath" /> --%>
<!-- 					</div> -->
<!-- 					<div class="naviEditValue"> -->
<%-- 						<select name="<%= Constants.PREF_NAVI_RELATIVE_PATH %>" id="<%= Constants.PREF_NAVI_RELATIVE_PATH %>"> --%>
<%-- 							<option value="false"><spring:message code="config.no" /></option> --%>
<%-- 							<option value="true"><spring:message code="config.yes" /></option> --%>
<!-- 						</select> -->
<!-- 					</div> -->
<!-- 				</div> -->
				<div>
					<div class="naviEditHeader">
						<spring:message code="config.navigationDefaultPath" />
					</div>				
					<div class="naviEditValue">
						<textarea rows="1" name="<%= Constants.PREF_NAVI_DEFAULT_PATH %>" id="<%= Constants.PREF_NAVI_DEFAULT_PATH %>"></textarea>
					</div>
				</div>
				<div>
					<div class="naviEditHeader">
						<spring:message code="config.pyhPath" />
					</div>				
					<div class="naviEditValue">
						<textarea rows="1" name="<%= Constants.PREF_NAVI_PYH %>" id="<%= Constants.PREF_NAVI_PYH %>"></textarea>
					</div>
				</div>
				<div>
					<div class="naviEditHeader">
						<spring:message code="config.lokPath" />
					</div>			
					<div class="naviEditValue">
						<textarea rows="1" name="<%= Constants.PREF_NAVI_LOK %>" id="<%= Constants.PREF_NAVI_LOK %>"></textarea>
					</div>
				</div>
				<div>
					<div class="naviEditHeader">
						<spring:message code="config.kksPath" />
					</div>			
					<div class="naviEditValue">
						<textarea rows="1" name="<%= Constants.PREF_NAVI_KKS %>" id="<%= Constants.PREF_NAVI_KKS %>"></textarea>				
					</div>
				</div>
				<div>
					<div class="naviEditSubmit">
						<input type="submit" value="<spring:message code="config.save"/>" />
					</div>
				</div>
			</div>
		</form>
	<% } else {
			// TODO: You should not end here! Show error message!
		}
		%>	
</div>
<script type="text/javascript">
	jQuery('#<%= Constants.PREF_NAVI_RELATIVE_PATH %>').val("<%= useRelativePath %>");
	jQuery('#<%= Constants.PREF_NAVI_KKS %>').val("<%= kksPref %>");
	jQuery('#<%= Constants.PREF_NAVI_LOK %>').val("<%= lokPref %>");
	jQuery('#<%= Constants.PREF_NAVI_PYH %>').val("<%= pyhPref %>");
	jQuery('#<%= Constants.PREF_NAVI_DEFAULT_PATH %>').val("<%= defaultPathPref %>");
	jQuery('#<%= Constants.PREF_NAVI_PORTAL %>').val("<%= naviPortalMode %>");
</script>