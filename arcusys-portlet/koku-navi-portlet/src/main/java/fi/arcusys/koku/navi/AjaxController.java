package fi.arcusys.koku.navi;

import static fi.arcusys.koku.util.Constants.*;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.ResourceResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import fi.arcusys.koku.av.AvCitizenServiceHandle;
import fi.arcusys.koku.exceptions.IntalioAuthException;
import fi.arcusys.koku.intalio.TaskHandle;
import fi.arcusys.koku.kv.message.MessageHandle;
import fi.arcusys.koku.kv.model.KokuFolderType;
import fi.arcusys.koku.navi.util.QueryProcess;
import fi.arcusys.koku.navi.util.impl.QueryProcessCitizenImpl;
import fi.arcusys.koku.navi.util.impl.QueryProcessDummyImpl;
import fi.arcusys.koku.navi.util.impl.QueryProcessEmployeeImpl;
import fi.arcusys.koku.tiva.TivaCitizenServiceHandle;
import fi.arcusys.koku.users.UserIdResolver;
import fi.arcusys.koku.util.PortalRole;
import fi.arcusys.koku.util.Properties;

/**
 * Handles ajax request and return the response with json string
 * @author Jinhua Chen
 * Jun 22, 2011
 */

@Controller("ajaxController")
@RequestMapping(value = "VIEW")
public class AjaxController {

	private static final Logger LOGGER = Logger.getLogger(AjaxController.class);	
	
	/**
	 * Gets the amount of new unread messages for user
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response PortletResponse
	 * @return amount of new unread messages in Json
	 */
	@ResourceMapping(value = "update")
	public String showAjax(ModelMap modelmap, PortletRequest request, PortletResponse response) {
		
		String username = request.getRemoteUser();
		String userId = null;
				
		try {
			if (username != null) {
				UserIdResolver resolver = new UserIdResolver();
				userId = resolver.getUserId(username, getPortalRole());
			}
		} catch (Exception e) {
			LOGGER.error("Error while trying to resolve userId. Usually WSDL location is wrong or server down. See following error msg: "+e.getMessage());
		}
		
		PortletSession session = request.getPortletSession();
		
		// Resolve user Intalio token (if not already done)
		String token = (String) session.getAttribute(ATTR_TOKEN);
		if (userId != null &&  (token == null || token.isEmpty())) {
			try {
				token = resolveIntalioToken(session, username);
				session.setAttribute(ATTR_TOKEN, token);
			} catch (IntalioAuthException iae) {
				LOGGER.error("Authentication exception. Invalid user. Username: '"+username+"' ErrorMsg: "+iae.getMessage());
				session.setAttribute(ATTR_TOKEN, "No token");
			} catch (Exception e) {
				session.setAttribute(ATTR_TOKEN, "No token");
				LOGGER.error("Error while trying to resolve Intalio token. Username: '"+username+"': ",e);
			}
		}
		
		// Resolve portalRole (Employee or Citizen portal)
		String portal = (String) session.getAttribute(ATTR_PORTAL_ROLE);
		PortalRole portalRole = null;
		if (portal == null || portal.isEmpty()) {
			portalRole = getPortalRole();
			session.setAttribute(ATTR_PORTAL_ROLE, portalRole.toString());
		} else {
			portalRole = PortalRole.fromValue(portal);		
		}
		
		QueryProcess query = null;
		switch(getPortalRole()) {
			case CITIZEN: query = new QueryProcessCitizenImpl(null); break;
			case EMPLOYEE: query = new QueryProcessEmployeeImpl(null); break;
			default: query = new QueryProcessDummyImpl(null); break;
		}
		
		JSONObject jsonModel = query.getJsonModel(username, userId, token, portalRole);
		modelmap.addAttribute(RESPONSE, jsonModel);
		return AjaxViewResolver.AJAX_PREFIX;
	}
		
	private String resolveIntalioToken(PortletSession session, String userId) throws IntalioAuthException {
		TaskHandle handle = new TaskHandle();
		// Magic password! Fix also TaskManagerController magic password when possible.
		return handle.getTokenByUser(INTALIO_GROUP_PREFIX + userId, "test");
	}	
	
	/**
	 * Creates render url mainly for gatein portal container
	 * @param newNaviType navigation tab name
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response ResourceResponse
	 * @return render url in Json 
	 */
	@ResourceMapping(value = "createNaviRenderUrl")
	public String createNaviRenderUrl(
			@RequestParam(value = "newNaviType") String newNaviType,
			ModelMap modelmap, PortletRequest request, ResourceResponse response) {
		PortletURL renderUrlObj = response.createRenderURL();
		renderUrlObj.setParameter(ATTR_MY_ACTION, MY_ACTION_SHOW_NAVI);
		renderUrlObj.setParameter(ATTR_NAVI_TYPE, newNaviType);
		String renderUrlString = renderUrlObj.toString();
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_RENDER_URL, renderUrlString);
		modelmap.addAttribute(RESPONSE, jsonModel);
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	/**
	 * Resolves which portalRole portal has
	 * 
	 * FIXME: Super stupid portalRole resolving. For now
	 * we determine if portal is citizen or employee only by portal name. 
	 * 
	 * @param request PortletRequest
	 * @return PortalRole
	 */
	protected PortalRole getPortalRole() {
		if (Properties.PORTAL_MODE.contains(PORTAL_MODE_KUNPO)) {
			return PortalRole.CITIZEN;
		} else if (Properties.PORTAL_MODE.contains(PORTAL_MODE_LOORA)) {
			return PortalRole.EMPLOYEE;
		} else {
			throw new IllegalArgumentException("PortalMode not supported: "+ Properties.PORTAL_MODE);
		}
	}
}
