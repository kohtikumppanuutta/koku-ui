package com.ixonos.koku.kks.malli;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ixonos.koku.kks.utils.enums.Tietotyyppi;
import com.ixonos.koku.kks.utils.enums.Tila;

public class DemoFactory {

  public static final String NELI_VUOTIS_TARKASTUS = "4-vuotistarkastus";
  public static final String VASU = "Varhaiskasvatussuunnitelma";

  public static KKSDemoMalli luo() {
    Henkilo h1 = new Henkilo("Paavo", "Petteri", "Paavola", "270608-223Z");
    Henkilo h2 = new Henkilo("Sanna", "Maria", "Paavola", "200107-211Y");
    lisaaKokoelmat(h1);

    KKSDemoMalli malli = new KKSDemoMalli();
    malli.lisaaHenkilo(h1);
    malli.lisaaHenkilo(h2);
    return malli;
  }

  public static void lisaaKokoelmat(Henkilo h) {
    h.getKks().lisaaKokoelma(luo4VuotisTarkastus());

  }

  public static Kokoelma luoKokoelma(String nimi) {

    if (nimi.equalsIgnoreCase(VASU)) {
      return luoVarhaiskasvatusSuunnitelma();
    } else if (nimi.equalsIgnoreCase(NELI_VUOTIS_TARKASTUS)) {
      return luo4VuotisTarkastus();
    }
    return null;
  }

  public static Kokoelma luoVarhaiskasvatusSuunnitelma() {
    KokoelmaTyyppi tyyppi = new KokoelmaTyyppi(1, VASU,
        "Sisältää lapsen varhaiskasvatussuunnitelman");

    List<KirjausTyyppi> l = new ArrayList<KirjausTyyppi>();
    KirjausTyyppi t1 = new KirjausTyyppi(1, "Kenttä1", "Kenttä1 kuvaus", false,
        Tietotyyppi.VAPAA_TEKSTI, null, "Huoltaja", "Henkilörekisteri");

    Kirjaus k1 = new Kirjaus("kirjaus 1", new Date(), 1, "huuhaa", t1);

    KirjausTyyppi t2 = new KirjausTyyppi(2, "Kenttä2", "Kenttä2 kuvaus", false,
        Tietotyyppi.VAPAA_TEKSTI, null, "Huoltaja", "Henkilörekisteri");

    Kirjaus k2 = new Kirjaus("kirjaus 2", new Date(), 1, "huuhaa", t2);

    KirjausTyyppi t3 = new KirjausTyyppi(3, "Kenttä3", "Kenttä3 kuvaus", false,
        Tietotyyppi.VAPAA_TEKSTI, null, "Huoltaja", "Henkilörekisteri");

    Kirjaus k3 = new Kirjaus("kirjaus 3", new Date(), 1, "huuhaa", t3);

    KirjausTyyppi t4 = new KirjausTyyppi(4, "Kenttä4", "Kenttä4 kuvaus", false,
        Tietotyyppi.VAPAA_TEKSTI, null, "Huoltaja", "Henkilörekisteri");

    Kirjaus k4 = new Kirjaus("kirjaus 4", new Date(), 1, "huuhaa", t4);

    List<String> arvoJoukko5 = new ArrayList<String>();
    arvoJoukko5.add("eka");
    arvoJoukko5.add("toka");
    arvoJoukko5.add("kolmas");
    KirjausTyyppi t5 = new KirjausTyyppi(5, "Kenttä5", "Kenttä5 kuvaus", false,
        Tietotyyppi.MONIVALINTA, arvoJoukko5, "Huoltaja", "Henkilörekisteri");

    Kirjaus k5 = new Kirjaus("toka", new Date(), 1, "huuhaa", t5);

    l.add(t1);
    l.add(t2);
    l.add(t3);
    l.add(t4);
    l.add(t5);

    tyyppi.setKirjausTyypit(l);
    Date nyt = new Date();
    Date loppuu = new Date();
    loppuu.setYear(loppuu.getYear() + 1);

    KokoelmaTila tila = new KokoelmaTila(Tila.ESITIEDOT, nyt, loppuu);
    Kokoelma k = new Kokoelma(tyyppi.getNimi(), tyyppi.getKuvaus(), tila,
        new Date(), 1, tyyppi);

    k.lisaaKirjaus(k1);
    k.lisaaKirjaus(k2);
    k.lisaaKirjaus(k3);
    k.lisaaKirjaus(k4);
    k.lisaaKirjaus(k5);
    return k;
  }

  public static Kokoelma luo4VuotisTarkastus() {
    KokoelmaTyyppi tyyppi = new KokoelmaTyyppi(1, NELI_VUOTIS_TARKASTUS,
        "Sisältää tiedot lapsen 4-vuotistarkastuksesta");

    List<KirjausTyyppi> l = new ArrayList<KirjausTyyppi>();
    KirjausTyyppi t1 = new KirjausTyyppi(1, "Kenttä1", "Kenttä1 kuvaus", false,
        Tietotyyppi.VAPAA_TEKSTI, null, "Huoltaja", "Henkilörekisteri");

    Kirjaus k1 = new Kirjaus("kirjaus 1", new Date(), 1, "huuhaa", t1);

    KirjausTyyppi t2 = new KirjausTyyppi(2, "Kenttä2", "Kenttä2 kuvaus", false,
        Tietotyyppi.VAPAA_TEKSTI, null, "Huoltaja", "Henkilörekisteri");

    Kirjaus k2 = new Kirjaus("kirjaus 2", new Date(), 1, "huuhaa", t2);

    KirjausTyyppi t3 = new KirjausTyyppi(3, "Kenttä3", "Kenttä3 kuvaus", false,
        Tietotyyppi.VAPAA_TEKSTI, null, "Huoltaja", "Henkilörekisteri");

    Kirjaus k3 = new Kirjaus("kirjaus 3", new Date(), 1, "huuhaa", t3);

    KirjausTyyppi t4 = new KirjausTyyppi(4, "Kenttä4", "Kenttä4 kuvaus", false,
        Tietotyyppi.VAPAA_TEKSTI, null, "Huoltaja", "Henkilörekisteri");

    Kirjaus k4 = new Kirjaus("kirjaus 4", new Date(), 1, "huuhaa", t4);

    List<String> arvoJoukko5 = new ArrayList<String>();
    arvoJoukko5.add("eka");
    arvoJoukko5.add("toka");
    arvoJoukko5.add("kolmas");
    KirjausTyyppi t5 = new KirjausTyyppi(5, "Kenttä5", "Kenttä5 kuvaus", false,
        Tietotyyppi.MONIVALINTA, arvoJoukko5, "Huoltaja", "Henkilörekisteri");

    Kirjaus k5 = new Kirjaus("toka", new Date(), 1, "huuhaa", t5);

    l.add(t1);
    l.add(t2);
    l.add(t3);
    l.add(t4);
    l.add(t5);

    tyyppi.setKirjausTyypit(l);
    Date nyt = new Date();
    Date loppuu = new Date();
    loppuu.setYear(loppuu.getYear() + 1);

    KokoelmaTila tila = new KokoelmaTila(Tila.AKTIIVINEN, nyt, loppuu);
    Kokoelma k = new Kokoelma(tyyppi.getNimi(), tyyppi.getKuvaus(), tila,
        new Date(), 1, tyyppi);

    k.lisaaKirjaus(k1);
    k.lisaaKirjaus(k2);
    k.lisaaKirjaus(k3);
    k.lisaaKirjaus(k4);
    k.lisaaKirjaus(k5);
    return k;
  }
  // public KirjausTyyppi luoKirjausTyyppi() {
  // KirjausTyyppi tyyppi = new KirjausTyyppi(koodi, nimi, kuvaus, moniArvoinen,
  // tietoTyyppi, arvoJoukko, vastuutaho, rekisteri)
  // }
}
