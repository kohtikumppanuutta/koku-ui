package fi.arcusys.koku.tiva.warrant;

import java.net.URL;
import java.util.List;

import fi.arcusys.koku.tiva.kunpovaltakirjaservice.AuthorizationShortSummary;
import fi.arcusys.koku.tiva.kunpovaltakirjaservice.AuthorizationSummary;
import fi.arcusys.koku.tiva.kunpovaltakirjaservice.KokuKunpoValtakirjaService_Service;

public class KokuWarrantService {
	
	public final URL WARRANT_SERVICE_WSDL_LOCATION = getClass().getClassLoader().getResource("KokuKunpoValtakirjaService.wsdl");
	private KokuKunpoValtakirjaService_Service service;
	
	
	public KokuWarrantService() {
		service = new KokuKunpoValtakirjaService_Service(WARRANT_SERVICE_WSDL_LOCATION);
	}
	
	public AuthorizationSummary getAuthorizationSummaryById(long arg0, String arg1) {
		return service.getKokuKunpoValtakirjaServicePort().getAuthorizationSummaryById(arg0, arg1);
	}
	
	public List<AuthorizationShortSummary> getReceivedAuthorizations(String arg0, int arg1, int arg2) {
		return service.getKokuKunpoValtakirjaServicePort().getReceivedAuthorizations(arg0, arg1, arg2);
	}
	
	public List<AuthorizationShortSummary> getSentAuthorizations(String arg0, int arg1, int arg2) {
		return service.getKokuKunpoValtakirjaServicePort().getSentAuthorizations(arg0, arg1, arg2);
	}
	
	public int getTotalSentAuthorizations(String arg0) {
		return service.getKokuKunpoValtakirjaServicePort().getTotalSentAuthorizations(arg0);
	}
	
}
