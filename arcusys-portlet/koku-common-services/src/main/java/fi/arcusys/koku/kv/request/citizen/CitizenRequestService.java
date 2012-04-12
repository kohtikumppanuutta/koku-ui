package fi.arcusys.koku.kv.request.citizen;

import java.util.List;

import javax.xml.ws.BindingProvider;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.kv.requestservice.KokuKunpoRequestService;
import fi.arcusys.koku.kv.requestservice.KokuKunpoRequestService_Service;
import fi.arcusys.koku.kv.requestservice.ResponseDetail;
import fi.arcusys.koku.kv.requestservice.ResponseSummary;
import fi.arcusys.koku.util.Properties;

/**
 * Retrieves request data and related operations via web services
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class CitizenRequestService {
	
	private final KokuKunpoRequestService service;
	
	/**
	 * Constructor and initialization
	 */
	public CitizenRequestService() {
		final KokuKunpoRequestService_Service rs = new KokuKunpoRequestService_Service();
		service = rs.getKokuKunpoRequestServicePort();
		((BindingProvider)service).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, Properties.KOKU_KUNPO_REQUEST_SERVICE);
	}
	
	/**
	 * Gets replied requests with parameters
	 * @param user user name
	 * @param startNum start index of request
	 * @param maxNum maximum number of requests
	 * @return a list of requests
	 */
	public List<ResponseSummary> getRepliedRequests(String userUid, int startNum, int maxNum) throws KokuServiceException {	
		try {
			return service.getRepliedRequests(userUid, startNum, maxNum);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getRepliedRequests failed. userUid: '"+userUid+"'", e);
		}
	}
	
	/**
	 * Gets old replied requests with parameters
	 * @param user user name
	 * @param startNum start index of request
	 * @param maxNum maximum number of requests
	 * @return a list of requests
	 */
	public List<ResponseSummary> getOldRequests(String userUid, int startNum, int maxNum) throws KokuServiceException {
		try {
			return service.getOldRequests(userUid, startNum, maxNum);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getOldRequests failed. userUid: '"+userUid+"'", e);
		}
	}
	
	/**
	 * Gets request in detail
	 * @param requestId
	 * @return detailed request
	 */
	public ResponseDetail getResponseDetail(long responseId) throws KokuServiceException {
		try {
			return service.getResponseDetail(responseId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getOldRequests failed. responseId: '"+responseId+"'", e);
		}
	}
	
	/**
	 * Gets total number of replied requests
	 * @param user user name
	 * @return the total number of requests
	 */
	public int getTotalRepliedRequests(String userUid) throws KokuServiceException {
		try {
			return service.getTotalRepliedRequests(userUid);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalRepliedRequests failed. userUid: '"+userUid+"'", e);
		}
	}
	
	/**
	 * Gets total number of old requests
	 * @param user user name
	 * @return the total number of requests
	 */
	public int getTotalOldRequests(String userUid) throws KokuServiceException {
		try {
			return service.getTotalOldRequests(userUid);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalOldRequests failed. userUid: '"+userUid+"'", e);
		}
	}
}
