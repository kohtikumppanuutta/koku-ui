package com.ixonos.eservices.koku.kks.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ixonos.eservices.koku.kks.utils.enums.KKSField;

/**
 * Class mapping a single KKS entry
 * 
 * @author tuomape
 */
public class KKSEntry {

	private String id;
	private Date date;
	private String description;
	private Child child;
	private List<KKSField> fields;

	public KKSEntry() {
		this.id = "" + System.currentTimeMillis();
		this.date = null;
		this.description = "";
		this.fields = new ArrayList<KKSField>();
	}

	public KKSEntry(Date date, String description) {
		this.date = date;
		this.description = description;
		this.fields = new ArrayList<KKSField>();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<KKSField> getFields() {
		return fields;
	}

	public void setFields(List<KKSField> fields) {
		this.fields = fields;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void addField(KKSField field) {
		this.fields.add(field);
	}

	public String getFieldsAsText() {
		StringBuffer b = new StringBuffer();

		for (int i = 0; i < fields.size(); i++) {

			b.append(fields.get(i).getBundleId().toUpperCase());

			if ((i + 1) < fields.size()) {
				b.append(", ");
			}
		}
		return b.toString();
	}

	public Child getChild() {
		return child;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public boolean hasField(KKSField f) {
		for (KKSField tmp : getFields()) {
			if (tmp.getBundleId().equals(f.getBundleId())) {
				return true;
			}
		}
		return false;
	}

	public boolean hasAtLeastOne(List<KKSField> fields) {

		if (fields == null) {
			return false;
		}

		for (KKSField f : fields) {
			if (hasField(f)) {
				return true;
			}
		}

		return false;
	}

}
