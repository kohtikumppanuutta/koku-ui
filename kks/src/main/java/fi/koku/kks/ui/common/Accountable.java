/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
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
