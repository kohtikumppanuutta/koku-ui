package fi.arcusys.koku.palvelut.controller;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.intalio.tempo.workflow.task.Task;

import fi.arcusys.koku.palvelut.model.client.FormHolder;
import fi.arcusys.koku.palvelut.util.TaskUtil;
import fi.arcusys.koku.palvelut.util.TokenResolver;
import fi.arcusys.koku.palvelut.util.URLUtil;


/**
 * @author Dmitry Kudinov (dmitry.kudinov@arcusys.fi)
 * Jul 21, 2011
 */
public abstract class FormHolderController
{
//	@Autowired(required = false)
//	private PortletContext portletContext;
	
	private static final Log LOG = LogFactory.getLog(FormHolderController.class);

	public FormHolderController() {
		super();
	}

	protected List<FormHolder> getFormHoldersFromTasks(PortletRequest request) {
		TokenResolver tokenResolver = new TokenResolver();
		String token = tokenResolver.getAuthenticationToken(request);
		List<Task> taskList = TaskUtil.getPIPATaskList(token);
		List<FormHolder> formList = new ArrayList<FormHolder>();
		for (Task task: taskList) {
				String taskFormURL = getFormUrlByTask(request, token, task);
				formList.add(new FormHolder(task.getDescription(), taskFormURL));
		}
		return formList;
	}

	protected FormHolder getFormHolderFromTask(PortletRequest request, String description) {
		TokenResolver tokenResolver = new TokenResolver();
		String token = tokenResolver.getAuthenticationToken(request);
	
		List<Task> taskList = TaskUtil.getPIPATaskList(token);
		for (Task task : taskList) {
			if (task.getDescription().equals(description)) {
				String taskFormURL = getFormUrlByTask(request, token, task);
//				try {
//					// String userName = MigrationUtil.getUser(request);
//					Scanner scanner = new Scanner(description);
//					scanner.useDelimiter(" ");
//					String action = "";
//					while (scanner.hasNext()) {
//						action = action + scanner.next() + "_";
//					}
//					
//					// Does not work.. 
//					// String actionString = action.substring(0, action.length());
//					// String customerId = getPortletContext().getInitParameter("loggingCustomer");
//					// String applicationId = getPortletContext().getInitParameter("loggingApplication");
//				} catch (Exception e) {
//					LOG.error("Something went wrong when parsing task description. Username: '"+request.getUserPrincipal().getName()+"' Description: '"+description+"'", e);
//				}
				return new FormHolder(description, taskFormURL);
			}
		}
		LOG.error("Didn't find any form! Username: '"+request.getUserPrincipal().getName()+"'");	
		return null;
	}
	
	private String getFormUrlByTask(PortletRequest request, String token, Task task) {
		return URLUtil.getFormURLForTask(task, token, request);
	}

}