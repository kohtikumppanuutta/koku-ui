package com.ixonos.koku.kks.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ixonos.koku.kks.utils.enums.KKSKentta;
import com.ixonos.koku.kks.utils.enums.KehitysAsiaTyyppi;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

@Service(value = "myKKSService")
public class KKSServiceImpl implements KKSService {

  private User user;
  private KKSMockModel model;
  private Map<String, KehitystietoTyyppi> fields;

  private Map<String, KehitysAsiaTyyppi> asiaTyypit;

  public User getUser() {
    return user;
  }

  public void create(String userRole) {
    user = new User();
    user.setRole(userRole);
    model = MockFactory.createModel();
    fields = createFieldsMap();
    asiaTyypit = createAsiaTyypitMap();
  }

  public List<Henkilo> getChilds(User user) {
    return model.getChilds();
  }

  public List<Kehitystieto> getEntries(Henkilo child) {

    return new ArrayList<Kehitystieto>(child.getKks().getKehitystiedot());
  }

  public void addEntry(Henkilo child, String description, KKSKentta... fields) {

  }

  public List<Henkilo> searchChilds(Henkilo target) {
    List<Henkilo> list = new ArrayList<Henkilo>();
    Henkilo tmp = getChild(target.getHetu());

    if (tmp != null && tmp.getEtunimi().equals(target.getEtunimi())
        && tmp.getSukunimi().equals(target.getSukunimi())) {
      list.add(tmp);
    }

    return list;
  }

  public List<Kehitystieto> searchEntries(Henkilo child, List<KKSKentta> fields) {

    return getEntries(child);
  }

  public Henkilo getChild(String socialSecurityNumber) {
    for (Henkilo tmp : model.getChilds()) {
      if (tmp.getHetu().equals(socialSecurityNumber)) {
        return tmp;
      }
    }
    return null;
  }

  public void addEntry(Kehitystieto entry) {
    model.addEntry(entry);
  }

  public KehitystietoTyyppi getField(String fieldId) {
    return fields.get(fieldId);
  }

  public KehitysAsiaTyyppi getAsiatyyppi(String fieldId) {
    return asiaTyypit.get(fieldId);
  }

  private Map<String, KehitystietoTyyppi> createFieldsMap() {
    Map<String, KehitystietoTyyppi> tmp = new HashMap<String, KehitystietoTyyppi>();

    for (KehitystietoTyyppi kt : KehitystietoTyyppi.values()) {
      tmp.put(kt.toString(), kt);
    }

    return tmp;

  }

  private Map<String, KehitysAsiaTyyppi> createAsiaTyypitMap() {
    Map<String, KehitysAsiaTyyppi> tmp = new HashMap<String, KehitysAsiaTyyppi>();

    for (KehitysAsiaTyyppi kt : KehitysAsiaTyyppi.values()) {
      tmp.put(kt.toString(), kt);
    }
    return tmp;
  }

}
