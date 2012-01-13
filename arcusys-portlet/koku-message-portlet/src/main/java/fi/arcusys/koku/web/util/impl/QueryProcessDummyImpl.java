package fi.arcusys.koku.web.util.impl;

import net.sf.json.JSONObject;

import org.springframework.context.MessageSource;

public class QueryProcessDummyImpl extends AbstractQueryProcess {
	
	public QueryProcessDummyImpl(MessageSource messages) {
		super(messages);
	}

	@Override
	public void setJsonTasks(JSONObject jsonModel, String taskType, int page, String keyword, String field, String orderType, String userUid) {
		return;
	}

}
