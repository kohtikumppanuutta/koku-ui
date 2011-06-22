package fi.arcusys.koku.requestservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import fi.arcusys.koku.request.Request;
import fi.arcusys.koku.request.RequestSummary;
import fi.arcusys.koku.request.RequestType;
import fi.arcusys.koku.util.MessageUtil;

public class RequestHandle {
	
	public RequestHandle() {}
	
	public List<KokuRequest> getRequests(String username, String requestTypeStr, String subQuery, int startNum, int maxNum) {	
		RequestService rs = new RequestService();
		RequestType requestType;
		
		if(requestTypeStr.equals("valid")) {
			requestType = RequestType.VALID;
		}else {
			requestType = RequestType.OUTDATED;
		}
		List<KokuRequest> reqList = new ArrayList<KokuRequest>();
		List<RequestSummary> reqs = rs.getRequests(username, requestType, subQuery, startNum, maxNum);
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
			req.setCreationDate(formatTaskDate(reqSum.getCreationDate()));
			req.setEndDate(formatTaskDate(reqSum.getEndDate()));
		
			reqList.add(req);
		}
		
		return reqList;
	}
	
	public KokuRequest getKokuRequestById(String requestId) {
		RequestService rs = new RequestService();		
		long  reqId = (long) Long.parseLong(requestId);
		Request req = rs.getRequestById(reqId);
		KokuRequest kokuReq = new KokuRequest();
		kokuReq.setRequestId(req.getRequestId());
		kokuReq.setSender(req.getSender());
		kokuReq.setSubject(req.getSubject());
		kokuReq.setCreationDate(formatTaskDate(req.getCreationDate()));
		kokuReq.setEndDate(formatTaskDate(req.getEndDate()));
		kokuReq.setRespondedList(req.getResponses());
		kokuReq.setUnrespondedList(req.getNotResponded());
		
		return kokuReq;
	}
	
	public int getTotalRequestsNum(String username, String requestTypeStr) {
		RequestService rs = new RequestService();
		RequestType requestType;
		
		if(requestTypeStr.equals("valid")) {
			requestType = RequestType.VALID;
		}else {
			requestType = RequestType.OUTDATED;
		}
		
		return rs.getTotalRequestNum(username, requestType);
	}
	
	public String formatTaskDate(XMLGregorianCalendar xmlGregorianCalendar) {
		
		if(xmlGregorianCalendar != null ) {
			Calendar cal = xmlGregorianCalendar.toGregorianCalendar();
			SimpleDateFormat dataformat = new SimpleDateFormat(MessageUtil.DATE_FORMAT);
			dataformat.setTimeZone(TimeZone.getTimeZone(MessageUtil.TIME_ZONE));
			String dateStr = dataformat.format(cal.getTime());
		
			return dateStr;	
		} else {
			return "";
		}
	}

}
