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
