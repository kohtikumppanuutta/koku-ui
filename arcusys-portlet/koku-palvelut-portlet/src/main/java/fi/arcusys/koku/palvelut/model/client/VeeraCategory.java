package fi.arcusys.koku.palvelut.model.client;

public interface VeeraCategory {

	/**
	 * @return
	 */
	Integer getEntryId();

	/**
	 * @param entryId
	 */
	void setEntryId(Integer entryId);

	/**
	 * @return
	 */
	Integer getParent();

	/**
	 * @param parent
	 */
	void setParent(Integer parent);

	/**
	 * @return
	 */
	String getName();

	/**
	 * @param name
	 */
	void setName(String name);

	/**
	 * @return
	 */
	String getDescription();

	/**
	 * @param description
	 */
	void setDescription(String description);

	/**
	 * @return
	 */
	String getHelpContent();

	/**
	 * @param helpContent
	 */
	void setHelpContent(String helpContent);

	/**
	 * @return
	 */
	Long getCompanyId();

	/**
	 * @param companyId
	 */
	void setCompanyId(Long companyId);

	/**
	 * @return
	 */
	int getFormCount();

	/**
	 * @param formCount
	 */
	void setFormCount(int formCount);

}