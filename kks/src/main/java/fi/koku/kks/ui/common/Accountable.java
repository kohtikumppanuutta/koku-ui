/*
 * Copyright 2012 Ixonos Plc, Finland. All rights reserved.
 * 
 * This file is part of Kohti kumppanuutta.
 *
 * This file is licensed under GNU LGPL version 3.
 * Please see the 'license.txt' file in the root directory of the package you received.
 * If you did not receive a license, please contact the copyright holder
 * (http://www.ixonos.com/).
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
