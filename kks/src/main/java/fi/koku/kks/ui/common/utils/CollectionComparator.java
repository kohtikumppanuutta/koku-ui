package fi.koku.kks.ui.common.utils;

import java.util.Comparator;

import fi.koku.kks.model.KKSCollection;

public class CollectionComparator implements Comparator<KKSCollection> {

  @Override
  public int compare(KKSCollection o1, KKSCollection o2) {
    // TODO Auto-generated method stub
    return o1.getName().compareTo(o2.getName());
  }

}
