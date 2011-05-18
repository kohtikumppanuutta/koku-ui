package com.ixonos.koku.kks.mock;

import java.util.Date;

public class MockFactory {

  public static KKSMockModel createModel() {

    Henkilo c1 = new Henkilo("Etu1", "Toka1", "Sukunimi1", "270608-223Z");
    Henkilo c2 = new Henkilo("Etu2", "Toka2", "Sukunimi2", "200107-211Y");

    KKSMockModel tmp = new KKSMockModel();
    tmp.addChild(c1);
    tmp.addChild(c2);

    tmp.addEntry(createEntry1(c1));
    tmp.addEntry(createEntry2(c1));
    tmp.addEntry(createEntry3(c2));

    return tmp;
  }

  public static KehitystietoOLD createEntry1(Henkilo c) {
    KehitystietoOLD entry1 = new KehitystietoOLD(new Date(
        System.currentTimeMillis()), "Pituus 123cm, paino 22kg. Ryhti hyvä.");
    entry1.setLapsi(c);
    return entry1;
  }

  public static KehitystietoOLD createEntry2(Henkilo c) {
    KehitystietoOLD entry1 = new KehitystietoOLD(
        new Date(System.currentTimeMillis()),
        "Oppi ajamaan pyörällä ilman apupyöriä. Ajaa jo pitkiä matkoja. Voi alkaa kulkea koulumatkoja pyörällä");
    entry1.setLapsi(c);
    return entry1;
  }

  public static KehitystietoOLD createEntry3(Henkilo c) {
    KehitystietoOLD entry1 = new KehitystietoOLD(new Date(
        System.currentTimeMillis()), "Lähineuvola lakkautusuhan alla");
    entry1.setLapsi(c);
    return entry1;
  }
}
