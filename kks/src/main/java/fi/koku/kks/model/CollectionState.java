/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
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
