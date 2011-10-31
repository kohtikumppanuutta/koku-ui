/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package com.ixonos.koku.pyh.model;

import com.ixonos.koku.pyh.PyhDemoService;
import com.ixonos.koku.pyh.util.CommunityRole;

public class DependantExecutable implements Executable {

  private String userPic;
  private String targetPic;
  private CommunityRole role;
  private PyhDemoService service;

  public DependantExecutable(String parentPic, String targetPic, CommunityRole role, PyhDemoService service) {
    super();
    this.userPic = parentPic;
    this.targetPic = targetPic;
    this.service = service;
    this.role = role;
  }
  
  @Override
  public void execute() {
    service.insertDependantToFamily(userPic, targetPic, role);
  }

}
