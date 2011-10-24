package fi.arcusys.koku.hak.model;

public class KokuApplicationSummary {
	
	
	private String kindergartenName;
	private String applicantAccepted;
	private String placeAccepted;
	
	private String applicantName;
	private String applicantGuardianName;
	
	private String createdAt;	// Hakemuksen luontipvm
	private String answeredAt;	// Palvelupäätös
	private String inEffectAt;	// Hoitotarpeen alkamispäivä
	
	private Integer applicationId;
	
	
	
	public String getKindergartenName() {
		return kindergartenName;
	}
	public void setKindergartenName(String kindergartenName) {
		this.kindergartenName = kindergartenName;
	}

	public String getApplicantName() {
		return applicantName;
	}
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
	public String getApplicantGuardianName() {
		return applicantGuardianName;
	}
	public void setApplicantGuardianName(String applicantGuardianName) {
		this.applicantGuardianName = applicantGuardianName;
	}

	public String getApplicantAccepted() {
		return applicantAccepted;
	}
	public void setApplicantAccepted(String applicantAccepted) {
		this.applicantAccepted = applicantAccepted;
	}
	public String getPlaceAccepted() {
		return placeAccepted;
	}
	public void setPlaceAccepted(String placeAccepted) {
		this.placeAccepted = placeAccepted;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getAnsweredAt() {
		return answeredAt;
	}
	public void setAnsweredAt(String answeredAt) {
		this.answeredAt = answeredAt;
	}
	public String getInEffectAt() {
		return inEffectAt;
	}
	public void setInEffectAt(String inEffectAt) {
		this.inEffectAt = inEffectAt;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	
	

	
}
