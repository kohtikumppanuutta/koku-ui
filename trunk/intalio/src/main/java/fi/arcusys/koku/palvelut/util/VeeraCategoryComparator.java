package fi.arcusys.koku.palvelut.util;

import java.util.Comparator;

import fi.arcusys.koku.palvelut.model.client.VeeraCategory;

public class VeeraCategoryComparator implements Comparator<VeeraCategory> {

	public int compare(VeeraCategory o1, VeeraCategory o2) {
		String firstCategoryName = o1.getName();
		String secondCategoryName = o2.getName();
		return firstCategoryName.compareTo(secondCategoryName);
	}

}
