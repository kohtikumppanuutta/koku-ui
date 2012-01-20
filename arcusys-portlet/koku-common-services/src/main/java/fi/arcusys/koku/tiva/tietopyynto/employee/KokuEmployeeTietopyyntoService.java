package fi.arcusys.koku.tiva.tietopyynto.employee;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.tiva.tietopyynto.employee.InformationRequestCriteria;
import fi.arcusys.koku.tiva.tietopyynto.employee.InformationRequestDetail;
import fi.arcusys.koku.tiva.tietopyynto.employee.InformationRequestQuery;
import fi.arcusys.koku.tiva.tietopyynto.employee.InformationRequestSummary;
import fi.arcusys.koku.tiva.tietopyynto.employee.KokuLooraTietopyyntoService_Service;
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
	
	public List<InformationRequestSummary> getRepliedRequests(String receiverUid, InformationRequestQuery query) throws KokuServiceException {
		try {
			return service.getKokuLooraTietopyyntoServicePort().getRepliedRequests(receiverUid, query);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getRepliedRequests failed. receiverUid: '"+receiverUid+"'", e);
		}
	}
	
	public InformationRequestDetail getRequestDetails(long requestId) throws KokuServiceException {
		try {
			return service.getKokuLooraTietopyyntoServicePort().getRequestDetails(requestId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getRequestDetails failed. requestId: '"+requestId+"'", e);
		}
	}
	
	public List<InformationRequestSummary> getRequests(InformationRequestQuery query) throws KokuServiceException {
		try {
			return service.getKokuLooraTietopyyntoServicePort().getRequests(query);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getRequests failed.", e);
		}
	}
	
	public List<InformationRequestSummary> getSentRequests(String senderUid, InformationRequestQuery query) throws KokuServiceException {
		try {
			return service.getKokuLooraTietopyyntoServicePort().getSentRequests(senderUid, query);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getSentRequests failed. senderUid: '"+senderUid+"'", e);
		}
	}
	
	public int getTotalRepliedRequests(String receiverUid, InformationRequestCriteria criteria) throws KokuServiceException {
		try {
			return service.getKokuLooraTietopyyntoServicePort().getTotalRepliedRequests(receiverUid, criteria);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalRepliedRequests failed. receiverUid: '"+receiverUid+"'", e);
		}
	}
	
	public int getTotalRequests(InformationRequestCriteria criteria) throws KokuServiceException {
		try {
			return service.getKokuLooraTietopyyntoServicePort().getTotalRequests(criteria);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalRequests failed.", e);
		}
	}
	
	public int getTotalSentRequests(String senderUid, InformationRequestCriteria criteria) throws KokuServiceException {
		try {
			return service.getKokuLooraTietopyyntoServicePort().getTotalSentRequests(senderUid, criteria);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalSentRequests failed.receiverUid: '"+senderUid+"'", e);
		}
	}	
}
