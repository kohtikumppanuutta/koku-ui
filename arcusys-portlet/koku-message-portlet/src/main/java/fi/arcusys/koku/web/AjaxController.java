package fi.arcusys.koku.web;

import static fi.arcusys.koku.util.Constants.ATTR_APPLICATION_ID;
import static fi.arcusys.koku.util.Constants.ATTR_APPOIMENT_ID;
import static fi.arcusys.koku.util.Constants.ATTR_AUTHORIZATION_ID;
import static fi.arcusys.koku.util.Constants.ATTR_CONSENT_ID;
import static fi.arcusys.koku.util.Constants.ATTR_CURRENT_PAGE;
import static fi.arcusys.koku.util.Constants.ATTR_KEYWORD;
import static fi.arcusys.koku.util.Constants.ATTR_KOKU_USER;
import static fi.arcusys.koku.util.Constants.ATTR_MESSAGE_ID;
import static fi.arcusys.koku.util.Constants.ATTR_MY_ACTION;
import static fi.arcusys.koku.util.Constants.ATTR_ORDER_TYPE;
import static fi.arcusys.koku.util.Constants.ATTR_REQUEST_ID;
import static fi.arcusys.koku.util.Constants.ATTR_RESPONSE_ID;
import static fi.arcusys.koku.util.Constants.ATTR_TARGET_PERSON;
import static fi.arcusys.koku.util.Constants.ATTR_TASK_TYPE;
import static fi.arcusys.koku.util.Constants.ATTR_USERNAME;
import static fi.arcusys.koku.util.Constants.JSON_RENDER_URL;
import static fi.arcusys.koku.util.Constants.JSON_RESULT;
import static fi.arcusys.koku.util.Constants.MY_ACTION_SHOW_APPLICATION_KINDERGARTEN;
import static fi.arcusys.koku.util.Constants.MY_ACTION_SHOW_APPOINTMENT;
import static fi.arcusys.koku.util.Constants.MY_ACTION_SHOW_CONSENT;
import static fi.arcusys.koku.util.Constants.MY_ACTION_SHOW_REQUEST;
import static fi.arcusys.koku.util.Constants.MY_ACTION_SHOW_REQUEST_RESPONSE;
import static fi.arcusys.koku.util.Constants.MY_ACTION_SHOW_TIPY;
import static fi.arcusys.koku.util.Constants.MY_ACTION_SHOW_WARRANT;
import static fi.arcusys.koku.util.Constants.RESPONSE;
import static fi.arcusys.koku.util.Constants.RESPONSE_OK;
import static fi.arcusys.koku.util.Constants.SUGGESTION_APPLICATION_KINDERGARTEN;
import static fi.arcusys.koku.util.Constants.SUGGESTION_CONSENT;
import static fi.arcusys.koku.util.Constants.SUGGESTION_WARRANT;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import fi.arcusys.koku.av.AvCitizenServiceHandle;
import fi.arcusys.koku.av.AvEmployeeServiceHandle;
import fi.arcusys.koku.hak.model.HakServiceHandle;
import fi.arcusys.koku.kv.message.MessageHandle;
import fi.arcusys.koku.tiva.TivaCitizenServiceHandle;
import fi.arcusys.koku.tiva.TivaEmployeeServiceHandle;
import fi.arcusys.koku.tiva.warrant.citizens.KokuCitizenWarrantHandle;
import fi.arcusys.koku.tiva.warrant.employee.KokuEmployeeWarrantHandle;
import fi.arcusys.koku.users.KokuUser;
import fi.arcusys.koku.users.KokuUserService;
import fi.arcusys.koku.users.UserIdResolver;
import fi.arcusys.koku.util.PortalRole;
import fi.arcusys.koku.util.Properties;
import fi.arcusys.koku.web.util.QueryProcess;
import fi.arcusys.koku.web.util.impl.QueryProcessDummyImpl;
import fi.arcusys.koku.web.util.impl.QueryProcessCitizenImpl;
import fi.arcusys.koku.web.util.impl.QueryProcessEmployeeImpl;
import fi.koku.portlet.filter.userinfo.UserInfo;

/**
 * Hanldes ajax request from portlet and returns the response with json string
 * @author Jinhua Chen
 * Jun 22, 2011
 */

@Controller("ajaxController")
@RequestMapping(value = "VIEW")
public class AjaxController extends AbstractController {
	
	private static final int MAX_SUGGESTION_RESULTS = 5;
		
	@Resource
	private ResourceBundleMessageSource messageSource;

	private static final Logger LOG = Logger.getLogger(AjaxController.class);
	
	/**
	 * Handles portlet ajax request of tasks such as messages, requests,
	 * appointments, consents and so on, distinguished by task type
	 * @param page the page number
	 * @param taskType type of requested task
	 * @param keyword keyword for filtering
	 * @param field field for filtering
	 * @param orderType order of tasks
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response PortletResponse
	 * @return tasks in Json format
	 */
	@ResourceMapping(value = "getTask")
	public String getTasks(@RequestParam(value = "page") int page,
			@RequestParam(value = "taskType") String taskType,
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "field") String field,
			@RequestParam(value = "orderType") String orderType,
			ModelMap modelmap, PortletRequest request, PortletResponse response) {
		
		PortletSession portletSession = request.getPortletSession();		
		String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		
		registerUserToWS(portletSession);
	    
		if (isInvalidStrongAuthentication(portletSession)) {
			return authenticationFailed(modelmap, username);
		}
				
		String userId = null;
		try {
			UserIdResolver resolver = new UserIdResolver();
			userId = resolver.getUserId(username, getPortalRole());			
		} catch (Exception e) {
			LOG.error("Error while trying to resolve userId. See following error msg: "+ e);
		}
		
		QueryProcess query = null;
		
		if (Properties.IS_KUNPO_PORTAL) {
			query = new QueryProcessCitizenImpl(messageSource);
		} else if (Properties.IS_LOORA_PORTAL) {
			query = new QueryProcessEmployeeImpl(messageSource);
		} else {
			query = new QueryProcessDummyImpl(messageSource);
			LOG.error("PortalMode unknown! Only kunpo/loora portal modes are supported. Please check that properties file is properly configured.");
		}	
		
		JSONObject jsonModel = query.getJsonModel(taskType, page, keyword, field, orderType, userId);
		modelmap.addAttribute(RESPONSE, jsonModel);		
		return AjaxViewResolver.AJAX_PREFIX;
	}	
	
	/**
	 * KOKU-805
	 * 
	 * Pilot KunPo users register themselves one-by-one in KunPo, they're not registered a priori.
	 * These users need to be added to Intalio LDAP also for HAK/TIVA/KV/AV to work.
	 * 
	 * @param portletSession
	 */
	private void registerUserToWS(PortletSession portletSession) {
		/* No need to re-register user if already done */
	    if (portletSession.getAttribute(ATTR_KOKU_USER) != null) {
	    	return;
	    }
	    
		final String username = (String) portletSession.getAttribute(ATTR_USERNAME);
    	final UserInfo userInfo = (UserInfo) portletSession.getAttribute(UserInfo.KEY_USER_INFO);	    
    	LOG.info("Username: '"+username+"' Hetu: "+userInfo);
    	
    	if (userInfo == null) {	    		
    		LOG.error("UserInfo is null! Can't register user to WS. Username: '"+username+"'");
    		return;
    	}
    	
		final KokuUserService userService = new KokuUserService();
		KokuUser user = null;
    	if (Properties.IS_KUNPO_PORTAL) {
    		// Kunpo
    		user = userService.loginKunpo(username, userInfo.getPic());
    	} else {
    		// Loora
    		user = userService.loginLoora(username, userInfo.getPic());
    	}
    	if (user == null) {
    		// TODO: Remove if statement when Loora side implementation is ready
    		if (Properties.IS_KUNPO_PORTAL) {
    			LOG.error("For some reason userService didn't find KokuUser. Username: '"+username+"' SSN: '"+userInfo.getPic()+"' ");
    		}
    		user = new KokuUser();
    	}
    	portletSession.setAttribute(ATTR_KOKU_USER, user);	    	
	}
	
	/**
	 * Archives the messages
	 * @param messageList a list of message ids to be archived
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response PortletResponse
	 * @return action response 'OK' or 'FAIL'
	 */
	@ResourceMapping(value = "archiveMessage")
	public String doArchive(@RequestParam(value = "messageList[]") String[] messageList,
			ModelMap modelmap, PortletRequest request, PortletResponse response) {
		
		PortletSession portletSession = request.getPortletSession();		
		String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		if (isInvalidStrongAuthentication(portletSession)) {
			return authenticationFailed(modelmap, username);
		}
		
		MessageHandle msghandle = new MessageHandle();		
		List<Long> messageIds = new ArrayList<Long>();
		
		for(String msgId : messageList) {
			try {
				messageIds.add(Long.parseLong(msgId));				
			} catch (NumberFormatException nfe) {
				LOG.warn("Error while parsing messageIds. Username: '"+username+"'  MessageId is not valid number: '"+msgId+"'");
			}
		}
		
		String result = msghandle.archiveMessages(messageIds); // OK or FAIL
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_RESULT, result);
		modelmap.addAttribute(RESPONSE, jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	/**
	 * Deletes the messages
	 * @param messageList a list of message ids to be deleted
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response PortletResponse
	 * @return action response 'OK' or 'FAIL'
	 */
	@ResourceMapping(value = "deleteMessage")
	public String doDelete(@RequestParam(value = "messageList[]") String[] messageList,
			ModelMap modelmap, PortletRequest request, PortletResponse response) {
		
		PortletSession portletSession = request.getPortletSession();		
		String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		if (isInvalidStrongAuthentication(portletSession)) {
			return authenticationFailed(modelmap, username);
		}
		
		MessageHandle msghandle = new MessageHandle();		
		List<Long> messageIds = new ArrayList<Long>();
		
		for(String msgId : messageList) {
			try {
				messageIds.add(Long.parseLong(msgId));
			} catch (NumberFormatException nfe) {
				LOG.warn("Couldn't delete message! Invalid messageId. Username: '"+ username + "' MessageId: '"+ msgId + "'");
			}
		}
		
		String result = msghandle.deleteMessages(messageIds); // OK or FAIL
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_RESULT, result);
		modelmap.addAttribute(RESPONSE, jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	/**
	 * Revokes the consents
	 * @param messageList a list of message/consent ids to be deleted
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response PortletResponse
	 * @return action response 'OK' or 'FAIL'
	 */
	@ResourceMapping(value = "revokeConsent")
	public String revokeConsent(@RequestParam(value = "messageList[]") String[] messageList,
			ModelMap modelmap, PortletRequest request, PortletResponse response) {
		PortletSession portletSession = request.getPortletSession();				
		String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		if (isInvalidStrongAuthentication(portletSession)) {
			return authenticationFailed(modelmap, username);
		}
		UserIdResolver resolver = new UserIdResolver();
		String userId = resolver.getUserId(username, getPortalRole());

		PortalRole role = getPortalRole();
		TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle(userId);		
		
		for(String consentId : messageList) {
			tivaHandle.revokeOwnConsent(consentId);
		}
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_RESULT, RESPONSE_OK);
		modelmap.addAttribute(RESPONSE, jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	/**
	 * Revokes the warrants
	 * @param messageList a list of message/consent ids to be deleted
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response PortletResponse
	 * @return action response 'OK' or 'FAIL'
	 */
	@ResourceMapping(value = "revokeWarrant")
	public String revokeWarrant(
			@RequestParam(value = "messageList[]") String[] messageList,
			@RequestParam(value = "comment") String comment,
			ModelMap modelmap, 
			PortletRequest request,
			PortletResponse response) {
		PortletSession portletSession = request.getPortletSession();				
		String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		if (isInvalidStrongAuthentication(portletSession)) {
			return authenticationFailed(modelmap, username);
		}
		UserIdResolver resolver = new UserIdResolver();
		String userId = resolver.getUserId(username, getPortalRole());

		KokuCitizenWarrantHandle warrantHandle = new KokuCitizenWarrantHandle();		
		
		for(String authorizationId : messageList) {
			try {
				long authId = Long.parseLong(authorizationId);
				warrantHandle.revokeOwnAuthorization(authId, userId, comment);				
			} catch (NumberFormatException nfe) {
				LOG.error("Couldn't revoke authorization! Invalid authorizationId. Username: "+ username + " UserId: "+ userId + " AuthorizationId: "+ authorizationId + "Comment: " + comment);
			}
		}
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_RESULT, RESPONSE_OK);
		modelmap.addAttribute(RESPONSE, jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	
	/**
	 * Cancels appointments
	 * @param messageList
	 * @param targetPersons
	 * @param comment
	 * @param modelmap
	 * @param request
	 * @param response
	 * @return action response 'OK' or 'FAIL'
	 */
	@ResourceMapping(value = "cancelAppointment")
	public String cancelAppointment(@RequestParam(value = "messageList[]") String[] messageList,
			@RequestParam(value = "targetPersons[]", required=false) String[] targetPersons,
			@RequestParam(value = "comment") String comment,
			@RequestParam(value = "taskType") String taskType,
			ModelMap modelmap, PortletRequest request, PortletResponse response) {
		
		
		PortletSession portletSession = request.getPortletSession();				
		String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		UserIdResolver resolver = new UserIdResolver();
		String userId = resolver.getUserId(username, getPortalRole());
		if (isInvalidStrongAuthentication(portletSession)) {
			return authenticationFailed(modelmap, username);
		}
		
		if(taskType.endsWith("citizen")) {
			AvCitizenServiceHandle handle = new AvCitizenServiceHandle(userId);
			String appointmentId;
			String targetPerson;
			
			for(int i=0, l= messageList.length; i < l; i++) {
				appointmentId = messageList[i];
				targetPerson = targetPersons[i];
				handle.cancelAppointments(appointmentId, targetPerson, comment);
			}
		}else if(taskType.endsWith("employee")) {
			AvEmployeeServiceHandle handle = new AvEmployeeServiceHandle();
			String appointmentId;
			
			for(int i=0, l= messageList.length; i < l; i++) {
				appointmentId = messageList[i];
				handle.cancelAppointments(appointmentId, comment);
			}
		}
					
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_RESULT, RESPONSE_OK);
		modelmap.addAttribute(RESPONSE, jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	@ResourceMapping(value = "getSuggestion")
	public String getSuggestion(
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "suggestType") String suggestionType,
			ModelMap modelmap, 
			PortletRequest request, 
			PortletResponse response) {
		
		PortletSession portletSession = request.getPortletSession();				
		String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		if (isInvalidStrongAuthentication(portletSession)) {
			return authenticationFailed(modelmap, username);
		}
		
		@SuppressWarnings("rawtypes")
		List resultList = null;
		if (suggestionType.equals(SUGGESTION_CONSENT)) {
			TivaEmployeeServiceHandle tivaHandle = new TivaEmployeeServiceHandle();					
			resultList = tivaHandle.searchConsentTemplates(keyword, MAX_SUGGESTION_RESULTS);
		} else if (suggestionType.equals(SUGGESTION_WARRANT)) {
			KokuEmployeeWarrantHandle handle = new KokuEmployeeWarrantHandle();
			resultList = handle.searchWarrantTemplates(keyword, MAX_SUGGESTION_RESULTS);
		} else if (suggestionType.equals(SUGGESTION_APPLICATION_KINDERGARTEN)) {
			HakServiceHandle handle = new HakServiceHandle();
			resultList = handle.searchKindergartenByName(keyword, MAX_SUGGESTION_RESULTS);
		} 
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_RESULT, resultList);
		modelmap.addAttribute(RESPONSE, jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	
	/**
	 * Creates message render url mainly for gatein portal, and keeps the page
	 * parameters such as page id, task type, keyword
	 * @param messageId message id
	 * @param currentPage current page
	 * @param taskType request task type
	 * @param keyword keyword
	 * @param orderType order type
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response ResourceResponse
	 * @return Message render url in Json format
	 */
	@ResourceMapping(value = "createMessageRenderUrl")
	public String createMessageRenderUrl(
			@RequestParam(value = "messageId") String messageId,
			@RequestParam(value = "currentPage") String currentPage,
			@RequestParam(value = "taskType") String taskType,
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "orderType") String orderType,
			ModelMap modelmap, PortletRequest request, ResourceResponse response) {
		
		PortletSession portletSession = request.getPortletSession();				
		String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		if (isInvalidStrongAuthentication(portletSession)) {
			return authenticationFailed(modelmap, username);
		}
		
		PortletURL renderUrlObj = response.createRenderURL();
		renderUrlObj.setParameter( ATTR_MY_ACTION, "showMessage");
		renderUrlObj.setParameter( ATTR_MESSAGE_ID, messageId);
		renderUrlObj.setParameter( ATTR_CURRENT_PAGE, currentPage);
		renderUrlObj.setParameter( ATTR_TASK_TYPE, taskType);
		renderUrlObj.setParameter( ATTR_KEYWORD, keyword);
		renderUrlObj.setParameter( ATTR_ORDER_TYPE, orderType);
		try {
			renderUrlObj.setWindowState(WindowState.NORMAL);
		} catch (WindowStateException e) {
			LOG.error("Create message render url failed");
		}
		String renderUrlString = renderUrlObj.toString();
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_RENDER_URL, renderUrlString);
		modelmap.addAttribute(RESPONSE, jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	/**
	 * Creates request render url mainly for gatein portal, and keeps the page
	 * parameters such as page id, task type, keyword
	 * @param requestId request id
	 * @param currentPage current page
	 * @param taskType request task type
	 * @param keyword keyword
	 * @param orderType order type
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response ResourceResponse
	 * @return Request render url in Json format
	 */
	@ResourceMapping(value = "createRequestRenderUrl")
	public String createRequestRenderUrl(
			@RequestParam(value = "requestId") String requestId,
			@RequestParam(value = "currentPage") String currentPage,
			@RequestParam(value = "taskType") String taskType,
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "orderType") String orderType,
			ModelMap modelmap, PortletRequest request, ResourceResponse response) {
		
		PortletSession portletSession = request.getPortletSession();				
		String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		if (isInvalidStrongAuthentication(portletSession)) {
			return authenticationFailed(modelmap, username);
		}
		
		PortletURL renderUrlObj = response.createRenderURL();
		renderUrlObj.setParameter( ATTR_MY_ACTION, MY_ACTION_SHOW_REQUEST);
		renderUrlObj.setParameter( ATTR_REQUEST_ID, requestId);
		renderUrlObj.setParameter( ATTR_CURRENT_PAGE, currentPage);
		renderUrlObj.setParameter( ATTR_TASK_TYPE, taskType);
		renderUrlObj.setParameter( ATTR_KEYWORD, keyword);
		renderUrlObj.setParameter( ATTR_ORDER_TYPE, orderType);	
		try {
			renderUrlObj.setWindowState(WindowState.NORMAL);
		} catch (WindowStateException e) {
			LOG.error("Create request render url failed");
		}
		String renderUrlString = renderUrlObj.toString();
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_RENDER_URL, renderUrlString);
		modelmap.addAttribute(RESPONSE, jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	/**
	 * Creates request response render url mainly for gatein portal, and keeps the page
	 * parameters such as page id, task type, keyword
	 * @param requestId request id
	 * @param currentPage current page
	 * @param taskType request task type
	 * @param keyword keyword
	 * @param orderType order type
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response ResourceResponse
	 * @return Request render url in Json format
	 */
	@ResourceMapping(value = "createResponseRenderUrl")
	public String createResponseRenderUrl (
			@RequestParam(value = "responseId") String responseId,
			@RequestParam(value = "currentPage") String currentPage,
			@RequestParam(value = "taskType") String taskType,
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "orderType") String orderType,
			ModelMap modelmap, PortletRequest request, ResourceResponse response) {
		
		PortletSession portletSession = request.getPortletSession();				
		String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		if (isInvalidStrongAuthentication(portletSession)) {
			return authenticationFailed(modelmap, username);
		}
		
		PortletURL renderUrlObj = response.createRenderURL();
		renderUrlObj.setParameter( ATTR_MY_ACTION, MY_ACTION_SHOW_REQUEST_RESPONSE);
		renderUrlObj.setParameter( ATTR_RESPONSE_ID, responseId);
		renderUrlObj.setParameter( ATTR_CURRENT_PAGE, currentPage);
		renderUrlObj.setParameter( ATTR_TASK_TYPE, taskType);
		renderUrlObj.setParameter( ATTR_KEYWORD, keyword);
		renderUrlObj.setParameter( ATTR_ORDER_TYPE, orderType);	
		try {
			renderUrlObj.setWindowState(WindowState.NORMAL);
		} catch (WindowStateException e) {
			LOG.error("Create request render url failed");
		}
		String renderUrlString = renderUrlObj.toString();
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_RENDER_URL, renderUrlString);
		modelmap.addAttribute(RESPONSE, jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	/**
	 * Creates appointment render url mainly for gatein portal, and keeps the page
	 * parameters such as page id, task type, keyword
	 * @param appointmentId appointment id
	 * @param currentPage current page
	 * @param taskType request task type
	 * @param keyword keyword
	 * @param orderType order type
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response ResourceResponse
	 * @return Appointment render url in Json format
	 */
	@ResourceMapping(value = "createAppointmentRenderUrl")
	public String createAppointmentRenderUrl(
			@RequestParam(value = "appointmentId") String appointmentId,
			@RequestParam(value = "currentPage") String currentPage,
			@RequestParam(value = "taskType") String taskType,
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "orderType") String orderType,
			@RequestParam(value = "targetPerson") String targetPerson,
			ModelMap modelmap, PortletRequest request, ResourceResponse response) {

		PortletSession portletSession = request.getPortletSession();				
		String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		if (isInvalidStrongAuthentication(portletSession)) {
			return authenticationFailed(modelmap, username);
		}
		
		PortletURL renderUrlObj = response.createRenderURL();
		renderUrlObj.setParameter( ATTR_MY_ACTION, MY_ACTION_SHOW_APPOINTMENT);
		renderUrlObj.setParameter( ATTR_APPOIMENT_ID, appointmentId);
		renderUrlObj.setParameter( ATTR_CURRENT_PAGE, currentPage);
		renderUrlObj.setParameter( ATTR_TASK_TYPE, taskType);
		renderUrlObj.setParameter( ATTR_KEYWORD, keyword);
		renderUrlObj.setParameter( ATTR_ORDER_TYPE, orderType);	
		renderUrlObj.setParameter( ATTR_TARGET_PERSON, targetPerson);
		try {
			renderUrlObj.setWindowState(WindowState.NORMAL);
		} catch (WindowStateException e) {
			LOG.error("Create appointment render url failed");
		}
		String renderUrlString = renderUrlObj.toString();
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_RENDER_URL, renderUrlString);
		modelmap.addAttribute(RESPONSE, jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	/**
	 * Creates consent render url mainly for gatein portal, and keeps the page
	 * parameters such as page id, task type, keyword
	 * 
	 * @param consentId consent id
	 * @param currentPage current page
	 * @param taskType request task type
	 * @param keyword keyword
	 * @param orderType order type
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response ResourceResponse
	 * @return Consent render url in Json format
	 */
	@ResourceMapping(value = "createConsentRenderUrl")
	public String createConsentRenderUrl(
			@RequestParam(value = "consentId") String consentId,
			@RequestParam(value = "currentPage") String currentPage,
			@RequestParam(value = "taskType") String taskType,
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "orderType") String orderType,
			ModelMap modelmap, PortletRequest request, ResourceResponse response) {

		
		PortletSession portletSession = request.getPortletSession();				
		String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		if (isInvalidStrongAuthentication(portletSession)) {
			return authenticationFailed(modelmap, username);
		}
		
		PortletURL renderUrlObj = response.createRenderURL();
		renderUrlObj.setParameter( ATTR_MY_ACTION, MY_ACTION_SHOW_CONSENT);
		renderUrlObj.setParameter( ATTR_CONSENT_ID, consentId);
		renderUrlObj.setParameter( ATTR_CURRENT_PAGE, currentPage);
		renderUrlObj.setParameter( ATTR_TASK_TYPE, taskType);
		renderUrlObj.setParameter( ATTR_KEYWORD, keyword);
		renderUrlObj.setParameter( ATTR_ORDER_TYPE, orderType);	
		try {
			renderUrlObj.setWindowState(WindowState.NORMAL);
		} catch (WindowStateException e) {
			LOG.error("Create consent render url failed");
		}
		String renderUrlString = renderUrlObj.toString();
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_RENDER_URL, renderUrlString);
		modelmap.addAttribute(RESPONSE, jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	/**
	 * Creates warrant render url mainly for gatein portal, and keeps the page
	 * parameters such as page id, task type, keyword
	 * 
	 * @param authorizationId authorization id
	 * @param currentPage current page
	 * @param taskType request task type
	 * @param keyword keyword
	 * @param orderType order type
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response ResourceResponse
	 * @return Consent render url in Json format
	 */
	@ResourceMapping(value = "createWarrantRenderUrl")
	public String createWarrantRenderUrl(
			@RequestParam(value = "authorizationId") String authorizationId,
			@RequestParam(value = "currentPage") String currentPage,
			@RequestParam(value = "taskType") String taskType,
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "orderType") String orderType,
			ModelMap modelmap, PortletRequest request, ResourceResponse response) {

		PortletSession portletSession = request.getPortletSession();				
		String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		if (isInvalidStrongAuthentication(portletSession)) {
			return authenticationFailed(modelmap, username);
		}
		
		PortletURL renderUrlObj = response.createRenderURL();
		renderUrlObj.setParameter( ATTR_MY_ACTION, MY_ACTION_SHOW_WARRANT);
		renderUrlObj.setParameter( ATTR_AUTHORIZATION_ID, authorizationId);
		renderUrlObj.setParameter( ATTR_CURRENT_PAGE, currentPage);
		renderUrlObj.setParameter( ATTR_TASK_TYPE, taskType);
		renderUrlObj.setParameter( ATTR_KEYWORD, keyword);
		renderUrlObj.setParameter( ATTR_ORDER_TYPE, orderType);	
		
		try {
			renderUrlObj.setWindowState(WindowState.NORMAL);
		} catch (WindowStateException e) {
			LOG.error("Create consent render url failed");
		}
		String renderUrlString = renderUrlObj.toString();
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_RENDER_URL, renderUrlString);
		modelmap.addAttribute(RESPONSE, jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	
	/**
	 * Creates info (tietopyyntö) render url mainly for gatein portal, and keeps the page
	 * parameters such as page id, task type, keyword
	 * 
	 * @param authorizationId authorization id
	 * @param currentPage current page
	 * @param taskType request task type
	 * @param keyword keyword
	 * @param orderType order type
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response ResourceResponse
	 * @return Consent render url in Json format
	 */
	@ResourceMapping(value = "createTipyRenderUrl")
	public String createTipyRenderUrl(
			@RequestParam(value = "requestId") String requestId,
			@RequestParam(value = "currentPage") String currentPage,
			@RequestParam(value = "taskType") String taskType,
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "orderType") String orderType,
			ModelMap modelmap, PortletRequest request, ResourceResponse response) {

		PortletSession portletSession = request.getPortletSession();				
		String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		if (isInvalidStrongAuthentication(portletSession)) {
			return authenticationFailed(modelmap, username);
		}
		
		PortletURL renderUrlObj = response.createRenderURL();
		renderUrlObj.setParameter( ATTR_MY_ACTION, MY_ACTION_SHOW_TIPY);
		renderUrlObj.setParameter( ATTR_AUTHORIZATION_ID, requestId);
		renderUrlObj.setParameter( ATTR_CURRENT_PAGE, currentPage);
		renderUrlObj.setParameter( ATTR_TASK_TYPE, taskType);
		renderUrlObj.setParameter( ATTR_KEYWORD, keyword);
		renderUrlObj.setParameter( ATTR_ORDER_TYPE, orderType);	
		
		try {
			renderUrlObj.setWindowState(WindowState.NORMAL);
		} catch (WindowStateException e) {
			LOG.error("Create consent render url failed");
		}
		String renderUrlString = renderUrlObj.toString();
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_RENDER_URL, renderUrlString);
		modelmap.addAttribute(RESPONSE, jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	/**
	 * Creates info (tietopyyntö) render url mainly for gatein portal, and keeps the page
	 * parameters such as page id, task type, keyword
	 * 
	 * @param authorizationId authorization id
	 * @param currentPage current page
	 * @param taskType request task type
	 * @param keyword keyword
	 * @param orderType order type
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response ResourceResponse
	 * @return Consent render url in Json format
	 */
	@ResourceMapping(value = "createTipyRenderUrl")
	public String createApplicationKindergartenRenderUrl(
			@RequestParam(value = "applicationId") String applicationId,
			@RequestParam(value = "currentPage") String currentPage,
			@RequestParam(value = "taskType") String taskType,
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "orderType") String orderType,
			ModelMap modelmap, PortletRequest request, ResourceResponse response) {

		PortletSession portletSession = request.getPortletSession();				
		String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		if (isInvalidStrongAuthentication(portletSession)) {
			return authenticationFailed(modelmap, username);
		}
		
		PortletURL renderUrlObj = response.createRenderURL();
		renderUrlObj.setParameter( ATTR_MY_ACTION, MY_ACTION_SHOW_APPLICATION_KINDERGARTEN);
		renderUrlObj.setParameter( ATTR_APPLICATION_ID, applicationId);
		renderUrlObj.setParameter( ATTR_CURRENT_PAGE, currentPage);
		renderUrlObj.setParameter( ATTR_TASK_TYPE, taskType);
		renderUrlObj.setParameter( ATTR_KEYWORD, keyword);
		renderUrlObj.setParameter( ATTR_ORDER_TYPE, orderType);	
		
		try {
			renderUrlObj.setWindowState(WindowState.NORMAL);
		} catch (WindowStateException e) {
			LOG.error("Create consent render url failed");
		}
		String renderUrlString = renderUrlObj.toString();
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_RENDER_URL, renderUrlString);
		modelmap.addAttribute(RESPONSE, jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
}
