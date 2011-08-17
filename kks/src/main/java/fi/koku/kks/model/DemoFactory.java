package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import fi.koku.kks.ui.common.DataType;
import fi.koku.kks.ui.common.State;
import fi.koku.kks.ui.common.utils.Constants;

/**
 * Generoi datan demoa varten
 * 
 * @author tuomape
 * 
 */
public class DemoFactory {

  public static final String NELI_VUOTIS_TARKASTUS = "4-vuotiaan terveystarkastus";
  public static final String VASU = "Varhaiskasvatussuunnitelma";
  private static int codeGenerator = 100;

  public static KKSDemoModel luo() {
    Person h1 = new Person("Paavo", "Petteri", "Paavola", "270608-223Z");
    Person h2 = new Person("Sanna", "Maria", "Paavola", "200107-211Y");
    lisaacollectiont(h1);

    KKSDemoModel malli = new KKSDemoModel();
    malli.addPerson(h1);
    malli.addPerson(h2);
    return malli;
  }

  public static void lisaacollectiont(Person h) {
    h.getKks().addCollection(luoVarhaiskasvatusSuunnitelma("Varhaiskasvatussuunnitelma"));
  }

  public static KKSCollection createCollection(String nimi, String type) {

    if (type.equalsIgnoreCase(VASU)) {
      return luoVarhaiskasvatusSuunnitelma(nimi);
    } else if (type.equalsIgnoreCase(NELI_VUOTIS_TARKASTUS)) {
      return luo4VuotisTarkastus(nimi);
    }
    return null;
  }

  public static KKSCollection createNewVersion(KKSCollection vanha, String nimi, boolean clean) {
    KKSCollection k = new KKSCollection("" + ++codeGenerator, vanha, clean, new Date(), new CollectionState(
        State.ACTIVE), 1);
    k.setName(nimi);
    vanha.setVersioned(true);
    vanha.setNextVersion(k.getId());
    vanha.getState().setState(State.LOCKED);
    return k;
  }

  public static CollectionType luoVarhaiskasvatusSuunnitelmantype() {
    return new CollectionType(1, VASU, "Sisältää lapsen varhaiskasvatussuunnitelman");
  }

  public static CollectionType luoNelivuotisTarkastustype() {
    return new CollectionType(2, NELI_VUOTIS_TARKASTUS, "Sisältää tiedot lapsen 4-vuotiaan terveystarkastuksesta");
  }

  public static KKSCollection luoVarhaiskasvatusSuunnitelma(String nimi) {
    CollectionType type = luoVarhaiskasvatusSuunnitelmantype();

    type.addEntryGroup(new Group(new GroupKey(1, "Huoltaja"), "Päivähoidon asiakasrekisteri"));
    type.addEntryGroup(new Group(new GroupKey(2, "Päivähoito"), "Päivähoidon asiakasrekisteri"));

    type.addEntryType(luoVapaaTekstientryType("Miten lapsesi on viihtynyt päivähoidossa",
        "Kuvaa lapsen hyvinvointia", "Lapsen hyvinvointi", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_KYSELY),
        luoclassification(Constants.classification_KUMPPANUUS, Constants.classification_KOTI, Constants.classification_HUOLENAIHEET)));

    type.addEntryType(luoVapaaTekstientryType("Mitä lapsesi kertoo päivähoidosta kotona",
        "Kuvaa lapsen hyvinvointia", "Lapsen hyvinvointi", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_HAVAINTO),
        luoclassification(Constants.classification_KUMPPANUUS, Constants.classification_KOTI, Constants.classification_HUOLENAIHEET)));

    type.addEntryType(luoVapaaTekstientryType("Lapsellesi läheiset ja tärkeät ihmiset",
        "Kuvaa lapsen hyvinvointia", "Lapsen hyvinvointi", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_KYSELY),
        luoclassification(Constants.classification_KUMPPANUUS, Constants.classification_KOTI)));

    type.addEntryType(luoVapaaTekstientryType(
        "Onko sinulla perushoitoon liittyen jotain ajatuksia tai kysyttävää",
        "pukeminen ja riisuminen, hygienia, ruokailu, ulkoilu, lepohetki", "Lapsen hyvinvointi", "Päivähoito",
        "Päivähoidon asiakasrekisteri", "Huoltaja", luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_KOTI, Constants.classification_PAIVITTAISET_TOIMINNOT)));

    type.addEntryType(luoVapaaTekstientryType(
        "Mietityttääkö sinua jokin asia lapsesi kehityksessä",
        "mm. pelot, arkuus, sosiaaliset taidot, puhe, motoriikka, siisteyskasvatus, perhetilanne, kasvatuskysymykset",
        "Lapsen hyvinvointi",
        "Päivähoito",
        "Päivähoidon asiakasrekisteri",
        "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_HUOLENAIHEET, Constants.classification_TERVEYDENTILA,
            Constants.classification_PSYYKKINEN_KEHITYS, Constants.classification_FYYSINEN_KEHITYS,
            Constants.classification_KIELI_JA_KOMMUNIKAATIO)));

    type.addEntryType(luoVapaaTekstientryType("Miten lapsesi suhtautuu uusiin tilanteisiin ja asioihin",
        "Kuvaa lapsen hyvinvointia", "Lapsen hyvinvointi", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_KOTI, Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU)));

    type.addEntryType(luoVapaaTekstientryType("Onko lapsellasi pelkoja", "Kuvaa lapsen hyvinvointia",
        "Lapsen hyvinvointi", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_PSYYKKINEN_KEHITYS, Constants.classification_HUOLENAIHEET)));

    type.addEntryType(luoVapaaTekstientryType(
        "Oletko jostain asiasta huolissasi lapseesi liittyen",
        "mm. pelot, arkuus, sosiaaliset taidot, puhe, motoriikka, siisteyskasvatus, perhetilanne, kasvatuskysymykset",
        "Lapsen hyvinvointi",
        "Päivähoito",
        "Päivähoidon asiakasrekisteri",
        "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_TERVEYDENTILA, Constants.classification_PSYYKKINEN_KEHITYS,
            Constants.classification_FYYSINEN_KEHITYS, Constants.classification_KIELI_JA_KOMMUNIKAATIO,
            Constants.classification_HUOLENAIHEET)));

    type.addEntryType(luoVapaaTekstientryType(
        "Mitkä asiat erityisesti tuottavat iloa lapsessasi",
        "Kuvaa lapsen hyvinvointia",
        "Lapsen hyvinvointi",
        "Päivähoito",
        "Päivähoidon asiakasrekisteri",
        "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_PSYYKKINEN_KEHITYS, Constants.classification_TERVEYDENTILA,
            Constants.classification_FYYSINEN_KEHITYS, Constants.classification_KIELI_JA_KOMMUNIKAATIO,
            Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU, Constants.classification_PAIVITTAISET_TOIMINNOT)));

    type.addEntryType(luoVapaaTekstientryType("Mistä asioista lapsesi on kiinnostunut",
        "Kuvaa lapsen hyvinvointia", "Lapsen hyvinvointi", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_VAHVUUDET_JA_KIINNOSTUKSET, Constants.classification_PAIVITTAISET_TOIMINNOT)));

    type.addEntryType(luoVapaaTekstientryType("Miten lapsesi toiveet huomioidaan", "Kuvaa lapsen hyvinvointia",
        "Lapsen hyvinvointi", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_KYSELY),
        luoclassification(Constants.classification_VAHVUUDET_JA_KIINNOSTUKSET, Constants.classification_PAIVITTAISET_TOIMINNOT)));

    type.addEntryType(luoVapaaTekstientryType("Yhteiset tavoitteet liittyen lapsen hyvinvointiin",
        "Kirjataan yhteiset tavoitteet liittyen lapsen hyvinvointiin", "", "Päivähoito",
        "Päivähoidon asiakasrekisteri", "Päivähoito", luoTyypit(Constants.KEHITYSASIAtype_KESKUSTELU),
        luoclassification(Constants.classification_KOTI, Constants.classification_PAIVITTAISET_TOIMINNOT)));

    type.addEntryType(luoVapaaTekstientryType(
        "Mitä ajatuksia lapsesi päivähoitoon tulo- ja lähtötilanteet sinussa herättävät",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_KYSELY),
        luoclassification(Constants.classification_PAIVITTAISET_TOIMINNOT, Constants.classification_KOTI)));

    type.addEntryType(luoVapaaTekstientryType("Miten ilo tai suru näkyy lapsessasi",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_PSYYKKINEN_KEHITYS, Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU)));

    type.addEntryType(luoVapaaTekstientryType("Milloin lapsesi haluaa olla yksin",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_HAVAINTO),
        luoclassification(Constants.classification_PSYYKKINEN_KEHITYS, Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU)));

    type.addEntryType(luoVapaaTekstientryType(
        "Onko lapsellasi kavereita kotona? päivähoidossa",
        "Kuvaa lapsen vuorovaikutusta",
        "Vuorovaikutus",
        "Päivähoito",
        "Päivähoidon asiakasrekisteri",
        "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_KYSELY),
        luoclassification(Constants.classification_PSYYKKINEN_KEHITYS, Constants.classification_KOTI,
            Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU, Constants.classification_PAIVITTAISET_TOIMINNOT)));

    type.addEntryType(luoVapaaTekstientryType(
        "Miten lapsesi suhtautuu toisiin childin? lapsesi ryhmän jäsenenä?",
        "Kuvaa lapsen vuorovaikutusta",
        "Vuorovaikutus",
        "Päivähoito",
        "Päivähoidon asiakasrekisteri",
        "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_PSYYKKINEN_KEHITYS, Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU,
            Constants.classification_LEIKKI, Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU)));

    type.addEntryType(luoVapaaTekstientryType("Miten lapsesi osaa ottaa toisen lapsen tunteet huomioon?",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_LEIKKI, Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU)));

    type.addEntryType(luoVapaaTekstientryType("Miten lapsesi suhtautuu aikuisiin?",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU, Constants.classification_PSYYKKINEN_KEHITYS)));

    type.addEntryType(luoVapaaTekstientryType("Missä tilanteissa lapsesi pyytää apua?",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU, Constants.classification_PAIVITTAISET_TOIMINNOT)));

    type.addEntryType(luoVapaaTekstientryType("Miten lapsesi käyttäytyy ristiriitatilanteissa?",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU, Constants.classification_PAIVITTAISET_TOIMINNOT)));

    type.addEntryType(luoVapaaTekstientryType("Mitä lapsesi leikkii? kenen kanssa?",
        "Kuvaa lapsen vuorovaikutusta", "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_HAVAINTO),
        luoclassification(Constants.classification_LEIKKI, Constants.classification_PAIVITTAISET_TOIMINNOT)));

    type.addEntryType(luoVapaaTekstientryType("Miten lapsesi ilmaisee itseään?", "Kuvaa lapsen vuorovaikutusta",
        "Vuorovaikutus", "Päivähoito", "Päivähoidon asiakasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU, Constants.classification_PAIVITTAISET_TOIMINNOT)));

    type.addEntryType(luoVapaaTekstientryType("Yhteiset tavoitteet liittyen vuorovaikutukseen",
        "Kirjataan yhteiset tavoitteet liittyen lapsen vuorovaikutukseen", "", "Päivähoito",
        "Päivähoidon asiakasrekisteri", "Päivähoito", luoTyypit(Constants.KEHITYSASIAtype_KESKUSTELU),
        luoclassification(Constants.classification_KUMPPANUUS, Constants.classification_KOTI)));

    type.addEntryType(luoVapaaTekstientryType(
        "Millaisia asioita toivoisit kerrottavan päivittäin lapsesi arjesta päivähoidossa",
        "Kuvaa kodin ja päivähoidon yhteistyötä", "Kodin ja päivähoidon yhteistyö", "Päivähoito",
        "Päivähoidon asiakasrekisteri", "Huoltaja", luoTyypit(Constants.KEHITYSASIAtype_KYSELY),
        luoclassification(Constants.classification_PAIVITTAISET_TOIMINNOT, Constants.classification_KOTI)));

    type.addEntryType(luoVapaaTekstientryType("Miten yhteistyö hoidetaan molempiin vanhempiin",
        "Kuvaa kodin ja päivähoidon yhteistyötä", "Kodin ja päivähoidon yhteistyö", "Päivähoito",
        "Päivähoidon asiakasrekisteri", "Huoltaja", luoTyypit(Constants.KEHITYSASIAtype_KYSELY),
        luoclassification(Constants.classification_PAIVITTAISET_TOIMINNOT, Constants.classification_KOTI)));

    type.addEntryType(luoVapaaTekstientryType("Millaisia yhteistyön tapoja toivotte",
        "Kuvaa kodin ja päivähoidon yhteistyötä", "Kodin ja päivähoidon yhteistyö", "Päivähoito",
        "Päivähoidon asiakasrekisteri", "Huoltaja", luoTyypit(Constants.KEHITYSASIAtype_KYSELY),
        luoclassification(Constants.classification_PAIVITTAISET_TOIMINNOT, Constants.classification_KOTI)));

    type.addEntryType(luoVapaaTekstientryType(
        "Minkälaiset asiat teille on kasvatuksessa tärkeitä? mitä toivotte meiltä",
        "Kuvaa kodin ja päivähoidon yhteistyötä", "Kodin ja päivähoidon yhteistyö", "Päivähoito",
        "Päivähoidon asiakasrekisteri", "Huoltaja", luoTyypit(Constants.KEHITYSASIAtype_KYSELY),
        luoclassification(Constants.classification_PAIVITTAISET_TOIMINNOT, Constants.classification_KOTI)));

    type.addEntryType(luoVapaaTekstientryType("Yhteiset tavoitteet liittyen yhteistyöhön",
        "Kirjataan yhteiset tavoitteet liittyen yhteistyöhön", "", "Päivähoito", "Päivähoidon asiakasrekisteri",
        "Päivähoito", luoTyypit(Constants.KEHITYSASIAtype_KESKUSTELU), luoclassification(Constants.classification_KUMPPANUUS)));

    type.addEntryType(luoMonivalueinenVapaaTekstientryType("Kommentit, havainnot ja tavoitteet",
        "Kirjatut kommentit, havainnot ja tavoitteet", "", "Päivähoito", "Päivähoidon asiakasrekisteri", "Päivähoito",
        "kommentti, havainto tai tavoite", luoTyypit(Constants.KEHITYSASIAtype_KESKUSTELU),
        luoclassification(Constants.classification_KOMMENTTI, Constants.classification_TAVOITE, Constants.classification_HAVAINTO)));

    Date nyt = new Date();
    Date loppuu = new Date();
    loppuu.setYear(loppuu.getYear() + 1);

    CollectionState tila = new CollectionState(State.ACTIVE);
    KKSCollection k = new KKSCollection("" + codeGenerator, nimi, type.getDescription(), tila, new Date(), 1, type);
    luoTyhjatKirjaukset(k);
    return k;
  }

  public static KKSCollection luo4VuotisTarkastus(String nimi) {
    CollectionType type = luoNelivuotisTarkastustype();

    type.addEntryGroup(new Group(new GroupKey(1, "Huoltaja"), "potilasrekisteri"));
    type.addEntryGroup(new Group(new GroupKey(2, "Päivähoito"), "päivähoidon asiakasrekisteri"));
    type.addEntryGroup(new Group(new GroupKey(3, "Neuvola"), "potilasrekisteri"));

    /********************************/

    type.addEntryType(luoVapaaTekstientryType("Mikä 4 – vuotiaassanne on parasta ja missä asioissa hän on hyvä?",
        "Kuvaa lapsen vahvuuksia", "", "neuvola", "potilasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_KYSELY), luoclassification(Constants.classification_VANHEMPIEN_valueSTAMAT_ASIAT)));

    type.addEntryType(luoVapaaTekstientryType("Mitä asioita teette yhdessä perheen kanssa?",
        "Kuvaa perheen valueja", "", "neuvola", "potilasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_KYSELY),
        luoclassification(Constants.classification_VANHEMPIEN_KASVATUKSELLISET_TAVOITTEET)));

    type.addEntryType(luoVapaaTekstientryType(
        "Mitä teette yhdessä 4-vuotiaanne kanssa ja mitkä tilanteet tuntuvat mukavilta?", "Kuvaa perheen valueja", "",
        "neuvola", "potilasrekisteri", "Huoltaja", luoTyypit(Constants.KEHITYSASIAtype_KYSELY),
        luoclassification(Constants.classification_VANHEMPIEN_valueSTAMAT_ASIAT)));

    type.addEntryType(luoVapaaTekstientryType(
        "Miten perheessänne arki sujuu ja miten käsittelette ristiriitatilanteita?",
        "Kuvaa perheen kasvatuksellisia keinoja", "", "neuvola", "potilasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_KYSELY),
        luoclassification(Constants.classification_VANHEMPIEN_KASVATUKSELLISET_TAVOITTEET, Constants.classification_HUOLENAIHEET)));

    type.addEntryType(luoVapaaTekstientryType(
        "Onko perheessänne tapahtunut viime aikoina jotain erityistä, mistä haluatte kertoa?",
        "Kuvaa perheen nykytilannetta/muutosta", "", "neuvola", "potilasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_KYSELY), luoclassification(Constants.classification_HUOLENAIHEET)));

    type.addEntryType(luoVapaaTekstientryType(
        "Oletteko huolissanne jostain lapsenne käyttäytymiseen tai kehitykseen liittyvästä asiasta?",
        "Kuvaa huolenaiheita", "", "neuvola", "potilasrekisteri", "Huoltaja",
        luoTyypit(Constants.KEHITYSASIAtype_KYSELY),
        luoclassification(Constants.classification_VANHEMPIEN_KASVATUKSELLISET_TAVOITTEET, Constants.classification_HUOLENAIHEET)));

    type.addEntryType(luoVapaaTekstientryType(
        "Onko lapsenne päivähoidossa tai kerhossa ja miten ryhmässä toimiminen sujuu?", "Kuvaa lasta ryhmässä", "",
        "neuvola", "potilasrekisteri", "Huoltaja", luoTyypit(Constants.KEHITYSASIAtype_KYSELY),
        luoclassification(Constants.classification_HUOLENAIHEET)));

    type.addEntryType(luoVapaaTekstientryType("Haluan keskustella lisäksi", "Kuvaa keskustelutarpeet", "",
        "neuvola", "potilasrekisteri", "Huoltaja", luoTyypit(Constants.KEHITYSASIAtype_TOIVE),
        luoclassification(Constants.classification_HUOLENAIHEET)));

    /***************************************/
    type.addEntryType(luoValintaentryType(
        "Pukee ja riisuu itse",
        "kuvaa päivittäisactionjen sujumista",
        "Päivittäistoiminnot",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luovaluet("kyllä", "opettelee", "ei vielä"),
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_MOTORIIKKA, Constants.classification_KEHON_HALLINTA,
            Constants.classification_PAIVITTAISET_TOIMINNOT)));

    type.addEntryType(luoValintaentryType(
        "Syö siististi",
        "kuvaa päivittäisactionjen sujumista",
        "Päivittäistoiminnot",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luovaluet("kyllä", "opettelee", "ei vielä"),
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_MOTORIIKKA, Constants.classification_KEHON_HALLINTA,
            Constants.classification_PAIVITTAISET_TOIMINNOT)));

    type.addEntryType(luoValintaentryType(
        "Käy omatoimisesti wc:ssä",
        "kuvaa päivittäisactionjen sujumista",
        "Päivittäistoiminnot",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luovaluet("kyllä", "opettelee", "ei vielä"),
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_MOTORIIKKA, Constants.classification_KEHON_HALLINTA,
            Constants.classification_PAIVITTAISET_TOIMINNOT)));

    type.addEntryType(luoValintaentryType(
        "Hahmottaa hoitopaikan hoitorytmin",
        "kuvaa päivittäisactionjen sujumista",
        "Päivittäistoiminnot",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luovaluet("kyllä", "opettelee", "ei vielä"),
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_TARKKAAVAISUUS, Constants.classification_PSYYKKINEN_KEHITYS,
            Constants.classification_PAIVITTAISET_TOIMINNOT)));

    type.addEntryType(luoValintaentryType(
        "Siirtyminen toiminnasta toiseen onnistuu",
        "kuvaa päivittäisactionjen sujumista",
        "Päivittäistoiminnot",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luovaluet("kyllä", "opettelee", "ei vielä"),
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_TARKKAAVAISUUS, Constants.classification_PSYYKKINEN_KEHITYS,
            Constants.classification_PAIVITTAISET_TOIMINNOT)));

    type.addEntryType(luoValintaentryType("Päivälepo", "kuvaa päivittäisactionjen sujumista",
        "Päivittäistoiminnot", "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito",
        luovaluet("Nukkuu päivittäin", "Satunnaisesti", "Ei nuku päiväunia"),
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO), luoclassification(Constants.classification_PAIVITTAISET_TOIMINNOT)));

    type.addEntryType(luoValintaentryType(
        "Juoksee sujuvasti",
        "kuvaa fyysistä kehitystä",
        "Liikkuminen ja hahmottaminen",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luovaluet("kyllä", "vaihtelevasti", "ei vielä"),
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_FYYSINEN_KEHITYS, Constants.classification_KEHON_HALLINTA,
            Constants.classification_LIIKUNANLLISET_TAIDOT, Constants.classification_MOTORIIKKA_KARKEA)));

    type.addEntryType(luoValintaentryType(
        "Kävelee kapealla penkillä / narua pitkin",
        "kuvaa fyysistä kehitystä",
        "Liikkuminen ja hahmottaminen",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luovaluet("kyllä", "vaihtelevasti", "ei vielä"),
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_FYYSINEN_KEHITYS, Constants.classification_KEHON_HALLINTA,
            Constants.classification_LIIKUNANLLISET_TAIDOT, Constants.classification_MOTORIIKKA_KARKEA)));

    type.addEntryType(luoValintaentryType(
        "Hyppää tasajalkahyppyä",
        "kuvaa fyysistä kehitystä",
        "Liikkuminen ja hahmottaminen",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luovaluet("kyllä", "vaihtelevasti", "ei vielä"),
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_FYYSINEN_KEHITYS, Constants.classification_KEHON_HALLINTA,
            Constants.classification_LIIKUNANLLISET_TAIDOT, Constants.classification_MOTORIIKKA_KARKEA)));

    type.addEntryType(luoValintaentryType(
        "Kävelee portaita ylös ja alas vuorotahtiin",
        "kuvaa fyysistä kehitystä",
        "Liikkuminen ja hahmottaminen",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luovaluet("kyllä", "vaihtelevasti", "ei vielä"),
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_FYYSINEN_KEHITYS, Constants.classification_KEHON_HALLINTA,
            Constants.classification_LIIKUNANLLISET_TAIDOT, Constants.classification_MOTORIIKKA_KARKEA)));

    type.addEntryType(luoValintaentryType(
        "Kiipeilee",
        "kuvaa fyysistä kehitystä",
        "Liikkuminen ja hahmottaminen",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luovaluet("kyllä", "vaihtelevasti", "ei vielä"),
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_FYYSINEN_KEHITYS, Constants.classification_KEHON_HALLINTA,
            Constants.classification_LIIKUNANLLISET_TAIDOT, Constants.classification_MOTORIIKKA_KARKEA)));

    type.addEntryType(luoValintaentryType("Tunnistaa ja nimeää kehonosia", "kuvaa psyykkistä kehitystä",
        "Liikkuminen ja hahmottaminen", "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito",
        luovaluet("kyllä", "vaihtelevasti", "ei vielä"), luoTyypit(Constants.KEHITYSASIAtype_HAVAINTO),
        luoclassification(Constants.classification_PSYYKKINEN_KEHITYS)));

    type.addEntryType(luoValintaentryType("Piirtää tunnistettavia asioita", "kuvaa psyykkistä kehitystä",
        "Liikkuminen ja hahmottaminen", "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito",
        luovaluet("kyllä", "vaihtelevasti", "ei vielä"), luoTyypit(Constants.KEHITYSASIAtype_HAVAINTO),
        luoclassification(Constants.classification_PSYYKKINEN_KEHITYS)));

    type.addEntryType(luoValintaentryType("Leikkaa saksilla", "kuvaa fyysistä ja psyykkistä kehitystä",
        "Liikkuminen ja hahmottaminen", "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito",
        luovaluet("kyllä", "vaihtelevasti", "ei vielä"), luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_PSYYKKINEN_KEHITYS, Constants.classification_MOTORIIKKA_HIENO)));

    type.addEntryType(luoValintaentryType("Kokoaa palapelejä", "kuvaa psyykkistä kehitystä",
        "Liikkuminen ja hahmottaminen", "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito",
        luovaluet("n.12", "n.20", ">20"), luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_PSYYKKINEN_KEHITYS, Constants.classification_MOTORIIKKA_HIENO)));

    type.addEntryType(luoValintaentryType("Kätisyys", "kuvaa fyysistä ja psyykkistä kehitystä",
        "Liikkuminen ja hahmottaminen", "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito",
        luovaluet("Oikea", "Vasen", "Vaihtaen"), luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_PSYYKKINEN_KEHITYS)));

    type.addEntryType(luoValintaentryType(
        "Ottaa ja säilyttää katsekontaktin vuorovaikutuksessa",
        "kuvaa vuorovaikutuksen ja kielen kehitystä",
        "Kieli ja kommunikaatio",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luovaluet("Yleensä", "Vaihtelevasti", "Ei vielä"),
        luoTyypit(Constants.KEHITYSASIAtype_HAVAINTO),
        luoclassification(Constants.classification_PSYYKKINEN_KEHITYS, Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU,
            Constants.classification_KIELI_JA_KOMMUNIKAATIO)));

    type.addEntryType(luoValintaentryType(
        "Osaa kuunnella vastavuoroisesti",
        "kuvaa vuorovaikutuksen ja kielen kehitystä",
        "Kieli ja kommunikaatio",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luovaluet("Yleensä", "Vaihtelevasti", "Ei vielä"),
        luoTyypit(Constants.KEHITYSASIAtype_HAVAINTO),
        luoclassification(Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU, Constants.classification_PSYYKKINEN_KEHITYS,
            Constants.classification_KIELI_JA_KOMMUNIKAATIO)));

    type.addEntryType(luoValintaentryType("Toimii ohjeiden mukaan", "kuvaa vuorovaikutuksen ja kielen kehitystä",
        "Kieli ja kommunikaatio", "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito",
        luovaluet("Yleensä", "Vaihtelevasti", "Ei vielä"), luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_KIELI_JA_KOMMUNIKAATIO, Constants.classification_PSYYKKINEN_KEHITYS)));

    type.addEntryType(luoValintaentryType(
        "On kiinnostunut saduista ja kertomuksista",
        "kuvaa vuorovaikutuksen ja kielen kehitystä",
        "Kieli ja kommunikaatio",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luovaluet("Yleensä", "Vaihtelevasti", "Ei vielä"),
        luoTyypit(Constants.KEHITYSASIAtype_HAVAINTO),
        luoclassification(Constants.classification_KIELI_JA_KOMMUNIKAATIO, Constants.classification_PSYYKKINEN_KEHITYS,
            Constants.classification_TARKKAAVAISUUS)));

    type.addEntryType(luoValintaentryType(
        "Puhe on selkeää ja ymmärrettävää",
        "kuvaa kielen kehitystä",
        "Kieli ja kommunikaatio",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luovaluet("Yleensä", "Vaihtelevasti", "Ei vielä"),
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_KIELI_JA_KOMMUNIKAATIO, Constants.classification_PSYYKKINEN_KEHITYS,
            Constants.classification_MOTORIIKKA_HIENO)));

    type.addEntryType(luoVapaaTekstientryType(
        "Huomioita puheesta",
        "esim. äänivirheet, änkytys, käheys, jne",
        "Kieli ja kommunikaatio",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_KIELI_JA_KOMMUNIKAATIO, Constants.classification_PSYYKKINEN_KEHITYS,
            Constants.classification_MOTORIIKKA_HIENO)));

    type.addEntryType(luoValintaentryType("Ymmärtää lukumäärien 1-4 vastaavuuden",
        "kuvaa matemaattisten taitojen kehitystä", "Kieli ja kommunikaatio", "päivähoito",
        "päivähoidon asiakasrekisteri", "Päivähoito", luovaluet("Yleensä", "Vaihtelevasti", "Ei vielä"),
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO), luoclassification(Constants.classification_MATEMAATTISET_TAIDOT)));

    type.addEntryType(luoValintaentryType("Tunnistaa ja nimeää perusvärit", "kuvaa kielen kehitystä",
        "Kieli ja kommunikaatio", "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito",
        luovaluet("Yleensä", "Vaihtelevasti", "Ei vielä"), luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_KIELI_JA_KOMMUNIKAATIO)));

    type.addEntryType(luoValintaentryType(
        "Osaa kertoa pieniä tarinoita / osaa kertoa tapahtuneista asioista",
        "kuvaa kielen kehitystä",
        "Kieli ja kommunikaatio",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luovaluet("Yleensä", "Vaihtelevasti", "Ei vielä"),
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_KIELI_JA_KOMMUNIKAATIO, Constants.classification_TARKKAAVAISUUS,
            Constants.classification_KESKITTYMINEN_JA_MUISTI, Constants.classification_PSYYKKINEN_KEHITYS)));

    type.addEntryType(luoVapaaTekstientryType(
        "Vahvuudet ja vaikeudet leikeissä",
        "kuvaa vuorovaikutuksen ja kielen kehitystä",
        "",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoTyypit(Constants.KEHITYSASIAtype_HAVAINTO),
        luoclassification(Constants.classification_HUOLENAIHEET, Constants.classification_VAHVUUDET_JA_KIINNOSTUKSET,
            Constants.classification_LEIKKI, Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU)));

    type.addEntryType(luoVapaaTekstientryType(
        "Vahvuudet ja vaikeudet sosiaalisissa tilanteissa",
        "kuvaa vuorovaikutuksen ja kielen kehitystä",
        "",
        "päivähoito",
        "päivähoidon asiakasrekisteri",
        "Päivähoito",
        luoTyypit(Constants.KEHITYSASIAtype_HAVAINTO),
        luoclassification(Constants.classification_HUOLENAIHEET, Constants.classification_VAHVUUDET_JA_KIINNOSTUKSET,
            Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU)));

    type.addEntryType(luoVapaaTekstientryType("Terveiset neuvolaan", "kerro terveiset neuvolaan", "",
        "päivähoito", "päivähoidon asiakasrekisteri", "Päivähoito", luoTyypit(Constants.KEHITYSASIAtype_PALAUTE),
        luoclassification(Constants.classification_KOTI)));

    /********************************/

    type.addEntryType(luoVapaaTekstientryType("Yleistä neuvolakäynnin sujumisesta",
        "kuvaa neuvolakäynnin sujumisen", "", "neuvola", "potilasrekisteri", "Neuvola",
        luoTyypit(Constants.KEHITYSASIAtype_HAVAINTO), luoclassification(Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU)));

    type.addEntryType(luoVapaaTekstientryType(
        "Onko huolenaiheita ja miten mahdolliset huolenaiheet on huomioitu",
        "mm. pelot, arkuus, sosiaaliset taidot, puhe, motoriikka, siisteyskasvatus, perhetilanne, kasvatuskysymykset",
        "",
        "neuvola",
        "potilasrekisteri",
        "Neuvola",
        luoTyypit(Constants.KEHITYSASIAtype_ARVIO),
        luoclassification(Constants.classification_SOSIAALISUUS_JA_TUNNEILMAISU, Constants.classification_HUOLENAIHEET,
            Constants.classification_TERVEYDENTILA, Constants.classification_PSYYKKINEN_KEHITYS,
            Constants.classification_FYYSINEN_KEHITYS, Constants.classification_KIELI_JA_KOMMUNIKAATIO)));

    type.addEntryType(luoVapaaTekstientryType("Neuvola ohjannut jatkoselvityksiin", "lähetteet", "", "neuvola",
        "potilasrekisteri", "Neuvola", luoTyypit(Constants.KEHITYSASIAtype_LAHETE),
        luoclassification(Constants.classification_TERVEYDENTILA)));

    type.addEntryType(luoVapaaTekstientryType("Terveiset päivähoitoon", "neuvolan terveiset päivähoidolle", "",
        "neuvola", "potilasrekisteri", "Neuvola", luoTyypit(Constants.KEHITYSASIAtype_PALAUTE),
        luoclassification(Constants.classification_TERVEYDENTILA)));

    type.addEntryType(luoVapaaTekstientryType("Terveiset päivähoitoon", "neuvolan terveiset päivähoidolle", "",
        "neuvola", "potilasrekisteri", "Neuvola", luoTyypit(Constants.KEHITYSASIAtype_PALAUTE),
        luoclassification(Constants.classification_TERVEYDENTILA)));

    type.addEntryType(luoTekstientryType("Päänympärys", "lapsen päänympärys sentteinä", "Mittaukset", "neuvola",
        "potilasrekisteri", "Neuvola", luoTyypit(Constants.KEHITYSASIAtype_KAYNTI),
        luoclassification(Constants.classification_MITTAUS)));

    type.addEntryType(luoTekstientryType("Pituus", "lapsen pituus sentteinä", "Mittaukset", "neuvola",
        "potilasrekisteri", "Neuvola", luoTyypit(Constants.KEHITYSASIAtype_KAYNTI),
        luoclassification(Constants.classification_MITTAUS)));

    type.addEntryType(luoTekstientryType("Paino", "lapsen paino kiloina", "Mittaukset", "neuvola",
        "potilasrekisteri", "Neuvola", luoTyypit(Constants.KEHITYSASIAtype_KAYNTI),
        luoclassification(Constants.classification_MITTAUS)));

    type.addEntryType(luoMonivalueinenVapaaTekstientryType("Kommentit, havainnot ja tavoitteet",
        "Kirjatut kommentit, havainnot ja tavoitteet", "", "neuvola", "potilasrekisteri", "Neuvola",
        "kommentti, havainto tai tavoite", luoTyypit(Constants.KEHITYSASIAtype_KESKUSTELU),
        luoclassification(Constants.classification_KOMMENTTI, Constants.classification_TAVOITE, Constants.classification_HAVAINTO)));

    Date nyt = new Date();
    Date loppuu = new Date();
    loppuu.setYear(loppuu.getYear() + 1);

    CollectionState tila = new CollectionState(State.ACTIVE);
    KKSCollection k = new KKSCollection("" + codeGenerator, nimi, type.getDescription(), tila, new Date(), 1, type);
    luoTyhjatKirjaukset(k);
    return k;
  }

  public static List<String> luovaluet(String... joukko) {
    return Arrays.asList(joukko);
  }

  public static List<Classification> luoclassification(String... classifications) {
    List<Classification> l = new ArrayList<Classification>();

    for (String s : classifications) {
      codeGenerator++;
      l.add(new Classification(codeGenerator, s, ""));
    }
    return l;
  }

  public static List<Classification> luoTyypit(String... tyypit) {
    List<Classification> l = new ArrayList<Classification>();

    for (String s : tyypit) {
      codeGenerator++;
      l.add(new Classification(codeGenerator, s, ""));
    }
    return l;
  }

  public static EntryType luoMonivalintaentryType(String kentta, String classification, String ryhma, String vastuutaho,
      String rekisteri, String tayttaja, List<String> valueJoukko, List<Classification> tyypit,
      List<Classification> classifications) {
    codeGenerator++;

    EntryType type = new EntryType("" + codeGenerator, kentta, classification, false, DataType.MULTI_SELECT, valueJoukko,
        vastuutaho, tayttaja, rekisteri, ryhma);

    type.setClassifications(classifications);
    type.setDevelopmentTypes(tyypit);
    return type;
  }

  public static EntryType luoValintaentryType(String kentta, String classification, String ryhma, String vastuutaho,
      String rekisteri, String tayttaja, List<String> valueJoukko, List<Classification> tyypit,
      List<Classification> classifications) {
    codeGenerator++;

    EntryType type = new EntryType("" + codeGenerator, kentta, classification, false, DataType.SELECT, valueJoukko,
        vastuutaho, tayttaja, rekisteri, ryhma);

    type.setClassifications(classifications);
    type.setDevelopmentTypes(tyypit);
    return type;
  }

  public static EntryType luoVapaaTekstientryType(String kentta, String classification, String ryhma, String vastuutaho,
      String rekisteri, String tayttaja, List<Classification> tyypit, List<Classification> classifications) {
    codeGenerator++;

    EntryType type = new EntryType("" + codeGenerator, kentta, classification, false, DataType.FREE_TEXT, null, vastuutaho,
        tayttaja, rekisteri, ryhma);

    type.setClassifications(classifications);
    type.setDevelopmentTypes(tyypit);
    return type;
  }

  public static EntryType luoMonivalueinenVapaaTekstientryType(String kentta, String classification, String ryhma,
      String vastuutaho, String rekisteri, String tayttaja, String luontiMuoto, List<Classification> tyypit,
      List<Classification> classifications) {
    codeGenerator++;

    EntryType type = new EntryType("" + codeGenerator, kentta, classification, true, DataType.FREE_TEXT, null, vastuutaho,
        tayttaja, rekisteri, ryhma);

    type.setClassifications(classifications);
    type.setDevelopmentTypes(tyypit);
    type.setCreationDesc(luontiMuoto);
    return type;
  }

  public static EntryType luoTekstientryType(String kentta, String classification, String ryhma, String vastuutaho,
      String rekisteri, String tayttaja, List<Classification> tyypit, List<Classification> classifications) {
    codeGenerator++;

    EntryType type = new EntryType("" + codeGenerator, kentta, classification, false, DataType.TEXT, null, vastuutaho,
        tayttaja, rekisteri, ryhma);

    type.setClassifications(classifications);
    type.setDevelopmentTypes(tyypit);
    return type;
  }

  public static void luoTyhjatKirjaukset(KKSCollection k) {
    for (EntryType t : k.getType().getEntryTypes()) {
      k.addEntry(luoentry(t, ""));
    }
  }

  public static Entry luoentry(EntryType t, String entry) {
    Entry k = new Entry(entry, new Date(), 1, t.getRegister(), "Kaisa Kirjaaja", t);
    return k;
  }
}
