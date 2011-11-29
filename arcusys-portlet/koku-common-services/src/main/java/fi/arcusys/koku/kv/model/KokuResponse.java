package fi.arcusys.koku.kv.model;

import java.util.ArrayList;
import java.util.List;

public class KokuResponse {
	
	private List<KokuAnswer> answers;
	private String name;
	private String comment;
	
	
	public KokuResponse() {
		
	}
	
	public KokuResponse(fi.arcusys.koku.kv.requestservice.Response response) {
		if (response == null) {
			return;
		}
		name = response.getName();
		comment = response.getComment();
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
	public final String getComment() {
		return comment;
	}
	public final void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "KokuResponse [answers=" + answers + ", name=" + name
				+ ", comment=" + comment + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answers == null) ? 0 : answers.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
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
			if (other.answers != null) {
				return false;
			}
		} else if (!answers.equals(other.answers)) {
			return false;
		}
		if (comment == null) {
			if (other.comment != null) {
				return false;
			}
		} else if (!comment.equals(other.comment)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	
}
