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
import fi.arcusys.koku.tiva.TivaCitizenServiceHandle;
import fi.arcusys.koku.users.UserIdResolver;
import fi.arcusys.koku.util.PortalRole;
import fi.koku.settings.KoKuPropertiesUtil;

/**
 * Handles ajax request and return the response with json string
 * @author Jinhua Chen
 * Jun 22, 2011
 */

@Controller("ajaxController")
@RequestMapping(value = "VIEW")
public class AjaxController {

	private static final Logger LOGGER = Logger.getLogger(AjaxController.class);	
	private static final String PORTAL_MODE;
	
	static {
		PORTAL_MODE = KoKuPropertiesUtil.get("environment.name");
		if (PORTAL_MODE == null) {
			throw new ExceptionInInitializerError("environment.name is null!");
		}
	}
	
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
		if ((token == null || token.isEmpty()  && userId != null)) {
			try {
				token = resolveIntalioToken(session, username);
				session.setAttribute(ATTR_TOKEN, token);
			} catch (IntalioAuthException iae) {
				LOGGER.error("Couldn't resolve intalio token. Username: '"+username+"' ErrorMsg: "+iae.getMessage());
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
		
		JSONObject jsonModel = getJsonModel(username, userId, token, portalRole);
		modelmap.addAttribute(RESPONSE, jsonModel);
		return AjaxViewResolver.AJAX_PREFIX;
	}
		
	private String resolveIntalioToken(PortletSession session, String userId) throws IntalioAuthException {
		TaskHandle handle = new TaskHandle();
		// Magic password! Fix also TaskManagerController magic password when possible.
		return handle.getTokenByUser(INTALIO_GROUP_PREFIX + userId, "test");
	}

	/**
	 * Gets the amount of new messages of Inbox and Archive_Inbox and puts values to model
	 * @param userId user that message belong to
	 * @return Json object contains result
	 */
	public JSONObject getJsonModel(String username, String userId, String token, PortalRole role) {
		JSONObject jsonModel = new JSONObject();
		if (userId == null) {
			jsonModel.put(JSON_LOGIN_STATUS, TOKEN_STATUS_INVALID);
		} else {			
			jsonModel.put(JSON_LOGIN_STATUS, TOKEN_STATUS_VALID);			
			jsonModel.put(JSON_INBOX, String.valueOf(getNewMessageNum(userId, KokuFolderType.INBOX)));			
			jsonModel.put(JSON_ARCHIVE_INBOX, String.valueOf(getNewMessageNum(userId, KokuFolderType.ARCHIVE_INBOX)));
			if (role.equals(PortalRole.CITIZEN)) {				
				jsonModel.put(JSON_CONSENTS_TOTAL, String.valueOf(getTotalAssignedConsents(userId)));
				jsonModel.put(JSON_APPOINTMENT_TOTAL, String.valueOf(getTotalAssignedAppointments(userId)));
				try {
					jsonModel.put(JSON_REQUESTS_TOTAL, String.valueOf(getTotalRequests(userId, token)));
				} catch (Exception e) {
					LOGGER.error("Coulnd't get TotalRequests (Valtakirja yht.). See following errorMsg: "+e.getMessage());
				}
			}
		}
		return jsonModel;
	}
	
	/**
	 * Gets number of new messages in the given folder type from web services
	 * 
	 * @param userId
	 * @param folderType
	 * @return number of messages
	 */
	private int getNewMessageNum(String userId, KokuFolderType folderType) {
		MessageHandle messageHandle = new MessageHandle();
		return messageHandle.getUnreadMessages(userId, folderType);
	}
	
	/**
	 * Returns total amount of assigned consents (not just new ones)
	 * 
	 * @param userId
	 * @return number of assigned consents
	 */
	private int getTotalAssignedConsents(String userId) {
		TivaCitizenServiceHandle handle = new TivaCitizenServiceHandle();
		return handle.getTotalAssignedConsents(userId);
	}
	
	/**
	 * Returns total amount of assigned appointments
	 * 
	 * @param userId
	 * @return number of assigned appointments
	 */
	private int getTotalAssignedAppointments(String userId) {
		AvCitizenServiceHandle handle = new AvCitizenServiceHandle(userId);
		return handle.getTotalAppointmentsNum(userId, TASK_TYPE_APPOINTMENT_INBOX_CITIZEN);
	}
	
	/**
	 * Returns total amount of requests
	 * 
	 * @param userId
	 * @return number or requests
	 */
	private int getTotalRequests(String username, String token) {
		if (username != null && !username.isEmpty() && token != null && !token.isEmpty()) {
			TaskHandle handle = new TaskHandle(token, username);
			return handle.getRequestsTasksTotalNumber();			
		} else {
			return 0;
		}
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
		if (PORTAL_MODE.contains(PORTAL_MODE_KUNPO)) {
			return PortalRole.CITIZEN;
		} else if (PORTAL_MODE.contains(PORTAL_MODE_LOORA)) {
			return PortalRole.EMPLOYEE;
		} else {
			throw new IllegalArgumentException("PortalMode not supported: "+ PORTAL_MODE);
		}
	}
}
