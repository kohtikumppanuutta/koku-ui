package fi.arcusys.koku.palvelut.services;

import static fi.arcusys.koku.palvelut.services.WsClientUtils.convert;
import static fi.arcusys.koku.palvelut.services.WsClientUtils.convertCategories;
import static fi.arcusys.koku.palvelut.services.WsClientUtils.convertFormHolders;
import static fi.arcusys.koku.palvelut.services.WsClientUtils.convertForms;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import fi.arcusys.koku.palvelut.model.client.FormHolder;
import fi.arcusys.koku.palvelut.model.client.VeeraCategoryImpl;
import fi.arcusys.koku.palvelut.model.client.VeeraFormImpl;
import fi.arcusys.koku.palvelut.ws.client.VeeraServicesFacadeWS;
import fi.arcusys.koku.palvelut.ws.client.VeeraServicesWsClient;

public class VeeraServicesFacadeImpl implements VeeraServicesFacade {
	private String wsdlLocation;
	private VeeraServicesWsClient servicesClient;

	/**
	 * @param wsdlLocation the wsdlLocation to set
	 */
	public void setWsdlLocation(String wsdlLocation) {
		this.wsdlLocation = wsdlLocation;
	}

	public void init() {
		try {
			if (wsdlLocation != null) {
				this.servicesClient = new VeeraServicesWsClient(new URL(wsdlLocation));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} finally {
			if (this.servicesClient == null) {
				this.servicesClient = new VeeraServicesWsClient();
			}
		}
	}
	
	@Override
	public void createCategory(VeeraCategoryImpl newCategory) {
		getServicePort().createCategory(convert(newCategory));
	}

	@Override
	public void createForm(VeeraFormImpl newForm) {
		getServicePort().createForm(convert(newForm));
	}

	@Override
	public List<VeeraCategoryImpl> findAllCategoriesByCompanyId(long companyId) {
		return convertCategories(getServicePort().findAllCategoriesByCompanyId(companyId));
	}

	@Override
	public VeeraCategoryImpl findCategoryByEntryAndCompanyId(Integer entryId, long companyId) {
		return convert(getServicePort().findCategoryByEntryAndCompanyId(entryId, companyId));
	}

	@Override
	public List<VeeraCategoryImpl> findChildCategories(Integer parent, long companyId) {
		return convertCategories(getServicePort().findChildCategories(parent, companyId));
	}

	@Override
	public List<VeeraFormImpl> findChildForms(Integer folderId) {
		return convertForms(getServicePort().findChildForms(folderId));
	}

	@Override
	public List<VeeraFormImpl> findChildFormsByFormHolders(Integer parent, long companyId, List<FormHolder> holders) {
		return convertForms(getServicePort().findChildFormsByFormHolders(parent, companyId, convertFormHolders(holders)));
	}

	@Override
	public List<?> findChildFormsCount(Integer categoryId, long companyId) {
		return getServicePort().findChildFormsCount(categoryId, companyId);
	}

	@Override
	public VeeraFormImpl findFormByEntryId(Integer entryId) {
		return convert(getServicePort().findFormByEntryId(entryId));
	}

	@Override
	public List<VeeraFormImpl> findFormsByDescription(String description, int maxResults) {
		return convertForms(getServicePort().findFormsByDescription(description, maxResults));
	}

	@Override
	public int removeCategoryByEntryAndCompanyId(Integer entryId, long companyId) {
		return getServicePort().removeCategoryByEntryAndCompanyId(entryId, companyId);
	}

	@Override
	public int removeFormByEntryId(Integer entryId) {
		return getServicePort().removeFormByEntryId(entryId);
	}

	@Override
	public void updateCategory(VeeraCategoryImpl category) {
		getServicePort().updateCategory(convert(category));
	}

	@Override
	public void updateForm(VeeraFormImpl form) {
		getServicePort().updateForm(convert(form));
	}

	private VeeraServicesFacadeWS getServicePort() {
		return servicesClient.getVeeraServicesFacadePort();
	}
	
}
