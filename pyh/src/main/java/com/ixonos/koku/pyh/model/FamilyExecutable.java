package com.ixonos.koku.pyh.model;

import com.ixonos.koku.pyh.PyhDemoService;
import com.ixonos.koku.pyh.util.CommunityRole;

public class FamilyExecutable implements Executable {

  private String userPic;
  private String targetPic;
  private CommunityRole role;
  private PyhDemoService service;

  public FamilyExecutable(String parentPic, String targetPic, CommunityRole role, PyhDemoService service) {
    super();
    this.userPic = parentPic;
    this.targetPic = targetPic;
    this.service = service;
    this.role = role;
  }
  
  @Override
  public void execute() {
    service.insertInto(userPic, targetPic, role);
  }

}
