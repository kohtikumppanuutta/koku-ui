package fi.arcusys.koku.navi.util.impl;

import static fi.arcusys.koku.util.Constants.JSON_LOGIN_STATUS;
import static fi.arcusys.koku.util.Constants.TOKEN_STATUS_INVALID;
import net.sf.json.JSONObject;

import org.springframework.context.MessageSource;

import fi.arcusys.koku.util.PortalRole;

public class QueryProcessDummyImpl extends AbstractQueryProcess {
	
	public QueryProcessDummyImpl(MessageSource messages) {
		super(messages);
	}

	@Override
	public JSONObject getJsonModel(String username, String userId, String token, PortalRole role) {
		JSONObject jsonModel = new JSONObject();
		jsonModel.put(JSON_LOGIN_STATUS, TOKEN_STATUS_INVALID);
		return jsonModel;
	}

}
