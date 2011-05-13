package com.ixonos.koku.kks.mock;

import java.util.ArrayList;
import java.util.List;

import com.ixonos.koku.kks.utils.enums.KKSKentta;
import com.ixonos.koku.kks.utils.enums.UIField;

/**
 * Model for entry search
 * 
 * @author tuomape
 * 
 */
public class KenttaHaku {

  public KKSKentta paaKentta;

  public List<KKSKentta> kentat;

  public KenttaHaku() {
    paaKentta = UIField.ALL;
    kentat = new ArrayList<KKSKentta>();
  }

  public List<KKSKentta> getKentat() {
    return kentat;
  }

  public List<KKSKentta> getAllKentat() {
    return kentat;
  }

  public void setKentat(List<KKSKentta> kentat) {
    this.kentat = kentat;
  }

  public void lisaaKentta(KKSKentta kentta) {
    this.kentat.add(kentta);
  }

  public KKSKentta getPaaKentta() {
    return this.paaKentta;
  }

  public void setPaaKentta(KKSKentta paakentta) {
    this.paaKentta = paakentta;
  }

  public void clear() {

    if (this.kentat != null)
      this.kentat.clear();
  }

}
