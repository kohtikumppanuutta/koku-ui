package fi.arcusys.koku.web.util.impl;

import static fi.arcusys.koku.util.Constants.JSON_FAILURE_UUID;
import static fi.arcusys.koku.util.Constants.JSON_LOGIN_STATUS;
import static fi.arcusys.koku.util.Constants.JSON_RESULT;
import static fi.arcusys.koku.util.Constants.RESPONSE_FAIL;
import static fi.arcusys.koku.util.Constants.TOKEN_STATUS_INVALID;

import java.util.UUID;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import fi.arcusys.koku.web.util.KokuTaskQueryProcess;

public abstract class AbstractQueryProcess implements KokuTaskQueryProcess {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractQueryProcess.class);
	private MessageSource messageSource;
	
	protected abstract void setJsonTasks(JSONObject jsonModel, String taskType, int page, String keyword, String field, String orderType, String userUid);
	
	public AbstractQueryProcess(MessageSource messages) {
		this.messageSource = messages;
	}
	
	@Override
	public JSONObject getJsonModel(String taskType, int page, String keyword, String field, String orderType, String userUid) {
		JSONObject jsonModel = new JSONObject();		
		try {			
			if (userUid == null) {
				jsonModel.put(JSON_LOGIN_STATUS, TOKEN_STATUS_INVALID);
				LOG.info("No logged in user");
			} else {
				setJsonTasks(jsonModel, taskType, page, keyword, field, orderType, userUid);
			}
		} catch (RuntimeException e) {
			String uuid = UUID.randomUUID().toString();
			LOG.error("Something went very wrong while trying to query tasks. Koku errorcode: '"+uuid+"'", e);
			jsonModel.put(JSON_RESULT, RESPONSE_FAIL);
			jsonModel.put(JSON_FAILURE_UUID, uuid);
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
