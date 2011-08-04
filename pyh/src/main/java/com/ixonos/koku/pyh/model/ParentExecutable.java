package com.ixonos.koku.pyh.model;

import com.ixonos.koku.pyh.PyhDemoService;
import com.ixonos.koku.pyh.util.Role;

public class ParentExecutable implements Executable {

  private String userSSN;
  private String targetSSN;
  private PyhDemoService service;
  private Role role;

  public ParentExecutable(String parentSSN, String targetSSN, Role role, PyhDemoService service) {
    super();
    this.userSSN = parentSSN;
    this.targetSSN = targetSSN;
    this.service = service;
    this.role = role;
  }

  @Override
  public void execute() {
    service.insertParentInto(userSSN, targetSSN, role);
  }

}
