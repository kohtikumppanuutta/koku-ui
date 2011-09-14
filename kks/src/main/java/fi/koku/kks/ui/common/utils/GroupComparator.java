package fi.koku.kks.ui.common.utils;

import java.util.Comparator;

import fi.koku.kks.model.Group;

public class GroupComparator implements Comparator<Group> {

  @Override
  public int compare(Group o1, Group o2) {
    return o1.compareTo(o2);
  }

}
