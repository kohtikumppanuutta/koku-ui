package fi.arcusys.koku.intalio;

import static fi.arcusys.koku.util.Constants.DATE_FORMAT;
import static fi.arcusys.koku.util.Constants.TIME_ZONE;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fi.arcusys.intalio.tms.Task.Input;
import fi.arcusys.intalio.tms.TaskMetadata;
import fi.arcusys.koku.exceptions.IntalioAuthException;
import fi.arcusys.koku.util.Properties;
import fi.arcusys.koku.util.TaskUtil;

/**
 * Handles the intalio task processing including querying tasks, formatting task
 * to be presented to web
 * @author Jinhua Chen
 * May 9, 2011
 */

public class TaskHandle {
	
	private static final Logger logger = Logger.getLogger(TaskHandle.class);
	// TODO: We probably need some sort filter here?
	public static final String TASKMGR_REQUESTS_FILTER = "";	
	private static final String LOCAL_AJAXFORMS_WEB_APP_URL_PART = "/palvelut-portlet/ajaxforms/";
	private static final String ADDRESS_REGEX = "http://.+/gi/";

	private final TaskManagementService taskMngServ = new TaskManagementService();
	
	private String message;
	private String participantToken;
	private String username;
	
	/**
	 * Constructor and initialization
	 */
	public TaskHandle() {
		this.message = "";
		this.participantToken = null;
	}

	/**
	 * Constructor with intalio participant token and username
	 */
	public TaskHandle(String token, String username) {
		this.participantToken = token;
		this.username = username;
	}

	/**
	 * Gets available tasks by the given parameters and return task list
	 * @param taskType the intalio task type
	 * @param keyword the keyword for searching/filetering
	 * @param orderType order type of tasks
	 * @param first the beginning index of the tasks 
	 * @param max the maximum tasks to be queried
	 * @return available task list
	 */
	public List<Task> getTasksByParams(int taskType, String keyword, 
			String orderType, String first, String max) {
		String taskTypeStr = TaskUtil.getTaskType(taskType);
		String subQuery = "";				
		subQuery = createTaskSubQuery(taskType, keyword, orderType);
		return getTasks(taskTypeStr, subQuery, first, max);
	}
	
	/**
	 * Gets tasks from task management service
	 * @param taskType the intalio task type
	 * @param subQuery the sql string for intalio tasks database
	 * @param first the beginning index of the tasks 
	 * @param max the maximum tasks to be queried
	 * @return a list of tasks
	 */
	public List<Task> getTasks(String taskType, String subQuery, String first, String max) {
		List<TaskMetadata> tasklist = taskMngServ.getAvailableTasks(participantToken, taskType, subQuery, 
        		first, max);
		return createTasks(tasklist, participantToken);
	}
	
	public Task getTask(String taskId, String token) {
		fi.arcusys.intalio.tms.Task task = taskMngServ.getTask(taskId, token);
		return createTask(task.getMetadata(), task.getInput());
	}

	/**
	 * Gets task status such as 'READY', 'CLAIMED', 'COMPLETED'
	 * @param taskId intalio task id
	 * @return the intalio task status
	 */
	public String getTaskStatus(String taskId) {
		return taskMngServ.getTask(taskId, participantToken).getMetadata().getTaskState();
	}
	
	/**
	 * Creates task model to be shown in portlet from intalio task
	 * @param tasklist a list of intalio tasks
	 * @return formatted task list to be presented on web
	 */
	private List<Task> createTasks(List<TaskMetadata> tasklist, String token) {
		List<Task> myTasklist = new ArrayList<Task>();
		/* Unfortunately getTasks WS call doesn't contain Input object which includes information
		 * what we want (like sender name). Only way seems to be call task details and this will generate
		 * more WS calls (default is 10). =\ 
		 * 
		 * Better ideas?
		 * */
		for (TaskMetadata task : tasklist) {
			fi.arcusys.intalio.tms.Task taskDetails = taskMngServ.getTask(task.getTaskId(), token);
			myTasklist.add(createTask(taskDetails.getMetadata(), taskDetails.getInput()));
		}
		return myTasklist;
	}
	
	private fi.arcusys.koku.intalio.Task createTask(TaskMetadata task, fi.arcusys.intalio.tms.Task.Input input) {
		if (task == null) {
			return null;
		}
		fi.arcusys.koku.intalio.Task myTask = new fi.arcusys.koku.intalio.Task();
		myTask.setDescription(task.getDescription());
		
		if (task.getTaskState() != null) {
			myTask.setState(task.getTaskState().toString());
		} else {
			myTask.setState("");
		}
		myTask.setCreationDate(formatTaskDate(task.getCreationDate()));
		myTask.setLink(createTaskLink(task));
		myTask.setSenderName(getSenderNameFromTaskInput(task, input));
		return myTask;
	}
	
	/**
	 * Returns task sender name (if available) 
	 * 
	 * @param task
	 * @param input
	 * @return sender name
	 */
	private String getSenderNameFromTaskInput(TaskMetadata task, Input input) {
		final String descriptionName = task.getDescription();
		if (descriptionName.startsWith(Properties.RECEIVED_REQUESTS_FILTER)) {
			// Uusi pyyntö
			return getSenderName(input, "User_SenderDisplay");
		} else if (descriptionName.contains(Properties.RECEIVED_INFO_REQUESTS_FILTER)) {
			// Uusi tietopyyntö
			return getSenderName(input, "Perustiedot_Lahettaja");
		} else if (descriptionName.endsWith(Properties.RECEIVED_WARRANTS_FILTER)) {			
			// Vastaanotetut valtakirjat
			return getSenderName(input, "Tiedot_LahettajaDisplay");
		} else {
			// Hm?
			return null;
		}
	}
	
	private String getSenderName(final fi.arcusys.intalio.tms.Task.Input input, final String nodename) {
		if (input == null || input.getAny() == null || input.getAny().isEmpty()) {
			return null;
		}
		final Object object = input.getAny().get(0);
		if (!(object instanceof Element)) {
			return null;
		}
		final Element element = ((Element)object);		
		final NodeList senderNameList = element.getElementsByTagName(nodename);
		if (senderNameList == null || senderNameList.getLength() == 0) {
			return null;
		}
		final Node senderNameNode = senderNameList.item(0);
		if (senderNameNode.getFirstChild() == null)  {
			return null;
		}
		return senderNameNode.getFirstChild().getNodeValue();
	}
	
	/**
	 * Formats the task date with given format and Helsinki timezone
	 * @param xmlGregorianCalendar
	 * @return formatted date string
	 */
	public String formatTaskDate(XMLGregorianCalendar xmlGregorianCalendar) {
		Calendar cal = xmlGregorianCalendar.toGregorianCalendar();
		SimpleDateFormat dataformat = new SimpleDateFormat(DATE_FORMAT);
		dataformat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
		return dataformat.format(cal.getTime());
	}
	
	/**
	 * Creates form operation link of task
	 * @param task intalio task object
	 * @return intalio task form string
	 */
	public String createTaskLink(TaskMetadata task) {
		String link = "";		
		String taskType = "";
        Object[] params = null;
        String type = task.getTaskType().toString();
		
		if (type.equals("ACTIVITY")) { // tasks	
			taskType = TaskUtil.TASK_TYPE;			
		} else if (type.equals("NOTIFICATION")) { // notifications
			taskType = TaskUtil.NOTIFICATION_TYPE;			
		} else if (type.equals("INIT")) { // processes
			taskType = TaskUtil.PROCESS_TYPE;
		} else {
			taskType = TaskUtil.TASK_TYPE;
		}
		String url = task.getFormUrl().toString();
		url = url.replaceFirst(ADDRESS_REGEX, LOCAL_AJAXFORMS_WEB_APP_URL_PART);
		
		try {
			params = new Object[] { url, task.getTaskId(), taskType, URLEncoder.encode(url, "UTF-8"), participantToken,
			        URLEncoder.encode(username, "UTF-8"), false };
		} catch (UnsupportedEncodingException e) {
			logger.error("Unsupported Encoding Exception");
		}
		link =  MessageFormat.format("{0}?id={1}&type={2}&url={3}&token={4}&user={5}&claimTaskOnOpen={6}", params);
		return link;
	}

	/**
	 * Gets total tasks number
	 * @param taskType the intalio task type
	 * @param subQuery the sql string for intalio tasks database
	 * @return total number of total tasks
	 */
	public int getTotalTasksNumber(int taskType, String keyword) {
		int totalNum = 0;
		String subQuery;
		String totalNumStr;
		String taskTypeStr;
		taskTypeStr = TaskUtil.getTaskType(taskType);
		subQuery = createTotalNumSubQuery(taskType, keyword);
		totalNumStr = taskMngServ.getTotalTasksNumber(participantToken, taskTypeStr, subQuery);
		totalNum = Integer.parseInt(totalNumStr);
		return totalNum;		
	}
	
	public int getTasksTotalNumber(final String keywordFilter) {
		final String filter = (keywordFilter != null) ? keywordFilter : ""; 
		return Integer.valueOf(taskMngServ.getTotalTasksNumber(participantToken, TaskUtil.TASK_TYPE, createTotalNumSubQuery(TaskUtil.TASK, filter)));
	}
	
	public int getRequestsTasksTotalNumber() {
		return getTasksTotalNumber("");
		// return Integer.valueOf(taskMngServ.getTotalTasksNumber(participantToken, TaskUtil.TASK_TYPE, createTotalNumSubQuery(TaskUtil.TASK, TASKMGR_REQUESTS_FILTER)));
	}
	
	/**
	 * Creates subquery to get total number of tasks
	 * @param taskType the intalio task type
	 * @param keyword the keyword for searching/filetering
	 * @return subquery the sql string for intalio tasks database
	 */
	public String createTotalNumSubQuery(int taskType, String keyword) {
		String subQuery;
		switch (taskType) {
		case TaskUtil.TASK:
			subQuery = "(T._state = TaskState.READY OR T._state = TaskState.CLAIMED)" + " AND T._description like '%" + keyword + "%'";
			break;			
		case TaskUtil.NOTIFICATION:
			subQuery = "T._state = TaskState.READY" + " AND T._description like '%" + keyword + "%'";
			break;
		case TaskUtil.PROCESS:
			subQuery = "T._description like '%" + keyword + "%'";;
			break;
		default:
			subQuery = "";
			break;
		}
		return subQuery;
	}
	
	/**
	 * Creates subquery to get available tasks
	 * @param taskType the intalio task type
	 * @param keyword the keyword for searching/filetering
	 * @param orderType order type of tasks
	 * @return query string for intalio database
	 */
	public String createTaskSubQuery(int taskType, String keyword, String orderType) {
		String subQuery;
		String orderTypeStr = getOrderTypeStr(orderType);
		switch (taskType) {

		case TaskUtil.TASK:
			subQuery = "(T._state = TaskState.READY OR T._state = TaskState.CLAIMED)" 
				+ " AND T._description like '%" + keyword + "%'"
				+ " ORDER BY " + orderTypeStr;
			break;
		case TaskUtil.NOTIFICATION:
			subQuery = "T._state = TaskState.READY" 
				+ " AND T._description like '%" + keyword + "%'"
				+ " ORDER BY " + orderTypeStr;
			break;
		case TaskUtil.PROCESS:
			subQuery = "T._description like '%" + keyword + "%'" 
				+ " ORDER BY " + orderTypeStr;
			break;
		default:
			subQuery = "";
			break;
		}

		return subQuery;

	}
	
	/**
	 * Gets query order type according to order string from jsp page
	 * @param orderType order type of tasks
	 * @return order type query for intalio tasks
	 */
	public String getOrderTypeStr(String orderType) {
		String orderTypeStr;
		
		if(orderType.equals("description_desc")) {
			orderTypeStr = "T._description DESC";
		}else if(orderType.equals("description_asc")) {
			orderTypeStr = "T._description ASC";
		}else if(orderType.equals("state_desc")) {
			orderTypeStr = "T._state DESC";
		}else if(orderType.equals("state_asc")) {
			orderTypeStr = "T._state ASC";
		}else if(orderType.equals("creationDate_desc")) {
			orderTypeStr = "T._creationDate DESC";
		}else if(orderType.equals("creationDate_asc")) {
			orderTypeStr = "T._creationDate ASC";
		}else {
			orderTypeStr = "T._creationDate DESC";
		}
		
		return orderTypeStr;
	} 
	
	/**
	 * Gets token authenticated by username and password
	 * @param username username of intalio user
	 * @param password password of intalio user
	 * @return intalio participant token
	 */
	public String getTokenByUser(String username, String password) throws IntalioAuthException  {
		String token = null;
		token = taskMngServ.getParticipantToken(username, password);
		return token;
	}

	/**
	 * Gets participant token
	 * @return intalio participant token
	 */
	public String getToken() {
		return participantToken;
	}

	/**
	 * Sets participant token 
	 * @param token intalio participant token
	 */
	public void setToken(String token) {
		this.participantToken = token;
	}

	/**
	 * Shows handling message e.g. error message
	 * @return message log
	 */
	public String getMessage() {
		return message;
	}



}
