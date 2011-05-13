package com.ixonos.koku.kks.utils.enums;

public enum UIField implements KKSKentta {

  ALL("uiField.all");

  private String bundleId;

  private UIField(String bundleId) {
    this.bundleId = bundleId;
  }

  public String getBundleId() {
    return bundleId;
  }

  public static UIField fromText(String text) {
    for (UIField ui : UIField.values()) {
      if (ui.toString().equalsIgnoreCase(text)) {
        return ui;
      }
    }
    return null;
  }

  public String getId() {
    // TODO change example to db id
    return bundleId;
  }

  public String getName() {
    return toString();
  }

  public String[] toArray() {
    return new String[] { getId() };
  }
}
