package fi.arcusys.koku.web;

import static fi.arcusys.koku.util.Constants.ATTR_CURRENT_PAGE;
import static fi.arcusys.koku.util.Constants.ATTR_KEYWORD;
import static fi.arcusys.koku.util.Constants.ATTR_ORDER_TYPE;
import static fi.arcusys.koku.util.Constants.ATTR_TASK_TYPE;
import static fi.arcusys.koku.util.Constants.ATTR_USERNAME;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_CONSENT_CITIZEN_CONSENTS;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_CONSENT_EMPLOYEE_CONSENTS;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_CONSENT_LIST_CITIZEN_CONSENTS;

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
import fi.arcusys.koku.tiva.TivaCitizenServiceHandle;
import fi.arcusys.koku.tiva.TivaEmployeeServiceHandle;

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
		return "showIntalioForm";
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

		if (taskType.equals(TASK_TYPE_CONSENT_LIST_CITIZEN_CONSENTS)) {
			// TODO: Need some logic here?
		}
		return consent;
	}

}
