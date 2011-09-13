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

import fi.arcusys.koku.AbstractHandle;
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
import fi.arcusys.koku.util.MessageUtil;

/**
 * Hanldes ajax request from portlet and returns the response with json string
 * @author Jinhua Chen
 * Jun 22, 2011
 */

@Controller("ajaxController")
@RequestMapping(value = "VIEW")
public class AjaxController {
	
	public static final String CONSENT_BROWSE_CUSTOMER_CONSENTS 		= "cst_browse_customer_consents";
	public static final String CONSENT_BROWSE_OWN_CONSENTS_EMPLOYEE 	= "cst_own_employee";
	public static final String CONSENT_BROWSE_OWN_CONSENTS_CITIZEN	 	= "cst_own_citizen";
	public static final String CONSENT_ASSIGNED_CITIZEN					= "cst_assigned_citizen";
	
	public static final String APPOINTMENT_RESPONSE_CITIZEN 			= "app_response_citizen";
	public static final String APPOINTMENT_RESPONSE_EMPLOYEE 			= "app_response_employee";
	public static final String APPOINTMENT_INBOX_CITIZEN 				= "app_inbox_citizen";
	public static final String APPOINTMENT_INBOX_EMPLOYEE 				= "app_inbox_employee";
	
	public static final String REQUEST_VALID_EMPLOYEE					= "req_valid";

	
	private static final String TASKS = "tasks";
	
	@Resource
	private ResourceBundleMessageSource messageSource;

	private Logger logger = Logger.getLogger(AjaxController.class);
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
		String username = (String) portletSession.getAttribute("USER_username");
		JSONObject jsonModel = getJsonModel(taskType, page, keyword, field, orderType, username);
		modelmap.addAttribute("response", jsonModel);
		
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
		jsonModel.put("result", result);
		modelmap.addAttribute("response", jsonModel);
		
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
		jsonModel.put("result", result);
		modelmap.addAttribute("response", jsonModel);
		
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
		String username = (String) portletSession.getAttribute("USER_username");
		TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle(username);		
		
		for(String consentId : messageList) {
			tivaHandle.revokeOwnConsent(consentId);
		}
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put("result", MessageUtil.RESPONSE_OK);
		modelmap.addAttribute("response", jsonModel);
		
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
		
		if(taskType.endsWith("citizen")) {
			PortletSession portletSession = request.getPortletSession();				
			String username = (String) portletSession.getAttribute("USER_username");
			AvCitizenServiceHandle handle = new AvCitizenServiceHandle(username);
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
		jsonModel.put("result", MessageUtil.RESPONSE_OK);
		modelmap.addAttribute("response", jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	@ResourceMapping(value = "getSuggestion")
	public String getSuggestion(@RequestParam(value = "keyword") String keyword,
			ModelMap modelmap, PortletRequest request, PortletResponse response) {
		
		TivaEmployeeServiceHandle tivaHandle = new TivaEmployeeServiceHandle();		
		
		List<SuostumuspohjaShort> consentTemplates = tivaHandle.searchConsentTemplates(keyword, 5);
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put("result", consentTemplates);
		modelmap.addAttribute("response", jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	/**
	 * Processes tasks query and get task list from web services
	 * @param taskType type of requested task
	 * @param page the page number
	 * @param keyword keyword for filtering
	 * @param field field for filtering
	 * @param orderType order of tasks
	 * @param username the user to which the tasks belong 
	 * @return task information in Json format
	 */
	public JSONObject getJsonModel(String taskType, int page, String keyword, String field, String orderType, String username) {
		JSONObject jsonModel = new JSONObject();
		
		if(username == null) {
			jsonModel.put("loginStatus", "INVALID");
			logger.info("No logged in user");
		}else {
			
			int numPerPage = MessageUtil.PAGE_NUMBER;
			int totalTasksNum = 0;
			int totalPages;
			
			int first = (page-1)*numPerPage + 1; // the start index of task
			int max =  page*numPerPage; // max amount of tasks
			
			if(taskType.equals(REQUEST_VALID_EMPLOYEE)) { // for request (Pyynnöt) - employee Avoimet
				List<KokuRequest> msgs;
				RequestHandle reqHandle = new RequestHandle();
				msgs = reqHandle.getRequests(username, "valid", "", first, max);
				totalTasksNum = reqHandle.getTotalRequestsNum(username, "valid");
				jsonModel.put(TASKS, msgs);
			} else if(taskType.startsWith("app")) { // for appointment (Tapaamiset)
				
				if(taskType.equals(APPOINTMENT_INBOX_CITIZEN) || taskType.equals(APPOINTMENT_RESPONSE_CITIZEN)) {	// Vastausta odottavat / vastatut 
					List<KokuAppointment> apps;
					AvCitizenServiceHandle handle = new AvCitizenServiceHandle();
					handle.setMessageSource(messageSource);
					apps = handle.getAppointments(username, first, max, taskType);
					totalTasksNum = handle.getTotalAppointmentsNum(username, taskType);
					jsonModel.put(TASKS, apps);
				}else if(taskType.equals(APPOINTMENT_INBOX_EMPLOYEE) || taskType.equals(APPOINTMENT_RESPONSE_EMPLOYEE)) { // Avoimet / Valmiit
					List<KokuAppointment> apps;
					AvEmployeeServiceHandle handle = new AvEmployeeServiceHandle();
					handle.setMessageSource(messageSource);
					apps = handle.getAppointments(username, first, max, taskType);
					totalTasksNum = handle.getTotalAppointmentsNum(username, taskType);
					jsonModel.put(TASKS, apps);
				}			
				
			} else if(taskType.startsWith("cst")) { // for consent (Valtakirja / Suostumus)
				List<KokuConsent> csts = null;
				if (taskType.equals(CONSENT_ASSIGNED_CITIZEN)) { // Kansalaiselle saapuneet pyynnöt(/suostumukset) 
					TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle();
					tivaHandle.setMessageSource(messageSource);
					csts = tivaHandle.getAssignedConsents(username, first, max);
					totalTasksNum = tivaHandle.getTotalAssignedConsents(username);
					jsonModel.put(TASKS, csts);
				} else if(taskType.equals(CONSENT_BROWSE_OWN_CONSENTS_CITIZEN)) { // Kansalaiselle vastatut pyynnöt(/suostumukset) 
					TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle();
					tivaHandle.setMessageSource(messageSource);
					csts = tivaHandle.getOwnConsents(username, first, max);
					totalTasksNum = tivaHandle.getTotalOwnConsents(username);
					jsonModel.put(TASKS, csts);
				} else if(taskType.equals(CONSENT_BROWSE_OWN_CONSENTS_EMPLOYEE)) { // Virkailijan lähetetyt suostumus pyynnöt
					TivaEmployeeServiceHandle tivaHandle = new TivaEmployeeServiceHandle();
					tivaHandle.setMessageSource(messageSource);
					csts = tivaHandle.getConsents(username, keyword, field, first, max);
					totalTasksNum = tivaHandle.getTotalConsents(username, keyword, field);
					jsonModel.put(TASKS, csts);
				} else if(taskType.equals(CONSENT_BROWSE_CUSTOMER_CONSENTS)) {	// Selaa käyttäjän x suostumuksia TIVA-13
					TivaEmployeeServiceHandle tivaHandle = new TivaEmployeeServiceHandle();
					tivaHandle.setMessageSource(messageSource);
					// FIXME: This should change when we get proper methods from WS
					csts = tivaHandle.getConsents(username, keyword, field, first, max);
					totalTasksNum = tivaHandle.getTotalConsents(username, keyword, field);
					jsonModel.put(TASKS, csts);
				}
				
			} else { // for message
				MessageHandle msgHandle = new MessageHandle();
				msgHandle.setMessageSource(messageSource);
				List<Message> msgs;
				msgs = msgHandle.getMessages(username, taskType, keyword, field, orderType, first, max);			
				totalTasksNum = msgHandle.getTotalMessageNum(username, taskType, keyword, field);
				jsonModel.put(TASKS, msgs);
			}
			
			totalPages = (totalTasksNum == 0) ? 1:(int) Math.ceil((double)totalTasksNum/numPerPage);	
			jsonModel.put("totalItems", totalTasksNum);
			jsonModel.put("totalPages", totalPages);
			jsonModel.put("loginStatus", "VALID");
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
		renderUrlObj.setParameter( "myaction", "showMessage");
		renderUrlObj.setParameter( "messageId", messageId);
		renderUrlObj.setParameter( "currentPage", currentPage);
		renderUrlObj.setParameter( "taskType", taskType);
		renderUrlObj.setParameter( "keyword", keyword);
		renderUrlObj.setParameter( "orderType", orderType);
		try {
			renderUrlObj.setWindowState(WindowState.NORMAL);
		} catch (WindowStateException e) {
			logger.error("Create message render url failed");
		}
		String renderUrlString = renderUrlObj.toString();
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put("renderUrl", renderUrlString);
		modelmap.addAttribute("response", jsonModel);
		
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
		renderUrlObj.setParameter( "myaction", "showRequest");
		renderUrlObj.setParameter( "requestId", requestId);
		renderUrlObj.setParameter( "currentPage", currentPage);
		renderUrlObj.setParameter( "taskType", taskType);
		renderUrlObj.setParameter( "keyword", keyword);
		renderUrlObj.setParameter( "orderType", orderType);	
		try {
			renderUrlObj.setWindowState(WindowState.NORMAL);
		} catch (WindowStateException e) {
			logger.error("Create request render url failed");
		}
		String renderUrlString = renderUrlObj.toString();
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put("renderUrl", renderUrlString);
		modelmap.addAttribute("response", jsonModel);
		
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
		renderUrlObj.setParameter( "myaction", "showAppointment");
		renderUrlObj.setParameter( "appointmentId", appointmentId);
		renderUrlObj.setParameter( "currentPage", currentPage);
		renderUrlObj.setParameter( "taskType", taskType);
		renderUrlObj.setParameter( "keyword", keyword);
		renderUrlObj.setParameter( "orderType", orderType);	
		renderUrlObj.setParameter( "targetPerson", targetPerson);
		try {
			renderUrlObj.setWindowState(WindowState.NORMAL);
		} catch (WindowStateException e) {
			logger.error("Create appointment render url failed");
		}
		String renderUrlString = renderUrlObj.toString();
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put("renderUrl", renderUrlString);
		modelmap.addAttribute("response", jsonModel);
		
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
		renderUrlObj.setParameter( "myaction", "showConsent");
		renderUrlObj.setParameter( "consentId", consentId);
		renderUrlObj.setParameter( "currentPage", currentPage);
		renderUrlObj.setParameter( "taskType", taskType);
		renderUrlObj.setParameter( "keyword", keyword);
		renderUrlObj.setParameter( "orderType", orderType);	
		try {
			renderUrlObj.setWindowState(WindowState.NORMAL);
		} catch (WindowStateException e) {
			logger.error("Create consent render url failed");
		}
		String renderUrlString = renderUrlObj.toString();
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put("renderUrl", renderUrlString);
		modelmap.addAttribute("response", jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	
}
