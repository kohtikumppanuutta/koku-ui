package fi.arcusys.koku.palvelut.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static fi.arcusys.koku.util.Constants.*;

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
	
	private static final Logger LOG = Logger.getLogger(ConfigurationController.class.getName());
	public static final String EDIT_ACTION = "configuration";
	public static final String JBOSS_PORTAL = "JBoss Portal 2.7";
	
	@RenderMapping
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		LOG.debug("handleRenderRequestInternal");
		Map<String, Object> map = new HashMap<String, Object>();
		ModelAndView mav = new ModelAndView(EDIT_ACTION, "model", map);
		String portalInfo = request.getPortalContext().getPortalInfo();
		
		if (portalInfo.startsWith(JBOSS_PORTAL)) {
			mav.addObject(ATTR_PORTAL_ID, PORTAL_JBOSS);
		} else {
			mav.addObject(ATTR_PORTAL_ID, PORTAL_GATEIN);
		}
		
		try {
			mav.addObject(ATTR_FORM_LIST, getTaskHolders(request));
			mav.addObject(ATTR_PREFERENCES, request.getPreferences());			
		} catch (Exception e) {
			LOG.error("Failure while trying to get TaskHolders. See following log for more information: ", e);
			ModelAndView failureMav = new ModelAndView("failureView", "model", null);
			failureMav.addObject(ATTR_PREFERENCES, request.getPreferences());
			return failureMav;
		}
		return mav;
	}
	
	@ActionMapping(params="action=config")
	protected void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		LOG.debug("handleActionRequestInternal - Save settings");
		PortletPreferences prefs = request.getPreferences();
		prefs.setValue(PREF_SHOW_ONLY_CHECKED, request.getParameter(PREF_SHOW_ONLY_CHECKED));
		prefs.setValue(PREF_SHOW_ONLY_FORM_BY_ID, request.getParameter(PREF_SHOW_ONLY_FORM_BY_ID));
		prefs.setValue(PREF_SHOW_TASKS_BY_ID, Boolean.TRUE.toString());
		prefs.store();
		LOG.debug("showOnlyChecked: " + prefs.getValue(PREF_SHOW_ONLY_CHECKED, null));
		LOG.debug("showOnlyForm: " + prefs.getValue(PREF_SHOW_ONLY_FORM_BY_ID, null));
		LOG.debug("showTasksById: "+ prefs.getValue(PREF_SHOW_TASKS_BY_ID, Boolean.TRUE.toString()));
		request.setAttribute(ATTR_PREFERENCES, prefs);
		 
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
