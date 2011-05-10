package com.ixonos.eservices.koku.kks.utils.enums;

/**
 * Enum for advancement fields
 * 
 * @author tuomape
 */
public enum AdvancementField implements KKSField {

  DAYCARE_START("advancementField.daycare"), FOURTH_ANNUAL_CHECK(
      "advancementField.4.year.check"), PRESCHOOL_TRANSFER(
      "advancementField.preschool.transfer"), SCHOOL_TRANSFER(
      "advancementField.school.transfer"), ASSISTANCE_CHANGE(
      "advancementField.support.need");

  private String bundleId;

  private AdvancementField(String bundleId) {
    this.bundleId = bundleId;
  }

  public String getBundleId() {
    return bundleId;
  }

  public static AdvancementField fromText(String text) {
    for (AdvancementField f : AdvancementField.values()) {
      if (f.toString().equalsIgnoreCase(text)) {
        return f;
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

}
