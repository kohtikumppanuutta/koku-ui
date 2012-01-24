package fi.arcusys.koku.web.util.impl;

import fi.arcusys.koku.tiva.tietopyynto.model.KokuInformationRequestDetail;
import fi.arcusys.koku.web.util.ResponseStatus;

/**
 * ModelWrapper class for Tietopyyntö
 * 
 * @author Toni Turunen
 *
 */
public class ModelWrapperTipyImpl extends AbstractModelWrapper<KokuInformationRequestDetail> {

	public ModelWrapperTipyImpl(KokuInformationRequestDetail model) {
		super(model);
	}

	public ModelWrapperTipyImpl(KokuInformationRequestDetail model, ResponseStatus responseStatus, String errorCode) {
		super(model, responseStatus, errorCode);
	}

	public ModelWrapperTipyImpl(KokuInformationRequestDetail model, ResponseStatus responseStatus) {
		super(model, responseStatus);
	}
}
