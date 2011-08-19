package fi.koku.taskmanager.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utilities used in the task manager
 * @author Jinhua Chen
 * May 10, 2011
 */
public class TaskUtil {
	
	private TaskUtil() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }
	
	/**
	 * Superuser contains username and participant token of intalio server
	 */
	public final static int PAGE_NUMBER = 10; // number of tasks in one page
	public final static int TASK = 1;
	public final static int NOTIFICATION = 2;
	public final static int PROCESS = 3;
	public final static String TASK_TYPE = "PATask";
	public final static String NOTIFICATION_TYPE = "Notification";
	public final static String PROCESS_TYPE = "PIPATask";
	// liferay variable for login username and password
	public final static String LOGIN_USERNAME_VAR = "_58_login";
	public final static String LOGIN_PASSWORD_VAR = "_58_password";
	// store some values that can not be stored in session
	private static Map<String, String> TASK_HASHMAP = new ConcurrentHashMap<String, String>();

	/**
	 * Gets intalio task type string according to the integer task type
	 * @param taskType intalio task type in Integer
	 * @return intalio task type as in database
	 */
	public static String getTaskType(int taskType) {
		
		switch (taskType) {
		
		case TaskUtil.TASK:
			return TaskUtil.TASK_TYPE;
		case TaskUtil.NOTIFICATION:
			return TaskUtil.NOTIFICATION_TYPE;
		case TaskUtil.PROCESS:
			return TaskUtil.PROCESS_TYPE;
		default:
			return TaskUtil.TASK_TYPE;
		}
		
	}
	
	/**
	 * Adds username and its participant token
	 * @param username current logged in user
	 * @param token participant token
	 */
	public static void addToken(String username, String token) {
		TaskUtil.TASK_HASHMAP.put(username, token);
	}
	
	/**
	 * Gets the participant token of user
	 * @param username
	 * @return participant token
	 */
	public static String getToken(String username) {
		return TaskUtil.TASK_HASHMAP.get(username);
	}
	
	/**
	 * Deletes the participant token from hashmap
	 * @param username current logged in user
	 */
	public static void removeToken(String username) {
		TaskUtil.TASK_HASHMAP.remove(username);
	}
	

}
