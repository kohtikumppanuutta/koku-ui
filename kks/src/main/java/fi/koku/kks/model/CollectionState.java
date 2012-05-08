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

import fi.koku.kks.ui.common.State;

/**
 * Collection state
 * 
 * @author tuomape
 * 
 */
public class CollectionState {

  private State state;

  public CollectionState(State state) {
    super();
    this.state = state;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public boolean isActive() {
    if (State.LOCKED.equals(state)) {
      return false;
    }

    return true;
  }
}
