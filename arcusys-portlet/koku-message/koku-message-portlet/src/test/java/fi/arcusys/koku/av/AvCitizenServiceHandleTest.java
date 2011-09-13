package fi.arcusys.koku.av;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class AvCitizenServiceHandleTest {

	AvCitizenServiceHandle tester;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		tester = new AvCitizenServiceHandle();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Ignore
	@Test
	public void testGetAppointments() {
		String user = "Ville Virkamies";
		int startNum = 1;
		int maxNum = 5;
		String taskType = "app_response_employee";
		
		List<KokuAppointment> apps = tester.getAppointments(user, startNum, maxNum, taskType);
		int expected = 1;
		int actual = apps.size();
		assertEquals("createTask first creation date failed", expected, actual);
	}

}
