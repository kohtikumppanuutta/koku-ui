<%@ include file="init.jsp"%>


<portlet:actionURL  var="configActionURL" portletMode="EDIT" >
	<portlet:param name="myaction" value="naviconfig"  />
</portlet:actionURL>
<div>
	<c:if test="${fn:contains(configActionURL, '/default/')}">
		<div class="naviEditDoNotEditHere">
			<spring:message code="config.doNotEditHere" />	
		</div>
	</c:if>

	<!-- Editing only available Gatein -->
	<c:if test="${fn:contains(configActionURL, '/classic/')}">
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
	</c:if>
	
</div>
<script type="text/javascript">
	jQuery('#<%= Constants.PREF_NAVI_RELATIVE_PATH %>').val("<%= useRelativePath %>");
	jQuery('#<%= Constants.PREF_NAVI_KKS %>').val("<%= kksPref %>");
	jQuery('#<%= Constants.PREF_NAVI_LOK %>').val("<%= lokPref %>");
	jQuery('#<%= Constants.PREF_NAVI_PYH %>').val("<%= pyhPref %>");
</script>