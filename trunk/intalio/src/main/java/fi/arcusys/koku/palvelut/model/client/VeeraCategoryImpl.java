package fi.arcusys.koku.palvelut.model.client;

import java.io.Serializable;

public class VeeraCategoryImpl implements Serializable, VeeraCategory {
	
	private static final long serialVersionUID = 1L;
	private Integer entryId;
	private Integer parent;
	private String name;
	private String description;
	private String helpContent;
	private Long companyId;
	private int formCount;
	
	/**
	 * @return
	 */
	public Integer getEntryId() {
		return entryId;
	}

	/**
	 * @param entryId
	 */
	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}

	/**
	 * @return
	 */
	public Integer getParent() {
		return parent;
	}

	/**
	 * @param parent
	 */
	public void setParent(Integer parent) {
		this.parent = parent;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return
	 */
	public String getHelpContent() {
		return helpContent;
	}

	/**
	 * @param helpContent
	 */
	public void setHelpContent(String helpContent) {
		this.helpContent = helpContent;
	}

	/**
	 * @return
	 */
	public Long getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return
	 */
	public int getFormCount() {
		return formCount;
	}

	/**
	 * @param formCount
	 */
	public void setFormCount(int formCount) {
		this.formCount = formCount;
	}
}