package com.ixonos.koku.kks.malli;

/**
 * Describes an element that can be activated (created). Element can be
 * collection type or existing collection
 * 
 * @author tuomape
 * 
 */
public class Aktivoitava {

  private String id;
  private boolean versioitava;
  private String nimi;

  public Aktivoitava(String id, boolean versioitava, String nimi) {
    super();
    this.id = id;
    this.versioitava = versioitava;
    this.nimi = nimi;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isVersioitava() {
    return versioitava;
  }

  public void setVersioitava(boolean versioitava) {
    this.versioitava = versioitava;
  }

  public String getNimi() {
    return nimi;
  }

  public void setNimi(String nimi) {
    this.nimi = nimi;
  }

  public String getTekstina() {
    return id + "#" + versioitava + "#" + nimi;
  }

  public static Aktivoitava luo(String text) {
    String tmp[] = text.split("#");

    return new Aktivoitava(tmp[0], Boolean.valueOf(tmp[1]).booleanValue(), tmp[2]);
  }

}
