package com.ixonos.koku.kks.mock;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ixonos.koku.kks.utils.enums.KehitysAsiaTyyppi;

public class KehitysAsia {

  private String id;
  private String nimi;
  private String muokkaaja;
  private Date muokkausPvm;
  private KehitysAsiaTyyppi tyyppi;
  private Map<String, KKSProperty> properties;

  public KehitysAsia(String nimi, KehitysAsiaTyyppi tyyppi) {
    super();
    this.nimi = nimi;
    this.tyyppi = tyyppi;
    properties = new HashMap<String, KKSProperty>();
  }

  public void setNimi(String nimi) {
    this.nimi = nimi;
  }

  public String getNimi() {
    return nimi;
  }

  public void setProperties(Map<String, KKSProperty> properties) {
    this.properties = properties;
  }

  public Map<String, KKSProperty> getProperties() {
    return properties;
  }

  public void addProperty(KKSProperty p) {
    properties.put(p.getName(), p);
  }

  public KehitysAsiaTyyppi getTyyppi() {
    return tyyppi;
  }

  public void setTyyppi(KehitysAsiaTyyppi tyyppi) {
    this.tyyppi = tyyppi;
  }

  public String getId() {
    // TODO: Oikea id hanskaus
    return getNimi();
  }

  public void setId(String id) {
    this.id = id;
  }

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

}
