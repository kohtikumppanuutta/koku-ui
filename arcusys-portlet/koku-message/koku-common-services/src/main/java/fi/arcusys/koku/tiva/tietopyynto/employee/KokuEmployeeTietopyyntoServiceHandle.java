package fi.arcusys.koku.tiva.tietopyynto.employee;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fi.arcusys.koku.tiva.tietopyynto.AbstractTietopyyntoHandle;
import fi.arcusys.koku.tiva.tietopyynto.model.KokuInformationRequestSummary;
import fi.arcusys.koku.users.KokuUserService;
import fi.arcusys.koku.util.MessageUtil;

public class KokuEmployeeTietopyyntoServiceHandle extends AbstractTietopyyntoHandle {
	
	private Logger LOG = Logger.getLogger(KokuEmployeeTietopyyntoServiceHandle.class);

	private final KokuEmployeeTietopyyntoService service;	
	private final KokuUserService userService;	
	
	/* SearchMap key values */
	private final int MAP_INIT_SIZE					= 7;
	private final String CREATED_FROM 				= "createdFrom";
	private final String CREATED_TO 				= "createdTo";
	private final String REPLIED_FROM 				= "repliedFrom";
	private final String REPLIED_TO 				= "repliedTo";
	private final String SENDER_UID 				= "senderUid";
	private final String RECIEVER_UID 				= "recieverUid";	
	private final String TARGET_PERSON_UID 			= "targetPersonUid";
	
	private final String REGEX_DATE					= "\\.";
	
	/**
	 * Constructor and initialization
	 */
	public KokuEmployeeTietopyyntoServiceHandle() {
		service = new KokuEmployeeTietopyyntoService();
		userService = new KokuUserService();
	}
	
	
	/**
	 * Returns list of KokuInformationRequestSummaries. (Tietopyyntö)
	 * 
	 * @param keyword
	 * @param startNum
	 * @param maxNum
	 * @return List of KokuInformationRequestSummaries
	 */
	public List<KokuInformationRequestSummary> getRepliedRequests(String userId, String keyword, int startNum, int maxNum) {
		Map<String, String> searchMap = getSearchMap(keyword);
		return getRepliedRequests(
				userId, 
				stringToDate(searchMap.get(CREATED_FROM)), 
				stringToDate(searchMap.get(CREATED_TO)), 
				stringToDate(searchMap.get(REPLIED_FROM)),
				stringToDate(searchMap.get(REPLIED_TO)), 
				searchMap.get(SENDER_UID), 
				searchMap.get(RECIEVER_UID), 
				searchMap.get(TARGET_PERSON_UID),
				startNum,
				maxNum
			);
	}
	
	/** 
	 * Parses string dates on finnish time format 
	 * 
	 * @param date
	 * @return Date 
	 */
	private Date stringToDate(String date) {		
		if (date == null || date.isEmpty()) {
			return null;
		}
		
		String[] splitted = date.split("\\.");
		if (splitted.length != 3) {
			return null;
		}
		Date parsedDate = new Date();
		parsedDate.setDate(Integer.valueOf(splitted[0]));
		parsedDate.setMonth(Integer.valueOf(splitted[1]));
		parsedDate.setYear(Integer.valueOf(splitted[2]));
		return parsedDate;
		
	}
	
	private Map<String, String> getSearchMap(String keyword) {
		if (keyword == null) {
			return null;
		}
		
		Map<String, String> searchMap = new HashMap<String, String>(MAP_INIT_SIZE);
		String[] split = keyword.split(SPLIT_REGEX);
		if (split.length == 0) {
			return null;
		}
		// FIXME: Not like this..  
		for (int i = 0; i < split.length; i++) {
			if (i == 0) {
				searchMap.put(CREATED_FROM, split[i]);				
			}
			if (i == 1) {
				searchMap.put(CREATED_TO, split[i]);				
			}
			if (i == 2) {
				searchMap.put(REPLIED_FROM, split[i]);				
			}
			if (i == 3) {
				searchMap.put(REPLIED_TO, split[i]);				
			}			
			if (i == 4) {
				searchMap.put(SENDER_UID, split[i]);				
			}
			if (i == 5) {
				searchMap.put(RECIEVER_UID, split[i]);				
			}
			if (i == 6) {
				searchMap.put(TARGET_PERSON_UID, split[i]);				
			}
		}
		return searchMap;
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
