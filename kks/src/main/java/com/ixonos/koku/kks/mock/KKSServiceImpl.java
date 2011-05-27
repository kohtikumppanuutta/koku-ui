package com.ixonos.koku.kks.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ixonos.koku.kks.utils.enums.KehitysAsiaTyyppi;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

@Service(value = "myKKSService")
public class KKSServiceImpl implements KKSService {

  private Kayttaja user;
  private KKSMockModel model;
  private Map<String, KehitystietoTyyppi> tyypit;
  private Map<String, KehitysAsiaTyyppi> asiaTyypit;

  public Kayttaja getUser() {
    return user;
  }

  public void create(String userRole) {
    user = new Kayttaja();
    user.setRole(userRole);
    model = MockFactory.createModel();
    tyypit = createTyyppiFieldsMap();
    asiaTyypit = createAsiaTyypitMap();
  }

  public List<Henkilo> haeLapset(Kayttaja user) {
    return model.getChilds();
  }

  public List<Henkilo> haeHenkilo(Henkilo target) {
    List<Henkilo> list = new ArrayList<Henkilo>();
    Henkilo tmp = haeLapsi(target.getHetu());

    if (tmp != null && tmp.getEtunimi().equals(target.getEtunimi())
        && tmp.getSukunimi().equals(target.getSukunimi())) {
      list.add(tmp);
    }

    return list;
  }

  public Henkilo haeLapsi(String socialSecurityNumber) {
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

  public KehitystietoTyyppi getTyyppi(String fieldId) {
    return tyypit.get(fieldId);
  }

  public KehitysAsiaTyyppi getAsiatyyppi(String fieldId) {
    return asiaTyypit.get(fieldId);
  }

  private Map<String, KehitystietoTyyppi> createTyyppiFieldsMap() {
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
