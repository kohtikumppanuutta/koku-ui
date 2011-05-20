package com.ixonos.koku.kks.utils;

import java.beans.PropertyEditorSupport;

import com.ixonos.koku.kks.mock.KKSService;
import com.ixonos.koku.kks.utils.enums.KehitystietoTyyppi;

/**
 * Property editor for KehitystietoTyyppi type properties.
 * 
 * @author tuomape
 */
public class KehitystietoTyyppiEditor extends PropertyEditorSupport {

  private KKSService service;

  public KehitystietoTyyppiEditor(KKSService service) {
    super();
    this.service = service;
  }

  @Override
  public String getAsText() {
    String returnVal = "";
    if (getValue() instanceof KehitystietoTyyppi) {
      returnVal = ((KehitystietoTyyppi) getValue()).toString();
    }
    if (getValue() != null && getValue() instanceof String[]) {
      returnVal = ((String[]) getValue())[0];
    }
    return returnVal;
  }

  @Override
  public void setAsText(String text) throws IllegalArgumentException {

    setValue(service.getField(text));
  }
}