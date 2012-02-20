package fi.arcusys.koku.kv.request.citizen;

import java.util.ArrayList;
import java.util.List;

import fi.arcusys.koku.AbstractHandle;
import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.kv.model.KokuResponseDetail;
import fi.arcusys.koku.kv.model.KokuResponseSummary;
import fi.arcusys.koku.kv.requestservice.ResponseDetail;
import fi.arcusys.koku.kv.requestservice.ResponseSummary;
import fi.arcusys.koku.users.KokuUserService;

/**
 * Handles request related operations
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class CitizenRequestHandle extends AbstractHandle {
		
	private CitizenRequestService rs;
	private final KokuUserService userService;	

	/**
	 * Constructor and initialization
	 */
	public CitizenRequestHandle() {
		rs = new CitizenRequestService();
		userService = new KokuUserService();
	}
	
	/**
	 * Gets request summary and generates koku request data model
	 * @param userUid
	 * @param requestTypeStr request type string
	 * @param subQuery query string for quering
	 * @param startNum start index of request
	 * @param maxNum maximum number of requests
	 * @return a list of requests
	 */
	public List<KokuResponseSummary> getRepliedRequests(String userUid, int startNum, int maxNum) throws KokuServiceException {		
		return getResponseList(rs.getRepliedRequests(userUid, startNum, maxNum));
	}
	
	/**
	 * Gets request summary and generates koku request data model
	 * @param userUid
	 * @param requestTypeStr request type string
	 * @param subQuery query string for quering
	 * @param startNum start index of request
	 * @param maxNum maximum number of requests
	 * @return a list of requests
	 */
	public List<KokuResponseSummary> getOldRequests(String userUid, int startNum, int maxNum) throws KokuServiceException {
		return getResponseList(rs.getOldRequests(userUid, startNum, maxNum));
	}
	
	private List<KokuResponseSummary> getResponseList(List<ResponseSummary> reqs) {
		List<KokuResponseSummary> reqList = new ArrayList<KokuResponseSummary>();
		for (ResponseSummary summary : reqs) {
			KokuResponseSummary kokuSummary = new KokuResponseSummary(summary);
			// TODO: SERVICES NEED TO UPDATE!
//			kokuSummary.setReplierName(userService.getKunpoNameByUserUid(kokuSummary.getReplierUid()));
			kokuSummary.setReplierName(kokuSummary.getReplierUid());
			reqList.add(kokuSummary);
		}
		return reqList;
	}
	
	/**
	 * Gets request in detail
	 * @param requestId request id
	 * @return detailed request
	 */
	public KokuResponseDetail getResponseById(String requestId) throws KokuServiceException {
		long reqId = 0;
		try {
			reqId = (long) Long.parseLong(requestId);			
		} catch (NumberFormatException nfe) {
			throw new KokuServiceException("RequestId is not valid. requestId: '"+requestId+"'", nfe);
		}
		ResponseDetail req = rs.getResponseDetail(reqId);
		KokuResponseDetail kokuReq = new KokuResponseDetail(req);
		// FIXME: Pitää korjata kun WS päivittyy!
//		kokuReq.setReplierName(userService.getKunpoNameByUserUid(kokuReq.getReplierUid()));
		kokuReq.setReplierName(kokuReq.getReplierUid());
		return kokuReq;
	}
	
	/**
	 * Gets total number of old replied requests
	 * @param user user name
	 * @param requestTypeStr request type string
	 * @return the total number of requests
	 */
	public int getTotalOldRequests(String userUid) throws KokuServiceException {
		return rs.getTotalOldRequests(userUid);
	}
	
	/**
	 * Gets total number of old replied requests
	 * @param user user name
	 * @param requestTypeStr request type string
	 * @return the total number of requests
	 */
	public int getTotalRepliedRequests(String userUid) throws KokuServiceException {
		return rs.getTotalRepliedRequests(userUid);
	}

}
