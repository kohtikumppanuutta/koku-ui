package fi.koku.taskmanager.controller;

import static fi.arcusys.koku.util.Constants.ATTR_TASK_LINK;
import static fi.arcusys.koku.util.Constants.VIEW_POPUP_FORM;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/**
 * Shows task form page in a popup window
 * 
 * @author Jinhua Chen
 * May 11, 2011
 */
@Controller("popupController")
@RequestMapping(value = "VIEW")
public class PopupController {
	
	/**
	 * Shows a Intalio form page and set the page variable
	 * 
	 * @param response
	 * @return Intalio form page
	 */
	@RenderMapping(params = "myaction=popup")
	public String showForm(RenderResponse response) {
		return VIEW_POPUP_FORM;
	}
	
	/**
	 * 
	 * 
	 * @param tasklink
	 * @param modelmap
	 * @param req
	 * @param res
	 * @return Intalio form page 
	 */
	@ResourceMapping(value = "getForm")
	public String showPopupForm(
			@RequestParam(value = "tasklink") String tasklink,
			ModelMap modelmap, PortletRequest req, 
			PortletResponse res) {
		modelmap.addAttribute(ATTR_TASK_LINK, tasklink);
		return VIEW_POPUP_FORM;
	}
	
}
