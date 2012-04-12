package fi.arcusys.koku.tiva.tietopyynto.employee;

import java.util.List;

import javax.xml.ws.BindingProvider;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.util.Properties;

public class KokuEmployeeTietopyyntoService {
	
	private final KokuLooraTietopyyntoService service;
	
	public KokuEmployeeTietopyyntoService() {
		final KokuLooraTietopyyntoService_Service serviceInit = new KokuLooraTietopyyntoService_Service();
		service = serviceInit.getKokuLooraTietopyyntoServicePort();
		((BindingProvider)service).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, Properties.KOKU_LOORA_TIETOPYYNTO_SERVICE);		
	}
	
	public List<InformationRequestSummary> getRepliedRequests(String receiverUid, InformationRequestQuery query) throws KokuServiceException {
		try {
			return service.getRepliedRequests(receiverUid, query);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getRepliedRequests failed. receiverUid: '"+receiverUid+"'", e);
		}
	}
	
	public InformationRequestDetail getRequestDetails(long requestId) throws KokuServiceException {
		try {
			return service.getRequestDetails(requestId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getRequestDetails failed. requestId: '"+requestId+"'", e);
		}
	}
	
	public List<InformationRequestSummary> getRequests(InformationRequestQuery query) throws KokuServiceException {
		try {
			return service.getRequests(query);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getRequests failed.", e);
		}
	}
	
	public List<InformationRequestSummary> getSentRequests(String senderUid, InformationRequestQuery query) throws KokuServiceException {
		try {
			return service.getSentRequests(senderUid, query);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getSentRequests failed. senderUid: '"+senderUid+"'", e);
		}
	}
	
	public int getTotalRepliedRequests(String receiverUid, InformationRequestCriteria criteria) throws KokuServiceException {
		try {
			return service.getTotalRepliedRequests(receiverUid, criteria);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalRepliedRequests failed. receiverUid: '"+receiverUid+"'", e);
		}
	}
	
	public int getTotalRequests(InformationRequestCriteria criteria) throws KokuServiceException {
		try {
			return service.getTotalRequests(criteria);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalRequests failed.", e);
		}
	}
	
	public int getTotalSentRequests(String senderUid, InformationRequestCriteria criteria) throws KokuServiceException {
		try {
			return service.getTotalSentRequests(senderUid, criteria);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalSentRequests failed.receiverUid: '"+senderUid+"'", e);
		}
	}	
}
