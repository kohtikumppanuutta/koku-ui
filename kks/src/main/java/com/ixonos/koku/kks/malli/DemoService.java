package com.ixonos.koku.kks.malli;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ixonos.koku.kks.mock.Kayttaja;

@Service(value = "demoKksService")
public class DemoService {

  private Kayttaja kayttaja;

  private KKSDemoMalli malli;

  public DemoService() {

  }

  public boolean onkoLuotu() {
    if (malli != null)
      return true;
    return false;
  }

  public void luo(String kayttaja) {
    System.out.println("LUODAAN!!!!!!!!!!!!");
    this.kayttaja = new Kayttaja();
    this.kayttaja.setRole(kayttaja);
    malli = DemoFactory.luo();
  }

  public List<Henkilo> haeLapset(Kayttaja user) {
    return malli.getHenkilot();
  }

  public List<Henkilo> haeHenkilo(Henkilo target) {
    List<Henkilo> list = new ArrayList<Henkilo>();
    Henkilo tmp = haeLapsi(target.getHetu());

    if (tmp != null && tmp.getEtunimi().equals(target.getEtunimi())
        && tmp.getSukunimi().equals(target.getSukunimi())) {
      list.add(tmp);
    }

    return list;
  }

  public Henkilo haeLapsi(String socialSecurityNumber) {
    for (Henkilo tmp : malli.getHenkilot()) {
      if (tmp.getHetu().equals(socialSecurityNumber)) {
        return tmp;
      }
    }
    return null;
  }

  public void luoKokoelma(Henkilo h, String nimi) {
    Kokoelma k = DemoFactory.luoKokoelma(nimi);

    if (k != null) {
      h.getKks().lisaaKokoelma(k);
    }
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
}
