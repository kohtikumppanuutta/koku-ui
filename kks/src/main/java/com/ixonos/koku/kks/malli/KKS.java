package com.ixonos.koku.kks.malli;

import java.util.List;

/**
 * Sisältää henkilön kasvatuksen ja kehityksen suunnitelmat
 * 
 * @author tuomape
 */
public class KKS {

  private List<Kokoelma> kokoelmat;

  public void lisaaKokoelma(Kokoelma k) {
    kokoelmat.add(k);
  }

}
