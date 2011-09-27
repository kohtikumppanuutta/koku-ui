package fi.arcusys.koku.web;

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
import fi.arcusys.koku.av.KokuAppointment;
import fi.arcusys.koku.kv.KokuRequest;
import fi.arcusys.koku.kv.Message;
import fi.arcusys.koku.kv.MessageHandle;
import fi.arcusys.koku.kv.RequestHandle;
import fi.arcusys.koku.tiva.KokuConsent;
import fi.arcusys.koku.tiva.TivaCitizenServiceHandle;
import fi.arcusys.koku.tiva.TivaEmployeeServiceHandle;
import fi.arcusys.koku.tiva.employeeservice.SuostumuspohjaShort;
import fi.arcusys.koku.tiva.tietopyynto.employee.KokuEmployeeTietopyyntoServiceHandle;
import fi.arcusys.koku.tiva.tietopyynto.model.KokuInformationRequestSummary;
import fi.arcusys.koku.tiva.warrant.citizens.KokuCitizenWarrantHandle;
import fi.arcusys.koku.tiva.warrant.employee.KokuEmployeeWarrantHandle;
import fi.arcusys.koku.tiva.warrant.model.KokuAuthorizationSummary;
import fi.arcusys.koku.tiva.warrant.model.KokuValtakirjapohja;
import fi.arcusys.koku.users.UserIdResolver;
import fi.arcusys.koku.util.PortalRole;
import static fi.arcusys.koku.util.Constants.*;

/**
 * Hanldes ajax request from portlet and returns the response with json string
 * @author Jinhua Chen
 * Jun 22, 2011
 */

@Controller("ajaxController")
@RequestMapping(value = "VIEW")
public class AjaxController extends AbstractController {
	
	private static final String TASKS = "tasks";
	
	private static final int MAX_SUGGESTION_RESULTS = 5;
	
	@Resource
	private ResourceBundleMessageSource messageSource;

	private static Logger LOG = Logger.getLogger(AjaxController.class);
	
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
		UserIdResolver resolver = new UserIdResolver();
		String userId = resolver.getUserId(username, getPortalRole(request));
		JSONObject jsonModel = getJsonModel(taskType, page, keyword, field, orderType, userId);
		modelmap.addAttribute(RESPONSE, jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
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
		MessageHandle msghandle = new MessageHandle();		
		List<Long> messageIds = new ArrayList<Long>();
		
		for(String msgId : messageList) {
			messageIds.add(Long.parseLong(msgId));
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
		MessageHandle msghandle = new MessageHandle();		
		List<Long> messageIds = new ArrayList<Long>();
		
		for(String msgId : messageList) {
			messageIds.add(Long.parseLong(msgId));
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
		UserIdResolver resolver = new UserIdResolver();
		String userId = resolver.getUserId(username, getPortalRole(request));

		PortalRole role = getPortalRole(request);
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
		UserIdResolver resolver = new UserIdResolver();
		String userId = resolver.getUserId(username, getPortalRole(request));

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
		String userId = resolver.getUserId(username, getPortalRole(request));

		
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
		
		@SuppressWarnings("rawtypes")
		List resultList = null;
		if (suggestionType.equals(SUGGESTION_CONSENT)) {
			TivaEmployeeServiceHandle tivaHandle = new TivaEmployeeServiceHandle();					
			resultList = tivaHandle.searchConsentTemplates(keyword, MAX_SUGGESTION_RESULTS);
		} else if (suggestionType.equals(SUGGESTION_WARRANT)) {
			KokuEmployeeWarrantHandle handle = new KokuEmployeeWarrantHandle();
			resultList = handle.searchWarrantTemplates(keyword, MAX_SUGGESTION_RESULTS);
		}
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_RESULT, resultList);
		modelmap.addAttribute(RESPONSE, jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	
	/**
	 * Processes tasks query and get task list from web services
	 * @param taskType type of requested task
	 * @param page the page number
	 * @param keyword keyword for filtering
	 * @param field field for filtering
	 * @param orderType order of tasks
	 * @param userId the user to which the tasks belong 
	 * @return task information in Json format
	 */
	public JSONObject getJsonModel(String taskType, int page, String keyword, String field, String orderType, String userId) {
		JSONObject jsonModel = new JSONObject();
		
		LOG.debug("Ajax call");
		
		if(userId == null) {
			jsonModel.put("loginStatus", "INVALID");
			LOG.info("No logged in user");
		} else {
			
			int numPerPage = PAGE_NUMBER;
			int totalTasksNum = 0;
			int totalPages;			
			int first = (page-1)*numPerPage + 1; // the start index of task
			int max =  page*numPerPage; // max amount of tasks
			
			if(taskType.equals(TASK_TYPE_REQUEST_VALID_EMPLOYEE)) { // for request (Pyynnöt) - employee Avoimet
				List<KokuRequest> msgs;
				RequestHandle reqHandle = new RequestHandle();
				msgs = reqHandle.getRequests(userId, "valid", "", first, max);
				totalTasksNum = reqHandle.getTotalRequestsNum(userId, "valid");
				jsonModel.put(TASKS, msgs);
			} else if(taskType.startsWith("app")) { // for appointment (Tapaamiset)
				
				if(taskType.equals(TASK_TYPE_APPOINTMENT_INBOX_CITIZEN) || taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN)) {	// Vastausta odottavat / vastatut 
					List<KokuAppointment> apps;
					AvCitizenServiceHandle handle = new AvCitizenServiceHandle();
					handle.setMessageSource(messageSource);
					apps = handle.getAppointments(userId, first, max, taskType);
					totalTasksNum = handle.getTotalAppointmentsNum(userId, taskType);
					jsonModel.put(TASKS, apps);
				}else if(taskType.equals(TASK_TYPE_APPOINTMENT_INBOX_EMPLOYEE) || taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_EMPLOYEE)) { // Avoimet / Valmiit
					List<KokuAppointment> apps;
					AvEmployeeServiceHandle handle = new AvEmployeeServiceHandle();
					handle.setMessageSource(messageSource);
					apps = handle.getAppointments(userId, first, max, taskType);
					totalTasksNum = handle.getTotalAppointmentsNum(userId, taskType);
					jsonModel.put(TASKS, apps);
				}			
				
			} else if(taskType.startsWith("cst")) { // for consent (Valtakirja / Suostumus)
				List<KokuConsent> csts = null;
				List<KokuAuthorizationSummary> summaries = null;
				if (taskType.equals(TASK_TYPE_CONSENT_ASSIGNED_CITIZEN)) { // Kansalaiselle saapuneet pyynnöt(/suostumukset) 
					TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle();
					tivaHandle.setMessageSource(messageSource);
					csts = tivaHandle.getAssignedConsents(userId, first, max);
					totalTasksNum = tivaHandle.getTotalAssignedConsents(userId);
					jsonModel.put(TASKS, csts);
				} else if(taskType.equals(TASK_TYPE_CONSENT_CITIZEN_CONSENTS)) { // Kansalaiselle vastatut pyynnöt(/suostumukset) 
					TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle();
					tivaHandle.setMessageSource(messageSource);
					csts = tivaHandle.getOwnConsents(userId, first, max);
					totalTasksNum = tivaHandle.getTotalOwnConsents(userId);
					jsonModel.put(TASKS, csts);
				} else if(taskType.equals(TASK_TYPE_CONSENT_EMPLOYEE_CONSENTS)) { // Virkailijan lähetetyt suostumus pyynnöt
					TivaEmployeeServiceHandle tivaHandle = new TivaEmployeeServiceHandle();
					tivaHandle.setMessageSource(messageSource);
					csts = tivaHandle.getConsents(userId, keyword, field, first, max);
					totalTasksNum = tivaHandle.getTotalConsents(userId, keyword, field);
					jsonModel.put(TASKS, csts);
				} else if(taskType.equals(TASK_TYPE_WARRANT_LIST_CITIZEN_CONSENTS)) {	// Virkailija: Selaa asiakkaan valtakirjoja TIVA-13
					if (keyword != null && !keyword.isEmpty()) {
						KokuEmployeeWarrantHandle warrantHandle = new KokuEmployeeWarrantHandle();
						warrantHandle.setMessageSource(messageSource);
						summaries = warrantHandle.getAuthorizationsByUserId(keyword, first, max);
						totalTasksNum = warrantHandle.getUserRecievedWarrantCount(keyword);						
					}
					jsonModel.put(TASKS, summaries);
				} else if(taskType.equals(TASK_TYPE_WARRANT_LIST_SUBJECT_CONSENTS)) {	// Virkailija: Selaa asian valtakirjoja TIVA-14
					if (keyword != null && !keyword.isEmpty()) {
						KokuEmployeeWarrantHandle warrantHandle = new KokuEmployeeWarrantHandle();
						warrantHandle.setMessageSource(messageSource);
						summaries = warrantHandle.getAuthorizationsByTemplateId(keyword, first, max);
						totalTasksNum = warrantHandle.getUserRecievedWarrantCount(keyword);
					}
					jsonModel.put(TASKS, summaries);
				} else if(taskType.equals(TASK_TYPE_WARRANT_BROWSE_RECEIEVED)) {	// Kuntalainen: Valtuuttajana TIVA-11
					KokuCitizenWarrantHandle warrantHandle = new KokuCitizenWarrantHandle();
					warrantHandle.setMessageSource(messageSource);
					summaries = warrantHandle.getReceivedAuthorizations(userId, first, max);
					totalTasksNum = warrantHandle.getTotalReceivedAuthorizations(userId);
					jsonModel.put(TASKS, summaries);
				} else if(taskType.equals(TASK_TYPE_WARRANT_BROWSE_SENT)) {	// Kuntalainen: Valtuutettuna TIVA-11
					KokuCitizenWarrantHandle warrantHandle = new KokuCitizenWarrantHandle();
					warrantHandle.setMessageSource(messageSource);
					summaries = warrantHandle.getSentWarrants(userId, first, max);
					totalTasksNum = warrantHandle.getTotalSentAuthorizations(userId);
					jsonModel.put(TASKS, summaries);
				}
				
			} else { // for message
				MessageHandle msgHandle = new MessageHandle();
				msgHandle.setMessageSource(messageSource);
				List<Message> msgs;
				msgs = msgHandle.getMessages(userId, taskType, keyword, field, orderType, first, max);			
				totalTasksNum = msgHandle.getTotalMessageNum(userId, taskType, keyword, field);
				jsonModel.put(TASKS, msgs);
			}
			
			totalPages = (totalTasksNum == 0) ? 1:(int) Math.ceil((double)totalTasksNum/numPerPage);	
			jsonModel.put(JSON_TOTAL_ITEMS, totalTasksNum);
			jsonModel.put(JSON_TOTAL_PAGES, totalPages);
			jsonModel.put(JSON_LOGIN_STATUS, "VALID");
		}		
		return jsonModel;	
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
	
}
