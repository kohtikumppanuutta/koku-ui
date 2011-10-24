package fi.arcusys.koku.palvelut.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fi.arcusys.koku.palvelut.model.client.FormHolder;
import fi.arcusys.koku.palvelut.model.client.VeeraCategory;
import fi.arcusys.koku.palvelut.model.client.VeeraCategoryImpl;
import fi.arcusys.koku.palvelut.model.client.VeeraForm;
import fi.arcusys.koku.palvelut.model.client.VeeraFormImpl;
import fi.arcusys.koku.palvelut.ws.client.FormHolderDTO;
import fi.arcusys.koku.palvelut.ws.client.VeeraCategoryDTO;
import fi.arcusys.koku.palvelut.ws.client.VeeraFormDTO;

/**
 * @author Dmitry Kudinov (dmitry.kudinov@arcusys.fi)
 * Apr 19, 2011
 */
class WsClientUtils {

	static List<VeeraCategoryImpl> convertCategories(final List<VeeraCategoryDTO> categories) {
		if (categories == null || categories.isEmpty()) {
			return Collections.emptyList();
		}
		final List<VeeraCategoryImpl> result = new ArrayList<VeeraCategoryImpl>(categories.size());
		for (final VeeraCategoryDTO category : categories) {
			result.add(convert(category));
		}
		return result;
	}

	static List<VeeraFormImpl> convertForms(final List<VeeraFormDTO> forms) {
		if (forms == null || forms.isEmpty()) {
			return Collections.emptyList();
		}
		final List<VeeraFormImpl> result = new ArrayList<VeeraFormImpl>(forms.size());
		for (final VeeraFormDTO form : forms) {
			result.add(convert(form));
		}
		return result;
	}

	static List<FormHolderDTO> convertFormHolders(final List<FormHolder> formHolders) {
		if (formHolders == null || formHolders.isEmpty()) {
			return Collections.emptyList();
		}
		final List<FormHolderDTO> result = new ArrayList<FormHolderDTO>(formHolders.size());
		for (final FormHolder formHolder : formHolders) {
			result.add(new FormHolderDTO(formHolder.getName(), formHolder.getUrl()));
		}
		return result;
	}

	static VeeraCategoryDTO convert(final VeeraCategoryImpl category) {
		return copy(category, new VeeraCategoryDTO());
	}

	static VeeraCategoryImpl convert(final VeeraCategoryDTO categoryDTO) {
		return copy(categoryDTO, new VeeraCategoryImpl());
	}

	static VeeraFormDTO convert(final VeeraFormImpl form) {
		return copy(form, new VeeraFormDTO());
	}

	static VeeraFormImpl convert(final VeeraFormDTO form) {
		return copy(form, new VeeraFormImpl());
	}

	private static <T extends VeeraForm> T copy(final VeeraForm source, final T dest) {
		if (dest == null) {
			return null;
		}
		dest.setCompanyId(source.getCompanyId());
		dest.setDescription(source.getDescription());
		dest.setEntryId(source.getEntryId());
		dest.setFolderId(source.getFolderId());
		dest.setHelpContent(source.getHelpContent());
		dest.setIdentity(source.getIdentity());
		dest.setIdentity2(source.getIdentity2());
		dest.setType(source.getType());
		return dest;
	}

	private static <T extends VeeraCategory> T copy(final VeeraCategory source, final T dest) {
		if (dest == null) {
			return null;
		}
		dest.setCompanyId(source.getCompanyId());
		dest.setDescription(source.getDescription());
		dest.setEntryId(source.getEntryId());
		dest.setFormCount(source.getFormCount());
		dest.setHelpContent(source.getHelpContent());
		dest.setName(source.getName());
		dest.setParent(source.getParent());
		return dest;
	}
	
}
