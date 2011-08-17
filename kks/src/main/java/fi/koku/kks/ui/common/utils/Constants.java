package fi.koku.kks.ui.common.utils;

public class Constants {

  public final static String KEHITYSASIAtype_KESKUSTELU = "keskustelu";
  public final static String KEHITYSASIAtype_ARVIO = "arvio";
  public final static String KEHITYSASIAtype_HAVAINTO = "havainto";
  public final static String KEHITYSASIAtype_KYSELY = "kysely";
  public final static String KEHITYSASIAtype_PALAUTE = "palaute";
  public final static String KEHITYSASIAtype_LAHETE = "lähete";
  public final static String KEHITYSASIAtype_TOIVE = "toive";
  public final static String KEHITYSASIAtype_KAYNTI = "kaynti";

  public final static String classification_KUMPPANUUS = "kumppanuus";

  public final static String classification_FYYSINEN_KEHITYS = "fyysinen_kehitys";
  public final static String classification_MOTORIIKKA = "motoriikka";
  public final static String classification_MOTORIIKKA_KARKEA = "motoriikka.karkea";
  public final static String classification_MOTORIIKKA_HIENO = "motoriikka.hieno";
  public final static String classification_KEHON_HALLINTA = "kehon_hallinta";
  public final static String classification_KIELI_JA_KOMMUNIKAATIO = "kieli_ja_kommunikaatio";
  public final static String classification_LAPSEN_TOIVEET = "lapsen_toiveet";
  public final static String classification_VAHVUUDET_JA_KIINNOSTUKSET = "vahvuudet_ja_kiinnostukset";
  public final static String classification_LEIKKI = "leikki";
  public final static String classification_LIIKUNANLLISET_TAIDOT = "liikunnalliset_taidot";
  public final static String classification_MATEMAATTISET_TAIDOT = "matemaattiset_taidot";
  public final static String classification_MOTORISTISET_TAIDOT = "motoriset_taidot";
  public final static String classification_PSYYKKINEN_KEHITYS = "psyykkinen_kehitys";
  public final static String classification_PAIVITTAISET_TOIMINNOT = "päivittäiset_toiminnot";
  public final static String classification_RAKENTELU = "rakentelu";
  public final static String classification_SOSIAALISUUS_JA_TUNNEILMAISU = "sosiaalisuus_ja_tunneilmaisu";
  public final static String classification_TARKKAAVAISUUS = "tarkkaavaisuus";
  public final static String classification_KESKITTYMINEN_JA_MUISTI = "keskittyminen_ja_muisti";
  public final static String classification_ULKOILU = "ulkoilu";
  public final static String classification_TUEN_TARVE = "tuen_tarve";
  public final static String classification_HUOLENAIHEET = "huolenaiheet";

  public final static String classification_TERVEYDENTILA = "terveydentila";
  public final static String classification_TERVEYDENTIILA_RUOKAVALIO = "terveydentila.ruokavalio";
  public final static String classification_TERVEYDENTILA_RUOKAVALIO_PERUSTE = "terveydentila.ruokavalio.peruste";

  public final static String classification_TERVEYDENTILA_SAIRAUS = "terveydentila.sairaus";
  public final static String classification_TERVEYDENTILA_SAIRAUS_ALLERGIA = "terveydentila.sairaus.allergia";
  public final static String classification_TERVEYDENTILA_SAIRAUS_ALLERGIA_ELAIN = "terveydentila.sairaus.allergia.eläin";
  public final static String classification_TERVEYDENTILA_SAIRAUS_ALLERGIA_RUOKA = "terveydentila.sairaus.allergia.ruoka";

  public final static String classification_TERVEYDENTILA_LAAKEHOITO = "terveydentila.lääkehoito";

  public final static String classification_KOTI = "koti";
  public final static String classification_VANHEMPIEN_valueSTAMAT_ASIAT = "koti.vanhempien_valuestamat_asiat";
  public final static String classification_VANHEMPIEN_KASVATUKSELLISET_TAVOITTEET = "koti.vanhempien_kasvatukselliset_tavoitteet";

  public final static String classification_MITTAUS = "mittaus";

  public final static String classification_HAVAINTO = "havainto";
  public final static String classification_KOMMENTTI = "kommentti";
  public final static String classification_TAVOITE = "tavoite";

  public static String developmentIssues() {
    StringBuffer sb = new StringBuffer();

    sb.append(KEHITYSASIAtype_KESKUSTELU + "</br>");
    sb.append(KEHITYSASIAtype_ARVIO + "</br>");
    sb.append(KEHITYSASIAtype_HAVAINTO + "</br>");
    sb.append(KEHITYSASIAtype_KYSELY + "</br>");
    sb.append(KEHITYSASIAtype_PALAUTE + "</br>");
    sb.append(KEHITYSASIAtype_LAHETE + "</br>");
    sb.append(KEHITYSASIAtype_TOIVE + "</br>");
    sb.append(KEHITYSASIAtype_KAYNTI + "</br>");

    return sb.toString();
  }

  public static String luokittelut() {
    StringBuffer sb = new StringBuffer();

    sb.append(classification_KUMPPANUUS);

    sb.append(classification_FYYSINEN_KEHITYS + "</br>");
    sb.append(classification_MOTORIIKKA + "</br>");
    sb.append(classification_MOTORIIKKA_KARKEA + "</br>");
    sb.append(classification_MOTORIIKKA_HIENO + "</br>");
    sb.append(classification_KEHON_HALLINTA + "</br>");
    sb.append(classification_KIELI_JA_KOMMUNIKAATIO + "</br>");
    sb.append(classification_LAPSEN_TOIVEET + "</br>");
    sb.append(classification_VAHVUUDET_JA_KIINNOSTUKSET + "</br>");
    sb.append(classification_LEIKKI + "</br>");
    sb.append(classification_LIIKUNANLLISET_TAIDOT + "</br>");
    sb.append(classification_MATEMAATTISET_TAIDOT + "</br>");
    sb.append(classification_MOTORISTISET_TAIDOT + "</br>");
    sb.append(classification_PSYYKKINEN_KEHITYS + "</br>");
    sb.append(classification_PAIVITTAISET_TOIMINNOT + "</br>");
    sb.append(classification_RAKENTELU + "</br>");
    sb.append(classification_SOSIAALISUUS_JA_TUNNEILMAISU + "</br>");
    sb.append(classification_TARKKAAVAISUUS + "</br>");
    sb.append(classification_KESKITTYMINEN_JA_MUISTI + "</br>");
    sb.append(classification_ULKOILU + "</br>");
    sb.append(classification_TUEN_TARVE + "</br>");
    sb.append(classification_HUOLENAIHEET + "</br>");

    sb.append(classification_TERVEYDENTILA + "</br>");
    sb.append(classification_TERVEYDENTIILA_RUOKAVALIO + "</br>");
    sb.append(classification_TERVEYDENTILA_RUOKAVALIO_PERUSTE + "</br>");

    sb.append(classification_TERVEYDENTILA_SAIRAUS + "</br>");
    sb.append(classification_TERVEYDENTILA_SAIRAUS_ALLERGIA + "</br>");
    sb.append(classification_TERVEYDENTILA_SAIRAUS_ALLERGIA_ELAIN + "</br>");
    sb.append(classification_TERVEYDENTILA_SAIRAUS_ALLERGIA_RUOKA + "</br>");

    sb.append(classification_TERVEYDENTILA_LAAKEHOITO + "</br>");

    sb.append(classification_KOTI + "</br>");
    sb.append(classification_VANHEMPIEN_valueSTAMAT_ASIAT + "</br>");
    sb.append(classification_VANHEMPIEN_KASVATUKSELLISET_TAVOITTEET + "</br>");
    sb.append(classification_HAVAINTO + "</br>");
    sb.append(classification_KOMMENTTI + "</br>");
    sb.append(classification_TAVOITE + "</br>");
    return sb.toString();
  }
}
