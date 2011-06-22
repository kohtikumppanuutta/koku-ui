package fi.arcusys.koku.requestservice;

import java.net.URL;
import java.util.List;
import fi.arcusys.koku.request.KokuRequestService_Service;
import fi.arcusys.koku.request.Request;
import fi.arcusys.koku.request.RequestSummary;
import fi.arcusys.koku.request.RequestType;

public class RequestService {
	
	public final URL REQUEST_WSDL_LOCATION = getClass().getClassLoader().getResource("RequestService.wsdl");
	private KokuRequestService_Service rs;
	
	public RequestService() {
		this.rs = new KokuRequestService_Service(REQUEST_WSDL_LOCATION);
	}
	
	public List<RequestSummary> getRequests(String user, RequestType requestType, String subQuery, int startNum, int maxNum) {		
		return rs.getKokuRequestServicePort().getRequests(user, requestType, subQuery, startNum, maxNum);
	}
	
	public Request getRequestById(long requestId) {
		return rs.getKokuRequestServicePort().getRequestById(requestId);
	}
	
	public int getTotalRequestNum(String user, RequestType requestType) {
		return rs.getKokuRequestServicePort().getTotalRequests(user, requestType);
	}

}
