package fi.arcusys.koku.web;


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
import fi.arcusys.koku.tiva.KokuConsent;
import fi.arcusys.koku.tiva.TivaCitizenServiceHandle;
import fi.arcusys.koku.tiva.TivaEmployeeServiceHandle;
import fi.arcusys.koku.users.UserIdResolver;
import fi.arcusys.koku.web.util.ModelWrapper;
import fi.arcusys.koku.web.util.ResponseStatus;
import fi.arcusys.koku.web.util.impl.ModelWrapperImpl;
import static fi.arcusys.koku.util.Constants.*;

/**
 * Shows task form page and store the current query information on the jsp page
 * @author Jinhua Chen
 * Aug 17, 2011
 */
@Controller("singleConsentController")
@RequestMapping(value = "VIEW")
public class ShowConsentController extends AbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(ShowConsentController.class);

	
	@Resource
	private ResourceBundleMessageSource messageSource;
	
	/**
	 * Shows consent page
	 * @param response RenderResponse
	 * @return consent page
	 */
	@RenderMapping(params = "myaction=showConsent")
	public String showPageView(RenderResponse response) {
		return VIEW_SHOW_CONSENT;
	}
		
	/**
	 * Creates data model integrated into the page and stores the page
	 * @param consentId consent id
	 * @param currentPage current page id
	 * @param taskType task type requested
	 * @param keyword page parameter keyword
	 * @param orderType page parameter order type
	 * @param request RenderRequest
	 * @return consent data model
	 */
	@ModelAttribute(value = "consent")
	public ModelWrapper<KokuConsent> model(
			@RequestParam(value="consentId", required=false) String consentId,
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
		
		ModelWrapper<KokuConsent> model = null;
		KokuConsent consent = null;
		
		final PortletSession portletSession = request.getPortletSession();
		final String username = (String) portletSession.getAttribute(ATTR_USERNAME);
		String userId = (String) portletSession.getAttribute(ATTR_USER_ID);
		if (userId == null) {
			try {			
				UserIdResolver resolver = new UserIdResolver();
				userId = resolver.getUserId(username, getPortalRole());
				portletSession.setAttribute(ATTR_USER_ID, userId);
			} catch (KokuServiceException e) {
				LOG.error("Failed to get UserUid username: '"+username+"' portalRole: '"+getPortalRole()+"'", e);
			}
		}
		
		try {			
			if (userId == null) {
				throw new KokuServiceException("UserId is null. Can't show consent details! username: '"+username+"'");
			}
			if(taskType.equals(TASK_TYPE_CONSENT_CITIZEN_CONSENTS) || taskType.equals(TASK_TYPE_CONSENT_CITIZEN_CONSENTS_OLD)) {
				TivaCitizenServiceHandle handle = new TivaCitizenServiceHandle(userId);
				handle.setMessageSource(messageSource);
				consent = handle.getConsentById(consentId);
			} else if(taskType.equals(TASK_TYPE_CONSENT_EMPLOYEE_CONSENTS)) {
				TivaEmployeeServiceHandle handle = new TivaEmployeeServiceHandle();
				handle.setMessageSource(messageSource);
				consent = handle.getConsentDetails(consentId);
			}
	//		else if (taskType.equals(TASK_TYPE_WARRANT_LIST_CITIZEN_CONSENTS)) {
	//			// TODO: Need some logic here? 
	//			// REMOVE ME?
	//		}
			model = new ModelWrapperImpl<KokuConsent>(consent);
		} catch (KokuServiceException kse) {
			LOG.error("Failed to show consent details. consentId: '"+consentId + 
					"' username: '"+request.getUserPrincipal().getName()+"' taskType: '"+taskType + 
					"' keyword: '" + keyword + "'", kse);
			model = new ModelWrapperImpl<KokuConsent>(null, ResponseStatus.FAIL, kse.getErrorcode());
		}
		return model;
	}
	
}
