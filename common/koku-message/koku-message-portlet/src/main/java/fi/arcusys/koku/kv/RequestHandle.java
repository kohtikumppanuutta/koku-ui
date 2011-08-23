package fi.arcusys.koku.kv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fi.arcusys.koku.kv.requestservice.Request;
import fi.arcusys.koku.kv.requestservice.RequestSummary;
import fi.arcusys.koku.kv.requestservice.RequestType;
import fi.arcusys.koku.util.MessageUtil;

/**
 * Handles request related operations
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class RequestHandle {
	
	private RequestService rs;
	
	/**
	 * Constructor and initialization
	 */
	public RequestHandle() {
		rs = new RequestService();
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
	public List<KokuRequest> getRequests(String user, String requestTypeStr, String subQuery, int startNum, int maxNum) {	
		RequestType requestType;
		
		if(requestTypeStr.equals("valid")) {
			requestType = RequestType.VALID;
		}else {
			requestType = RequestType.OUTDATED;
		}
		List<KokuRequest> reqList = new ArrayList<KokuRequest>();
		List<RequestSummary> reqs = rs.getRequests(user, requestType, subQuery, startNum, maxNum);
		Iterator<RequestSummary> it = reqs.iterator();
		KokuRequest req;
		
		while(it.hasNext()) {
			RequestSummary reqSum = it.next();
			req = new KokuRequest();
			req.setRequestId(reqSum.getRequestId());
			req.setSender(reqSum.getSender());
			req.setSubject(reqSum.getSubject());
			req.setRespondedAmount(reqSum.getRespondedAmount());
			req.setMissedAmount(reqSum.getMissedAmout());
			req.setCreationDate(MessageUtil.formatTaskDate(reqSum.getCreationDate()));
			req.setEndDate(MessageUtil.formatTaskDate(reqSum.getEndDate()));
		
			reqList.add(req);
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
		KokuRequest kokuReq = new KokuRequest();
		kokuReq.setRequestId(req.getRequestId());
		kokuReq.setSender(req.getSender());
		kokuReq.setSubject(req.getSubject());
		kokuReq.setContent(req.getContent());
		kokuReq.setCreationDate(MessageUtil.formatTaskDate(req.getCreationDate()));
		kokuReq.setEndDate(MessageUtil.formatTaskDate(req.getEndDate()));
		kokuReq.setRespondedList(req.getResponses());
		kokuReq.setUnrespondedList(req.getNotResponded());
		kokuReq.setQuestions(req.getQuestions());
		
		return kokuReq;
	}
	
	/**
	 * Gets total number of requests
	 * @param user user name
	 * @param requestTypeStr request type string
	 * @return the total number of requests
	 */
	public int getTotalRequestsNum(String user, String requestTypeStr) {
		RequestType requestType;
		
		if(requestTypeStr.equals("valid")) {
			requestType = RequestType.VALID;
		}else {
			requestType = RequestType.OUTDATED;
		}
		
		return rs.getTotalRequestNum(user, requestType);
	}

}
