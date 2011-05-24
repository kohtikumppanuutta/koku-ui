package com.ixonos.koku.kks.mock;

import java.util.List;

import com.ixonos.koku.kks.utils.enums.KKSKentta;
import com.ixonos.koku.kks.utils.enums.KehitysAsiaTyyppi;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

public interface KKSService {

  public User getUser();

  public void create(String userRole);

  public List<Henkilo> getChilds(User user);

  public List<Kehitystieto> getEntries(Henkilo child);

  public void addEntry(Henkilo child, String description, KKSKentta... fields);

  public void addEntry(Kehitystieto entry);

  public List<Henkilo> searchChilds(Henkilo target);

  public Henkilo getChild(String socialSecurityNumber);

  public List<Kehitystieto> searchEntries(Henkilo child, List<KKSKentta> fields);

  public KehitystietoTyyppi getField(String fieldId);

  public KehitysAsiaTyyppi getAsiatyyppi(String fieldId);

}
