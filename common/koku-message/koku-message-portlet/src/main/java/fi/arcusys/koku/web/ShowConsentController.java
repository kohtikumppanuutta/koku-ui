package fi.arcusys.koku.web;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

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
@Controller("singleConsentController")
@RequestMapping(value = "VIEW")
public class ShowConsentController {
	
	/**
	 * Shows consent page
	 * @param response RenderResponse
	 * @return consent page
	 */
	@RenderMapping(params = "myaction=showConsent")
	public String showPageView(RenderResponse response) {

		return "showconsent";
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
	public KokuConsent model(@RequestParam String consentId,
			@RequestParam String currentPage,@RequestParam String taskType, 
			@RequestParam String keyword, @RequestParam String orderType,
			RenderRequest request) {

		// store parameters in session for returning page from form page	
		request.getPortletSession().setAttribute("currentPage", currentPage, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("taskType", taskType, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("keyword", keyword, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("orderType", orderType, PortletSession.APPLICATION_SCOPE);
		
		KokuConsent consent = null;
		
		if(taskType.equals("cst_own_citizen")) {
			PortletSession portletSession = request.getPortletSession();
			String username = (String) portletSession.getAttribute("USER_username");
			TivaCitizenServiceHandle handle = new TivaCitizenServiceHandle(username);
			consent = handle.getConsentById(consentId);
		} else if(taskType.equals("cst_own_employee")) {
			TivaEmployeeServiceHandle handle = new TivaEmployeeServiceHandle();
			consent = handle.getConsentDetails(consentId);
		}
				
		return consent;
	}	

}
