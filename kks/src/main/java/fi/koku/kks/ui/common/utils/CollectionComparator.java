/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.kks.ui.common.utils;

import java.util.Comparator;

import fi.koku.kks.model.KKSCollection;

/**
 * Comparator for collections
 * 
 * @author Ixonos / tuomape
 * 
 */
public class CollectionComparator implements Comparator<KKSCollection> {

  @Override
  public int compare(KKSCollection o1, KKSCollection o2) {
    return o1.getName().compareTo(o2.getName());
  }

}
