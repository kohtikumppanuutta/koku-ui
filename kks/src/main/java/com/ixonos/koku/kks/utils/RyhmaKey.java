package com.ixonos.koku.kks.utils;

public class RyhmaKey implements Comparable<RyhmaKey> {

  private Integer order;
  private String name;

  public RyhmaKey(Integer order, String name) {
    super();
    this.order = order;
    this.name = name;
  }

  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int compareTo(RyhmaKey arg0) {

    return order.compareTo(arg0.getOrder());
  }

}
