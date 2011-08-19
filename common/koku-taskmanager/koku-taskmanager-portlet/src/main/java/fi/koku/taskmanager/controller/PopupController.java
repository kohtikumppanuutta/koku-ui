package fi.koku.taskmanager.controller;

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
 * @author Jinhua Chen
 * May 11, 2011
 */
@Controller("popupController")
@RequestMapping(value = "VIEW")
public class PopupController {
	
	@RenderMapping(params = "myaction=popup")
	public String showForm(RenderResponse response) {
		System.out.println("show popup form");
		return "popupform";
	}
	
	@ResourceMapping(value = "getForm")
	public String showPopupForm(@RequestParam(value = "tasklink") String tasklink,
			ModelMap modelmap, PortletRequest req, PortletResponse res) {
		modelmap.addAttribute("tasklink", tasklink);
		System.out.println("show popup form");
		return "popupform";
	}
	
}
