package fi.arcusys.koku.web;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
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
import fi.arcusys.koku.util.MessageUtil;

/**
 * Hanldes ajax request from portlet and returns the response with json string
 * @author Jinhua Chen
 * Jun 22, 2011
 */

@Controller("ajaxController")
@RequestMapping(value = "VIEW")
public class AjaxController {

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
		TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle();		
		
		for(String consentId : messageList) {
			tivaHandle.revokeOwnConsent(consentId);
		}
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put("result", MessageUtil.RESPONSE_OK);
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
			
			if(taskType.equals("req_valid")) { // for request
				List<KokuRequest> msgs;
				RequestHandle reqHandle = new RequestHandle();
				msgs = reqHandle.getRequests(username, "valid", "", first, max);
				totalTasksNum = reqHandle.getTotalRequestsNum(username, "valid");
				jsonModel.put("tasks", msgs);
			} else if(taskType.startsWith("app")) { // for appointment
				
				
				if(taskType.equals("app_inbox_citizen") || taskType.equals("app_response_citizen")) {
					List<KokuAppointment> apps;
					AvCitizenServiceHandle handle = new AvCitizenServiceHandle();
					apps = handle.getAppointments(username, first, max, taskType);
					totalTasksNum = handle.getTotalAppointmentsNum(username, taskType);
					jsonModel.put("tasks", apps);
				}else if(taskType.equals("app_inbox_employee") || taskType.equals("app_response_employee")) {
					List<KokuAppointment> apps;
					AvEmployeeServiceHandle handle = new AvEmployeeServiceHandle();
					apps = handle.getAppointments(username, first, max, taskType);
					totalTasksNum = handle.getTotalAppointmentsNum(username, taskType);
					jsonModel.put("tasks", apps);
				}			
				
			} else if(taskType.startsWith("cst")) { // for consent
				if(taskType.equals("cst_assigned_citizen")) {
					List<KokuConsent> csts;
					TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle();
					csts = tivaHandle.getAssignedConsents(username, first, max);
					totalTasksNum = tivaHandle.getTotalAssignedConsents(username);
					jsonModel.put("tasks", csts);
				}else if(taskType.equals("cst_own_citizen")) {
					List<KokuConsent> csts;
					TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle();
					csts = tivaHandle.getOwnConsents(username, first, max);
					totalTasksNum = tivaHandle.getTotalOwnConsents(username);
					jsonModel.put("tasks", csts);
				}else if(taskType.equals("cst_own_employee")) {
					List<KokuConsent> csts;
					TivaEmployeeServiceHandle tivaHandle = new TivaEmployeeServiceHandle();
					csts = tivaHandle.getConsents(username, keyword, first, max);
					totalTasksNum = tivaHandle.getTotalConsents(username, keyword);
					jsonModel.put("tasks", csts);
				}	
				
			} else { // for message
				MessageHandle msgHandle = new MessageHandle();
				List<Message> msgs;
				msgs = msgHandle.getMessages(username, taskType, keyword, field, orderType, first, max);			
				totalTasksNum = msgHandle.getTotalMessageNum(username, taskType, keyword, field);
				jsonModel.put("tasks", msgs);
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
			renderUrlObj.setWindowState(WindowState.MAXIMIZED);
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
			renderUrlObj.setWindowState(WindowState.MAXIMIZED);
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
			ModelMap modelmap, PortletRequest request, ResourceResponse response) {

		PortletURL renderUrlObj = response.createRenderURL();
		renderUrlObj.setParameter( "myaction", "showAppointment");
		renderUrlObj.setParameter( "appointmentId", appointmentId);
		renderUrlObj.setParameter( "currentPage", currentPage);
		renderUrlObj.setParameter( "taskType", taskType);
		renderUrlObj.setParameter( "keyword", keyword);
		renderUrlObj.setParameter( "orderType", orderType);	
		try {
			renderUrlObj.setWindowState(WindowState.MAXIMIZED);
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
			renderUrlObj.setWindowState(WindowState.MAXIMIZED);
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
