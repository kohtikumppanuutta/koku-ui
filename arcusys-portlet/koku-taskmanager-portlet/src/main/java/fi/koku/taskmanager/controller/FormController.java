package fi.koku.taskmanager.controller;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import static fi.arcusys.koku.util.Constants.*;

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
		return VIEW_TASK_FORM;
	}
		
	// @ModelAttribute here works as the referenceData method
	@ModelAttribute(value = "tasklink")
	public String model(@RequestParam String tasklink,
			@RequestParam String currentPage,@RequestParam String taskType, 
			@RequestParam String keyword, @RequestParam String orderType,
			RenderRequest request) {

		// store parameters in session for returning page from form page	
		request.getPortletSession().setAttribute(ATTR_CURRENT_PAGE, currentPage, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_TASK_TYPE, taskType, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_KEYWORD, keyword, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_ORDER_TYPE, orderType, PortletSession.APPLICATION_SCOPE);
		return tasklink;
	}
}
