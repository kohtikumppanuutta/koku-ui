package fi.arcusys.koku.tiva;

import java.util.List;

import javax.xml.ws.BindingProvider;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.tiva.employeeservice.ConsentCriteria;
import fi.arcusys.koku.tiva.employeeservice.ConsentQuery;
import fi.arcusys.koku.tiva.employeeservice.ConsentSummary;
import fi.arcusys.koku.tiva.employeeservice.ConsentTO;
import fi.arcusys.koku.tiva.employeeservice.KokuLooraSuostumusService;
import fi.arcusys.koku.tiva.employeeservice.KokuLooraSuostumusService_Service;
import fi.arcusys.koku.tiva.employeeservice.SuostumuspohjaShort;
import fi.arcusys.koku.util.Properties;

/**
 * Retrieves Tiva consent data and related operations via web services
 * @author Jinhua Chen
 * Aug 15, 2011
 */
public class TivaEmployeeService {
				
	private final KokuLooraSuostumusService service;
	
	/**
	 * Constructor and initialization
	 */
	public TivaEmployeeService() {
		KokuLooraSuostumusService_Service kls = new KokuLooraSuostumusService_Service();
		service = kls.getKokuLooraSuostumusServicePort();
		((BindingProvider)service).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, Properties.TIVA_EMPLOYEE_SERVICE);
	}
	
	/**
	 * Gets consents
	 * @param user user name
	 * @param query query for filtering consents
	 * @return a list of summary consents
	 */
	public List<ConsentSummary> getConsents(String user, ConsentQuery query) throws KokuServiceException {
		try {
			return service.getConsents(user, query);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getConsents failed. User: '"+user+"'", e);
		}
	}
	
	/**
	 * Gets consent in detail
	 * @param consentId consent id
	 * @return detailed consent
	 */
	public ConsentTO getConsentDetails(long consentId) throws KokuServiceException {
		try {
			return service.getConsentDetails(consentId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getConsentDetails failed. consentId: '"+consentId+"'", e);
		}
	}
	
	/**
	 * Gets total number of consents
	 * @param user user name
	 * @param query query for filtering consents
	 * @return the total number of consents
	 */
	public int getTotalConsents(String user, ConsentCriteria criteria) throws KokuServiceException {
		try {
			return service.getTotalConsents(user, criteria);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalConsents failed. User: '"+user+"'", e);
		}
	}
	
	/**
	 * Searches the consent templates
	 * @param searchString searching keyword
	 * @param limit limited number of results
	 * @return a list of templates
	 */
	public List<SuostumuspohjaShort> searchConsentTemplates(String searchString, int limit) throws KokuServiceException {
		try {
			return service.searchConsentTemplates(searchString, limit);
		} catch(RuntimeException e) {
			throw new KokuServiceException("searchConsentTemplates failed. User: '"+searchString+"' limit: '"+limit+"'", e);
		}
	}
}
