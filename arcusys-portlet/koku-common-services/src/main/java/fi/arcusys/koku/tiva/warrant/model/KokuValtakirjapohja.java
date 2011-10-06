package fi.arcusys.koku.tiva.warrant.model;

public class KokuValtakirjapohja {

    private long templateId;
    private String templateName;
    private String description;
    
    
	public KokuValtakirjapohja(fi.arcusys.koku.tiva.warrant.employeewarrantservice.Valtakirjapohja template) {
		setTemplateId(template.getTemplateId());
		setTemplateName(template.getTemplateName());
		setDescription(template.getDescription());
	}
	
	public KokuValtakirjapohja(fi.arcusys.koku.tiva.warrant.citizenwarrantservice.Valtakirjapohja template) {
		setTemplateId(template.getTemplateId());
		setTemplateName(template.getTemplateName());
		setDescription(template.getDescription());
	}
	
	public long getTemplateId() {
		return templateId;
	}
	
	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}
	
	public String getTemplateName() {
		return templateName;
	}
	
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "Valtakirjapohja [templateId=" + templateId + ", templateName="
				+ templateName + ", description=" + description + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (templateId ^ (templateId >>> 32));
		result = prime * result
				+ ((templateName == null) ? 0 : templateName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KokuValtakirjapohja other = (KokuValtakirjapohja) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (templateId != other.templateId)
			return false;
		if (templateName == null) {
			if (other.templateName != null)
				return false;
		} else if (!templateName.equals(other.templateName))
			return false;
		return true;
	}
    
   
}

