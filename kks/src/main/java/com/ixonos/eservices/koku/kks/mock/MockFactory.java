package com.ixonos.eservices.koku.kks.mock;

import java.util.Date;

import com.ixonos.eservices.koku.kks.utils.enums.AdvancementField;
import com.ixonos.eservices.koku.kks.utils.enums.AdvancementType;
import com.ixonos.eservices.koku.kks.utils.enums.ChildInfo;

public class MockFactory {

	public static KKSModel createModel() {

		Child c1 = new Child("Etu1", "Toka1", "Sukunimi1");
		c1.setSocialSecurityNumber("270608-223Z");
		Child c2 = new Child("Etu2", "Toka2", "Sukunimi2");
		c2.setSocialSecurityNumber("200107-211Y");

		KKSModel tmp = new KKSModel();
		tmp.addChild(c1);
		tmp.addChild(c2);

		tmp.addEntry(createEntry1(c1));
		tmp.addEntry(createEntry2(c1));
		tmp.addEntry(createEntry3(c2));

		return tmp;
	}

	public static KKSEntry createEntry1(Child c) {
		KKSEntry entry1 = new KKSEntry(new Date(System.currentTimeMillis()),
				"Pituus 123cm, paino 22kg. Ryhti hyvä.");
		entry1.addField(AdvancementField.FOURTH_ANNUAL_CHECK);
		entry1.addField(AdvancementType.MEASUREMENT);
		entry1.addField(AdvancementType.PHYSICAL);
		entry1.addField(ChildInfo.CHILD_HEALTH_CENTRE);
		entry1.setChild(c);
		return entry1;
	}

	public static KKSEntry createEntry2(Child c) {
		KKSEntry entry1 = new KKSEntry(
				new Date(System.currentTimeMillis()),
				"Oppi ajamaan pyörällä ilman apupyöriä. Ajaa jo pitkiä matkoja. Voi alkaa kulkea koulumatkoja pyörällä");
		entry1.addField(AdvancementField.SCHOOL_TRANSFER);
		entry1.addField(ChildInfo.PARENTING_INFO);
		entry1.addField(AdvancementType.OBSERVATION);
		entry1.setChild(c);
		return entry1;
	}

	public static KKSEntry createEntry3(Child c) {
		KKSEntry entry1 = new KKSEntry(new Date(System.currentTimeMillis()),
				"Lähineuvola lakkautusuhan alla");
		entry1.addField(AdvancementField.ASSISTANCE_CHANGE);
		entry1.addField(ChildInfo.LIVING_CONDITIONS);
		entry1.addField(AdvancementType.OBSERVATION);
		entry1.setChild(c);
		return entry1;
	}
}
