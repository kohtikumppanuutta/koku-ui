package fi.arcusys.koku.web;

import java.util.Locale;

import javax.annotation.Resource;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.arcusys.koku.av.AvCitizenServiceHandle;
import fi.arcusys.koku.av.AvEmployeeServiceHandle;
import fi.arcusys.koku.av.KokuAppointment;
import static fi.arcusys.koku.util.Constants.*;


/**
 * Shows appointment page and store the current query information on the jsp page
 * @author Jinhua Chen
 * Aug 4, 2011
 */
@Controller("singleAppointmentController")
@RequestMapping(value = "VIEW")
public class ShowAppointmentController {
	
	private Logger LOG = Logger.getLogger(ShowAppointmentController.class);
	
	@Resource
	ResourceBundleMessageSource messageSource;
	
	/**
	 * Shows the page that presents appointment in detail for either employee
	 * or citizen according to task type
	 * @param taskType task type requested
	 * @param response RenderResponse
	 * @return appointment page
	 */
	@RenderMapping(params = "myaction=showAppointment")
	public String showPageView(@RequestParam String taskType, RenderResponse response) {

		String page = VIEW_SHOW_CITIZEN_APPOINTMENT;
		
		if(taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN)) {
			page = VIEW_SHOW_CITIZEN_APPOINTMENT;
		} else if(taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_EMPLOYEE)) {
			page = VIEW_SHOW_EMPLOYEE_APPOINTMENT;
		}		
		return page;
	}
		
	/**
	 * Creates data model integrated into the page and stores the page
	 * parameters in the session
	 * @param appointmentId appointment id
	 * @param currentPage current page id
	 * @param taskType task type requested
	 * @param keyword page parameter keyword
	 * @param orderType page parameter order type
	 * @param request RenderRequest
	 * @return appointment data model
	 */
	@ModelAttribute(value = "appointment")
	public KokuAppointment model(@RequestParam String appointmentId,
			@RequestParam String currentPage,@RequestParam String taskType, 
			@RequestParam String keyword, @RequestParam String orderType,
			@RequestParam(value = "targetPerson", required = false) String targetPerson, RenderRequest request) {
	
		// store parameters in session for returning page from form page	
		request.getPortletSession().setAttribute(ATTR_CURRENT_PAGE, currentPage, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_TASK_TYPE, taskType, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_KEYWORD, keyword, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_ORDER_TYPE, orderType, PortletSession.APPLICATION_SCOPE);
		
		KokuAppointment app = null;
		
		if(taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN)
				|| taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN_OLD)) {
			AvCitizenServiceHandle handle = new AvCitizenServiceHandle();
			handle.setMessageSource(messageSource);
			app = handle.getAppointmentById(appointmentId, targetPerson);
		} else if(taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_EMPLOYEE)) {
			AvEmployeeServiceHandle handle = new AvEmployeeServiceHandle();
			handle.setMessageSource(messageSource);
			app = handle.getAppointmentById(appointmentId);
		}
		return app;
	}	
	
}
