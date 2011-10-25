<%@ include file="init.jsp"%>


<portlet:actionURL  var="configActionURL" portletMode="EDIT" >
	<portlet:param name="myaction" value="naviconfig"  />
</portlet:actionURL>
<div>


<c:choose>  
	<c:when test="${fn:contains(configActionURL, '/default/')}">  
		<div class="naviEditDoNotEditHere">
			<spring:message code="config.doNotEditHere" />	
		</div>
		 </c:when>  
	<c:otherwise>
	<!-- Editing only available Gatein -->

		<form name="configForm" action="${configActionURL}" method="post" >
			<div>
				<div>
					<div class="naviEditHeader">
						<spring:message code="config.useRelativePath" />
					</div>
					<div class="naviEditValue">
						<select name="<%= Constants.PREF_NAVI_RELATIVE_PATH %>" id="<%= Constants.PREF_NAVI_RELATIVE_PATH %>">
							<option value="false"><spring:message code="config.no" /></option>
							<option value="true"><spring:message code="config.yes" /></option>
						</select>
					</div>
				</div>
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
	</c:otherwise>
	</c:choose>  
	
</div>
<script type="text/javascript">
	jQuery('#<%= Constants.PREF_NAVI_RELATIVE_PATH %>').val("<%= useRelativePath %>");
	jQuery('#<%= Constants.PREF_NAVI_KKS %>').val("<%= kksPref %>");
	jQuery('#<%= Constants.PREF_NAVI_LOK %>').val("<%= lokPref %>");
	jQuery('#<%= Constants.PREF_NAVI_PYH %>').val("<%= pyhPref %>");
	jQuery('#<%= Constants.PREF_NAVI_DEFAULT_PATH %>').val("<%= defaultPathPref %>");
</script>