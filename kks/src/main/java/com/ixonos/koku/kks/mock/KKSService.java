package com.ixonos.koku.kks.mock;

import java.util.List;

import com.ixonos.koku.kks.utils.enums.KKSKentta;

public interface KKSService {

  public User getUser();

  public void create(String userRole);

  public List<Lapsi> getChilds(User user);

  public List<Kehitystieto> getEntries(Lapsi child);

  public void addEntry(Lapsi child, String description, KKSKentta... fields);

  public void addEntry(Kehitystieto entry);

  public List<Lapsi> searchChilds(Lapsi target);

  public Lapsi getChild(String socialSecurityNumber);

  public List<Kehitystieto> searchEntries(Lapsi child, List<KKSKentta> fields);

  public KKSKentta getField(String fieldId);

}
