package com.ixonos.eservices.koku.kks.utils.enums;


/**
 * Enum for healt conditions
 * 
 * @author tuomape
 * 
 */
public enum HealthCondition implements KKSField {

  ILLNESS("healthCondition.illness"), DIET("healthCondition.diet"), SPECIAL_DIET(
      "healthCondition.special.diet");

  private String bundleId;

  private HealthCondition(String bundleId) {
    this.bundleId = bundleId;
  }

  public String getBundleId() {
    return bundleId;
  }

  public static HealthCondition fromText(String text) {
    for (HealthCondition hc : HealthCondition.values()) {
      if (hc.toString().equalsIgnoreCase(text)) {
        return hc;
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
