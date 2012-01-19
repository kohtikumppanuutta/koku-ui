package fi.arcusys.koku.web.util.impl;

import static fi.arcusys.koku.util.Constants.JSON_LOGIN_STATUS;
import static fi.arcusys.koku.util.Constants.TOKEN_STATUS_INVALID;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import fi.arcusys.koku.web.util.KokuTaskQueryProcess;

public abstract class AbstractQueryProcess implements KokuTaskQueryProcess {
	
	private static final Logger LOG = Logger.getLogger(AbstractQueryProcess.class);
	private MessageSource messageSource;
	
	protected abstract void setJsonTasks(JSONObject jsonModel, String taskType, int page, String keyword, String field, String orderType, String userUid);
	
	public AbstractQueryProcess(MessageSource messages) {
		this.messageSource = messages;
	}
	
	@Override
	public JSONObject getJsonModel(String taskType, int page, String keyword, String field, String orderType, String userUid) {
	
		JSONObject jsonModel = new JSONObject();		
		if (userUid == null) {
			jsonModel.put(JSON_LOGIN_STATUS, TOKEN_STATUS_INVALID);
			LOG.info("No logged in user");
		} else {
			setJsonTasks(jsonModel, taskType, page, keyword, field, orderType, userUid);
		}
		return jsonModel;
	}
	
	@Override
	public final void setMessageSource(MessageSource messages) {
		this.messageSource = messages;
	}

	@Override
	public final MessageSource getMessageSource() {
		return messageSource;
	}

}
