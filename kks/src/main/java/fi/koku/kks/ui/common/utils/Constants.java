package fi.koku.kks.ui.common.utils;

public final class Constants {

  private static final String LINE_BREAK = "</br>";

  private Constants() {

  }

  final public static String ENDPOINT = "http://localhost:8180";

  final public static String CUSTOMER_SERVICE_USER_ID = "marko";
  final public static String CUSTOMER_SERVICE_PASSWORD = "marko";

  final public static String COMMUNITY_SERVICE_USER_ID = "marko";
  final public static String COMMUNITY_SERVICE_PASSWORD = "marko";

  final public static String KKS_SERVICE_USER_ID = "marko";
  final public static String KKS_SERVICE_PASSWORD = "marko";

  final public static String COMMUNITY_TYPE_GUARDIAN_COMMUNITY = "guardian_community";

  final public static String ROLE_DEPENDANT = "dependant";
  final public static String ROLE_GUARDIAN = "guardian";

  final public static String COMPONENT_KKS = "KKS";

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
  public static final String LUOKITUS_VANHEMPIEN_ARVOSTAMAT_ASIAT = "koti.vanhempien_valuestamat_asiat";
  public static final String LUOKITUS_VANHEMPIEN_KASVATUKSELLISET_TAVOITTEET = "koti.vanhempien_kasvatukselliset_tavoitteet";

  public static final String LUOKITUS_MITTAUS = "mittaus";

  public static final String LUOKITUS_HAVAINTO = "havainto";
  public static final String LUOKITUS_KOMMENTTI = "kommentti";
  public static final String LUOKITUS_TAVOITE = "tavoite";

  public static String developmentIssues() {
    StringBuffer sb = new StringBuffer();

    sb.append(KEHITYSASIATYYPPI_KESKUSTELU + LINE_BREAK);
    sb.append(KEHITYSASIATYYPPI_ARVIO + LINE_BREAK);
    sb.append(KEHITYSASIATYYPPI_HAVAINTO + LINE_BREAK);
    sb.append(KEHITYSASIATYYPPI_KYSELY + LINE_BREAK);
    sb.append(KEHITYSASIATYYPPI_PALAUTE + LINE_BREAK);
    sb.append(KEHITYSASIATYYPPI_LAHETE + LINE_BREAK);
    sb.append(KEHITYSASIATYYPPI_TOIVE + LINE_BREAK);
    sb.append(KEHITYSASIATYYPPI_KAYNTI + LINE_BREAK);

    return sb.toString();
  }

  public static String luokittelut() {
    StringBuffer sb = new StringBuffer();

    sb.append(LUOKITUS_KUMPPANUUS);

    sb.append(LUOKITUS_FYYSINEN_KEHITYS + LINE_BREAK);
    sb.append(LUOKITUS_MOTORIIKKA + LINE_BREAK);
    sb.append(LUOKITUS_MOTORIIKKA_KARKEA + LINE_BREAK);
    sb.append(LUOKITUS_MOTORIIKKA_HIENO + LINE_BREAK);
    sb.append(LUOKITUS_KEHON_HALLINTA + LINE_BREAK);
    sb.append(LUOKITUS_KIELI_JA_KOMMUNIKAATIO + LINE_BREAK);
    sb.append(LUOKITUS_LAPSEN_TOIVEET + LINE_BREAK);
    sb.append(LUOKITUS_VAHVUUDET_JA_KIINNOSTUKSET + LINE_BREAK);
    sb.append(LUOKITUS_LEIKKI + LINE_BREAK);
    sb.append(LUOKITUS_LIIKUNANLLISET_TAIDOT + LINE_BREAK);
    sb.append(LUOKITUS_MATEMAATTISET_TAIDOT + LINE_BREAK);
    sb.append(LUOKITUS_MOTORISTISET_TAIDOT + LINE_BREAK);
    sb.append(LUOKITUS_PSYYKKINEN_KEHITYS + LINE_BREAK);
    sb.append(LUOKITUS_PAIVITTAISET_TOIMINNOT + LINE_BREAK);
    sb.append(LUOKITUS_RAKENTELU + LINE_BREAK);
    sb.append(LUOKITUS_SOSIAALISUUS_JA_TUNNEILMAISU + LINE_BREAK);
    sb.append(LUOKITUS_TARKKAAVAISUUS + LINE_BREAK);
    sb.append(LUOKITUS_KESKITTYMINEN_JA_MUISTI + LINE_BREAK);
    sb.append(LUOKITUS_ULKOILU + LINE_BREAK);
    sb.append(LUOKITUS_TUEN_TARVE + LINE_BREAK);
    sb.append(LUOKITUS_HUOLENAIHEET + LINE_BREAK);

    sb.append(LUOKITUS_TERVEYDENTILA + LINE_BREAK);
    sb.append(LUOKITUS_TERVEYDENTIILA_RUOKAVALIO + LINE_BREAK);
    sb.append(LUOKITUS_TERVEYDENTILA_RUOKAVALIO_PERUSTE + LINE_BREAK);

    sb.append(LUOKITUS_TERVEYDENTILA_SAIRAUS + LINE_BREAK);
    sb.append(LUOKITUS_TERVEYDENTILA_SAIRAUS_ALLERGIA + LINE_BREAK);
    sb.append(LUOKITUS_TERVEYDENTILA_SAIRAUS_ALLERGIA_ELAIN + LINE_BREAK);
    sb.append(LUOKITUS_TERVEYDENTILA_SAIRAUS_ALLERGIA_RUOKA + LINE_BREAK);

    sb.append(LUOKITUS_TERVEYDENTILA_LAAKEHOITO + LINE_BREAK);

    sb.append(LUOKITUS_KOTI + LINE_BREAK);
    sb.append(LUOKITUS_VANHEMPIEN_ARVOSTAMAT_ASIAT + LINE_BREAK);
    sb.append(LUOKITUS_VANHEMPIEN_KASVATUKSELLISET_TAVOITTEET + LINE_BREAK);
    sb.append(LUOKITUS_HAVAINTO + LINE_BREAK);
    sb.append(LUOKITUS_KOMMENTTI + LINE_BREAK);
    sb.append(LUOKITUS_TAVOITE + LINE_BREAK);
    return sb.toString();
  }
}
