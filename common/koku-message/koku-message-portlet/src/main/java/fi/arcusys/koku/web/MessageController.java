package fi.arcusys.koku.web;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Handle the main message page
 * 
 * @author Jinhua Chen
 * 
 */
@Controller("messageController")
@RequestMapping(value = "VIEW")
public class MessageController {

	// --maps the incoming portlet request to this method
	@RenderMapping
	public String home(RenderRequest request, RenderResponse response,
			ModelMap modelmap) {		
		System.out.println("show default homepage");
		
		return "message";
	}

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

		System.out.println("return homepage");
		return "message";
	}

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
	 * Check user logged in or not
	 * 
	 * @param request
	 * @return true if user is login, otherwise false
	 */
	public boolean checkUserToken(RenderRequest request) {
		String userid = null;

		try {
			userid = request.getRemoteUser();

			if (userid != null) { // user is logged in
				if(userid.equals("root")) // for gatein
					userid = "Ville Virkamies";
				
				PortletSession portletSession = request.getPortletSession();
				String username = (String) portletSession
						.getAttribute("USER_username");
				
				// not existing in session, then put it to session
				if (username == null) { 
					portletSession.setAttribute("USER_username", userid);
				}
			}

		} catch (Exception e) {
			// e.printStackTrace();
		}

		if (userid != null) {
			return true;
		} else {
			return false;
		}
	}

}
