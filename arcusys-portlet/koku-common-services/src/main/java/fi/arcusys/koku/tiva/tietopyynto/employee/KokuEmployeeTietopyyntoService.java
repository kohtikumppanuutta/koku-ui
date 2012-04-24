package fi.arcusys.koku.tiva.tietopyynto.employee;

import java.util.List;

import javax.xml.ws.BindingProvider;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.util.Properties;

/**
 * Employee side service to gather InfoRequests (Tietopyyntö)
 * 
 * @author Toni Turunen
 *
 */
public class KokuEmployeeTietopyyntoService {
	
	private final KokuLooraTietopyyntoService service;
	
	public KokuEmployeeTietopyyntoService() {
		final KokuLooraTietopyyntoService_Service serviceInit = new KokuLooraTietopyyntoService_Service();
		service = serviceInit.getKokuLooraTietopyyntoServicePort();
		((BindingProvider)service).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, Properties.KOKU_LOORA_TIETOPYYNTO_SERVICE);		
	}
	
	/**
	 * Returns list of replied InfoRequests
	 * 
	 * @param receiverUid
	 * @param query
	 * @return List of InfoRequests
	 * @throws KokuServiceException
	 */
	public List<InformationRequestSummary> getRepliedRequests(String receiverUid, InformationRequestQuery query) throws KokuServiceException {
		try {
			return service.getRepliedRequests(receiverUid, query);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getRepliedRequests failed. receiverUid: '"+receiverUid+"'", e);
		}
	}
	
	/**
	 * Returns InformationRequest details
	 * 
	 * @param requestId
	 * @return detailed information about infoRequest
	 * @throws KokuServiceException
	 */
	public InformationRequestDetail getRequestDetails(long requestId) throws KokuServiceException {
		try {
			return service.getRequestDetails(requestId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getRequestDetails failed. requestId: '"+requestId+"'", e);
		}
	}
	
	/**
	 * Returns infoRequests by given query params
	 * 
	 * @param query
	 * @return List of InfoRequests
	 * @throws KokuServiceException
	 */
	public List<InformationRequestSummary> getRequests(InformationRequestQuery query) throws KokuServiceException {
		try {
			return service.getRequests(query);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getRequests failed.", e);
		}
	}
	
	/**
	 * Returns sent infoRequests
	 * 
	 * @param senderUid
	 * @param query
	 * @return List of sent requests
	 * @throws KokuServiceException
	 */
	public List<InformationRequestSummary> getSentRequests(String senderUid, InformationRequestQuery query) throws KokuServiceException {
		try {
			return service.getSentRequests(senderUid, query);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getSentRequests failed. senderUid: '"+senderUid+"'", e);
		}
	}
	
	/**
	 * Returns total number of replied infoRequests
	 * 
	 * @param receiverUid
	 * @param criteria
	 * @return number of replied infoRequests
	 * @throws KokuServiceException
	 */
	public int getTotalRepliedRequests(String receiverUid, InformationRequestCriteria criteria) throws KokuServiceException {
		try {
			return service.getTotalRepliedRequests(receiverUid, criteria);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalRepliedRequests failed. receiverUid: '"+receiverUid+"'", e);
		}
	}
	
	/**
	 * Returns total number of infoRequests by given criteria
	 * 
	 * @param criteria
	 * @return total number of infoRequests
	 * @throws KokuServiceException
	 */
	public int getTotalRequests(InformationRequestCriteria criteria) throws KokuServiceException {
		try {
			return service.getTotalRequests(criteria);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalRequests failed.", e);
		}
	}
	
	/**
	 * Return total num of sent infoReqests by given criteria
	 * 
	 * @param senderUid
	 * @param criteria
	 * @return total num of sent infoRequests
	 * @throws KokuServiceException
	 */
	public int getTotalSentRequests(String senderUid, InformationRequestCriteria criteria) throws KokuServiceException {
		try {
			return service.getTotalSentRequests(senderUid, criteria);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalSentRequests failed.receiverUid: '"+senderUid+"'", e);
		}
	}	
}
