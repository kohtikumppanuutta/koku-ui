package fi.arcusys.koku.tiva.tietopyynto.employee;

import java.net.URL;
import java.util.List;

import fi.arcusys.koku.tiva.tietopyynto.employee.InformationRequestCriteria;
import fi.arcusys.koku.tiva.tietopyynto.employee.InformationRequestDetail;
import fi.arcusys.koku.tiva.tietopyynto.employee.InformationRequestQuery;
import fi.arcusys.koku.tiva.tietopyynto.employee.InformationRequestSummary;
import fi.arcusys.koku.tiva.tietopyynto.employee.KokuLooraTietopyyntoService_Service;

public class KokuEmployeeTietopyyntoService {
	
	public final URL TIETOPYYNTO_SERVICE_WSDL_LOCATION = getClass().getClassLoader().getResource("KokuLooraTietopyyntoServiceImpl.wsdl");
	private KokuLooraTietopyyntoService_Service service;
	
	
	public KokuEmployeeTietopyyntoService() {
		service = new KokuLooraTietopyyntoService_Service(TIETOPYYNTO_SERVICE_WSDL_LOCATION);
	}
	
	public List<InformationRequestSummary> getRepliedRequests(String receiverUid, InformationRequestQuery query) {
		return service.getKokuLooraTietopyyntoServicePort().getRepliedRequests(receiverUid, query);
	}
	
	public InformationRequestDetail getRequestDetails(long requestId) {
		return service.getKokuLooraTietopyyntoServicePort().getRequestDetails(requestId);
	}
	
	public List<InformationRequestSummary> getRequests(InformationRequestQuery query) {
		return service.getKokuLooraTietopyyntoServicePort().getRequests(query);
	}
	
	public List<InformationRequestSummary> getSentRequests(String senderUid, InformationRequestQuery query) {
		return service.getKokuLooraTietopyyntoServicePort().getSentRequests(senderUid, query);
	}
	
	public int getTotalRepliedRequests(String receiverUid, InformationRequestCriteria criteria) {
		return service.getKokuLooraTietopyyntoServicePort().getTotalRepliedRequests(receiverUid, criteria);
	}
	
	public int getTotalRequests(InformationRequestCriteria criteria) {
		return service.getKokuLooraTietopyyntoServicePort().getTotalRequests(criteria);
	}
	
	public int getTotalSentRequests(String senderUid, InformationRequestCriteria criteria) {
		return service.getKokuLooraTietopyyntoServicePort().getTotalSentRequests(senderUid, criteria);
	}
}
