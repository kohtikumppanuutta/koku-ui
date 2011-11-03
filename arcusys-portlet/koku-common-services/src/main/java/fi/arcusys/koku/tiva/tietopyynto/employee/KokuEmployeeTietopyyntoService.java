package fi.arcusys.koku.tiva.tietopyynto.employee;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.koku.tiva.tietopyynto.employee.InformationRequestCriteria;
import fi.arcusys.koku.tiva.tietopyynto.employee.InformationRequestDetail;
import fi.arcusys.koku.tiva.tietopyynto.employee.InformationRequestQuery;
import fi.arcusys.koku.tiva.tietopyynto.employee.InformationRequestSummary;
import fi.arcusys.koku.tiva.tietopyynto.employee.KokuLooraTietopyyntoService_Service;
import fi.arcusys.koku.tiva.warrant.employee.KokuEmployeeWarrantService;
import fi.arcusys.koku.util.PropertiesUtil;
import fi.koku.settings.KoKuPropertiesUtil;

public class KokuEmployeeTietopyyntoService {
		
	private final static Logger LOG = Logger.getLogger(KokuEmployeeTietopyyntoService.class);		
	public final static URL TIETOPYYNTO_SERVICE_WSDL_LOCATION;
	
	static {
		try {
			LOG.info("KokuLooraTietopyyntoService WSDL location: " + KoKuPropertiesUtil.get("KokuLooraTietopyyntoService"));
			TIETOPYYNTO_SERVICE_WSDL_LOCATION =  new URL(KoKuPropertiesUtil.get("KokuLooraTietopyyntoService"));
		} catch (MalformedURLException e) {
			LOG.error("Failed to create KokuLooraTietopyyntoService WSDL url! Given URL address is not valid!");
			throw new ExceptionInInitializerError(e);
		}
	}
	
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
