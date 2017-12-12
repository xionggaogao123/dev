package com.fulaan.questionbook.dto;

import com.pojo.questionbook.QuestionWebSizeEntry;
import com.sys.constants.Constant;

/**
 * Created by James on 2017/12/12.
 */
public class QuestionWebSizeDTO {

    /*ObjectId questionId,
            int questionHeight,
            int answerHeight*/
    private String questionId;
    private int questionHeight;
    private int answerHeight;
    public QuestionWebSizeDTO(){

    }
    public QuestionWebSizeDTO(QuestionWebSizeEntry entry) {
        this.questionHeight = entry.getQuestionHeight();
        this.answerHeight = entry.getAnswerHeight();
        this.questionId = null!=entry.getQuestionId()?entry.getQuestionId().toString(): Constant.EMPTY;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public int getQuestionHeight() {
        return questionHeight;
    }

    public void setQuestionHeight(int questionHeight) {
        this.questionHeight = questionHeight;
    }

    public int getAnswerHeight() {
        return answerHeight;
    }

    public void setAnswerHeight(int answerHeight) {
        this.answerHeight = answerHeight;
    }
}
