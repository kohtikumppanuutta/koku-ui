package com.ixonos.koku.pyh.model;

import java.util.Iterator;
import java.util.List;

public class Guardianship {
  
  private List<Dependant> dependants;
  private List<Guardian> guardians;
  
  public Guardianship() {
    
  }
  
  public Guardianship(List<Dependant> d, List<Guardian> g) {
    this.dependants = d;
    this.guardians = g;
  }
  
  public List<Dependant> getDependants() {
    return dependants;
  }
  
  public void setDependants(List<Dependant> d) {
    this.dependants = d;
  }
  
  public void addDependant(Dependant d) {
    this.dependants.add(d);
  }
  
  public List<Guardian> getGuardians() {
    return guardians;
  }
  
  public void setGuardians(List<Guardian> g) {
    this.guardians = g;
  }
  
  public void addGuardian(Guardian g) {
    this.guardians.add(g);
  }
  
  public boolean guardianshipExists(String guardianSSN, String dependantSSN) {
    boolean exists = false;
    
    Iterator<Guardian> gi = guardians.iterator();
    while (gi.hasNext()) {
      if (gi.next().getSsn().equals(guardianSSN)) {
        
        // we have guardian match, next check if dependant match is found
        Iterator<Dependant> di = dependants.iterator();
        while (di.hasNext()) {
          if (di.next().getSsn().equals(dependantSSN)) {
            
            // guardianship exists
            exists = true;
            return exists;
          }
        }
        
      }
    }
    return exists;
  }
  
}
