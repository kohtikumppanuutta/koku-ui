package fi.arcusys.koku.kv.request.employee;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.koku.kv.requestservice.KokuRequestService_Service;
import fi.arcusys.koku.kv.requestservice.Request;
import fi.arcusys.koku.kv.requestservice.RequestSummary;
import fi.arcusys.koku.kv.requestservice.RequestType;
import fi.arcusys.koku.kv.requestservice.ResponseDetail;
import fi.arcusys.koku.kv.requestservice.ResponseSummary;
import fi.koku.settings.KoKuPropertiesUtil;

/**
 * Retrieves request data and related operations via web services
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class EmployeeRequestService {
		
	private static final Logger LOG = Logger.getLogger(EmployeeRequestService.class);		
	private static final URL REQUEST_WSDL_LOCATION;	
	
	static {
		try {
			LOG.info("KvMessageservice WSDL location: "+ KoKuPropertiesUtil.get("KvRequestService"));
			REQUEST_WSDL_LOCATION =  new URL(KoKuPropertiesUtil.get("KvRequestService"));
		} catch (MalformedURLException e) {
			LOG.error("Failed to create KvRequestService WSDL url! Given URL address is not valid!");
			throw new ExceptionInInitializerError(e);
		}
	}
	
	private KokuRequestService_Service rs;
	
	/**
	 * Constructor and initialization
	 */
	public EmployeeRequestService() {
		this.rs = new KokuRequestService_Service(REQUEST_WSDL_LOCATION);
	}
	
	/**
	 * Returns list of sent requests 
	 * 
	 * @param user user name
	 * @param requestType request type
	 * @param subQuery sub query string for querying in database
	 * @param startNum start index of request
	 * @param maxNum maximum number of requests
	 * @return a list of requests
	 */
	public List<RequestSummary> getRequests(String user, RequestType requestType, String subQuery, int startNum, int maxNum) {		
		return rs.getKokuRequestServicePort().getRequests(user, requestType, subQuery, startNum, maxNum);
	}
	
	/**
	 * Return list of replied Requests
	 * 
	 * @param userUid
	 * @param startNum
	 * @param maxNum
	 * @return List of responses (summary)
	 */
	public List<ResponseSummary> getRepliedRequests(String userUid, int startNum, int maxNum) {
		return rs.getKokuRequestServicePort().getRepliedRequests(userUid, startNum, maxNum);
	}
	
	/**
	 * Return list of old replied Requests?
	 * 
	 * @param userUid
	 * @param startNum
	 * @param maxNum
	 * @return List of old requests (summary)
	 */
	public List<ResponseSummary> getOldRequests(String userUid, int startNum, int maxNum) {
		return rs.getKokuRequestServicePort().getOldRequests(userUid, startNum, maxNum);
	}
	
	/**
	 * Returns request in detail
	 * 
	 * @param requestId
	 * @return detailed request
	 */
	public Request getRequestById(long requestId) {
		return rs.getKokuRequestServicePort().getRequestById(requestId);
	}
	
	/**
	 * Return response in detail (old/replied)
	 * 
	 * @param responseId
	 * @return detailed response
	 */
	public ResponseDetail getResponseById(long responseId) {
		return rs.getKokuRequestServicePort().getResponseDetail(responseId);
	}
	
	
	
	/**
	 * Gets total number of requests
	 * @param user user name
	 * @param requestType request type
	 * @return the total number of requests
	 */
	public int getTotalRequestNum(String user, RequestType requestType) {
		return rs.getKokuRequestServicePort().getTotalRequests(user, requestType);
	}
	
	/**
	 * Gets total number of old responses
	 * 
	 * @param user userUid
	 * @return the total number of old responses
	 */
	public int getTotalResponsesOldNum(String userUid) {
		return rs.getKokuRequestServicePort().getTotalOldRequests(userUid);
	}
	
	/**
	 * Gets total number of replied responses
	 * 
	 * @param user userUid
	 * @return the total number of replied responses
	 */
	public int getTotalResponsesRepliedNum(String userUid) {
		return rs.getKokuRequestServicePort().getTotalRepliedRequests(userUid);
	}
	
}
