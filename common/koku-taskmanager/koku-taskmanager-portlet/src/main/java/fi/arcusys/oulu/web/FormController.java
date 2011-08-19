package fi.arcusys.oulu.web;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Shows task form page and store the current query information on the jsp page
 * @author Jinhua Chen
 * May 11, 2011
 */
@Controller("formController")
@RequestMapping(value = "VIEW")
public class FormController {
	
	@RenderMapping(params = "myaction=taskform")
	public String showForm(RenderResponse response) {
		System.out.println("show task form");
		return "taskform";
	}
		
	// @ModelAttribute here works as the referenceData method
	@ModelAttribute(value = "tasklink")
	public String model(@RequestParam String tasklink,
			@RequestParam String currentPage,@RequestParam String taskType, 
			@RequestParam String keyword, @RequestParam String orderType,
			RenderRequest request) {

		// store parameters in session for returning page from form page	
		request.getPortletSession().setAttribute("currentPage", currentPage, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("taskType", taskType, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("keyword", keyword, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("orderType", orderType, PortletSession.APPLICATION_SCOPE);
			
		return tasklink;
	}

}
