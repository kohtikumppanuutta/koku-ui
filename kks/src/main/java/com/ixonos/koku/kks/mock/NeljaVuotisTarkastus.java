package com.ixonos.koku.kks.mock;

import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

public class NeljaVuotisTarkastus extends Kehitystieto {

  @Override
  public String getId() {
    return this.getClass().getName();
  }

  @Override
  public KehitystietoTyyppi getTyyppi() {
    return KehitystietoTyyppi.NELJA_VUOTISTARKASTUS;
  }

  @Override
  public String getNimi() {
    return "4-vuotistarkastus";
  }

}
