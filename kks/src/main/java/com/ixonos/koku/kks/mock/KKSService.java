package com.ixonos.koku.kks.mock;

import java.util.List;

import com.ixonos.koku.kks.utils.enums.KKSKentta;

public interface KKSService {

  public User getUser();

  public void create(String userRole);

  public List<Henkilo> getChilds(User user);

  public List<KehitystietoOLD> getEntries(Henkilo child);

  public void addEntry(Henkilo child, String description, KKSKentta... fields);

  public void addEntry(KehitystietoOLD entry);

  public List<Henkilo> searchChilds(Henkilo target);

  public Henkilo getChild(String socialSecurityNumber);

  public List<KehitystietoOLD> searchEntries(Henkilo child, List<KKSKentta> fields);

  public KKSKentta getField(String fieldId);

}
