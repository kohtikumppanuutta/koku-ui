package fi.arcusys.koku.tiva.tietopyynto.employee;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import fi.arcusys.koku.tiva.tietopyynto.AbstractTietopyyntoHandle;
import fi.arcusys.koku.tiva.tietopyynto.model.KokuInformationRequestDetail;
import fi.arcusys.koku.tiva.tietopyynto.model.KokuInformationRequestSummary;
import fi.arcusys.koku.users.KokuUserService;
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
	
	/**
	 * Returns total replied requests count 
	 * 
	 * @param receiverUid
	 * @return replied requests count
	 */
	public int getTotalRepliedRequests(String receiverUid) {
		return service.getTotalRepliedRequests(receiverUid, new InformationRequestCriteria());
	}
	
	/**
	 * Returns requests total
	 * 
	 * @param receiverUid
	 * @return request count
	 */
	public int getTotalRequests(String receiverUid) {
		return service.getTotalRequests(new InformationRequestCriteria());
	}
	
	/**
	 * Return sent requests count
	 * 
	 * @param senderUid
	 * @return sent requests count
	 */
	public int getTotalSentRequests(String senderUid) {
		return service.getTotalSentRequests(senderUid, new InformationRequestCriteria());
	}
	
	
	private List<KokuInformationRequestSummary> getRepliedRequests(String userId, InformationRequestCriteria criteria, int startNum, int maxNum) {
		try {
			return createLocalPojos(service.getRepliedRequests(userId, getInformationReqQuery(criteria, startNum, maxNum)));			
		} catch (RuntimeException e) {
			LOG.error("Request: getRepliedRequests has failed", e);
			return new ArrayList<KokuInformationRequestSummary>();
		}
	}
	
	private List<KokuInformationRequestSummary> getSentRequests(String userId, InformationRequestCriteria criteria, int startNum, int maxNum) {
		try {
			return createLocalPojos(service.getSentRequests(userId, getInformationReqQuery(criteria, startNum, maxNum)));
		} catch (RuntimeException e) {
			LOG.error("Request: getSentRequests has failed", e);
			return new ArrayList<KokuInformationRequestSummary>();
		}
	}
	
	private List<KokuInformationRequestSummary> getRequests(InformationRequestCriteria criteria, int startNum, int maxNum) {
		try {
			return createLocalPojos(service.getRequests(getInformationReqQuery(criteria, startNum, maxNum)));
		} catch (RuntimeException e) {
			LOG.error("Request: getRequests has failed", e);
			return new ArrayList<KokuInformationRequestSummary>();
		}
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
		criteria.setFreeText(searchMap.get(FREE_TEXT_SEARCH));
		criteria.setInformationContent(searchMap.get(INFORMATION));
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
		if (keyword == null || keyword.isEmpty()) {
			return null;
		}
		Map<String, String> searchMap = new HashMap<String, String>(MAP_INIT_SIZE);		
		try {
			JSONObject json = new JSONObject(keyword);						
			addToMap(searchMap, CREATED_FROM, json.getString("createdFrom"));
			addToMap(searchMap, CREATED_TO, json.getString("createdTo"));
			addToMap(searchMap, REPLIED_FROM, json.getString("repliedFrom")); 
			addToMap(searchMap, REPLIED_TO, json.getString("repliedTo"));
			final String sender = json.getString("sender");
			final String reciever = json.getString("reciever");
			final String targetPerson = json.getString("targetPerson");
			if (sender != null && !sender.isEmpty()) {
				//addToMap(searchMap, SENDER_UID, userService.getLooraUserUidByUsername(sender));				
				addToMap(searchMap, SENDER_UID, sender);
			}
			if (reciever != null && !reciever.isEmpty()) {
				//addToMap(searchMap, RECIEVER_UID, userService.getLooraUserUidByUsername(reciever));
				addToMap(searchMap, RECIEVER_UID, reciever);
			}
			if (targetPerson != null && !targetPerson.isEmpty()) {
				addToMap(searchMap, TARGET_PERSON_UID, targetPerson);		
			}
			addToMap(searchMap, INFORMATION, json.getString("information"));
			addToMap(searchMap, FREE_TEXT_SEARCH, json.getString("freeTextSearch"));
		} catch (JSONException e) {
			LOG.warn("Failed parse JSON. Can't create searchMap for Tietopyyntö. keyword: '"+keyword+"'");
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
