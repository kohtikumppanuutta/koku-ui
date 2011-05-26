package com.ixonos.koku.kks.mock;

import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

public class Aktivointi {

  private KehitystietoTyyppi aktivoitavaKentta;

  private String alkaa;

  private String loppuu;

  public KehitystietoTyyppi getAktivoitavaKentta() {
    return aktivoitavaKentta;
  }

  public void setAktivoitavaKentta(KehitystietoTyyppi aktivoitavaKentta) {
    this.aktivoitavaKentta = aktivoitavaKentta;
  }

  public String getAlkaa() {
    return alkaa;
  }

  public void setAlkaa(String alkaa) {
    this.alkaa = alkaa;
  }

  public String getLoppuu() {
    return loppuu;
  }

  public void setLoppuu(String loppuu) {
    this.loppuu = loppuu;
  }

}
