package fi.arcusys.koku.web.util.impl;

import fi.arcusys.koku.kv.model.Message;
import fi.arcusys.koku.web.util.ResponseStatus;

/**
 * ModelWrapper class for Messages (Viestit)
 * 
 * @author Toni Turunen
 *
 */
public class ModelWrapperMessageImpl extends AbstractModelWrapper<Message> {

	public ModelWrapperMessageImpl(Message model) {
		super(model);
	}

	public ModelWrapperMessageImpl(Message model, ResponseStatus responseStatus, String errorCode) {
		super(model, responseStatus, errorCode);
	}

	public ModelWrapperMessageImpl(Message model, ResponseStatus responseStatus) {
		super(model, responseStatus);
	}
}
