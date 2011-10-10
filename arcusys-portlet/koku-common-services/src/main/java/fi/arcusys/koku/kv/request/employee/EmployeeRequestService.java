package fi.arcusys.koku.kv.request.employee;

import java.net.URL;
import java.util.List;

import fi.arcusys.koku.kv.requestservice.KokuRequestService_Service;
import fi.arcusys.koku.kv.requestservice.Request;
import fi.arcusys.koku.kv.requestservice.RequestSummary;
import fi.arcusys.koku.kv.requestservice.RequestType;

/**
 * Retrieves request data and related operations via web services
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class EmployeeRequestService {
	
	public final URL REQUEST_WSDL_LOCATION = getClass().getClassLoader().getResource("KvRequestService.wsdl");
	private KokuRequestService_Service rs;
	
	/**
	 * Constructor and initialization
	 */
	public EmployeeRequestService() {
		this.rs = new KokuRequestService_Service(REQUEST_WSDL_LOCATION);
	}
	
	/**
	 * Gets requests with parameters
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
	 * Gets request in detail
	 * @param requestId
	 * @return detailed request
	 */
	public Request getRequestById(long requestId) {
		return rs.getKokuRequestServicePort().getRequestById(requestId);
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

}
