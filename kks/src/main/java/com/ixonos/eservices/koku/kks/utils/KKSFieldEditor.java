package com.ixonos.eservices.koku.kks.utils;

import java.beans.PropertyEditorSupport;

import com.ixonos.eservices.koku.kks.mock.KKSService;
import com.ixonos.eservices.koku.kks.utils.enums.KKSField;

/**
 * Property editor for KKSField type properties.
 * 
 * @author tuomape
 */
public class KKSFieldEditor extends PropertyEditorSupport {

	private KKSService service;

	public KKSFieldEditor(KKSService service) {
		super();
		this.service = service;
	}

	@Override
	public String getAsText() {
		String returnVal = "";
		if (getValue() instanceof KKSField) {
			returnVal = ((KKSField) getValue()).getId();
		}
		if (getValue() != null && getValue() instanceof String[]) {
			returnVal = ((String[]) getValue())[0];
		}
		return returnVal;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {

		setValue(service.getField(text));
	}
}