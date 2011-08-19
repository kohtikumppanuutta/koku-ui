package fi.arcusys.koku.web;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.arcusys.koku.appointment.AppointmentHandle;
import fi.arcusys.koku.appointment.KokuAppointment;

/**
 * Shows appointment page and store the current query information on the jsp page
 * @author Jinhua Chen
 * Aug 4, 2011
 */
@Controller("singleAppointmentController")
@RequestMapping(value = "VIEW")
public class ShowAppointmentController {
	
	@RenderMapping(params = "myaction=showAppointment")
	public String showRequest(RenderResponse response) {

		return "showappointment";
	}
		
	// @ModelAttribute here works as the referenceData method
	@ModelAttribute(value = "appointment")
	public KokuAppointment model(@RequestParam String appointmentId,
			@RequestParam String currentPage,@RequestParam String taskType, 
			@RequestParam String keyword, @RequestParam String orderType,
			RenderRequest request) {
	
		// store parameters in session for returning page from form page	
		request.getPortletSession().setAttribute("currentPage", currentPage, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("taskType", taskType, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("keyword", keyword, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute("orderType", orderType, PortletSession.APPLICATION_SCOPE);
		
		AppointmentHandle appHandle = new AppointmentHandle();
		KokuAppointment app = appHandle.getAppointmentById(appointmentId);
		
		return app;
	}
	

}
