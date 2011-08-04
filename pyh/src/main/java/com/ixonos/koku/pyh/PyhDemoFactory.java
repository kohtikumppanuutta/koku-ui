package com.ixonos.koku.pyh;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixonos.koku.pyh.model.Dependant;
import com.ixonos.koku.pyh.model.Family;
import com.ixonos.koku.pyh.model.FamilyMember;
import com.ixonos.koku.pyh.model.Guardian;
import com.ixonos.koku.pyh.model.Guardianship;
import com.ixonos.koku.pyh.model.Person;
import com.ixonos.koku.pyh.util.Role;

public class PyhDemoFactory {

  private static Logger log = LoggerFactory.getLogger(PyhDemoFactory.class);

  public static PyhDemoModel createModel() {

    PyhDemoModel model = new PyhDemoModel();

    Person p1 = new Person("Matti", "Mikael", "Meikäläinen", "010203-1234", "010203", "");
    Person p2 = new Person("Tytti", "Taina", "Peltola", "020304-2345", "020304", "tytti@mummula.fi");
    Person p3 = new Person("Jouni", "Josef", "Merinen", "030405-3456", "030405", "");
    Person p4 = new Person("Tapani", "Toivo", "Ruohonen", "040506-4567", "040506", "");
    Person p5 = new Person("Liisa", "Leila", "Ruohonen", "050607-5678", "050607", "");
    Person p6 = new Person("Pekka", "", "Peltola", "010101-1010", "010101", "pekka.peltola@meili.fi");
    Person p7 = new Person("Piritta", "", "Peltola", "020202-2020", "020202", "irina.peltola@meili.fi");
    Person p8 = new Person("Tero", "Tapani", "Peltola", "111111-1111", "111111", "");
    Person p9 = new Person("Tiina", "Terhi", "Peltola", "222222-2222", "222222", "");
    Person p10 = new Person("Maija", "Mette", "Merinen", "333333-3333", "333333", "");
    Person p11 = new Person("Janne", "Kari", "Merinen", "444444-4444", "444444", "");
    Person p12 = new Person("Laura", "Liina", "Merinen", "555555-5555", "555555", "");
    Person p13 = new Person("Heikki", "Juhani", "Järvinen", "666666-6666", "666666", "");

    model.addPerson(p1);
    model.addPerson(p2);
    model.addPerson(p3);
    model.addPerson(p4);
    model.addPerson(p5);
    model.addPerson(p6);
    model.addPerson(p7);
    model.addPerson(p8);
    model.addPerson(p9);
    model.addPerson(p10);
    model.addPerson(p11);
    model.addPerson(p12);
    model.addPerson(p13);

    // create families

    Family f1 = new Family();
    f1.addFamilyMember(new FamilyMember(p6, Role.PARENT));

    Family f3 = new Family();
    f3.addFamilyMember(new FamilyMember(p7, Role.PARENT));

    Family f2 = new Family();
    f2.addFamilyMember(new FamilyMember(p2, Role.PARENT));

    model.addFamily(f1);
    model.addFamily(f2);
    model.addFamily(f3);

    // create guardianships

    ArrayList<Dependant> d1 = new ArrayList<Dependant>();
    Dependant dep1 = new Dependant(p8);
    Dependant dep2 = new Dependant(p9);
    dep1.setMemberOfUserFamily(false);
    dep2.setMemberOfUserFamily(false);
    d1.add(dep1);
    d1.add(dep2);
    ArrayList<Guardian> g1 = new ArrayList<Guardian>();
    g1.add(new Guardian(p6, "isa"));
    Guardianship gs1 = new Guardianship();
    gs1.setGuardians(g1);
    gs1.setDependants(d1);
    model.addGuardianship(gs1);

    return model;
  }

}
