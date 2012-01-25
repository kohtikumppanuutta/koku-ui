package fi.arcusys.koku.web;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.kv.model.KokuRequest;
import fi.arcusys.koku.kv.request.citizen.CitizenRequestHandle;
import fi.arcusys.koku.kv.request.employee.EmployeeRequestHandle;
import fi.arcusys.koku.util.Constants;
import fi.arcusys.koku.web.util.ModelWrapper;
import fi.arcusys.koku.web.util.ResponseStatus;
import fi.arcusys.koku.web.util.impl.ModelWrapperImpl;
import static fi.arcusys.koku.util.Constants.*;


/**
 * Shows request details page and store the current query information on the jsp page
 * @author Jinhua Chen
 * Jul 29, 2011
 */
@Controller("singleRequestController")
@RequestMapping(value = "VIEW")
public class ShowRequestController {
	
	private static final Logger LOG = Logger.getLogger(ShowRequestController.class);

	
	/**
	 * Shows request page
	 * @param response RenderResponse
	 * @return request page
	 */
	@RenderMapping(params = "myaction=showRequest")
	public String showPageView(RenderResponse response) {

		return VIEW_SHOW_REQUEST;
	}
		
	/**
	 * Creates data model integrated into the page and stores the page
	 * @param requestId request id
	 * @param currentPage current page id
	 * @param taskType task type requested
	 * @param keyword page parameter keyword
	 * @param orderType page parameter order type
	 * @param request RenderRequest
	 * @return request data model
	 */
	@ModelAttribute(value = "request")
	public ModelWrapper<KokuRequest> model(@RequestParam String requestId,
			@RequestParam String currentPage,@RequestParam String taskType, 
			@RequestParam String keyword, @RequestParam String orderType,
			RenderRequest request) {

		// store parameters in session for returning page from form page	
		request.getPortletSession().setAttribute(ATTR_CURRENT_PAGE, currentPage, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_TASK_TYPE, taskType, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_KEYWORD, keyword, PortletSession.APPLICATION_SCOPE);
		request.getPortletSession().setAttribute(ATTR_ORDER_TYPE, orderType, PortletSession.APPLICATION_SCOPE);
		
		ModelWrapper<KokuRequest> model = null;
		KokuRequest kokuRequest = null;
		try {
			if (taskType.equals(Constants.TASK_TYPE_REQUEST_VALID_EMPLOYEE)) {
				EmployeeRequestHandle reqhandle = new EmployeeRequestHandle();
				kokuRequest = reqhandle.getKokuRequestById(requestId);			
			}
			model = new ModelWrapperImpl<KokuRequest>(kokuRequest);
		} catch (KokuServiceException kse) {
			LOG.error("Failed to show request details. requestId: '"+requestId + 
					"' username: '"+request.getUserPrincipal().getName()+" taskType: '"+taskType + 
					"' keyword: '" + keyword + "'", kse);
			model = new ModelWrapperImpl<KokuRequest>(null, ResponseStatus.FAIL, kse.getErrorcode());
		}
		
		return model;
	}
	

}
