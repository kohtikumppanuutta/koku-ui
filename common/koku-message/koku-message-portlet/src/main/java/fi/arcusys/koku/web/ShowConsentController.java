package fi.arcusys.koku.web;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.arcusys.koku.tiva.CitizenConsent;
import fi.arcusys.koku.tiva.TivaCitizenServiceHandle;

/**
 * Show task form page and store the current query information on the jsp page
 * @author Jinhua Chen
 *
 */
@Controller("singleConsentController")
@RequestMapping(value = "VIEW")
public class ShowConsentController {
	
	@RenderMapping(params = "myaction=showConsent")
	public String showConsent(RenderResponse response) {
		System.out.println("show single consent");
		return "showconsent";
	}
		
	// @ModelAttribute here works as the referenceData method
	@ModelAttribute(value = "consent")
	public CitizenConsent model(@RequestParam String consentId,
			@RequestParam String currentPage,@RequestParam String taskType, 
			@RequestParam String keyword, @RequestParam String orderType,
			RenderRequest request) {

		// store parameters in session for returning page from form page	
		request.getPortletSession().setAttribute("currentPage", currentPage, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("taskType", taskType, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("keyword", keyword, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("orderType", orderType, PortletSession.APPLICATION_SCOPE);
		
		TivaCitizenServiceHandle handle = new TivaCitizenServiceHandle();
		CitizenConsent consent = handle.getConsentById(consentId);
		
		return consent;
	}	

}
