package com.ixonos.koku.kks.mock;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

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

  private KehitysTietoTila tila;

  private Map<String, KKSProperty> properties;

  private Map<String, KehitysAsia> kehitysAsiat;

  public abstract String getId();

  public abstract KehitystietoTyyppi getTyyppi();

  public abstract String getNimi();

  protected void init() {
    properties = new LinkedHashMap<String, KKSProperty>();
    kehitysAsiat = new LinkedHashMap<String, KehitysAsia>();
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

  public KehitysTietoTila getTila() {
    return tila;
  }

  public void setTila(KehitysTietoTila tila) {
    this.tila = tila;
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

  public KKSProperty getProperty(String name) {
    return properties.get(name);
  }

  public Map<String, KehitysAsia> getKehitysAsiat() {
    return kehitysAsiat;
  }

  public void setKehitysAsiat(Map<String, KehitysAsia> kehitysAsiat) {
    this.kehitysAsiat = kehitysAsiat;
  }

  public void addKehitysAsia(KehitysAsia asia) {
    kehitysAsiat.put(asia.getId(), asia);
  }

  public void removeKehitysAsia(String id) {
    kehitysAsiat.remove(id);
  }

  public KehitysAsia getKehitysAsia(String id) {
    return kehitysAsiat.get(id);
  }

}