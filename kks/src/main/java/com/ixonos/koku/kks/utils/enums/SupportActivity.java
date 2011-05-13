package com.ixonos.koku.kks.utils.enums;

/**
 * Enum for support activitys
 * 
 * @author tuomape
 */
public enum SupportActivity implements KKSKentta {

  SUPPORT("supportActivity.support"), TOOL("supportActivity.tool"), TRANSPORT(
      "supportActivity.transport");

  private String bundleId;

  private SupportActivity(String bundleId) {
    this.bundleId = bundleId;
  }

  public String getBundleId() {
    return bundleId;
  }

  public static SupportActivity fromText(String text) {
    for (SupportActivity sa : SupportActivity.values()) {
      if (sa.toString().equalsIgnoreCase(text)) {
        return sa;
      }
    }
    return null;
  }

  public String getId() {
    // TODO change example to db id
    return toString();
  }

  public String getName() {
    return toString();
  }

}
