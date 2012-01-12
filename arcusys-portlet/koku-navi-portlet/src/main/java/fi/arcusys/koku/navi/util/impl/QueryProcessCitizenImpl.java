package fi.arcusys.koku.navi.util.impl;

import static fi.arcusys.koku.util.Constants.JSON_APPOINTMENT_TOTAL;
import static fi.arcusys.koku.util.Constants.JSON_ARCHIVE_INBOX;
import static fi.arcusys.koku.util.Constants.JSON_CONSENTS_TOTAL;
import static fi.arcusys.koku.util.Constants.JSON_INBOX;
import static fi.arcusys.koku.util.Constants.JSON_LOGIN_STATUS;
import static fi.arcusys.koku.util.Constants.JSON_REQUESTS_TOTAL;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_APPOINTMENT_INBOX_CITIZEN;
import static fi.arcusys.koku.util.Constants.TOKEN_STATUS_INVALID;
import static fi.arcusys.koku.util.Constants.TOKEN_STATUS_VALID;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import fi.arcusys.koku.av.AvCitizenServiceHandle;
import fi.arcusys.koku.kv.model.KokuFolderType;
import fi.arcusys.koku.tiva.TivaCitizenServiceHandle;
import fi.arcusys.koku.util.PortalRole;

public class QueryProcessCitizenImpl extends AbstractQueryProcess {
	
	private static final Logger LOGGER = Logger.getLogger(QueryProcessCitizenImpl.class);	

	public QueryProcessCitizenImpl(MessageSource messages) {
		super(messages);
	}

	@Override
	public JSONObject getJsonModel(String username, String userId, String token, PortalRole role) {
		JSONObject jsonModel = new JSONObject();
		if (userId == null) {
			jsonModel.put(JSON_LOGIN_STATUS, TOKEN_STATUS_INVALID);
		} else {			
			jsonModel.put(JSON_LOGIN_STATUS, TOKEN_STATUS_VALID);			
			jsonModel.put(JSON_INBOX, String.valueOf(getNewMessageNum(userId, KokuFolderType.INBOX)));			
			jsonModel.put(JSON_ARCHIVE_INBOX, String.valueOf(getNewMessageNum(userId, KokuFolderType.ARCHIVE_INBOX)));
			jsonModel.put(JSON_CONSENTS_TOTAL, String.valueOf(getTotalAssignedConsents(userId)));
			jsonModel.put(JSON_APPOINTMENT_TOTAL, String.valueOf(getTotalAssignedAppointments(userId)));
			try {
				jsonModel.put(JSON_REQUESTS_TOTAL, String.valueOf(getTotalRequests(userId, token)));
			} catch (Exception e) {
				LOGGER.error("Coulnd't get TotalRequests (Valtakirja yht.). See following errorMsg: "+e.getMessage());
			}
		}
		return jsonModel;
	}
	
	
	/**
	 * Returns total amount of assigned consents (not just new ones)
	 * 
	 * @param userId
	 * @return number of assigned consents
	 */
	private int getTotalAssignedConsents(String userId) {
		TivaCitizenServiceHandle handle = new TivaCitizenServiceHandle();
		return handle.getTotalAssignedConsents(userId);
	}
	
	/**
	 * Returns total amount of assigned appointments
	 * 
	 * @param userId
	 * @return number of assigned appointments
	 */
	private int getTotalAssignedAppointments(String userId) {
		AvCitizenServiceHandle handle = new AvCitizenServiceHandle(userId);
		return handle.getTotalAppointmentsNum(userId, TASK_TYPE_APPOINTMENT_INBOX_CITIZEN);
	}
	


}
