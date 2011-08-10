package com.ixonos.koku.kks.malli;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ixonos.koku.kks.mock.Kayttaja;
import com.ixonos.koku.kks.utils.HakuTulokset;

/**
 * Service demoa varten
 * 
 * @author tuomape
 * 
 */
@Service(value = "demoKksService")
public class DemoService {

  private Kayttaja kayttaja;

  private KKSDemoMalli malli;

  public DemoService() {

  }

  private static Logger log = LoggerFactory.getLogger(DemoService.class);

  public boolean onkoLuotu() {
    if (malli != null)
      return true;
    return false;
  }

  public void luo(String kayttaja) {
    this.kayttaja = new Kayttaja();
    this.kayttaja.setRole(kayttaja);
    malli = DemoFactory.luo();
  }

  public List<Henkilo> haeLapset(Kayttaja user) {
    return malli.getHenkilot();
  }

  public List<Henkilo> haeHenkilo(Henkilo target) {
    List<Henkilo> list = new ArrayList<Henkilo>();
    Henkilo tmp = haeLapsi(target.getHetu().trim());

    if (tmp != null) {
      list.add(tmp);
    }
    return list;
  }

  public Henkilo haeLapsi(String socialSecurityNumber) {
    for (Henkilo tmp : malli.getHenkilot()) {
      if (tmp.getHetu().equals(socialSecurityNumber.trim())) {
        return tmp;
      }
    }
    return null;
  }

  /**
   * Creates a collection for person
   * 
   * @param h
   *          Person
   * @param nimi
   *          Name of the collection
   */
  public Kokoelma luoKokoelma(Henkilo h, String nimi, Aktivoitava aktivointi) {

    Kokoelma k = null;

    if (aktivointi.isVersioitava()) {
      Kokoelma kokoelma = h.getKks().getKokoelma(aktivointi.getId());
      k = DemoFactory.luoUusiVersio(kokoelma, nimi, aktivointi.isNoCopy());
    } else {
      k = DemoFactory.luoKokoelma(nimi, aktivointi.getNimi());
    }

    h.getKks().lisaaKokoelma(k);
    return k;
  }

  public List<String> haeAktivoitavatKokoelmat(Henkilo h) {
    List<String> nimet = haeKokoelmaNimet();

    List<String> tmp = new ArrayList<String>(nimet);
    for (String nimi : tmp) {
      if (!h.getKks().hasKokoelma(nimi))
        nimet.remove(nimi);
    }

    return nimet;
  }

  /**
   * This method lists all the possible collections for this person
   * 
   * @param h
   * @return
   */
  public List<Aktivoitava> haeHenkilonKokoelmat(Henkilo h) {
    Set<KokoelmaTyyppi> tyypit = haeKokoelmaTyypit(h);

    List<Aktivoitava> aktivoitavat = new ArrayList<Aktivoitava>();

    for (KokoelmaTyyppi kt : tyypit) {
      aktivoitavat.add(new Aktivoitava("" + kt.getKoodi(), false, kt.getNimi()));
    }

    return aktivoitavat;
  }

  public Set<KokoelmaTyyppi> haeKokoelmaTyypit(Henkilo h) {
    Set<KokoelmaTyyppi> tmp = new LinkedHashSet<KokoelmaTyyppi>();
    tmp.add(DemoFactory.luoNelivuotisTarkastusTyyppi());
    tmp.add(DemoFactory.luoVarhaiskasvatusSuunnitelmanTyyppi());
    return tmp;
  }

  // 21.6. this method is probably no longer needed
  public List<String> haeLuotavatKokoelmat(Henkilo h) {
    List<String> nimet = haeKokoelmaNimet();
    List<Kokoelma> kokoelmat = h.getKks().getKokoelmat();
    for (Kokoelma k : kokoelmat) {
      nimet.remove(k.getNimi());
    }
    return nimet;
  }

  public List<String> haeKokoelmaNimet() {
    List<String> tmp = new ArrayList<String>();
    tmp.add(DemoFactory.NELI_VUOTIS_TARKASTUS);
    tmp.add(DemoFactory.VASU);
    return tmp;
  }

  public HakuTulokset haeKirjauksia(Henkilo h, String... luokitus) {
    HakuTulokset tulos = new HakuTulokset();
    List<Kokoelma> kokoelmat = h.getKks().getKokoelmat();

    for (Kokoelma k : kokoelmat) {
      for (Kirjaus ki : k.getKirjaukset().values()) {
        boolean lisatty = false;
        for (int i = 0; i < luokitus.length && !lisatty; i++) {
          String tmp = luokitus[i];
          if (ki.hasLuokitus(tmp) && !ki.getArvo().equals("")) {
            tulos.lisaaTulos(k, ki);
            lisatty = true;
          }
        }
      }

      for (List<Kirjaus> tmp : k.getMoniArvoisetKirjaukset().values()) {
        for (Kirjaus ki : tmp) {
          boolean lisatty = false;
          for (int i = 0; i < luokitus.length && !lisatty; i++) {
            String str = luokitus[i];
            if (ki.hasLuokitus(str) && !ki.getArvo().equals("")) {
              tulos.lisaaTulos(k, ki);
              lisatty = true;
            }
          }
        }
      }
    }

    return tulos;
  }

  public List<KokoelmaTyyppi> haeKokoelmaTyypit() {
    List<KokoelmaTyyppi> tmp = new ArrayList<KokoelmaTyyppi>();

    List<Kokoelma> tmp2 = new ArrayList<Kokoelma>();

    tmp2.add(DemoFactory.luo4VuotisTarkastus(""));
    tmp2.add(DemoFactory.luoVarhaiskasvatusSuunnitelma(""));

    for (Kokoelma k : tmp2) {
      tmp.add(k.getTyyppi());
    }
    return tmp;
  }
}
