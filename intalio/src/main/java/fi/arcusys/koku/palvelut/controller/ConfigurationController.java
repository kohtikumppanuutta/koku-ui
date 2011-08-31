package fi.arcusys.koku.palvelut.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.apache.log4j.Logger;
import org.intalio.tempo.workflow.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.arcusys.koku.palvelut.model.client.TaskHolder;
import fi.arcusys.koku.palvelut.util.TaskUtil;
import fi.arcusys.koku.palvelut.util.TokenUtil;
import fi.arcusys.koku.palvelut.util.URLUtil;

@Controller("configurationController")
@RequestMapping(value = "EDIT")
public class ConfigurationController { 
	
	public static final String EDIT_ACTION = "configuration";
	private static final Logger LOG = Logger.getLogger(ConfigurationController.class.getName());

	
	@RenderMapping
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		LOG.debug("handleRenderRequestInternal");
		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("taskList", getTaskHolders(request));
		ModelAndView mav = new ModelAndView(EDIT_ACTION, "model", map);
		try {
			mav.addObject("formList", getTaskHolders(request));
			mav.addObject("prefs", request.getPreferences());			
		} catch (Exception e) {
			LOG.error("Failure while trying to get TaskHolders. See following log for more information: ", e);
			ModelAndView failureMav = new ModelAndView("failureView", "model", null);
			failureMav.addObject("prefs", request.getPreferences());
			return failureMav;
		}
		return mav;
	}
	
	@ActionMapping
	protected void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		// TODO Auto-generated method stub
		LOG.debug("handleActionRequestInternal");
		PortletPreferences prefs = request.getPreferences();
		LOG.debug("showOnlyChecked: " + prefs.getValue("showOnlyChecked", null));
		LOG.debug("showOnlyForm: " + prefs.getValue("showOnlyForm", null));
		prefs.setValue("showOnlyChecked", request.getParameter("showOnlyChecked"));
		prefs.setValue("showOnlyForm", request.getParameter("showOnlyForm"));
		prefs.store();
		request.setAttribute("prefs", prefs);
		
		/* Return back to VIEW mode */
		response.setPortletMode(PortletMode.VIEW);
        response.setWindowState(WindowState.NORMAL);
	}
	
	private List<TaskHolder<Task>> getTaskHolders(PortletRequest request) {
		String token = TokenUtil.getAuthenticationToken(request);
		LOG.debug("Token: "+token);
		List<Task> taskList = TaskUtil.getPIPATaskList(token);
		LOG.debug("taskList size: "+ taskList.size());
		List<TaskHolder<Task>> tasks = new ArrayList<TaskHolder<Task>>();
		for (Task task : taskList) {
			String taskFormURL = URLUtil
					.getFormURLForTask(task, token, request);
			tasks.add(new TaskHolder<Task>(task, taskFormURL));
		}
		return tasks;
	}
}
