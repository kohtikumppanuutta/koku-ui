package fi.arcusys.koku.kv.request.citizen;

import java.net.URL;
import java.util.List;

import fi.arcusys.koku.kv.requestservice.KokuKunpoRequestService_Service;
import fi.arcusys.koku.kv.requestservice.ResponseDetail;
import fi.arcusys.koku.kv.requestservice.ResponseSummary;

/**
 * Retrieves request data and related operations via web services
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class CitizenRequestService {
	
	public final URL REQUEST_WSDL_LOCATION = getClass().getClassLoader().getResource("KokuKunpoRequestServiceImpl.wsdl");
	private KokuKunpoRequestService_Service rs;
	
	/**
	 * Constructor and initialization
	 */
	public CitizenRequestService() {
		this.rs = new KokuKunpoRequestService_Service(REQUEST_WSDL_LOCATION);
	}
	
	/**
	 * Gets replied requests with parameters
	 * @param user user name
	 * @param startNum start index of request
	 * @param maxNum maximum number of requests
	 * @return a list of requests
	 */
	public List<ResponseSummary> getRepliedRequests(String userUid, int startNum, int maxNum) {		
		return rs.getKokuKunpoRequestServicePort().getRepliedRequests(userUid, startNum, maxNum);
	}
	
	/**
	 * Gets old replied requests with parameters
	 * @param user user name
	 * @param startNum start index of request
	 * @param maxNum maximum number of requests
	 * @return a list of requests
	 */
	public List<ResponseSummary> getOldRequests(String userUid, int startNum, int maxNum) {
		return rs.getKokuKunpoRequestServicePort().getOldRequests(userUid, startNum, maxNum);
	}
	
	/**
	 * Gets request in detail
	 * @param requestId
	 * @return detailed request
	 */
	public ResponseDetail getResponseDetail(long responseId) {
		return rs.getKokuKunpoRequestServicePort().getResponseDetail(responseId);
	}
	
	/**
	 * Gets total number of replied requests
	 * @param user user name
	 * @return the total number of requests
	 */
	public int getTotalRepliedRequests(String userUid) {
		return rs.getKokuKunpoRequestServicePort().getTotalRepliedRequests(userUid);
	}
	
	/**
	 * Gets total number of old requests
	 * @param user user name
	 * @return the total number of requests
	 */
	public int getTotalOldRequests(String userUid) {
		return rs.getKokuKunpoRequestServicePort().getTotalOldRequests(userUid);
	}
}
