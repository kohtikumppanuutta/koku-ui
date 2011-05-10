package com.ixonos.eservices.koku.kks.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ixonos.eservices.koku.kks.utils.enums.AdvancementField;
import com.ixonos.eservices.koku.kks.utils.enums.AdvancementType;
import com.ixonos.eservices.koku.kks.utils.enums.ChildInfo;
import com.ixonos.eservices.koku.kks.utils.enums.HealthCondition;
import com.ixonos.eservices.koku.kks.utils.enums.KKSField;
import com.ixonos.eservices.koku.kks.utils.enums.SupportActivity;
import com.ixonos.eservices.koku.kks.utils.enums.UIField;

@Service(value = "myKKSService")
public class KKSServiceImpl implements KKSService {

	private User user;
	private KKSModel model;
	private Map<String, KKSField> fields;

	public User getUser() {
		return user;
	}

	public void create(String userRole) {
		user = new User();
		user.setRole(userRole);
		model = MockFactory.createModel();
		fields = createFieldsMap();
	}

	public List<Child> getChilds(User user) {
		return model.getChilds();
	}

	public List<KKSEntry> getEntries(Child child) {
		List<KKSEntry> tmp = new ArrayList<KKSEntry>();

		for (KKSEntry e : model.getEntries()) {
			if (e.getChild().equals(child)) {
				tmp.add(e);
			}
		}
		return tmp;
	}

	public void addEntry(Child child, String description, KKSField... fields) {
		KKSEntry tmp = new KKSEntry(new Date(System.currentTimeMillis()),
				description);
		tmp.setChild(child);

		if (fields != null) {
			tmp.setFields(Arrays.asList(fields));
		}
		model.addEntry(tmp);
	}

	public List<Child> searchChilds(Child target) {
		List<Child> list = new ArrayList<Child>();
		Child tmp = getChild(target.getSocialSecurityNumber());

		if (tmp != null && tmp.getFirstName().equals(target.getFirstName())
				&& tmp.getLastName().equals(target.getLastName())) {
			list.add(tmp);
		}

		return list;
	}

	public List<KKSEntry> searchEntries(Child child, List<KKSField> fields) {

		if (fields.contains(UIField.ALL)) {
			return getEntries(child);
		}

		List<KKSEntry> tmp = new ArrayList<KKSEntry>();
		for (KKSEntry e : model.getEntries()) {
			if (e.getChild().equals(child) && e.hasAtLeastOne(fields)) {
				tmp.add(e);
			}
		}
		return tmp;
	}

	public Child getChild(String socialSecurityNumber) {
		for (Child tmp : model.getChilds()) {
			if (tmp.getSocialSecurityNumber().equals(socialSecurityNumber)) {
				return tmp;
			}
		}
		return null;
	}

	public void addEntry(KKSEntry entry) {
		model.addEntry(entry);
	}

	public KKSField getField(String fieldId) {
		return fields.get(fieldId);
	}

	private Map<String, KKSField> createFieldsMap() {
		Map<String, KKSField> tmp = new HashMap<String, KKSField>();

		for (AdvancementField field : AdvancementField.values()) {
			tmp.put(field.getId(), field);
		}

		for (AdvancementType field : AdvancementType.values()) {
			tmp.put(field.getId(), field);
		}

		for (ChildInfo field : ChildInfo.values()) {
			tmp.put(field.getId(), field);
		}

		for (HealthCondition field : HealthCondition.values()) {
			tmp.put(field.getId(), field);
		}

		for (SupportActivity field : SupportActivity.values()) {
			tmp.put(field.getId(), field);
		}

		for (UIField field : UIField.values()) {
			tmp.put(field.getId(), field);
		}

		return tmp;

	}

}
