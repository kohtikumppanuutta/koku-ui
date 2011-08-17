package fi.arcusys.koku.palvelut.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.intalio.tempo.workflow.task.Task;
import org.springframework.web.portlet.mvc.AbstractController;

import fi.arcusys.koku.palvelut.model.client.FormHolder;
import fi.arcusys.koku.palvelut.util.MigrationUtil;
import fi.arcusys.koku.palvelut.util.TaskUtil;
import fi.arcusys.koku.palvelut.util.TokenUtil;
import fi.arcusys.koku.palvelut.util.URLUtil;

/**
 * @author Dmitry Kudinov (dmitry.kudinov@arcusys.fi)
 * Jul 21, 2011
 */
public abstract class FormHolderController extends AbstractController {

	private static Log log = LogFactory.getLog(FormHolderController.class);

	/**
	 * 
	 */
	public FormHolderController() {
		super();
	}

	protected List<FormHolder> getFormHoldersFromTasks(PortletRequest request) {
		String token = TokenUtil.getAuthenticationToken(request);
		//String formManagerUrl = "http://ouka-jboss2.mermit.fi:8080/xFormsManager/init";
		//String formManagerUrl = "/veera/xforms/init";
		List<Task> taskList = TaskUtil.getPIPATaskList(token);
		List<FormHolder> formList = new ArrayList<FormHolder>();
		for (Task task: taskList) {
				//String formManagerUrl = URLUtil.getFormManagerURLForTask(task);
				String taskFormURL = getFormUrlByTask(request, token, task);
				formList.add(new FormHolder(task.getDescription(), taskFormURL));
		}
		return formList;
	}

	protected FormHolder getFormHolderFromTask(PortletRequest request, String description) {
		String token = TokenUtil.getAuthenticationToken(request);
	
		List<Task> taskList = TaskUtil.getPIPATaskList(token);
		for (Task task : taskList) {
			if (task.getDescription().equals(description)) {
				String taskFormURL = getFormUrlByTask(request, token, task);
				try {
					String userName = MigrationUtil.getUser(request);
					Scanner scanner = new Scanner(description);
					scanner.useDelimiter(" ");
					String action = "";
					while (scanner.hasNext()) {
						action = action + scanner.next() + "_";
					}
					String actionString = action.substring(0, action.length());
					String customerId = getPortletContext().getInitParameter(
							"loggingCustomer");
					String applicationId = getPortletContext()
							.getInitParameter("loggingApplication");
	
					String message = customerId + " " + applicationId + " "
							+ userName + " Käyttäjä_avasi_lomakkeen_"
							+ actionString;
					log.warn(message);
				} catch (Exception e) {
				}
				return new FormHolder(description, taskFormURL);
			}
		}
		log.error("Didn't find any form!");
	
		return null;
	}

	private String getFormUrlByTask(PortletRequest request, String token,
			Task task) {
		return URLUtil.getFormURLForTask(task, token, request);
	}

}