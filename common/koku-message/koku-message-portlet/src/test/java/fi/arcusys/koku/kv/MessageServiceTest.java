package fi.arcusys.koku.kv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

import fi.arcusys.koku.kv.MessageHandle;
import fi.arcusys.koku.kv.MessageService;
import fi.arcusys.koku.kv.messageservice.Criteria;
import fi.arcusys.koku.kv.messageservice.Fields;
import fi.arcusys.koku.kv.messageservice.FolderType;
import fi.arcusys.koku.kv.messageservice.MessageQuery;
import fi.arcusys.koku.kv.messageservice.MessageSummary;
import fi.arcusys.koku.kv.messageservice.OrderBy;
import fi.arcusys.koku.kv.messageservice.Type;

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
		int startNum = 1;
		int maxNum = 5;
		MessageQuery messageQuery = new MessageQuery();
		messageQuery.setStartNum(startNum);
		messageQuery.setMaxNum(maxNum);
		
		/* sets the criteria for searching including keyword for each field, default is searching all fields */
		String keyword="";
		String field = "1 2 3 4";
		MessageHandle handle = new MessageHandle();
		Criteria criteria = handle.createCriteria(keyword, field);
		messageQuery.setCriteria(criteria);
		
		/* sets the order type, default is ordering by created date descending */
		OrderBy orderby = new OrderBy();
		orderby.setField(Fields.CREATED_DATE);
		orderby.setType(Type.DESC);		
		messageQuery.getOrderBy().add(orderby);
		
		List<MessageSummary> messageList = tester.getMessages(user, folderType, messageQuery);
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
		String user = "Ville Virkamies";
		FolderType folderType = FolderType.OUTBOX;
		String keyword="test";
		String field = "1 2 3 4";
		MessageHandle handle = new MessageHandle();
		Criteria criteria = handle.createCriteria(keyword, field);
		int num = tester.getTotalMessageNum(user, folderType, criteria);
		
		System.out.println("total number: " + num);
		
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
