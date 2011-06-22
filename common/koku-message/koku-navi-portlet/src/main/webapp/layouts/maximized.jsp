<%@ taglib uri="/WEB-INF/theme/portal-layout.tld" prefix="p"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
                      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Koku project</title>
<meta http-equiv="Content-Type" content="text/html;" />
<p:theme themeName="naviTheme" />
<p:headerContent />
</head>
<body>
<p:region regionName='AJAXScripts' regionID='AJAXScripts'/>
	<div id="login-modal" style="display:none">
      <div id="login-modal-msg" style="display:none;width:257px;height:157px">
      	<iframe src="" frameborder="0" width="257" height="157" scrolling="no" marginheight="0" marginwidth="0" name="login-content" class="login-content" id="loginIframe"></iframe>
      </div>
   </div>
	<div id="portal-container">
		<div id="header-container">
			<!-- Utility controls --> 
			<p:region regionName='dashboardnav' regionID='dashboardnav' />	
			<!-- navigation tabs and such --> 
			<p:region regionName='navigation' regionID='navigation' />	
		</div>
		<div id="content-container">
			<div id="portal-content"/>
				<table width="100%">
				<tr>
					<td valign="top">
                  	<p:region regionName='maximized' regionID='regionMaximized'/>
                	</td>
				</tr>
				</table>
			</div>
			
		</div>
	</div>

	<div id="footer-container">Powered by Arcusys. All rights reserved.</div>

</body>
</html>