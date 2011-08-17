package fi.arcusys.koku.palvelut.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fi.arcusys.koku.palvelut.model.client.VeeraCategoryImpl;


public class CategoryUtil {

	public static void test(HttpServletRequest request) {
		System.out.println(request);
	}

	public static int[] getEntryIds(List<VeeraCategoryImpl> categories) {
		int[] entryIds = new int[categories.size()];
		int index = 0;
		for (VeeraCategoryImpl category : categories) {
			entryIds[(index++)] = category.getEntryId().intValue();
		}
		return entryIds;
	}

	public static Integer min(int[] integers) {
		if ((integers == null) || (integers.length == 0))
			return null;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < integers.length; ++i) {
			if (integers[i] >= min)
				continue;
			min = integers[i];
		}
		return Integer.valueOf(min);
	}
}