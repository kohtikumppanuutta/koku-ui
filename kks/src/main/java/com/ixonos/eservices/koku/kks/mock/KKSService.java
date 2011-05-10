package com.ixonos.eservices.koku.kks.mock;

import java.util.List;

import com.ixonos.eservices.koku.kks.utils.enums.KKSField;

public interface KKSService {

	public User getUser();

	public void create(String userRole);

	public List<Child> getChilds(User user);

	public List<KKSEntry> getEntries(Child child);

	public void addEntry(Child child, String description, KKSField... fields);

	public void addEntry(KKSEntry entry);

	public List<Child> searchChilds(Child target);

	public Child getChild(String socialSecurityNumber);

	public List<KKSEntry> searchEntries(Child child, List<KKSField> fields);

	public KKSField getField(String fieldId);

}
