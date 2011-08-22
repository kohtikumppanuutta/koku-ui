package fi.koku.kks.ui.common.utils;

public class Constants {

  private Constants() {

  }

  public static final String KEHITYSASIATYYPPI_KESKUSTELU = "keskustelu";
  public static final String KEHITYSASIATYYPPI_ARVIO = "arvio";
  public static final String KEHITYSASIATYYPPI_HAVAINTO = "havainto";
  public static final String KEHITYSASIATYYPPI_KYSELY = "kysely";
  public static final String KEHITYSASIATYYPPI_PALAUTE = "palaute";
  public static final String KEHITYSASIATYYPPI_LAHETE = "lähete";
  public static final String KEHITYSASIATYYPPI_TOIVE = "toive";
  public static final String KEHITYSASIATYYPPI_KAYNTI = "kaynti";

  public static final String LUOKITUS_KUMPPANUUS = "kumppanuus";

  public static final String LUOKITUS_FYYSINEN_KEHITYS = "fyysinen_kehitys";
  public static final String LUOKITUS_MOTORIIKKA = "motoriikka";
  public static final String LUOKITUS_MOTORIIKKA_KARKEA = "motoriikka.karkea";
  public static final String LUOKITUS_MOTORIIKKA_HIENO = "motoriikka.hieno";
  public static final String LUOKITUS_KEHON_HALLINTA = "kehon_hallinta";
  public static final String LUOKITUS_KIELI_JA_KOMMUNIKAATIO = "kieli_ja_kommunikaatio";
  public static final String LUOKITUS_LAPSEN_TOIVEET = "lapsen_toiveet";
  public static final String LUOKITUS_VAHVUUDET_JA_KIINNOSTUKSET = "vahvuudet_ja_kiinnostukset";
  public static final String LUOKITUS_LEIKKI = "leikki";
  public static final String LUOKITUS_LIIKUNANLLISET_TAIDOT = "liikunnalliset_taidot";
  public static final String LUOKITUS_MATEMAATTISET_TAIDOT = "matemaattiset_taidot";
  public static final String LUOKITUS_MOTORISTISET_TAIDOT = "motoriset_taidot";
  public static final String LUOKITUS_PSYYKKINEN_KEHITYS = "psyykkinen_kehitys";
  public static final String LUOKITUS_PAIVITTAISET_TOIMINNOT = "päivittäiset_toiminnot";
  public static final String LUOKITUS_RAKENTELU = "rakentelu";
  public static final String LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU = "sosiaalisuus_ja_tunneilmaisu";
  public static final String LUOKITUS_TARKKAAVAISUUS = "tarkkaavaisuus";
  public static final String LUOKITUS_KESKITTYMINEN_JA_MUISTI = "keskittyminen_ja_muisti";
  public static final String LUOKITUS_ULKOILU = "ulkoilu";
  public static final String LUOKITUS_TUEN_TARVE = "tuen_tarve";
  public static final String LUOKITUS_HUOLENAIHEET = "huolenaiheet";

  public static final String LUOKITUS_TERVEYDENTILA = "terveydentila";
  public static final String LUOKITUS_TERVEYDENTIILA_RUOKAVALIO = "terveydentila.ruokavalio";
  public static final String LUOKITUS_TERVEYDENTILA_RUOKAVALIO_PERUSTE = "terveydentila.ruokavalio.peruste";

  public static final String LUOKITUS_TERVEYDENTILA_SAIRAUS = "terveydentila.sairaus";
  public static final String LUOKITUS_TERVEYDENTILA_SAIRAUS_ALLERGIA = "terveydentila.sairaus.allergia";
  public static final String LUOKITUS_TERVEYDENTILA_SAIRAUS_ALLERGIA_ELAIN = "terveydentila.sairaus.allergia.eläin";
  public static final String LUOKITUS_TERVEYDENTILA_SAIRAUS_ALLERGIA_RUOKA = "terveydentila.sairaus.allergia.ruoka";

  public static final String LUOKITUS_TERVEYDENTILA_LAAKEHOITO = "terveydentila.lääkehoito";

  public static final String LUOKITUS_KOTI = "koti";
  public static final String LUOKITUS_VANHEMPIEN_valueSTAMAT_ASIAT = "koti.vanhempien_valuestamat_asiat";
  public static final String LUOKITUS_VANHEMPIEN_KASVATUKSELLISET_TAVOITTEET = "koti.vanhempien_kasvatukselliset_tavoitteet";

  public static final String LUOKITUS_MITTAUS = "mittaus";

  public static final String LUOKITUS_HAVAINTO = "havainto";
  public static final String LUOKITUS_KOMMENTTI = "kommentti";
  public static final String LUOKITUS_TAVOITE = "tavoite";

  public static String developmentIssues() {
    StringBuffer sb = new StringBuffer();

    sb.append(KEHITYSASIATYYPPI_KESKUSTELU + "</br>");
    sb.append(KEHITYSASIATYYPPI_ARVIO + "</br>");
    sb.append(KEHITYSASIATYYPPI_HAVAINTO + "</br>");
    sb.append(KEHITYSASIATYYPPI_KYSELY + "</br>");
    sb.append(KEHITYSASIATYYPPI_PALAUTE + "</br>");
    sb.append(KEHITYSASIATYYPPI_LAHETE + "</br>");
    sb.append(KEHITYSASIATYYPPI_TOIVE + "</br>");
    sb.append(KEHITYSASIATYYPPI_KAYNTI + "</br>");

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
    sb.append(LUOKITUS_VANHEMPIEN_valueSTAMAT_ASIAT + "</br>");
    sb.append(LUOKITUS_VANHEMPIEN_KASVATUKSELLISET_TAVOITTEET + "</br>");
    sb.append(LUOKITUS_HAVAINTO + "</br>");
    sb.append(LUOKITUS_KOMMENTTI + "</br>");
    sb.append(LUOKITUS_TAVOITE + "</br>");
    return sb.toString();
  }
}
