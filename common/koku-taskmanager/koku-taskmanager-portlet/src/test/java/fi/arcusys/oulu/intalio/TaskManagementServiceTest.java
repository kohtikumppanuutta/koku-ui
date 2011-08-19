package fi.arcusys.oulu.intalio;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class TaskManagementServiceTest {
	TaskManagementService tester;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
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
	public void testTaskManagementService() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testGetParticipantToken() {
		String username = "Kalle Kuntalainen";
		String password = "test";
		String token = tester.getParticipantToken(username, password);
		//System.out.println(token);
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testGetAvailableTasks() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testGetTotalTasksNumber() {
		String username = "Kalle Kuntalainen";
		String password = "test";
		String token = tester.getParticipantToken(username, password);
		String num = tester.getTotalTasksNumber(token, "PATask", "");
		System.out.println(num);
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testGetTask() {
		fail("Not yet implemented");
	}

}
