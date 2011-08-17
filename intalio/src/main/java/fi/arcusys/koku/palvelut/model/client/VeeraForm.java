package fi.arcusys.koku.palvelut.model.client;

public interface VeeraForm {

	Integer getEntryId();

	void setEntryId(Integer entryId);

	Integer getFolderId();

	void setFolderId(Integer folderId);

	Integer getType();

	void setType(Integer type);

	String getIdentity();

	void setIdentity(String identity);

	String getIdentity2();

	void setIdentity2(String identity2);

	String getDescription();

	void setDescription(String description);

	String getHelpContent();

	void setHelpContent(String helpContent);

	Long getCompanyId();

	void setCompanyId(Long companyId);

	String toString();

}