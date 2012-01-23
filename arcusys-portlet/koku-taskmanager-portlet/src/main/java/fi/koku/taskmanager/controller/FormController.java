package fi.koku.taskmanager.controller;

import static fi.arcusys.koku.util.Constants.ATTR_CURRENT_PAGE;
import static fi.arcusys.koku.util.Constants.ATTR_KEYWORD;
import static fi.arcusys.koku.util.Constants.ATTR_ORDER_TYPE;
import static fi.arcusys.koku.util.Constants.ATTR_TASK_TYPE;
import static fi.arcusys.koku.util.Constants.VIEW_TASK_FORM;

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
 * 
 * @author Jinhua Chen
 * May 11, 2011
 */
@Controller("formController")
@RequestMapping(value = "VIEW")
public class FormController {
	
	/**
	 * Shows a Intalio form page and set the page variable
	 * 
	 * @param response
	 * @return Intalio form page
	 */
	@RenderMapping(params = "myaction=taskform")
	public String showForm(RenderResponse response) {
		return VIEW_TASK_FORM;
	}
	
	/**
	 * Returns tasklink
	 *  
	 * @param tasklink
	 * @param currentPage
	 * @param taskType
	 * @param keyword
	 * @param orderType
	 * @param request
	 * @return tasklink
	 */
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
