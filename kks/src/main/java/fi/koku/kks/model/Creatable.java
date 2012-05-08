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
package fi.koku.kks.model;

/**
 * Describes an element that can be created. Element can be collection type or
 * existing collection
 * 
 * @author tuomape
 * 
 */
public class Creatable {

  private String id;
  private boolean needsVersioning;
  private boolean copyContent;
  private String name;

  public Creatable(String id, boolean needsVersioning, String name) {
    super();
    this.id = id;
    this.needsVersioning = needsVersioning;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isNeedsVersioning() {
    return needsVersioning;
  }

  public void setNeedsVersioning(boolean needsVersioning) {
    this.needsVersioning = needsVersioning;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isCopyContent() {
    return copyContent;
  }

  public void setCopyContent(boolean noCopy) {
    this.copyContent = noCopy;
  }

  /**
   * Gets creatable as text
   * 
   * @return creatable as text (fields separated by #)
   * @see #create(String)
   */
  public String getAsText() {
    return id + "#" + needsVersioning + "#" + name;
  }

  /**
   * Creates creatable from given text (fields needs to be separated by #)
   * 
   * @param text
   *          that contains creatable information
   * @return created object
   * @see #getAsText()
   */
  public static Creatable create(String text) {
    String tmp[] = text.split("#");

    return new Creatable(tmp[0], Boolean.valueOf(tmp[1]).booleanValue(), tmp[2]);
  }

}
