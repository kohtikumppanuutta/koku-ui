package fi.arcusys.koku.web;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Handles the main message page
 * @author Jinhua Chen
 * Aug 12, 2011
 */
@Controller("messageController")
@RequestMapping(value = "VIEW")
public class MessageController {

	Logger logger = Logger.getLogger(MessageController.class);
	
	/**
	 * Handles the portlet request to show the default page
	 * @param request RenderRequest
	 * @param response RenderResponse
	 * @param modelmap ModelMap
	 * @return message page
	 */
	@RenderMapping
	public String home(RenderRequest request, RenderResponse response,
			ModelMap modelmap) {		
		logger.info("show default page message");
		
		return "message";
	}

	/**
	 * Returns the default home page and  stores page parameters
	 * @param request RenderRequest
	 * @param response RenderResponse
	 * @param modelmap ModelMap
	 * @return home page with page parameters
	 */
	@RenderMapping(params = "myaction=home")
	public String showHome(RenderRequest request, RenderResponse response,
			ModelMap modelmap) {
		// get parameters from session
		String currentPage = (String) request.getPortletSession().getAttribute(
				"currentPage", PortletSession.APPLICATION_SCOPE);
		String taskType = (String) request.getPortletSession().getAttribute(
				"taskType", PortletSession.APPLICATION_SCOPE);
		String keyword = (String) request.getPortletSession().getAttribute(
				"keyword", PortletSession.APPLICATION_SCOPE);
		String orderType = (String) request.getPortletSession().getAttribute(
				"orderType", PortletSession.APPLICATION_SCOPE);
		clearSession(request); // clear session since it's used only once
		
		modelmap.addAttribute("currentPage", currentPage);
		modelmap.addAttribute("taskType", taskType);
		modelmap.addAttribute("keyword", keyword);
		modelmap.addAttribute("orderType", orderType);

		return "message";
	}

	/**
	 * Clears page parameters in session
	 * @param request RenderRequest
	 */
	public void clearSession(RenderRequest request) {
		PortletSession ps = request.getPortletSession();
		ps.removeAttribute("currentPage", PortletSession.APPLICATION_SCOPE);
		ps.removeAttribute("taskType", PortletSession.APPLICATION_SCOPE);
		ps.removeAttribute("keyword", PortletSession.APPLICATION_SCOPE);
		ps.removeAttribute("orderType", PortletSession.APPLICATION_SCOPE);
	}
	
	// -- @ModelAttribute here works as the referenceData method
	@ModelAttribute(value = "loginStatus")
	public String model(RenderRequest request) {
		
		if (checkUserToken(request)) {
			return "VALID";
		} else {
			return "INVALID";
		}
	}

	/**
	 * Check user logged in or not, and put user info to session
	 * @param request RenderRequest
	 * @return true if user is login, otherwise false
	 */
	public boolean checkUserToken(RenderRequest request) {
		String userid = null;

		try {
			userid = request.getRemoteUser();

			if (userid != null) { // user is logged in				
				PortletSession portletSession = request.getPortletSession();
				portletSession.setAttribute("USER_username", userid);
			}

		} catch (Exception e) {
			logger.error("Exception when getting user id");
		}

		if (userid != null) {
			return true;
		} else {
			return false;
		}
	}

}
