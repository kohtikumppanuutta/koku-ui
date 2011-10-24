package fi.arcusys.koku.palvelut.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.intalio.tempo.workflow.auth.AuthException;
import org.intalio.tempo.workflow.task.Task;
import org.intalio.tempo.workflow.tms.ITaskManagementService;
import org.intalio.tempo.workflow.tms.UnavailableTaskException;
import org.intalio.tempo.workflow.tms.client.RemoteTMSFactory;

import fi.arcusys.koku.palvelut.bean.Configuration;

public class TaskUtil {
	
	private static Log LOG = LogFactory.getLog(TaskUtil.class);
	
	private TaskUtil() {
		// No need to instantiate this class
	}

	/*
	 * Fetches the task list from the TaskManagementService web service for the given token.
	 * 
	 * @param token The participant authentication token.
	 */
	public static List<Task> getTaskList(String token) {
		LOG.debug("Getting task list via TaskManagerService for participant token: " + token);
		List<Task> tasks = null;
		ITaskManagementService tms = getTaskManager(Configuration.getInstance().getTaskManagerServiceEndpoint(), token);
		try {
			tasks = Arrays.asList(tms.getTaskList());
		} catch (AuthException e) {
			// TODO Auto-generated catch block
			LOG.error("Authentication error", e);
		}
		return tasks;
	}

	public static List<Task> getPIPATaskList(String token) {
		LOG.debug("Getting PIPA task list via TaskManagerService for participant token: " + token);
		List<Task> tasks = null;
		ITaskManagementService tms = getTaskManager(Configuration.getInstance().getTaskManagerServiceEndpoint(), token);
		try {
			tasks = Arrays.asList(tms.getAvailableTasks("PIPATask",""));
		} catch (AuthException e) {
			LOG.error("Authentication error", e);
		}
		return tasks;
	}

	public static Task getTask(String token, String taskId) {
		LOG.debug("Getting task with id = " + taskId + " via TaskManagerService for participant token: " + token);
		Task task = null;
		ITaskManagementService tms = getTaskManager(Configuration.getInstance().getTaskManagerServiceEndpoint(), token);
		try {
			task = tms.getTask(taskId);
		} catch (AuthException e) {
			LOG.error("Authentication error ", e);
		} catch (UnavailableTaskException e) {
			LOG.error("UnavailableTaskException ", e);
		}
		return task;
	}
	
	/**
	 * Returns task by given description. Very inefficient, because we can't 
	 * save form IDs (Portlet restriction)
	 * 
	 * @param token
	 * @param description
	 * @return Task
	 */
	public static Task getTaskByDescription(String token, String description) {
		LOG.debug("Getting PIPA task list via TaskManagerService for participant token: " + token);
		Task selectedTask = null;
		ITaskManagementService tms = getTaskManager(Configuration.getInstance().getTaskManagerServiceEndpoint(), token);
		try {
			List<Task> tasks = null;
			tasks = Arrays.asList(tms.getAvailableTasks("PIPATask",""));
			if (!tasks.isEmpty()) {
				for (Task task : tasks) {
					if (task.getDescription().equals(description)) {
						selectedTask = task;
						break;
					}
				}
			}		
		} catch (AuthException e) {
			LOG.error("Authentication error", e);
		}
		
		if (selectedTask == null) {
			LOG.error("UnavailableTaskException. Coulnd't find Task by given description:  '"+ description + "'");
		}		
		return selectedTask;
	}
	
	
    protected static ITaskManagementService getTaskManager(String endpoint, String token){
    	return new RemoteTMSFactory(endpoint, token).getService();
    }	

}
