/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
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
