package com.ixonos.koku.kks.mock;

import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;
import com.ixonos.koku.kks.utils.enums.Tila;

public class DefaultKehitysTieto extends Kehitystieto {

  private String id;

  private String nimi;

  private KehitystietoTyyppi tyyppi;

  public DefaultKehitysTieto(String id, KehitystietoTyyppi tyyppi, String nimi) {
    this.id = id;
    this.nimi = nimi;
    this.tyyppi = tyyppi;
    setTila(new KehitysTietoTila(Tila.LUKITTU, null, null));
  }

  @Override
  public String getId() {
    // TODO Auto-generated method stub
    return id;
  }

  @Override
  public KehitystietoTyyppi getTyyppi() {
    // TODO Auto-generated method stub
    return tyyppi;
  }

  @Override
  public String getNimi() {
    // TODO Auto-generated method stub
    return nimi;
  }

}
