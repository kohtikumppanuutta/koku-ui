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
import static fi.arcusys.koku.util.Constants.*;

/**
 * Handles the main message page
 * @author Jinhua Chen
 * Aug 12, 2011
 */
@Controller("messageController")
@RequestMapping(value = "VIEW")
public class MessageController extends AbstractController {

	private static final Logger LOG = Logger.getLogger(MessageController.class);
	
	/**
	 * Handles the portlet request to show the default page
	 * @param request RenderRequest
	 * @param response RenderResponse
	 * @param modelmap ModelMap
	 * @return message page
	 */
	@RenderMapping
	public String showPageView(RenderRequest request, RenderResponse response,
			ModelMap modelmap) {
		return getView();
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
				ATTR_CURRENT_PAGE, PortletSession.APPLICATION_SCOPE);
		String taskType = (String) request.getPortletSession().getAttribute(
				ATTR_TASK_TYPE, PortletSession.APPLICATION_SCOPE);
		String keyword = (String) request.getPortletSession().getAttribute(
				ATTR_KEYWORD, PortletSession.APPLICATION_SCOPE);
		String orderType = (String) request.getPortletSession().getAttribute(
				ATTR_ORDER_TYPE, PortletSession.APPLICATION_SCOPE);
		clearSession(request); // clear session since it's used only once
		
		modelmap.addAttribute(ATTR_CURRENT_PAGE, currentPage);
		modelmap.addAttribute(ATTR_TASK_TYPE, taskType);
		modelmap.addAttribute(ATTR_KEYWORD, keyword);
		modelmap.addAttribute(ATTR_ORDER_TYPE, orderType);

		return getView();
	}

	private String getView() {
		switch(getPortalRole()) {
			case CITIZEN: return VIEW_MESSAGE_CITIZEN;
			case EMPLOYEE: return VIEW_MESSAGE_EMPLOYEE;
			default: return VIEW_ERROR;
		}
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
				portletSession.setAttribute(ATTR_USERNAME, userid);
			}
		} catch (Exception e) {
			LOG.error("Exception when getting user id");
		}

		if (userid != null) {
			return true;
		} else {
			return false;
		}
	}

}
