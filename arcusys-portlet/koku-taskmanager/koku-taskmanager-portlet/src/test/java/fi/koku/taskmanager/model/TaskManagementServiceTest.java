package fi.koku.taskmanager.model;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import fi.arcusys.intalio.tms.TaskMetadata;
import fi.koku.taskmanager.model.TaskManagementService;

public class TaskManagementServiceTest {
	
	private String TEST_USERNAME = "Ville Virkamies";
	private String TEST_PASSWORD = "test";
	
	TaskManagementService tester;
	
	@BeforeClass  
	public static void runBeforeClass() {  
		System.out.println("*** Test TaskManagementService class starts ***");		
	} 
	
	@AfterClass  
	public static void runAfterClass() {  
		System.out.println("*** Test TaskManagementService class ends ***");
	} 
	
	@Before
    public void setUp() throws Exception {
		tester = new TaskManagementService();		
    }
	
	@After
    public void tearDown() throws Exception {
		
    }
	
	@Ignore
	@Test
	public void getParticipantToken() {
		String username = TEST_USERNAME;
		String password = TEST_PASSWORD;
		String participantToken = tester.getParticipantToken(username, password);
		assertNotNull("Correct account, authentication failed", participantToken);
		
		String wrongUsername = "wrongusername";
		String wrongPassword = "wrongpassword";	
		participantToken = tester.getParticipantToken(wrongUsername, wrongPassword);
		assertNull("Incorrect account, authentication failed", participantToken);
	}

	@Ignore
	@Test
	public void getAvailableTasks() {
		String participantToken = getToken();
		String taskType = "PATask";
		String subQuery = "T._state = TaskState.READY";
		String first = "0";
		String max = "5";
		List<TaskMetadata> tasklist = tester.getAvailableTasks(participantToken, taskType, subQuery, first, max);
		assertTrue("Corrent params,get available tasks failed", tasklist.size() > 0);
		
		String wrongTaskType = "wrongType";
		tasklist = tester.getAvailableTasks(participantToken, wrongTaskType, subQuery, first, max);
		assertTrue("Incorrect params, get available tasks failed", tasklist.size() == 0);
		
		String wrongToken = "wrongToken";
		tasklist = tester.getAvailableTasks(wrongToken, taskType, subQuery, first, max);
		assertTrue("Incorrect participant token, get available tasks failed", tasklist.size() == 0);
		
	}
	
	@Ignore
	@Test
	public void getTotalTasksNumber() {
		String participantToken = getToken();
		String taskTypeStr = "PATask";
		String subQuery = "T._state = TaskState.READY";
		String totalNumStr;
		totalNumStr = tester.getTotalTasksNumber(participantToken, taskTypeStr, subQuery);
		int totalNum = Integer.parseInt(totalNumStr);
		assertTrue("Correct params, get total tasks failed", totalNum > 0);
		
		String wrongToken = "wrongToken";
		totalNumStr = tester.getTotalTasksNumber(wrongToken, taskTypeStr, subQuery);
		totalNum = Integer.parseInt(totalNumStr);
		assertTrue("Incorrect token, get available tasks failed", totalNum == 0);
		
	}
	
	@Ignore
	@Test
	public void getTask() {
		String taskId = "77630ab9-ad62-41c2-8fdf-dc875a0795e3";
		//taskId = "e795b3bfa0de38ff:-23df788e:13064213459:-75a210.5.12.2331554"; // incorrect form url
		// taskId = "ebc49ec8-ff7c-4455-b5bf-f90e9596e5f0";
		String participantToken = getToken();
		fi.arcusys.intalio.tms.Task task = tester.getTask(taskId, participantToken);
		String newTaskId = task.getMetadata().getTaskId();
		assertEquals("getTask failed", taskId, newTaskId);
		
		taskId = "wrong task id";
		task = tester.getTask(taskId, participantToken);
		assertNull("getTask with wrong task id failed", task);
	}
	
	private String getToken() {
		String username = TEST_USERNAME;
		String password = TEST_PASSWORD;
		String participantToken = tester.getParticipantToken(username, password);
		assertNotNull("Get authentication failed", participantToken);
		return participantToken;
	}

}
