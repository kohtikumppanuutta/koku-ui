
package fi.arcusys.koku.tiva.warrant.employee;

import java.util.List;

import javax.xml.ws.BindingProvider;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationCriteria;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationQuery;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationShortSummary;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationSummary;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.KokuLooraValtakirjaService;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.KokuLooraValtakirjaService_Service;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.Valtakirjapohja;
import fi.arcusys.koku.util.Properties;


/**
 * Warrant (or authorization) services
 * 
 * @author Toni Turunen
 *
 */
public class KokuEmployeeWarrantService {	
	
	private final KokuLooraValtakirjaService service;
	
	public KokuEmployeeWarrantService() {
		KokuLooraValtakirjaService_Service serviceInit = new KokuLooraValtakirjaService_Service();
		service = serviceInit.getKokuLooraValtakirjaServicePort();
		((BindingProvider)service).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, Properties.KOKU_LOORA_VALTAKIRJA_SERVICE);		
	}
	
	/**
	 * Returns number of authorizations by given criteria
	 * 
	 * @param criteria
	 * @return number of authorizations
	 * @throws KokuServiceException
	 */
	public int getTotalAuthorizations(AuthorizationCriteria criteria) throws KokuServiceException {
		try {
			return service.getTotalAuthorizations(criteria);
		} catch(RuntimeException e) {
			if (criteria != null) {
				throw new KokuServiceException("getTotalAuthorizations failed. " +
						"receipientUid: '"+	criteria.getReceipientUid()+
						"' senderUid: '"+ criteria.getSenderUid() +
						"' targetPersonUid: '"+ criteria.getTargetPersonUid()+"'", e);
			} else {
				throw new KokuServiceException("getTotalAuthorizations failed. criteria: null", e);
			}
		}
	}
	
	/**
	 * Return list of authorizatins by given query
	 * 
	 * @param query
	 * @return list of authorizations
	 * @throws KokuServiceException
	 */
	public List<AuthorizationShortSummary> getAuthorizations(AuthorizationQuery query) throws KokuServiceException {
		try {
			return service.getAuthorizations(query);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getAuthorizations failed. query: '"+query+"'", e);
		}
	}
	
	/**
	 * Returns authorization with additional details by given authorizationId
	 * 
	 * @param valtakirjaId authorizationId
	 * @return authorization with additional details
	 * @throws KokuServiceException
	 */
	public AuthorizationSummary getAuthorizationDetails(int valtakirjaId) throws KokuServiceException {
		try {
			return service.getAuthorizationDetails(valtakirjaId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getAuthorizationDetails failed. valtakirjaId: '"+valtakirjaId+"'", e);
		}
	}
	
	/**
	 * Search authorization templates by given search string
	 * 
	 * @param searchString
	 * @param limit
	 * @return list of authorization templates
	 * @throws KokuServiceException
	 */
	public List<Valtakirjapohja> searchAuthorizationTemplates(String searchString, int limit) throws KokuServiceException {
		try {
			return service.searchAuthorizationTemplates(searchString, limit);
		} catch(RuntimeException e) {
			throw new KokuServiceException("searchAuthorizationTemplates failed. searchString: '"+searchString+"'", e);
		}
	}
	
	
}
