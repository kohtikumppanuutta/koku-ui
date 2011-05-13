package com.ixonos.koku.kks.utils.enums;

/**
 * Enum for advancement types
 * 
 * @author tuomape
 */
public enum AdvancementType implements KKSKentta {

  VASU("advancementType.vasu"), HOJKS("advancementType.hojks"), ESTIMATE(
      "advancementType.estimate"), MEASUREMENT("advancementType.measurement"), TARGET(
      "advancementType.target"), OBSERVATION("advancementType.observation"), PHYSICAL(
      "advancementType.physical"), MENTAL("advancementType.mental"), LINGUISTIC(
      "advancementType.linguistic"), SOCIAL("advancementType.social");

  private String bundleId;

  private AdvancementType(String bundleId) {
    this.bundleId = bundleId;

  }

  public String getBundleId() {
    return bundleId;
  }

  public static AdvancementType fromText(String text) {
    for (AdvancementType at : AdvancementType.values()) {
      if (at.toString().equalsIgnoreCase(text)) {
        return at;
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
