package fi.arcusys.koku.kv.request.citizen;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.kv.requestservice.KokuKunpoRequestService_Service;
import fi.arcusys.koku.kv.requestservice.ResponseDetail;
import fi.arcusys.koku.kv.requestservice.ResponseSummary;
import fi.koku.settings.KoKuPropertiesUtil;

/**
 * Retrieves request data and related operations via web services
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class CitizenRequestService {
		
	private static final Logger LOG = Logger.getLogger(CitizenRequestService.class);		
	private static final URL REQUEST_WSDL_LOCATION;	
	
	static {
		try {
			LOG.info("KvMessageservice WSDL location: "+ KoKuPropertiesUtil.get("KokuKunpoRequestService"));
			REQUEST_WSDL_LOCATION =  new URL(KoKuPropertiesUtil.get("KokuKunpoRequestService"));
		} catch (MalformedURLException e) {
			LOG.error("Failed to create KokuKunpoRequestService WSDL url! Given URL address is not valid!");
			throw new ExceptionInInitializerError(e);
		}
	}
	
	
	private KokuKunpoRequestService_Service rs;
	
	/**
	 * Constructor and initialization
	 */
	public CitizenRequestService() {
		this.rs = new KokuKunpoRequestService_Service(REQUEST_WSDL_LOCATION);
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
			return rs.getKokuKunpoRequestServicePort().getRepliedRequests(userUid, startNum, maxNum);
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
			return rs.getKokuKunpoRequestServicePort().getOldRequests(userUid, startNum, maxNum);
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
			return rs.getKokuKunpoRequestServicePort().getResponseDetail(responseId);
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
			return rs.getKokuKunpoRequestServicePort().getTotalRepliedRequests(userUid);
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
			return rs.getKokuKunpoRequestServicePort().getTotalOldRequests(userUid);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalOldRequests failed. userUid: '"+userUid+"'", e);
		}
	}
}
