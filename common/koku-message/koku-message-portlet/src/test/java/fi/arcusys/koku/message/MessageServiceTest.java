package fi.arcusys.koku.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import fi.arcusys.koku.service.FolderType;
import fi.arcusys.koku.service.MessageSummary;

public class MessageServiceTest {

	MessageService tester;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("*** Test MessageService class started ***");	
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("*** Test MessageService class ended ***");	
	}

	@Before
	public void setUp() throws Exception {
		tester = new MessageService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore("Not Ready to Run")
	public void testMessageService() {
		fail("Not yet implemented");
	}

	@Ignore("Not Ready to Run")
	public void testGetMessages() {
		String user = "Ville Virkamies";
		FolderType folderType = FolderType.OUTBOX;
		String subQuery = "";
		int startNum = 0;
		int maxNum = 5;
		List<MessageSummary> messageList = tester.getMessages(user, folderType, subQuery, startNum, maxNum);
		int expected = 5;
		int actual = messageList.size();
		assertEquals("createTask first creation date failed", expected, actual);	
	}

	@Ignore("Not Ready to Run")
	public void testGetMessageById() {
		fail("Not yet implemented");
	}

	@Ignore("Not Ready to Run")
	public void testGetTotalMessageNum() {
		fail("Not yet implemented");
	}

	@Ignore("Not Ready to Run")
	public void testGetUnreadMessageNum() {
		fail("Not yet implemented");
	}

	@Ignore("Not Ready to Run")
	public void testDeleteMessages() {
		fail("Not yet implemented");
	}

	@Ignore("Not Ready to Run")
	public void testSetMessageStatus() {
		fail("Not yet implemented");
	}

	@Ignore("Not Ready to Run")
	public void testArchiveMessages() {
		fail("Not yet implemented");
	}

}
