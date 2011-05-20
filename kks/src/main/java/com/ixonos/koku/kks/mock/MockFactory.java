package com.ixonos.koku.kks.mock;

import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

public class MockFactory {

  public static KKSMockModel createModel() {

    Henkilo c1 = new Henkilo("Etu1", "Toka1", "Sukunimi1", "270608-223Z");
    Henkilo c2 = new Henkilo("Etu2", "Toka2", "Sukunimi2", "200107-211Y");
    createNewEntry(c1);
    createNewEntry1(c2);
    KKSMockModel tmp = new KKSMockModel();
    tmp.addChild(c1);
    tmp.addChild(c2);

    return tmp;
  }

  public static Kehitystieto createNewEntry(Henkilo c) {
    Kehitystieto entry1 = new DefaultKehitysTieto("1",
        KehitystietoTyyppi.LAPSEN_KEHITYS, "Lapsen kehitys");
    Kehitystieto entry2 = new DefaultKehitysTieto("2",
        KehitystietoTyyppi.TUKITARVE, "Lapsen tukitarpeet");
    Kehitystieto entry3 = new DefaultKehitysTieto("3",
        KehitystietoTyyppi.TERVEYDEN_TILA, "Lapsen terveydentila");
    KKS tmp = new KKS();
    tmp.addKehitystieto(entry1);
    tmp.addKehitystieto(entry2);
    tmp.addKehitystieto(entry3);
    c.setKks(tmp);
    return entry1;
  }

  public static Kehitystieto createNewEntry1(Henkilo c) {
    Kehitystieto entry1 = new DefaultKehitysTieto("4",
        KehitystietoTyyppi.LAPSEN_KEHITYS, "Lapsen kehitys");
    Kehitystieto entry2 = new DefaultKehitysTieto("5",
        KehitystietoTyyppi.TUKITARVE, "Lapsen tukitarpeet");
    Kehitystieto entry3 = new DefaultKehitysTieto("6",
        KehitystietoTyyppi.TERVEYDEN_TILA, "Lapsen terveydentila");
    KKS tmp = new KKS();
    tmp.addKehitystieto(entry1);
    tmp.addKehitystieto(entry2);
    tmp.addKehitystieto(entry3);
    c.setKks(tmp);
    return entry1;
  }
}
