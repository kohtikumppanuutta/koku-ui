package com.ixonos.koku.kks.mock;

public class Henkilo {

  private String etunimi;
  private String toinenNimi;
  private String sukunimi;
  private String hetu;
  private KKS kks;

  public Henkilo() {
    this.etunimi = "";
    this.toinenNimi = "";
    this.sukunimi = "";
  }

  public Henkilo(String etuNimi, String toinenNimi, String sukuNimi, String hetu) {
    this.etunimi = etuNimi;
    this.toinenNimi = toinenNimi;
    this.sukunimi = sukuNimi;
    this.hetu = hetu;
  }

  public String getEtunimi() {
    return etunimi;
  }

  public void setEtunimi(String etuNimi) {
    this.etunimi = etuNimi;
  }

  public String getToinenNimi() {
    return toinenNimi;
  }

  public void setToinenNimi(String toinenNimi) {
    this.toinenNimi = toinenNimi;
  }

  public String getSukunimi() {
    return sukunimi;
  }

  public void setSukunimi(String sukuNimi) {
    this.sukunimi = sukuNimi;
  }

  public String getHetu() {
    return hetu;
  }

  public void setHetu(String hetu) {
    this.hetu = hetu;
  }

  public String getId() {
    return getHetu();
  }

  public KKS getKks() {
    return kks;
  }

  public void setKks(KKS kks) {
    this.kks = kks;
  }

  public String getNimi() {
    return etunimi + " " + sukunimi;
  }

}
