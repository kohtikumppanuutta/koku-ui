package fi.arcusys.koku.web.util.impl;

import fi.arcusys.koku.kv.model.Message;
import fi.arcusys.koku.web.util.ModelWrapper;
import fi.arcusys.koku.web.util.ResponseStatus;

public class ModelWrapperMessageImpl implements ModelWrapper<Message> {

	private final Message message;
	private final ResponseStatus responseStatus;
	private final String errorCode;

	/**
	 * Creates new ModelWrapper. Note that message can't be null othervise responseStatus
	 * will be set FAIL
	 * 
	 * @param message
	 */
	public ModelWrapperMessageImpl(Message message) {
		this(message, ResponseStatus.OK, null);		
	}
	
	public ModelWrapperMessageImpl(Message message, ResponseStatus responseStatus) {
		this(message, responseStatus, null);
	}
	
	public ModelWrapperMessageImpl(Message message, ResponseStatus responseStatus, String errorCode) {
		this.message = message;
		this.errorCode = errorCode;
		if (message != null) {			
			this.responseStatus = responseStatus;
		} else {
			this.responseStatus = ResponseStatus.FAIL;
		}
	}
	
	@Override
	public Message getModel() {
		return message;
	}

	@Override
	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}

	@Override
	public String getErrorCode() { 
		return errorCode;
	}

}
