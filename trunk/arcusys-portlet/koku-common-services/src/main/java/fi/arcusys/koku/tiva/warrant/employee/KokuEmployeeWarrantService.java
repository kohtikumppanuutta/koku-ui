
package fi.arcusys.koku.tiva.warrant.employee;

import java.net.URL;
import java.util.List;

import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationCriteria;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationQuery;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationShortSummary;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationSummary;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.KokuLooraValtakirjaService_Service;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.Valtakirjapohja;


public class KokuEmployeeWarrantService {
	
	public final URL WARRANT_SERVICE_WSDL_LOCATION = getClass().getClassLoader().getResource("KokuLooraValtakirjaServiceImpl.wsdl");
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
