package com.ixonos.koku.kks.mock;

import java.util.Date;

import com.ixonos.koku.kks.utils.enums.KehitystietoTila;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

/**
 * Perusta kaikille kehitystiedoille
 * 
 * @author tuomape
 * 
 */
public abstract class Kehitystieto {

  private String muokkaaja;

  private Date muokkausPvm;

  private KehitystietoTila tila;

  public abstract String getId();

  public abstract KehitystietoTyyppi getTyyppi();

  public abstract String getNimi();

  public String getMuokkaaja() {
    return muokkaaja;
  }

  public void setMuokkaaja(String muokkaaja) {
    this.muokkaaja = muokkaaja;
  }

  public Date getMuokkausPvm() {
    return muokkausPvm;
  }

  public void setMuokkausPvm(Date muokkausPvm) {
    this.muokkausPvm = muokkausPvm;
  }

  public KehitystietoTila getTila() {
    return tila;
  }

  public void setTila(KehitystietoTila tila) {
    this.tila = tila;
  }

  public boolean isAktiivinen() {
    return KehitystietoTila.AKTIIVINEN.equals(tila);
  }
}
