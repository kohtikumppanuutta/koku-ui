package fi.koku.taskmanager.controller;

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

import fi.koku.taskmanager.model.Task;
import fi.koku.taskmanager.model.TaskHandle;
import fi.koku.taskmanager.util.TaskUtil;

/**
 * Handles ajax request from web and returns the data with json string
 * @author Jinhua Chen
 * May 11, 2011
 */
@Controller("ajaxController")
@RequestMapping(value = "VIEW")
public class AjaxController {

	/**
	 * Shows ajax for retrieving intalio tasks
	 * @param page page id
	 * @param taskTypeStr intalio task type
	 * @param keyword keyword for searching/filtering
	 * @param orderType order type of task
	 * @param modelmap
	 * @param request
	 * @param response
	 * @return ajax view with intalio tasks and related information in json format
	 */
	@ResourceMapping(value = "getTask")
	public String showAjax(@RequestParam(value = "page") int page,
			@RequestParam(value = "taskType") String taskTypeStr,
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "orderType") String orderType,
			ModelMap modelmap, PortletRequest request, PortletResponse response) {
		PortletSession portletSession = request.getPortletSession();				
		String token = (String) portletSession.getAttribute("USER_token");
		String username = (String) portletSession.getAttribute("USER_username");
		System.out.println("show ajax for the user " + username );
		int taskType = getTaskType(taskTypeStr);	
		JSONObject jsonModel = getJsonModel(taskType, page, keyword, orderType, token, username);
		modelmap.addAttribute("response", jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	/**
	 * Gets the task state
	 * @param taskId intalio task id
	 * @param modelmap
	 * @param request
	 * @param response
	 * @return ajax view with task state in json format
	 */
	@ResourceMapping(value = "getState")
	public String getTaskState(@RequestParam(value = "taskId") String taskId,
			ModelMap modelmap, PortletRequest request, PortletResponse response) {
		PortletSession portletSession = request.getPortletSession();				
		String token = (String) portletSession.getAttribute("USER_token");
		String username = (String) portletSession.getAttribute("USER_username");
		System.out.println("show ajax get task state");	
		JSONObject jsonModel = new JSONObject();
		
		if(token == null) {
			jsonModel.put("tokenStatus", "INVALID");
			System.out.println("Intalio token is invalid!");
		}else {
			TaskHandle taskhandle = new TaskHandle(token, username);
			String taskState = taskhandle.getTaskStatus(taskId);
			jsonModel.put("taskState", taskState);
		}
		
		modelmap.addAttribute("response", jsonModel);		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	/**
	 * Processes task query and gets task list
	 * @param taskType task type
	 * @param page page id
	 * @param keyword keyword for searching/filtering
	 * @param orderType order type of tasks
	 * @param token user participant token
	 * @param username user name
	 * @return task information in Json format
	 */
	public JSONObject getJsonModel(int taskType, int page, String keyword, String orderType, String token, String username) {
		JSONObject jsonModel = new JSONObject();
		
		if(token == null) {
			jsonModel.put("tokenStatus", "INVALID");
			System.out.println("Intalio token is invalid!");
		}else {
			TaskHandle taskhandle = new TaskHandle(token, username);
			int numPerPage = TaskUtil.PAGE_NUMBER;
			int totalTasksNum;
			int totalPages;
			List<Task> tasks;
			String first = String.valueOf((page-1)*numPerPage);
			String max =  String.valueOf(numPerPage);
			tasks = taskhandle.GetTasksByParams(taskType, keyword, orderType, first, max);
			totalTasksNum = taskhandle.getTotalTasksNumber(taskType, keyword);
			System.out.println("total number is: " + totalTasksNum);
			totalPages = (totalTasksNum == 0) ? 1:(int) Math.ceil((double)totalTasksNum/numPerPage);	
			jsonModel.put("totalItems", totalTasksNum);
			jsonModel.put("totalPages", totalPages);		
			jsonModel.put("tasks", tasks);
			jsonModel.put("tokenStatus", "VALID");
		}		
		
		return jsonModel;	
	}
	
	/**
	 * Converts task type string to integer
	 * @param taskTypeStr task type string
	 * @return task type
	 */
	private int getTaskType(String taskTypeStr) {
		
		if(taskTypeStr.equals("task")) {
			return TaskUtil.TASK;
		}else if(taskTypeStr.equals("notification")) {
			return TaskUtil.NOTIFICATION;
		}else if(taskTypeStr.equals("process")) {
			return TaskUtil.PROCESS;
		}else {
			return TaskUtil.PROCESS;
		}
	}


}
