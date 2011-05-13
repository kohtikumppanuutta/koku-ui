package com.ixonos.koku.kks.utils;

import java.beans.PropertyEditorSupport;

import com.ixonos.koku.kks.mock.KKSService;
import com.ixonos.koku.kks.utils.enums.KKSKentta;

/**
 * Property editor for KKSField type properties.
 * 
 * @author tuomape
 */
public class KKSKenttaEditor extends PropertyEditorSupport {

  private KKSService service;

  public KKSKenttaEditor(KKSService service) {
    super();
    this.service = service;
  }

  @Override
  public String getAsText() {
    String returnVal = "";
    if (getValue() instanceof KKSKentta) {
      returnVal = ((KKSKentta) getValue()).getId();
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