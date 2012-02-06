package fi.arcusys.koku.web;

import static fi.arcusys.koku.util.Constants.JSON_LOGIN_STATUS;
import static fi.arcusys.koku.util.Constants.PORTAL_MODE_KUNPO;
import static fi.arcusys.koku.util.Constants.PORTAL_MODE_LOORA;
import static fi.arcusys.koku.util.Constants.RESPONSE;
import static fi.arcusys.koku.util.Constants.TOKEN_STATUS_INVALID;
import static fi.arcusys.koku.util.Properties.IS_KUNPO_PORTAL;
import static fi.arcusys.koku.util.Properties.PORTAL_MODE;
import static fi.arcusys.koku.util.Properties.VETUMA_ENABLED;

import javax.portlet.PortletSession;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;

import fi.arcusys.koku.util.PortalRole;

public class AbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractController.class);

	/**
	 * Resolves which portalRole portal has
	 * 
	 * @return PortalRole
	 */
	protected PortalRole getPortalRole() {		
		if (PORTAL_MODE.contains(PORTAL_MODE_KUNPO)) {
			return PortalRole.CITIZEN;
		} else if (PORTAL_MODE.contains(PORTAL_MODE_LOORA)) {
			return PortalRole.EMPLOYEE;
		} else {
			throw new IllegalArgumentException("PortalMode not supported: "+ PORTAL_MODE);
		}		
	}
	
	
	protected String authenticationFailed(ModelMap modelMap, String username){
		LOG.error("Strong authentication required! User '"+username+"' is not Vetuma authenticated!");	
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_LOGIN_STATUS, TOKEN_STATUS_INVALID);
		modelMap.addAttribute(RESPONSE, jsonModel);
		return AjaxViewResolver.AJAX_PREFIX;
	}

	
	/**
	 * Returns true when following conditions are true:
	 * <ul>
	 * <li>Vetuma is enabled (vetuma.authentication=true)</li>
	 * <li>User is _NOT_ authenticated by using strong authentication (Vetuma)</li>
	 * <li>Portal is Kunpo (enviroment.name=kunpo)</li>
	 * </ul>
	 * @param portletSession
	 * @return 
//	 */
//	protected boolean isInvalidStrongAuthentication(PortletSession portletSession) {
//		// if (VETUMA_ENABLED && IS_KUNPO_PORTAL && !AuthenticationUtil.getUserInfoFromSession(portletSession).hasStrongAuthentication()) {
//		if (VETUMA_ENABLED && IS_KUNPO_PORTAL) {
//			return true;
//		} else {
//			return false;
//		}
//	}
}
