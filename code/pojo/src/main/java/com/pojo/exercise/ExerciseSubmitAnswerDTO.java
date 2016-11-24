package com.pojo.exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生提交答案DTO
 * 
 * @author fourer
 *
 */
public class ExerciseSubmitAnswerDTO {

	private String docId;
	private List<ExerciseSubmitAnswerDTO.Answer> answerList = new ArrayList<ExerciseSubmitAnswerDTO.Answer>();

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public List<ExerciseSubmitAnswerDTO.Answer> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(
			List<ExerciseSubmitAnswerDTO.Answer> answerList) {
		this.answerList = answerList;
	}

	public static class Answer {
		private String titleId;
		private String answer;

		public String getTitleId() {
			return titleId;
		}

		public void setTitleId(String titleId) {
			this.titleId = titleId;
		}

		public String getAnswer() {
			return answer;
		}

		public void setAnswer(String answer) {
			this.answer = answer;
		}

	

	}
}
