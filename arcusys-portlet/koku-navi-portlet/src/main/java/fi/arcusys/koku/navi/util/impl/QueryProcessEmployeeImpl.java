package fi.arcusys.koku.navi.util.impl;

import static fi.arcusys.koku.util.Constants.JSON_ARCHIVE_INBOX;
import static fi.arcusys.koku.util.Constants.JSON_INBOX;
import static fi.arcusys.koku.util.Constants.JSON_INFO_REQUESTS_TOTAL;
import static fi.arcusys.koku.util.Constants.JSON_LOGIN_STATUS;
import static fi.arcusys.koku.util.Constants.JSON_REQUESTS_TOTAL;
import static fi.arcusys.koku.util.Constants.TOKEN_STATUS_INVALID;
import static fi.arcusys.koku.util.Constants.TOKEN_STATUS_VALID;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.kv.model.KokuFolderType;
import fi.arcusys.koku.util.PortalRole;
import static fi.arcusys.koku.util.Properties.RECEIVED_REQUESTS_FILTER;
import static fi.arcusys.koku.util.Properties.RECEIVED_INFO_REQUESTS_FILTER;


public class QueryProcessEmployeeImpl extends AbstractQueryProcess {
	
	private static final Logger LOG = Logger.getLogger(QueryProcessEmployeeImpl.class);	

	public QueryProcessEmployeeImpl(MessageSource messages) {
		super(messages);
	}

	@Override
	public JSONObject getJsonModel(String username, String userId, String token, PortalRole role) {
		JSONObject jsonModel = new JSONObject();
		if (userId == null) {
			jsonModel.put(JSON_LOGIN_STATUS, TOKEN_STATUS_INVALID);
		} else {
			try {					
				jsonModel.put(JSON_LOGIN_STATUS, TOKEN_STATUS_VALID);			
				jsonModel.put(JSON_INBOX, String.valueOf(getNewMessageNum(userId, KokuFolderType.INBOX)));			
				jsonModel.put(JSON_ARCHIVE_INBOX, String.valueOf(getNewMessageNum(userId, KokuFolderType.ARCHIVE_INBOX)));
			} catch (KokuServiceException kse) {
				LOG.error("Failed to get count(s) (message/archive/consensts/appointments. ", kse);
				jsonModel.put(JSON_INBOX, "0");
				jsonModel.put(JSON_ARCHIVE_INBOX, "0");
			}
			
			try {				
				jsonModel.put(JSON_INFO_REQUESTS_TOTAL, getTotalTasks(username, token, RECEIVED_INFO_REQUESTS_FILTER));
				jsonModel.put(JSON_REQUESTS_TOTAL, String.valueOf(getTotalTasks(userId, token, RECEIVED_REQUESTS_FILTER)));
			} catch (Exception e) {
				LOG.error("Coulnd't get TotalRequests (Saapuneet pyynnöt / Tietopyynnöt). See following errorMsg: "+e.getMessage(), e);
				jsonModel.put(JSON_INFO_REQUESTS_TOTAL, "0");
				jsonModel.put(JSON_REQUESTS_TOTAL, "0");
			}
		}
		return jsonModel;
	}
	
	

}
