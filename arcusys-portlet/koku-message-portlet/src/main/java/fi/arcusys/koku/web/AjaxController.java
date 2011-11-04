package fi.arcusys.koku.web;

import static fi.arcusys.koku.util.Constants.*;

import java.util.ArrayList;
import java.util.Collections;
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
import fi.arcusys.koku.kv.request.citizen.CitizenRequestHandle;
import fi.arcusys.koku.kv.request.employee.EmployeeRequestHandle;
import fi.arcusys.koku.tiva.TivaCitizenServiceHandle;
import fi.arcusys.koku.tiva.TivaEmployeeServiceHandle;
import fi.arcusys.koku.tiva.tietopyynto.employee.KokuEmployeeTietopyyntoServiceHandle;
import fi.arcusys.koku.tiva.warrant.citizens.KokuCitizenWarrantHandle;
import fi.arcusys.koku.tiva.warrant.employee.KokuEmployeeWarrantHandle;
import fi.arcusys.koku.users.UserIdResolver;
import fi.arcusys.koku.util.PortalRole;

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
		String userId = null;
		try {
			UserIdResolver resolver = new UserIdResolver();
			userId = resolver.getUserId(username, getPortalRole());			
		} catch (Exception e) {
			LOG.error("Error while trying to resolve userId. See following error msg: "+ e.getMessage());
		}
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
	 * Processes tasks query and get task list from web services
	 * @param taskType type of requested task
	 * @param page the page number
	 * @param keyword keyword for filtering
	 * @param field field for filtering
	 * @param orderType order of tasks
	 * @param userUid the user to which the tasks belong 
	 * @return task information in Json format
	 */
	public JSONObject getJsonModel(String taskType, int page, String keyword, String field, String orderType, String userUid) {
		JSONObject jsonModel = new JSONObject();
		
		LOG.debug("Ajax call");
		
		if(userUid == null) {
			jsonModel.put(JSON_LOGIN_STATUS, TOKEN_STATUS_INVALID);
			LOG.info("No logged in user");
		} else {			
			int numPerPage = PAGE_NUMBER;
			int totalTasksNum = 0;
			int totalPages;
			int first = (page-1)*numPerPage + 1; // the start index of task
			int max =  page*numPerPage; // max amount of tasks
			Object tasks = Collections.EMPTY_LIST; // Returned tasks
			
			if (taskType.equals(TASK_TYPE_APPLICATION_KINDERGARTEN_BROWSE)) { // Asiointipalvelut - Selaa hakemuksia (päivähoito) - employee
				HakServiceHandle reqHandle = new HakServiceHandle();
				tasks = reqHandle.getApplicants(userUid, keyword, first, max);
				totalTasksNum = reqHandle.getTotalRequestedApplicants(userUid, keyword);
			} else if (taskType.equals(TASK_TYPE_REQUEST_VALID_EMPLOYEE)) { // for request (Pyynnöt) - employee Avoimet
				EmployeeRequestHandle reqHandle = new EmployeeRequestHandle();
				tasks = reqHandle.getRequests(userUid, "valid", "", first, max);
				totalTasksNum = reqHandle.getTotalRequestsNum(userUid, "valid");
			} else if (taskType.equals(TASK_TYPE_REQUEST_REPLIED)) { // Pyynnöt - vastatut
				CitizenRequestHandle handle = new CitizenRequestHandle();
				tasks = handle.getRepliedRequests(userUid, first, max);
				totalTasksNum = handle.getTotalRepliedRequests(userUid);
			}  else if (taskType.equals(TASK_TYPE_REQUEST_OLD)) { // Pyynnöt - vanhat
				CitizenRequestHandle handle = new CitizenRequestHandle();
				tasks = handle.getOldRequests(userUid, first, max);
				totalTasksNum = handle.getTotalOldRequests(userUid);
			} else if (taskType.equals(TASK_TYPE_REQUEST_DONE_EMPLOYEE)) { // Pyynnöt - vastatut/vanhat
				// TODO: Need proper WS service
				tasks = Collections.EMPTY_LIST;
				totalTasksNum = 0;				
			} else if (taskType.equals(TASK_TYPE_INFO_REQUEST_BROWSE_REPLIED)) { // Virkailija: Selaa vastaanotettuja tietopyyntöjä
				KokuEmployeeTietopyyntoServiceHandle handle = new KokuEmployeeTietopyyntoServiceHandle();
				handle.setMessageSource(messageSource);
				tasks = handle.getRepliedRequests(userUid, keyword, first, max);
			} else if (taskType.equals(TASK_TYPE_INFO_REQUEST_BROWSE_SENT)) { // Virkailija: Selaa lähetettyjä tietopyyntöjä
				KokuEmployeeTietopyyntoServiceHandle handle = new KokuEmployeeTietopyyntoServiceHandle();
				handle.setMessageSource(messageSource);
				tasks = handle.getSentRequests(userUid, keyword, first, max);
			} else if (taskType.equals(TASK_TYPE_INFO_REQUEST_BROWSE)) { // ADMIN: Selaa tietopyyntöjä
				KokuEmployeeTietopyyntoServiceHandle handle = new KokuEmployeeTietopyyntoServiceHandle();
				handle.setMessageSource(messageSource);
				tasks = handle.getRequests(keyword, first, max);
			} else if (taskType.equals(TASK_TYPE_APPOINTMENT_INBOX_CITIZEN)
					|| taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN_OLD)
					|| taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN)) {	// Tapaamiset - Vastausta odottavat / vastatut 
				AvCitizenServiceHandle handle = new AvCitizenServiceHandle();
				handle.setMessageSource(messageSource);
				tasks = handle.getAppointments(userUid, first, max, taskType);
				totalTasksNum = handle.getTotalAppointmentsNum(userUid, taskType);
			} else if(taskType.equals(TASK_TYPE_APPOINTMENT_INBOX_EMPLOYEE) || taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_EMPLOYEE)) { // Tapaamiset - Avoimet / Valmiit
				AvEmployeeServiceHandle handle = new AvEmployeeServiceHandle();
				handle.setMessageSource(messageSource);
				tasks = handle.getAppointments(userUid, first, max, taskType);
				totalTasksNum = handle.getTotalAppointmentsNum(userUid, taskType);
			} else if (taskType.equals(TASK_TYPE_CONSENT_ASSIGNED_CITIZEN)) { // consent (Valtakirja / Suostumus) Kansalaiselle saapuneet pyynnöt(/suostumukset) 
				TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle();
				tivaHandle.setMessageSource(messageSource);
				tasks = tivaHandle.getAssignedConsents(userUid, first, max);
				totalTasksNum = tivaHandle.getTotalAssignedConsents(userUid);
			} else if(taskType.equals(TASK_TYPE_CONSENT_CITIZEN_CONSENTS)) { // consent (Valtakirja / Suostumus) Kansalaiselle vastatut pyynnöt(/suostumukset) 
				TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle();
				tivaHandle.setMessageSource(messageSource);
				tasks = tivaHandle.getOwnConsents(userUid, first, max);
				totalTasksNum = tivaHandle.getTotalOwnConsents(userUid);
			} else if(taskType.equals(TASK_TYPE_CONSENT_CITIZEN_CONSENTS_OLD)) { // consent (Valtakirja / Suostumus) Kansalaiselle vastatut vanhentuneet pyynnöt(/suostumukset) 
				TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle();
				tivaHandle.setMessageSource(messageSource);
				tasks = tivaHandle.getOwnOldConsents(userUid, first, max);
				totalTasksNum = tivaHandle.getTotalOwnOldConsents(userUid);
			} else if(taskType.equals(TASK_TYPE_CONSENT_EMPLOYEE_CONSENTS)) { // Virkailijan lähetetyt suostumus pyynnöt
				TivaEmployeeServiceHandle tivaHandle = new TivaEmployeeServiceHandle();
				tivaHandle.setMessageSource(messageSource);
				tasks = tivaHandle.getConsents(userUid, keyword, field, first, max);
				totalTasksNum = tivaHandle.getTotalConsents(userUid, keyword, field);
			} else if(taskType.equals(TASK_TYPE_WARRANT_LIST_CITIZEN_CONSENTS)) {	// Virkailija: Selaa asiakkaan valtakirjoja TIVA-13
				if (keyword != null && !keyword.isEmpty()) {
					KokuEmployeeWarrantHandle warrantHandle = new KokuEmployeeWarrantHandle();
					warrantHandle.setMessageSource(messageSource);
					tasks = warrantHandle.getAuthorizationsByUserId(keyword, first, max);
					totalTasksNum = warrantHandle.getUserRecievedWarrantCount(keyword);						
				}
			} else if(taskType.equals(TASK_TYPE_WARRANT_LIST_SUBJECT_CONSENTS)) {	// Virkailija: Selaa asian valtakirjoja TIVA-14
				if (keyword != null && !keyword.isEmpty()) {
					KokuEmployeeWarrantHandle warrantHandle = new KokuEmployeeWarrantHandle();
					warrantHandle.setMessageSource(messageSource);
					tasks = warrantHandle.getAuthorizationsByTemplateId(keyword, first, max);
					totalTasksNum = warrantHandle.getUserRecievedWarrantCount(keyword);
				}
			} else if(taskType.equals(TASK_TYPE_WARRANT_BROWSE_RECEIEVED)) {	// Kuntalainen: Valtuuttajana TIVA-11
				KokuCitizenWarrantHandle warrantHandle = new KokuCitizenWarrantHandle();
				warrantHandle.setMessageSource(messageSource);
				tasks = warrantHandle.getReceivedAuthorizations(userUid, first, max);
				totalTasksNum = warrantHandle.getTotalReceivedAuthorizations(userUid);
			} else if(taskType.equals(TASK_TYPE_WARRANT_BROWSE_SENT)) {	// Kuntalainen: Valtuutettuna TIVA-11
				KokuCitizenWarrantHandle warrantHandle = new KokuCitizenWarrantHandle();
				warrantHandle.setMessageSource(messageSource);
				tasks = warrantHandle.getSentWarrants(userUid, first, max);
				totalTasksNum = warrantHandle.getTotalSentAuthorizations(userUid);
			} else { // for message
				MessageHandle msgHandle = new MessageHandle();
				msgHandle.setMessageSource(messageSource);
				tasks = msgHandle.getMessages(userUid, taskType, keyword, field, orderType, first, max);			
				totalTasksNum = msgHandle.getTotalMessageNum(userUid, taskType, keyword, field);
			}
			
			totalPages = (totalTasksNum == 0) ? 1:(int) Math.ceil((double)totalTasksNum/numPerPage);
			jsonModel.put(TASKS, tasks);
			jsonModel.put(JSON_TOTAL_ITEMS, totalTasksNum);
			jsonModel.put(JSON_TOTAL_PAGES, totalPages);
			jsonModel.put(JSON_LOGIN_STATUS, TOKEN_STATUS_VALID);
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
