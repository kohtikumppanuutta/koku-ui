package fi.arcusys.koku.palvelut.controller;

import java.util.List;
import java.util.Scanner;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.intalio.tempo.workflow.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import fi.arcusys.koku.palvelut.model.client.FormHolder;
import fi.arcusys.koku.palvelut.util.MigrationUtil;
import fi.arcusys.koku.palvelut.util.TaskUtil;
import fi.arcusys.koku.palvelut.util.TokenUtil;
import fi.arcusys.koku.palvelut.util.URLUtil;

/**
 * @author Dmitry Kudinov (dmitry.kudinov@arcusys.fi)
 * Jul 21, 2011
 */
public abstract class FormHolderController {
	@Autowired(required = false)
	private PortletContext portletContext;
	
	private static final Log LOG = LogFactory.getLog(FormHolderController.class);

	public FormHolderController() {
		super();
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
					
					// Does not work.. 
					String actionString = action.substring(0, action.length());
					String customerId = getPortletContext().getInitParameter(
							"loggingCustomer");
					String applicationId = getPortletContext()
							.getInitParameter("loggingApplication");
	
					String message = customerId + " " + applicationId + " "
							+ userName + " Käyttäjä_avasi_lomakkeen_"
							+ actionString;
					LOG.warn(message);
				} catch (Exception e) {
					LOG.error("Something went wrong" +e);
				}
				return new FormHolder(description, taskFormURL);
			}
		}
		LOG.error("Didn't find any form!");
		return null;
	}
	
	private PortletContext getPortletContext() {
		return this.portletContext;
	}

	private String getFormUrlByTask(PortletRequest request, String token, Task task) {
		return URLUtil.getFormURLForTask(task, token, request);
	}

}