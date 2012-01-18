package fi.arcusys.koku.web.util;

import org.springframework.context.MessageSource;

import net.sf.json.JSONObject;


/**
 * General interface for retrieving koku tasks
 * 
 * @author Toni Turunen
 *
 */
public interface QueryProcess {
	
	JSONObject getJsonModel(String taskType, int page, String keyword, String field, String orderType, String userUid);
	
	void setMessageSource(MessageSource messages);
	MessageSource getMessageSource();

}
