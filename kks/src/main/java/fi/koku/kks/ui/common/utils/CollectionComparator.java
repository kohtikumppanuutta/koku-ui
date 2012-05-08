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
