package fi.arcusys.koku.web;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.arcusys.koku.message.Message;
import fi.arcusys.koku.message.MessageHandle;

/**
 * Show task form page and store the current query information on the jsp page
 * @author Jinhua Chen
 *
 */
@Controller("singleMessageController")
@RequestMapping(value = "VIEW")
public class ShowMessageController {
	
	@RenderMapping(params = "myaction=showMessage")
	public String showForm(RenderResponse response) {
		System.out.println("show single message");
		return "showmessage";
	}
		
	// @ModelAttribute here works as the referenceData method
	@ModelAttribute(value = "message")
	public Message model(@RequestParam String messageId,
			@RequestParam String currentPage,@RequestParam String taskType, 
			@RequestParam String keyword, @RequestParam String orderType,
			RenderRequest request) {

		// store parameters in session for returning page from form page	
		request.getPortletSession().setAttribute("currentPage", currentPage, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("taskType", taskType, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("keyword", keyword, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("orderType", orderType, PortletSession.APPLICATION_SCOPE);
		
		MessageHandle msghandle = new MessageHandle();
		Message message = msghandle.getMessageById(messageId);
		
		return message;
	}

}
