
package fi.arcusys.koku.tiva.warrant.employee;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationCriteria;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationQuery;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationShortSummary;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationSummary;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.KokuLooraValtakirjaService_Service;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.Valtakirjapohja;
import fi.arcusys.koku.util.PropertiesUtil;
import fi.koku.settings.KoKuPropertiesUtil;


public class KokuEmployeeWarrantService {	
	
	private static final Logger LOG = Logger.getLogger(KokuEmployeeWarrantService.class);		
	public static final URL WARRANT_SERVICE_WSDL_LOCATION;
	
	static {
		try {
			LOG.info("KokuLooraValtakirjaService WSDL location: " + KoKuPropertiesUtil.get("KokuLooraValtakirjaService"));
			WARRANT_SERVICE_WSDL_LOCATION =  new URL(KoKuPropertiesUtil.get("KokuLooraValtakirjaService"));
		} catch (MalformedURLException e) {
			LOG.error("Failed to create KokuLooraValtakirjaService WSDL url! Given URL address is not valid!");
			throw new ExceptionInInitializerError(e);
		}
	}
	
	private KokuLooraValtakirjaService_Service service;
	
	
	public KokuEmployeeWarrantService() {
		service = new KokuLooraValtakirjaService_Service(WARRANT_SERVICE_WSDL_LOCATION);
	}
	
	public int getTotalAuthorizations(AuthorizationCriteria criteria) {
		return service.getKokuLooraValtakirjaServicePort().getTotalAuthorizations(criteria);
	}
	
	public List<AuthorizationShortSummary> getAuthorizations(AuthorizationQuery query) {
		return service.getKokuLooraValtakirjaServicePort().getAuthorizations(query);
	}
	
	public AuthorizationSummary getAuthorizationDetails(int valtakirjaId) {
		return service.getKokuLooraValtakirjaServicePort().getAuthorizationDetails(valtakirjaId);
	}
	
	public List<Valtakirjapohja> searchAuthorizationTemplates(String searchString, int limit) {
		return service.getKokuLooraValtakirjaServicePort().searchAuthorizationTemplates(searchString, limit);
	}
	
}
