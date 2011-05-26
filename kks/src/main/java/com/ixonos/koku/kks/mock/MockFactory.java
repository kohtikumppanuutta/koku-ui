package com.ixonos.koku.kks.mock;

import java.util.Date;

import com.ixonos.koku.kks.utils.enums.ErikoisruokaPeruste;
import com.ixonos.koku.kks.utils.enums.KaynninTyyppi;
import com.ixonos.koku.kks.utils.enums.KehitysAsiaTyyppi;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;
import com.ixonos.koku.kks.utils.enums.Tila;

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

    Date now = new Date();
    Date valid = new Date();
    valid.setYear(valid.getYear() + 1);

    Kehitystieto entry1 = createLapsenKehitys();
    KehitysTietoTila tila1 = entry1.getTila();
    tila1.setTila(Tila.AKTIIVINEN);
    tila1.setAlkuPvm(now);
    tila1.setLoppuPvm(valid);

    Kehitystieto entry2 = new DefaultKehitysTieto("2",
        KehitystietoTyyppi.TUKITARVE, "Lapsen tukitarpeet");

    entry2.addKehitysAsia(createTukiTarve("Kuljetustuki"));
    entry2.addKehitysAsia(createTukiTarve("Kävelytuki"));
    entry2.setMuokkausPvm(new Date());
    entry2.setMuokkaaja("Mikko Muokkaaja");

    KehitysTietoTila tila2 = entry2.getTila();
    tila2.setTila(Tila.AKTIIVINEN);
    tila2.setAlkuPvm(now);
    tila2.setLoppuPvm(valid);

    Kehitystieto entry3 = createKasvatutsTieto("3",
        "Kasvatusta ohjaavat tiedot");
    entry3.setMuokkausPvm(new Date());
    entry3.setMuokkaaja("Mikko Muokkaaja");

    KehitysTietoTila tila3 = entry3.getTila();
    tila3.setTila(Tila.AKTIIVINEN);
    tila3.setAlkuPvm(now);
    tila3.setLoppuPvm(valid);

    Kehitystieto entry4 = new DefaultKehitysTieto("7",
        KehitystietoTyyppi.NELJA_VUOTISTARKASTUS, "4-vuotistarkastus");
    entry4.setMuokkausPvm(new Date());
    entry4.setMuokkaaja("Mikko Muokkaaja");

    Kehitystieto entry5 = createTerveysTieto();

    KehitysTietoTila tila5 = entry5.getTila();
    tila5.setTila(Tila.AKTIIVINEN);
    tila5.setAlkuPvm(now);
    tila5.setLoppuPvm(valid);

    KKS tmp = new KKS();
    tmp.addKehitystieto(entry1);
    tmp.addKehitystieto(entry2);
    tmp.addKehitystieto(entry3);
    tmp.addKehitystieto(entry4);
    tmp.addKehitystieto(entry5);
    c.setKks(tmp);
    return entry1;
  }

  public static Kehitystieto createNewEntry1(Henkilo c) {
    Date now = new Date();
    Date valid = new Date();
    valid.setYear(valid.getYear() + 1);

    Kehitystieto entry1 = createLapsenKehitys();
    KehitysTietoTila tila1 = entry1.getTila();
    tila1.setTila(Tila.AKTIIVINEN);
    tila1.setAlkuPvm(now);
    tila1.setLoppuPvm(valid);

    Kehitystieto entry2 = new DefaultKehitysTieto("5",
        KehitystietoTyyppi.TUKITARVE, "Lapsen tukitarpeet");
    entry2.setMuokkausPvm(new Date());
    entry2.setMuokkaaja("Mikko Muokkaaja");
    Kehitystieto entry3 = createKasvatutsTieto("6",
        "Kasvatusta ohjaavat tiedot");
    entry3.setMuokkausPvm(new Date());
    entry3.setMuokkaaja("Mikko Muokkaaja");
    KKS tmp = new KKS();
    tmp.addKehitystieto(entry1);
    tmp.addKehitystieto(entry2);
    tmp.addKehitystieto(entry3);
    c.setKks(tmp);
    return entry1;
  }

  private static KehitysAsia createTukiTarve(String nimi) {
    KehitysAsia tmp = new KehitysAsia(nimi, KehitysAsiaTyyppi.TUKITARVE);
    tmp.setMuokkaaja("Mikko Muokkaaja");
    tmp.setMuokkausPvm(new Date());
    tmp.addProperty(new KKSProperty("kuvaus", nimi + " kuvaus"));
    tmp.addProperty(new KKSProperty("tehtavat", nimi + " tehtävät"));
    return tmp;
  }

  private static Kehitystieto createKasvatutsTieto(String id, String nimi) {
    Kehitystieto tmp = new DefaultKehitysTieto(id,
        KehitystietoTyyppi.KASVATUSTA_OHJAAVAT_TIEDOT, nimi);
    tmp.setMuokkaaja("Mikko Muokkaaja");
    tmp.setMuokkausPvm(new Date());
    tmp.addProperty(new KKSProperty("tarkeat_asiat",
        "päivärytmi, vuorovaikutus"));
    tmp.addProperty(new KKSProperty("tavoitteet", "sosiaalisuus"));
    return tmp;
  }

  private static KehitysAsia createMittaus(String nimi) {
    KehitysAsia tmp = new KehitysAsia(nimi, KehitysAsiaTyyppi.MITTAUS);
    tmp.setMuokkaaja("Mikko Muokkaaja");
    tmp.setMuokkausPvm(new Date());
    tmp.addProperty(new KKSProperty("kuvaus", nimi + " kuvaus"));
    return tmp;
  }

  private static KehitysAsia createHavainto(String nimi) {
    KehitysAsia tmp = new KehitysAsia(nimi, KehitysAsiaTyyppi.HAVAINTO);
    tmp.setMuokkaaja("Mikko Muokkaaja");
    tmp.setMuokkausPvm(new Date());
    tmp.addProperty(new KKSProperty("kuvaus", nimi + " kuvaus"));
    return tmp;
  }

  private static KehitysAsia createArvio(String nimi) {
    KehitysAsia tmp = new KehitysAsia(nimi, KehitysAsiaTyyppi.ARVIO);
    tmp.setMuokkaaja("Mikko Muokkaaja");
    tmp.setMuokkausPvm(new Date());
    tmp.addProperty(new KKSProperty("kuvaus", nimi + " kuvaus"));
    return tmp;
  }

  private static Kehitystieto createLapsenKehitys() {
    Date now = new Date();
    Date valid = new Date();
    valid.setYear(valid.getYear() + 1);

    Kehitystieto entry1 = new DefaultKehitysTieto("1",
        KehitystietoTyyppi.LAPSEN_KEHITYS, "Lapsen kehitys");
    KehitysTietoTila tila1 = entry1.getTila();
    tila1.setTila(Tila.AKTIIVINEN);
    tila1.setAlkuPvm(now);
    tila1.setLoppuPvm(valid);

    entry1.addKehitysAsia(createMittaus("3kk mittaus"));
    entry1.addKehitysAsia(createMittaus("6kk mittaus"));
    entry1.addKehitysAsia(createArvio("Kielellinen"));
    entry1.addKehitysAsia(createArvio("Sosiaalinen"));
    entry1.addKehitysAsia(createHavainto("Aktiivisuus"));
    entry1.setMuokkausPvm(new Date());
    entry1.setMuokkaaja("Mikko Muokkaaja");
    return entry1;
  }

  private static Kehitystieto createTerveysTieto() {
    Kehitystieto entry5 = new DefaultKehitysTieto("8",
        KehitystietoTyyppi.TERVEYDEN_TILA, "Lapsen terveydentila");
    entry5.setMuokkausPvm(new Date());
    entry5.setMuokkaaja("Mikko Muokkaaja");
    entry5.addKehitysAsia(createSairaus("Astma"));
    entry5.addKehitysAsia(createErikoisruoka("Gluteeniton",
        ErikoisruokaPeruste.ALLERGIA));
    entry5.addKehitysAsia(createErikoisruoka("Kosher",
        ErikoisruokaPeruste.VAKAUMUKSELLINEN));
    entry5.addKehitysAsia(createKaynti("1v-neuvola", KaynninTyyppi.NEUVOLA));
    entry5.addKehitysAsia(createKaynti("2v-neuvola", KaynninTyyppi.NEUVOLA));
    entry5.addKehitysAsia(createKaynti("Terveydenhoitajalla käynti",
        KaynninTyyppi.TERVEYDEN_HOITAJA));
    return entry5;
  }

  private static KehitysAsia createSairaus(String nimi) {
    KehitysAsia tmp = new KehitysAsia(nimi, KehitysAsiaTyyppi.SAIRAUS);
    tmp.setMuokkaaja("Mikko Muokkaaja");
    tmp.setMuokkausPvm(new Date());
    tmp.addProperty(new KKSProperty("oireet", nimi + " oireet"));
    tmp.addProperty(new KKSProperty("hoito", nimi + " hoito"));
    tmp.addProperty(new KKSProperty("laake", nimi + " lääkeohjeet"));
    tmp.addProperty(new KKSProperty("toimintasuunnitelma", nimi
        + " suunnitelma"));
    return tmp;
  }

  private static KehitysAsia createErikoisruoka(String nimi,
      ErikoisruokaPeruste peruste) {
    KehitysAsia tmp = new KehitysAsia(nimi, KehitysAsiaTyyppi.ERIKOISRUOKAVALIO);
    tmp.setMuokkaaja("Mikko Muokkaaja");
    tmp.setMuokkausPvm(new Date());
    tmp.addProperty(new KKSProperty("peruste", peruste.toString()));
    tmp.addProperty(new KKSProperty("kuvaus", nimi + " erikoisruuan kuvaus"));
    return tmp;
  }

  private static KehitysAsia createKaynti(String nimi, KaynninTyyppi peruste) {
    KehitysAsia tmp = new KehitysAsia(nimi, KehitysAsiaTyyppi.KAYNTI);
    tmp.setMuokkaaja("Mikko Muokkaaja");
    tmp.setMuokkausPvm(new Date());
    tmp.addProperty(new KKSProperty("kaynti", peruste.toString()));
    tmp.addProperty(new KKSProperty("kuvaus", nimi + " käynnin kuvaus"));
    return tmp;
  }
}
