package com.ixonos.koku.pyh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ixonos.koku.pyh.mock.User;
import com.ixonos.koku.pyh.model.Dependant;
import com.ixonos.koku.pyh.model.DependantExecutable;
import com.ixonos.koku.pyh.model.Family;
import com.ixonos.koku.pyh.model.FamilyExecutable;
import com.ixonos.koku.pyh.model.FamilyMember;
import com.ixonos.koku.pyh.model.Guardian;
import com.ixonos.koku.pyh.model.Guardianship;
import com.ixonos.koku.pyh.model.Message;
import com.ixonos.koku.pyh.model.MessageService;
import com.ixonos.koku.pyh.model.ParentExecutable;
import com.ixonos.koku.pyh.model.Person;
import com.ixonos.koku.pyh.util.Role;

@Service(value = "pyhDemoService")
public class PyhDemoService {

  private static Logger log = LoggerFactory.getLogger(PyhDemoService.class);

  private PyhDemoModel model;
  private User user;
  private List<Person> searchedUsers;

  @Autowired
  @Qualifier(value = "pyhMessageService")
  private MessageService messageService;

  public PyhDemoService() {
    if (model == null) {
      this.model = PyhDemoFactory.createModel();
    }
    if (user == null) {
      this.user = new User("010101-1010", "guardian");
      // this.user = new User("030405-3456", "guardian");
    }
  }

  public void setUser(String ssn) {
    this.user = new User(ssn, "guardian");
  }

  public void reset() {
    this.model = PyhDemoFactory.createModel();
  }

  public Person getUser() {
    return model.getPerson(user.getSsn());
  }

  public List<Dependant> getDependants() {
    List<Dependant> dependants = new ArrayList<Dependant>();

    List<Guardianship> guardianships = model.getGuardianships();
    Iterator<Guardianship> gsi = guardianships.iterator();
    while (gsi.hasNext()) {
      Guardianship gs = gsi.next();

      List<Guardian> guardians = gs.getGuardians();
      Iterator<Guardian> gi = guardians.iterator();
      while (gi.hasNext()) {
        Guardian g = gi.next();
        if (g.getSsn().equals(user.getSsn())) {

          // if user SSN matches guardian's SSN then we have correct
          // guardianship
          // and we can return guardianship's dependants (that is user's
          // dependants)
          dependants = gs.getDependants();
        }
      }
    }
    return dependants;
  }

  public List<FamilyMember> getNonDependants() {
    Family usersFamily = getUsersFamily();
    return usersFamily.getChilds();
  }

  public List<FamilyMember> getOtherFamilyMembers() {
    List<FamilyMember> otherFamilyMembers = new ArrayList<FamilyMember>();
    Family usersFamily = getUsersFamily();
    List<FamilyMember> fmList = usersFamily.getAllMembers();

    Iterator<FamilyMember> fmi = fmList.iterator();
    while (fmi.hasNext()) {
      FamilyMember fm = fmi.next();
      if (!fm.getSsn().equals(user.getSsn()) && !isUsersDependant(fm.getSsn())) {
        otherFamilyMembers.add(fm);
      }
    }
    return otherFamilyMembers;
  }

  public boolean isParentsSet() {
    return getUsersFamily().isParentsSet();
  }

  public void searchUsers(String firstname, String surname, String ssn) {
    clearSearchedUsers();

    Family family = getUsersFamily();
    if (firstname.equals("") == false || surname.equals("") == false || ssn.equals("") == false) {
      Iterator<Person> pi = model.getPersons().iterator();
      while (pi.hasNext()) {
        Person person = pi.next();
        if ((person.getFirstname().equalsIgnoreCase(firstname) || person.getSurname().equalsIgnoreCase(surname) || person
            .getSsn().equalsIgnoreCase(ssn)) && !family.isFamilyMember(person.getSsn())) {
          searchedUsers.add(person);
        }
      }
    }
  }

  public List<Person> getSearchedUsers() {
    return searchedUsers;
  }

  public void clearSearchedUsers() {
    if (searchedUsers == null) {
      searchedUsers = new ArrayList<Person>();
    } else {
      searchedUsers.clear();
    }
  }

  public void addDependantAsFamilyMember(String dependantSSN) {
    Person user = getUser();
    List<String> tmp = generateRecipients(dependantSSN, user);

    sendDependantFamilyAdditionMessage(tmp, user, model.getPerson(dependantSSN), Role.CHILD);
  }

  private List<String> generateRecipients(String targetSSN, Person user) {
    List<String> tmp = new ArrayList<String>();
    FamilyMember f = getUsersFamily().getOtherParent(user.getSsn());

    if (f != null) {
      tmp.add(f.getSsn());
    }
    Family fam = getFamily(targetSSN);

    if (fam != null) {
      for (FamilyMember fm : fam.getParents()) {
        if (!fm.getSsn().equals(user.getSsn()) && !tmp.contains(fm.getSsn())) {
          tmp.add(fm.getSsn());
        }
      }
    }
    return tmp;
  }

  public void insertDependantToFamily(String userSSN, String dependantSSN, Role role) {
    Dependant dependant = model.getDependant(dependantSSN);
    dependant.setMemberOfUserFamily(true);
    Family family = getFamily(userSSN);
    family.addFamilyMember(new FamilyMember(dependant, role));
  }

  public void addNewDependant(Person dependant) {

    if (model.getPerson(dependant.getSsn()) == null) {
      model.addPerson(dependant);
    }

    // get user's guardianship or create if does not exist
    Guardianship guardianship = model.getGuardianship(user.getSsn());
    if (guardianship == null) {

      guardianship = new Guardianship();
      Person person = model.getPerson(user.getSsn());
      // TODO: get guardian's role
      guardianship.addGuardian(new Guardian(person, "rooli"));
      model.addGuardianship(guardianship);
    }
    guardianship.addDependant(new Dependant(dependant));
  }

  public void removeGuardianShip(String dependantSSN) {
    // get user's guardianship or create if does not exist
    Guardianship guardianship = model.getGuardianship(user.getSsn());
    if (guardianship != null) {
      guardianship.removeGuardian(user.getSsn());

      if (guardianship.isEmpty()) {
        model.getGuardianships().remove(guardianship);
      }
    }
  }

  public void removeFamilyChild(String familyMemberSSN) {
    Family family = getUsersFamily();
    FamilyMember familyMember = family.getChild(familyMemberSSN);
    family.removeFamilyMember(familyMember);
  }

  public void removeFamilyMember(String familyMemberSSN) {
    Family family = getUsersFamily();
    FamilyMember familyMember = family.getFamilyMember(familyMemberSSN);
    family.removeFamilyMember(familyMember);

    if (Role.PARENT.equals(familyMember.getRole())) {

      // Remove childs when parent is removed (both needs to set up childs
      // again)
      List<FamilyMember> childs = family.getChilds();
      for (FamilyMember f : childs) {
        family.removeFamilyMember(f);
      }

      Family tmp = getFamily(familyMember.getSsn());
      if (tmp == null) {
        Family f = new Family();
        f.addFamilyMember(familyMember);
        model.addFamily(f);
      }
    }
  }

  public void addPersonsAsFamilyMembers(HashMap<String, String> personMap) {
    Family family = getUsersFamily();
    Set<String> keys = personMap.keySet();
    Iterator<String> si = keys.iterator();
    Person user = getUser();
    while (si.hasNext()) {
      String ssn = si.next();
      String role = personMap.get(ssn);

      Person person = model.getPerson(ssn);
      Role r = Role.create(role);

      List<String> tmp = generateRecipients(ssn, user);

      if (Role.PARENT.equals(r)) {
        sendParentAdditionMessage(ssn, user, ssn, person, r);
      } else {
        sendFamilyAdditionMessage(tmp, user, ssn, person, r);
      }

    }
  }

  private void sendParentAdditionMessage(String recipient, Person user, String ssn, Person person, Role r) {
    List<String> tmp = new ArrayList<String>();
    tmp.add(recipient);
    Message message = Message.createMessage(tmp, user.getSsn(), ssn, person.getCapFullName()
        + " Uusi perheyhteystieto.", "Käyttäjä " + user.getFullName()
        + " on lisännyt sinut perheyhteisön toiseksi vanhemmaksi. "
        + "Hyväksymällä pyynnön perheyhteisönne yhdistetään automaattisesti.", new ParentExecutable(user.getSsn(), ssn,
        r, this));
    messageService.addMessage(message);
  }

  private void sendFamilyAdditionMessage(List<String> recipients, Person user, String ssn, Person person, Role r) {
    Message message = Message.createMessage(recipients, user.getSsn(), ssn, person.getCapFullName()
        + " Uusi perheyhteystieto.", "Käyttäjä " + user.getFullName() + " on lisännyt henkilön " + person.getFullName()
        + " perheyhteisön muuksi jäseneksi. "
        + "Kaikkien opsapuolten on hyväksyttävä uuden jäsenen liittäminen perheyhteisöön.",
        new FamilyExecutable(user.getSsn(), ssn, r, this));
    messageService.addMessage(message);
  }

  private void sendDependantFamilyAdditionMessage(List<String> recipients, Person user, Person person, Role r) {
    Message message = Message.createMessage(recipients, user.getSsn(), person.getSsn(), person.getCapFullName()
        + " Uusi perheyhteystieto.", "Käyttäjä " + user.getFullName() + " on lisännyt henkilön " + person.getFullName()
        + " perheyhteisön muuksi jäseneksi. "
        + "Kaikkien opsapuolten on hyväksyttävä uuden jäsenen liittäminen perheyhteisöön.",
        new DependantExecutable(user.getSsn(), person.getSsn(), r, this));
    messageService.addMessage(message);
  }

  public void insertInto(String toFamilySSN, String personSSN, Role role) {
    Family family = getFamily(toFamilySSN);
    Person person = model.getPerson(personSSN);
    switch (role) {
    case DEPENDANT:
      addNewDependant(person);
      break;
    default:
      family.addFamilyMember(new FamilyMember(person, role));
      break;
    }
  }

  public void insertParentInto(String toFamilySSN, String personSSN, Role role) {
    Family family = getFamily(toFamilySSN);
    Family combine = getFamily(personSSN);
    Person person = model.getPerson(personSSN);
    family.addFamilyMember(new FamilyMember(person, role));
    family.combineFamily(combine);
    model.removeFamily(combine);
  }

  private Family getFamily(String ssn) {
    Iterator<Family> fi = model.getFamilies().iterator();
    while (fi.hasNext()) {
      Family f = fi.next();
      if (f.isFamilyMember(ssn)) {
        return f;
      }
    }
    return null;
  }

  private Family getUsersFamily() {
    Iterator<Family> fi = model.getFamilies().iterator();
    while (fi.hasNext()) {
      Family f = fi.next();
      if (f.isFamilyMember(user.getSsn())) {
        return f;
      }
    }
    return null;
  }

  private boolean isUsersDependant(String dependantSSN) {
    String userSSN = user.getSsn();
    List<Guardianship> guardianships = model.getGuardianships();
    Iterator<Guardianship> gi = guardianships.iterator();
    while (gi.hasNext()) {
      Guardianship g = gi.next();
      if (g.guardianshipExists(user.getSsn(), dependantSSN)) {
        return true;
      }
    }
    return false;
  }

}
