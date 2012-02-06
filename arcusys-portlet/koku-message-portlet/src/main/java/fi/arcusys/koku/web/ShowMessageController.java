package fi.arcusys.koku.web;

import static fi.arcusys.koku.util.Constants.ATTR_CURRENT_PAGE;
import static fi.arcusys.koku.util.Constants.ATTR_KEYWORD;
import static fi.arcusys.koku.util.Constants.ATTR_ORDER_TYPE;
import static fi.arcusys.koku.util.Constants.ATTR_TASK_TYPE;
import static fi.arcusys.koku.util.Constants.VIEW_SHOW_MESSAGE;

import javax.annotation.Resource;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.kv.message.MessageHandle;
import fi.arcusys.koku.kv.model.Message;
import fi.arcusys.koku.web.util.ModelWrapper;
import fi.arcusys.koku.web.util.ResponseStatus;
import fi.arcusys.koku.web.util.impl.ModelWrapperImpl;

/**
 * Shows message details page and store the current query information on the jsp page
 * @author Jinhua Chen
 * Jun 22, 2011
 */
@Controller("singleMessageController")
@RequestMapping(value = "VIEW")
public class ShowMessageController {
	private static final Logger LOG = LoggerFactory.getLogger(ShowMessageController.class);

	
	@Resource
	private ResourceBundleMessageSource messageSource;
	
	/**
	 * Shows message page
	 * @param response RenderResponse
	 * @return message page
	 */
	@RenderMapping(params = "myaction=showMessage")
	public String showPageView(RenderResponse response) {

		return VIEW_SHOW_MESSAGE;
	}
			
	/**
	 * Creates data model integrated into the page and stores the page
	 * @param messageId message id
	 * @param currentPage current page id
	 * @param taskType task type requested
	 * @param keyword page parameter keyword
	 * @param orderType page parameter order type
	 * @param request RenderRequest
	 * @return 
	 * @return message data model
	 */
	@ModelAttribute(value = "message")
	public ModelWrapper<Message> model (
			@RequestParam String messageId,
			@RequestParam String currentPage,
			@RequestParam String taskType, 
			@RequestParam String keyword,
			@RequestParam String orderType,
			RenderRequest request) {

		// store parameters in session for returning page from form page	
		request.getPortletSession().setAttribute(ATTR_CURRENT_PAGE, currentPage, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_TASK_TYPE, taskType, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_KEYWORD, keyword, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_ORDER_TYPE, orderType, PortletSession.APPLICATION_SCOPE);
		
		ModelWrapper<Message> modelWrapper = null;
		Message message = null;
		try {
			MessageHandle msghandle = new MessageHandle();
			msghandle.setMessageSource(messageSource);
			message = msghandle.getMessageById(messageId);
			modelWrapper = new ModelWrapperImpl<Message>(message, ResponseStatus.OK);
		} catch (KokuServiceException kse) {
			LOG.error("Failed to show message details. messageId: '"+messageId + 
					"' username: '"+request.getUserPrincipal().getName()+" taskType: '"+taskType + 
					"' keyword: '" + keyword + "'", kse);
			modelWrapper = new ModelWrapperImpl<Message>(null, ResponseStatus.FAIL, kse.getErrorcode());
		}
		return modelWrapper;
	}

}
