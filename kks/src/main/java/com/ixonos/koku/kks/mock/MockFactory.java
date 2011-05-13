package com.ixonos.koku.kks.mock;

import java.util.Date;

import com.ixonos.koku.kks.utils.enums.AdvancementField;
import com.ixonos.koku.kks.utils.enums.AdvancementType;
import com.ixonos.koku.kks.utils.enums.ChildInfo;

public class MockFactory {

  public static KKSModel createModel() {

    Lapsi c1 = new Lapsi("Etu1", "Toka1", "Sukunimi1", "270608-223Z");
    Lapsi c2 = new Lapsi("Etu2", "Toka2", "Sukunimi2", "200107-211Y");

    KKSModel tmp = new KKSModel();
    tmp.addChild(c1);
    tmp.addChild(c2);

    tmp.addEntry(createEntry1(c1));
    tmp.addEntry(createEntry2(c1));
    tmp.addEntry(createEntry3(c2));

    return tmp;
  }

  public static Kehitystieto createEntry1(Lapsi c) {
    Kehitystieto entry1 = new Kehitystieto(
        new Date(System.currentTimeMillis()),
        "Pituus 123cm, paino 22kg. Ryhti hyvä.");
    entry1.lisaaKentta(AdvancementField.FOURTH_ANNUAL_CHECK);
    entry1.lisaaKentta(AdvancementType.MEASUREMENT);
    entry1.lisaaKentta(AdvancementType.PHYSICAL);
    entry1.lisaaKentta(ChildInfo.CHILD_HEALTH_CENTRE);
    entry1.setLapsi(c);
    return entry1;
  }

  public static Kehitystieto createEntry2(Lapsi c) {
    Kehitystieto entry1 = new Kehitystieto(
        new Date(System.currentTimeMillis()),
        "Oppi ajamaan pyörällä ilman apupyöriä. Ajaa jo pitkiä matkoja. Voi alkaa kulkea koulumatkoja pyörällä");
    entry1.lisaaKentta(AdvancementField.SCHOOL_TRANSFER);
    entry1.lisaaKentta(ChildInfo.PARENTING_INFO);
    entry1.lisaaKentta(AdvancementType.OBSERVATION);
    entry1.setLapsi(c);
    return entry1;
  }

  public static Kehitystieto createEntry3(Lapsi c) {
    Kehitystieto entry1 = new Kehitystieto(
        new Date(System.currentTimeMillis()), "Lähineuvola lakkautusuhan alla");
    entry1.lisaaKentta(AdvancementField.ASSISTANCE_CHANGE);
    entry1.lisaaKentta(ChildInfo.LIVING_CONDITIONS);
    entry1.lisaaKentta(AdvancementType.OBSERVATION);
    entry1.setLapsi(c);
    return entry1;
  }
}
