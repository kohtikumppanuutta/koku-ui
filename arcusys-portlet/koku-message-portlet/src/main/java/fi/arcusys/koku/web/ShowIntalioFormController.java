package fi.arcusys.koku.web;

import static fi.arcusys.koku.util.Constants.*;

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

import fi.arcusys.koku.tiva.KokuConsent;

/**
 * Shows task form page and store the current query information on the jsp page
 * @author Jinhua Chen
 * Aug 17, 2011
 */
@Controller("singleIntalioFormController")
@RequestMapping(value = "VIEW")
public class ShowIntalioFormController {
	
	@Resource
	private ResourceBundleMessageSource messageSource;

	
	/**
	 * Shows message page
	 * @param response RenderResponse
	 * @return message page
	 */
	@RenderMapping(params = "myaction=showIntalioForm")
	public String showPageView(RenderResponse response) {
		return VIEW_SHOW_INTALIO_FORM;
	}
	
	@ModelAttribute(value = "intalioForm")
	public KokuConsent model(
			@RequestParam String consentId,
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
		
		KokuConsent consent = null;

//		if (taskType.equals(TASK_TYPE_WARRANT_LIST_CITIZEN_CONSENTS)) {
//			// TODO: Need some logic here?
//		}
		return consent;
	}

}
