package com.ixonos.koku.kks.malli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.ixonos.koku.kks.utils.Vakiot;
import com.ixonos.koku.kks.utils.enums.Tietotyyppi;
import com.ixonos.koku.kks.utils.enums.Tila;

/**
 * Generoi datan demoa varten
 * 
 * @author tuomape
 * 
 */
public class DemoFactory {

  public static final String NELI_VUOTIS_TARKASTUS = "4-vuotistarkastus";
  public static final String VASU = "Varhaiskasvatussuunnitelma";
  private static int codeGenerator = 0;

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
    h.getKks().lisaaKokoelma(luoVarhaiskasvatusSuunnitelma("Varhaiskasvatussuunnitelma"));
  }

  public static Kokoelma luoKokoelma(String nimi, String tyyppi) {

    if (tyyppi.equalsIgnoreCase(VASU)) {
      return luoVarhaiskasvatusSuunnitelma(nimi);
    } else if (tyyppi.equalsIgnoreCase(NELI_VUOTIS_TARKASTUS)) {
      return luo4VuotisTarkastus(nimi);
    }
    return null;
  }

  public static Kokoelma luoVarhaiskasvatusSuunnitelma(String nimi) {
    KokoelmaTyyppi tyyppi = new KokoelmaTyyppi(1, VASU, "Sisältää lapsen varhaiskasvatussuunnitelman");

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Miten lapsesi on viihtynyt päivähoidossa",
        "Kuvaa lapsen hyvinvointia", "Lapsen hyvinvointi", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_KYSELY),
        luoLuokitus(Vakiot.LUOKITUS_KUMPPANUUS, Vakiot.LUOKITUS_KOTI, Vakiot.LUOKITUS_HUOLENAIHEET)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Mitä lapsesi kertoo päivähoidosta kotona",
        "Kuvaa lapsen hyvinvointia", "Lapsen hyvinvointi", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_HAVAINTO),
        luoLuokitus(Vakiot.LUOKITUS_KUMPPANUUS, Vakiot.LUOKITUS_KOTI, Vakiot.LUOKITUS_HUOLENAIHEET)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Lapsellesi läheiset ja tärkeät ihmiset",
        "Kuvaa lapsen hyvinvointia", "Lapsen hyvinvointi", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_KYSELY), luoLuokitus(Vakiot.LUOKITUS_KUMPPANUUS, Vakiot.LUOKITUS_KOTI)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Onko sinulla perushoitoon liittyen jotain ajatuksia tai kysyttävää",
        "pukeminen ja riisuminen, hygienia, ruokailu, ulkoilu, lepohetki", "Lapsen hyvinvointi", "Päivähoito",
        "Päivähoidon asiakasrekisteri", "Huoltaja", luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_KOTI, Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Mietityttääkö sinua jokin asia lapsesi kehityksessä",
        "mm. pelot, arkuus, sosiaaliset taidot, puhe, motoriikka, siisteyskasvatus, perhetilanne, kasvatuskysymykset",
        "Lapsen hyvinvointi",
        "Päivähoito",
        "Päivähoidon asiakasrekisteri",
        "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_HUOLENAIHEET, Vakiot.LUOKITUS_TERVEYDENTILA, Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS,
            Vakiot.LUOKITUS_FYYSINEN_KEHITYS, Vakiot.LUOKITUS_KIELI_JA_KOMMUNIKAATIO)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Miten lapsesi suhtautuu uusiin tilanteisiin ja asioihin",
        "Kuvaa lapsen hyvinvointia", "Lapsen hyvinvointi", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_KOTI, Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Onko lapsellasi pelkoja", "Kuvaa lapsen hyvinvointia",
        "Lapsen hyvinvointi", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS, Vakiot.LUOKITUS_HUOLENAIHEET)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Oletko jostain asiasta huolissasi lapseesi liittyen",
        "mm. pelot, arkuus, sosiaaliset taidot, puhe, motoriikka, siisteyskasvatus, perhetilanne, kasvatuskysymykset",
        "Lapsen hyvinvointi",
        "Päivähoito",
        "Päivähoidon asiakasrekisteri",
        "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_TERVEYDENTILA, Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS,
            Vakiot.LUOKITUS_FYYSINEN_KEHITYS, Vakiot.LUOKITUS_KIELI_JA_KOMMUNIKAATIO, Vakiot.LUOKITUS_HUOLENAIHEET)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Mitkä asiat erityisesti tuottavat iloa lapsessasi",
        "Kuvaa lapsen hyvinvointia",
        "Lapsen hyvinvointi",
        "Päivähoito",
        "Päivähoidon asiakasrekisteri",
        "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS, Vakiot.LUOKITUS_TERVEYDENTILA,
            Vakiot.LUOKITUS_FYYSINEN_KEHITYS, Vakiot.LUOKITUS_KIELI_JA_KOMMUNIKAATIO,
            Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU, Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Mistä asioista lapsesi on kiinnostunut",
        "Kuvaa lapsen hyvinvointia", "Lapsen hyvinvointi", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_VAHVUUDET_JA_KIINNOSTUKSET, Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Miten lapsesi toiveet huomioidaan",
        "Kuvaa lapsen hyvinvointia", "Lapsen hyvinvointi", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_KYSELY),
        luoLuokitus(Vakiot.LUOKITUS_VAHVUUDET_JA_KIINNOSTUKSET, Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Yhteiset tavoitteet liittyen lapsen hyvinvointiin",
        "Kirjataan yhteiset tavoitteet liittyen lapsen hyvinvointiin", "", "Päivähoito",
        "Päivähoidon asiakasrekisteri", "Huoltaja ja päivähoito yhdessä",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_KESKUSTELU),
        luoLuokitus(Vakiot.LUOKITUS_KOTI, Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Mitä ajatuksia lapsesi päivähoitoon tulo- ja lähtötilanteet sinussa herättävät",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_KYSELY),
        luoLuokitus(Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT, Vakiot.LUOKITUS_KOTI)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Miten ilo tai suru näkyy lapsessasi",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS, Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Milloin lapsesi haluaa olla yksin",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_HAVAINTO),
        luoLuokitus(Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS, Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Onko lapsellasi kavereita kotona? päivähoidossa",
        "Kuvaa lapsen vuorovaikutusta",
        "Vuorovaikutus",
        "Päivähoito",
        "Päivähoidon asiakasrekisteri",
        "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_KYSELY),
        luoLuokitus(Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS, Vakiot.LUOKITUS_KOTI,
            Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU, Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Miten lapsesi suhtautuu toisiin lapsiin? lapsesi ryhmän jäsenenä?",
        "Kuvaa lapsen vuorovaikutusta",
        "Vuorovaikutus",
        "Päivähoito",
        "Päivähoidon asiakasrekisteri",
        "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS, Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU,
            Vakiot.LUOKITUS_LEIKKI, Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Miten lapsesi osaa ottaa toisen lapsen tunteet huomioon?",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_LEIKKI, Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Miten lapsesi suhtautuu aikuisiin?",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU, Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Missä tilanteissa lapsesi pyytää apua?",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU, Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Miten lapsesi käyttäytyy ristiriitatilanteissa?",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU, Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Mitä lapsesi leikkii? kenen kanssa?",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_HAVAINTO),
        luoLuokitus(Vakiot.LUOKITUS_LEIKKI, Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Miten lapsesi ilmaisee itseään?",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU, Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Yhteiset tavoitteet liittyen vuorovaikutukseen",
        "Kirjataan yhteiset tavoitteet liittyen lapsen vuorovaikutukseen", "", "Päivähoito",
        "Päivähoidon asiakasrekisteri", "Huoltaja ja päivähoito yhdessä",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_KESKUSTELU), luoLuokitus(Vakiot.LUOKITUS_KUMPPANUUS, Vakiot.LUOKITUS_KOTI)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Millaisia asioita toivoisit kerrottavan päivittäin lapsesi arjesta päivähoidossa",
        "Kuvaa kodin ja päivähoidon yhteistyötä", "Kodin ja päivähoidon yhteistyö", "Päivähoito",
        "Päivähoidon asiakasrekisteri", "Huoltaja", luoTyypit(Vakiot.KEHITYSASIATYYPPI_KYSELY),
        luoLuokitus(Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT, Vakiot.LUOKITUS_KOTI)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Miten yhteistyö hoidetaan molempiin vanhempiin",
        "Kuvaa kodin ja päivähoidon yhteistyötä", "Kodin ja päivähoidon yhteistyö", "Päivähoito",
        "Päivähoidon asiakasrekisteri", "Huoltaja", luoTyypit(Vakiot.KEHITYSASIATYYPPI_KYSELY),
        luoLuokitus(Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT, Vakiot.LUOKITUS_KOTI)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Millaisia yhteistyön tapoja toivotte",
        "Kuvaa kodin ja päivähoidon yhteistyötä", "Kodin ja päivähoidon yhteistyö", "Päivähoito",
        "Päivähoidon asiakasrekisteri", "Huoltaja", luoTyypit(Vakiot.KEHITYSASIATYYPPI_KYSELY),
        luoLuokitus(Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT, Vakiot.LUOKITUS_KOTI)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Minkälaiset asiat teille on kasvatuksessa tärkeitä? mitä toivotte meiltä",
        "Kuvaa kodin ja päivähoidon yhteistyötä", "Kodin ja päivähoidon yhteistyö", "Päivähoito",
        "Päivähoidon asiakasrekisteri", "Huoltaja", luoTyypit(Vakiot.KEHITYSASIATYYPPI_KYSELY),
        luoLuokitus(Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT, Vakiot.LUOKITUS_KOTI)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Yhteiset tavoitteet liittyen yhteistyöhön",
        "Kirjataan yhteiset tavoitteet liittyen yhteistyöhön", "", "Päivähoito", "Päivähoidon asiakasrekisteri",
        "Huoltaja ja päivähoito yhdessä", luoTyypit(Vakiot.KEHITYSASIATYYPPI_KESKUSTELU),
        luoLuokitus(Vakiot.LUOKITUS_KUMPPANUUS)));

    tyyppi.lisaaKirjausTyyppi(luoMoniarvoinenVapaaTekstiKirjausTyyppi("Kommentit, havainnot ja tavoitteet",
        "Kirjatut kommentit, havainnot ja tavoitteet", "", "", "", "", "kommentti, havainto tai tavoite",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_KESKUSTELU),
        luoLuokitus(Vakiot.LUOKITUS_KOMMENTTI, Vakiot.LUOKITUS_TAVOITE, Vakiot.LUOKITUS_HAVAINTO)));

    Date nyt = new Date();
    Date loppuu = new Date();
    loppuu.setYear(loppuu.getYear() + 1);

    KokoelmaTila tila = new KokoelmaTila(Tila.AKTIIVINEN, nyt, loppuu);
    Kokoelma k = new Kokoelma("" + codeGenerator, nimi, tyyppi.getKuvaus(), tila, new Date(), 1, tyyppi);
    luoTyhjatKirjaukset(k);
    return k;
  }

  public static Kokoelma luo4VuotisTarkastus(String nimi) {
    KokoelmaTyyppi tyyppi = new KokoelmaTyyppi(1, NELI_VUOTIS_TARKASTUS,
        "Sisältää tiedot lapsen 4-vuotistarkastuksesta");

    tyyppi
        .lisaaKirjausTyyppi(luoValintaKirjausTyyppi(
            "Pukee ja riisuu itse",
            "kuvaa päivittäistoimintojen sujumista",
            "päivittäistoiminnot",
            "päivähoito",
            "päivähoidon asiakasrekisteri",
            "Päivähoito",
            luoArvot("kyllä", "opettelee", "ei vielä"),
            luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
            luoLuokitus(Vakiot.LUOKITUS_MOTORIIKKA, Vakiot.LUOKITUS_KEHON_HALLINTA,
                Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT)));

    tyyppi
        .lisaaKirjausTyyppi(luoValintaKirjausTyyppi(
            "Syö siististi",
            "kuvaa päivittäistoimintojen sujumista",
            "päivittäistoiminnot",
            "päivähoito",
            "päivähoidon asiakasrekisteri",
            "Päivähoito",
            luoArvot("kyllä", "opettelee", "ei vielä"),
            luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
            luoLuokitus(Vakiot.LUOKITUS_MOTORIIKKA, Vakiot.LUOKITUS_KEHON_HALLINTA,
                Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT)));

    tyyppi
        .lisaaKirjausTyyppi(luoValintaKirjausTyyppi(
            "Käy omatoimisesti wc:ssä",
            "kuvaa päivittäistoimintojen sujumista",
            "päivittäistoiminnot",
            "päivähoito",
            "päivähoidon asiakasrekisteri",
            "Päivähoito",
            luoArvot("kyllä", "opettelee", "ei vielä"),
            luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
            luoLuokitus(Vakiot.LUOKITUS_MOTORIIKKA, Vakiot.LUOKITUS_KEHON_HALLINTA,
                Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi(
        "Hahmottaa hoitopaikan hoitorytmin",
        "kuvaa päivittäistoimintojen sujumista",
        "päivittäistoiminnot",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoArvot("kyllä", "opettelee", "ei vielä"),
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_TARKKAAVAISUUS, Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS,
            Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi(
        "Siirtyminen toiminnasta toiseen onnistuu",
        "kuvaa päivittäistoimintojen sujumista",
        "päivittäistoiminnot",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoArvot("kyllä", "opettelee", "ei vielä"),
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_TARKKAAVAISUUS, Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS,
            Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi("Päivälepo", "kuvaa päivittäistoimintojen sujumista",
        "päivittäistoiminnot", "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito",
        luoArvot("Nukkuu päivittäin", "Satunnaisesti", "Ei nuku päiväunia"), luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_PAIVITTAISET_TOIMINNOT)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi(
        "Juoksee sujuvasti",
        "kuvaa fyysistä kehitystä",
        "liikkuminen ja hahmottaminen",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoArvot("kyllä", "vaihtelevasti", "ei vielä"),
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_FYYSINEN_KEHITYS, Vakiot.LUOKITUS_KEHON_HALLINTA,
            Vakiot.LUOKITUS_LIIKUNANLLISET_TAIDOT, Vakiot.LUOKITUS_MOTORIIKKA_KARKEA)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi(
        "Kävelee kapealla penkillä / narua pitkin",
        "kuvaa fyysistä kehitystä",
        "liikkuminen ja hahmottaminen",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoArvot("kyllä", "vaihtelevasti", "ei vielä"),
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_FYYSINEN_KEHITYS, Vakiot.LUOKITUS_KEHON_HALLINTA,
            Vakiot.LUOKITUS_LIIKUNANLLISET_TAIDOT, Vakiot.LUOKITUS_MOTORIIKKA_KARKEA)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi(
        "Hyppää tasajalkahyppyä",
        "kuvaa fyysistä kehitystä",
        "liikkuminen ja hahmottaminen",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoArvot("kyllä", "vaihtelevasti", "ei vielä"),
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_FYYSINEN_KEHITYS, Vakiot.LUOKITUS_KEHON_HALLINTA,
            Vakiot.LUOKITUS_LIIKUNANLLISET_TAIDOT, Vakiot.LUOKITUS_MOTORIIKKA_KARKEA)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi(
        "Kävelee portaita ylös ja alas vuorotahtiin",
        "kuvaa fyysistä kehitystä",
        "liikkuminen ja hahmottaminen",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoArvot("kyllä", "vaihtelevasti", "ei vielä"),
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_FYYSINEN_KEHITYS, Vakiot.LUOKITUS_KEHON_HALLINTA,
            Vakiot.LUOKITUS_LIIKUNANLLISET_TAIDOT, Vakiot.LUOKITUS_MOTORIIKKA_KARKEA)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi(
        "Kiipeilee",
        "kuvaa fyysistä kehitystä",
        "liikkuminen ja hahmottaminen",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoArvot("kyllä", "vaihtelevasti", "ei vielä"),
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_FYYSINEN_KEHITYS, Vakiot.LUOKITUS_KEHON_HALLINTA,
            Vakiot.LUOKITUS_LIIKUNANLLISET_TAIDOT, Vakiot.LUOKITUS_MOTORIIKKA_KARKEA)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi("Tunnistaa ja nimeää kehonosia", "kuvaa psyykkistä kehitystä",
        "liikkuminen ja hahmottaminen", "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito",
        luoArvot("kyllä", "vaihtelevasti", "ei vielä"), luoTyypit(Vakiot.KEHITYSASIATYYPPI_HAVAINTO),
        luoLuokitus(Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi("Piirtää tunnistettavia asioita", "kuvaa psyykkistä kehitystä",
        "liikkuminen ja hahmottaminen", "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito",
        luoArvot("kyllä", "vaihtelevasti", "ei vielä"), luoTyypit(Vakiot.KEHITYSASIATYYPPI_HAVAINTO),
        luoLuokitus(Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi("Leikkaa saksilla", "kuvaa fyysistä ja psyykkistä kehitystä",
        "liikkuminen ja hahmottaminen", "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito",
        luoArvot("kyllä", "vaihtelevasti", "ei vielä"), luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS, Vakiot.LUOKITUS_MOTORIIKKA_HIENO)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi("Kokoaa palapelejä", "kuvaa psyykkistä kehitystä",
        "liikkuminen ja hahmottaminen", "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito",
        luoArvot("n.12", "n.20", ">20"), luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS, Vakiot.LUOKITUS_MOTORIIKKA_HIENO)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi("Kätisyys", "kuvaa fyysistä ja psyykkistä kehitystä",
        "liikkuminen ja hahmottaminen", "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito",
        luoArvot("Oikea", "Vasen", "Vaihtaen"), luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi(
        "Ottaa ja säilyttää katsekontaktin vuorovaikutuksessa",
        "kuvaa vuorovaikutuksen ja kielen kehitystä",
        "kieli ja kommunikaatio",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoArvot("Yleensä", "Vaihtelevasti", "Ei vielä"),
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_HAVAINTO),
        luoLuokitus(Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS, Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU,
            Vakiot.LUOKITUS_KIELI_JA_KOMMUNIKAATIO)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi(
        "Osaa kuunnella vastavuoroisesti",
        "kuvaa vuorovaikutuksen ja kielen kehitystä",
        "kieli ja kommunikaatio",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoArvot("Yleensä", "Vaihtelevasti", "Ei vielä"),
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_HAVAINTO),
        luoLuokitus(Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU, Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS,
            Vakiot.LUOKITUS_KIELI_JA_KOMMUNIKAATIO)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi("Toimii ohjeiden mukaan",
        "kuvaa vuorovaikutuksen ja kielen kehitystä", "kieli ja kommunikaatio", "päivähoito",
        "päivähoidon asiakasrekisteri", "Päivähoito", luoArvot("Yleensä", "Vaihtelevasti", "Ei vielä"),
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_KIELI_JA_KOMMUNIKAATIO, Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi(
        "On kiinnostunut saduista ja kertomuksista",
        "kuvaa vuorovaikutuksen ja kielen kehitystä",
        "kieli ja kommunikaatio",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoArvot("Yleensä", "Vaihtelevasti", "Ei vielä"),
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_HAVAINTO),
        luoLuokitus(Vakiot.LUOKITUS_KIELI_JA_KOMMUNIKAATIO, Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS,
            Vakiot.LUOKITUS_TARKKAAVAISUUS)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi(
        "Puhe on selkeää ja ymmärrettävää",
        "kuvaa kielen kehitystä",
        "kieli ja kommunikaatio",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoArvot("Yleensä", "Vaihtelevasti", "Ei vielä"),
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_KIELI_JA_KOMMUNIKAATIO, Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS,
            Vakiot.LUOKITUS_MOTORIIKKA_HIENO)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Huomioita puheesta",
        "esim. äänivirheet, änkytys, käheys, jne",
        "kieli ja kommunikaatio",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_KIELI_JA_KOMMUNIKAATIO, Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS,
            Vakiot.LUOKITUS_MOTORIIKKA_HIENO)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi("Ymmärtää lukumäärien 1-4 vastaavuuden",
        "kuvaa matemaattisten taitojen kehitystä", "kieli ja kommunikaatio", "päivähoito",
        "päivähoidon asiakasrekisteri", "Päivähoito", luoArvot("Yleensä", "Vaihtelevasti", "Ei vielä"),
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO), luoLuokitus(Vakiot.LUOKITUS_MATEMAATTISET_TAIDOT)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi("Tunnistaa ja nimeää perusvärit", "kuvaa kielen kehitystä",
        "kieli ja kommunikaatio", "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito",
        luoArvot("Yleensä", "Vaihtelevasti", "Ei vielä"), luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_KIELI_JA_KOMMUNIKAATIO)));

    tyyppi.lisaaKirjausTyyppi(luoValintaKirjausTyyppi(
        "Osaa kertoa pieniä tarinoita / osaa kertoa tapahtuneista asioista",
        "kuvaa kielen kehitystä",
        "kieli ja kommunikaatio",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoArvot("Yleensä", "Vaihtelevasti", "Ei vielä"),
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_KIELI_JA_KOMMUNIKAATIO, Vakiot.LUOKITUS_TARKKAAVAISUUS,
            Vakiot.LUOKITUS_KESKITTYMINEN_JA_MUISTI, Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Vahvuudet ja vaikeudet leikeissä",
        "kuvaa vuorovaikutuksen ja kielen kehitystä",
        "",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_HAVAINTO),
        luoLuokitus(Vakiot.LUOKITUS_HUOLENAIHEET, Vakiot.LUOKITUS_VAHVUUDET_JA_KIINNOSTUKSET, Vakiot.LUOKITUS_LEIKKI,
            Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Vahvuudet ja vaikeudet sosiaalisissa tilanteissa",
        "kuvaa vuorovaikutuksen ja kielen kehitystä",
        "",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_HAVAINTO),
        luoLuokitus(Vakiot.LUOKITUS_HUOLENAIHEET, Vakiot.LUOKITUS_VAHVUUDET_JA_KIINNOSTUKSET,
            Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Terveiset neuvolaan", "kerro terveiset neuvolaan", "",
        "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito", luoTyypit(Vakiot.KEHITYSASIATYYPPI_PALAUTE),
        luoLuokitus(Vakiot.LUOKITUS_KOTI)));

    /********************************/

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Yleistä neuvolakäynnin sujumisesta",
        "kuvaa neuvolakäynnin sujumisen", "", "neuvola", "potilasrekisteri", "Neuvola",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_HAVAINTO), luoLuokitus(Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Onko huolenaiheita ja miten mahdolliset huolenaiheet on huomioitu",
        "mm. pelot, arkuus, sosiaaliset taidot, puhe, motoriikka, siisteyskasvatus, perhetilanne, kasvatuskysymykset",
        "",
        "neuvola",
        "potilasrekisteri",
        "Neuvola",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_ARVIO),
        luoLuokitus(Vakiot.LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU, Vakiot.LUOKITUS_HUOLENAIHEET,
            Vakiot.LUOKITUS_TERVEYDENTILA, Vakiot.LUOKITUS_PSYYKKINEN_KEHITYS, Vakiot.LUOKITUS_FYYSINEN_KEHITYS,
            Vakiot.LUOKITUS_KIELI_JA_KOMMUNIKAATIO)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Neuvola ohjannut jatkoselvityksiin", "lähetteet", "",
        "neuvola", "potilasrekisteri", "Neuvola", luoTyypit(Vakiot.KEHITYSASIATYYPPI_LAHETE),
        luoLuokitus(Vakiot.LUOKITUS_TERVEYDENTILA)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Terveiset päivähoitoon", "neuvolan terveiset päivähoidolle",
        "", "neuvola", "potilasrekisteri", "Neuvola", luoTyypit(Vakiot.KEHITYSASIATYYPPI_PALAUTE),
        luoLuokitus(Vakiot.LUOKITUS_TERVEYDENTILA)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Terveiset päivähoitoon", "neuvolan terveiset päivähoidolle",
        "", "neuvola", "potilasrekisteri", "Neuvola", luoTyypit(Vakiot.KEHITYSASIATYYPPI_PALAUTE),
        luoLuokitus(Vakiot.LUOKITUS_TERVEYDENTILA)));

    tyyppi.lisaaKirjausTyyppi(luoTekstiKirjausTyyppi("Päänympärys", "lapsen päänympärys sentteinä", "Mittaukset",
        "neuvola", "potilasrekisteri", "Neuvola", luoTyypit(Vakiot.KEHITYSASIATYYPPI_KAYNTI),
        luoLuokitus(Vakiot.LUOKITUS_MITTAUS)));

    tyyppi
        .lisaaKirjausTyyppi(luoTekstiKirjausTyyppi("Pituus", "lapsen pituus sentteinä", "Mittaukset", "neuvola",
            "potilasrekisteri", "Neuvola", luoTyypit(Vakiot.KEHITYSASIATYYPPI_KAYNTI),
            luoLuokitus(Vakiot.LUOKITUS_MITTAUS)));

    tyyppi
        .lisaaKirjausTyyppi(luoTekstiKirjausTyyppi("Paino", "lapsen paino kiloina", "Mittaukset", "neuvola",
            "potilasrekisteri", "Neuvola", luoTyypit(Vakiot.KEHITYSASIATYYPPI_KAYNTI),
            luoLuokitus(Vakiot.LUOKITUS_MITTAUS)));

    /********************************/

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Mikä 4 – vuotiaassanne on parasta ja missä asioissa hän on hyvä?", "Kuvaa lapsen vahvuuksia", "", "neuvola",
        "potilasrekisteri", "Huoltaja", luoTyypit(Vakiot.KEHITYSASIATYYPPI_KYSELY),
        luoLuokitus(Vakiot.LUOKITUS_VANHEMPIEN_ARVOSTAMAT_ASIAT)));

    tyyppi
        .lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Mitä asioita teette yhdessä perheen kanssa?",
            "Kuvaa perheen arvoja", "", "neuvola", "potilasrekisteri", "Huoltaja",
            luoTyypit(Vakiot.KEHITYSASIATYYPPI_KYSELY),
            luoLuokitus(Vakiot.LUOKITUS_VANHEMPIEN_KASVATUKSELLISET_TAVOITTEET)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Mitä teette yhdessä 4-vuotiaanne kanssa ja mitkä tilanteet tuntuvat mukavilta?", "Kuvaa perheen arvoja", "",
        "neuvola", "potilasrekisteri", "Huoltaja", luoTyypit(Vakiot.KEHITYSASIATYYPPI_KYSELY),
        luoLuokitus(Vakiot.LUOKITUS_VANHEMPIEN_ARVOSTAMAT_ASIAT)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Miten perheessänne arki sujuu ja miten käsittelette ristiriitatilanteita?",
        "Kuvaa perheen kasvatuksellisia keinoja", "", "neuvola", "potilasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_KYSELY),
        luoLuokitus(Vakiot.LUOKITUS_VANHEMPIEN_KASVATUKSELLISET_TAVOITTEET, Vakiot.LUOKITUS_HUOLENAIHEET)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Onko perheessänne tapahtunut viime aikoina jotain erityistä, mistä haluatte kertoa?",
        "Kuvaa perheen nykytilannetta/muutosta", "", "neuvola", "potilasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_KYSELY), luoLuokitus(Vakiot.LUOKITUS_HUOLENAIHEET)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Oletteko huolissanne jostain lapsenne käyttäytymiseen tai kehitykseen liittyvästä asiasta?",
        "Kuvaa huolenaiheita", "", "neuvola", "potilasrekisteri", "Huoltaja",
        luoTyypit(Vakiot.KEHITYSASIATYYPPI_KYSELY),
        luoLuokitus(Vakiot.LUOKITUS_VANHEMPIEN_KASVATUKSELLISET_TAVOITTEET, Vakiot.LUOKITUS_HUOLENAIHEET)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi(
        "Onko lapsenne päivähoidossa tai kerhossa ja miten ryhmässä toimiminen sujuu?", "Kuvaa lasta ryhmässä", "",
        "neuvola", "potilasrekisteri", "Huoltaja", luoTyypit(Vakiot.KEHITYSASIATYYPPI_KYSELY),
        luoLuokitus(Vakiot.LUOKITUS_HUOLENAIHEET)));

    tyyppi.lisaaKirjausTyyppi(luoVapaaTekstiKirjausTyyppi("Haluan keskustella lisäksi", "Kuvaa keskustelutarpeet", "",
        "neuvola", "potilasrekisteri", "Huoltaja", luoTyypit(Vakiot.KEHITYSASIATYYPPI_TOIVE),
        luoLuokitus(Vakiot.LUOKITUS_HUOLENAIHEET)));

    Date nyt = new Date();
    Date loppuu = new Date();
    loppuu.setYear(loppuu.getYear() + 1);

    KokoelmaTila tila = new KokoelmaTila(Tila.AKTIIVINEN, nyt, loppuu);
    Kokoelma k = new Kokoelma("" + codeGenerator, nimi, tyyppi.getKuvaus(), tila, new Date(), 1, tyyppi);
    luoTyhjatKirjaukset(k);
    return k;
  }

  public static List<String> luoArvot(String... joukko) {
    return Arrays.asList(joukko);
  }

  public static List<Luokitus> luoLuokitus(String... luokitukset) {
    List<Luokitus> l = new ArrayList<Luokitus>();

    for (String s : luokitukset) {
      codeGenerator++;
      l.add(new Luokitus(codeGenerator, s, ""));
    }
    return l;
  }

  public static List<Luokitus> luoTyypit(String... tyypit) {
    List<Luokitus> l = new ArrayList<Luokitus>();

    for (String s : tyypit) {
      codeGenerator++;
      l.add(new Luokitus(codeGenerator, s, ""));
    }
    return l;
  }

  public static KirjausTyyppi luoMonivalintaKirjausTyyppi(String kentta, String kuvaus, String ryhma,
      String vastuutaho, String rekisteri, String tayttaja, List<String> arvoJoukko, List<Luokitus> tyypit,
      List<Luokitus> luokitukset) {
    codeGenerator++;

    KirjausTyyppi tyyppi = new KirjausTyyppi(codeGenerator, kentta, kuvaus, false, Tietotyyppi.MONIVALINTA, arvoJoukko,
        vastuutaho, rekisteri, ryhma, tayttaja);

    tyyppi.setLuokitukset(luokitukset);
    tyyppi.setKehitysasiaTyypit(tyypit);
    return tyyppi;
  }

  public static KirjausTyyppi luoValintaKirjausTyyppi(String kentta, String kuvaus, String ryhma, String vastuutaho,
      String rekisteri, String tayttaja, List<String> arvoJoukko, List<Luokitus> tyypit, List<Luokitus> luokitukset) {
    codeGenerator++;

    KirjausTyyppi tyyppi = new KirjausTyyppi(codeGenerator, kentta, kuvaus, false, Tietotyyppi.VALINTA, arvoJoukko,
        vastuutaho, rekisteri, ryhma, tayttaja);

    tyyppi.setLuokitukset(luokitukset);
    tyyppi.setKehitysasiaTyypit(tyypit);
    return tyyppi;
  }

  public static KirjausTyyppi luoVapaaTekstiKirjausTyyppi(String kentta, String kuvaus, String ryhma,
      String vastuutaho, String rekisteri, String tayttaja, List<Luokitus> tyypit, List<Luokitus> luokitukset) {
    codeGenerator++;

    KirjausTyyppi tyyppi = new KirjausTyyppi(codeGenerator, kentta, kuvaus, false, Tietotyyppi.VAPAA_TEKSTI, null,
        vastuutaho, rekisteri, ryhma, tayttaja);

    tyyppi.setLuokitukset(luokitukset);
    tyyppi.setKehitysasiaTyypit(tyypit);
    return tyyppi;
  }

  public static KirjausTyyppi luoMoniarvoinenVapaaTekstiKirjausTyyppi(String kentta, String kuvaus, String ryhma,
      String vastuutaho, String rekisteri, String tayttaja, String luontiMuoto, List<Luokitus> tyypit,
      List<Luokitus> luokitukset) {
    codeGenerator++;

    KirjausTyyppi tyyppi = new KirjausTyyppi(codeGenerator, kentta, kuvaus, true, Tietotyyppi.VAPAA_TEKSTI, null,
        vastuutaho, rekisteri, ryhma, tayttaja);

    tyyppi.setLuokitukset(luokitukset);
    tyyppi.setKehitysasiaTyypit(tyypit);
    tyyppi.setLuontiKuvaus(luontiMuoto);
    return tyyppi;
  }

  public static KirjausTyyppi luoTekstiKirjausTyyppi(String kentta, String kuvaus, String ryhma, String vastuutaho,
      String rekisteri, String tayttaja, List<Luokitus> tyypit, List<Luokitus> luokitukset) {
    codeGenerator++;

    KirjausTyyppi tyyppi = new KirjausTyyppi(codeGenerator, kentta, kuvaus, false, Tietotyyppi.TEKSTI, null,
        vastuutaho, rekisteri, ryhma, tayttaja);

    tyyppi.setLuokitukset(luokitukset);
    tyyppi.setKehitysasiaTyypit(tyypit);
    return tyyppi;
  }

  public static void luoTyhjatKirjaukset(Kokoelma k) {
    for (KirjausTyyppi t : k.getTyyppi().getKirjausTyypit()) {
      k.lisaaKirjaus(luoKirjaus(t, ""));
    }
  }

  public static Kirjaus luoKirjaus(KirjausTyyppi t, String kirjaus) {
    Kirjaus k = new Kirjaus(kirjaus, new Date(), 1, t.getRekisteri(), "Kaisa Kirjaaja", t);
    return k;
  }
}
