package fi.arcusys.koku.web;


import static fi.arcusys.koku.util.Constants.ATTR_CURRENT_PAGE;
import static fi.arcusys.koku.util.Constants.ATTR_KEYWORD;
import static fi.arcusys.koku.util.Constants.ATTR_ORDER_TYPE;
import static fi.arcusys.koku.util.Constants.ATTR_TASK_TYPE;
import static fi.arcusys.koku.util.Constants.ATTR_USERNAME;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_INFO_REQUEST_BROWSE_REPLIED;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_INFO_REQUEST_BROWSE_SENT;
import static fi.arcusys.koku.util.Constants.VIEW_SHOW_INFO_REQUEST;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_INFO_REQUEST_BROWSE;

import javax.annotation.Resource;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.tiva.tietopyynto.employee.KokuEmployeeTietopyyntoServiceHandle;
import fi.arcusys.koku.tiva.tietopyynto.model.KokuInformationRequestDetail;
import fi.arcusys.koku.users.UserIdResolver;

/**
 * Shows Tietopyyntö form page and store the current query information on the jsp page
 * 
 * @author Toni Turunen
 */
@Controller("singleTipyController")
@RequestMapping(value = "VIEW")
public class ShowTipyController extends AbstractController {
	private static final Logger LOG = Logger.getLogger(ShowTipyController.class);

	@Resource
	ResourceBundleMessageSource messageSource;
	
	/**
	 * Shows warrant page
	 * @param response RenderResponse
	 * @return consent page
	 */
	@RenderMapping(params = "myaction=showTipy")
	public String showPageView(RenderResponse response) {
		return VIEW_SHOW_INFO_REQUEST;
	}
		
	/**
	 * Creates data model integrated into the page and stores the page
	 * @param authorizationId authorization id
	 * @param currentPage current page id
	 * @param taskType task type requested
	 * @param keyword page parameter keyword
	 * @param orderType page parameter order type
	 * @param request RenderRequest
	 * @return KokuAuthorizationSummary data model
	 */
	@ModelAttribute(value = "tipy")
	public KokuInformationRequestDetail model(
			@RequestParam String requestId,
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
		
		KokuInformationRequestDetail  info = null;
		
		PortletSession portletSession = request.getPortletSession();
		String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		UserIdResolver resolver = new UserIdResolver();
		String userId = resolver.getUserId(username, getPortalRole());
		long reqId = -1; 
		try {
			reqId  = Long.valueOf(requestId);
		} catch (NumberFormatException nfe) {
			LOG.error("AuthorizationID is not valid! Username: " + username + " UserId: " + userId + " AuthorizationId: "+ requestId);
			return null;
		}
		
		try {
			if(taskType.equals(TASK_TYPE_INFO_REQUEST_BROWSE_REPLIED) 
			   || taskType.equals(TASK_TYPE_INFO_REQUEST_BROWSE_SENT)
			   || taskType.equals(TASK_TYPE_INFO_REQUEST_BROWSE)) {
				KokuEmployeeTietopyyntoServiceHandle handle = new KokuEmployeeTietopyyntoServiceHandle();
				handle.setMessageSource(messageSource);
				info = handle.getRequestDetails(reqId);
			}
		} catch (KokuServiceException kse) {
			LOG.error("Failed to show infoRequest details. infoRequestId: '"+requestId + 
					"' username: '"+request.getUserPrincipal().getName()+" taskType: '"+taskType + 
					"' keyword: '" + keyword + "'", kse);
		}
		return info;
	}
}
