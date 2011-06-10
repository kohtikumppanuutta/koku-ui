package com.ixonos.koku.kks.malli;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sisältää yhden kasvatuksen ja kehityksen kokoelman tiedot
 * 
 * @author tuomape
 */
public class Kokoelma {

  private String nimi;
  private String kuvaus;
  private String muokkaaja;
  private KokoelmaTila tila;
  private Date luontiAika;
  private int versio;
  private KokoelmaTyyppi tyyppi;
  private List<Luokitus> luokitukset;
  public Map<String, Kirjaus> kirjaukset;

  public Kokoelma(String nimi, String kuvaus, KokoelmaTila tila,
      Date luontiAika, int versio, KokoelmaTyyppi tyyppi) {
    super();
    this.nimi = nimi;
    this.kuvaus = kuvaus;
    this.tila = tila;
    this.luontiAika = luontiAika;
    this.versio = versio;
    this.tyyppi = tyyppi;
    kirjaukset = new HashMap<String, Kirjaus>();
  }

  public String getNimi() {
    return nimi;
  }

  public void setNimi(String nimi) {
    this.nimi = nimi;
  }

  public String getKuvaus() {
    return kuvaus;
  }

  public void setKuvaus(String kuvaus) {
    this.kuvaus = kuvaus;
  }

  public KokoelmaTila getTila() {
    return tila;
  }

  public void setTila(KokoelmaTila tila) {
    this.tila = tila;
  }

  public Date getLuontiAika() {
    return luontiAika;
  }

  public void setLuontiAika(Date luontiAika) {
    this.luontiAika = luontiAika;
  }

  public int getVersio() {
    return versio;
  }

  public void setVersio(int versio) {
    this.versio = versio;
  }

  public List<Luokitus> getLuokitukset() {
    return luokitukset;
  }

  public void setLuokitukset(List<Luokitus> luokitukset) {
    this.luokitukset = luokitukset;
  }

  public KokoelmaTyyppi getTyyppi() {
    return tyyppi;
  }

  public void setTyyppi(KokoelmaTyyppi tyyppi) {
    this.tyyppi = tyyppi;
  }

  public String getMuokkaaja() {
    return muokkaaja;
  }

  public void setMuokkaaja(String muokkaaja) {
    this.muokkaaja = muokkaaja;
  }

  public void lisaaKirjaus(Kirjaus kirjaus) {
    kirjaukset.put("" + kirjaus.getTyyppi().getKoodi(), kirjaus);
  }

  public Map<String, Kirjaus> getKirjaukset() {
    return kirjaukset;
  }

  public void setKirjaukset(Map<String, Kirjaus> kirjaukset) {
    this.kirjaukset = kirjaukset;
  }

}
