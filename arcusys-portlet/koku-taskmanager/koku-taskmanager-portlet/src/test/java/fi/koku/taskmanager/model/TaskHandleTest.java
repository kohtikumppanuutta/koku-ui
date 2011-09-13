package fi.koku.taskmanager.model;


import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import fi.arcusys.intalio.tms.TaskMetadata;
import fi.koku.taskmanager.model.Task;
import fi.koku.taskmanager.model.TaskHandle;
import fi.koku.taskmanager.model.TaskManagementService;


public class TaskHandleTest{
	
	private String TEST_USERNAME = "Ville Virkamies";
	private String TEST_PASSWORD = "test";
	TaskHandle tester;
	
	@BeforeClass  
	public static void runBeforeClass() {  
		System.out.println("*** Test TaskHandle class starts ***");
		
	} 
	
	@AfterClass  
	public static void runAfterClass() {  	
		System.out.println("*** Test TaskHandle class ends ***");
	} 
	
	@Before
    public void setUp() throws Exception {
		String username = TEST_USERNAME;
		String token = getTestToken();
		tester = new TaskHandle(token, username);
    }
	
	@After
    public void tearDown() throws Exception {
    }
	
	@Ignore
	@Test
	public void getTasksByParams() {			
		int taskType = 1;
		String keyword = "";
		String orderType = "creationDate_desc";
		String first = "0";
		String max = "5";
		List<Task> tasklist = tester.getTasksByParams(taskType, keyword, orderType, first, max);
		assertTrue("GetTasksByParams failed", tasklist.size() > 0);
	}

	@Ignore
	@Test
	public void getTasksFromServ() {
		String taskType = "PATask";
		String subQuery = "T._state = TaskState.READY AND T._description like '%%' ORDER BY T._creationDate DESC";
		String first = "0";
		String max = "5";
		List<Task> tasklist = tester.getTasksFromServ(taskType, subQuery, first, max);
		assertTrue("GetTasksFromServ failed", tasklist.size() > 0);
	}

	@Test
	public void createTask() {
		List<TaskMetadata> tasklist = new ArrayList<TaskMetadata>();
		TaskMetadata task = new TaskMetadata();
		List<Task> myTasklist = new ArrayList<Task>();
		task.setTaskType("ACTIVITY");
		task.setDescription("task test 1");
		task.setTaskState(null);
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.set(2011, 4, 20, 10, 30, 20);
		XMLGregorianCalendar xmlGregorianCalendar;
		try {
			xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			task.setCreationDate(xmlGregorianCalendar);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		task.setTaskId("task-id-1");
		String url = "http://localhost:8080/form.htm";
		task.setFormUrl(url);
		tasklist.add(task);
		task.setDescription("task test 1");
		tasklist.add(task);
		myTasklist = tester.createTask(tasklist);
		Task myTask = myTasklist.get(0);
		String expected = "task test 1";
		String actual = myTask.getDescription();
		assertEquals("createTask first description failed", expected, actual);
		expected = "20.5.2011 10:30:20";
		actual = myTask.getCreationDate();
		assertEquals("createTask first creation date failed", expected, actual);		
	}
	
	@Test
	public void formatTaskDate() {
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.set(2011, 4, 20, 10, 30, 20);
		XMLGregorianCalendar xmlGregorianCalendar = null;
		try {
			xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String expected = "20.5.2011 10:30:20";
		String actual = tester.formatTaskDate(xmlGregorianCalendar);
		assertEquals("formatTaskDate failed", expected, actual);
	}
	
	@Test
	public void createTaskLink() {
		TaskMetadata task = new TaskMetadata();
		task.setTaskType("ACTIVITY");
		task.setDescription("task test 1");
		task.setTaskState(null);
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.set(2011, 4, 20, 10, 30, 20);
		XMLGregorianCalendar xmlGregorianCalendar = null;
		try {
			xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		task.setCreationDate(xmlGregorianCalendar);
		task.setTaskId("task-id-1");
		String url = "http://localhost:8080/form.htm";
		task.setFormUrl(url);
		tester.setToken("testtoken");
		String expected = "http://localhost:8080/form.htm?id=task-id-1&type=PATask&url=http%3A%2F%2Flocalhost%3A8080%2Fform.htm&token=testtoken&user=Ville+Virkamies&claimTaskOnOpen=false";
		String actual = tester.createTaskLink(task);
		assertEquals("createTask task link failed", expected, actual);
	}

	@Ignore("Not Ready to Run") 
	@Test
	public void getTotalTasksNumber() {
		int taskType = 1;
		String keyword = "Marko";
		int expected = 3;
		int actual = tester.getTotalTasksNumber(taskType, keyword);
		assertEquals("getTotalTasksNumber failed", expected, actual);
	}

	@Test
	public void createTotalNumSubQuery() {
		int taskType = 1;
		String keyword = "";
		String expected = "(T._state = TaskState.READY OR T._state = TaskState.CLAIMED) AND T._description like '%%'";
		String actual = tester.createTotalNumSubQuery(taskType, keyword);
		assertEquals("createTotalNumSubQuery for task failed", expected, actual);
		
		taskType = 2;
		keyword = "key";
		expected = "T._state = TaskState.READY AND T._description like '%key%'";
		actual = tester.createTotalNumSubQuery(taskType, keyword);
		assertEquals("createTotalNumSubQuery for notification failed", expected, actual);
		
		taskType = 3;
		keyword = "key";
		expected = "T._description like '%key%'";
		actual = tester.createTotalNumSubQuery(taskType, keyword);
		assertEquals("createTotalNumSubQuery for process failed", expected, actual);
		
		taskType = 5;
		keyword = "key";
		expected = "";
		actual = tester.createTotalNumSubQuery(taskType, keyword);
		assertEquals("createTotalNumSubQuery for other invalid processes failed", expected, actual);
	}

	@Test
	public void createTaskSubQuery() {
		int taskType = 1;
		String keyword = "";
		String orderType = "description_desc";
		String expected = "(T._state = TaskState.READY OR T._state = TaskState.CLAIMED) AND T._description like '%%' ORDER BY T._description DESC";
		String actual = tester.createTaskSubQuery(taskType, keyword, orderType);
		assertEquals("createTaskSubQuery for task failed", expected, actual);
		
		taskType = 2;
		keyword = "key";
		expected = "T._state = TaskState.READY AND T._description like '%key%' ORDER BY T._description DESC";
		actual = tester.createTaskSubQuery(taskType, keyword, orderType);
		assertEquals("createTaskSubQuery for notification failed", expected, actual);
		
		taskType = 3;
		keyword = "key";
		expected = "T._description like '%key%' ORDER BY T._description DESC";
		actual = tester.createTaskSubQuery(taskType, keyword, orderType);
		assertEquals("createTaskSubQuery for process failed", expected, actual);
		
		taskType = 5;
		keyword = "key";
		expected = "";
		actual = tester.createTaskSubQuery(taskType, keyword, orderType);
		assertEquals("createTaskSubQuery for other invalid processes failed", expected, actual);
	}

	@Test
	public void getOrderTypeStr() {
		String orderType = "description_desc";
		String expected = "T._description DESC";
		String actual = tester.getOrderTypeStr(orderType);
		assertEquals("getOrderTypeStr " + orderType + " failed", expected, actual);
		
		orderType = "description_asc";
		expected = "T._description ASC";
		actual = tester.getOrderTypeStr(orderType);
		assertEquals("getOrderTypeStr " + orderType + " failed", expected, actual);
		
		orderType = "state_desc";
		expected = "T._state DESC";
		actual = tester.getOrderTypeStr(orderType);
		assertEquals("getOrderTypeStr " + orderType + " failed", expected, actual);
		
		orderType = "state_asc";
		expected = "T._state ASC";
		actual = tester.getOrderTypeStr(orderType);
		assertEquals("getOrderTypeStr " + orderType + " failed", expected, actual);
		
		orderType = "creationDate_desc";
		expected = "T._creationDate DESC";
		actual = tester.getOrderTypeStr(orderType);
		assertEquals("getOrderTypeStr " + orderType + " failed", expected, actual);
		
		orderType = "creationDate_asc";
		expected = "T._creationDate ASC";
		actual = tester.getOrderTypeStr(orderType);
		assertEquals("getOrderTypeStr " + orderType + " failed", expected, actual);
		
		orderType = "other";
		expected = "T._creationDate DESC";
		actual = tester.getOrderTypeStr(orderType);
		assertEquals("getOrderTypeStr " + orderType + " failed", expected, actual);
		
	}

	@Ignore
	@Test
	public void getTokenByUser() {
		String username = TEST_USERNAME;
		String password = TEST_PASSWORD;
		String participantToken = tester.getTokenByUser(username, password);
		System.out.println(participantToken);
		assertNotNull("getTokenByUser failed", participantToken);
		
		username = "wrong";
		password = "wrong";
		participantToken = tester.getTokenByUser(username, password);
		assertNull("getTokenByUser failed", participantToken);
	}

	@Ignore("Not Ready to Run") 
	@Test
	public void getToken() {
		fail("Not yet implemented");
	}

	@Ignore("Not Ready to Run") 
	@Test
	public void setToken() {
		fail("Not yet implemented");
	}

	@Ignore("Not Ready to Run") 
	@Test
	public void getMessage() {
		fail("Not yet implemented");
	}
	
	private String getTestToken() {
		TaskManagementService tms = new TaskManagementService();
		String username = TEST_USERNAME;
		String password = TEST_PASSWORD;
		String participantToken = tms.getParticipantToken(username, password);

		return participantToken;
	}

}
