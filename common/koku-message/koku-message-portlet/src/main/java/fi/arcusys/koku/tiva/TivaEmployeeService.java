package fi.arcusys.koku.tiva;

import java.net.URL;
import java.util.List;

import fi.arcusys.koku.tiva.employeeservice.ConsentQuery;
import fi.arcusys.koku.tiva.employeeservice.ConsentSummary;
import fi.arcusys.koku.tiva.employeeservice.ConsentTO;
import fi.arcusys.koku.tiva.employeeservice.ConsentTemplateSummary;
import fi.arcusys.koku.tiva.employeeservice.KokuLooraSuostumusService_Service;

/**
 * 
 * @author Jinhua Chen
 * Aug 15, 2011
 */
public class TivaEmployeeService {
	public final URL TIVA_EMPLOYEE_WSDL_LOCATION = getClass().getClassLoader().getResource("TivaEmployeeService.wsdl");
	private KokuLooraSuostumusService_Service kls;
	
	public TivaEmployeeService() {
		this.kls = new KokuLooraSuostumusService_Service(TIVA_EMPLOYEE_WSDL_LOCATION);
	}
	
	public List<ConsentSummary> getConsents(String user, ConsentQuery query) {
		return kls.getKokuLooraSuostumusServicePort().getConsents(user, query);
	}
	
	public ConsentTO getConsentDetails(long consentId) {
		return kls.getKokuLooraSuostumusServicePort().getConsentDetails(consentId);
	}
	
	public int getTotalConsents(String user, ConsentQuery query) {
		return kls.getKokuLooraSuostumusServicePort().getTotalConsents(user, query);
	}
	
	public List<ConsentTemplateSummary> searchConsentTemplates(String searchString, int limit) {
		return kls.getKokuLooraSuostumusServicePort().searchConsentTemplates(searchString, limit);
	}
}
