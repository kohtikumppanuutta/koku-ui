package fi.arcusys.koku.web.util.impl;

import fi.arcusys.koku.web.util.ModelWrapper;
import fi.arcusys.koku.web.util.ResponseStatus;

/**
 * Abstract ModelWrapper class
 * 
 * @author Toni Turunen
 *
 * @param <T>
 */
public class ModelWrapperImpl<T> implements ModelWrapper<T> {

	private final T model;
	private final ResponseStatus responseStatus;
	private final String errorCode;

	/**
	 * Creates new ModelWrapper. Note that model can't be null otherwise responseStatus
	 * will be set FAIL
	 * 
	 * @param message
	 */
	public ModelWrapperImpl(T model) {
		this(model, ResponseStatus.OK, null);		
	}
	
	/**
	 * Creates new ModelWrapper. Note that model can't be null otherwise responseStatus
	 * will be set FAIL. Also if responseStatus is null, it will be set FAIL automatically.
	 * 
	 * @param message
	 * @param responseStatus
	 */
	public ModelWrapperImpl(T model, ResponseStatus responseStatus) {
		this(model, responseStatus, null);
	}
	
	/**
	 * Creates new ModelWrapper. Note that model can't be null otherwise responseStatus
	 * will be set FAIL. Also if responseStatus is null, it will be set FAIL automatically.
	 * 
	 * @param message
	 * @param responseStatus
	 * @param errorCode
	 */
	public ModelWrapperImpl(T model, ResponseStatus responseStatus, String errorCode) {
		this.model = model;
		this.errorCode = errorCode;
		if (model != null && responseStatus != null) {			
			this.responseStatus = responseStatus;
		} else {
			this.responseStatus = ResponseStatus.FAIL;
		}
	}
	
	@Override
	public T getModel() {
		return model;
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
