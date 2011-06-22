package fi.arcusys.koku.web;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import fi.arcusys.koku.requestservice.KokuRequest;
import fi.arcusys.koku.requestservice.RequestHandle;

/**
 * Show task form page and store the current query information on the jsp page
 * @author Jinhua Chen
 *
 */
@Controller("singleRequestController")
@RequestMapping(value = "VIEW")
public class ShowRequestController {
	
	@RenderMapping(params = "myaction=showRequest")
	public String showRequest(RenderResponse response) {
		System.out.println("show single request");
		return "showrequest";
	}
		
	// @ModelAttribute here works as the referenceData method
	@ModelAttribute(value = "request")
	public KokuRequest model(@RequestParam String requestId,
			@RequestParam String currentPage,@RequestParam String taskType, 
			@RequestParam String keyword, @RequestParam String orderType,
			RenderRequest request) {

		// store parameters in session for returning page from form page	
		request.getPortletSession().setAttribute("currentPage", currentPage, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("taskType", taskType, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("keyword", keyword, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("orderType", orderType, PortletSession.APPLICATION_SCOPE);
		
		RequestHandle reqhandle = new RequestHandle();
		KokuRequest req = reqhandle.getKokuRequestById(requestId);
		
		return req;
	}

}
