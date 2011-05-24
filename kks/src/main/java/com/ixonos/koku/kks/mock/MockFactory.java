package com.ixonos.koku.kks.mock;

import java.util.Date;

import com.ixonos.koku.kks.utils.enums.KehitysAsiaTyyppi;
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

    entry1.setMuokkausPvm(new Date());
    entry1.setMuokkaaja("Mikko Muokkaaja");

    Kehitystieto entry2 = new DefaultKehitysTieto("2",
        KehitystietoTyyppi.TUKITARVE, "Lapsen tukitarpeet");

    entry2.addKehitysAsia(createTukiTarve("Tukitarve 1"));
    entry2.addKehitysAsia(createTukiTarve("Tukitarve 2"));
    entry2.setMuokkausPvm(new Date());
    entry2.setMuokkaaja("Mikko Muokkaaja");

    Kehitystieto entry3 = new DefaultKehitysTieto("3",
        KehitystietoTyyppi.TERVEYDEN_TILA, "Lapsen terveydentila");
    entry3.setMuokkausPvm(new Date());
    entry3.setMuokkaaja("Mikko Muokkaaja");
        
        
    Kehitystieto entry4 = new DefaultKehitysTieto("4",
            KehitystietoTyyppi.NELJA_VUOTISTARKASTUS, "4-vuotistarkastus");
        
    
    KKS tmp = new KKS();
    tmp.addKehitystieto(entry1);
    tmp.addKehitystieto(entry2);
    tmp.addKehitystieto(entry3);
    tmp.addKehitystieto(entry4);
    c.setKks(tmp);
    return entry1;
  }

  public static Kehitystieto createNewEntry1(Henkilo c) {
    Kehitystieto entry1 = new DefaultKehitysTieto("4",
        KehitystietoTyyppi.LAPSEN_KEHITYS, "Lapsen kehitys");
    entry1.addKehitysAsia(createTukiTarve("Tukitarve 1"));
    entry1.setMuokkausPvm(new Date());
    entry1.setMuokkaaja("Mikko Muokkaaja");
    Kehitystieto entry2 = new DefaultKehitysTieto("5",
        KehitystietoTyyppi.TUKITARVE, "Lapsen tukitarpeet");
    entry2.setMuokkausPvm(new Date());
    entry2.setMuokkaaja("Mikko Muokkaaja");
    Kehitystieto entry3 = new DefaultKehitysTieto("6",
        KehitystietoTyyppi.TERVEYDEN_TILA, "Lapsen terveydentila");
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
    tmp.addProperty(new KKSProperty("tehtavat", nimi + " tehtavat"));
    return tmp;
  }
}
