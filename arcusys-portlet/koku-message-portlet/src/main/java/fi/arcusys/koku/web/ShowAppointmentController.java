package fi.arcusys.koku.web;

import static fi.arcusys.koku.util.Constants.ATTR_CURRENT_PAGE;
import static fi.arcusys.koku.util.Constants.ATTR_KEYWORD;
import static fi.arcusys.koku.util.Constants.ATTR_ORDER_TYPE;
import static fi.arcusys.koku.util.Constants.ATTR_TASK_TYPE;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN_OLD;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_APPOINTMENT_RESPONSE_EMPLOYEE;
import static fi.arcusys.koku.util.Constants.VIEW_SHOW_CITIZEN_APPOINTMENT;
import static fi.arcusys.koku.util.Constants.VIEW_SHOW_EMPLOYEE_APPOINTMENT;

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

import fi.arcusys.koku.av.AvCitizenServiceHandle;
import fi.arcusys.koku.av.AvEmployeeServiceHandle;
import fi.arcusys.koku.av.KokuAppointment;
import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.web.util.ModelWrapper;
import fi.arcusys.koku.web.util.ResponseStatus;
import fi.arcusys.koku.web.util.impl.ModelWrapperImpl;


/**
 * Shows appointment page and store the current query information on the jsp page
 * @author Jinhua Chen
 * Aug 4, 2011
 */
@Controller("singleAppointmentController")
@RequestMapping(value = "VIEW")
public class ShowAppointmentController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ShowAppointmentController.class);
	
	@Resource
	private ResourceBundleMessageSource messageSource;
	
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
	public ModelWrapper<KokuAppointment> model(
			@RequestParam String appointmentId,
			@RequestParam String currentPage,
			@RequestParam String taskType, 
			@RequestParam String keyword, 
			@RequestParam String orderType,
			@RequestParam(value = "targetPerson", required = false) String targetPerson, RenderRequest request) {
	
		// store parameters in session for returning page from form page	
		request.getPortletSession().setAttribute(ATTR_CURRENT_PAGE, currentPage, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_TASK_TYPE, taskType, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_KEYWORD, keyword, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_ORDER_TYPE, orderType, PortletSession.APPLICATION_SCOPE);
		
		ModelWrapper<KokuAppointment> model = null;		
		KokuAppointment app = null;
		try {
			if (taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN)
					|| taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN_OLD)) {
				AvCitizenServiceHandle handle = new AvCitizenServiceHandle();
				handle.setMessageSource(messageSource);
				app = handle.getAppointmentById(appointmentId, targetPerson);
			} else if(taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_EMPLOYEE)) {
				AvEmployeeServiceHandle handle = new AvEmployeeServiceHandle();
				handle.setMessageSource(messageSource);
				app = handle.getAppointmentById(appointmentId);
			}
			model = new ModelWrapperImpl<KokuAppointment>(app);
		} catch (KokuServiceException e) {
			LOG.error("Failed to show appointment details. appointmentId: '"+appointmentId + 
					"' username: '"+request.getUserPrincipal().getName()+"' taskType: '"+taskType + 
					"' keyword: '" + keyword + "'", e);
			model = new ModelWrapperImpl<KokuAppointment>(null, ResponseStatus.FAIL, e.getErrorcode());
		}
		return model;
	}	
	
}
