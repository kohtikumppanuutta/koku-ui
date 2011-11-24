<%@ include file="init.jsp"%>


<portlet:actionURL  var="configActionURL" portletMode="EDIT" >
	<portlet:param name="myaction" value="naviconfig"  />
</portlet:actionURL>
<div>

	<div class="kokuNavigationInfo">
		Navigaatio portletin asetuksia voi muuttaa <pre>koku-settings.properties</pre> tiedostosta käsin
	</div>
			<div class="kokuNavigationSettings">
				<div>
					<div class="naviEditHeader">
						<spring:message code="config.navigationDefaultPath" />:
					</div>				
					<div class="naviEditValue">
						<div id="<%= Constants.PREF_NAVI_DEFAULT_PATH %>">'<%= defaultPath %>'</div>
					</div>
				</div>
				<div>
					<div class="naviEditHeader">
						<spring:message code="config.frontpagePath" />:
					</div>			
					<div class="naviEditValue">
						<div id="<%= Constants.PREF_NAVI_FRONTPAGE %>">'<%= frontPagePath %>'</div>				
					</div>
				</div>
				<div>
					<div class="naviEditHeader">
						<spring:message code="config.pyhPath" />:
					</div>				
					<div class="naviEditValue">
						<div" id="<%= Constants.PREF_NAVI_PYH %>">'<%= pyhPref %>'</div>
					</div>
				</div>
				<div>
					<div class="naviEditHeader">
						<spring:message code="config.lokPath" />:
					</div>			
					<div class="naviEditValue">
						<div id="<%= Constants.PREF_NAVI_LOK %>">'<%= lokPref %>'</div>
					</div>
				</div>
				<div>
					<div class="naviEditHeader">
						<spring:message code="config.kksPath" />:
					</div>			
					<div class="naviEditValue">
						<div id="<%= Constants.PREF_NAVI_KKS %>">'<%= kksPref %>'</div>				
					</div>
				</div>
			</div>
</div>
