/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package com.ixonos.koku.pyh.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Guardianship {

  private Map<String, Dependant> dependants;
  private Map<String, Guardian> guardians;

  public Guardianship() {
    guardians = new LinkedHashMap<String, Guardian>();
    dependants = new LinkedHashMap<String, Dependant>();
  }

  public List<Dependant> getDependants() {
    return new ArrayList<Dependant>(dependants.values());
  }

  public void setDependants(List<Dependant> d) {
    for (Dependant dep : d) {
      dependants.put(dep.getPic(), dep);
    }
  }

  public void removeGuardian(String ssn) {
    guardians.remove(ssn);

    if (guardians.size() == 0) {
      dependants.clear();
    }
  }

  public void addDependant(Dependant dep) {
    dependants.put(dep.getPic(), dep);
  }

  public List<Guardian> getGuardians() {
    return new ArrayList<Guardian>(guardians.values());
  }

  public void setGuardians(List<Guardian> g) {
    for (Guardian dep : g) {
      guardians.put(dep.getPic(), dep);
    }
  }

  public void addGuardian(Guardian g) {
    this.guardians.put(g.getPic(), g);
  }

  public boolean guardianshipExists(String guardianSSN, String dependantSSN) {
    return guardians.containsKey(guardianSSN) && dependants.containsKey(dependantSSN);
  }

  public boolean isEmpty() {
    return guardians.size() == 0 && dependants.size() == 0;
  }
}
