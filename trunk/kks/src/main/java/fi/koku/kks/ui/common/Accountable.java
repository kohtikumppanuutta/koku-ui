package fi.koku.kks.ui.common;

/**
 * Enum for accountable
 * 
 * @author Ixonos / tuomape
 * 
 */
public enum Accountable {

  GUARDIAN("guardian"), MUNICIPAL_EMPLOYEE("municipal_employee");

  private String name;

  private Accountable(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}
