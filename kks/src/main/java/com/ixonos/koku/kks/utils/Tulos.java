package com.ixonos.koku.kks.utils;

import java.util.ArrayList;
import java.util.List;

import com.ixonos.koku.kks.malli.Kirjaus;

public class Tulos {

  private String nimi;
  private List<Kirjaus> kirjaukset;

  public Tulos(String nimi) {
    this.nimi = nimi;
    kirjaukset = new ArrayList<Kirjaus>();
  }

  public String getNimi() {
    return nimi;
  }

  public List<Kirjaus> getKirjaukset() {
    return kirjaukset;
  }

  public void lisaa(Kirjaus k) {
    kirjaukset.add(k);
  }
}
