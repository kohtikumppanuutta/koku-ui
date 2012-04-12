package fi.arcusys.koku.intalio;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.BindingProvider;

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
import fi.arcusys.intalio.tms.TaskManagementServicesPortType;
import fi.arcusys.intalio.tms.TaskMetadata;
import fi.arcusys.intalio.token.TokenService;
import fi.arcusys.intalio.token.TokenServicePortType;
import fi.arcusys.koku.exceptions.IntalioAuthException;
import fi.arcusys.koku.util.Properties;

/**
 * Handles tasks processing via intalio web services
 * @author Jinhua Chen
 * May 9, 2011
 */
public class TaskManagementService {
	
	private static final Logger LOG = Logger.getLogger(TaskManagementService.class);		
	
	private final TokenServicePortType tokenService;
	private final TaskManagementServicesPortType  taskMgrService;
	
	/**
	 * Constructor
	 */
	public TaskManagementService() {
		final TokenService ts = new TokenService();
		final TaskManagementServices tms = new TaskManagementServices();

		tokenService = ts.getService();
		taskMgrService = tms.getTaskManagementServicesSOAP();
		((BindingProvider)tokenService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, Properties.INTALIO_TOKEN_SERVICE);
		((BindingProvider)taskMgrService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, Properties.INTALIO_TASKMGR_SERVICE);		
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
		try {			
			participantToken = tokenService.authenticateUser(username, password);
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
		
		List<TaskMetadata> taskList = new ArrayList<TaskMetadata>();				
		try {
			GetAvailableTasksRequest getAvailTasksReq = new GetAvailableTasksRequest();
			getAvailTasksReq.setParticipantToken(participantToken);
			getAvailTasksReq.setTaskType(taskType);
			getAvailTasksReq.setSubQuery(subQuery);
			getAvailTasksReq.setFirst(first);
			getAvailTasksReq.setMax(max);
			GetAvailableTasksResponse availTasksRes;
			availTasksRes = taskMgrService.getAvailableTasks(getAvailTasksReq);
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
		try {
			CountAvailableTasksRequest countAvailTasksReq = new CountAvailableTasksRequest();
			countAvailTasksReq.setParticipantToken(participantToken);
			countAvailTasksReq.setTaskType(taskType);
			countAvailTasksReq.setSubQuery(subQuery);
			totalNum = taskMgrService.countAvailableTasks(countAvailTasksReq);
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
		try {
			GetTaskRequest req = new GetTaskRequest();
			req.setTaskId(taskId);
			req.setParticipantToken(participantToken);
			GetTaskResponse res = taskMgrService.getTask(req);
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
