package fi.koku.taskmanager.controller;

import static fi.arcusys.koku.util.Constants.ATTR_CURRENT_PAGE;
import static fi.arcusys.koku.util.Constants.ATTR_KEYWORD;
import static fi.arcusys.koku.util.Constants.ATTR_ORDER_TYPE;
import static fi.arcusys.koku.util.Constants.ATTR_TASK_TYPE;
import static fi.arcusys.koku.util.Constants.ATTR_TOKEN;
import static fi.arcusys.koku.util.Constants.ATTR_USERNAME;
import static fi.arcusys.koku.util.Constants.INTALIO_GROUP_PREFIX;
import static fi.arcusys.koku.util.Constants.TOKEN_STATUS_INVALID;
import static fi.arcusys.koku.util.Constants.TOKEN_STATUS_VALID;
import static fi.arcusys.koku.util.Constants.VIEW_TASK_MANAGER;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.arcusys.koku.intalio.TaskHandle;


/**
 * Handles the main task manager page
 * 
 * @author Jinhua Chen
 * May 11, 2011
 */
@Controller("taskManagerController")
@RequestMapping(value = "VIEW")
public class TaskManagerController {

	private static final Logger LOG = Logger.getLogger(TaskManagerController.class);

	/**
	 * Handles the portlet request to show default page
	 * 
	 * @param request RenderRequest
	 * @param response RenderResponse
	 * @param modelmap ModelMap
	 * @return default page taskmanager
	 */
	@RenderMapping
	public String home(RenderRequest request, RenderResponse response, ModelMap modelmap) {		
		return VIEW_TASK_MANAGER;
	}

	/**
	 * Returns to default page and set the page variable
	 * 
	 * @param request RenderRequest
	 * @param response RenderResponse
	 * @param modelmap ModelMap
	 * @return taskmanager page with page parameters
	 */
	@RenderMapping(params = "myaction=home")
	public String showHome(RenderRequest request, RenderResponse response, ModelMap modelmap) {
		//get parameters from 
		String currentPage = (String) request.getPortletSession().getAttribute(ATTR_CURRENT_PAGE, PortletSession.APPLICATION_SCOPE);
		String taskType = (String) request.getPortletSession().getAttribute(ATTR_TASK_TYPE, PortletSession.APPLICATION_SCOPE);
		String keyword = (String) request.getPortletSession().getAttribute(ATTR_KEYWORD, PortletSession.APPLICATION_SCOPE);
		String orderType = (String) request.getPortletSession().getAttribute(ATTR_ORDER_TYPE, PortletSession.APPLICATION_SCOPE);
		
		clearSession(request); // clear session since it's used only once
		
		modelmap.addAttribute(ATTR_CURRENT_PAGE, currentPage);
		modelmap.addAttribute(ATTR_TASK_TYPE, taskType);
		modelmap.addAttribute(ATTR_KEYWORD, keyword);
		modelmap.addAttribute(ATTR_ORDER_TYPE, orderType);

		return VIEW_TASK_MANAGER;
	}
	
	/**
	 * Clears page parameters in session
	 * 
	 * @param request RenderRequest
	 */
	public void clearSession(RenderRequest request) {
		PortletSession ps = request.getPortletSession();
		ps.removeAttribute(ATTR_CURRENT_PAGE, PortletSession.APPLICATION_SCOPE);
		ps.removeAttribute(ATTR_TASK_TYPE, PortletSession.APPLICATION_SCOPE);
		ps.removeAttribute(ATTR_KEYWORD, PortletSession.APPLICATION_SCOPE);
		ps.removeAttribute(ATTR_ORDER_TYPE, PortletSession.APPLICATION_SCOPE);
	}

	/**
	 * Returns intalio token status
	 * 
	 * @param request
	 * @return returns intalio token status
	 */
	@ModelAttribute(value = "tokenStatus")
	public String model(RenderRequest request) {
		if(checkUserToken(request)) {
			return TOKEN_STATUS_VALID;
		} else {
			return TOKEN_STATUS_INVALID;
		}
	}

	/**
	 * Checks user logged in or not, if logged in, verify the participant token
	 * 
	 * @param request
	 * @return true if token is valid, otherwise false
	 */
	private boolean checkUserToken(RenderRequest request) {
		String userid = null;
		String token = null;
		userid = request.getRemoteUser();
		
		try {
			
			if(userid != null) { // user is logged in
				
				PortletSession portletSession = request.getPortletSession();				
				token = (String) portletSession.getAttribute(ATTR_TOKEN);
				
				if(token == null) {
					TaskHandle taskhandle = new TaskHandle();
					//String username = "Kalle Kuntalainen";
					// FIXME: Okay.. How to get proper user password?
					// Fix also koku-navi-portlet AjaxController request counter which uses same method
					String username = INTALIO_GROUP_PREFIX + userid;
					String password = "test";
					token = taskhandle.getTokenByUser(username, password);
					portletSession.setAttribute(ATTR_TOKEN, token);
					portletSession.setAttribute(ATTR_USERNAME, username);
					LOG.debug("Portal username: '"+userid+"' Intalio username: '" + username + "' Password:  '" + password + "' Intalio token: '" + token + "'");
				}
				LOG.debug("Login user:" + userid);
			}

		} catch (Exception e) {
			LOG.error("Exception when getting user id. Username: '"+userid+"'",e);
		}
		
		if (token != null) {
			LOG.info("Intalio token is valid! Username: '"+userid+"' Token: '"+ token +"'");
			return true;
		} else {
			LOG.info("Intalio token is invalid! Username: '"+userid+"'");
			return false;
		}
	}

}
