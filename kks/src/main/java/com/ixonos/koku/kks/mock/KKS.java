package com.ixonos.koku.kks.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

public class KKS {

  private Map<KehitystietoTyyppi, Kehitystieto> kehitystiedot;

  public KKS() {
    kehitystiedot = new HashMap<KehitystietoTyyppi, Kehitystieto>();
  }

  public void addKehitystieto(Kehitystieto tieto) {
    kehitystiedot.put(tieto.getTyyppi(), tieto);
  }

  public Kehitystieto getKehitystieto(KehitystietoTyyppi tyyppi) {
    return kehitystiedot.get(tyyppi);
  }

  public Collection<Kehitystieto> getKehitystiedot() {
    return kehitystiedot.values();
  }

  public List<Kehitystieto> getAktiivisetKehitystiedot() {
    List<Kehitystieto> tmp = new ArrayList<Kehitystieto>();

    for (Kehitystieto k : kehitystiedot.values()) {
      if (k.getTila().isAktiivinen()) {
        tmp.add(k);
      }
    }
    return tmp;
  }
}
