package fi.arcusys.koku.web;

import javax.annotation.Resource;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.arcusys.koku.kv.Message;
import fi.arcusys.koku.kv.MessageHandle;
import static fi.arcusys.koku.util.Constants.*;

/**
 * Shows message details page and store the current query information on the jsp page
 * @author Jinhua Chen
 * Jun 22, 2011
 */
@Controller("singleMessageController")
@RequestMapping(value = "VIEW")
public class ShowMessageController {
	
	@Resource
	private ResourceBundleMessageSource messageSource;
	
	/**
	 * Shows message page
	 * @param response RenderResponse
	 * @return message page
	 */
	@RenderMapping(params = "myaction=showMessage")
	public String showPageView(RenderResponse response) {

		return "showmessage";
	}
		
	/**
	 * Creates data model integrated into the page and stores the page
	 * @param messageId message id
	 * @param currentPage current page id
	 * @param taskType task type requested
	 * @param keyword page parameter keyword
	 * @param orderType page parameter order type
	 * @param request RenderRequest
	 * @return message data model
	 */
	@ModelAttribute(value = "message")
	public Message model(@RequestParam String messageId,
			@RequestParam String currentPage,@RequestParam String taskType, 
			@RequestParam String keyword, @RequestParam String orderType,
			RenderRequest request) {

		// store parameters in session for returning page from form page	
		request.getPortletSession().setAttribute(ATTR_CURRENT_PAGE, currentPage, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_TASK_TYPE, taskType, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_KEYWORD, keyword, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_ORDER_TYPE, orderType, PortletSession.APPLICATION_SCOPE);
		
		MessageHandle msghandle = new MessageHandle();
		msghandle.setMessageSource(messageSource);
		Message message = msghandle.getMessageById(messageId);
		
		return message;
	}

}
