package com.ixonos.koku.lok;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ixonos.koku.lok.model.User;

@Service(value = "lokDemoService")
public class LokDemoService {

  private static final Logger log = LoggerFactory.getLogger(LokDemoService.class);

  private List<User> searchedUsers;

  public LokDemoService() {
    if (searchedUsers == null) {
      searchedUsers = new ArrayList<User>();

      searchedUsers.add(new User("111111-1111", "uid1", "Paavo", "Paavola"));
      searchedUsers.add(new User("222222-2222", "uid2", "Tytti", "Paavola"));
      searchedUsers.add(new User("333333-3333", "uid3", "Pirjo", "Paavola"));
      searchedUsers.add(new User("444444-4444", "uid4", "Pasi", "Paavola"));
    }
  }

  public List<User> findUsers(String pic, String uid, String fname, String sname) {
    log.info("pic=" + pic + ", fname=" + fname + ", sname=" + sname);
    // Return list of users if one of the search params is not null and not
    // empty string. Else return empty arraylist.
    if (StringUtils.isNotBlank(pic) | StringUtils.isNotBlank(fname) | StringUtils.isNotBlank(sname)) {
      log.info("Returning searchedUsers.size=" + searchedUsers.size());
      return searchedUsers;
    } else {
      return new ArrayList<User>(0);
    }

  }

}
