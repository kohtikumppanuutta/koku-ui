package fi.arcusys.koku.tiva.warrant.employee;

import java.util.ArrayList;
import java.util.List;

import fi.arcusys.koku.AbstractHandle;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationCriteria;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationQuery;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationShortSummary;
import fi.arcusys.koku.tiva.warrant.model.AuthorizationSummary;
import fi.arcusys.koku.tiva.warrant.model.Valtakirjapohja;

public class KokuEmployeeWarrantHandle extends AbstractHandle {

	private final KokuEmployeeWarrantService service;

	public KokuEmployeeWarrantHandle() {
		service = new KokuEmployeeWarrantService();		
	}
	
	public List<AuthorizationSummary> getAuthorizations(long authorizationTemplateId, String receipientUid, String senderUid, int startNum, int maxNum) {
		AuthorizationQuery query = new AuthorizationQuery();
		AuthorizationCriteria criteria = new AuthorizationCriteria();
		criteria.setAuthorizationTemplateId(authorizationTemplateId);
		criteria.setReceipientUid(receipientUid);
		criteria.setSenderUid(senderUid);
		query.setCriteria(criteria);
		query.setStartNum(startNum);
		query.setMaxNum(maxNum);
		List<AuthorizationShortSummary> authShortSummaries = service.getAuthorizations(query);
		List<AuthorizationSummary> summaries = new ArrayList<AuthorizationSummary>();
		for (AuthorizationShortSummary shortSummary : authShortSummaries) {
			summaries.add(new AuthorizationSummary(shortSummary));
		}
		return summaries;
	}
	
	public int getTotalAuthorizations(long authorizationTemplateId, String receipientUid, String senderUid) {
		AuthorizationCriteria criteria = new AuthorizationCriteria();
		criteria.setAuthorizationTemplateId(authorizationTemplateId);
		criteria.setReceipientUid(receipientUid);
		criteria.setSenderUid(senderUid);
		return service.getTotalAuthorizations(criteria);
	}

	public AuthorizationSummary getAuthorizationDetails(int valtakirjaId) {
		return new AuthorizationSummary(service.getAuthorizationDetails(valtakirjaId));
	}

	public List<Valtakirjapohja> searchAuthorizationTemplates(String searchString, int limit) {
		List<Valtakirjapohja> valtakirjas = new ArrayList<Valtakirjapohja>();
		List<fi.arcusys.koku.tiva.warrant.employeewarrantservice.Valtakirjapohja> valtakirjasFromService = service.searchAuthorizationTemplates(searchString, limit);
		for (fi.arcusys.koku.tiva.warrant.employeewarrantservice.Valtakirjapohja valtakirjapohja : valtakirjasFromService) {
			valtakirjas.add(new Valtakirjapohja(valtakirjapohja));
		}
		return valtakirjas;
	}
	
}
