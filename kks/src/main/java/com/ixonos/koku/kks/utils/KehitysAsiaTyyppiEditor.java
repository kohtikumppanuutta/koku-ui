package com.ixonos.koku.kks.utils;

import java.beans.PropertyEditorSupport;

import com.ixonos.koku.kks.mock.KKSService;
import com.ixonos.koku.kks.utils.enums.KehitysAsiaTyyppi;

public class KehitysAsiaTyyppiEditor extends PropertyEditorSupport {

  private KKSService service;

  public KehitysAsiaTyyppiEditor(KKSService service) {
    super();
    this.service = service;
  }

  @Override
  public String getAsText() {
    String returnVal = "";
    if (getValue() instanceof KehitysAsiaTyyppi) {
      returnVal = ((KehitysAsiaTyyppi) getValue()).toString();
    }
    if (getValue() != null && getValue() instanceof String[]) {
      returnVal = ((String[]) getValue())[0];
    }
    return returnVal;
  }

  @Override
  public void setAsText(String text) throws IllegalArgumentException {

    setValue(service.getAsiatyyppi(text));
  }
}
