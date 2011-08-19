package fi.arcusys.oulu.intalio;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
import fi.arcusys.intalio.tms.UnavailableTaskFault_Exception;
import fi.arcusys.intalio.token.TokenService;

/**
 * Handles tasks processing via intalio web services
 * @author Jinhua Chen
 * May 9, 2011
 */
public class TaskManagementService {
	
	private final  URL TMS_WSDL_LOCATION = getClass().getClassLoader().getResource("TaskManagement.wsdl");
	private final  URL TOKEN_WSDL_LOCATION = getClass().getClassLoader().getResource("TokenService.wsdl");
	
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
	public String getParticipantToken(String username, String password) {
		String participantToken = null;
		
		try {     
			TokenService ts = new TokenService(TOKEN_WSDL_LOCATION);
			participantToken = ts.getService().authenticateUser(username, password);
		} catch (Exception e) {
			//e.printStackTrace();
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
		} catch (InvalidParticipantTokenFault_Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		} catch (InvalidInputMessageFault_Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidInputMessageFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
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
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (InvalidInputMessageFault_Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (UnavailableTaskFault_Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		return task;		
	}
	

}
