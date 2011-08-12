package com.ixonos.koku.kks.utils;

import java.util.Comparator;

import com.ixonos.koku.kks.malli.Ryhma;

public class RyhmaComaparator implements Comparator<Ryhma> {

  @Override
  public int compare(Ryhma o1, Ryhma o2) {
    return o1.compareTo(o2);
  }

}
