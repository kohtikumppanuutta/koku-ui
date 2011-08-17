package fi.arcusys.koku.palvelut.services;

import java.util.List;

import fi.arcusys.koku.palvelut.model.client.FormHolder;
import fi.arcusys.koku.palvelut.model.client.VeeraCategoryImpl;
import fi.arcusys.koku.palvelut.model.client.VeeraFormImpl;

public interface VeeraServicesFacade {
	
	/**
	 * Category service methods 
	 */
	
	VeeraCategoryImpl findCategoryByEntryAndCompanyId(final Integer entryId, final long companyId);
	
	List<VeeraCategoryImpl> findChildCategories(final Integer parent, final long companyId);
	
	int removeCategoryByEntryAndCompanyId(final Integer entryId, final long companyId);
	
	List<VeeraCategoryImpl> findAllCategoriesByCompanyId(final long companyId);

	void createCategory(final VeeraCategoryImpl newCategory);

	void updateCategory(final VeeraCategoryImpl category);

	/**
	 * Form service methods 
	 */
	VeeraFormImpl findFormByEntryId(final Integer entryId);
	
	List<VeeraFormImpl> findChildForms(final Integer folderId);
	
	int removeFormByEntryId(final Integer entryId);
	
	List<VeeraFormImpl> findFormsByDescription(final String description, final int maxResults);
	
	List<VeeraFormImpl> findChildFormsByFormHolders(final Integer parent, final long companyId, final List<FormHolder> holders);
	
	List<?> findChildFormsCount(final Integer categoryId, final long companyId);

	void createForm(final VeeraFormImpl newForm);

	void updateForm(final VeeraFormImpl form);
}
