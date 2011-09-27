package fi.arcusys.koku.tiva.tietopyynto.employee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.koku.tiva.tietopyynto.AbstractTietopyyntoHandle;
import fi.arcusys.koku.tiva.tietopyynto.model.KokuInformationRequestSummary;
import fi.arcusys.koku.users.KokuUserService;
import fi.arcusys.koku.util.MessageUtil;

public class KokuEmployeeTietopyyntoServiceHandle extends AbstractTietopyyntoHandle {
	
	private Logger LOG = Logger.getLogger(KokuEmployeeTietopyyntoServiceHandle.class);

	private final KokuEmployeeTietopyyntoService service;	
	private final KokuUserService userService;	
	
	/**
	 * Constructor and initialization
	 */
	public KokuEmployeeTietopyyntoServiceHandle() {
		service = new KokuEmployeeTietopyyntoService();
		userService = new KokuUserService();
	}
	
	public List<KokuInformationRequestSummary> getRepliedRequests(
			String userId, Date createdFrom, Date createdTo, Date repliedFrom, Date repliedTo, 
			String senderUid, String recieverUid, String targetPersonUid, int startNum, int maxNum) {
		
		InformationRequestCriteria criteria = new InformationRequestCriteria();
		criteria.setCreatedFromDate(MessageUtil.convertDateToXMLGregorianCalendar(createdFrom));
		criteria.setCreatedToDate(MessageUtil.convertDateToXMLGregorianCalendar(createdTo));
		criteria.setRepliedFromDate(MessageUtil.convertDateToXMLGregorianCalendar(repliedFrom));
		criteria.setRepliedToDate(MessageUtil.convertDateToXMLGregorianCalendar(repliedTo));
		criteria.setTargetPersonUid(targetPersonUid);
		criteria.setReceiverUid(recieverUid);
		criteria.setSenderUid(senderUid);
		return getRepliedRequests(userId, criteria, startNum, maxNum);
	}
		
	private List<KokuInformationRequestSummary> getRepliedRequests(String userId, InformationRequestCriteria criteria, int startNum, int maxNum) {
		
		InformationRequestQuery query = new InformationRequestQuery();
		query.setStartNum(startNum);
		query.setMaxNum(maxNum);	
		query.setCriteria(criteria);
		
		List<InformationRequestSummary> summaries = service.getRepliedRequests(userId, query);
		List<KokuInformationRequestSummary> kokuSummaries = new ArrayList<KokuInformationRequestSummary>();
		for (InformationRequestSummary summary : summaries) {
			KokuInformationRequestSummary kokuSummary = new KokuInformationRequestSummary(summary);
			if (kokuSummary.getStatus() != null) {
				kokuSummary.setLocalizedStatus(getLocalizedInformationRequestSummary(summary.getStatus()));
			}
			if (kokuSummary.getSenderUid() != null && kokuSummary.getReceiverUid() != null) {
				kokuSummary.setSenderName(userService.getUserUidByKunpoName(summary.getSenderUid()));
				kokuSummary.setRecieverName(userService.getUserUidByKunpoName(summary.getReceiverUid()));
			}
			kokuSummaries.add(kokuSummary);
		}
		return kokuSummaries;
	}
}
