package fi.arcusys.koku.palvelut.util;

import java.util.Comparator;

import fi.arcusys.koku.palvelut.model.client.VeeraFormImpl;
import fi.arcusys.koku.palvelut.model.client.VeeraForm;

public class VeeraFormComparator implements Comparator<VeeraForm> {

	public int compare(VeeraForm o1, VeeraForm o2) {
		String firstFormName = VeeraFormImpl.identityFrom64(o1.getIdentity());
		String secondFormName = VeeraFormImpl.identityFrom64(o2.getIdentity());
		return firstFormName.compareTo(secondFormName);
	}

}