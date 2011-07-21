package com.ixonos.koku.kks.malli;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Sisältää henkilön kasvatuksen ja kehityksen suunnitelmat
 * 
 * @author tuomape
 */
public class KKS {

  private Map<String, Kokoelma> kokoelmat;

  public KKS() {
    kokoelmat = new LinkedHashMap<String, Kokoelma>();
  }

  public void lisaaKokoelma(Kokoelma k) {
    assert k != null : "Kokoelma on null";
    kokoelmat.put(k.getId(), k);
  }

  public void poistaKokoelma(Kokoelma k) {
    assert k != null : "Ei voi poistaa tyhjää kokoelmaa";
    kokoelmat.remove(k.getId());
  }

  public Kokoelma getKokoelma(String id) {
    assert id != null : "Kokoelmaa yritetään hakea null nimellä";
    return kokoelmat.get(id);
  }

  public List<Kokoelma> getKokoelmat() {
    return new ArrayList<Kokoelma>(kokoelmat.values());
  }

  public boolean hasKokoelma(String id) {
    return kokoelmat.containsKey(id);
  }
}
