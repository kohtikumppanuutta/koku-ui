package com.ixonos.koku.kks.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ixonos.koku.kks.utils.enums.KKSKentta;

/**
 * Class mapping a single KKS entry
 * 
 * @author tuomape
 */
public class Kehitystieto {

  private String id;
  private Date pvm;
  private String kuvaus;
  private Lapsi lapsi;
  private List<KKSKentta> kentat;

  public Kehitystieto() {
    this.id = "" + System.currentTimeMillis();
    this.pvm = null;
    this.kuvaus = "";
    this.kentat = new ArrayList<KKSKentta>();
  }

  public Kehitystieto(Date pvm, String kuvaus) {
    this.pvm = pvm;
    this.kuvaus = kuvaus;
    this.kentat = new ArrayList<KKSKentta>();
  }

  public Date getPvm() {
    return pvm;
  }

  public void setPvm(Date date) {
    this.pvm = date;
  }

  public String getKuvaus() {
    return kuvaus;
  }

  public void setKuvaus(String kuvaus) {
    this.kuvaus = kuvaus;
  }

  public List<KKSKentta> getKentat() {
    return kentat;
  }

  public void setKentat(List<KKSKentta> kentat) {
    this.kentat = kentat;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void lisaaKentta(KKSKentta kentta) {
    this.kentat.add(kentta);
  }

  public String getKenttaTekstina() {
    StringBuffer b = new StringBuffer();

    for (int i = 0; i < kentat.size(); i++) {

      b.append(kentat.get(i).getBundleId().toUpperCase());

      if ((i + 1) < kentat.size()) {
        b.append(", ");
      }
    }
    return b.toString();
  }

  public Lapsi getLapsi() {
    return lapsi;
  }

  public void setLapsi(Lapsi lapsi) {
    this.lapsi = lapsi;
  }

  public boolean hasKentta(KKSKentta f) {
    for (KKSKentta tmp : getKentat()) {
      if (tmp.getBundleId().equals(f.getBundleId())) {
        return true;
      }
    }
    return false;
  }

  public boolean hasAtLeastOne(List<KKSKentta> kentat) {

    if (kentat == null) {
      return false;
    }

    for (KKSKentta f : kentat) {
      if (hasKentta(f)) {
        return true;
      }
    }

    return false;
  }

}
