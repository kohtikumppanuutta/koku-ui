package fi.arcusys.koku.palvelut.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.intalio.tempo.workflow.task.Task;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import fi.arcusys.koku.palvelut.model.client.TaskHolder;
import fi.arcusys.koku.palvelut.util.TaskUtil;
import fi.arcusys.koku.palvelut.util.TokenUtil;
import fi.arcusys.koku.palvelut.util.URLUtil;

@Resource(mappedName = "configurationController")
public class ConfigurationController extends AbstractController {
	
	public static final String EDIT_ACTION = "configuration";
	
	@Override
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		System.out.println("handleRenderRequestInternal");
		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("taskList", getTaskHolders(request));
		ModelAndView mav = new ModelAndView(EDIT_ACTION, "model", map);
		mav.addObject("formList", getTaskHolders(request));
		mav.addObject("prefs", request.getPreferences());
		return mav;
	}
	
	@Override
	protected void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("handleActionRequestInternal");
		PortletPreferences prefs = request.getPreferences();
		System.out.println(prefs.getValue("showOnlyChecked", null));
		System.out.println(prefs.getValue("showOnlyForm", null));
		prefs.setValue("showOnlyChecked", request.getParameter("showOnlyChecked"));
		prefs.setValue("showOnlyForm", request.getParameter("showOnlyForm"));
		prefs.store();
		request.setAttribute("prefs", prefs);
	}
	
	private List<TaskHolder<Task>> getTaskHolders(PortletRequest request) {
		String token = TokenUtil.getAuthenticationToken(request);
		System.out.println(token);
		List<Task> taskList = TaskUtil.getPIPATaskList(token);
		System.out.println(taskList.size());
		List<TaskHolder<Task>> tasks = new ArrayList<TaskHolder<Task>>();
		for (Task task : taskList) {
			String taskFormURL = URLUtil
					.getFormURLForTask(task, token, request);
			tasks.add(new TaskHolder<Task>(task, taskFormURL));
		}
		return tasks;
	}
}
