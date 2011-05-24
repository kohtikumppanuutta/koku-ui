package com.ixonos.koku.kks.utils;

import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

public class ViewHelper {

  public static String toiminto(KehitystietoTyyppi tyyppi) {

    switch (tyyppi) {
    case TUKITARVE:
      return "naytaTukitoimet";
    case KASVATUSTA_OHJAAVAT_TIEDOT:
      return "naytaKasvatusTiedot";
    case NELJA_VUOTISTARKASTUS:
      return "naytaNeljavuotistarkastus";
    case LAPSEN_KEHITYS:
      return "naytaKehitys";
    }
    return "";
  }
}
