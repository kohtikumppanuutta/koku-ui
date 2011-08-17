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
	
	private static Log logger = LogFactory.getLog(TaskUtil.class);

	/*
	 * Fetches the task list from the TaskManagementService web service for the given token.
	 * 
	 * @param token The participant authentication token.
	 */
	public static List<Task> getTaskList(String token) {
		logger.debug("Getting task list via TaskManagerService for participant token: " + token);
		List<Task> tasks = null;
		ITaskManagementService tms = getTaskManager(Configuration.getInstance().getTaskManagerServiceEndpoint(), token);
		try {
			tasks = Arrays.asList(tms.getTaskList());
		} catch (AuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tasks;
	}

	public static List<Task> getPIPATaskList(String token) {
		logger.debug("Getting PIPA task list via TaskManagerService for participant token: " + token);
		List<Task> tasks = null;
		ITaskManagementService tms = getTaskManager(Configuration.getInstance().getTaskManagerServiceEndpoint(), token);
		try {
			tasks = Arrays.asList(tms.getAvailableTasks("PIPATask",""));
		} catch (AuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tasks;
	}

	public static Task getTask(String token, String taskId) {
		logger.debug("Getting task with id=" + taskId + " via TaskManagerService for participant token: " + token);
		Task task = null;
		ITaskManagementService tms = getTaskManager(Configuration.getInstance().getTaskManagerServiceEndpoint(), token);
		try {
			task = tms.getTask(taskId);
		} catch (AuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnavailableTaskException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return task;
	}
	
    protected static ITaskManagementService getTaskManager(String endpoint, String token){
    	return new RemoteTMSFactory(endpoint, token).getService();
    }	

}
