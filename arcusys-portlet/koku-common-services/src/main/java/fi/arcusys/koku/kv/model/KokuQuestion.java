package fi.arcusys.koku.kv.model;

import fi.arcusys.koku.kv.requestservice.QuestionType;


public class KokuQuestion {
	
    private String description;
    private int number;
    private QuestionType type;
    private KokuAnswer answer;

	public KokuQuestion(fi.arcusys.koku.kv.requestservice.Question question) {
		description = question.getDescription();
		number = question.getNumber();
		type = question.getType();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public KokuAnswer getAnswer() {
		return answer;
	}

	public void setAnswer(KokuAnswer answer) {
		this.answer = answer;
	}

	@Override
	public String toString() {
		return "KokuQuestion [description=" + description + ", number="
				+ number + ", type=" + type + ", answer=" + answer + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answer == null) ? 0 : answer.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + number;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		KokuQuestion other = (KokuQuestion) obj;
		if (answer == null) {
			if (other.answer != null)
				return false;
		} else if (!answer.equals(other.answer))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (number != other.number)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
}
