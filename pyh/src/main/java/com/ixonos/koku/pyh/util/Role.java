package com.ixonos.koku.pyh.util;

public enum Role {

  FATHER("Isä"), MOTHER("Äiti"), FAMILY_MEMBER("Perheyhteisön jäsen"), DEPENDANT("Huollettava lapsi"), CHILD("Lapsi"), PARENT(
      "Vanhempi");

  private String text;

  private Role(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
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
