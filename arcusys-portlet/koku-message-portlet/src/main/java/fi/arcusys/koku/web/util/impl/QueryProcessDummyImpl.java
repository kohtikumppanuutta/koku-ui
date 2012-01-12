package fi.arcusys.koku.web.util.impl;

import static fi.arcusys.koku.util.Constants.JSON_LOGIN_STATUS;
import static fi.arcusys.koku.util.Constants.TOKEN_STATUS_INVALID;

import org.springframework.context.MessageSource;

import net.sf.json.JSONObject;

public class QueryProcessDummyImpl extends AbstractQueryProcess {
	
	public QueryProcessDummyImpl(MessageSource messages) {
		super(messages);
	}

	@Override
	public JSONObject getJsonModel(String taskType, int page, String keyword, String field, String orderType, String userUid) {
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_LOGIN_STATUS, TOKEN_STATUS_INVALID);
		return jsonModel;
	}

}
