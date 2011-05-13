package com.ixonos.koku.kks.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ixonos.koku.kks.utils.enums.AdvancementField;
import com.ixonos.koku.kks.utils.enums.AdvancementType;
import com.ixonos.koku.kks.utils.enums.ChildInfo;
import com.ixonos.koku.kks.utils.enums.HealthCondition;
import com.ixonos.koku.kks.utils.enums.KKSKentta;
import com.ixonos.koku.kks.utils.enums.SupportActivity;
import com.ixonos.koku.kks.utils.enums.UIField;

@Service(value = "myKKSService")
public class KKSServiceImpl implements KKSService {

  private User user;
  private KKSModel model;
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

  public List<Lapsi> getChilds(User user) {
    return model.getChilds();
  }

  public List<Kehitystieto> getEntries(Lapsi child) {
    List<Kehitystieto> tmp = new ArrayList<Kehitystieto>();

    for (Kehitystieto e : model.getEntries()) {
      if (e.getLapsi().equals(child)) {
        tmp.add(e);
      }
    }
    return tmp;
  }

  public void addEntry(Lapsi child, String description, KKSKentta... fields) {
    Kehitystieto tmp = new Kehitystieto(new Date(System.currentTimeMillis()),
        description);
    tmp.setLapsi(child);

    if (fields != null) {
      tmp.setKentat(Arrays.asList(fields));
    }
    model.addEntry(tmp);
  }

  public List<Lapsi> searchChilds(Lapsi target) {
    List<Lapsi> list = new ArrayList<Lapsi>();
    Lapsi tmp = getChild(target.getHetu());

    if (tmp != null && tmp.getEtunimi().equals(target.getEtunimi())
        && tmp.getSukunimi().equals(target.getSukunimi())) {
      list.add(tmp);
    }

    return list;
  }

  public List<Kehitystieto> searchEntries(Lapsi child, List<KKSKentta> fields) {

    if (fields.contains(UIField.ALL)) {
      return getEntries(child);
    }

    List<Kehitystieto> tmp = new ArrayList<Kehitystieto>();
    for (Kehitystieto e : model.getEntries()) {
      if (e.getLapsi().equals(child) && e.hasAtLeastOne(fields)) {
        tmp.add(e);
      }
    }
    return tmp;
  }

  public Lapsi getChild(String socialSecurityNumber) {
    for (Lapsi tmp : model.getChilds()) {
      if (tmp.getHetu().equals(socialSecurityNumber)) {
        return tmp;
      }
    }
    return null;
  }

  public void addEntry(Kehitystieto entry) {
    model.addEntry(entry);
  }

  public KKSKentta getField(String fieldId) {
    return fields.get(fieldId);
  }

  private Map<String, KKSKentta> createFieldsMap() {
    Map<String, KKSKentta> tmp = new HashMap<String, KKSKentta>();

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
