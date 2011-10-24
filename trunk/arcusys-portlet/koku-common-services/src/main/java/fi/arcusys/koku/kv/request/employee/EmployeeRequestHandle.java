package fi.arcusys.koku.kv.request.employee;

import java.util.ArrayList;
import java.util.List;

import fi.arcusys.koku.AbstractHandle;
import fi.arcusys.koku.kv.requestservice.Request;
import fi.arcusys.koku.kv.requestservice.RequestSummary;
import fi.arcusys.koku.kv.requestservice.RequestType;
import fi.arcusys.koku.kv.model.KokuRequest;
import fi.arcusys.koku.users.KokuUserService;

/**
 * Handles request related operations
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class EmployeeRequestHandle extends AbstractHandle {
	
	private EmployeeRequestService rs;
	private final KokuUserService userService;	

	/**
	 * Constructor and initialization
	 */
	public EmployeeRequestHandle() {
		rs = new EmployeeRequestService();
		userService = new KokuUserService();

	}
	
	/**
	 * Gets request summary and generates koku request data model
	 * @param username user name
	 * @param requestTypeStr request type string
	 * @param subQuery query string for quering
	 * @param startNum start index of request
	 * @param maxNum maximum number of requests
	 * @return a list of requests
	 */
	public List<KokuRequest> getRequests(String userId, String requestTypeStr, String subQuery, int startNum, int maxNum) {
		RequestType requestType;
		
		if (requestTypeStr.equals("valid")) {
			requestType = RequestType.VALID;
		} else {
			requestType = RequestType.OUTDATED;
		}
		List<KokuRequest> reqList = new ArrayList<KokuRequest>();
		List<RequestSummary> reqs = rs.getRequests(userId, requestType, subQuery, startNum, maxNum);
		for (RequestSummary summary : reqs) {
			reqList.add(new KokuRequest(summary));			
		}
		return reqList;
	}
	
	/**
	 * Gets request in detail
	 * @param requestId request id
	 * @return detailed request
	 */
	public KokuRequest getKokuRequestById(String requestId) {	
		long  reqId = (long) Long.parseLong(requestId);
		Request req = rs.getRequestById(reqId);
		KokuRequest kokuReq = new KokuRequest(req);		
		return kokuReq;
	}
	
	/**
	 * Gets total number of requests
	 * @param user user name
	 * @param requestTypeStr request type string
	 * @return the total number of requests
	 */
	public int getTotalRequestsNum(String userId, String requestTypeStr) {
		RequestType requestType;		
		if (requestTypeStr.equals("valid")) {
			requestType = RequestType.VALID;
		} else {
			requestType = RequestType.OUTDATED;
		}		
		return rs.getTotalRequestNum(userId, requestType);
	}

}
