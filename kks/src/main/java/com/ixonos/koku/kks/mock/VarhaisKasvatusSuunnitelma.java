package com.ixonos.koku.kks.mock;

import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

public class VarhaisKasvatusSuunnitelma extends Kehitystieto {

  @Override
  public String getId() {
    return getClass().getName();
  }

  @Override
  public KehitystietoTyyppi getTyyppi() {

    return KehitystietoTyyppi.VARHAISKASVATUSSUUNNITELMA;
  }

  @Override
  public String getNimi() {

    return "Varhaiskasvatussuunnitelma";
  }

}
