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
