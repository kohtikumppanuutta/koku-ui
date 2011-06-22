package com.ixonos.koku.kks.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ixonos.koku.kks.malli.Kirjaus;

/**
 * Sisältää kirjaushaun tulokset
 * 
 * @author tuomape
 * 
 */
public class HakuTulokset {

  private LinkedMapWrapper<String, Tulos> tulokset;

  public HakuTulokset() {
    tulokset = new LinkedMapWrapper<String, Tulos>();
  }

  public void lisaaTulos(String kokoelma, Kirjaus kirjaus) {
    if (tulokset.get().containsKey(kokoelma)) {
      Tulos tmp = tulokset.get().get(kokoelma);
      tmp.lisaa(kirjaus);
    } else {
      Tulos tmp = new Tulos(kokoelma);
      tmp.lisaa(kirjaus);
      tulokset.get().put(kokoelma, tmp);
    }
  }

  public Collection<Tulos> getTulokset() {
    return tulokset.getValues();
  }

  public void setTulokset(Map<String, List<Kirjaus>> tulokset) {
    for (Entry<String, List<Kirjaus>> tmp : tulokset.entrySet())
      for (Kirjaus k : tmp.getValue()) {
        lisaaTulos(tmp.getKey(), k);
      }
  }

}
