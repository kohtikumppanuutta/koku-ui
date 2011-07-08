package com.ixonos.koku.kks.malli;

import java.util.Date;

import com.ixonos.koku.kks.utils.enums.Tila;

/**
 * Kuvaa kokoelman tilaa
 * 
 * @author tuomape
 * 
 */
public class KokoelmaTila {

  private Tila tila;
  private Date alkuPvm;
  private Date loppuPvm;

  public KokoelmaTila(Tila tila, Date alkuPvm, Date loppuPvm) {
    super();
    this.tila = tila;
    this.alkuPvm = alkuPvm;
    this.loppuPvm = loppuPvm;
  }

  public Tila getTila() {
    return tila;
  }

  public void setTila(Tila tila) {
    this.tila = tila;
  }

  public Date getAlkuPvm() {
    return alkuPvm;
  }

  public void setAlkuPvm(Date alkuPvm) {
    this.alkuPvm = alkuPvm;
  }

  public Date getLoppuPvm() {
    return loppuPvm;
  }

  public void setLoppuPvm(Date loppuPvm) {
    this.loppuPvm = loppuPvm;
  }

  public boolean isAktiivinen() {
    if (Tila.LUKITTU.equals(tila))
      return false;

    if (Tila.ESITIEDOT.equals(tila)) {
      return true;
    }

    if (alkuPvm == null || loppuPvm == null) {
      return true;
    }

    Date d = new Date();

    if (d.after(alkuPvm) && d.before(loppuPvm)) {
      return true;
    }

    if (d.after(alkuPvm))
      setTila(Tila.LUKITTU);

    return false;
  }
}