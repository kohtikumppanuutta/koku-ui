package fi.arcusys.koku.palvelut.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

//@Resource(mappedName = "configurationController")
@Controller("configurationController")
@RequestMapping(value = "EDIT")
public class ConfigurationController { 
//extends AbstractController {
	
	public static final String EDIT_ACTION = "configuration";
	private static final Logger log = Logger.getLogger(ConfigurationController.class.getName());

	
	@RenderMapping
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		log.debug("handleRenderRequestInternal");
		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("taskList", getTaskHolders(request));
		ModelAndView mav = new ModelAndView(EDIT_ACTION, "model", map);
		try {
			mav.addObject("formList", getTaskHolders(request));
			mav.addObject("prefs", request.getPreferences());			
		} catch (Exception e) {
			log.error("Failure while trying to get TaskHolders. See following log for more information: ", e);
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
		log.debug("handleActionRequestInternal");
		PortletPreferences prefs = request.getPreferences();
		log.debug("showOnlyChecked: " + prefs.getValue("showOnlyChecked", null));
		log.debug("showOnlyForm: " + prefs.getValue("showOnlyForm", null));
		prefs.setValue("showOnlyChecked", request.getParameter("showOnlyChecked"));
		prefs.setValue("showOnlyForm", request.getParameter("showOnlyForm"));
		prefs.store();
		request.setAttribute("prefs", prefs);
	}
	
	private List<TaskHolder<Task>> getTaskHolders(PortletRequest request) {
		String token = TokenUtil.getAuthenticationToken(request);
		log.debug("Token: "+token);
		List<Task> taskList = TaskUtil.getPIPATaskList(token);
		log.debug("taskList size: "+ taskList.size());
		List<TaskHolder<Task>> tasks = new ArrayList<TaskHolder<Task>>();
		for (Task task : taskList) {
			String taskFormURL = URLUtil
					.getFormURLForTask(task, token, request);
			tasks.add(new TaskHolder<Task>(task, taskFormURL));
		}
		return tasks;
	}
}
