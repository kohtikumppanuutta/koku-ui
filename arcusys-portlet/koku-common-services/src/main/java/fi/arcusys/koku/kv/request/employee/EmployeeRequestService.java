package fi.arcusys.koku.kv.request.employee;

import java.util.List;

import javax.xml.ws.BindingProvider;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.kv.requestservice.KokuRequestService;
import fi.arcusys.koku.kv.requestservice.KokuRequestService_Service;
import fi.arcusys.koku.kv.requestservice.Request;
import fi.arcusys.koku.kv.requestservice.RequestSummary;
import fi.arcusys.koku.kv.requestservice.RequestType;
import fi.arcusys.koku.kv.requestservice.ResponseDetail;
import fi.arcusys.koku.kv.requestservice.ResponseSummary;
import fi.arcusys.koku.util.Properties;

/**
 * Retrieves request data and related operations via web services
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class EmployeeRequestService {
	
	private final KokuRequestService service;
	
	/**
	 * Constructor and initialization
	 */
	public EmployeeRequestService() {
		final KokuRequestService_Service rs = new KokuRequestService_Service();
		service = rs.getKokuRequestServicePort();
		((BindingProvider)service).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, Properties.KV_REQUEST_SERVICE);
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
	public List<RequestSummary> getRequests(String user, RequestType requestType, String subQuery, int startNum, int maxNum) throws KokuServiceException {
		try {
			return service.getRequests(user, requestType, subQuery, startNum, maxNum);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getRequests failed. user: '"+user+"' requestType: '"+requestType+"' subQuery: '"+subQuery+"'", e);
		}
	}
	
	/**
	 * Return list of replied Requests
	 * 
	 * @param userUid
	 * @param startNum
	 * @param maxNum
	 * @return List of responses (summary)
	 */
	public List<ResponseSummary> getRepliedRequests(String userUid, int startNum, int maxNum) throws KokuServiceException {
		try {
			return service.getRepliedRequests(userUid, startNum, maxNum);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getRepliedRequests failed. userUid: '"+userUid+"'", e);
		}
	}
	
	/**
	 * Return list of old replied Requests?
	 * 
	 * @param userUid
	 * @param startNum
	 * @param maxNum
	 * @return List of old requests (summary)
	 */
	public List<ResponseSummary> getOldRequests(String userUid, int startNum, int maxNum) throws KokuServiceException {
		try {
			return service.getOldRequests(userUid, startNum, maxNum);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getOldRequests failed. userUid: '"+userUid+"'", e);
		}
	}
	
	/**
	 * Returns request in detail
	 * 
	 * @param requestId
	 * @return detailed request
	 */
	public Request getRequestById(long requestId) throws KokuServiceException {
		try {
			return service.getRequestById(requestId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getRequestById failed. requestId: '"+requestId+"'", e);
		}
	}
	
	/**
	 * Return response in detail (old/replied)
	 * 
	 * @param responseId
	 * @return detailed response
	 */
	public ResponseDetail getResponseById(long responseId) throws KokuServiceException {
		try {
			return service.getResponseDetail(responseId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getResponseById failed. responseId: '"+responseId+"'", e);
		}
	}
	
	
	
	/**
	 * Gets total number of requests
	 * @param user user name
	 * @param requestType request type
	 * @return the total number of requests
	 */
	public int getTotalRequestNum(String user, RequestType requestType) throws KokuServiceException {
		try {
			return service.getTotalRequests(user, requestType);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalRequestNum failed. user: '"+user+"' requestType: '"+requestType+"'", e);
		}
	}
	
	/**
	 * Gets total number of old responses
	 * 
	 * @param user userUid
	 * @return the total number of old responses
	 */
	public int getTotalResponsesOldNum(String userUid) throws KokuServiceException {
		try {
			return service.getTotalOldRequests(userUid);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalResponsesOldNum failed. userUid: '"+userUid+"'", e);
		}
	}
	
	/**
	 * Gets total number of replied responses
	 * 
	 * @param user userUid
	 * @return the total number of replied responses
	 */
	public int getTotalResponsesRepliedNum(String userUid) throws KokuServiceException {
		try {
			return service.getTotalRepliedRequests(userUid);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalResponsesRepliedNum failed. userUid: '"+userUid+"'", e);
		}
	}
	
}
