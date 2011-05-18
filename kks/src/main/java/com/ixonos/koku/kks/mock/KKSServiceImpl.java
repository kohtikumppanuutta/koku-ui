package com.ixonos.koku.kks.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ixonos.koku.kks.utils.enums.KKSKentta;

@Service(value = "myKKSService")
public class KKSServiceImpl implements KKSService {

  private User user;
  private KKSMockModel model;
  private Map<String, KKSKentta> fields;

  public User getUser() {
    return user;
  }

  public void create(String userRole) {
    user = new User();
    user.setRole(userRole);
    model = MockFactory.createModel();
    fields = createFieldsMap();
  }

  public List<Henkilo> getChilds(User user) {
    return model.getChilds();
  }

  public List<KehitystietoOLD> getEntries(Henkilo child) {
    List<KehitystietoOLD> tmp = new ArrayList<KehitystietoOLD>();

    for (KehitystietoOLD e : model.getEntries()) {
      if (e.getLapsi().equals(child)) {
        tmp.add(e);
      }
    }
    return tmp;
  }

  public void addEntry(Henkilo child, String description, KKSKentta... fields) {
    KehitystietoOLD tmp = new KehitystietoOLD(new Date(
        System.currentTimeMillis()), description);
    tmp.setLapsi(child);

    if (fields != null) {
      tmp.setKentat(Arrays.asList(fields));
    }
    model.addEntry(tmp);
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

  public List<KehitystietoOLD> searchEntries(Henkilo child,
      List<KKSKentta> fields) {

    List<KehitystietoOLD> tmp = new ArrayList<KehitystietoOLD>();
    for (KehitystietoOLD e : model.getEntries()) {
      if (e.getLapsi().equals(child) && e.hasAtLeastOne(fields)) {
        tmp.add(e);
      }
    }
    return tmp;
  }

  public Henkilo getChild(String socialSecurityNumber) {
    for (Henkilo tmp : model.getChilds()) {
      if (tmp.getHetu().equals(socialSecurityNumber)) {
        return tmp;
      }
    }
    return null;
  }

  public void addEntry(KehitystietoOLD entry) {
    model.addEntry(entry);
  }

  public KKSKentta getField(String fieldId) {
    return fields.get(fieldId);
  }

  private Map<String, KKSKentta> createFieldsMap() {
    Map<String, KKSKentta> tmp = new HashMap<String, KKSKentta>();

    return tmp;

  }

}
