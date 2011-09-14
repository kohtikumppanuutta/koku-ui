package fi.koku.taskmanager.controller;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.taskmanager.model.TaskHandle;
import static fi.arcusys.koku.util.Constants.*;


/**
 * Handles the main task manager page
 * @author Jinhua Chen
 * May 11, 2011
 */
@Controller("taskManagerController")
@RequestMapping(value = "VIEW")
public class TaskManagerController {

	Logger logger = Logger.getLogger(TaskManagerController.class);

	/**
	 * Handles the portlet request to show default page
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
	 * @param request RenderRequest
	 */
	public void clearSession(RenderRequest request) {
		PortletSession ps = request.getPortletSession();
		ps.removeAttribute(ATTR_CURRENT_PAGE, PortletSession.APPLICATION_SCOPE);
		ps.removeAttribute(ATTR_TASK_TYPE, PortletSession.APPLICATION_SCOPE);
		ps.removeAttribute(ATTR_KEYWORD, PortletSession.APPLICATION_SCOPE);
		ps.removeAttribute(ATTR_ORDER_TYPE, PortletSession.APPLICATION_SCOPE);
	}

	// -- @ModelAttribute here works as the referenceData method
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
	 * @param request
	 * @return true if token is valid, otherwise false
	 */
	public boolean checkUserToken(RenderRequest request) {
		String userid = null;
		String token = null;
		
		try {
			userid = request.getRemoteUser();
			
			if(userid != null) { // user is logged in
				
				PortletSession portletSession = request.getPortletSession();				
				token = (String) portletSession.getAttribute(ATTR_TOKEN);
				
				if(token == null) {
					TaskHandle taskhandle = new TaskHandle();
					//String username = "Kalle Kuntalainen";
					// FIXME: Okay.. How to get user password?
					String username = "koku/"+userid;
					String password = "test";
					token = taskhandle.getTokenByUser(username, password);
					portletSession.setAttribute(ATTR_TOKEN, token);
					portletSession.setAttribute(ATTR_USERNAME, username);
				}
				logger.info("Login user:" + userid);
			}

		} catch (Exception e) {
			logger.error("Exception when getting user id");
		}
		
		if (token != null) {
			logger.info("Intalio token is valid!");
			return true;
		} else {
			logger.info("Intalio token is invalid!");
			return false;
		}
	}

}
