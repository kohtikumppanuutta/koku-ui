package fi.arcusys.koku.palvelut.model.client;

import java.io.Serializable;

import org.bouncycastle.util.encoders.Base64;

public class VeeraFormImpl implements Serializable, VeeraForm {

	private static final long serialVersionUID = 1L;
	private Integer entryId;
	private Integer folderId;
	private Integer type;
	private String identity;
	private String identity2;
	private String description;
	private String helpContent;
	private Long companyId;
	
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
	public Integer getFolderId() {
		return folderId;
	}

	/**
	 * @param folderId
	 */
	public void setFolderId(Integer folderId) {
		this.folderId = folderId;
	}

	/**
	 * @return
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * @param identity
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	/**
	 * @return
	 */
	public String getIdentity2() {
		return identity2;
	}

	/**
	 * @param identity2
	 */
	public void setIdentity2(String identity2) {
		this.identity2 = identity2;
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
	public String getIdentity_64() {
		return identityFrom64(this.identity);
	}

	/**
	 * @return
	 */
	public String getIdentity2_64() {
		return identityFrom64(this.identity2);
	}

	public static String identityFrom64(final String id) {
		return new String(Base64.decode(id.getBytes()));
	}

	public static String identityTo64(String id) {
		return new String(Base64.encode(id.getBytes()));
	}
	
	/**
	 * @return
	 */
	@Override
	public String toString() {
		return "VeeraForm [companyId=" + companyId + ",\\n description="
		+ description + ",\\n entryId=" + entryId + ",\\n folderId="
		+ folderId + ",\\n helpContent=" + helpContent
		+ ",\\n identity=" + identity + ",\\n identity2=" + identity2
		+ ",\\n type=" + type + "]";
	}
}