package com.ixonos.koku.kks.mock;

import java.util.Collection;
import java.util.HashMap;
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
}
