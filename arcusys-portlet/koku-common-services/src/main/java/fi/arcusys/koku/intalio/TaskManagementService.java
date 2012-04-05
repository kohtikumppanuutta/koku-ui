package fi.arcusys.koku.intalio;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.intalio.tms.CountAvailableTasksRequest;
import fi.arcusys.intalio.tms.GetAvailableTasksRequest;
import fi.arcusys.intalio.tms.GetAvailableTasksResponse;
import fi.arcusys.intalio.tms.GetTaskRequest;
import fi.arcusys.intalio.tms.GetTaskResponse;
import fi.arcusys.intalio.tms.InvalidInputMessageFault_Exception;
import fi.arcusys.intalio.tms.InvalidParticipantTokenFault_Exception;
import fi.arcusys.intalio.tms.Task;
import fi.arcusys.intalio.tms.TaskManagementServices;
import fi.arcusys.intalio.tms.TaskMetadata;
import fi.arcusys.intalio.token.TokenService;
import fi.arcusys.koku.exceptions.IntalioAuthException;
import fi.arcusys.koku.util.PropertiesUtil;
import fi.koku.settings.KoKuPropertiesUtil;

/**
 * Handles tasks processing via intalio web services
 * @author Jinhua Chen
 * May 9, 2011
 */
public class TaskManagementService {
	
	private static final Logger LOG = Logger.getLogger(TaskManagementService.class);		
	private static final URL TOKEN_WSDL_LOCATION;
	private static final URL TMS_WSDL_LOCATION;
	
	static {
		try {
			String taskmanagerService = KoKuPropertiesUtil.get("TaskManagerService");
			String tokenService = KoKuPropertiesUtil.get("TokenService");
			
			LOG.info("TaskManagerService WSDL location: "+taskmanagerService);
			LOG.info("TokenService WSDL location: "+tokenService);
			TMS_WSDL_LOCATION =  new URL(taskmanagerService);
			TOKEN_WSDL_LOCATION = new URL(tokenService);
		} catch (MalformedURLException e) {
			LOG.error("Failed to create TaskManger WSDL url! Given URL address is not valid!");
			throw new ExceptionInInitializerError(e);
		}
	}
	
	/**
	 * Constructor
	 */
	public TaskManagementService() {

	}

	/**
	 * Gets participant token from intalio bpms server. The server authenticates 
	 * user by username and password, then generates token.
	 * @param username username of intalio user
	 * @param password password of intalio user
	 * @return Intalio participant token
	 */
	public String getParticipantToken(String username, String password) throws IntalioAuthException {
		String participantToken = null;
		String wsdlLocation = null;
		try {     
			TokenService ts = new TokenService(TOKEN_WSDL_LOCATION);
			
			participantToken = ts.getService().authenticateUser(username, password);
		} catch (Exception e) {
			throw new IntalioAuthException("Trying to get intalio token failed: " + e.getMessage(), e);	
		}
		return participantToken;
	}

	/**
	 * Gets Tasks from WS /axis2/services/TaskManagementServices using
	 * getAvailableTasks operation.
	 * @param participantToken intalio participant token
	 * @param taskType the intalio task type: "PATask", "PIPATask", "Notification"
	 * @param subQuery the sql string for intalio tasks database
	 * @param first the beginning index of the tasks 
	 * @param max the maximum tasks to be queried
	 * @return a list of intalio tasks
	 */
	public List<TaskMetadata> getAvailableTasks(String participantToken,
			String taskType, String subQuery, String first, String max) {
		TaskManagementServices tms;
		List<TaskMetadata> taskList = new ArrayList<TaskMetadata>();
				
		try {
			tms = new TaskManagementServices(TMS_WSDL_LOCATION);
			GetAvailableTasksRequest getAvailTasksReq = new GetAvailableTasksRequest();
			getAvailTasksReq.setParticipantToken(participantToken);
			getAvailTasksReq.setTaskType(taskType);
			getAvailTasksReq.setSubQuery(subQuery);
			getAvailTasksReq.setFirst(first);
			getAvailTasksReq.setMax(max);			
			GetAvailableTasksResponse availTasksRes;
			availTasksRes = tms.getTaskManagementServicesSOAP().getAvailableTasks(getAvailTasksReq);
			taskList = availTasksRes.getTask();
		} catch (InvalidParticipantTokenFault_Exception e) {
			LOG.error("getAvailableTasks - InvalidParticipantTokenFault_Exception: "+e.getMessage());
		} catch (InvalidInputMessageFault_Exception e2) {
			LOG.error("getAvailableTasks - InvalidInputMessageFault_Exception: "+e2.getMessage());
		} catch (Exception e1) {
			LOG.error("getAvailableTasks - Intalio exception: "+e1.getMessage() ,e1);
		}
		
		return taskList;
	}

	/**
	 * Gets the total number of tasks
	 * @param participantToken intalio participant token
	 * @param taskType the intalio task type: "PATask", "PIPATask", "Notification"
	 * @param subQuery the sql string for intalio tasks database
	 * @return total number, returns '0' if no results found
	 */
	public String getTotalTasksNumber(String participantToken, String taskType, String subQuery) {
		String totalNum = "0";
		TaskManagementServices tms;
		
		try {
			tms = new TaskManagementServices(TMS_WSDL_LOCATION);
			CountAvailableTasksRequest countAvailTasksReq = new CountAvailableTasksRequest();
			countAvailTasksReq.setParticipantToken(participantToken);
			countAvailTasksReq.setTaskType(taskType);
			countAvailTasksReq.setSubQuery(subQuery);
			totalNum = tms.getTaskManagementServicesSOAP().countAvailableTasks(countAvailTasksReq);
		} catch (InvalidParticipantTokenFault_Exception e) {
			LOG.error("getTotalTasksNumber - InvalidParticipantTokenFault_Exception: ", e);
		} catch (InvalidInputMessageFault_Exception e2) {
			LOG.error("getTotalTasksNumber - InvalidInputMessageFault_Exception: ", e2);
		} catch (Exception e1) {
			LOG.error("getTotalTasksNumber - Intalio exception: ", e1);
		}
			
		
		return totalNum;
	}

	/**
	 * Gets a Intalio task with task id and participant token
	 * @param taskId intalio task id
	 * @param participantToken intalio participant token
	 * @return Intalio task
	 */
	public Task getTask(String taskId, String participantToken) {
		Task task = null;
		TaskManagementServices tms;
		
		try {
			tms = new TaskManagementServices(TMS_WSDL_LOCATION);		
			GetTaskRequest req = new GetTaskRequest();
			req.setTaskId(taskId);
			req.setParticipantToken(participantToken);
			GetTaskResponse res = tms.getTaskManagementServicesSOAP().getTask(req);
			task = res.getTask();
		} catch (InvalidParticipantTokenFault_Exception e) {
			LOG.error("getTask - InvalidParticipantTokenFault_Exception: "+e.getMessage());
		} catch (InvalidInputMessageFault_Exception e2) {
			LOG.error("getTask - InvalidInputMessageFault_Exception: "+e2.getMessage());
		} catch (Exception e1) {
			LOG.error("getTask - Intalio exception: "+e1.getMessage());
		}
		
		return task;		
	}
	

}
