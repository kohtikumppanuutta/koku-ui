package com.ixonos.eservices.koku.kks.mock;

import java.util.ArrayList;
import java.util.List;

import com.ixonos.eservices.koku.kks.utils.enums.KKSField;
import com.ixonos.eservices.koku.kks.utils.enums.UIField;

/**
 * Model for entry search
 * 
 * @author tuomape
 * 
 */
public class EntrySearch {

	public KKSField mainField;

	public List<KKSField> fields;

	public EntrySearch() {
		mainField = UIField.ALL;
		fields = new ArrayList<KKSField>();
	}

	public List<KKSField> getFields() {
		return fields;
	}

	public List<KKSField> getAllFields() {
		return fields;
	}

	public void setFields(List<KKSField> fields) {
		this.fields = fields;
	}

	public void addField(KKSField field) {
		this.fields.add(field);
	}

	public KKSField getMainField() {
		return mainField;
	}

	public void setMainField(KKSField mainField) {
		this.mainField = mainField;
	}

	public void clear() {

		if (this.fields != null)
			this.fields.clear();
	}

}
