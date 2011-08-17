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

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import fi.arcusys.koku.appointment.AppointmentHandle;
import fi.arcusys.koku.appointment.KokuAppointment;
import fi.arcusys.koku.message.Message;
import fi.arcusys.koku.message.MessageHandle;
import fi.arcusys.koku.request.KokuRequest;
import fi.arcusys.koku.request.RequestHandle;
import fi.arcusys.koku.tiva.CitizenConsent;
import fi.arcusys.koku.tiva.TivaCitizenServiceHandle;
import fi.arcusys.koku.util.MessageUtil;

/**
 * Hanlde ajax request and return the response with json string
 * @author Jinhua Chen
 *
 */

@Controller("ajaxController")
@RequestMapping(value = "VIEW")
public class AjaxController {

	@ResourceMapping(value = "getTask")
	public String showAjax(@RequestParam(value = "page") int page,
			@RequestParam(value = "taskType") String taskType,
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "field") String field,
			@RequestParam(value = "orderType") String orderType,
			ModelMap modelmap, PortletRequest request, PortletResponse response) {
		PortletSession portletSession = request.getPortletSession();				
		String username = (String) portletSession.getAttribute("USER_username");
		System.out.println("show ajax for the user " + username );	
		JSONObject jsonModel = getJsonModel(taskType, page, keyword, field, orderType, username);
		modelmap.addAttribute("response", jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
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
	 * Process task query and get task list from intalio server
	 * @param taskType
	 * @param page
	 * @param keyword
	 * @param orderType
	 * @return task information in Json format
	 */
	public JSONObject getJsonModel(String taskType, int page, String keyword, String field, String orderType, String username) {
		JSONObject jsonModel = new JSONObject();
		
		if(username == null) {
			jsonModel.put("loginStatus", "INVALID");
			System.out.println("Invalid login!");
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
				List<KokuAppointment> apps;
				AppointmentHandle appHandle = new AppointmentHandle();
				apps = appHandle.getAppointments(username, first, max, taskType);
				totalTasksNum = appHandle.getTotalAppointmentsNum(username, taskType);
				jsonModel.put("tasks", apps);				
				
			} else if(taskType.startsWith("cst")) { // for consent
				if(taskType.equals("cst_assigned_citizen")) {
					List<CitizenConsent> csts;
					TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle();
					csts = tivaHandle.getAssignedConsents(username, first, max);
					totalTasksNum = tivaHandle.getTotalAssignedConsents(username);
					jsonModel.put("tasks", csts);
				}else if(taskType.equals("cst_own_citizen")) {
					List<CitizenConsent> csts;
					TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle();
					csts = tivaHandle.getOwnConsents(username, first, max);
					totalTasksNum = tivaHandle.getTotalOwnConsents(username);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String renderUrlString = renderUrlObj.toString();
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put("renderUrl", renderUrlString);
		modelmap.addAttribute("response", jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String renderUrlString = renderUrlObj.toString();
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put("renderUrl", renderUrlString);
		modelmap.addAttribute("response", jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String renderUrlString = renderUrlObj.toString();
		
		JSONObject jsonModel = new JSONObject();
		jsonModel.put("renderUrl", renderUrlString);
		modelmap.addAttribute("response", jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
}
