package com.ixonos.koku.kks.utils;

/**
 * Sisältää demossa käytettävät luokittelut ja kehitysasiatyypit
 * 
 * @author tuomape
 * 
 */
public class Vakiot {

  public final static String KEHITYSASIATYYPPI_KESKUSTELU = "keskustelu";
  public final static String KEHITYSASIATYYPPI_ARVIO = "arvio";
  public final static String KEHITYSASIATYYPPI_HAVAINTO = "havainto";
  public final static String KEHITYSASIATYYPPI_KYSELY = "kysely";
  public final static String KEHITYSASIATYYPPI_PALAUTE = "palaute";
  public final static String KEHITYSASIATYYPPI_LAHETE = "lähete";
  public final static String KEHITYSASIATYYPPI_TOIVE = "toive";
  public final static String KEHITYSASIATYYPPI_KAYNTI = "kaynti";

  public final static String LUOKITUS_KUMPPANUUS = "kumppanuus";

  public final static String LUOKITUS_FYYSINEN_KEHITYS = "fyysinen_kehitys";
  public final static String LUOKITUS_MOTORIIKKA = "motoriikka";
  public final static String LUOKITUS_MOTORIIKKA_KARKEA = "motoriikka.karkea";
  public final static String LUOKITUS_MOTORIIKKA_HIENO = "motoriikka.hieno";
  public final static String LUOKITUS_KEHON_HALLINTA = "kehon_hallinta";
  public final static String LUOKITUS_KIELI_JA_KOMMUNIKAATIO = "kieli_ja_kommunikaatio";
  public final static String LUOKITUS_LAPSEN_TOIVEET = "lapsen_toiveet";
  public final static String LUOKITUS_VAHVUUDET_JA_KIINNOSTUKSET = "vahvuudet_ja_kiinnostukset";
  public final static String LUOKITUS_LEIKKI = "leikki";
  public final static String LUOKITUS_LIIKUNANLLISET_TAIDOT = "liikunnalliset_taidot";
  public final static String LUOKITUS_MATEMAATTISET_TAIDOT = "matemaattiset_taidot";
  public final static String LUOKITUS_MOTORISTISET_TAIDOT = "motoriset_taidot";
  public final static String LUOKITUS_PSYYKKINEN_KEHITYS = "psyykkinen_kehitys";
  public final static String LUOKITUS_PAIVITTAISET_TOIMINNOT = "päivittäiset_toiminnot";
  public final static String LUOKITUS_RAKENTELU = "rakentelu";
  public final static String LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU = "sosiaalisuus_ja_tunneilmaisu";
  public final static String LUOKITUS_TARKKAAVAISUUS = "tarkkaavaisuus";
  public final static String LUOKITUS_KESKITTYMINEN_JA_MUISTI = "keskittyminen_ja_muisti";
  public final static String LUOKITUS_ULKOILU = "ulkoilu";
  public final static String LUOKITUS_TUEN_TARVE = "tuen_tarve";
  public final static String LUOKITUS_HUOLENAIHEET = "huolenaiheet";

  public final static String LUOKITUS_TERVEYDENTILA = "terveydentila";
  public final static String LUOKITUS_TERVEYDENTIILA_RUOKAVALIO = "terveydentila.ruokavalio";
  public final static String LUOKITUS_TERVEYDENTILA_RUOKAVALIO_PERUSTE = "terveydentila.ruokavalio.peruste";

  public final static String LUOKITUS_TERVEYDENTILA_SAIRAUS = "terveydentila.sairaus";
  public final static String LUOKITUS_TERVEYDENTILA_SAIRAUS_ALLERGIA = "terveydentila.sairaus.allergia";
  public final static String LUOKITUS_TERVEYDENTILA_SAIRAUS_ALLERGIA_ELAIN = "terveydentila.sairaus.allergia.eläin";
  public final static String LUOKITUS_TERVEYDENTILA_SAIRAUS_ALLERGIA_RUOKA = "terveydentila.sairaus.allergia.ruoka";

  public final static String LUOKITUS_TERVEYDENTILA_LAAKEHOITO = "terveydentila.lääkehoito";

  public final static String LUOKITUS_KOTI = "koti";
  public final static String LUOKITUS_VANHEMPIEN_ARVOSTAMAT_ASIAT = "koti.vanhempien_arvostamat_asiat";
  public final static String LUOKITUS_VANHEMPIEN_KASVATUKSELLISET_TAVOITTEET = "koti.vanhempien_kasvatukselliset_tavoitteet";

  public final static String LUOKITUS_MITTAUS = "mittaus";

  public final static String LUOKITUS_HAVAINTO = "havainto";
  public final static String LUOKITUS_KOMMENTTI = "kommentti";
  public final static String LUOKITUS_TAVOITE = "tavoite";

  public static String kehitysasiaLajit() {
    StringBuffer sb = new StringBuffer();

    sb.append(KEHITYSASIATYYPPI_KESKUSTELU + "</br>");
    sb.append(KEHITYSASIATYYPPI_ARVIO + "</br>");
    sb.append(KEHITYSASIATYYPPI_HAVAINTO + "</br>");
    sb.append(KEHITYSASIATYYPPI_KYSELY + "</br>");
    sb.append(KEHITYSASIATYYPPI_PALAUTE + "</br>");
    sb.append(KEHITYSASIATYYPPI_LAHETE + "</br>");
    sb.append(KEHITYSASIATYYPPI_TOIVE + "</br>");

    return sb.toString();
  }

  public static String luokittelut() {
    StringBuffer sb = new StringBuffer();

    sb.append(LUOKITUS_KUMPPANUUS);

    sb.append(LUOKITUS_FYYSINEN_KEHITYS + "</br>");
    sb.append(LUOKITUS_MOTORIIKKA + "</br>");
    sb.append(LUOKITUS_MOTORIIKKA_KARKEA + "</br>");
    sb.append(LUOKITUS_MOTORIIKKA_HIENO + "</br>");
    sb.append(LUOKITUS_KEHON_HALLINTA + "</br>");
    sb.append(LUOKITUS_KIELI_JA_KOMMUNIKAATIO + "</br>");
    sb.append(LUOKITUS_LAPSEN_TOIVEET + "</br>");
    sb.append(LUOKITUS_VAHVUUDET_JA_KIINNOSTUKSET + "</br>");
    sb.append(LUOKITUS_LEIKKI + "</br>");
    sb.append(LUOKITUS_LIIKUNANLLISET_TAIDOT + "</br>");
    sb.append(LUOKITUS_MATEMAATTISET_TAIDOT + "</br>");
    sb.append(LUOKITUS_MOTORISTISET_TAIDOT + "</br>");
    sb.append(LUOKITUS_PSYYKKINEN_KEHITYS + "</br>");
    sb.append(LUOKITUS_PAIVITTAISET_TOIMINNOT + "</br>");
    sb.append(LUOKITUS_RAKENTELU + "</br>");
    sb.append(LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU + "</br>");
    sb.append(LUOKITUS_TARKKAAVAISUUS + "</br>");
    sb.append(LUOKITUS_KESKITTYMINEN_JA_MUISTI + "</br>");
    sb.append(LUOKITUS_ULKOILU + "</br>");
    sb.append(LUOKITUS_TUEN_TARVE + "</br>");
    sb.append(LUOKITUS_HUOLENAIHEET + "</br>");

    sb.append(LUOKITUS_TERVEYDENTILA + "</br>");
    sb.append(LUOKITUS_TERVEYDENTIILA_RUOKAVALIO + "</br>");
    sb.append(LUOKITUS_TERVEYDENTILA_RUOKAVALIO_PERUSTE + "</br>");

    sb.append(LUOKITUS_TERVEYDENTILA_SAIRAUS + "</br>");
    sb.append(LUOKITUS_TERVEYDENTILA_SAIRAUS_ALLERGIA + "</br>");
    sb.append(LUOKITUS_TERVEYDENTILA_SAIRAUS_ALLERGIA_ELAIN + "</br>");
    sb.append(LUOKITUS_TERVEYDENTILA_SAIRAUS_ALLERGIA_RUOKA + "</br>");

    sb.append(LUOKITUS_TERVEYDENTILA_LAAKEHOITO + "</br>");

    sb.append(LUOKITUS_KOTI + "</br>");
    sb.append(LUOKITUS_VANHEMPIEN_ARVOSTAMAT_ASIAT + "</br>");
    sb.append(LUOKITUS_VANHEMPIEN_KASVATUKSELLISET_TAVOITTEET + "</br>");

    return sb.toString();
  }
}
