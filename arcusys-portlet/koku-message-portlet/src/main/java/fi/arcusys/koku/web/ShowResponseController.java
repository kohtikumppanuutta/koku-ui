package fi.arcusys.koku.web;

import static fi.arcusys.koku.util.Constants.ATTR_CURRENT_PAGE;
import static fi.arcusys.koku.util.Constants.ATTR_KEYWORD;
import static fi.arcusys.koku.util.Constants.ATTR_ORDER_TYPE;
import static fi.arcusys.koku.util.Constants.ATTR_TASK_TYPE;
import static fi.arcusys.koku.util.Constants.VIEW_SHOW_RESPONSE;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.arcusys.koku.kv.model.KokuResponseDetail;
import fi.arcusys.koku.kv.request.citizen.CitizenRequestHandle;
import fi.arcusys.koku.util.Constants;


/**
 * Shows request response details page and store the current query information on the jsp page
 * @author Jinhua Chen
 * Jul 29, 2011
 */
@Controller("singleResponseController")
@RequestMapping(value = "VIEW")
public class ShowResponseController {
	
	/**
	 * Shows request page
	 * @param response RenderResponse
	 * @return request page
	 */
	@RenderMapping(params = "myaction=showResponse")
	public String showPageView(RenderResponse response) {
		return VIEW_SHOW_RESPONSE;
	}
		
	/**
	 * Creates data model integrated into the page and stores the page
	 * @param requestId request id
	 * @param currentPage current page id
	 * @param taskType task type requested
	 * @param keyword page parameter keyword
	 * @param orderType page parameter order type
	 * @param request RenderRequest
	 * @return request data model
	 */
	@ModelAttribute(value = "response")
	public KokuResponseDetail model(@RequestParam String responseId,
			@RequestParam String currentPage,@RequestParam String taskType, 
			@RequestParam String keyword, @RequestParam String orderType,
			RenderRequest request) {

		// store parameters in session for returning page from form page	
		request.getPortletSession().setAttribute(ATTR_CURRENT_PAGE, currentPage, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_TASK_TYPE, taskType, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_KEYWORD, keyword, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_ORDER_TYPE, orderType, PortletSession.APPLICATION_SCOPE);
		
		KokuResponseDetail details = null;
		if (taskType.equals(Constants.TASK_TYPE_REQUEST_REPLIED) || taskType.equals(Constants.TASK_TYPE_REQUEST_OLD)) {
			CitizenRequestHandle handle = new CitizenRequestHandle();
			details = handle.getResponseById(responseId);
		}
		return details;
	}
}
