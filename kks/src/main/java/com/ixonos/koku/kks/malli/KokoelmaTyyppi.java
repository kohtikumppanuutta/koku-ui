package com.ixonos.koku.kks.malli;

import java.util.ArrayList;
import java.util.List;

import com.ixonos.koku.kks.utils.LinkedMapWrapper;

/**
 * Kuvaa kokoelman sisällön, edellytykset ja mahdollisuudet
 * 
 * @author tuomape
 */
public class KokoelmaTyyppi {

  private int koodi;
  private String nimi;
  private String kuvaus;
  private List<KirjausTyyppi> kirjausTyypit;
  private LinkedMapWrapper<String, Ryhma> kirjausRyhmat;

  public KokoelmaTyyppi(int koodi, String nimi, String kuvaus) {
    super();
    this.koodi = koodi;
    this.nimi = nimi;
    this.kuvaus = kuvaus;
    this.kirjausTyypit = new ArrayList<KirjausTyyppi>();
    this.kirjausRyhmat = new LinkedMapWrapper<String, Ryhma>();
  }

  public int getKoodi() {
    return koodi;
  }

  public void setKoodi(int koodi) {
    this.koodi = koodi;
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

  public List<KirjausTyyppi> getKirjausTyypit() {
    return kirjausTyypit;
  }

  public void setKirjausTyypit(List<KirjausTyyppi> kirjausTyypit) {
    assert kirjausTyypit != null : "Kirjaustyypit ei voi olla NULL ";
    this.kirjausTyypit = kirjausTyypit;

    for (KirjausTyyppi tmp : kirjausTyypit) {
      lisaaKirjausRyhma(tmp);
    }
  }

  public void lisaaKirjausTyyppi(KirjausTyyppi tyyppi) {
    assert tyyppi != null : "Kirjaustyyppi ei voi olla NULL ";
    this.kirjausTyypit.add(tyyppi);
    lisaaKirjausRyhma(tyyppi);
  }

  private void lisaaKirjausRyhma(KirjausTyyppi tyyppi) {
    if (kirjausRyhmat.get().containsKey(tyyppi.getTayttaja())) {
      Ryhma tmp = kirjausRyhmat.get().get(tyyppi.getTayttaja());
      tmp.lisaa(tyyppi);
    } else {
      Ryhma tmp = new Ryhma(tyyppi.getTayttaja());
      tmp.lisaa(tyyppi);
      kirjausRyhmat.get().put(tyyppi.getTayttaja(), tmp);
    }
  }

  public LinkedMapWrapper<String, Ryhma> getKirjausRyhmat() {
    return kirjausRyhmat;
  }

}
