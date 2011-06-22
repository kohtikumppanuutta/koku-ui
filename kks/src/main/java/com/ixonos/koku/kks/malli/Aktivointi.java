package com.ixonos.koku.kks.malli;

/**
 * Luokka lomakkeen aktivointiin
 * 
 * @author tuomape
 * 
 */
public class Aktivointi {

  private String aktivoitavaKentta;

  private String alkaa;

  private String loppuu;

  public String getAktivoitavaKentta() {
    return aktivoitavaKentta;
  }

  public void setAktivoitavaKentta(String aktivoitavaKentta) {
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
