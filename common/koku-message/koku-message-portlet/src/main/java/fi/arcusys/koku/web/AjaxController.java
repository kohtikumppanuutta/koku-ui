package fi.arcusys.koku.web;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import fi.arcusys.koku.message.Message;
import fi.arcusys.koku.message.MessageHandle;
import fi.arcusys.koku.requestservice.KokuRequest;
import fi.arcusys.koku.requestservice.RequestHandle;
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
			MessageHandle msghandle = new MessageHandle();
			int numPerPage = MessageUtil.PAGE_NUMBER;
			int totalTasksNum;
			int totalPages;
			
			int first = (page-1)*numPerPage + 1;
			int max =  page*numPerPage;
			
			if(taskType.equals("valid_request")) {
				List<KokuRequest> msgs;
				RequestHandle reqhandle = new RequestHandle();
				msgs = reqhandle.getRequests(username, "valid", "", first, max);
				totalTasksNum = reqhandle.getTotalRequestsNum(username, "valid");
				jsonModel.put("tasks", msgs);
			} else {
				List<Message> msgs;
				msgs = msghandle.getMessages(username, taskType, keyword, field, orderType, first, max);			
				totalTasksNum = msghandle.getTotalMessageNum(username, taskType, keyword, field);
				jsonModel.put("tasks", msgs);
			}
			
			/*
			int fromIndex = (msgs.size()-page*numPerPage) > 0 ?  (msgs.size()-page*numPerPage) : 0;
			int toIndex = msgs.size() > 0 ? msgs.size()-(page-1)*numPerPage : 0;
			List<Message> subMsgs = msgs.subList(fromIndex, toIndex);
			*/
			totalPages = (totalTasksNum == 0) ? 1:(int) Math.ceil((double)totalTasksNum/numPerPage);	
			jsonModel.put("totalItems", totalTasksNum);
			jsonModel.put("totalPages", totalPages);
			//jsonModel.put("tasks", msgs);
			jsonModel.put("loginStatus", "VALID");
		}		
		
		return jsonModel;	
	}


}
