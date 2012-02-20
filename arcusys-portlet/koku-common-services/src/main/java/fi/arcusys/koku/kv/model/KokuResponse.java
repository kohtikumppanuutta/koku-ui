package fi.arcusys.koku.kv.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fi.arcusys.koku.users.KokuUser;

public class KokuResponse {
	
	private List<KokuAnswer> answers;
	private String name;
	private KokuUser replierUser;
	private String comment;
	
	public KokuResponse() {
		
	}
	
	public KokuResponse(fi.arcusys.koku.kv.requestservice.Response response) {
		if (response == null) {
			return;
		}
		name = response.getName();
		replierUser = new KokuUser(response.getReplierUserInfo());
		comment = response.getComment();
		answers = new ArrayList<KokuAnswer>();
		if (response.getAnswers() != null) {
			for (fi.arcusys.koku.kv.requestservice.Answer answer : response.getAnswers()) {
				answers.add(new KokuAnswer(answer));
			}
		}
		
		Collections.sort(answers, new Comparator<KokuAnswer>() {
			@Override
			public int compare(KokuAnswer o1, KokuAnswer o2) {
				if (o1.getQuestionNumber() > o2.getQuestionNumber()) {
					return 1;
				} else if (o1.getQuestionNumber() == o2.getQuestionNumber()) {
					return 0;
				} else {
					return -1;
				}
			}				
		});
	}
	
	/**
	 * @return the replierUser
	 */
	public final KokuUser getReplierUser() {
		return replierUser;
	}

	/**
	 * @param replierUser the replierUser to set
	 */
	public final void setReplierUser(KokuUser replierUser) {
		this.replierUser = replierUser;
	}

	public List<KokuAnswer> getAnswers() {
		if (answers == null) {
			answers = new ArrayList<KokuAnswer>();
		}
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answers == null) ? 0 : answers.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((replierUser == null) ? 0 : replierUser.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		if (replierUser == null) {
			if (other.replierUser != null) {
				return false;
			}
		} else if (!replierUser.equals(other.replierUser)) {
			return false;
		}
		return true;
	}
	
}
