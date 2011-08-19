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

		return "taskmanager";
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
		String currentPage = (String) request.getPortletSession().getAttribute("currentPage", PortletSession.APPLICATION_SCOPE);
		String taskType = (String) request.getPortletSession().getAttribute("taskType", PortletSession.APPLICATION_SCOPE);
		String keyword = (String) request.getPortletSession().getAttribute("keyword", PortletSession.APPLICATION_SCOPE);
		String orderType = (String) request.getPortletSession().getAttribute("orderType", PortletSession.APPLICATION_SCOPE);
		
		modelmap.addAttribute("currentPage", currentPage);
		modelmap.addAttribute("taskType", taskType);
		modelmap.addAttribute("keyword", keyword);
		modelmap.addAttribute("orderType", orderType);

		return "taskmanager";
	}
	
	// -- @ModelAttribute here works as the referenceData method
	@ModelAttribute(value = "tokenStatus")
	public String model(RenderRequest request) {
		if(checkUserToken(request)) {
			return "VALID";
		}else {
			return "INVALID";
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
				token = (String) portletSession.getAttribute("USER_token");
				
				if(token == null) {
					TaskHandle taskhandle = new TaskHandle();
					//String username = "Kalle Kuntalainen";
					String username = "koku/"+userid;
					String password = "test";
					token = taskhandle.getTokenByUser(username, password);
					portletSession.setAttribute("USER_token", token);
					portletSession.setAttribute("USER_username", username);
				}
				
				logger.info("Login user:" + userid);
			}

		} catch (Exception e) {
			logger.error("Exception when getting user id");
		}
		
		if(token != null) {
			logger.info("Intalio token is valid!");
			return true;
		}else {
			logger.info("Intalio token is invalid!");
			return false;
		}
	}

}
