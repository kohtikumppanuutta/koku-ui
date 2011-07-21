package com.ixonos.koku.kks.malli;

import java.util.ArrayList;
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

  private String edellinenVersio;
  private String id;

  private String nimi;
  private String kuvaus;
  private String muokkaaja;
  private KokoelmaTila tila;
  private Date luontiAika;
  private int versio;
  private KokoelmaTyyppi tyyppi;
  private List<Luokitus> luokitukset;
  public Map<String, Kirjaus> kirjaukset;
  public Map<String, List<Kirjaus>> moniArvoisetKirjaukset;

  public Kokoelma(String id, Kokoelma edellinen, Date luontiAika, KokoelmaTila tila, int versio) {
    this(id, edellinen.getNimi(), edellinen.getKuvaus(), tila, luontiAika, versio, edellinen.getTyyppi());
    for (Kirjaus k : edellinen.getKirjaukset().values()) {
      lisaaKirjaus(new Kirjaus(k.getArvo(), new Date(), k.getVersio(), k.getRekisteri(), k.getKirjaaja(), k.getTyyppi()));
    }
  }

  public Kokoelma(String id, String nimi, String kuvaus, KokoelmaTila tila, Date luontiAika, int versio,
      KokoelmaTyyppi tyyppi) {
    super();
    this.edellinenVersio = null;
    this.id = id;
    this.nimi = nimi;
    this.kuvaus = kuvaus;
    this.tila = tila;
    this.luontiAika = luontiAika;
    this.versio = versio;
    this.tyyppi = tyyppi;
    kirjaukset = new HashMap<String, Kirjaus>();
    moniArvoisetKirjaukset = new HashMap<String, List<Kirjaus>>();
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

  public String getEdellinenVersio() {
    return edellinenVersio;
  }

  public void setEdellinenVersio(String edellinenVersio) {
    this.edellinenVersio = edellinenVersio;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Map<String, List<Kirjaus>> getMoniArvoisetKirjaukset() {
    return moniArvoisetKirjaukset;
  }

  public void setMoniArvoisetKirjaukset(Map<String, List<Kirjaus>> moniArvoisetKirjaukset) {
    this.moniArvoisetKirjaukset = moniArvoisetKirjaukset;
  }

  public void lisaaMoniarvoinenKirjaus(Kirjaus kirjaus) {

    String key = "" + kirjaus.getTyyppi().getKoodi();
    if (moniArvoisetKirjaukset.containsKey(key)) {
      moniArvoisetKirjaukset.get(key).add(kirjaus);
    } else {
      List<Kirjaus> tmp = new ArrayList<Kirjaus>();
      tmp.add(kirjaus);
      moniArvoisetKirjaukset.put(key, tmp);
    }
  }

  public void poistaMoniarvoinenKirjaus(String id) {
    for (List<Kirjaus> kirjaukset : moniArvoisetKirjaukset.values()) {

      List<Kirjaus> tmp = new ArrayList<Kirjaus>(kirjaukset);
      for (Kirjaus k : tmp) {
        if (k.getId().equals(id)) {
          kirjaukset.remove(k);
        }
      }
    }
  }

  public Kirjaus getKirjaus(String id) {

    for (List<Kirjaus> tmp : moniArvoisetKirjaukset.values()) {
      for (Kirjaus k : tmp) {
        if (k.getId().equals(id)) {
          return k;
        }
      }
    }

    for (Kirjaus k : kirjaukset.values()) {
      if (k.getId().equals(id)) {
        return k;
      }
    }

    return null;
  }

}
