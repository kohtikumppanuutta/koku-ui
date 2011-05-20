package com.ixonos.koku.kks.mock;

import java.util.Date;

import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

public class Aktivointi {

  private KehitystietoTyyppi aktivoitavaKentta;

  private Date alkaa;

  private Date loppuu;

  public KehitystietoTyyppi getAktivoitavaKentta() {
    return aktivoitavaKentta;
  }

  public void setAktivoitavaKentta(KehitystietoTyyppi aktivoitavaKentta) {
    this.aktivoitavaKentta = aktivoitavaKentta;
  }

  public Date getAlkaa() {
    return alkaa;
  }

  public void setAlkaa(Date alkaa) {
    this.alkaa = alkaa;
  }

  public Date getLoppuu() {
    return loppuu;
  }

  public void setLoppuu(Date loppuu) {
    this.loppuu = loppuu;
  }

}
