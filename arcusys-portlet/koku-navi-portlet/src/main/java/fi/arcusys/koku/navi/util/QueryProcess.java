package fi.arcusys.koku.navi.util;

import org.springframework.context.MessageSource;

import fi.arcusys.koku.util.PortalRole;

import net.sf.json.JSONObject;

public interface QueryProcess {
	
	/**
	 * Returns the amount of new messages of Inbox and Archive_Inbox and puts values to model
	 * 
	 * @param userId user that message belong to
	 * @return Json object contains result
	 */
	public JSONObject getJsonModel(String username, String userId, String token, PortalRole role);
	
	void setMessageSource(MessageSource messages);
	MessageSource getMessageSource();

}
