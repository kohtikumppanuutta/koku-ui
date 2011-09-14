package fi.koku.kks.model;

public class GroupKey implements Comparable<GroupKey> {

  private Integer order;
  private String name;

  public GroupKey(Integer order, String name) {
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
  public int compareTo(GroupKey arg0) {

    return order.compareTo(arg0.getOrder());
  }

}
