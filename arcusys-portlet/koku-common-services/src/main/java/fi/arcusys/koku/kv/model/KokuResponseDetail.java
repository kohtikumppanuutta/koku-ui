package fi.arcusys.koku.kv.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fi.arcusys.koku.kv.requestservice.Answer;
import fi.arcusys.koku.kv.requestservice.Question;
import fi.arcusys.koku.kv.requestservice.ResponseDetail;

public class KokuResponseDetail extends KokuResponseSummary {
	
	private static final Comparator<KokuQuestion> SORT_QUESTIONS_BY_QUESTION_NUMBER = new Comparator<KokuQuestion>() {
			@Override
			public int compare(KokuQuestion o1, KokuQuestion o2) {
				if (o1.getNumber() > o2.getNumber()) {
					return 1;
				} else if (o1.getNumber() == o2.getNumber()) {
					return 0;
				} else {
					return -1;
				}
			}				
		};
		
	public static final Comparator<KokuAnswer> SORT_ANSWERS_BY_QUESTION_NUMBER = new Comparator<KokuAnswer>() {
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
		};

	private List<KokuAnswer> answers;
	private List<KokuQuestion> questions;
	private String comment;
	
	public KokuResponseDetail(ResponseDetail req) {
		super(req);
		if (req != null) {
			this.comment = req.getComment();
			if (req.getQuestions() != null) {
				questions = new ArrayList<KokuQuestion>();
				for (Question question : req.getQuestions()) {
					questions.add(new KokuQuestion(question));
				}
			}			
			
			if (req.getAnswers() != null) {
				answers = new ArrayList<KokuAnswer>();
				for (Answer answer : req.getAnswers()) {
					answers.add(new KokuAnswer(answer));					
				}
			}
			
			Collections.sort(questions,SORT_QUESTIONS_BY_QUESTION_NUMBER);			
			Collections.sort(answers, SORT_ANSWERS_BY_QUESTION_NUMBER );
			
			for (int i=0; i < questions.size(); i++) {
				questions.get(i).setAnswer(answers.get(i));
			}
		}
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

	public List<KokuQuestion> getQuestions() {
		if (questions == null) {
			questions = new ArrayList<KokuQuestion>();
		}
		return questions;
	}

	public void setQuestions(List<KokuQuestion> questions) {
		this.questions = questions;
	}
	
	public final String getComment() {
		return comment;
	}

	public final void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "KokuResponseDetail [answers=" + answers + ", questions="
				+ questions + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((answers == null) ? 0 : answers.hashCode());
		result = prime * result
				+ ((questions == null) ? 0 : questions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		KokuResponseDetail other = (KokuResponseDetail) obj;
		if (answers == null) {
			if (other.answers != null) {
				return false;
			}
		} else if (!answers.equals(other.answers)) {
			return false;
		}
		if (questions == null) {
			if (other.questions != null) {
				return false;
			}
		} else if (!questions.equals(other.questions)) {
			return false;
		}
		return true;
	}

}
