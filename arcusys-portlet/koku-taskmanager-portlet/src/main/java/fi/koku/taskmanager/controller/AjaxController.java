package fi.koku.taskmanager.controller;

import static fi.arcusys.koku.util.Constants.ATTR_CURRENT_PAGE;
import static fi.arcusys.koku.util.Constants.ATTR_KEYWORD;
import static fi.arcusys.koku.util.Constants.ATTR_MY_ACTION;
import static fi.arcusys.koku.util.Constants.ATTR_ORDER_TYPE;
import static fi.arcusys.koku.util.Constants.ATTR_TASK_LINK;
import static fi.arcusys.koku.util.Constants.ATTR_TASK_TYPE;
import static fi.arcusys.koku.util.Constants.ATTR_TOKEN;
import static fi.arcusys.koku.util.Constants.ATTR_USERNAME;
import static fi.arcusys.koku.util.Constants.JSON_EDITABLE;
import static fi.arcusys.koku.util.Constants.JSON_LOGIN_STATUS;
import static fi.arcusys.koku.util.Constants.JSON_RENDER_URL;
import static fi.arcusys.koku.util.Constants.JSON_TASKS;
import static fi.arcusys.koku.util.Constants.JSON_TASK_STATE;
import static fi.arcusys.koku.util.Constants.JSON_TOKEN_STATUS;
import static fi.arcusys.koku.util.Constants.JSON_TOTAL_ITEMS;
import static fi.arcusys.koku.util.Constants.JSON_TOTAL_PAGES;
import static fi.arcusys.koku.util.Constants.MY_ACTION_TASKFORM;
import static fi.arcusys.koku.util.Constants.PREF_EDITABLE;
import static fi.arcusys.koku.util.Constants.RESPONSE;
import static fi.arcusys.koku.util.Constants.TOKEN_STATUS_INVALID;
import static fi.arcusys.koku.util.Constants.TOKEN_STATUS_VALID;

import java.util.List;

import javax.portlet.PortletPreferences;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import fi.arcusys.koku.intalio.Task;
import fi.arcusys.koku.intalio.TaskHandle;
import fi.arcusys.koku.util.TaskUtil;

/**
 * Handles ajax request from web and returns the data with json string
 * 
 * @author Jinhua Chen
 * May 11, 2011
 */
@Controller("ajaxController")
@RequestMapping(value = "VIEW")
public class AjaxController {
	
	private static final Logger LOG = LoggerFactory.getLogger(AjaxController.class);

	/**
	 * Shows ajax for retrieving intalio tasks
	 * 
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
	public String showAjax(
			@RequestParam(value = "page") int page,
			@RequestParam(value = "taskType") String taskTypeStr,
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "orderType") String orderType,
			ModelMap modelmap, 
			PortletRequest request, 
			PortletResponse response) {
		
		final PortletSession portletSession = request.getPortletSession();				
		final String token = (String) portletSession.getAttribute(ATTR_TOKEN);
		final String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		
		int taskType = getTaskType(taskTypeStr);
		JSONObject jsonModel = getJsonModel(taskType, page, keyword, orderType, token, username);
				
		PortletPreferences pref = request.getPreferences();
		Boolean editableForm = Boolean.valueOf(pref.getValue(PREF_EDITABLE, Boolean.FALSE.toString()));
		jsonModel.put(JSON_EDITABLE, editableForm.toString());
		modelmap.addAttribute(RESPONSE, jsonModel);
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	/**
	 * Gets the task state
	 * 
	 * @param taskId intalio task id
	 * @param modelmap
	 * @param request
	 * @param response
	 * @return ajax view with task state in json format
	 */
	@ResourceMapping(value = "getState")
	public String getTaskState(
			@RequestParam(value = "taskId") String taskId,
			ModelMap modelmap, 
			PortletRequest request, 
			PortletResponse response) {
		
		final PortletSession portletSession = request.getPortletSession();				
		final String token = (String) portletSession.getAttribute(ATTR_TOKEN);
		final String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		
		JSONObject jsonModel = new JSONObject();
		
		if (token == null) {
			jsonModel.put(JSON_TOKEN_STATUS, TOKEN_STATUS_INVALID);
			LOG.info("Intalio token is invalid!");
		} else {
			TaskHandle taskhandle = new TaskHandle(token, username);
			String taskState = taskhandle.getTaskStatus(taskId);
			jsonModel.put(JSON_TASK_STATE, taskState);
		}		
		modelmap.addAttribute(RESPONSE, jsonModel);		
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	/**
	 * Processes task query and gets task list
	 * 
	 * @param taskType task type
	 * @param page page id
	 * @param keyword keyword for searching/filtering
	 * @param orderType order type of tasks
	 * @param token user participant token
	 * @param username user name
	 * @return task information in Json format
	 */
	public JSONObject getJsonModel(
			int taskType, 
			int page, 
			String keyword, 
			String orderType, 
			String token, 
			String username) {
		JSONObject jsonModel = new JSONObject();
		
		if (token == null) {
			jsonModel.put(JSON_TOKEN_STATUS, TOKEN_STATUS_INVALID);
			LOG.info("Intalio token is invalid!");
		} else {
			TaskHandle taskhandle = new TaskHandle(token, username);
			int numPerPage = TaskUtil.PAGE_NUMBER;
			int totalTasksNum;
			int totalPages;
			List<Task> tasks;
			String first = String.valueOf((page-1)*numPerPage);
			String max =  String.valueOf(numPerPage);
			tasks = taskhandle.getTasksByParams(taskType, keyword, orderType, first, max);
			totalTasksNum = taskhandle.getTotalTasksNumber(taskType, keyword);
			totalPages = (totalTasksNum == 0) ? 1:(int) Math.ceil((double)totalTasksNum/numPerPage);	
			jsonModel.put(JSON_TOTAL_ITEMS, totalTasksNum);
			jsonModel.put(JSON_TOTAL_PAGES, totalPages);		
			jsonModel.put(JSON_TASKS, tasks);
			jsonModel.put(JSON_TOKEN_STATUS, TOKEN_STATUS_VALID);
		}		
		return jsonModel;
	}
	
	/**
	 * Converts task type string to integer
	 * 
	 * @param taskTypeStr task type string
	 * @return task type
	 */
	private int getTaskType(String taskTypeStr) {
		
		if (taskTypeStr.equals("task")) {
			return TaskUtil.TASK;
		} else if(taskTypeStr.equals("notification")) {
			return TaskUtil.NOTIFICATION;
		} else if(taskTypeStr.equals("process")) {
			return TaskUtil.PROCESS;
		} else {
			return TaskUtil.PROCESS;
		}
	}
	
	/**
	 * Returns createForm renderUrl
	 * 
	 * @param taskLink
	 * @param currentPage
	 * @param taskType
	 * @param keyword
	 * @param orderType
	 * @param modelmap
	 * @param request
	 * @param response
	 * @return returns jsonData
	 */
	@ResourceMapping(value = "createFormRenderUrl")
	public String createFormRenderUrl(
			@RequestParam(value = "tasklink") String taskLink,
			@RequestParam(value = "currentPage") String currentPage,
			@RequestParam(value = "taskType") String taskType,
			@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "orderType") String orderType,
			ModelMap modelmap, 
			PortletRequest request, 
			ResourceResponse response) {
				
		final PortletURL renderUrlObj = response.createRenderURL();
		renderUrlObj.setParameter( ATTR_MY_ACTION, MY_ACTION_TASKFORM);
		renderUrlObj.setParameter( ATTR_TASK_LINK, taskLink);
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
	 * Returns popupRenderUrl
	 * 
	 * @param taskLink
	 * @param modelmap
	 * @param request
	 * @param response
	 * @return returns jsonData
	 */
	@ResourceMapping(value = "createPopupRenderUrl")
	public String createPopupRenderUrl(
			@RequestParam(value = "tasklink") String taskLink,
			ModelMap modelmap, PortletRequest request, ResourceResponse response) {
				
		final PortletURL renderUrlObj = response.createRenderURL();
		renderUrlObj.setParameter( ATTR_MY_ACTION, MY_ACTION_TASKFORM);
		renderUrlObj.setParameter( ATTR_TASK_LINK, taskLink);
		try {
			renderUrlObj.setWindowState(WindowState.MAXIMIZED);
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
	 * Creates authentication failed JSON response
	 * 
	 * @param modelMap
	 * @param username
	 * @return jsondata
	 */
	protected String authenticationFailed(ModelMap modelMap, String username){
		LOG.error("Strong authentication required! User '"+username+"' is not Vetuma authenticated!");	
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_LOGIN_STATUS, TOKEN_STATUS_INVALID);
		modelMap.addAttribute(RESPONSE, jsonModel);
		return AjaxViewResolver.AJAX_PREFIX;
	}
}
