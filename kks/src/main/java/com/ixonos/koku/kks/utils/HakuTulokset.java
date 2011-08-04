package com.ixonos.koku.kks.utils;

import java.util.Collection;

import com.ixonos.koku.kks.malli.Kirjaus;
import com.ixonos.koku.kks.malli.Kokoelma;

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

  public void lisaaTulos(Kokoelma kokoelma, Kirjaus kirjaus) {
    if (tulokset.get().containsKey(kokoelma.getId())) {
      Tulos tmp = tulokset.get().get(kokoelma.getId());
      tmp.lisaa(kirjaus);
    } else {
      Tulos tmp = new Tulos(kokoelma.getId());
      tmp.setNimi(kokoelma.getNimi());
      tmp.setKokoelmaAktiivinen(kokoelma.getTila().isAktiivinen());
      tmp.lisaa(kirjaus);
      tulokset.get().put(kokoelma.getId(), tmp);
    }
  }

  public Collection<Tulos> getTulokset() {
    return tulokset.getValues();
  }

}
