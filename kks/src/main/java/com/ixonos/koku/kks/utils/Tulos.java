package com.ixonos.koku.kks.utils;

import java.util.ArrayList;
import java.util.List;

import com.ixonos.koku.kks.malli.Kirjaus;

/**
 * Kuvaa yhtä kirjaushaun tulosta
 * 
 * @author tuomape
 * 
 */
public class Tulos {

  private String kokoelmaId;
  private boolean kokoelmaAktiivinen;

  private String nimi;
  private List<Kirjaus> kirjaukset;

  public Tulos(String id) {
    this.kokoelmaId = id;
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

  public String getKokoelmaId() {
    return kokoelmaId;
  }

  public void setNimi(String nimi) {
    this.nimi = nimi;
  }

  public boolean isKokoelmaAktiivinen() {
    return kokoelmaAktiivinen;
  }

  public void setKokoelmaAktiivinen(boolean kokoelmaAktiivinen) {
    this.kokoelmaAktiivinen = kokoelmaAktiivinen;
  }

}
