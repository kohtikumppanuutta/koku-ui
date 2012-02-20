package fi.arcusys.koku.kv.model;


public class KokuAnswer {
	
	private String answer;
	private String comment;
	private int questionNumber;
	
	
	public KokuAnswer(fi.arcusys.koku.kv.requestservice.Answer answer) {
		this.answer = answer.getAnswer();
		this.comment = answer.getComment();
		this.questionNumber = answer.getQuestionNumber();
	}	
	
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getQuestionNumber() {
		return questionNumber;
	}
	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}
	
	@Override
	public String toString() {
		return "KokuAnswer [answer=" + answer + ", comment=" + comment
				+ ", questionNumber=" + questionNumber + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answer == null) ? 0 : answer.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + questionNumber;
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
		KokuAnswer other = (KokuAnswer) obj;
		if (answer == null) {
			if (other.answer != null) {
				return false;
			}
		} else if (!answer.equals(other.answer)) {
			return false;
		}
		if (comment == null) {
			if (other.comment != null) {
				return false;
			}
		} else if (!comment.equals(other.comment)) {
			return false;
		}
		if (questionNumber != other.questionNumber) {
			return false;
		}
		return true;
	}
	
	
}
