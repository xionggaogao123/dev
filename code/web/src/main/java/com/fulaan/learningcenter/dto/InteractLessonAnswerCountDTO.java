package com.fulaan.learningcenter.dto;

/**
 * Created by guojing on 2015/12/1.
 */
public class InteractLessonAnswerCountDTO {

    private int answer;
    private String answerDes;
    private String rate;
    private int count;

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getAnswerDes() {
        return answerDes;
    }

    public void setAnswerDes(String answerDes) {
        this.answerDes = answerDes;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
