package fi.arcusys.koku.kv.model;

import java.util.ArrayList;
import java.util.List;

public class KokuResponse {
	
	private List<KokuAnswer> answers;
	private String name;
	
	
	public KokuResponse() {
		
	}
	
	public KokuResponse(fi.arcusys.koku.kv.requestservice.Response response) {
		if (response == null) {
			return;
		}
		name = response.getName();
		answers = new ArrayList<KokuAnswer>();
		if (response.getAnswers() != null) {
			for (fi.arcusys.koku.kv.requestservice.Answer answer : response.getAnswers()) {
				answers.add(new KokuAnswer(answer));
			}
		}
	}

	public List<KokuAnswer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<KokuAnswer> answers) {
		this.answers = answers;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "KokuResponse [answers=" + answers + ", name=" + name + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answers == null) ? 0 : answers.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		KokuResponse other = (KokuResponse) obj;
		if (answers == null) {
			if (other.answers != null)
				return false;
		} else if (!answers.equals(other.answers)) {
			return false;
		}
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
}
