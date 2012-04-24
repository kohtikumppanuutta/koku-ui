package fi.arcusys.koku.web;


import static fi.arcusys.koku.util.Constants.ATTR_CURRENT_PAGE;
import static fi.arcusys.koku.util.Constants.ATTR_KEYWORD;
import static fi.arcusys.koku.util.Constants.ATTR_ORDER_TYPE;
import static fi.arcusys.koku.util.Constants.ATTR_TASK_TYPE;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_APPLICATION_KINDERGARTEN_BROWSE;
import static fi.arcusys.koku.util.Constants.VIEW_SHOW_APPLICATION_KINDERGARTEN;

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

import fi.arcusys.koku.hak.model.HakServiceHandle;
import fi.arcusys.koku.hak.model.KokuApplicationSummary;

/**
 * Shows Tietopyyntö form page and store the current query information on the jsp page
 * 
 * @author Toni Turunen
 */
@Controller("singleKindergartenController")
@RequestMapping(value = "VIEW")
public class ShowKindergartenController extends AbstractController {
	
	@Resource
	private ResourceBundleMessageSource messageSource;
	
	/**
	 * Shows warrant page
	 * @param response RenderResponse
	 * @return consent page
	 */
	@RenderMapping(params = "myaction=showApplicationKindergarten")
	public String showPageView(RenderResponse response) {
		return VIEW_SHOW_APPLICATION_KINDERGARTEN;
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
	@ModelAttribute(value = "application")
	public KokuApplicationSummary model(
			@RequestParam String applicationId,
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
		
		KokuApplicationSummary application = null;
		
		
		if(taskType.equals(TASK_TYPE_APPLICATION_KINDERGARTEN_BROWSE)) {
			HakServiceHandle handle = new HakServiceHandle();
			handle.setMessageSource(messageSource);
			application = handle.getApplicantDetails(applicationId);
		}
		return application;
	}
}
