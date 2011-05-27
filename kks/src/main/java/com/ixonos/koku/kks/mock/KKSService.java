package com.ixonos.koku.kks.mock;

import java.util.List;

import com.ixonos.koku.kks.utils.enums.KehitysAsiaTyyppi;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

public interface KKSService {

  public Kayttaja getUser();

  public void create(String userRole);

  public List<Henkilo> haeLapset(Kayttaja user);

  public List<Henkilo> haeHenkilo(Henkilo hakuehdot);

  public Henkilo haeLapsi(String hetu);

  public KehitystietoTyyppi getTyyppi(String fieldId);

  public KehitysAsiaTyyppi getAsiatyyppi(String fieldId);

}
