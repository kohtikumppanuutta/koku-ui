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
 * Enum for data type
 * 
 * @author Ixonos / tuomape
 * 
 */
public enum DataType {

  FREE_TEXT, MULTI_SELECT, SELECT, TEXT;

  public String getName() {
    return toString();
  }
}
