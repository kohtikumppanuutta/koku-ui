package com.ixonos.koku.pyh.util;

public enum Role {

  FATHER("ui.pyh.father"), MOTHER("ui.pyh.mother"), FAMILY_MEMBER("ui.pyh.family"), DEPENDANT("ui.pyh.dependant"), CHILD(
      "ui.pyh.child"), PARENT("ui.pyh.parent");

  private String bundleId;

  private Role(String text) {
    this.bundleId = text;
  }

  public String getBundleId() {
    return bundleId;
  }

  public static Role create(String text) {
    for (Role r : values()) {
      if (r.toString().equals(text)) {
        return r;
      }

    }
    return Role.FAMILY_MEMBER;
  }

}
