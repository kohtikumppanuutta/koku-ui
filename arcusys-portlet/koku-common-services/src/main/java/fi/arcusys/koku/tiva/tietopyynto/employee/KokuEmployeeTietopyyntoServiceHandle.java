package fi.arcusys.koku.tiva.tietopyynto.employee;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import fi.arcusys.koku.tiva.tietopyynto.AbstractTietopyyntoHandle;
import fi.arcusys.koku.tiva.tietopyynto.model.KokuInformationRequestDetail;
import fi.arcusys.koku.tiva.tietopyynto.model.KokuInformationRequestSummary;
import fi.arcusys.koku.users.KokuUserService;
import fi.arcusys.koku.util.Constants;
import fi.arcusys.koku.util.MessageUtil;

public class KokuEmployeeTietopyyntoServiceHandle extends AbstractTietopyyntoHandle {
	
	private final static Logger LOG = Logger.getLogger(KokuEmployeeTietopyyntoServiceHandle.class);

	private final KokuEmployeeTietopyyntoService service;	
	private final KokuUserService userService;	
	
	/* SearchMap key values */
	private final static int MAP_INIT_SIZE					= 7;
	private final static String CREATED_FROM 				= "createdFrom";
	private final static String CREATED_TO 					= "createdTo";
	private final static String REPLIED_FROM 				= "repliedFrom";
	private final static String REPLIED_TO 					= "repliedTo";
	private final static String SENDER_UID 					= "senderUid";
	private final static String RECIEVER_UID 				= "recieverUid";	
	private final static String TARGET_PERSON_UID 			= "targetPersonUid";
	private final static String INFORMATION					= "searchFromTietosisalto";
	private final static String FREE_TEXT_SEARCH			= "freeTextSearch";
		
	private final static String REGEX_DATE					= "\\.";
	
	/**
	 * Constructor and initialization
	 */
	public KokuEmployeeTietopyyntoServiceHandle() {
		service = new KokuEmployeeTietopyyntoService();
		userService = new KokuUserService();
	}
	
	/**
	 * Returns list of replied KokuInformationRequestSummaries. (Tietopyyntö)
	 * 
	 * @param keyword
	 * @param startNum
	 * @param maxNum
	 * @return List of KokuInformationRequestSummaries
	 */
	public List<KokuInformationRequestSummary> getRepliedRequests(String userId, String keyword, int startNum, int maxNum) {
		Map<String, String> searchMap = getSearchMap(keyword);
		if (searchMap == null) {
			return new ArrayList<KokuInformationRequestSummary>();
		};		
		
		return getRepliedRequests(
				userId, 
				createCriteria(searchMap),
				startNum,
				maxNum
			);
	}

	/**
	 * Returns list of sent KokuInformationRequestSummaries. (Tietopyyntö)
	 * 
	 * @param keyword
	 * @param startNum
	 * @param maxNum
	 * @return List of KokuInformationRequestSummaries
	 */	
	public List<KokuInformationRequestSummary> getSentRequests(String userId, String keyword, int startNum, int maxNum) {
		Map<String, String> searchMap = getSearchMap(keyword);
		if (searchMap == null) {
			return new ArrayList<KokuInformationRequestSummary>();
		};
		
		return getSentRequests(
				userId, 
				createCriteria(searchMap),
				startNum,
				maxNum
			);
	}
	
	/**
	 * Returns list of KokuInformationRequestSummaries. (Tietopyyntö) For admin users only
	 * 
	 * @param keyword
	 * @param startNum
	 * @param maxNum
	 * @return List of KokuInformationRequestSummaries
	 */	
	public List<KokuInformationRequestSummary> getRequests(String keyword, int startNum, int maxNum) {
		Map<String, String> searchMap = getSearchMap(keyword);
		if (searchMap == null) {
			return new ArrayList<KokuInformationRequestSummary>();
		};
		
		return getRequests(
				createCriteria(searchMap),
				startNum,
				maxNum
			);
	}
	
	/**
	 * Returns detailed information about tietopyyntö
	 * 
	 * @param requestId
	 * @return Detailed info
	 */
	public KokuInformationRequestDetail getRequestDetails(long requestId) {
		KokuInformationRequestDetail details =  new KokuInformationRequestDetail(service.getRequestDetails(requestId));
		if (details.getAccessType() != null) {
			details.setLocalizedAccessType(getLocalizedInfoAccessType(details.getAccessType()));			
		}
		localizeDetails(details);
		return details;
	}
	
	private List<KokuInformationRequestSummary> getRepliedRequests(String userId, InformationRequestCriteria criteria, int startNum, int maxNum) {
		 return createLocalPojos(service.getRepliedRequests(userId, getInformationReqQuery(criteria, startNum, maxNum)));
	}
	
	private List<KokuInformationRequestSummary> getSentRequests(String userId, InformationRequestCriteria criteria, int startNum, int maxNum) {
		return createLocalPojos(service.getSentRequests(userId, getInformationReqQuery(criteria, startNum, maxNum)));
	}
	
	private List<KokuInformationRequestSummary> getRequests(InformationRequestCriteria criteria, int startNum, int maxNum) {
		return createLocalPojos(service.getRequests(getInformationReqQuery(criteria, startNum, maxNum)));
	}

	private InformationRequestCriteria createCriteria(Map<String, String> searchMap) {
		InformationRequestCriteria criteria = new InformationRequestCriteria();
		criteria.setCreatedFromDate(MessageUtil.convertDateToXMLGregorianCalendar(stringToDate(searchMap.get(CREATED_FROM))));
		criteria.setCreatedToDate(MessageUtil.convertDateToXMLGregorianCalendar(stringToDate(searchMap.get(CREATED_TO))));
		criteria.setRepliedFromDate(MessageUtil.convertDateToXMLGregorianCalendar(stringToDate(searchMap.get(REPLIED_FROM))));
		criteria.setRepliedToDate(MessageUtil.convertDateToXMLGregorianCalendar(stringToDate(searchMap.get(REPLIED_TO))));
		criteria.setTargetPersonUid(searchMap.get(TARGET_PERSON_UID));
		criteria.setReceiverUid(searchMap.get(RECIEVER_UID));
		criteria.setSenderUid(searchMap.get(SENDER_UID));
		// TODO: FreeText search missing
		// TODO: Tietosisältö search missing
		return criteria;
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
		
		Calendar cal = Calendar.getInstance(new Locale("fi", "FI"));
		cal.set(Integer.valueOf(splitted[2]), Integer.valueOf(splitted[1])-1, Integer.valueOf(splitted[0]), 0, 0, 0);
		return cal.getTime();
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
			if (i == 0) { addToMap(searchMap, CREATED_FROM, split[i]); }
			if (i == 1) { addToMap(searchMap, CREATED_TO, split[i]);	}
			if (i == 2) { addToMap(searchMap, REPLIED_FROM, split[i]);}
			if (i == 3) { addToMap(searchMap, REPLIED_TO, split[i]);	}			
			if (i == 4) { addToMap(searchMap, SENDER_UID, split[i]); }
			if (i == 5) { addToMap(searchMap, RECIEVER_UID, split[i]); }
			if (i == 6) { addToMap(searchMap, TARGET_PERSON_UID, split[i]); }
			if (i == 7) { addToMap(searchMap, INFORMATION, split[i]); }
			if (i == 8) { addToMap(searchMap, FREE_TEXT_SEARCH, split[i]); }
		}
		return searchMap;
	}
	
	private void addToMap(final Map<String, String> map, final String key, final String value) {
		String trimmed = value.trim();
		if (trimmed.isEmpty()) {
			map.put(key, null);
		} else {
			map.put(key, value);			
		}
	}
	
	private List<KokuInformationRequestSummary> createLocalPojos(List<InformationRequestSummary> summaries) {
		List<KokuInformationRequestSummary> kokuSummaries = new ArrayList<KokuInformationRequestSummary>();
		for (InformationRequestSummary summary : summaries) {
			KokuInformationRequestSummary kokuSummary = new KokuInformationRequestSummary(summary);
			localizeSummary(kokuSummary);
			kokuSummaries.add(kokuSummary);
		}
		return kokuSummaries;
	}
	
	private InformationRequestQuery getInformationReqQuery(InformationRequestCriteria criteria, int startNum, int maxNum) {
		InformationRequestQuery query = new InformationRequestQuery();
		query.setStartNum(startNum);
		query.setMaxNum(maxNum);	
		query.setCriteria(criteria);
		return query;
	}
	
	private void localizeDetails(KokuInformationRequestDetail kokuSummary) {
		localizeSummary(kokuSummary);
		if (kokuSummary.getTargetPersonUid() != null) {
			kokuSummary.setTargetPersonName(userService.getKunpoNameByUserUid(kokuSummary.getTargetPersonUid()));
		}
	}
	
	private void localizeSummary(KokuInformationRequestSummary kokuSummary) {
		if (kokuSummary.getStatus() != null) {
			kokuSummary.setLocalizedStatus(getLocalizedInformationRequestSummary(kokuSummary.getStatus()));
		}
		if (kokuSummary.getSenderUid() != null ) {
			kokuSummary.setSenderName(userService.getLooraNameByUserUid(kokuSummary.getSenderUid()));
		}
		if (kokuSummary.getReceiverUid() != null) {
			kokuSummary.setRecieverName(userService.getLooraNameByUserUid(kokuSummary.getReceiverUid()));			
		} else {
			// NOTE: Small hack if ReceiverUid missing. This might change in future.
			kokuSummary.setRecieverName(kokuSummary.getRecieverRoleUid());
		}
		if (kokuSummary.getTargetPersonUid() != null) {
			kokuSummary.setTargetPersonName(userService.getKunpoNameByUserUid(kokuSummary.getTargetPersonUid()));
		}
	}
	
}
