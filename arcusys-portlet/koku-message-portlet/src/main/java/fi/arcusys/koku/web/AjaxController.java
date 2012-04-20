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
import static fi.arcusys.koku.util.Constants.ATTR_USER_ID;
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
import static fi.arcusys.koku.util.Constants.RESPONSE_FAIL;
import static fi.arcusys.koku.util.Constants.RESPONSE_OK;
import static fi.arcusys.koku.util.Constants.SUGGESTION_APPLICATION_KINDERGARTEN;
import static fi.arcusys.koku.util.Constants.SUGGESTION_CONSENT;
import static fi.arcusys.koku.util.Constants.SUGGESTION_NO_TYPE;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.hak.model.HakServiceHandle;
import fi.arcusys.koku.tiva.TivaEmployeeServiceHandle;
import fi.arcusys.koku.tiva.warrant.employee.KokuEmployeeWarrantHandle;
import fi.arcusys.koku.users.KokuUser;
import fi.arcusys.koku.users.KokuUserService;
import fi.arcusys.koku.users.UserIdResolver;
import fi.arcusys.koku.util.Properties;
import fi.arcusys.koku.web.util.KokuActionProcess;
import fi.arcusys.koku.web.util.KokuTaskQueryProcess;
import fi.arcusys.koku.web.util.exception.KokuActionProcessException;
import fi.arcusys.koku.web.util.impl.KokuActionProcessCitizenImpl;
import fi.arcusys.koku.web.util.impl.KokuActionProcessDummyImpl;
import fi.arcusys.koku.web.util.impl.KokuActionProcessEmployeeImpl;
import fi.arcusys.koku.web.util.impl.QueryProcessCitizenImpl;
import fi.arcusys.koku.web.util.impl.QueryProcessDummyImpl;
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
	private static final Logger LOG = LoggerFactory.getLogger(AjaxController.class);
		
	@Resource
	private ResourceBundleMessageSource messageSource;

	
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
		
		final long start = System.nanoTime();
		final PortletSession portletSession = request.getPortletSession();		
		final String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		String userId = (String) portletSession.getAttribute(ATTR_USER_ID);
		registerUserToWS(portletSession);
		
		if (userId == null) {
			try {
				long startUser = System.nanoTime();
				UserIdResolver resolver = new UserIdResolver();
				userId = resolver.getUserId(username, getPortalRole());
				portletSession.setAttribute(ATTR_USER_ID, userId);
				LOG.info("UserResolver took "+((System.nanoTime()-startUser)/1000/1000) + "ms");
			} catch (KokuServiceException e) {
				LOG.error("Failed to get UserUid username: '"+username+"' portalRole: '"+getPortalRole()+"'", e);
			} catch (Exception e) {
				LOG.error("Error while trying to resolve userId. See following error msg: "+ e);
			}
		}
		
		KokuTaskQueryProcess query = null;		
		if (Properties.IS_KUNPO_PORTAL) {
			query = new QueryProcessCitizenImpl(messageSource);
		} else if (Properties.IS_LOORA_PORTAL) {
			query = new QueryProcessEmployeeImpl(messageSource);
		} else {
			query = new QueryProcessDummyImpl(messageSource);
			LOG.error("PortalMode unknown! Only kunpo/loora portal modes are supported. Please check that properties file is properly configured.");
		}
		final JSONObject jsonModel = query.getJsonModel(taskType, page, keyword, field, orderType, userId);
		modelmap.addAttribute(RESPONSE, jsonModel);
		LOG.info("getTasks  - "+((System.nanoTime()-start)/1000/1000) + "ms");
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
		
		try {
	    	if (Properties.IS_KUNPO_PORTAL) {
	    		// Kunpo
	    		user = userService.loginKunpo(username, userInfo.getPic());
	    	} else if (Properties.IS_LOORA_PORTAL) {
	    		// Loora
	    		user = userService.loginLoora(username, userInfo.getPic());
	    	} else {
	    		LOG.error("Can't register user to WS! Portlet doesn't have information if portal is Citizen/Employee mode! username: '"+username+"'");
	    		return;
	    	}
    	} catch (KokuServiceException kse) {
    		LOG.error("Failed register user to WS.",kse);    		
    	}
    	
    	if (user == null) {
    		// TODO: Remove if statement when Loora side implementation is ready, if ever..
    		if (Properties.IS_KUNPO_PORTAL) {
    			LOG.error("For some reason userService didn't find KokuUser. Username: '"+username+"' SSN: '"+userInfo.getPic()+"' ");
    		}
    		user = new KokuUser();
    	}
    	portletSession.setAttribute(ATTR_KOKU_USER, user);	    	
	}
	
	
	/**
	 * Archive old messages (more than 3 month old)
	 * 
	 * @param folderType folder which messages should archive
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response PortletResponse
	 * @return action response 'OK' or 'FAIL'
	 */
	@ResourceMapping(value = "archiveMessageOld")
	public String doArchiveOld(@RequestParam(value = "folderType") String folderType,
			ModelMap modelmap, PortletRequest request, PortletResponse response) {
		
		final PortletSession portletSession = request.getPortletSession();		
		final String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		final JSONObject jsonModel = new JSONObject();
		String userId = (String) portletSession.getAttribute(ATTR_USER_ID);
		
		try {
			if (userId == null) {
				UserIdResolver resolver = new UserIdResolver();
				resolver.getUserId(username, getPortalRole());				
			}
			KokuActionProcess actionProcess = null;
			switch (getPortalRole()) {
				case CITIZEN: actionProcess = new KokuActionProcessCitizenImpl(userId); break;
				case EMPLOYEE: actionProcess = new KokuActionProcessEmployeeImpl(userId); break;
				default: actionProcess = new KokuActionProcessDummyImpl(null); break;
			}		
			actionProcess.archiveOldMessages(folderType);
			jsonModel.put(JSON_RESULT, RESPONSE_OK);
		} catch (KokuServiceException e) {
			LOG.error("Failed to get UserUid username: '"+username+"' portalRole: '"+getPortalRole()+"'", e);
		} catch (KokuActionProcessException kape) {
			LOG.error("Failed to archive old messages. Username: '"+ username+"'", kape);
			jsonModel.put(JSON_RESULT, RESPONSE_FAIL);
		}
		modelmap.addAttribute(RESPONSE, jsonModel);
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	/**
	 * Archives the messages
	 * 
	 * @param messageList a list of message ids to be archived
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response PortletResponse
	 * @return action response 'OK' or 'FAIL'
	 */
	@ResourceMapping(value = "archiveMessage")
	public String doArchive(@RequestParam(value = "messageList[]") String[] messageList,
			ModelMap modelmap, PortletRequest request, PortletResponse response) {
		
		final PortletSession portletSession = request.getPortletSession();		
		final String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		final JSONObject jsonModel = new JSONObject();
		String userId = (String) portletSession.getAttribute(ATTR_USER_ID);		
		try {
			if (userId == null) {
				UserIdResolver resolver = new UserIdResolver();
				userId = resolver.getUserId(username, getPortalRole());						
			}
			KokuActionProcess actionProcess = null;
			switch (getPortalRole()) {
				case CITIZEN: actionProcess = new KokuActionProcessCitizenImpl(userId); break;
				case EMPLOYEE: actionProcess = new KokuActionProcessEmployeeImpl(userId); break;
				default: actionProcess = new KokuActionProcessDummyImpl(null); break;
			}		
			actionProcess.archiveMessages(messageList);
			jsonModel.put(JSON_RESULT, RESPONSE_OK);
		} catch (KokuServiceException e) {
			LOG.error("Failed to get UserUid username: '"+username+"' portalRole: '"+getPortalRole()+"'", e);
		} catch (KokuActionProcessException kape) {
			LOG.error("Failed to archive message(s). Username: '"+ username+"'", kape);
			jsonModel.put(JSON_RESULT, RESPONSE_FAIL);
		}
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
		
		final PortletSession portletSession = request.getPortletSession();		
		final String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		final JSONObject jsonModel = new JSONObject();		
		String userId = (String) portletSession.getAttribute(ATTR_USER_ID);

		try {
			if (userId == null) {
				UserIdResolver resolver = new UserIdResolver();
				userId = resolver.getUserId(username, getPortalRole());				
			}
			KokuActionProcess actionProcess = null;
			switch (getPortalRole()) {
				case CITIZEN: actionProcess = new KokuActionProcessCitizenImpl(userId); break;
				case EMPLOYEE: actionProcess = new KokuActionProcessEmployeeImpl(userId); break;
				default: actionProcess = new KokuActionProcessDummyImpl(null); break;
			}		
			actionProcess.deleteMessages(messageList);
			jsonModel.put(JSON_RESULT, RESPONSE_OK);
		} catch (KokuServiceException e) {
			LOG.error("Failed to get UserUid username: '"+username+"' portalRole: '"+getPortalRole()+"'", e);
		} catch (KokuActionProcessException kape) {
			LOG.error("Failed to delete message. Username: '"+ username+"'", kape);
			jsonModel.put(JSON_RESULT, RESPONSE_FAIL);
		}
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
		
		final PortletSession portletSession = request.getPortletSession();
		final String username = (String) portletSession.getAttribute(ATTR_USERNAME);		
		final JSONObject jsonModel = new JSONObject();
		String userId = (String) portletSession.getAttribute(ATTR_USER_ID);
		try {
			if (userId == null) {
				UserIdResolver resolver = new UserIdResolver();
				userId = resolver.getUserId(username, getPortalRole());				
			}
			KokuActionProcess actionProcess = null;
			switch (getPortalRole()) {
				case CITIZEN: actionProcess = new KokuActionProcessCitizenImpl(userId); break;
				case EMPLOYEE: actionProcess = new KokuActionProcessEmployeeImpl(userId); break;
				default: actionProcess = new KokuActionProcessDummyImpl(null); break;
			}
			actionProcess.revokeConsents(messageList);
			jsonModel.put(JSON_RESULT, RESPONSE_OK);
		} catch (KokuServiceException e) {
			LOG.error("Failed to get UserUid username: '"+username+"' portalRole: '"+getPortalRole()+"'", e);
		} catch (KokuActionProcessException kape) {
			LOG.error("Failed to revoke consent. Username: '"+ username+"'", kape);
			jsonModel.put(JSON_RESULT, RESPONSE_FAIL);
		}
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
		final PortletSession portletSession = request.getPortletSession();				
		final String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		final JSONObject jsonModel = new JSONObject();		
		String userId = (String) portletSession.getAttribute(ATTR_USER_ID);
		try {
			if (userId == null) {
				UserIdResolver resolver = new UserIdResolver();
				userId = resolver.getUserId(username, getPortalRole());				
			}
			KokuActionProcess actionProcess = null;
			switch (getPortalRole()) {
				case CITIZEN: actionProcess = new KokuActionProcessCitizenImpl(userId); break;
				case EMPLOYEE: actionProcess = new KokuActionProcessEmployeeImpl(userId); break;
				default: actionProcess = new KokuActionProcessDummyImpl(null); break;
			}
			actionProcess.revokeWarrants(messageList, comment);
			jsonModel.put(JSON_RESULT, RESPONSE_OK);
		} catch (KokuServiceException e) {
			LOG.error("Failed to get UserUid username: '"+username+"' portalRole: '"+getPortalRole()+"'", e);
		} catch (KokuActionProcessException kape) {
			LOG.error("Failed to revoke warrant. Username: '"+ username+"'", kape);
			jsonModel.put(JSON_RESULT, RESPONSE_FAIL);
		}
			
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
		
		
		final PortletSession portletSession = request.getPortletSession();				
		final String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		final JSONObject jsonModel = new JSONObject();
		String userId = (String) portletSession.getAttribute(ATTR_USER_ID);
		
		KokuActionProcess actionProcess = null;
		if (taskType == null || taskType.isEmpty()) {
			jsonModel.put(JSON_RESULT, RESPONSE_FAIL);
		} else {			
			try {
				if (userId == null) {
					final UserIdResolver resolver = new UserIdResolver();
					userId = resolver.getUserId(username, getPortalRole());					
				}
				if (taskType.endsWith("citizen")) {
					actionProcess = new KokuActionProcessCitizenImpl(userId);
				} else if (taskType.endsWith("employee")) {
					actionProcess = new KokuActionProcessEmployeeImpl(null);
				} else {
					actionProcess = new KokuActionProcessDummyImpl(null);
				}
				actionProcess.cancelAppointments(messageList, targetPersons, comment);
				jsonModel.put(JSON_RESULT, RESPONSE_OK);
			} catch (KokuServiceException e) {
				LOG.error("Failed to get UserUid username: '"+username+"' portalRole: '"+getPortalRole()+"'", e);
			} catch (KokuActionProcessException kape) {
				LOG.error("Failed to cancelAppointment", kape);
				jsonModel.put(JSON_RESULT, RESPONSE_FAIL);
			}
		}
		
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
		
		final PortletSession portletSession = request.getPortletSession();				
		final String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		final JSONObject jsonModel = new JSONObject();
		
		@SuppressWarnings("rawtypes")
		List resultList = null;
		try {
			if (suggestionType.equals(SUGGESTION_CONSENT)) {
				TivaEmployeeServiceHandle tivaHandle = new TivaEmployeeServiceHandle();					
				resultList = tivaHandle.searchConsentTemplates(keyword, MAX_SUGGESTION_RESULTS);
			} else if (suggestionType.equals(SUGGESTION_WARRANT)) {
				KokuEmployeeWarrantHandle handle = new KokuEmployeeWarrantHandle();
				resultList = handle.searchWarrantTemplates(keyword, MAX_SUGGESTION_RESULTS);
			} else if (suggestionType.equals(SUGGESTION_APPLICATION_KINDERGARTEN)) {
				HakServiceHandle handle = new HakServiceHandle();
				resultList = handle.searchKindergartenByName(keyword, MAX_SUGGESTION_RESULTS);
			} else if (suggestionType.equals(SUGGESTION_NO_TYPE)) {
				resultList = new ArrayList<String>();
				LOG.warn("No sugguestion type! Possibly bug in system. User: "+username+" Keyword: "+keyword);
			}
		} catch (KokuServiceException kse) {
			LOG.error("Failed to get suggestion. keyword: '"+keyword+"' suggestionType: '"+suggestionType+"'", kse);
			resultList = new ArrayList<String>();
		}
		
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
			@RequestParam(value = "targetPerson", required=false) String targetPerson,
			ModelMap modelmap, PortletRequest request, ResourceResponse response) {
		
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
		
		PortletURL renderUrlObj = response.createRenderURL();
		renderUrlObj.setParameter( ATTR_MY_ACTION, MY_ACTION_SHOW_TIPY);
		renderUrlObj.setParameter( ATTR_REQUEST_ID, requestId);
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
	@ResourceMapping(value = "createApplicationKindergartenRenderUrl")
	public String createApplicationKindergartenRenderUrl(
			@RequestParam(value = "applicationId") String applicationId,
			@RequestParam(value = "currentPage") String currentPage,
			@RequestParam(value = "taskType") String taskType,
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "orderType") String orderType,
			ModelMap modelmap, PortletRequest request, ResourceResponse response) {
		
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
