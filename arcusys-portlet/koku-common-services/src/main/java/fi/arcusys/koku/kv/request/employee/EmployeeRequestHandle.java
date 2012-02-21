package fi.arcusys.koku.kv.request.employee;

import java.util.ArrayList;
import java.util.List;

import fi.arcusys.koku.AbstractHandle;
import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.kv.model.KokuRequest;
import fi.arcusys.koku.kv.model.KokuResponseDetail;
import fi.arcusys.koku.kv.model.KokuResponseSummary;
import fi.arcusys.koku.kv.requestservice.RequestSummary;
import fi.arcusys.koku.kv.requestservice.RequestType;
import fi.arcusys.koku.kv.requestservice.ResponseSummary;

/**
 * Handles request related operations
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class EmployeeRequestHandle extends AbstractHandle {
		
	private EmployeeRequestService rs;

	/**
	 * Constructor and initialization
	 */
	public EmployeeRequestHandle() {
		rs = new EmployeeRequestService();
	}
	
	/**
	 * Returns list of requestSummaries
	 * 
	 * @param username user name
	 * @param requestTypeStr request type string
	 * @param subQuery query string for quering
	 * @param startNum start index of request
	 * @param maxNum maximum number of requests
	 * @return a list of requests
	 */
	public List<KokuRequest> getRequests(String userId, RequestType requestType, String subQuery, int startNum, int maxNum)  throws KokuServiceException {
		List<KokuRequest> reqList = new ArrayList<KokuRequest>();
		List<RequestSummary> reqs = rs.getRequests(userId, requestType, subQuery, startNum, maxNum);
		for (RequestSummary summary : reqs) {
			reqList.add(new KokuRequest(summary));			
		}
		return reqList;
	}
	
	/**
	 * Returns list of replied responses
	 * 
	 * @param userUid
	 * @param startNum
	 * @param maxNum
	 * @return a list of replied responses
	 */
	public List<KokuResponseSummary> getRepliedResponseSummaries(String userUid, int startNum, int maxNum) throws KokuServiceException {
		return rawToViewModel(rs.getRepliedRequests(userUid, startNum, maxNum));
	}
	
	/**
	 * Returns list of old responses
	 * 
	 * @param userUid
	 * @param startNum
	 * @param maxNum
	 * @return a list of old requests
	 */
	public List<KokuResponseSummary> getOldResponseSummaries(String userUid, int startNum, int maxNum) throws KokuServiceException {
		return rawToViewModel(rs.getOldRequests(userUid, startNum, maxNum));
	}
	
	private List<KokuResponseSummary> rawToViewModel(List<ResponseSummary> responses) {
		List<KokuResponseSummary> resultList = new ArrayList<KokuResponseSummary>();
		for (ResponseSummary summary : responses) {
			resultList.add(new KokuResponseSummary(summary));
		}
		return resultList;
	}
	
	/**
	 * Returns request in detail
	 * 
	 * @param requestId request id
	 * @return detailed request
	 */
	public KokuRequest getKokuRequestById(String requestId) throws KokuServiceException {
		long  reqId = 0;
		try {
			reqId = (long) Long.parseLong(requestId);			
		} catch (NumberFormatException nfe) {
			throw new KokuServiceException("Given requestId invalid. RequestId: '"+requestId+"'", nfe);
		}
		return getKokuRequestById(reqId);
	}	

	/**
	 * Returns request in detail
	 * 
	 * @param requestId request id
	 * @return detailed request
	 */
	public KokuRequest getKokuRequestById(long requestId) throws KokuServiceException {
		return new KokuRequest(rs.getRequestById(requestId));
	}
	
	/**
	 * Returns response in detail
	 * 
	 * @param responseId
	 * @return detailed response
	 */
	public KokuResponseDetail getKokuResponseById(long responseId) throws KokuServiceException {
		return new KokuResponseDetail(rs.getResponseById(responseId));
	}	
	
	/**
	 * Returns total number of requests
	 * 
	 * @param user user name
	 * @param requestTypeStr request type string
	 * @return the total number of requests
	 */
	public int getTotalRequestsNum(String userId, RequestType requestType) throws KokuServiceException {
		return rs.getTotalRequestNum(userId, requestType);
	}
	
	/**
	 * Returns total number of old responses
	 * 
	 * @param userUid
	 * @return the total number of old responses
	 */
	public int getTotalResponsesOldNum(String userUid) throws KokuServiceException {
		return rs.getTotalResponsesOldNum(userUid);
	}
	
	/**
	 * Returns total number of replied responses
	 * 
	 * @param userUid
	 * @return the total number of old responses
	 */
	public int getTotalResponsesRepliedNum(String userUid) throws KokuServiceException {
		return rs.getTotalResponsesRepliedNum(userUid);
	}
}
