package com.ixonos.koku.kks.utils.enums;

/**
 * Enum for child info fields
 * 
 * @author tuomape
 * 
 */
public enum ChildInfo implements KKSKentta {

  LIVING_CONDITIONS("childInfo.living.conditions"), PARENTING_INFO(
      "childInfo.parenting.info"), CHILD_HEALTH_CENTRE(
      "childInfo.child.health.centre"), DAYCARE("childInfo.daycare"), BASE_EDUCATION(
      "childInfo.base.education"), SCHOOL_HEALTHCARE(
      "childInfo.school.healt.care");

  private String bundleId;

  private ChildInfo(String bundleId) {
    this.bundleId = bundleId;

  }

  public String getBundleId() {
    return bundleId;
  }

  public static ChildInfo fromText(String text) {
    for (ChildInfo c : ChildInfo.values()) {
      if (c.toString().equalsIgnoreCase(text)) {
        return c;
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
